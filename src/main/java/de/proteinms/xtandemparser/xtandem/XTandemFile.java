package de.proteinms.xtandemparser.xtandem;

import de.proteinms.xtandemparser.interfaces.Ion;
import de.proteinms.xtandemparser.parser.XTandemParser;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * This class represents the xtandem file object as the starting point which
 * provides all the methods to use the information which are parsed by the
 * Xtandem parser.
 *
 * @author Thilo Muth
 */
public class XTandemFile implements Serializable {

    /**
     * The filename of xtandem xml file.
     */
    private String iFileName = null;
    /**
     * This list contains all the spectra from the xtandem file.
     */
    private ArrayList<Spectrum> iSpectraList = null;
    /**
     * This is an instance of the peptide map object.
     */
    private PeptideMap iPeptideMap = null;
    /**
     * This is an instance of the protein map object.
     */
    private ProteinMap iProteinMap = null;
    /**
     * This is an instance of the modification map object.
     */
    private ModificationMap iModMap = null;
    /**
     * Private variable iXTParser as an instance of XTandemParser.
     */
    private XTandemParser iXTParser = null;
    /**
     * HashMap that gets an id an returns a number.
     */
    private HashMap<String, Integer> iIdToNumberMap;
    /**
     * This is an instance of the InputParams object.
     */
    private InputParams iInputParams = null;
    /**
     * This is an instance of the PerformParams object.
     */
    private PerformParams iPerformParams = null;
    /**
     * This variable holds the number of spectra.
     */
    private int iSpectraNumber = 0;

    /**
     * Constructor of XTandemFile gets a string to an existing path and filename
     * of the xtandem file.
     *
     * @param aXTandemFile the given XTandem file
     * @throws SAXException SAX parsing exception thrown
     * @throws ParserConfigurationException if a ParserConfigurationException
     * occurs
     */
    public XTandemFile(String aXTandemFile) throws SAXException, ParserConfigurationException {
        this(aXTandemFile, false);
    }

