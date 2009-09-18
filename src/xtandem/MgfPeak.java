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

	public double getMZ() {
		return iPeakMz;
	}
	public double getIntensity() {
		return iPeakIntensity;
	}
	public int getCharge() {
		return iPeakCharge;
	}
	public void setPeakMz(double aPeakMz) {
		iPeakMz = aPeakMz;
	}
	public void setPeakIntensity(double aPeakIntensity) {
		iPeakIntensity = aPeakIntensity;
	}
	public void setPeakCharge(int aPeakCharge) {
		iPeakCharge = aPeakCharge;
	}
}
