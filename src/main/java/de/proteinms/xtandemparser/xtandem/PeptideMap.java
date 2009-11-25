package de.proteinms.xtandemparser.xtandem;

import de.proteinms.xtandemparser.interfaces.Ion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class holds the peptide informatin in a map.
 * 
 * @author Thilo Muth
 */
public class PeptideMap implements Serializable {

    /**
     * This variable holds the second dimension hash map for the peptide object as value.
     */
    private HashMap<String, Peptide> iPeptideMap = null;
    /**
     * This variable holds the first dimension hash map for the peptide hash map as value.
     */
    private HashMap<String, HashMap> iSpectrumAndPeptideMap = null;

    /**
     * Builds the peptide map
     *
     * @param aRawPeptideMap
     * @param aProteinMap
     * @param aNumberOfSpectra
     */
    public PeptideMap(HashMap aRawPeptideMap, ProteinMap aProteinMap, int aNumberOfSpectra) {
        buildPeptideMap(aRawPeptideMap, aProteinMap, aNumberOfSpectra);
    }

    /**
     * Constructs the 2-dim hashmap, the first dimension is the map with the spectrum-number 
     * as key and another hash map as value. The second dimension has the peptideID (e.g.
     * s171_p2 for spectrum number 171 and the second peptide) and the peptide object as value.
     *
     * @param aRawPeptideMap
     * @param aProteinMap
     * @param aNumberOfSpectra
     * @return spectrumAndPeptideMap
     */
    private HashMap buildPeptideMap(HashMap aRawPeptideMap, ProteinMap aProteinMap, int aNumberOfSpectra) {

        // First dimension of the map, which contains the spectra as key and the peptide hash maps as values
        iSpectrumAndPeptideMap = new HashMap(aNumberOfSpectra);

        // Hashmap for the peptide objects
        iPeptideMap = new HashMap<String, Peptide>();

        if (aRawPeptideMap != null) {
            for (int i = 1; i <= aNumberOfSpectra; i++) {

                // The counter for the peptides
                int counter = 1;

                // Check if there are any values given out of the map
                while (aRawPeptideMap.get("s" + i + "_p" + counter) != null) {

                    // The peptide id is consists of s + spectrum# + _p + peptide#
                    String peptideID = ("s" + i + "_p" + counter).toString();
                    int peptideStart = Integer.parseInt(aRawPeptideMap.get("start" + "_s" + i + "_p" + counter).toString());
                    int peptideEnd = Integer.parseInt(aRawPeptideMap.get("end" + "_s" + i + "_p" + counter).toString());
                    String sequence = aRawPeptideMap.get("seq" + "_s" + i + "_p" + counter).toString().trim();

                    // Create an instance of a protein.
                    Peptide peptide = new Peptide(peptideID, peptideStart, peptideEnd, sequence);

                    // Set the domain values
                    peptide.setSpectrumNumber(i);
                    peptide.setDomainID(aRawPeptideMap.get("domainid" + "_s" + i + "_p" + counter).toString());
                    peptide.setDomainStart(Integer.parseInt(aRawPeptideMap.get("domainstart" + "_s" + i + "_p" + counter).toString()));
                    peptide.setDomainEnd(Integer.parseInt(aRawPeptideMap.get("domainend" + "_s" + i + "_p" + counter).toString()));
                    peptide.setDomainExpect(Double.parseDouble(aRawPeptideMap.get("expect" + "_s" + i + "_p" + counter).toString()));
                    peptide.setDomainMh(Double.parseDouble(aRawPeptideMap.get("mh" + "_s" + i + "_p" + counter).toString()));
                    peptide.setDomainDeltaMh(Double.parseDouble(aRawPeptideMap.get("delta" + "_s" + i + "_p" + counter).toString()));
                    peptide.setDomainHyperScore(Double.parseDouble(aRawPeptideMap.get("hyperscore" + "_s" + i + "_p" + counter).toString()));
                    peptide.setDomainNextScore(Double.parseDouble(aRawPeptideMap.get("nextscore" + "_s" + i + "_p" + counter).toString()));
                    ArrayList<Ion> ionList = new ArrayList<Ion>();


//                        if(aRawPeptideMap.get("a_score" + "_s" + i +"_p" + counter) != null ){
//                            double aScore = Double.parseDouble(aRawPeptideMap.get("a_score" + "_s" + i +"_p" + counter).toString());
//		    	            int aNumber = Integer.parseInt(aRawPeptideMap.get("a_ions" + "_s" + i +"_p" + counter).toString());
//                            Ion aIon = new Ion (aNumber, aScore);
//                            aIon.setType(interfaces.aION_TYPE);
//                            ionList.add(aIon);
//                        }
//
//                        if (aRawPeptideMap.get("b_score" + "_s" + i +"_p" + counter) != null){
//                            double bScore = Double.parseDouble(aRawPeptideMap.get("b_score" + "_s" + i +"_p" + counter).toString());
//		    	            int bNumber = Integer.parseInt(aRawPeptideMap.get("b_ions" + "_s" + i +"_p" + counter).toString());
//                            Ion bIon = new Ion (bNumber, bScore);
//                            bIon.setType(interfaces.bION_TYPE);
//                            ionList.add(bIon);
//                        }
//
//                        if(aRawPeptideMap.get("c_score" + "_s" + i +"_p" + counter) != null){
//                            double cScore = Double.parseDouble(aRawPeptideMap.get("c_score" + "_s" + i +"_p" + counter).toString());
//		    	            int cNumber = Integer.parseInt(aRawPeptideMap.get("c_ions" + "_s" + i +"_p" + counter).toString());
//                            Ion cIon = new Ion (cNumber, cScore);
//                            cIon.setType(interfaces.cION_TYPE);
//                            ionList.add(cIon);
//                        }
//
//                        if(aRawPeptideMap.get("x_score" + "_s" + i +"_p" + counter) != null){
//                            double xScore = Double.parseDouble(aRawPeptideMap.get("x_score" + "_s" + i +"_p" + counter).toString());
//		    	            int xNumber = Integer.parseInt(aRawPeptideMap.get("x_ions" + "_s" + i +"_p" + counter).toString());
//                            Ion xIon = new Ion (xNumber, xScore);
//                            xIon.setType(interfaces.xION_TYPE);
//                            ionList.add(xIon);
//                        }
//
//                        if(aRawPeptideMap.get("y_score" + "_s" + i +"_p" + counter) != null){
//                            double yScore = Double.parseDouble(aRawPeptideMap.get("y_score" + "_s" + i +"_p" + counter).toString());
//		    	            int yNumber = Integer.parseInt(aRawPeptideMap.get("y_ions" + "_s" + i +"_p" + counter).toString());
//                            Ion yIon = new Ion (yNumber, yScore);
//                            yIon.setType(interfaces.yION_TYPE);
//                            ionList.add(yIon);
//                        }
//
//                        if(aRawPeptideMap.get("z_score" + "_s" + i +"_p" + counter) != null){
//                            double zScore = Double.parseDouble(aRawPeptideMap.get("z_score" + "_s" + i +"_p" + counter).toString());
//		    	            int zNumber = Integer.parseInt(aRawPeptideMap.get("z_ions" + "_s" + i +"_p" + counter).toString());
//                            Ion zIon = new Ion (zNumber, zScore);
//                            zIon.setType(interfaces.zION_TYPE);
//                            ionList.add(zIon);
//                        }

                    peptide.setIons(ionList);
                    peptide.setUpFlankSequence(aRawPeptideMap.get("pre" + "_s" + i + "_p" + counter).toString());
                    peptide.setDownFlankSequence(aRawPeptideMap.get("post" + "_s" + i + "_p" + counter).toString());
                    peptide.setDomainSequence(aRawPeptideMap.get("domainseq" + "_s" + i + "_p" + counter).toString());
                    peptide.setMissedCleavages(Integer.parseInt(aRawPeptideMap.get("missed_cleavages" + "_s" + i + "_p" + counter).toString()));

                    // Put the peptide into the map, value is the id.
                    iPeptideMap.put(peptideID, peptide);

                    // Update the PeptideHit in the ProteinMap.
//	                    PeptideHit lPeptideHit = (PeptideHit) lSecondDimension.get(lCount - 1);
//	                    int lNumberOfProteinHits = lPeptideHit.getProteinHits().size();
//	                    for (int k = 0; k < lNumberOfProteinHits; k++) {
//	                        ProteinHit lProteinHit = (ProteinHit) lPeptideHit.getProteinHits().get(k);
//	                        aProteinMap.addProteinSource(lProteinHit.getAccession(), i, lCount);
//	                    }
                    counter++;
                }

                iSpectrumAndPeptideMap.put("s" + i, iPeptideMap);
            }
        }
        return iSpectrumAndPeptideMap;
    }

