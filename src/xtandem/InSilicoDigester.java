package xtandem;

import interfaces.Modification;
import interfaces.Peak;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * This class is used to do a calculation for the theoretical masses of a, b, c, x, y and z ions (plus double charged ones)
 * and do the matching of experimental and theoretical masses. 
 * @author Thilo Muth
 *
 */
public class InSilicoDigester {	
	/**
	 * This variable contains the peptide sequence.
	 * @param aSequence
	 */
	private String iSequence;
	/**
	 * Contains the modification map.
	 */
	private ModificationMap iModMap;
	/**
	 * Contains the peptide object which should be digested.
	 */
	private Peptide iPeptide;
	
	/**
	 * The masses map for knowledge of amino acid masses etc.
	 */
	private HashMap<String, Double> iMasses;
	
	/**
	 * The a ions.
	 */
	private FragmentIon[] iAIons;
	
	/**
	 * The b ions.
	 */
	private FragmentIon[] iBIons;
	
	/**
	 * The c ions.
	 */
	private FragmentIon[] iCIons;
	
	/**
	 * The x ions.
	 */
	private FragmentIon[] iXIons;
	
	/**
	 * The y ions.
	 */
	private FragmentIon[] iYIons;
	
	/**
	 * The z ions.
	 */
	private FragmentIon[] iZIons;
	
	/**
	 * The a++ ions.
	 */
	private FragmentIon[] iADoubleIons;
	
	/**
	 * The b++ ions.
	 */
	private FragmentIon[] iBDoubleIons;
	
	/**
	 * The c++ ions.
	 */
	private FragmentIon[] iCDoubleIons;
	
	/**
	 * The x++ ions.
	 */
	private FragmentIon[] iXDoubleIons;
	
	/**
	 * The y++ ions.
	 */
	private FragmentIon[] iYDoubleIons;
	
	/**
	 * The z++ ions.
	 */
	private FragmentIon[] iZDoubleIons;
	
	/**
	 * The fragment mass error tolerance.
	 */
	private double iFragmentMassError;	
	
	/**
	 * Constructor get a peptide object, the modification map, the input parameters and the masses map.
	 * @param aPeptide A peptide object which should be "in silico" digested.
	 * @param aModMap Modification map to know where have been modifications on the peptide.
	 * @param aMasses Masses map to know which amino acid has which mass. 
	 */
	public InSilicoDigester(Peptide aPeptide, ModificationMap aModMap, HashMap aMasses){
		iPeptide = aPeptide;
		iSequence = iPeptide.getDomainSequence();	
		iModMap = aModMap;
		iMasses = aMasses;		
		iBIons = new FragmentIon[iSequence.length()];
		iYIons = new FragmentIon[iSequence.length()];
		iFragmentMassError = Parameters.FRAGMENTMASSERROR;
		calculateIons();
	}
	
	/**
     * This method calculates the masses of the peptide, including the masses of the aminoacids plus
     * the masses of the modifications (fixed/variable and N- resp. C-term included)
     *
     * @return double[] Contains the mass of the part of the sequence. The amino acid position is the index.
     */
	public double[] calculatePeptideMasses(){
		double mass;
        double[] peptideMasses = new double[iSequence.length()];       
		for (int i = 0; i < iSequence.length(); i++) {
			mass = 0.0;

			// Add the fixed modifications masses (N and C term included)
			ArrayList<Modification> fixModList = iModMap.getFixedModifications(iPeptide.getSpectrumNumber());
			if (fixModList.size() > 0) {
				for (Modification fixMod : fixModList) {
					int modIndex = (Integer.parseInt(fixMod.getLocation()) - iPeptide.getDomainStart());
					if(modIndex == i){	
						mass += fixMod.getMass();
					}					
				}
			}

			// Add the the variable modification masses (N and C term included)
			ArrayList<Modification> varModList = iModMap.getVariableModifications(iPeptide.getSpectrumNumber());
			if (varModList.size() > 0) {
				for (Modification varMod : varModList) {
					int modIndex = (Integer.parseInt(varMod.getLocation()) - iPeptide.getDomainStart());
					if(modIndex == i){
						mass += varMod.getMass();
					}
				}
			}
			
			// For each amino acid add the specific mass			
			String aa = String.valueOf(iSequence.charAt(i));
			mass += iMasses.get(aa);
			
			// Add each specific mass to the array
			peptideMasses[i] = mass;
		}		 
		return peptideMasses;		
	}
	
