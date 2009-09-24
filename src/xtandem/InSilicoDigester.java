package xtandem;

import interfaces.Modification;
import interfaces.Peak;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * This class is used to do a calculation for the theoretical masses of b and y ions 
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
	 * The b ions.
	 */
	private FragmentIon[] iBIons;
	/**
	 * The y ions.
	 */
	private FragmentIon[] iYIons;
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
	 * @param theoFrags The theoretical fragment ions.
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
	 * Returns an array of the (successfully matched) y ions.
	 * @return iYIons
	 */
	public FragmentIon[] getYIons() {
		return iYIons;
	}
}
