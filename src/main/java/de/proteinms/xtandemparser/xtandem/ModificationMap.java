package de.proteinms.xtandemparser.xtandem;

import de.proteinms.xtandemparser.interfaces.Modification;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Map;

/**
 * In this class the fixed and variable modifications are sorted out
 * and lists for these modifications are build.
 *
 * @author Thilo Muth
 */
public class ModificationMap implements Serializable {

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
     * InputParams object
     */
    private InputParams iInputParams;

    /**
     * The constructor builds the fixed and variable modification maps from the given raw mod map and the input parameters.
     *
     * @param aRawModMap The raw modification map from the parser
     * @param aPeptideMap The peptide map
     * @param aInputParams The input parameters from the parser
     * @param aNumberOfSpectra The total number of spectra
     */
    public ModificationMap(HashMap aRawModMap, PeptideMap aPeptideMap, InputParams aInputParams, int aNumberOfSpectra) {
        iInputParams = aInputParams;
        buildModificationMaps(aRawModMap, aPeptideMap, aNumberOfSpectra);
        iPeptideMap = aPeptideMap;        
    }

    /**
     * This method checks for fixed or variable modifications and builds the maps.
     *
     * @param rawModMap The raw modification map from the parser
     * @param peptideMap The peptide map 
     * @param numberOfSpectra The total number of spectra
     */
    private void buildModificationMaps(HashMap rawModMap, PeptideMap peptideMap, int numberOfSpectra) {

        // Initialize the modification hash maps
        iFixedModificationMap = new HashMap<String, Modification>();
        iVarModificationMap = new HashMap<String, Modification>();

        if (rawModMap != null) {
            for (int i = 1; i <= numberOfSpectra; i++) {

                for (int j = 1; j <= peptideMap.getNumberOfPeptides(i); j++) {
                    // The counter for the peptides
                    int m_counter = 1;
                    int m_counter_variable = 1;
                    int m_counter_fixed = 1;

                    while (rawModMap.get("name" + "_s" + i + "_p" + j + "_m" + m_counter) != null) {


                        // Get the specific parameters for the modification
                        String modName = rawModMap.get("name" + "_s" + i + "_p" + j + "_m" + m_counter).toString();
                        double modMass = Double.parseDouble(rawModMap.get("modified" + "_s" + i + "_p" + j + "_m" + m_counter).toString());
                        String modLocation = rawModMap.get("at" + "_s" + i + "_p" + j + "_m" + m_counter).toString();
                                              
                        // Get the domainID
                        String domainID = peptideMap.getPeptideByIndex(i, j).getDomainID();


                        // Check for fixed modification
                        if (isFixedModificationInput(modMass)) {

                        // Get a specific id for the modification (domainID)_m(modifcation#)
                        String modID = (domainID + "_m" + m_counter_fixed).toString();

                            // Create an instance of a fixed modification.
                            FixedModification fixedMod = new FixedModification(modName, modMass, modLocation, m_counter);

                            // Put the modification into the map, value is the mod id.
                            iFixedModificationMap.put(modID, fixedMod);
                            m_counter_fixed++;

                        } else if(isVariableModificationInput(modMass)){

                        // Get a specific id for the modification (domainID)_m(modifcation#)
                        String modID = (domainID + "_m" + m_counter_variable).toString();

                            // The rest will be assumed to be variable modifications.
                            VariableModification varMod = new VariableModification(modName, modMass, modLocation, m_counter);

                            // Put the modification into the map, value is the mod id.
                            iVarModificationMap.put(modID, varMod);
                            m_counter_variable++;

                        } else {

                        // Get a specific id for the modification (domainID)_m(modifcation#)
                        String modID = (domainID + "_m" + m_counter_variable).toString();

                             // The rest will be assumed to be variable modifications.
                            VariableModification varMod = new VariableModification(modName, modMass, modLocation, m_counter);

                            // Put the modification into the map, value is the mod id.
                            iVarModificationMap.put(modID, varMod);
                            m_counter_variable++;
                        }
                        m_counter++;
                    }
                }
            }
        }
    }

