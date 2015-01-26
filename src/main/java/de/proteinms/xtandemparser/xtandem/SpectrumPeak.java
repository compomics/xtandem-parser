package de.proteinms.xtandemparser.xtandem;

import de.proteinms.xtandemparser.interfaces.Peak;

/**
 * This class is an implementation of Peak and is used for the fragment ion
 * calculation.
 *
 * @author Thilo Muth
 */
public class SpectrumPeak implements Peak {

    /**
     * This double holds the m/z.
     */
    private double iMz;
    /**
     * This double holds the intensity.
     */
    private double iIntensity;
    /**
     * This Integer holds the charge.
     */
    private int iCharge;

    /**
     * Empty constructor for a spectrum peak.
     */
    public SpectrumPeak() {
    }

    /**
     * Constructor gets the m/z, the intensity and the charge.
     *
     * @param aMz the m/z
     * @param aIntensity the intensity
     * @param aCharge the charge
     */
    public SpectrumPeak(double aMz, double aIntensity, int aCharge) {
        iMz = aMz;
        iIntensity = aIntensity;
        iCharge = aCharge;
    }

    /**
     * Sets the charge.
     *
     * @param aCharge the charge
     */
    public void setCharge(int aCharge) {
        iCharge = aCharge;
    }

    /**
     * Returns the charge.
     *
     * @return the charge
     */
    public int getCharge() {
        return iCharge;
    }

    /**
     * Sets the intensity.
     *
     * @param aIntensity the intensity
     */
    public void setIntensity(double aIntensity) {
        iIntensity = aIntensity;
    }

    /**
     * Returns the intensity.
     *
     * @return the intensity
     */
    public double getIntensity() {
        return iIntensity;
    }

    /**
     * Sets the m/z.
     *
     * @param aMz the m/z
     */
    public void setMz(double aMz) {
        iMz = aMz;
    }

    /**
     * Returns the m/z.
     *
     * @return the m/z
     */
    public double getMZ() {
        return iMz;
    }
}
