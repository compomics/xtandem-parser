package de.proteinms.xtandemparser.xtandem;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class holds the spectrum details given from the xtandem xml file.
 *
 * @author Thilo Muth
 */
public class Spectrum implements Serializable {

    /**
     * This variable holds the spectrum id, which is the number associated with
     * the mass spectrum that was identified. The number usually represents the
     * 1-based position of this spectrum in the original data file.
     */
    private int iSpectrumId = 0;
    /**
     * This variable represents the precursor ion mass (plus a proton) from the
     * spectrum.
     */
    private double iPrecursorMh = 0;
    /**
     * This variable holds the precursor ion charge from the spectrum.
     */
    private int iPrecursorCharge = 0;
    /**
     * This variable holds the precursor retention time from the spectrum.
     */
    private String iPrecursorRetentionTime = "";
    /**
     * This variable holds the expectation value for the top ranked protein
     * identified with this spectrum.
     */
    private double iExpectValue = 0;
    /**
     * This variable holds the label that is the text from the protein sequence
     * FASTA file description line for the top ranked protein identified.
     */
    private String iLabel = null;
    /**
     * This variable holds the log10-value of the sum of all the fragment ion
     * intensities.
     */
    private double iSummedScore = 0;
    /**
     * This variable holds the maximum fragment ion intensity.
     */
    private double iMaxFragIonIntensity = 0;
    /**
     * This variable represents a multiplier to convert the normalized spectrum
     * contained in this group back to the original intensity values.
     */
    private double iIntensityMultiplier = 0;
    /**
     * The list holds the double values of the expectation value, the summed
     * score, the maximum fragment ion intensity and the intensity multiplier.
     */
    private ArrayList<Double> iSpectrumValues = null;
    /**
     * This variable holds the (internal) spectrum number.
     */
    private int iSpectrumNumber = 0;

    /**
     * Empty constructor for spectrum object.
     */
    public Spectrum() {
    }

    /**
     * Constructor with all given parameters: 1) spectrumID 2) precursor mh 3)
     * precursor charge 4) spectrum values 5) label 6) spectrum number.
     *
     * @param aSpectrumID the spectrum ID
     * @param aPrecursorMh the precursor mh
     * @param aPrecursorCharge the precursor charge
     * @param aPrecursorRetentionTime the precursor retention time
     * @param aSpectrumValues the spectrum values
     * @param aLabel the label
     * @param aSpectrumNumber the spectrum number
     */
    public Spectrum(int aSpectrumID, double aPrecursorMh, int aPrecursorCharge, String aPrecursorRetentionTime,
            ArrayList<Double> aSpectrumValues, String aLabel, int aSpectrumNumber) {

        iSpectrumId = aSpectrumID;
        iPrecursorMh = aPrecursorMh;
        iPrecursorCharge = aPrecursorCharge;
        iPrecursorRetentionTime = aPrecursorRetentionTime;
        iExpectValue = aSpectrumValues.get(0);
        iLabel = aLabel;
        iSummedScore = aSpectrumValues.get(1);
        iMaxFragIonIntensity = aSpectrumValues.get(2);
        iIntensityMultiplier = aSpectrumValues.get(3);
        iSpectrumNumber = aSpectrumNumber;
    }

    /**
     * Returns the spectrum id.
     *
     * @return iSpectrumId
     */
    public int getSpectrumId() {
        return iSpectrumId;
    }

    /**
     * Sets the spectrum id.
     *
     * @param aSpectrumId the spectrum id
     */
    public void setSpectrumId(int aSpectrumId) {
        this.iSpectrumId = aSpectrumId;
    }

    /**
     * Returns the precursor mass.
     *
     * @return iPrecursorMh
     */
    public double getPrecursorMh() {
        return iPrecursorMh;
    }

    /**
     * Sets the precursor mass.
     *
     * @param aPrecursorMh he precursor mass
     */
    public void setPrecursorMh(double aPrecursorMh) {
        this.iPrecursorMh = aPrecursorMh;
    }

