package xtandem;

import interfaces.Peaklist;

import java.util.ArrayList;
/**
 * This class is an implemtation of the peaklist information, it holds the precursor mass, the charge, the title, the mstype,
 * the spectrum number, the mgf peaks and the identified peptides.
 *
 * @author Thilo Muth
 *
 */
public class MgfPeaklist implements Peaklist{

	 /**
	  * This list holds the peak objects.
	  */
    private ArrayList<MgfPeak> iPeakList = new ArrayList();
    /**
     * This string holds the ms type.
     */
    private String iMSType = null;
    /**
     * This double holds the peptide precursor mass.
     */
    private double iPepmass = 0;
    /**
     * This variable holds the charge string.
     */
    private String iCharge = null;
    /**
     * This variable holds the title.
     */
    private String iTitle = null;
    /**
     * This boolean defines identified peaks.
     */
    private boolean iIdentfied = false;
    /**
     * This int holds the spectrum number.
     */
    private int iSpecNumber = 0;
    /**
     * This list holds the identified peptides.
     */
    private ArrayList<Peptide> iIdentifiedPeptides = null;

    /**
     * This method returns the peaks as a vector.
     * @return Peak[]
     */
	public ArrayList<MgfPeak> getPeaks() {
		return iPeakList;
	}

	/**
	 * This method sets the peak array and trims the peak list.
	 * @param aPeakList
	 */
    public void setPeaks(ArrayList<MgfPeak> aPeakList) {
    	// Trim to save some space + time
    	aPeakList.trimToSize();
    	// Set the new peak vector
    	iPeakList = aPeakList;
    }

	/**
	 * Returns the ms type used for the peaks.
	 */
	public String getMSType() {
		return iMSType;
	}
	/**
	 * Sets the ms type used for the peaks.
	 * @param aType
	 */
	public void setMSType(String aType) {
		iMSType = aType;
	}

	/**
	 * Returns the peptide mass of the peaks.
	 * @return iPepmass
	 */
	public double getPepmass() {
		return iPepmass;
	}

	/**
	 * Sets the peptide mass of the peaks.
	 * @param aPepmass
	 */
	public void setPepmass(double aPepmass) {
		iPepmass = aPepmass;
	}

	/**
	 * Returns the charge of the peaks.
	 * @return iCharge
	 */
	public String getCharge() {
		return iCharge;
	}

	/**
	 * Sets the charge of the peaks.
	 * @param aCharge
	 */
	public void setCharge(String aCharge) {
		iCharge = aCharge;
	}

	/**
	 * Returns the title of the peaks.
	 * @return iTitle
	 */
	public String getTitle() {
		return iTitle;
	}

	/**
	 * Sets the title of the peaks.
	 * @param aTitle
	 */
	public void setTitle(String aTitle) {
		iTitle = aTitle;
	}

	public boolean isIdentfied() {
		return iIdentfied;
	}

	public void setIdentfied(boolean identfied) {
		iIdentfied = identfied;
	}

	public int getIdentifiedSpectrumNumber() {
		return iSpecNumber;
	}

	public void setIdentifiedSpectrumNumber(int aSpecNumber) {
		iSpecNumber = aSpecNumber;
	}

	public ArrayList<Peptide> getIdentifiedPeptides() {
		return iIdentifiedPeptides;
	}

	public void setIdentifiedPeptides(ArrayList<Peptide> identifiedPeptides) {
		iIdentifiedPeptides = identifiedPeptides;
	}

}
