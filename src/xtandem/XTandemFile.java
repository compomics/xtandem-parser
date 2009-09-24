package xtandem;

import interfaces.Ion;
import interfaces.Peaklist;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import parser.MgfFileParser;
import parser.XTandemParser;
/**
 * This class represents the xtandem file object as the starting point which provides all the methods
 * to use the information which are parsed by the Xtandem parser.
 * 
 * @author Thilo Muth
 *
 */
public class XTandemFile implements Serializable{

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
     * Private variable iXTParser as an instance of XTandemParser.     *
     */
    private XTandemParser iXTParser = null;

    /**
     * HashMap that gets an id an returns a number.
     */
	private HashMap<String, Integer> iIdToNumberMap;

	/**
	 *  This is an instance of the InputParams object.
	 */
	private InputParams iInputParams = null;

	/**
	 *  This is an instance of the PerformParams object.
	 */
	private PerformParams iPerformParams = null;

	/**
	 * This is an instance of the SupportData object.
	 */
	private SupportData iSupportData = null;

	private String iRawFile = null;

	/**
	 * This string holds the raw file type.
	 */
	private String iRawFileType = null;

	/**
	 * This variable holds the number of spectra.
	 */
	private int iSpectraNumber = 0;

	private HashMap<Integer, Peaklist> iRawFileMap;
	
