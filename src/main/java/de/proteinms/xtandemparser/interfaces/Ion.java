package de.proteinms.xtandemparser.interfaces;

/**
 * This class represents an Ion object. It holds the ion number,
 * the ion score and the 6 different types of ions:
 * 1) a-c ions
 * 2) x-z ions
 * 
 * @author Thilo Muth
 */
public interface Ion {

    /**
     * This int is the identifier for an a ion.
     */
    public final static int A_ION = 0;
    /**
     * This int is the identifier for a b ion.
     */
    public final static int B_ION = 1;
    /**
     * This int is the identifier for a c ion.
     */
    public final static int C_ION = 2;
    /**
     * This int is the identifier for a x ion.
     */
    public final static int X_ION = 3;
    /**
     * This int is the identifier for a y ion.
     */
    public final static int Y_ION = 4;
    /**
     * This int is the identifier for a z ion.
     */
    public final static int Z_ION = 5;
    /**
     * This int is the identifier for an a++ ion.
     */
    public final static int A_DOUBLE_ION = 6;
    /**
     * This int is the identifier for a b++ ion.
     */
    public final static int B_DOUBLE_ION = 7;
    /**
     * This int is the identifier for a c++ ion.
     */
    public final static int C_DOUBLE_ION = 8;
    /**
     * This int is the identifier for a x++ ion.
     */
    public final static int X_DOUBLE_ION = 9;
    /**
     * This int is the identifier for a y++ ion.
     */
    public final static int Y_DOUBLE_ION = 10;
    /**
     * This int is the identifier for a z++ ion.
     */
    public final static int Z_DOUBLE_ION = 11;
    /**
     * This int is the identifier for a b-h20 ion.
     */
    public final static int B_H20_ION = 12;
    /**
     * This int is the identifier for a b-nh3 ion.
     */
    public final static int B_NH3_ION = 13;

    /**
     * This method checks whether the ion matched with a given set of peaks
     *
     * @param peaks
     * @param aMassError
     * @return boolean
     */
    public boolean isMatch(Peak[] peaks, double aMassError);

    /**
     * Returns the m/z.
     *
     * @return double
     */
    public double getMZ();

    /**
     * Returns the intensity.
     *
     * @return double
     */
    public double getIntensity();

    /**
     * Returns the type.
     *
     * @return String
     */
    public String getType();

    /**
     * Returns the ion number.
     *
     * @return int
     */
    public int getNumber();

    /**
     * Returns the score.
     *
     * @return double
     */
    public double getScore();
}