    /**
     * Constructor of XTandemFile gets a string to an existing path and filename
     * of the xtandem file.
     *
     * @param aXTandemFile the given XTandem file.
     * @param skipDetails if true only the spectrum identifiers, the peptides
     * sequences, modifications and matches e-values will be loaded. Plus the
     * input and performance parameters.
     * @throws SAXException SAX parsing exception thrown.
     * @throws ParserConfigurationException if a ParserConfigurationException
     * occurs
     */
    public XTandemFile(String aXTandemFile, boolean skipDetails) throws SAXException, ParserConfigurationException {
        try {
            File inputFile = new File(aXTandemFile);
            if (!inputFile.exists()) {
                throw new IllegalArgumentException("XTandem xml-file " + aXTandemFile + " doesn't exist.");
            }
            iXTParser = new XTandemParser(inputFile, skipDetails);
            setFileName(aXTandemFile);
            if (!skipDetails) {
                iSpectraList = getSpectraList();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the input parameters.
     *
     * @return the input parameters
     */
    public InputParams getInputParameters() {
        if (iInputParams == null) {
            iInputParams = new InputParams(iXTParser.getInputParamMap());
        }
        return iInputParams;
    }

    /**
     * Returns the perform parameters.
     *
     * @return the perform parameters
     */
    public PerformParams getPerformParameters() {
        if (iPerformParams == null) {
            iPerformParams = new PerformParams(iXTParser.getPerformParamMap());
        }
        return iPerformParams;
    }

    /**
     * This method returns a hash map for the masses.
     *
     * @return map The masses hashmap
     */
    public static HashMap getMassesMap() {
        HashMap<String, Double> map = new HashMap<String, Double>();
        map.put("A", Masses.A);
        map.put("B", Masses.B);
        map.put("C", Masses.C);
        map.put("D", Masses.D);
        map.put("E", Masses.E);
        map.put("F", Masses.F);
        map.put("G", Masses.G);
        map.put("H", Masses.H);
        map.put("I", Masses.I);
        map.put("J", Masses.J);
        map.put("K", Masses.K);
        map.put("L", Masses.L);
        map.put("M", Masses.M);
        map.put("N", Masses.N);
        map.put("O", Masses.O);
        map.put("P", Masses.P);
        map.put("Q", Masses.Q);
        map.put("R", Masses.R);
        map.put("S", Masses.S);
        map.put("T", Masses.T);
        map.put("U", Masses.U);
        map.put("V", Masses.V);
        map.put("W", Masses.W);
        map.put("X", Masses.X);
        map.put("Y", Masses.Y);
        map.put("Z", Masses.Z);
        map.put("Hydrogen", Masses.Hydrogen);
        map.put("Carbon", Masses.Carbon);
        map.put("Nitrogen", Masses.Nitrogen);
        map.put("Oxygen", Masses.Oxygen);
        map.put("Electron", Masses.Electron);
        map.put("C_term", Masses.C_term);
        map.put("N_term", Masses.N_term);
        return map;
    }

    /**
     * This method returns the modification hash map.
     *
     * @return iModMap ModificationMap
     */
    public ModificationMap getModificationMap() {
        if (iModMap == null) {
            iModMap = new ModificationMap(iXTParser.getRawModMap(), this.getPeptideMap(), this.getInputParameters(), iXTParser.getNumberOfSpectra());
        }
        return iModMap;
    }

    /**
     * Returns a vector with two arrays of b ions and y ions respectively.
     *
     * @param peptide The given peptide
     * @param domain The domain
     * @param fragmentIonAccuracy the fragment ion annotation accuracy
     * @return Vector The vector containing b ions and y ions
     */
    public Vector getFragmentIonsForPeptide(Peptide peptide, Domain domain, double fragmentIonAccuracy) {
        Vector<Ion[]> fragIons = new Vector();
        int charge = getSpectrum(peptide.getSpectrumNumber()).getPrecursorCharge();
        // Get an instance of the InSilicoDigester
        InSilicoDigester digester = new InSilicoDigester(peptide, domain, this.getModificationMap(), getMassesMap(), charge, fragmentIonAccuracy);

        // The vector should contain two arrays: b ions & y ions
        SupportData supData = this.getSupportData(peptide.getSpectrumNumber());

        ArrayList<Double> mzList = supData.getXValuesFragIonMass2Charge();
        ArrayList<Double> intList = supData.getYValuesFragIonMass2Charge();
        SpectrumPeak[] peaks = new SpectrumPeak[mzList.size()];
        for (int i = 0; i < mzList.size(); i++) {
            peaks[i] = new SpectrumPeak();
            peaks[i].setMz(mzList.get(i));
            peaks[i].setIntensity(intList.get(i));
        }

        // Match MH peaks
        Vector<FragmentIon> matchedMHIons = digester.getMatchedIons(FragmentIon.MH_ION, peaks);
        FragmentIon[] matchMHIons = new FragmentIon[matchedMHIons.size()];
        for (int i = 0; i < matchedMHIons.size(); i++) {
            matchMHIons[i] = matchedMHIons.get(i);
        }
        Vector<FragmentIon> matchedMHNH3Ions = digester.getMatchedIons(FragmentIon.MHNH3_ION, peaks);
        FragmentIon[] matchMHNH3Ions = new FragmentIon[matchedMHNH3Ions.size()];
        for (int i = 0; i < matchedMHNH3Ions.size(); i++) {
            matchMHNH3Ions[i] = matchedMHNH3Ions.get(i);
        }
        Vector<FragmentIon> matchedMHH2OIons = digester.getMatchedIons(FragmentIon.MHH2O_ION, peaks);
        FragmentIon[] matchMHH2OIons = new FragmentIon[matchedMHH2OIons.size()];
        for (int i = 0; i < matchedMHH2OIons.size(); i++) {
            matchMHH2OIons[i] = matchedMHH2OIons.get(i);
        }

        // Match the a ions
        Vector<FragmentIon> matchedAIons = digester.getMatchedIons(FragmentIon.A_ION, peaks);
        FragmentIon[] matchAIons = new FragmentIon[matchedAIons.size()];
        for (int i = 0; i < matchedAIons.size(); i++) {
            matchAIons[i] = matchedAIons.get(i);
        }
        Vector<FragmentIon> matchedAH2OIons = digester.getMatchedIons(FragmentIon.AH2O_ION, peaks);
        FragmentIon[] matchAH2OIons = new FragmentIon[matchedAH2OIons.size()];
        for (int i = 0; i < matchedAH2OIons.size(); i++) {
            matchAH2OIons[i] = matchedAH2OIons.get(i);
        }
        Vector<FragmentIon> matchedANH3Ions = digester.getMatchedIons(FragmentIon.ANH3_ION, peaks);
        FragmentIon[] matchANH3Ions = new FragmentIon[matchedANH3Ions.size()];
        for (int i = 0; i < matchedANH3Ions.size(); i++) {
            matchANH3Ions[i] = matchedANH3Ions.get(i);
        }

        // Match the b ions
        Vector<FragmentIon> matchedBIons = digester.getMatchedIons(FragmentIon.B_ION, peaks);
        FragmentIon[] matchBIons = new FragmentIon[matchedBIons.size()];
        for (int i = 0; i < matchedBIons.size(); i++) {
            matchBIons[i] = matchedBIons.get(i);
        }
        Vector<FragmentIon> matchedBH2OIons = digester.getMatchedIons(FragmentIon.BH2O_ION, peaks);
        FragmentIon[] matchBH2OIons = new FragmentIon[matchedBH2OIons.size()];
        for (int i = 0; i < matchedBH2OIons.size(); i++) {
            matchBH2OIons[i] = matchedBH2OIons.get(i);
        }
        Vector<FragmentIon> matchedBNH3Ions = digester.getMatchedIons(FragmentIon.BNH3_ION, peaks);
        FragmentIon[] matchBNH3Ions = new FragmentIon[matchedBNH3Ions.size()];
        for (int i = 0; i < matchedBNH3Ions.size(); i++) {
            matchBNH3Ions[i] = matchedBNH3Ions.get(i);
        }

        // Match the c ions
        Vector<FragmentIon> matchedCIons = digester.getMatchedIons(FragmentIon.C_ION, peaks);
        FragmentIon[] matchCIons = new FragmentIon[matchedCIons.size()];
        for (int i = 0; i < matchedCIons.size(); i++) {
            matchCIons[i] = matchedCIons.get(i);
        }

        // Match the x ions
        Vector<FragmentIon> matchedXIons = digester.getMatchedIons(FragmentIon.X_ION, peaks);
        FragmentIon[] matchXIons = new FragmentIon[matchedXIons.size()];
        for (int i = 0; i < matchedXIons.size(); i++) {
            matchXIons[i] = matchedXIons.get(i);
        }

        // Match the Y ions
        Vector<FragmentIon> matchedYIons = digester.getMatchedIons(FragmentIon.Y_ION, peaks);
        FragmentIon[] matchYIons = new FragmentIon[matchedYIons.size()];
        for (int i = 0; i < matchedYIons.size(); i++) {
            matchYIons[i] = matchedYIons.get(i);
        }
        Vector<FragmentIon> matchedYH2OIons = digester.getMatchedIons(FragmentIon.YH2O_ION, peaks);
        FragmentIon[] matchYH2OIons = new FragmentIon[matchedYH2OIons.size()];
        for (int i = 0; i < matchedYH2OIons.size(); i++) {
            matchYH2OIons[i] = matchedYH2OIons.get(i);
        }
        Vector<FragmentIon> matchedYNH3Ions = digester.getMatchedIons(FragmentIon.YNH3_ION, peaks);
        FragmentIon[] matchYNH3Ions = new FragmentIon[matchedYNH3Ions.size()];
        for (int i = 0; i < matchedYNH3Ions.size(); i++) {
            matchYNH3Ions[i] = matchedYNH3Ions.get(i);
        }

        // Match the z ions
        Vector<FragmentIon> matchedZIons = digester.getMatchedIons(FragmentIon.Z_ION, peaks);
        FragmentIon[] matchZIons = new FragmentIon[matchedZIons.size()];
        for (int i = 0; i < matchedZIons.size(); i++) {
            matchZIons[i] = matchedZIons.get(i);
        }

        // Add the matched ions to the vector
        fragIons.add(matchMHIons);
        fragIons.add(matchMHNH3Ions);
        fragIons.add(matchMHH2OIons);
        fragIons.add(matchAIons);
        fragIons.add(matchAH2OIons);
        fragIons.add(matchANH3Ions);
        fragIons.add(matchBIons);
        fragIons.add(matchBH2OIons);
        fragIons.add(matchBNH3Ions);
        fragIons.add(matchCIons);
        fragIons.add(matchXIons);
        fragIons.add(matchYIons);
        fragIons.add(matchYH2OIons);
        fragIons.add(matchYNH3Ions);
        fragIons.add(matchZIons);
        return fragIons;
    }

    /**
     * This method returns the 2-dim peptide hash map
     *
     * @return iPeptideMap PeptideMap
     */
    public PeptideMap getPeptideMap() {
        if (iPeptideMap == null) {
            iPeptideMap = new PeptideMap(iXTParser.getRawPeptideMap(), this.getProteinMap(), iXTParser.getNumberOfSpectra());
        }
        return iPeptideMap;
    }

    /**
     * Returns an iterator over all the spectra.
     *
     * @return Iterator
     */
    public Iterator getSpectraIterator() {
        if (iSpectraList == null) {
            iSpectraList = this.getSpectraList();
            return iSpectraList.iterator();
        } else {
            return iSpectraList.iterator();
        }
    }

    /**
     * This method returns a list of all the spectra.
     *
     * @return iSpectraList all the spectra
     */
    public ArrayList<Spectrum> getSpectraList() {
        if (iSpectraList == null) {

            iSpectraNumber = iXTParser.getNumberOfSpectra();
            iSpectraList = new ArrayList<Spectrum>();
            //Get the entries from the spectrum map
            HashMap<String, String> spectrumSection = iXTParser.getRawSpectrumMap();

            // Set the IdToNumberMap
            iIdToNumberMap = new HashMap<String, Integer>();

            for (int i = 1; i < iSpectraNumber + 1; i++) {

                // SpectrumID
                int spectrumID = Integer.parseInt(spectrumSection.get("id" + i));
                iIdToNumberMap.put(spectrumSection.get("id" + i), i);

                // Precursor mass
                double precursorMh = Double.parseDouble(spectrumSection.get("mh" + i));

                // Precursor charge
                int precursorCharge = Integer.parseInt(spectrumSection.get("z" + i));

                // Precursor retention time
                String precursorRetentionTime = spectrumSection.get("rt" + i);

                // valueList contains exspectValue, summedScore, maxFragIonIntensity and intensityMultiplier
                ArrayList<Double> valueList = new ArrayList<Double>();

                double expectValue = Double.parseDouble(spectrumSection.get("expect" + i));
                valueList.add(expectValue);
                double summedScore = Double.parseDouble(spectrumSection.get("sumI" + i));
                valueList.add(summedScore);
                double maxFragIonIntensity = Double.parseDouble(spectrumSection.get("maxI" + i));
                valueList.add(maxFragIonIntensity);
                double intensityMultiplier = Double.parseDouble(spectrumSection.get("fI" + i));
                valueList.add(intensityMultiplier);

                // Spectrum label
                String label = spectrumSection.get("label" + i);

                // Add all the spectra parameters to the list
                iSpectraList.add(new Spectrum(spectrumID, precursorMh, precursorCharge, precursorRetentionTime, valueList, label, i));
            }
        }
        return iSpectraList;
    }

    /**
     * This method returns a specific spectrum for a given spectrum number.
     *
     * @param aSpectrumNumber The spectrum number
     * @return Spectrum
     */
    public Spectrum getSpectrum(final int aSpectrumNumber) {
        return getSpectraList().get(aSpectrumNumber - 1);
    }

    /**
     * Returns a spectrum number for a given spectrum id.
     *
     * @param aSpectrumID The spectrumID
     * @return spectrumNumber
     */
    public int getSpectrumNumberForId(String aSpectrumID) {
        int spectrumNumber;

        if (iSpectraList == null) {
            iSpectraList = this.getSpectraList();
        }
        spectrumNumber = iIdToNumberMap.get(aSpectrumID);
        return spectrumNumber;
    }

    /**
     * Returns the support data section.
     *
     * @param aSpectrumNumber The spectrum number
     * @return the support data section
     */
    public SupportData getSupportData(int aSpectrumNumber) {
        return new SupportData(iXTParser.getSupportDataMap(), aSpectrumNumber);

    }

    /**
     * This method returns the support data hash map.
     *
     * @return iProteinMap ProteinMap
     */
    public ProteinMap getProteinMap() {
        if (iProteinMap == null) {
            iProteinMap = new ProteinMap(iXTParser.getProteinIDList(), iXTParser.getRawProteinMap());
        }
        return iProteinMap;
    }

    /**
     * Returns the path and the name of the X!Tandem file.
     *
     * @return iFileName String
     */
    public String getFileName() {
        if (iFileName == null) {
            return "N/A";
        } else {
            return iFileName;
        }
    }

    /**
     * Sets the path and the name of the X!Tandem file.
     *
     * @param aFileName The name of the X!Tandem file
     */
    private void setFileName(String aFileName) {
        iFileName = aFileName;
    }

    /**
     * This method returns the xtandem parser.
     *
     * @return iXTParser XTandemParser
     */
    public XTandemParser getXTandemParser() {
        return iXTParser;
    }

    /**
     * Returns the total number of spectra.
     *
     * @return iSpectraNumber
     */
    public int getSpectraNumber() {
        return iSpectraNumber;
    }
}