	/**
	 * This method calculated the theoretical masses of the b and y ions of the peptide.
	 * The fragment ion are stored as objects, for example yIons[0] is the y1 ion.
	 */
	private void calculateIons(){
		double [] peptideMasses = calculatePeptideMasses();
		double hydrogenMass = iMasses.get("Hydrogen");
		double c_termMass = iMasses.get("C_term");		
		
		// Calculate the b and y ions
		int length = iSequence.length();
		for (int i = 0; i < length; i++){
			double bMass = 0.0;
			double yMass = 0.0;
			
			 // Each peptide mass is added to the b ion mass 
			for(int j = 0; j <= i; j++){
				bMass += peptideMasses[j];
			}
			// Add one extra hydrogen on the N terminal amino acid
			bMass = bMass + hydrogenMass;
			
			// Create an instance of the fragment b ion
			iBIons[i] = new FragmentIon(bMass, FragmentIon.B_ION, i+1, "b", iFragmentMassError);
			
			// Each peptide mass is added to the y ion mass, taking the reverse direction (from the C terminal end) 
			for(int j = 0; j <= i; j++){
				yMass += peptideMasses[(length - 1) - j];
			}
			// Add two extra hydrogen on the N terminal end and one hydroxyl at the C terminal end
			yMass = yMass + c_termMass + (2 * hydrogenMass);
			
			// Create an instance of the fragment y ion
			iYIons[i] = new FragmentIon(yMass, FragmentIon.Y_ION, i+1, "y", iFragmentMassError);			
		}
	}
	
	/**
	 * This method tries to match the theoretical masses of the ions with the masses of the experimental peaks.
	 * @param theoFragIons The theoretical fragment ions.
	 * @param aPeaks The experimental peaks.
	 * @return matchedIons A Vector containing all the matched fragment ions.
	 */
	public Vector getMatchedIons(FragmentIon[] theoFragIons, Peak[] aPeaks) {
        Vector matchedIons = new Vector();
        for (FragmentIon fragIon : theoFragIons) {
        	if (fragIon.isMatch(aPeaks, iFragmentMassError)) {
            	matchedIons.add(fragIon);
            }
		}        
        return matchedIons;
    }
	
	/**
	 * Returns an array of the (successfully matched) b ions.
	 * @return iBions 
	 */
	public FragmentIon[] getBIons() {
		return iBIons;
	}
	
	/**
     * Returns an array of the b++ ions: b ions plus H divided by 2
     *
     * @return iBDoubleIons 
     */
    public FragmentIon[] getBDoubleIons() {
    	iBDoubleIons = new FragmentIon[iBIons.length];
        for (int i = 0; i < iBIons.length; i++) {            
        	iBDoubleIons[i] = new FragmentIon(((iBIons[i].getMZ() + 1.007825) / 2), FragmentIon.B_DOUBLE_ION, (i + 1), "b++", iFragmentMassError);
        }
        return iBDoubleIons;
    }
	
	/**
     * Returns an array of the a ions: b ions minus CO and plus H2.
     *
     * @return iAIons 
     */
    public FragmentIon[] getAIons() {
        iAIons = new FragmentIon[iBIons.length];
        for (int i = 0; i < iBIons.length; i++) {            
        	iAIons[i] = new FragmentIon((iBIons[i].getMZ() - 27.994915), FragmentIon.A_ION, (i + 1), "a", iFragmentMassError);
        }
        return iAIons;
    }
    
    /**
     * Returns an array of the a++ ions: a ions plus H divided by 2
     *
     * @return iADoubleIons 
     */
    public FragmentIon[] getADoubleIons() {
    	iADoubleIons = new FragmentIon[iAIons.length];
        for (int i = 0; i < iAIons.length; i++) {            
        	iADoubleIons[i] = new FragmentIon(((iAIons[i].getMZ() + 1.007825) / 2), FragmentIon.A_DOUBLE_ION, (i + 1), "a++", iFragmentMassError);
        }
        return iADoubleIons;
    }
    	
