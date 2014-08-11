package de.proteinms.xtandemparser.parser;

import com.compomics.util.Util;
import com.compomics.util.experiment.biology.AminoAcidPattern;
import com.compomics.util.experiment.identification.Advocate;
import com.compomics.util.experiment.identification.PeptideAssumption;
import com.compomics.util.experiment.identification.SpectrumIdentificationAssumption;
import com.compomics.util.experiment.identification.matches.ModificationMatch;
import com.compomics.util.experiment.identification.matches.SpectrumMatch;
import com.compomics.util.experiment.io.identifications.IdfileReader;
import com.compomics.util.experiment.massspectrometry.*;
import com.compomics.util.experiment.personalization.ExperimentObject;
import com.compomics.util.preferences.SequenceMatchingPreferences;
import com.compomics.util.waiting.WaitingHandler;
import de.proteinms.xtandemparser.interfaces.Modification;
import de.proteinms.xtandemparser.xtandem.*;
import java.io.IOException;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import javax.xml.parsers.ParserConfigurationException;

/**
 * This reader will import identifications from an X!Tandem XML result file.
 * <p/>
 * @author Marc Vaudel
 */
public class XTandemIdfileReader extends ExperimentObject implements IdfileReader {

    /**
     * The instance of the X!Tandem parser.
     */
    private XTandemFile xTandemFile = null;
    /**
     * The modification map.
     */
    private ModificationMap modificationMap;
    /**
     * The peptide map.
     */
    private PeptideMap peptideMap;

    /**
     * Constructor for the reader.
     */
    public XTandemIdfileReader() {
    }

    /**
     * Constructor for the reader.
     *
     * @param aFile the inspected file
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public XTandemIdfileReader(File aFile) throws SAXException, ParserConfigurationException {
        xTandemFile = new XTandemFile(aFile.getPath(), true);
        peptideMap = xTandemFile.getPeptideMap();
        modificationMap = xTandemFile.getModificationMap();
    }

    public String getExtension() {
        return "t.xml";
    }

    /**
     * Getter for the file name.
     *
     * @return the file name
     */
    public String getFileName() {
        File tempFile = new File(xTandemFile.getFileName());
        return tempFile.getName();
    }

    @Override
    public HashSet<SpectrumMatch> getAllSpectrumMatches(WaitingHandler waitingHandler) throws IOException, IllegalArgumentException, Exception {

        HashSet<SpectrumMatch> foundPeptides = new HashSet<SpectrumMatch>();

        if (waitingHandler != null) {
            waitingHandler.setMaxSecondaryProgressCounter(xTandemFile.getSpectraNumber());
        }

        HashMap<Integer, String> idToSpectrumTitleMap = xTandemFile.getXTandemParser().getIdToSpectrumMap();

        for (String id : peptideMap.getSpectrumAndPeptideMap().keySet()) {

            Integer spectrumNumber = new Integer(id.substring(1));
            String tempTitle = idToSpectrumTitleMap.get(spectrumNumber);
            String spectrumName = fixMgfTitle(tempTitle);

            // try to remove the retention time as added by xtandem...
            if (spectrumName.indexOf("RTINSECONDS=") != -1) {
                spectrumName = spectrumName.substring(0, spectrumName.indexOf("RTINSECONDS="));
            }

            // remove white space
            spectrumName = spectrumName.trim();

            ArrayList<Peptide> spectrumPeptides = peptideMap.getAllPeptides(spectrumNumber);

            if (spectrumPeptides.size() > 0) {

                String tempFile = xTandemFile.getInputParameters().getSpectrumPath();
                String filename = Util.getFileName(tempFile);
                Integer parsedCharge = new Integer(xTandemFile.getXTandemParser().getRawSpectrumMap().get("z" + spectrumNumber));
                Charge charge = new Charge(Charge.PLUS, parsedCharge);
                String spectrumKey = com.compomics.util.experiment.massspectrometry.Spectrum.getSpectrumKey(filename, spectrumName);
                SpectrumMatch currentMatch = new SpectrumMatch(spectrumKey);
                currentMatch.setSpectrumNumber(spectrumNumber); //@TODO: verify that this work when sorting spectra according to proteins
                HashMap<Double, ArrayList<Domain>> hitMap = new HashMap<Double, ArrayList<Domain>>();

                for (Peptide peptide : spectrumPeptides) {
                    for (Domain domain : peptide.getDomains()) {
                        if (!hitMap.containsKey(domain.getDomainExpect())) {
                            hitMap.put(domain.getDomainExpect(), new ArrayList<Domain>());
                        }
                        hitMap.get(domain.getDomainExpect()).add(domain);
                    }
                }

                ArrayList<Double> eValues = new ArrayList<Double>(hitMap.keySet());
                Collections.sort(eValues);
                int rank = 1;

                for (Double eValue : eValues) {
                    int rankIncrease = 0;
                    for (Domain domain : hitMap.get(eValue)) {
                        PeptideAssumption newAssumption = getPeptideAssumption(domain, charge.value, rank);
                        boolean found = false;
                        for (SpectrumIdentificationAssumption loadedAssumption : currentMatch.getAllAssumptions()) {
                            PeptideAssumption peptideAssumption = (PeptideAssumption) loadedAssumption;
                            if (peptideAssumption.getPeptide().isSameSequenceAndModificationStatus(newAssumption.getPeptide(), SequenceMatchingPreferences.defaultStringMatching)) {
                                if (peptideAssumption.getPeptide().sameModificationsAs(newAssumption.getPeptide())) {
                                    found = true;
                                }
                            }
                        }
                        if (!found) {
                            rankIncrease++;
                            currentMatch.addHit(Advocate.xtandem.getIndex(), newAssumption, false);
                        }
                    }
                    rank += rankIncrease;
                }

                foundPeptides.add(currentMatch);
            }

            if (waitingHandler != null) {
                if (waitingHandler.isRunCanceled()) {
                    break;
                }
                waitingHandler.increaseSecondaryProgressCounter();
            }
        }

        return foundPeptides;
    }