	/**
     * Constructor of XTandemFile gets a string to an existing path and filename of the xtandem file.
     *
     * @param aXTandemFile 
     */
	public XTandemFile(String aXTandemFile) {
        try {
            File inputFile = new File(aXTandemFile);
            if (!inputFile.exists()) {
                throw new IllegalArgumentException("XTandem xml-file " + aXTandemFile + " doesn't exist.");
            }
            iXTParser = new XTandemParser(inputFile);
            setFileName(aXTandemFile);            
            iSpectraList = this.getSpectraList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    /**
     * Constructor of XTandemFile gets a string to an existing path and filename of the xtandem file.
     *
     * @param aXTandemFile
     * @param aRawFile
     */
	public XTandemFile(String aXTandemFile, String aRawFile) {
        try {
            File inputFile = new File(aXTandemFile);
            if (!inputFile.exists()) {
                throw new IllegalArgumentException("XTandem xml-file " + aXTandemFile + " doesn't exist.");
            }
            iXTParser = new XTandemParser(inputFile);
            setFileName(new File(aXTandemFile).getName());
            iRawFile = aRawFile;            
            setRawFileType(iRawFile);
            iSpectraList = this.getSpectraList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InputParams getInputParameters() {
        if (iInputParams == null) {
        	iInputParams = new InputParams(iXTParser.getInputParamMap());
        }
        return iInputParams;
    }

    public PerformParams getPerformParameters() {
        if (iPerformParams == null) {
        	iPerformParams = new PerformParams(iXTParser.getPerformParamMap());
        }
        return iPerformParams;
    }
    
    /**
     * This method returns a hash map for the masses.
     * @return map The masses hashmap
     */
    public HashMap getMassesMap(){
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
     * This method returns the modification hash map
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
     * @param peptide
     * @return Vector The vector containing b ions and y ions
     */
    public Vector getFragmentIonsForPeptide(Peptide peptide){
    	Vector<Ion[]> fragIons = new Vector();
    	// Get an instance of the InSilicoDigester
    	InSilicoDigester digester = new InSilicoDigester(peptide,this.getModificationMap(), this.getMassesMap());
    	// The vector should contain two arrays: b ions & y ions
    	fragIons.add(digester.getBIons());
    	fragIons.add(digester.getYIons());    	
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
     * This method returns a map of peaklist from the raw files. It differentiates between mgf, mzData and mzML file types.
     * @return rawFileMap
     */
    public HashMap<Integer, Peaklist> getRawFileMap(){

    	if (this.getRawFileType().equals("mgf")){
    		iRawFileMap = new MgfFileParser(iRawFile, iXTParser.getTitle2SpectrumIDMap(), this.getPeptideMap()).getPeakListMap();
    	}
    	//TODO: Implement the other two parsers!

    	return iRawFileMap;
    }

    /**
     * Returns the total number of spectra from the raw file.
     * @return number The total number of spectra.
     */
    public int getRawFileSpectraNumber(){

    	int number = 0;
    	if(iRawFileMap != null){
    		number = iRawFileMap.size();
    	} else {
    		if (this.getRawFileType().equals("mgf")){
        		number = new MgfFileParser(iRawFile, iXTParser.getTitle2SpectrumIDMap(), this.getPeptideMap()).getSpectraNumber();
        	}
            //TODO: Implement the other two parsers! 
    	}

    	return number;
    }

    public String getRawFileType(){
    	return iRawFileType;
    }

    public void setRawFileType(String aRawFile){
    		if (iRawFile.endsWith(".mgf") || iRawFile.endsWith(".MGF")){
        		iRawFileType = "mgf";
        	}
        	if (iRawFile.endsWith(".mzData") || iRawFile.endsWith(".mzdata") || iRawFile.endsWith(".MZDATA")){
        		iRawFileType = "mzdata";
        	}
        	if (iRawFile.endsWith(".mzML") || iRawFile.endsWith(".mzml") || iRawFile.endsWith(".MZML")){
        		iRawFileType = "mzml";
        	}
    }

    /**
     * Returns an iterator over all the spectra.
     * @return Iterator
     */
    public Iterator getSpectraIterator(){
    	if(iSpectraList == null){
    		iSpectraList = this.getSpectraList();
    		return iSpectraList.iterator();
    	} else {
    		return iSpectraList.iterator();
    	}
    }
	/**
	 * This method returns a list of all the spectra.
	 * @return iSpectraList ArrayList<Spectrum>
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
	                int spectrumID = Integer.parseInt((String) spectrumSection.get("id" + i));
	                iIdToNumberMap.put(spectrumSection.get("id" + i).toString(), i);

	                // Precursor mass
	                double precursorMh = Double.parseDouble((String) spectrumSection.get("mh" + i));

	                // Precursor charge
	                int precursorCharge = Integer.parseInt((String) spectrumSection.get("z" + i));

	                // valueList contains exspectValue, summedScore, maxFragIonIntensity and intensityMultiplier
	                ArrayList<Double> valueList = new ArrayList<Double>();

	                double expectValue = Double.parseDouble((String) spectrumSection.get("expect" + i));
	                valueList.add(expectValue);
	                double summedScore = Double.parseDouble((String) spectrumSection.get("sumI" + i));
	                valueList.add(summedScore);
	                double maxFragIonIntensity = Double.parseDouble((String) spectrumSection.get("maxI" + i));
	                valueList.add(maxFragIonIntensity);
	                double intensityMultiplier = Double.parseDouble((String) spectrumSection.get("fI" + i));
	                valueList.add(intensityMultiplier);

	                // Spectrum label
	                String label = (String) spectrumSection.get("label" + i);

	                // Add all the spectra parameters to the list
	                iSpectraList.add(new Spectrum(spectrumID, precursorMh, precursorCharge, valueList, label, i));
	            }
	      }
	      return iSpectraList;
	}

	/**
	 * This method returns a specific spectrum for a given spectrum number.
	 * @param aSpectrumNumber
	 * @return Spectrum
	 */
	public Spectrum getSpectrum(final int aSpectrumNumber) {
        return (Spectrum) getSpectraList().get(aSpectrumNumber-1);  //To change body of implemented methods use File | Settings | File Templates.
    }

	/**
	 * Returns a spectrum number for a given spectrum id.
	 * @param aSpectrumID
	 * @return spectrumNumber
	 */
	public int getSpectrumNumberForId(String aSpectrumID){
		int spectrumNumber = 0;

		if (iSpectraList == null) {
			iSpectraList = this.getSpectraList();
		}
		spectrumNumber = iIdToNumberMap.get(aSpectrumID);
		return spectrumNumber;
	}

	public SupportData getSupportData(int aSpectrumNumber) {
       	iSupportData = new SupportData(iXTParser.getSupportDataMap(), aSpectrumNumber);
        return iSupportData;
    }

	/**
     * This method returns the support data hash map
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
	 * @return iFileName String
	 */
	public String getFileName() {
		if (iFileName == null){
			return "N/A";
		} else {
			return iFileName;
		}
	}

	/**
	 * Sets the path and the name of the X!Tandem file.
	 * @param aFileName
	 */
	public void setFileName(String aFileName) {
		iFileName = aFileName;
	}

	/**
	 * This method returns the xtandem parser.
	 * @return iXTParser XTandemParser
	 */
	public XTandemParser getXTandemParser() {
		return iXTParser;
	}

	public int getSpectraNumber() {
		return iSpectraNumber;
	}
}