    /**
     * Returns an array of the c ions: b ions plus NH3
     *
     * @return iCIons 
     */
    public FragmentIon[] getCIons() {
    	iCIons = new FragmentIon[iBIons.length];
        for (int i = 0; i < iBIons.length; i++) {            
        	iCIons[i] = new FragmentIon((iBIons[i].getMZ() + 17.026549), FragmentIon.C_ION, (i + 1), "c", iFragmentMassError);
        }
        return iCIons;
    }
    
    /**
     * Returns an array of the c++ ions: c ions plus H divided by 2
     *
     * @return iCDoubleIons 
     */
    public FragmentIon[] getCDoubleIons() {
    	iCDoubleIons = new FragmentIon[iCIons.length];
        for (int i = 0; i < iCIons.length; i++) {            
        	iCDoubleIons[i] = new FragmentIon(((iCIons[i].getMZ() + 1.007825) / 2), FragmentIon.C_DOUBLE_ION, (i + 1), "c++", iFragmentMassError);
        }
        return iCDoubleIons;
    }
    
	/**
	 * Returns an array of the (successfully matched) y ions.
	 * @return iYIons
	 */
	public FragmentIon[] getYIons() {
		return iYIons;
	}
	
	/**
     * Returns an array of the y++ ions: y ions plus H divided by 2
     *
     * @return iYDoubleIons 
     */
    public FragmentIon[] getYDoubleIons() {
    	iYDoubleIons = new FragmentIon[iYIons.length];
        for (int i = 0; i < iYIons.length; i++) {            
        	iYDoubleIons[i] = new FragmentIon(((iYIons[i].getMZ() + 1.007825) / 2), FragmentIon.Y_DOUBLE_ION, (i + 1), "y++", iFragmentMassError);
        }
        return iYDoubleIons;
    }
    
	
	/**
     * Returns an array of the x ions: y ions plus CO and minus H2
     *
     * @return iXIons 
     */
    public FragmentIon[] getXIons() {
    	iXIons = new FragmentIon[iYIons.length];
        for (int i = 0; i < iYIons.length; i++) {            
        	iXIons[i] = new FragmentIon((iYIons[i].getMZ() + 25.979265), FragmentIon.X_ION, (i + 1), "x", iFragmentMassError);
        }
        return iXIons;
    }
    
    /**
     * Returns an array of the x++ ions: x ions plus H divided by 2
     *
     * @return iXDoubleIons 
     */
    public FragmentIon[] getXDoubleIons() {
    	iXDoubleIons = new FragmentIon[iXIons.length];
        for (int i = 0; i < iXIons.length; i++) {            
        	iXDoubleIons[i] = new FragmentIon(((iXIons[i].getMZ() + 1.007825) / 2), FragmentIon.X_DOUBLE_ION, (i + 1), "x++", iFragmentMassError);
        }
        return iXDoubleIons;
    }
    
    /**
     * Returns an array of the z ions: y ions minus NH3
     *
     * @return iZIons 
     */
    public FragmentIon[] getZIons() {
    	iZIons = new FragmentIon[iYIons.length];
        for (int i = 0; i < iYIons.length; i++) {            
        	iZIons[i] = new FragmentIon((iYIons[i].getMZ() - 17.026549), FragmentIon.Z_ION, (i + 1), "z", iFragmentMassError);
        }
        return iZIons;
    }
    
    /**
     * Returns an array of the z++ ions: z ions plus H divided by 2
     *
     * @return iZDoubleIons 
     */
    public FragmentIon[] getZDoubleIons() {
    	iZDoubleIons = new FragmentIon[iZIons.length];
        for (int i = 0; i < iZIons.length; i++) {            
        	iZDoubleIons[i] = new FragmentIon(((iZIons[i].getMZ() + 1.007825) / 2), FragmentIon.Z_DOUBLE_ION, (i + 1), "z++", iFragmentMassError);
        }
        return iZDoubleIons;
    }
}
