package xtandem;

import interfaces.Peaklist;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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

    // TODO: Delete this... as well as the Query class :)
//    public ArrayList<Query> getQueries(){
//    	ArrayList<Query> queries = new ArrayList<Query>();
//    	HashMap<Integer, Peaklist> rawFileMap = this.getRawFileMap();
//
//    	int spectraNumber = this.getRawFileSpectraNumber();
//    	if (this.getRawFileType().equals("mgf")){
//    		for(int i = 1; i <= spectraNumber; i++){
//    			MgfPeaklist peaks = (MgfPeaklist) rawFileMap.get(i);
//
//    			Query query = new Query();
//    			query.setQueryID(i);
//   				query.setPrecursorCharge(Integer.parseInt(peaks.getCharge().substring(7, 8)));
//    			query.setPrecursorMz(peaks.getPepmass());
//    			ArrayList<Double> masses = new ArrayList<Double>();
//    			ArrayList<Double> intensities = new ArrayList<Double>();
//
//    			// Check if it's a identified spectrum in all spectra from the raw file
//    			for(int j = 0; j < this.getSpectraNumber(); j++){
//    				SupportData supportData = this.getSupportData(i);
//    				// Get the masses/intensities from the mgf file
//    				System.out.println("sup data: " + supportData.getFragIonSpectrumDescription());
//    				System.out.println("peak" + peaks.getTitle());
//        			if(supportData.getFragIonSpectrumDescription().equals(peaks.getTitle())){
//        				query.setMasses(supportData.getXValuesFragIonMass2Charge());
//        				System.out.println("test!");
//            			query.setIntensities(supportData.getYValuesFragIonMass2Charge());
//            			query.setTitle(supportData.getFragIonSpectrumDescription());
//            			query.setIdentified(true);
//        			}
//    			}
//
//    			// Get the masses/intensities from the xtandem xml file, if not identified
//    			if (!query.isIdentified()){
//    				ArrayList<MgfPeak> mgfPeaks = peaks.getPeaks();
//    				for (MgfPeak mgfPeak : mgfPeaks) {
//    					masses.add(mgfPeak.getMZ());
//    					intensities.add(mgfPeak.getIntensity());
//    				}
//        			query.setMasses(masses);
//        			query.setIntensities(intensities);
//        			query.setTitle(peaks.getTitle());
//    			}
//    			queries.add(query);
//    		}
//    	}
//    	return queries;
//    }
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
