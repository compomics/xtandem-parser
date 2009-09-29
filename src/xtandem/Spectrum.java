package xtandem;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class holds the spectrum details given from the xtandem xml file.
 *
 * @author Thilo Muth
 *
 */
public class Spectrum implements Serializable{

	/**
	 * This variable holds the spectrum id, which is the number associated with the mass spectrum that was identified.
	 * The number usually represents the 1-based position of this spectrum in the original data file.
	 */
	private int iSpectrumId = 0;

	/**
	 * This variable represents the precursor ion mass (plus a proton) from the spectrum.
	 */
	private double iPrecursorMh = 0;

	/**
	 * This variable hold the precursor ion charge from the spectrum.
	 */
	private int iPrecursorCharge = 0;

	/**
	 * This variable holds the expectation value for the top ranked protein identfied with this spectrum.
	 */
	private double iExpectValue = 0;

	/**
	 * This variable holds the label that is the text from the protein sequence FASTA file description
	 * line for the top ranked protein identified.
	 */
	private String iLabel = null;

	/**
	 * This variable holds the log10-value of the sum of all the fragment ion intensities.
	 */
	private double iSummedScore = 0;

	/**
	 * This variable holds the maximum fragment ion intensity.
	 */
	private double iMaxFragIonIntensity = 0;

	/**
	 * This variable represents a multiplier to convert the normalized spectrum contained in this
	 * group back to the original intensity values.
	 */
	private double iIntensityMultiplier = 0;

	/**
	 * The list holds the double values of the expectation value, the summed score, the maximum fragment ion intensity
	 * and the intensity multiplier.
	 */
	private ArrayList<Double> iSpectrumValues = null;

	/**
	 * This variable holds the (internal) spectrum number.
	 */
	private int iSpectrumNumber = 0;	

	/**
     * Empty constructor for spectrum object.
     */
	public Spectrum(){
	}
    
	/**
	 * Constructor with all given parameters:
	 * 1) spectrumID
	 * 2) precorsor mh
	 * 3) precursor charge
	 * 4) spectrum values
	 * 5) label
	 * 6) spectrum number
	 *
	 * @param aSpectrumID
	 * @param aPrecursorMh
	 * @param aPrecursorCharge
	 * @param aSpectrumValues
	 * @param aLabel
	 * @param aSpectrumNumber
	 */
	public Spectrum(int aSpectrumID, double aPrecursorMh, int aPrecursorCharge, ArrayList<Double> aSpectrumValues, String aLabel, int aSpectrumNumber) {
        iSpectrumId = aSpectrumID;
        iPrecursorMh = aPrecursorMh;
        iPrecursorCharge = aPrecursorCharge;
        iExpectValue = aSpectrumValues.get(0);
        iLabel = aLabel;
        iSummedScore = aSpectrumValues.get(1);
        iMaxFragIonIntensity = aSpectrumValues.get(2);
        iIntensityMultiplier = aSpectrumValues.get(3);
        iSpectrumNumber = aSpectrumNumber;        
    }

	/**
	 * Returns the spectrum id.
	 * @return iSpectrumId
	 */
	public int getSpectrumId() {
		return iSpectrumId;
	}
	
	/**
	 * Sets the spectrum id.
	 * @param aSpectrumId
	 */
	public void setSpectrumId(int aSpectrumId) {
		this.iSpectrumId = aSpectrumId;
	}
	
	/**
	 * Returns the precursor mass.
	 * @return iPrecursorMh
	 */
	public double getPrecursorMh() {
		return iPrecursorMh;
	}
	
	/**
	 * Sets the precursor mass.
	 * @param aPrecursorMh
	 */
	public void setPrecursorMh(double aPrecursorMh) {
		this.iPrecursorMh = aPrecursorMh;
	}
	
	/**
	 * Returns the precursor charge.
	 * @return iPrecursorCharge
	 */
	public int getPrecursorCharge() {
		return iPrecursorCharge;
	}
	
	/**
	 * Sets the precursor charge.
	 * @param aPrecursorCharge
	 */
	public void setPrecursorCharge(int aPrecursorCharge) {
		this.iPrecursorCharge = aPrecursorCharge;
	}
	
	/**
	 * Returns the expect value.
	 * @return iExpectValue
	 */
	public double getExpectValue() {
		return iExpectValue;
	}
	
	/**
	 * Sets the expect value
	 * @param aExpectValue
	 */
	public void setExpectValue(double aExpectValue) {
		this.iExpectValue = aExpectValue;
	}
	
	/**
	 * Returns the label of the spectrum.
	 * @return iLabel
	 */
	public String getLabel() {
		return iLabel;
	}
	
	/**
	 * Sets the label of the spectrum.
	 * @param aLabel
	 */
	public void setLabel(String aLabel) {
		this.iLabel = aLabel;
	}
	
	/**
	 * Returns the summed score of the spectrum.
	 * @return iSummedScore
	 */
	public double getSummedScore() {
		return iSummedScore;
	}
	
	/**
	 * Sets the summed score of the spectrum.
	 * @param aSummedScore
	 */
	public void setSummedScore(double aSummedScore) {
		this.iSummedScore = aSummedScore;
	}
	
	/**
	 * Returns the maximum fragment ion intensity.
	 * @return iMaxFragIonIntensity
	 */
	public double getMaxFragIonIntensity() {
		return iMaxFragIonIntensity;
	}
	
	/**
	 * Sets the maximum fragment ion intensity.
	 * @param aMaxFragIonIntensity
	 */
	public void setMaxFragIonIntensity(double aMaxFragIonIntensity) {
		this.iMaxFragIonIntensity = aMaxFragIonIntensity;
	}
	
	/**
	 * Returns the intensity multiplier.
	 * @return iIntensityMultiplier
	 */
	public double getIntensityMultiplier() {
		return iIntensityMultiplier;
	}
	
	/**
	 * Sets he intensity multiplier.
	 * @param aIntensityMultiplier
	 */
	public void setIntensityMultiplier(double aIntensityMultiplier) {
		this.iIntensityMultiplier = aIntensityMultiplier;
	}
	
	/**
	 * Returns a double list of the spectrum values.
	 * @return iSpectrumValues
	 */
	public ArrayList<Double> getISpectrumValues() {
		return iSpectrumValues;
	}
	
	/**
	 * Sets a double list of the spectrum values.
	 * @param aSpectrumValues
	 */
	public void setISpectrumValues(ArrayList<Double> aSpectrumValues) {
		iSpectrumValues = aSpectrumValues;
	}
	
	/**
	 * Returns the number of the spectrum.
	 * @return iSpectrumNumber
	 */
	public int getSpectrumNumber() {
		return iSpectrumNumber;
	}
	
	/**
	 * Sets the number of the spectrum.
	 * @param aSpectrumNumber
	 */
	public void setSpectrumNumber(int aSpectrumNumber) {
		this.iSpectrumNumber = aSpectrumNumber;
	}
}
