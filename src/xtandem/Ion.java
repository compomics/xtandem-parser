package xtandem;
/**
 * This class represents an Ion object. It holds the ion number, the ion score and the 6 different types of ions:
 * 1) a-c ions
 * 2) x-z ions
 * 
 * @author Thilo Muth
 *
 */
public class Ion {

    private int iIonNumber = 0;
    private double iIonScore = 0;
    private String iType = "";

	public final static String aION_TYPE = "a_Ion";
	public final static String bION_TYPE = "b_Ion";
	public final static String cION_TYPE = "c_Ion";
	public final static String xION_TYPE = "x_Ion";
	public final static String yION_TYPE = "y_Ion";
	public final static String zION_TYPE = "z_Ion";


    Ion (int aIonNumber, double aIonScore){
        iIonNumber = aIonNumber;
        iIonScore = aIonScore;
    }

    public String getType(){
        return iType;
    }

    public void setType(String aType){
        iType = aType;
    }

    public int getIonNumber(){
        return iIonNumber;
    }

    public double getIonScore(){
        return iIonScore;
    }


}
