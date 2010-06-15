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
                    peptide.setUpFlankSequence(aRawPeptideMap.get("pre" + "_s" + i + "_p" + counter).toString());
                    peptide.setDownFlankSequence(aRawPeptideMap.get("post" + "_s" + i + "_p" + counter).toString());
                    peptide.setDomainSequence(aRawPeptideMap.get("domainseq" + "_s" + i + "_p" + counter).toString());
                    peptide.setMissedCleavages(Integer.parseInt(aRawPeptideMap.get("missed_cleavages" + "_s" + i + "_p" + counter).toString()));

                    // Put the peptide into the map, value is the id.
                    iPeptideMap.put(peptideID, peptide);

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
