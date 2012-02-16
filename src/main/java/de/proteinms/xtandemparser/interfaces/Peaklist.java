package de.proteinms.xtandemparser.interfaces;

import java.util.ArrayList;

/**
 * This class respresents the Peaklist interface.
 *
 * @author Thilo Muth
 */
public interface Peaklist {

    /**
     * This method returns an array list of peaks.
     *
     * @return ArrayList
     */
    public ArrayList getPeaks();

    /**
     * This method returns a string which gives information about the MS type
     * used, for example 1.) MS 2.) MS/MS (MS^2) 3.) MS/MS/MS (MS^3)
     *
     * @return String
     */
    public String getMSType();
}
