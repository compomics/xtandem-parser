package de.proteinms.xtandemparser.xtandem;

import de.proteinms.xtandemparser.interfaces.Ion;
import de.proteinms.xtandemparser.interfaces.Peak;

/**
 * This class holds the fragment ion and is an implementation of Ion.
 *
 * @author Thilo Muth
 */
public class FragmentIon implements Ion {

    /**
     * The m/z of the frament ion.
     */
    private double iMz = 0.0;
    /**
     * The intensity of the fragment ion.
     */
    private double iIntensity = 0.0;
    /**
     * The number of the fragment ion. For example: b1 ion has the number 1, b4 has number 4 etc.
     */
    private int iNumber = 0;
    /**
     * The score of the fragment ion.
     */
    private int iScore = 0;
    /**
     * The type of this ion. b1 is a b type
     */
    private String iType;
    /**
     * The error margin of the fragment ion. Is needed later for the spectrum panel.
     */
    private double iErrorMargin = 0.0;
    /**
     * The ID of the fragment ion. The values are specified in the Ion interface.
     */
    private int iID = -1;
    /**
     * This variable holds the calculated theoretical vs. experimental error.
     */
    private double iTheoreticalExperimentalMassError;  


    /**
     * Constructor gets all the parameters to create an fragment ion object.
     *
     * @param aMz The m/z value of the fragment ion.
     * @param aID The ID of the fragment ion.
     * @param aNumber The number of the fragment ion.
     * @param aType The type of the fragment ion.
     * @param aErrorMargin The error margin of the fragment ion.
     */
    public FragmentIon(double aMz, int aID, int aNumber, String aType, double aErrorMargin) {
        iMz = aMz;
        iID = aID;
        iNumber = aNumber;
        iType = aType;
        iErrorMargin = aErrorMargin;
    }

    /**
     * The same constructor as above but with the intensity.
     *
     * @param aMz The m/z value of the fragment ion.
     * @param aIntensity The intensity of the fragment ion.
     * @param aID The ID of the fragment ion.
     * @param aNumber The number of the fragment ion.
     * @param aType The type of the fragment ion.
     * @param aErrorMargin The error margin of the fragment ion.
     */
    public FragmentIon(double aMz, double aIntensity, int aID, int aNumber, String aType, double aErrorMargin) {
        iMz = aMz;
        iIntensity = aIntensity;
        iID = aID;
        iNumber = aNumber;
        iType = aType;
        iErrorMargin = aErrorMargin;
    }

    /**
     * This method compares the theoretical mass peak with the experimental one and tells
     * if it's a match using a specific mass error tolerance and calculating the theoretical/
     * experimental mass error.
     *
     * @param aPeaks The mass peak array
     * @param aMassError The mass error 
     * @return matchFlag boolean
     */
    public boolean isMatch(Peak[] aPeaks, double aMassError) {

        boolean matchFlag = false;

        for (int i = 0; i < aPeaks.length; i++) {
            if (-aMassError <= (aPeaks[i].getMZ() - iMz) && (aPeaks[i].getMZ() - iMz) <= aMassError) {
                iTheoreticalExperimentalMassError = aPeaks[i].getMZ() - iMz;
                matchFlag = true;
                iIntensity = aPeaks[i].getIntensity();
                break;
            }
        }

        return matchFlag;
    }

    /**
     * Returns the m/z of the fragment ion.
     *
     * @return iMz
     */
    public double getMZ() {
        return iMz;
    }

    /**
     * Returns the intensity of the fragment ion.
     *
     * @return iIntensity
     */
    public double getIntensity() {
        return iIntensity;
    }

    /**
     * Returns the number of the fragment ion.
     *
     * @return iNumber
     */
    public int getNumber() {
        return iNumber;
    }

    /**
     * Returns the score of the fragment ion.
     *
     * @return iScore
     */
    public double getScore() {
        return iScore;
    }

    /**
     * Returns the type of the fragment ion.
     *
     * @return iType
     */
    public String getType() {
        return iType;
    }

    /**
     * Returns the error margin.
     *
     * @return iErrorMargin
     */
    public double getErrorMargin() {
        return iErrorMargin;
    }

    /**
     * Returns the id of the fragment ion.
     *
     * @return iID
     */
    public int getID() {
        return iID;
    }

    /**
     * Returns the theoretical experimental mass error.
     * 
     * @return iTheoreticalExperimentalMassError
     */
    public double getTheoreticalExperimentalMassError() {
        return iTheoreticalExperimentalMassError;
    }
}
