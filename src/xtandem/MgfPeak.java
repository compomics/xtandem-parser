package xtandem;

import interfaces.Peak;
/**
 * The class is an implementation of the peak interface.
 * It hold the peak m/z, the peak intensity and the peak charge.
 * 
 * @author Thilo Muth
 *
 */
public class MgfPeak implements Peak{

    /**
     * This variable holds the peak m/z.
     */
	private double iPeakMz = 0;

    /**
     * This variable holds the peak intensity.
     */
	private double iPeakIntensity = 0;

    /**
     * This variable holds the peak charge.
     */
	private int iPeakCharge = 0;

    /**
     * The empty constructor for a mgfpeak object.
     */
	public MgfPeak(){
	}

	/**
	 * Returns the m/z of the mgf peak.
	 * @return iPeakMz
	 */
	public double getMZ() {
		return iPeakMz;
	}
	
	/**
	 * Returns the intensity of the mgf peak.
	 * @return iPeakIntensity
	 */
	public double getIntensity() {
		return iPeakIntensity;
	}
	
	/**
	 * Returns the charge of the mgf peak.
	 * @return iPeakCharge
	 */
	public int getCharge() {
		return iPeakCharge;
	}
	
	/**
	 * Sets the m/z of the mgf peak.
	 * @param aPeakMz
	 */
	public void setPeakMz(double aPeakMz) {
		iPeakMz = aPeakMz;
	}
	
	/**
	 * Sets the intensity of the mgf peak.
	 * @param aPeakIntensity
	 */
	public void setPeakIntensity(double aPeakIntensity) {
		iPeakIntensity = aPeakIntensity;
	}
	
	/**
	 * Sets the charge of the mgf peak.
	 * @param aPeakCharge
	 */
	public void setPeakCharge(int aPeakCharge) {
		iPeakCharge = aPeakCharge;
	}
}