    /**
     * Returns the 2-dim spectrum and peptide map.
     *
     * @return iSpectrumAndPeptideMap HashMap
     */
    public HashMap<String, HashMap> getSpectrumAndPeptideMap() {
        return iSpectrumAndPeptideMap;
    }

    /**
     * Retrieve all possible peptide objects for a given spectrum.
     *
     * @param aSpectrumNumber
     * @return peptideList ArrayList
     */
    public ArrayList<Peptide> getAllPeptides(int aSpectrumNumber) {
        ArrayList<Peptide> peptideList = new ArrayList<Peptide>();
        HashMap<String, Peptide> peptideMap = iSpectrumAndPeptideMap.get("s" + aSpectrumNumber);
        int pepCount = 1;
        while (peptideMap.get("s" + aSpectrumNumber + "_p" + pepCount) != null) {
            peptideList.add(peptideMap.get("s" + aSpectrumNumber + "_p" + pepCount));
            pepCount++;
        }
        return peptideList;
    }

    /**
     * Returns a specific peptide by an index.
     *
     * @param aSpectrumNumber
     * @param index
     * @return peptide Peptide
     */
    public Peptide getPeptideByIndex(int aSpectrumNumber, int index) {
        ArrayList<Peptide> peptideList = this.getAllPeptides(aSpectrumNumber);
        Peptide peptide = null;
        if (peptideList.get(index - 1) != null) {
            peptide = peptideList.get(index - 1);
        }
        return peptide;
    }

    /**
     * Returns the number of peptides for a given spectrum
     *
     * @param aSpectrumNumber
     * @return The total number of peptides
     */
    public int getNumberOfPeptides(int aSpectrumNumber) {
        return this.getAllPeptides(aSpectrumNumber).size();
    }
}
