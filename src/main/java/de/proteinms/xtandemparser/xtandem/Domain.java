package de.proteinms.xtandemparser.xtandem;

/**
 * Domain object.
 *
 * @author Thilo Muth
 */
public class Domain {

    /**
     * This String contains the domain identifier (spectrum#).(i#).(domain#) as
     * present in the X!Tandem file. <b>Note: This might not be unique!</b>
     */
    private String iDomainID = null;
    /**
     * The protein key
     */
    private String proteinKey;
    /**
     * The domain key (unique)
     */
    private String domainKey;
    /**
     * Contains the domain start position == first residue of the domain
     */
    private int iDomainStart = 0;
    /**
     * Contains the domain end position == last residue of the domain
     */
    private int iDomainEnd = 0;
    /**
     * This double holds the expectation value for the peptide identification
     */
    private double iDomainExpect = 0;
    /**
     * This double holds the calculated peptide mass + a proton
     */
    private double iDomainMz = 0;
    /**
     * This double holds the spectrum mh minus the calculated mh
     */
    private double iDomainDeltaMz = 0;
    /**
     * This double hold Tandem's score for the identification
     */
    private double iDomainHyperScore = 0;
    /**
     * This double holds the next score of the domain
     */
    private double iDomainNextScore = 0;
    /**
     * This String holds the upstream flanking sequence
     */
    private String iUpFlankSequence = null;
    /**
     * This String holds the downstream flanking sequence
     */
    private String iDownFlankSequence = null;
    /**
     * This String presents the sequence of the domain
     */
    private String iDomainSequence = null;
    /**
     * Contains the total of missed cleavages
     */
    private int iMissedCleavages = 0;

    /**
     * Returns the domain id of the peptide.
     *
     * @return iDomainID
     */
    public String getDomainID() {
        return iDomainID;
    }

    /**
     * Sets the domain id of the peptide.
     *
     * @param domainID
     */
    public void setDomainID(String domainID) {
        iDomainID = domainID;
    }

    /**
     * Returns the domain key of the peptide.
     *
     * @return domainKey
     */
    public String getDomainKey() {
        return domainKey;
    }

    /**
     * Sets the domain key of the peptide.
     *
     * @param domainKey
     */
    public void setDomainKey(String domainKey) {
        this.domainKey = domainKey;
    }

    /**
     * Returns the domains start location.
     *
     * @return iDomainStart
     */
    public int getDomainStart() {
        return iDomainStart;
    }

    /**
     * Sets the domain start location.
     *
     * @param domainStart
     */
    public void setDomainStart(int domainStart) {
        iDomainStart = domainStart;
    }

    /**
     * Returns the domain end location.
     *
     * @return iDomainEnd
     */
    public int getDomainEnd() {
        return iDomainEnd;
    }

    /**
     * Sets the domain end location.
     *
     * @param domainEnd
     */
    public void setDomainEnd(int domainEnd) {
        iDomainEnd = domainEnd;
    }

    /**
     * Returns the domain expect value.
     *
     * @return iDomainExpect
     */
    public double getDomainExpect() {
        return iDomainExpect;
    }

    /**
     * Sets the domain expect value
     *
     * @param domainExpect
     */
    public void setDomainExpect(double domainExpect) {
        iDomainExpect = domainExpect;
    }

    /**
     * Returns the domain mh/mz of the peptide.
     *
     * @return iDomainMz
     */
    public double getDomainMh() {
        return iDomainMz;
    }

    /**
     * Sets the domain mh/mz for the peptide.
     *
     * @param domainMh
     */
    public void setDomainMh(double domainMh) {
        this.iDomainMz = domainMh;
    }

    /**
     * Returns the domain delta mh/mz.
     *
     * @return iDomainDeltaMz
     */
    public double getDomainDeltaMh() {
        return iDomainDeltaMz;
    }

    /**
     * Sets the domain delta mh/mz.
     *
     * @param domainDeltaMh
     */
    public void setDomainDeltaMh(double domainDeltaMh) {
        this.iDomainDeltaMz = domainDeltaMh;
    }

    /**
     * Returns the domain hyperscore.
     *
     * @return iDomainHyperScore
     */
    public double getDomainHyperScore() {
        return iDomainHyperScore;
    }

    /**
     * Sets the domain hyperscore
     *
     * @param domainHyperScore
     */
    public void setDomainHyperScore(double domainHyperScore) {
        this.iDomainHyperScore = domainHyperScore;
    }

    /**
     * Returns the domain nextscore.
     *
     * @return iDomainNextScore
     */
    public double getDomainNextScore() {
        return iDomainNextScore;
    }

    /**
     * Sets the domain nextscore.
     *
     * @param domainNextScore
     */
    public void setDomainNextScore(double domainNextScore) {
        this.iDomainNextScore = domainNextScore;
    }

    /**
     * Returns the upstream flanking sequence of the peptide.
     *
     * @return iUpFlankSequence
     */
    public String getUpFlankSequence() {
        return iUpFlankSequence;
    }

    /**
     * Sets the upstream flanking sequence of the peptide.
     *
     * @param aUpFlankSequence
     */
    public void setUpFlankSequence(String aUpFlankSequence) {
        this.iUpFlankSequence = aUpFlankSequence;
    }

    /**
     * Returns the downstream flanking sequence of the peptide.
     *
     * @return iDownFlankSequence
     */
    public String getDownFlankSequence() {
        return iDownFlankSequence;
    }

    /**
     * Sets the downstream flanking sequence of the peptide.
     *
     * @param aDownFlankSequence
     */
    public void setDownFlankSequence(String aDownFlankSequence) {
        this.iDownFlankSequence = aDownFlankSequence;
    }

    /**
     * Returns the domain sequence as a string.
     *
     * @return iDomainSequence
     */
    public String getDomainSequence() {
        return iDomainSequence;
    }

    /**
     * Sets the domain sequence of the peptide.
     *
     * @param aDomainSequence
     */
    public void setDomainSequence(String aDomainSequence) {
        this.iDomainSequence = aDomainSequence;
    }

    /**
     * Returns the number of missed cleavages.
     *
     * @return iMissedCleavages
     */
    public int getMissedCleavages() {
        return iMissedCleavages;
    }

    /**
     * Sets the number of missed cleavages.
     *
     * @param aMissedCleavages
     */
    public void setMissedCleavages(int aMissedCleavages) {
        this.iMissedCleavages = aMissedCleavages;
    }
    
    /**
     * Sets the protein key
     * @param proteinKey the protein key
     */
    public void setProteinKey(String proteinKey) {
        this.proteinKey = proteinKey;
    }
    
    /**
     * Returns the protein key
     * @return the protein key
     */
    public String getProteinKey() {
        return proteinKey;
    }
}