    /**
     * Returns the precursor charge.
     *
     * @return iPrecursorCharge
     */
    public int getPrecursorCharge() {
        return iPrecursorCharge;
    }

    /**
     * Sets the precursor charge.
     *
     * @param aPrecursorCharge the precursor charge
     */
    public void setPrecursorCharge(int aPrecursorCharge) {
        this.iPrecursorCharge = aPrecursorCharge;
    }
    
    /**
     * Returns the precursor retention time.
     *
     * @return iPrecursorCharge
     */
    public String getPrecursorRetentionTime() {
        return iPrecursorRetentionTime;
    }

    /**
     * Sets the precursor retention time.
     *
     * @param aPrecursorRetentionTime he precursor retention time
     */
    public void setPrecursorRetentionTime(String aPrecursorRetentionTime) {
        this.iPrecursorRetentionTime = aPrecursorRetentionTime;
    }

    /**
     * Returns the expect value.
     *
     * @return iExpectValue
     */
    public double getExpectValue() {
        return iExpectValue;
    }

    /**
     * Sets the expect value.
     *
     * @param aExpectValue the expect value
     */
    public void setExpectValue(double aExpectValue) {
        this.iExpectValue = aExpectValue;
    }

    /**
     * Returns the label of the spectrum.
     *
     * @return iLabel
     */
    public String getLabel() {
        return iLabel;
    }

    /**
     * Sets the label of the spectrum.
     *
     * @param aLabel the label of the spectrum
     */
    public void setLabel(String aLabel) {
        this.iLabel = aLabel;
    }

    /**
     * Returns the summed score of the spectrum.
     *
     * @return iSummedScore
     */
    public double getSummedScore() {
        return iSummedScore;
    }

    /**
     * Sets the summed score of the spectrum.
     *
     * @param aSummedScore the summed score
     */
    public void setSummedScore(double aSummedScore) {
        this.iSummedScore = aSummedScore;
    }

    /**
     * Returns the maximum fragment ion intensity.
     *
     * @return iMaxFragIonIntensity
     */
    public double getMaxFragIonIntensity() {
        return iMaxFragIonIntensity;
    }

    /**
     * Sets the maximum fragment ion intensity.
     *
     * @param aMaxFragIonIntensity the maximum fragment ion intensity
     */
    public void setMaxFragIonIntensity(double aMaxFragIonIntensity) {
        this.iMaxFragIonIntensity = aMaxFragIonIntensity;
    }

    /**
     * Returns the intensity multiplier.
     *
     * @return iIntensityMultiplier
     */
    public double getIntensityMultiplier() {
        return iIntensityMultiplier;
    }

    /**
     * Sets the intensity multiplier.
     *
     * @param aIntensityMultiplier the intensity multiplier
     */
    public void setIntensityMultiplier(double aIntensityMultiplier) {
        this.iIntensityMultiplier = aIntensityMultiplier;
    }

    /**
     * Returns a double list of the spectrum values.
     *
     * @return iSpectrumValues
     */
    public ArrayList<Double> getISpectrumValues() {
        return iSpectrumValues;
    }

    /**
     * Sets a double list of the spectrum values.
     *
     * @param aSpectrumValues list of the spectrum values
     */
    public void setISpectrumValues(ArrayList<Double> aSpectrumValues) {
        iSpectrumValues = aSpectrumValues;
    }

    /**
     * Returns the number of the spectrum.
     *
     * @return iSpectrumNumber
     */
    public int getSpectrumNumber() {
        return iSpectrumNumber;
    }

    /**
     * Sets the number of the spectrum.
     *
     * @param aSpectrumNumber the number of the spectrum
     */
    public void setSpectrumNumber(int aSpectrumNumber) {
        this.iSpectrumNumber = aSpectrumNumber;
    }
}
