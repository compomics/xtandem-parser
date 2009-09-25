package xtandem;

import interfaces.Peak;
/**
 * This class is an implementation of Peak and is used for the fragment ion calculation.
 * @author Thilo Muth
 *
 */
public class SpectrumPeak implements Peak{

	private double iMz;
	private double iIntensity;
	private int iCharge;
	
	public SpectrumPeak(){
		
	}
	
	public SpectrumPeak(double aMz, double aIntensity, int aCharge){
		iMz = aMz;
		iIntensity = aIntensity;
		iCharge = aCharge;
	}
	
	public void setCharge(int aCharge){
		iCharge = aCharge;
	}
	
	public int getCharge() {	
		return iCharge;
	}
	
	public void setIntensity(double aIntensity){
		iIntensity = aIntensity;
	}
	
	public double getIntensity() {
		return iIntensity;
	}

	public void setMz(double aMz){
		iMz = aMz;
	}
	
	public double getMZ() {		
		return iMz;
	}

}
