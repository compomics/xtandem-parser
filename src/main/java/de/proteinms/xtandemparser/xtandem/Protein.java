package de.proteinms.xtandemparser.xtandem;

import java.io.Serializable;

/**
 * This class contains all the parsed data from a xtandem file protein.
 *
 * @author Thilo Muth
 */
public class Protein implements Serializable {

    /**
     * This variable holds the log10-value of the expectation value of the protein.
     */
    private double iExpectValue = 0;
    /**
     *This variable presents the identifier for this particular identification (spectrum#).(id#).
     */
    private String iID = null;
    /**
     * This variable holds a unique number for this protein, calculated by the search engine.
     */
    private String iUID = null;
    /**
     * This variable holds the label which is the description line from the FASTA file.
     */
    private String iLabel = null;
    /**
     * This variable presents the sum of all the fragment ions that identify this protein.
     */
    private double iSummedScore = 0;

    /**
     * The constructor get id, uid, label, expect value and summed score to build a protein object.
     *
     * @param aID
     * @param aUID
     * @param aLabel
     * @param aExpectValue
     * @param aSummedScore
     */
    public Protein(String aID, String aUID, String aLabel, double aExpectValue, double aSummedScore) {
        iID = aID;
        iUID = aUID;
        iLabel = aLabel;
        iExpectValue = aExpectValue;
        iSummedScore = aSummedScore;
    }

    /**
     * Returns the expect value for the protein.
     *
     * @return iExpectValue
     */
    public double getExpectValue() {
        return iExpectValue;
    }

    /**
     * Sets the expect value for the protein.
     *
     * @param aExpectValue
     */
    public void setExpectValue(double aExpectValue) {
        iExpectValue = aExpectValue;
    }

    /**
     * Returns the id of the protein.
     *
     * @return iID
     */
    public String getID() {
        return iID;
    }

    /**
     * Sets the id of the protein.
     *
     * @param aId
     */
    public void setID(String aId) {
        iID = aId;
    }

    /**
     * Returns the uId of the protein.
     *
     * @return iUID
     */
    public String getUID() {
        return iUID;
    }

    /**
     * Sets the uID of the protein.
     *
     * @param aUid
     */
    public void setUID(String aUid) {
        iUID = aUid;
    }

    /**
     * Returns the label of the protein.
     *
     * @return iLabel
     */
    public String getLabel() {
        return iLabel;
    }

    /**
     * Sets the label of the protein.
     *
     * @param aLabel
     */
    public void setLabel(String aLabel) {
        iLabel = aLabel;
    }

    /**
     * Returns the summed score of the protein.
     *
     * @return iSummedScore
     */
    public double getSummedScore() {
        return iSummedScore;
    }

    /**
     * Sets the summed score of the protein.
     * 
     * @param aSummedScore
     */
    public void setSummedScore(double aSummedScore) {
        iSummedScore = aSummedScore;
    }
}
