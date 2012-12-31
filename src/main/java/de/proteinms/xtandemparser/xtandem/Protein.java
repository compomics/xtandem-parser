package de.proteinms.xtandemparser.xtandem;

import java.io.Serializable;

/**
 * This class contains all the parsed data from a xtandem file protein.
 *
 * @author Thilo Muth
 * @author Harald Barsnes
 */
public class Protein implements Serializable {

    /**
     * This variable holds the log10-value of the expectation value of the
     * protein. Note: returns a null object if the protein expected score cannot
     * be estimated.
     */
    private Double iExpectValue = 0.0;
    /**
     * This variable presents the identifier for this particular identification
     * (spectrum#).(id#).
     */
    private String iID = null;
    /**
     * This variable holds a unique number for this protein, calculated by the
     * search engine.
     */
    private String iUID = null;
    /**
     * This variable holds the label which is the description line from the
     * FASTA file. Note that the information is often identical to the content
     * of iDescription, but that iLabel can be shortened. It is therefore
     * recommended to use iDescription instead of iLabel.
     */
    private String iLabel = null;
    /**
     * This variable holds the label which is the description line from the
     * FASTA file, stored as a note for the protein, e.g., &ltnote
     * label="description"&gttr|F1RRZ6|F1RRZ6_PIG Uncharacterized protein
     * (Fragment) OS=Sus scrofa GN=SH3PXD2B PE=4 SV=1:reversed&lt/note&gt. Note
     * that the information is often identical to the content of iLabel, but
     * that iLabel can be shortened.
     */
    private String iDescription = null;
    /**
     * This variable presents the sum of all the fragment ions that identify
     * this protein.
     */
    private Double iSummedScore = 0.0;

    /**
     * The constructor get id, uid, label, description, expect value and summed
     * score to build a protein object.
     *
     * @param aID
     * @param aUID
     * @param aLabel
     * @param aDescription
     * @param aExpectValue
     * @param aSummedScore
     */
    public Protein(String aID, String aUID, String aLabel, String aDescription, Double aExpectValue, Double aSummedScore) {
        iID = aID;
        iUID = aUID;
        iLabel = aLabel;
        iDescription = aDescription;
        iExpectValue = aExpectValue;
        iSummedScore = aSummedScore;
    }

    /**
     * Returns the expect value for the protein. Note: returns a null object if
     * the protein expected score cannot be estimated.
     *
     * @return iExpectValue - can be null
     */
    public Double getExpectValue() {
        return iExpectValue;
    }

    /**
     * Sets the expect value for the protein.
     *
     * @param aExpectValue
     */
    public void setExpectValue(Double aExpectValue) {
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
     * Returns the label of the protein. Note that the information is often
     * identical to the content of iDescription, but that iLabel can be
     * shortened. It is therefore recommended to use iDescription instead of
     * iLabel.
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
     * Returns the description of the protein. Note that the information is
     * often identical to the content of iLabel, but that iLabel can be
     * shortened. It is therefore recommended to use iDescription instead of
     * iLabel.
     *
     * @return iDescription
     */
    public String getDescription() {
        return iDescription;
    }

    /**
     * Sets the description of the protein.
     *
     * @param aDescription
     */
    public void setDescription(String aDescription) {
        iDescription = aDescription;
    }

    /**
     * Returns the summed score of the protein.
     *
     * @return iSummedScore
     */
    public Double getSummedScore() {
        return iSummedScore;
    }

    /**
     * Sets the summed score of the protein.
     *
     * @param aSummedScore
     */
    public void setSummedScore(Double aSummedScore) {
        iSummedScore = aSummedScore;
    }
}