    /**
     * Checks if a given modification mass is given in the fixed modification input parameter section:
     * --> label="residue, modification mass">
     * @param aModMass
     * @return boolean
     */
    private boolean isFixedModificationInput(double aModMass){
            BigDecimal modMass = new BigDecimal(aModMass);
            modMass = modMass.setScale(2,BigDecimal.ROUND_HALF_UP);
            String modificationMasses = iInputParams.getResidueModMass();
            StringTokenizer tokenizer = new StringTokenizer(modificationMasses, ",");
            while (tokenizer.hasMoreTokens()){
                String[] tokens = tokenizer.nextToken().split("@");
                BigDecimal inputMass = new BigDecimal(new Double(tokens[0]));
                inputMass = inputMass.setScale(2,BigDecimal.ROUND_HALF_UP);                
                if (modMass.equals(inputMass)) return true;
            }
        return false;
    }

    /**
     * Checks if a given modification mass is given in the variable modification input parameter section:
     * --> label="residue, potential modification mass">
     * @param aModMass
     * @return boolean
     */
    private boolean isVariableModificationInput(double aModMass){
            BigDecimal modMass = new BigDecimal(aModMass);
            modMass = modMass.setScale(3,BigDecimal.ROUND_HALF_UP);
            String modificationMasses = iInputParams.getResiduePotModMass();
            StringTokenizer tokenizer = new StringTokenizer(modificationMasses, ",");
            while (tokenizer.hasMoreTokens()){
                String[] tokens = tokenizer.nextToken().split("@");
                BigDecimal inputMass = new BigDecimal(new Double(tokens[0]));
                inputMass = inputMass.setScale(3,BigDecimal.ROUND_HALF_UP);               
                if (modMass.equals(inputMass)) return true;
            }
        return false;
    }

    /**
     * Returns the fixed modifications as list.
     *
     * @param aDomainID The domainID of the identification
     * @return modificationsList The fixed modification list
     */
    public ArrayList<Modification> getFixedModifications(String aDomainID) {
        ArrayList<Modification> modificationList = new ArrayList<Modification>();
        if (iFixedModificationMap != null) {
            int modCount = 1;
            while (iFixedModificationMap.get(aDomainID + "_m" + modCount) != null) {
                modificationList.add(iFixedModificationMap.get(aDomainID + "_m" + modCount));
                modCount++;
            }
        }
        return modificationList;
    }

    /**
     * Returns an arrayList of all the fixed modifications in the file.
     * @return fixedModList
     */
    public ArrayList<Modification> getAllFixedModifications(){
        ArrayList<Modification> fixedModList = new ArrayList<Modification>();
        for(Map.Entry<String, Modification> e : iFixedModificationMap.entrySet()){
            fixedModList.add(e.getValue());
        }
        return fixedModList;
    }

      /**
     * Returns an arrayList of all the variable modifications in the file.
     * @return varModList ArrayList<Modification>
     */
    public ArrayList<Modification> getAllVariableModifications(){
        ArrayList<Modification> varModList = new ArrayList<Modification>();
        for(Map.Entry<String, Modification> e : iVarModificationMap.entrySet()){
            varModList.add(e.getValue());
        }
        return varModList;
    }
    
    /**
     * Returns the variable modifications as list.
     *
     * @param aDomainID The domainID of the identification
     * @return modificationsList The variable modification list
     */
    public ArrayList<Modification> getVariableModifications(String aDomainID) {
        ArrayList<Modification> modificationList = new ArrayList<Modification>();
        if (iVarModificationMap != null) {
            int modCount = 1;
            while (iVarModificationMap.get(aDomainID + "_m" + modCount) != null) {
                modificationList.add(iVarModificationMap.get(aDomainID + "_m" + modCount));
                modCount++;
            }
        }
        return modificationList;
    }
}
