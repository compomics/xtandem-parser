package de.proteinms.xtandemparser.parser;

import com.compomics.util.Util;
import com.compomics.util.experiment.biology.AminoAcid;
import com.compomics.util.experiment.biology.AminoAcidSequence;
import com.compomics.util.experiment.identification.Advocate;
import com.compomics.util.experiment.identification.PeptideAssumption;
import com.compomics.util.experiment.identification.SequenceFactory;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import javax.xml.bind.JAXBException;
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
     * A map of the peptides found in this file.
     */
    private HashMap<String, LinkedList<com.compomics.util.experiment.biology.Peptide>> foundPeptidesMap;
    /**
     * The length of the keys of the peptide map.
     */
    private int peptideMapKeyLength;

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
    public LinkedList<SpectrumMatch> getAllSpectrumMatches(WaitingHandler waitingHandler) throws IOException, IllegalArgumentException, SQLException, ClassNotFoundException, InterruptedException, JAXBException {
        return getAllSpectrumMatches(waitingHandler, null, true);
    }

    @Override
    public LinkedList<SpectrumMatch> getAllSpectrumMatches(WaitingHandler waitingHandler, SequenceMatchingPreferences sequenceMatchingPreferences, boolean expandAaCombinations) throws IOException, IllegalArgumentException, SQLException, ClassNotFoundException, InterruptedException, JAXBException {

        if (sequenceMatchingPreferences != null) {
            SequenceFactory sequenceFactory = SequenceFactory.getInstance();
            peptideMapKeyLength = sequenceFactory.getDefaultProteinTree().getInitialTagSize();
            foundPeptidesMap = new HashMap<String, LinkedList<com.compomics.util.experiment.biology.Peptide>>(1024);
        }

        LinkedList<SpectrumMatch> result = new LinkedList<SpectrumMatch>();

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
                        PeptideAssumption peptideAssumption = getPeptideAssumption(domain, charge.value, rank, sequenceMatchingPreferences);
                        com.compomics.util.experiment.biology.Peptide peptide = peptideAssumption.getPeptide();
                        boolean found = false;
                        for (SpectrumIdentificationAssumption loadedAssumption : currentMatch.getAllAssumptions()) {
                            PeptideAssumption tempAssumption = (PeptideAssumption) loadedAssumption;
                            if (tempAssumption.getPeptide().isSameSequenceAndModificationStatus(peptide, SequenceMatchingPreferences.defaultStringMatching)) {
                                if (tempAssumption.getPeptide().sameModificationsAs(peptideAssumption.getPeptide())) {
                                    found = true;
                                }
                            }
                        }
                        if (!found) {
                            rankIncrease++;
                            if (expandAaCombinations && AminoAcidSequence.hasCombination(peptideAssumption.getPeptide().getSequence())) {
                                for (StringBuilder expandedSequence : AminoAcidSequence.getCombinations(peptideAssumption.getPeptide().getSequence())) {
                                    com.compomics.util.experiment.biology.Peptide newPeptide = new com.compomics.util.experiment.biology.Peptide(expandedSequence.toString(), peptide.getModificationMatches());
                                    ArrayList<ModificationMatch> modificationMatches = peptide.getModificationMatches();
                                    for (ModificationMatch modificationMatch : modificationMatches) {
                                        newPeptide.addModificationMatch(new ModificationMatch(modificationMatch.getTheoreticPtm(), modificationMatch.isVariable(), modificationMatch.getModificationSite()));
                                    }
                                    PeptideAssumption newAssumption = new PeptideAssumption(newPeptide, peptideAssumption.getRank(), peptideAssumption.getAdvocate(), peptideAssumption.getIdentificationCharge(), peptideAssumption.getScore(), peptideAssumption.getIdentificationFile());
                                    currentMatch.addHit(Advocate.xtandem.getIndex(), newAssumption, false);
                                }
                            } else {
                                currentMatch.addHit(Advocate.xtandem.getIndex(), peptideAssumption, false);
                            }
                        }
                    }
                    rank += rankIncrease;
                }

                result.add(currentMatch);
            }

            if (waitingHandler != null) {
                if (waitingHandler.isRunCanceled()) {
                    break;
                }
                waitingHandler.increaseSecondaryProgressCounter();
            }
        }

        return result;
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
     * @param sequenceMatchingPreferences the sequence matching preferences to
     * use to fill the secondary maps
     *
     * @return the corresponding peptide assumption
     */
    private PeptideAssumption getPeptideAssumption(Domain domain, int charge, int rank, SequenceMatchingPreferences sequenceMatchingPreferences) {

        String sequence = domain.getDomainSequence();

        ArrayList<ModificationMatch> foundModifications = new ArrayList<ModificationMatch>();
        ArrayList<de.proteinms.xtandemparser.interfaces.Modification> foundVariableModifications = modificationMap.getVariableModifications(domain.getDomainKey());

        // add the variable mods
        for (Modification currentModification : foundVariableModifications) {
            int location = new Integer(currentModification.getLocation()) - domain.getDomainStart() + 1;
            foundModifications.add(new ModificationMatch(currentModification.getName(), true, location));
        }

        com.compomics.util.experiment.biology.Peptide peptide = new com.compomics.util.experiment.biology.Peptide(sequence, foundModifications);

        if (sequenceMatchingPreferences != null) {
            String subSequence = sequence.substring(0, peptideMapKeyLength);
            subSequence = AminoAcid.getMatchingSequence(subSequence, sequenceMatchingPreferences);
            LinkedList<com.compomics.util.experiment.biology.Peptide> peptidesForTag = foundPeptidesMap.get(subSequence);
            if (peptidesForTag == null) {
                peptidesForTag = new LinkedList<com.compomics.util.experiment.biology.Peptide>();
                foundPeptidesMap.put(subSequence, peptidesForTag);
            }
            peptidesForTag.add(peptide);
        }

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

    @Override
    public HashMap<String, LinkedList<com.compomics.util.experiment.biology.Peptide>> getPeptidesMap() {
        return foundPeptidesMap;
    }

    @Override
    public HashMap<String, LinkedList<SpectrumMatch>> getTagsMap() {
        return new HashMap<String, LinkedList<SpectrumMatch>>();
    }

    @Override
    public void clearTagsMap() {
        // No tags here
    }

    @Override
    public void clearPeptidesMap() {
        foundPeptidesMap.clear();
    }
}
