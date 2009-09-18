package xtandem;

import java.util.ArrayList;

/**
 * This class holds the spectrum details given from the xtandem xml file.
 *
 * @author Thilo Muth
 *
 */
public class Spectrum {

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

	public int getSpectrumId() {
		return iSpectrumId;
	}
	public void setSpectrumId(int spectrumId) {
		this.iSpectrumId = spectrumId;
	}
	public double getPrecursorMh() {
		return iPrecursorMh;
	}
	public void setPrecursorMh(double precursorMh) {
		this.iPrecursorMh = precursorMh;
	}
	public int getPrecursorCharge() {
		return iPrecursorCharge;
	}
	public void setPrecursorCharge(int precursorCharge) {
		this.iPrecursorCharge = precursorCharge;
	}
	public double getExpectValue() {
		return iExpectValue;
	}
	public void setExpectValue(double expectValue) {
		this.iExpectValue = expectValue;
	}
	public String getLabel() {
		return iLabel;
	}
	public void setLabel(String label) {
		this.iLabel = label;
	}
	public double getSummedScore() {
		return iSummedScore;
	}
	public void setSummedScore(double summedScore) {
		this.iSummedScore = summedScore;
	}
	public double getMaxFragIonIntensity() {
		return iMaxFragIonIntensity;
	}
	public void setMaxFragIonIntensity(double maxFragIonIntensity) {
		this.iMaxFragIonIntensity = maxFragIonIntensity;
	}
	public double getIntensityMultiplier() {
		return iIntensityMultiplier;
	}
	public void setIntensityMultiplier(double intensityMultiplier) {
		this.iIntensityMultiplier = intensityMultiplier;
	}
	public ArrayList<Double> getISpectrumValues() {
		return iSpectrumValues;
	}
	public void setISpectrumValues(ArrayList<Double> spectrumValues) {
		iSpectrumValues = spectrumValues;
	}
	public int getSpectrumNumber() {
		return iSpectrumNumber;
	}
	public void setSpectrumNumber(int spectrumNumber) {
		this.iSpectrumNumber = spectrumNumber;
	}

}
