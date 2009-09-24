package interfaces;


/**
 * This class represents an Ion object. It holds the ion number, the ion score and the 6 different types of ions:
 * 1) a-c ions
 * 2) x-z ions
 * 
 * @author Thilo Muth
 *
 */
public interface Ion {    

	public final static int A_ION = 0;
	public final static int B_ION = 1;
	public final static int C_ION = 2;
	public final static int X_ION = 3;
	public final static int Y_ION = 4;
	public final static int Z_ION = 5;

	public boolean isMatch(Peak[] peaks, double aMassError);
	
    public double getMZ();
   
    public double getIntensity();
    
    public String getType();
    
    public int getNumber();

    public double getScore();

}
