package xtandem;

import java.util.ArrayList;

/**
 * This class contains all the parsed data from a xtandem file peptide
 * 
 * @author Thilo Muth
 */
public class Peptide {

	/**
	 * This variable contains the peptide id (as an index).
	 */
	private String iPeptideID;
	/**
	 * Contains the start position of the peptide == beginning of the protein's peptide sequence
	 */
	private int iStart = 0;
	/**
	 * Contains the end position of the peptide == end ot the protein's peptide sequence
	 */
	private int iEnd = 0;
	/**
	 * This String contains the peptide sequence
	 */
	private String iSequence = null;
	/**
	 * This String contains the original FASTA file path
	 */
	private String iFastaFilePath = null;
	/**
	 * This String contains the domain identifier (spectrum#).(i#).(domain#)
	 */
	private String iDomainID = null;
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
	private double domainMh = 0;
	/**
	 * This double holds the spectrum mh minus the calculated mh
	 */
	private double domainDeltaMh = 0;
	/**
	 * This double hold Tandem's score for the identification
	 */
	private double domainHyperScore = 0;
	/**
	 * This double holds the next score of the domain
	 */
	private double domainNextScore = 0;
	/**
	 * This list contains all the differnt sorts of ions.
	 */
	private ArrayList<Ion> iIons = null;
	/**
	 * This String holds the upstream flanking sequence
	 */
	private String upFlankSequence = null;
	/**
	 * This String holds the downstream flanking sequence
	 */
	private String downFlankSequence = null;
	/**
	 * This String presents the sequence of the domain
	 */
	private String domainSequence = null;
    /**
     * Contains the total of missed cleavages
     */
    private int missedCleavages = 0;


    /**
     * The Peptide constructor gets the peptide id the start + end position and the sequences as string.
     */
    public Peptide(String aPeptideID,int aStart, int aEnd, String aSequence){
    	iPeptideID = aPeptideID;
    	iStart = aStart;
    	iEnd = aEnd;
    	iSequence = aSequence;
    }

    /**
     * This method gets the start position of the peptide
     *
     * @return int iStart
     */
	public int getStart() {
		return iStart;
	}

	/**
     * This method sets the start position of the peptide
     *
     * @param aStart
     */
	public void setStart(int aStart) {
		this.iStart = aStart;
	}

	/**
     * This method gets the end position of the peptide
     *
     * @return int iEnd
     */
	public int getEnd() {
		return iEnd;
	}

	/**
     * This method sets the end position of the peptide
     *
     * @param aEnd
     */
	public void setEnd(int aEnd) {
		this.iEnd = aEnd;
	}


	//TODO: API for getters and setters!
	public String getSequence() {
		return iSequence;
	}
	public void setSequence(String aSequence) {
		this.iSequence = aSequence;
	}

	public String getDomainID() {
		return iDomainID;
	}

	public void setDomainID(String domainID) {
		iDomainID = domainID;
	}

	public int getDomainStart() {
		return iDomainStart;
	}

	public void setDomainStart(int domainStart) {
		iDomainStart = domainStart;
	}

	public int getDomainEnd() {
		return iDomainEnd;
	}

	public void setDomainEnd(int domainEnd) {
		iDomainEnd = domainEnd;
	}

	public double getDomainExpect() {
		return iDomainExpect;
	}

	public void setDomainExpect(double domainExpect) {
		iDomainExpect = domainExpect;
	}

	public double getDomainMh() {
		return domainMh;
	}

	public void setDomainMh(double domainMh) {
		this.domainMh = domainMh;
	}

	public double getDomainDeltaMh() {
		return domainDeltaMh;
	}

	public void setDomainDeltaMh(double domainDeltaMh) {
		this.domainDeltaMh = domainDeltaMh;
	}

	public double getDomainHyperScore() {
		return domainHyperScore;
	}

	public void setDomainHyperScore(double domainHyperScore) {
		this.domainHyperScore = domainHyperScore;
	}

	public double getDomainNextScore() {
		return domainNextScore;
	}

	public void setDomainNextScore(double domainNextScore) {
		this.domainNextScore = domainNextScore;
	}

	public ArrayList<Ion> getIons(){
        return iIons;
    }

    public void setIons(ArrayList<Ion> aIons){
        iIons = aIons;
    }

	public String getUpFlankSequence() {
		return upFlankSequence;
	}

	public void setUpFlankSequence(String aUpFlankSequence) {
		this.upFlankSequence = aUpFlankSequence;
	}

	public String getDownFlankSequence() {
		return downFlankSequence;
	}

	public void setDownFlankSequence(String aDownFlankSequence) {
		this.downFlankSequence = aDownFlankSequence;
	}

	public String getDomainSequence() {
		return domainSequence;
	}

	public void setDomainSequence(String aDomainSequence) {
		this.domainSequence = aDomainSequence;
	}

	public int getMissedCleavages() {
		return missedCleavages;
	}

	public void setMissedCleavages(int aMissedCleavages) {
		this.missedCleavages = aMissedCleavages;
	}

	public String getFastaFilePath() {
		return iFastaFilePath;
	}

	public void setFastaFilePath(String aFastaFilePath) {
		iFastaFilePath = aFastaFilePath;
	}

	public String getPeptideID() {
		return iPeptideID;
	}

}
