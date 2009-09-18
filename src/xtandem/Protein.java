package xtandem;

/**
 *
 * This class contains all the parsed data from a xtandem file protein.
 * @author Thilo Muth
 */
public class Protein {

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

	public Protein(String aID, String aUID, String aLabel, double aExpectValue, double aSummedScore){
		iID = aID;
		iUID = aUID;
		iLabel = aLabel;
		iExpectValue = aExpectValue;
		iSummedScore = aSummedScore;
	}

	public double getExpectValue() {
		return iExpectValue;
	}

	public void setExpectValue(double expectValue) {
		iExpectValue = expectValue;
	}

	public String getID() {
		return iID;
	}

	public void setID(String aId) {
		iID = aId;
	}

	public String getUID() {
		return iUID;
	}

	public void setUID(String aUid) {
		iUID = aUid;
	}

	public String getLabel() {
		return iLabel;
	}

	public void setLabel(String aLabel) {
		iLabel = aLabel;
	}

	public double getSummedScore() {
		return iSummedScore;
	}

	public void setSummedScore(double aSummedScore) {
		iSummedScore = aSummedScore;
	}

}
