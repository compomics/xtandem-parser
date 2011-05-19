package de.proteinms.xtandemparser.xtandem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class holds the peptide informatin in a map.
 * 
 * @author Thilo Muth
 */
public class PeptideMap implements Serializable {

    /**
     * This variable holds the first dimension hash map for the peptide hash map as value.
     */
    private HashMap<String, HashMap<String, Peptide>> iSpectrumAndPeptideMap = null;

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

        if (aRawPeptideMap != null) {
            for (int i = 1; i <= aNumberOfSpectra; i++) {

        // Hashmap for the peptide objects
        HashMap<String, Peptide> lPeptideMap = new HashMap<String, Peptide>();

                // The counter for the peptides
                int pCount = 1;

                // Check if there are any values given out of the map
                while (aRawPeptideMap.get("s" + i + "_p" + pCount) != null) {

                    // The peptide id is consists of s + spectrum# + _p + peptide#
                    String peptideID = ("s" + i + "_p" + pCount).toString();
                    int peptideStart = Integer.parseInt(aRawPeptideMap.get("start" + "_s" + i + "_p" + pCount).toString());
                    int peptideEnd = Integer.parseInt(aRawPeptideMap.get("end" + "_s" + i + "_p" + pCount).toString());
                    String sequence = aRawPeptideMap.get("seq" + "_s" + i + "_p" + pCount).toString().trim();

                    // Create an instance of a protein.
                    Peptide peptide = new Peptide(peptideID, peptideStart, peptideEnd, sequence);
                    // Set the domain values
                    peptide.setSpectrumNumber(i);
                    // set the fasta filename
                    if (aRawPeptideMap.get("URL" + pCount) != null) {
                        peptide.setFastaFilePath(aRawPeptideMap.get("URL" + pCount).toString());
                    }

                    // The counter for the domains
                    int dCount = 1;

                    // List of the domains
                    List<Domain> domainList = new ArrayList<Domain>();
                    while (aRawPeptideMap.get("s" + i + "_p" + pCount + "_d" + dCount) != null) {
                        Domain domain = new Domain();
                        domain.setDomainID(aRawPeptideMap.get("domainid" + "_s" + i + "_p" + pCount + "_d" + dCount).toString());
                        domain.setDomainStart(Integer.parseInt(aRawPeptideMap.get("domainstart" + "_s" + i + "_p" + pCount + "_d" + dCount).toString()));
                        domain.setDomainEnd(Integer.parseInt(aRawPeptideMap.get("domainend" + "_s" + i + "_p" + pCount + "_d" + dCount).toString()));
                        domain.setDomainExpect(Double.parseDouble(aRawPeptideMap.get("expect" + "_s" + i + "_p" + pCount + "_d" + dCount).toString()));
                        domain.setDomainMh(Double.parseDouble(aRawPeptideMap.get("mh" + "_s" + i + "_p" + pCount + "_d" + dCount).toString()));
                        domain.setDomainDeltaMh(Double.parseDouble(aRawPeptideMap.get("delta" + "_s" + i + "_p" + pCount + "_d" + dCount).toString()));
                        domain.setDomainHyperScore(Double.parseDouble(aRawPeptideMap.get("hyperscore" + "_s" + i + "_p" + pCount + "_d" + dCount).toString()));
                        domain.setDomainNextScore(Double.parseDouble(aRawPeptideMap.get("nextscore" + "_s" + i + "_p" + pCount + "_d" + dCount).toString()));
                        domain.setUpFlankSequence(aRawPeptideMap.get("pre" + "_s" + i + "_p" + pCount + "_d" + dCount).toString());
                        domain.setDownFlankSequence(aRawPeptideMap.get("post" + "_s" + i + "_p" + pCount + "_d" + dCount).toString());
                        domain.setDomainSequence(aRawPeptideMap.get("domainseq" + "_s" + i + "_p" + pCount + "_d" + dCount).toString());
                        domain.setMissedCleavages(Integer.parseInt(aRawPeptideMap.get("missed_cleavages" + "_s" + i + "_p" + pCount + "_d" + dCount).toString()));
                        domainList.add(domain);
                        dCount++;
                    }

                    // Set the domains for the peptide
                    peptide.setDomains(domainList);

                    // Put the peptide into the map, value is the domain id.
                    lPeptideMap.put(peptideID, peptide);
                    pCount++;
                }
                iSpectrumAndPeptideMap.put("s" + i, lPeptideMap);
            }
        }
        return iSpectrumAndPeptideMap;
    }

    /**
     * Returns the 2-dim spectrum and peptide map.
     *
     * @return iSpectrumAndPeptideMap HashMap
     */
    public HashMap<String, HashMap<String, Peptide>> getSpectrumAndPeptideMap() {
        return iSpectrumAndPeptideMap;
    }

    /**
     * Retrieve all possible peptide objects for a given spectrum.
     *
     * @param aSpectrumNumber
     * @return peptideList ArrayList
     */
    public ArrayList<Peptide> getAllPeptides(int aSpectrumNumber) {
        return new ArrayList<Peptide>(iSpectrumAndPeptideMap.get("s" + aSpectrumNumber).values());
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
        return iSpectrumAndPeptideMap.get("s" + aSpectrumNumber).size();
    }
}
