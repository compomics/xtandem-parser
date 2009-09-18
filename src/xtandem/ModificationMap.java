package xtandem;

import interfaces.Modification;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * In this class the fixed and variable modifications are sorted out and lists for these modifications are build.
 *
 * @author Thilo Muth
 *
 */
public class ModificationMap {
	/**
	 * Contains a hash map for the fixed modifications.
	 */
	private HashMap<String, Modification> iFixedModificationMap;
	/**
	 * Contains a hash map for the variable modifications.
	 */
	private HashMap<String, Modification> iVarModificationMap;
	/**
	 * The instance contains a PeptideMap object.
	 */
	private PeptideMap iPeptideMap;

	/**
	 * The constructor builds the fixed and variable modification maps from the given raw mod map and the input parameters.
	 *
	 * @param aRawModMap The raw modification map from the parser
	 * @param aPeptideMap The peptide map
	 * @param aInputParams The input parameters from the parser
	 * @param aNumberOfSpectra The total number of spectra
	 */
	public ModificationMap(HashMap aRawModMap, PeptideMap aPeptideMap, InputParams aInputParams, int aNumberOfSpectra){
		buildModificationMaps(aRawModMap, aPeptideMap, aInputParams, aNumberOfSpectra);
		iPeptideMap = aPeptideMap;
	}

	/**
	 * This method checks for fixed or variable modifications and builds the maps.
	 *
	 * @param rawModMap The raw modification map from the parser
	 * @param peptideMap The peptide map
	 * @param inputParams The input parameters from the parser
	 * @param numberOfSpectra The total number of spectra
	 */
	private void buildModificationMaps(HashMap rawModMap, PeptideMap peptideMap, InputParams inputParams, int numberOfSpectra) {

		// Initialize the modification hash maps
		iFixedModificationMap = new HashMap<String, Modification>();
		iVarModificationMap = new HashMap<String, Modification>();

        if (rawModMap != null) {
            for (int i = 1; i <= numberOfSpectra; i++) {

            	for (int j = 1; j <= peptideMap.getNumberOfPeptides(i); j++){
            		// The counter for the peptides
                    int m_counter = 1;

                    while (rawModMap.get("name" + "_s" + i + "_p" + j + "_m" + m_counter) != null) {
                    	// Get a specific id for the modification s_(spectrum#)_p(peptide#)_m(modifcation#)
                    	String modID = ("s" + i + "_p" + j + "_m" + m_counter).toString();

                    	// Get the specific parameters for the modification
    	    	        String modName = rawModMap.get("name" + "_s" + i + "_p" + j + "_m" + m_counter).toString();
    	    	        double modMass = Double.parseDouble(rawModMap.get("modified" + "_s" + i + "_p" + j + "_m" + m_counter).toString());
    	    	        String modLocation = rawModMap.get("at" + "_s" + i + "_p" + j + "_m" + m_counter).toString();

    	    	        // Get the domainID
    	    	        String domainID = peptideMap.getPeptideByIndex(i, j).getDomainID();

    	    	        // Check for fixed modification
    	    	        if(inputParams.getResidueModMass().equals(modName)){
    	    	        	// Create an instance of a fixed modification.
        	    	        FixedModification fixedMod = new FixedModification(modName, modMass, modLocation, domainID);

        	    	        // Put the modification into the map, value is the mod id.
        	    	        iFixedModificationMap.put(modID, fixedMod);
    	    	        } else {
    	    	        	// The rest will be assumed to be variable modifications.
    	    	        	VariableModification varMod = new VariableModification(modName, modMass, modLocation, domainID);

    	    	        	// Put the modification into the map, value is the mod id.
        	    	        iVarModificationMap.put(modID, varMod);
    	    	        }
    	    	        m_counter++;
                    }
            	}
            }
        }
    }

	/**
	 * Returns the fixed modifications as list.
	 *
	 * @param aSpectrumNumber The number of the spectrum
	 * @return modificationsList The fixed modification list
	 */
	public ArrayList<Modification> getFixedModifications(int aSpectrumNumber){
		ArrayList<Modification> modificationList = new ArrayList<Modification>();
		if(iPeptideMap != null && iFixedModificationMap != null){
			for (int j = 1; j <= iPeptideMap.getNumberOfPeptides(aSpectrumNumber); j++){
				int modCount = 1;
				while (iFixedModificationMap.get("s" + aSpectrumNumber + "_p" + j + "_m" + modCount) != null) {
					modificationList.add((Modification) iFixedModificationMap.get("s" + aSpectrumNumber + "_p" + j + "_m" + modCount));
					modCount++;
				}
			}
		}
		return modificationList;
	}

	/**
	 * Returns the variable modifications as list.
	 *
	 * @param aSpectrumNumber The number of the spectrum
	 * @return modificationsList The variable modification list
	 */
	public ArrayList<Modification> getVariableModifications(int aSpectrumNumber){
		ArrayList<Modification> modificationList = new ArrayList<Modification>();
		if(iPeptideMap != null && iVarModificationMap != null){
			for (int j = 1; j <= iPeptideMap.getNumberOfPeptides(aSpectrumNumber); j++){
				int modCount = 1;
				while (iVarModificationMap.get("s" + aSpectrumNumber + "_p" + j + "_m" + modCount) != null) {
					modificationList.add((Modification) iVarModificationMap.get("s" + aSpectrumNumber + "_p" + j + "_m" + modCount));
					modCount++;
				}
			}
		}
		return modificationList;
	}
}
