package de.proteinms.xtandemparser.xtandem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class holds the protein information in a map.
 *
 * @author Thilo Muth
 */
public class ProteinMap implements Serializable {

    /**
     * This map has as key the proteinID and as value the protein.
     */
    private HashMap<String, Protein> iProteinMap = null;

    /**
     * The constructor gets a raw protein map from the xtandem parser.
     *
     * @param aProteinIDList
     * @param aRawProteinMap
     */
    public ProteinMap(ArrayList<String> aProteinIDList, HashMap aRawProteinMap) {
        if (aProteinIDList != null && aRawProteinMap != null) {
            initMap(aProteinIDList, aRawProteinMap);
        }
    }

    /**
     * This method initializes the protein map from a given raw map.
     *
     * @param aProteinIDList
     * @param aProteinMap
     */
    private void initMap(ArrayList<String> aProteinIDList, HashMap<String, String> aProteinMap) {
        iProteinMap = new HashMap<String, Protein>();

        // Iterate over the protein id list
        if (aProteinIDList != null) {
            Iterator iter = aProteinIDList.iterator();
            while (iter.hasNext()) {
                // Get the proteinID
                String proteinID = iter.next().toString();

                // Get the rest of the protein parameters for the protein map.
                String proteinUID = aProteinMap.get("uid" + proteinID).toString();
                String proteinLabel = aProteinMap.get("label" + proteinID).toString();
                String proteinDescription;
                if (aProteinMap.get("description" + proteinID) != null) {
                    proteinDescription = aProteinMap.get("description" + proteinID).toString();
                } else {
                    proteinDescription = proteinLabel;
                }

                Double expectValue;

                try {
                    expectValue = Double.parseDouble(aProteinMap.get("expect" + proteinID).toString());
                } catch (NumberFormatException e) {
                    expectValue = null;
                }

                Double summedScore = Double.parseDouble(aProteinMap.get("sumI" + proteinID).toString());

                // Create an instance of a protein.
                Protein protein = new Protein(proteinID, proteinUID, proteinLabel, proteinDescription, expectValue, summedScore);

                // Put the protein into the map, value is the id.
                iProteinMap.put(proteinID, protein);
            }
        }
    }

    /**
     * Returns a protein object for a given protein id.
     *
     * @param aProteinID String
     * @return iProtein Protein
     */
    public Protein getProtein(String aProteinID) {
        if (iProteinMap != null) {
            return iProteinMap.get(aProteinID);
        } else {
            return null;
        }
    }

    /**
     * This method returns an iterator of the keys in the protein map.
     *
     * @return iProteinMap.keySet().iterator()
     */
    public Iterator getProteinIDIterator() {
        return iProteinMap.keySet().iterator();
    }
}