    /**
     * Returns a utilities peptide assumption from an X!Tandem peptide.
     *
     * Warning: the fixed modifications are not implemented and need to be added
     * subsequently. That can be done using the compomics utilities PTMFactory
     * (https://code.google.com/p/compomics-utilities/source/browse/trunk/src/main/java/com/compomics/util/experiment/biology/PTMFactory.java).
     *
     * @param domain the domain of the X!Tandem peptide
     * @param charge the charge of the precursor of the inspected spectrum
     * @param rank the rank of the peptide hit
     * @return the corresponding peptide assumption
     */
    private PeptideAssumption getPeptideAssumption(Domain domain, int charge, int rank) {

        String sequence = domain.getDomainSequence();

        ArrayList<ModificationMatch> foundModifications = new ArrayList<ModificationMatch>();
        ArrayList<de.proteinms.xtandemparser.interfaces.Modification> foundVariableModifications = modificationMap.getVariableModifications(domain.getDomainKey());

        // add the variable mods
        for (Modification currentModification : foundVariableModifications) {
            int location = new Integer(currentModification.getLocation()) - domain.getDomainStart() + 1;
            foundModifications.add(new ModificationMatch(currentModification.getName(), true, location));
        }

        com.compomics.util.experiment.biology.Peptide peptide = new com.compomics.util.experiment.biology.Peptide(sequence, foundModifications);
        return new PeptideAssumption(peptide, rank, Advocate.xtandem.getIndex(), new Charge(Charge.PLUS, charge), domain.getDomainExpect(), getFileName());
    }

    /**
     * Returns the fixed mgf title.
     *
     * @param spectrumTitle
     * @return the fixed mgf title
     */
    private String fixMgfTitle(String spectrumTitle) {

        // a special fix for mgf files with titles containing url encoding, e.g.: %3b instead of ;
        try {
            spectrumTitle = URLDecoder.decode(spectrumTitle, "utf-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("An exception was thrown when trying to decode an mgf tile!");
            e.printStackTrace();
        }


        // a special fix for mgf files with titles containing \\ instead of \
        //spectrumTitle = spectrumTitle.replaceAll("\\\\\\\\", "\\\\");  // @TODO: only needed for omssa???

        return spectrumTitle;
    }

    @Override
    public void close() throws IOException {
        xTandemFile = null;
    }

    @Override
    public HashMap<String, ArrayList<String>> getSoftwareVersions() {
        HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
        ArrayList<String> versions = new ArrayList<String>();
        versions.add(xTandemFile.getPerformParameters().getProcVersion());
        result.put("X!Tandem", versions);
        return result;
    }
}
