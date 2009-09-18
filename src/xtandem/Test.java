package xtandem;

import interfaces.Modification;
import interfaces.Peak;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import parser.MgfFileParser;

/**
 * Do JUnit tests instead of this class!
 * @author Thilo Muth
 *
 */
public class Test {

	public Test(){

	}

	public static void main(String[] args) {
        XTandemFile xtandemfile = new XTandemFile("C:\\XTandem\\output.xml", "C:\\XTandem\\control.RAW.mgf");
        MgfFileParser mgfParser= new MgfFileParser("C:\\XTandem\\control.RAW.mgf", xtandemfile.getXTandemParser().getTitle2SpectrumIDMap(), xtandemfile.getPeptideMap());

        HashMap<Integer, MgfPeaklist> map = mgfParser.getPeakListMap();
        ModificationMap modMap = xtandemfile.getModificationMap();

        for (int i = 1; i <= mgfParser.getSpectraNumber(); i++){
            MgfPeaklist peaklist = map.get(i);
            if (peaklist.isIdentfied()){
                System.out.println("identified nr.: " + i);
                System.out.println(peaklist.getIdentifiedSpectrumNumber());

                ArrayList<Modification> modList = modMap.getVariableModifications(peaklist.getIdentifiedSpectrumNumber());
                for (Modification mod : modList) {
                    System.out.println("domainID: " + mod.getDomainID());
                    System.out.println("modName: " + mod.getName());
                    System.out.println("modLocation: " + mod.getLocation());
                    System.out.println("modMass: " + mod.getMass());
                    System.out.println("modFixed:" + mod.isFixed());
                }
                ArrayList<Peptide> peptides = peaklist.getIdentifiedPeptides();
                for (Peptide peptide : peptides) {
                    System.out.println(peptide.getDomainID());
                    System.out.println(peptide.getDomainSequence());
                }

            }
        }

        System.out.println("spectra number: " + mgfParser.getSpectraNumber());


        MgfPeaklist peaklist = map.get(7382);

        System.out.println("mstype of 7382: " + peaklist.getMSType());
        System.out.println("title of 7382: " + peaklist.getTitle());
        System.out.println("pepmass of 7382: " + peaklist.getPepmass());

        ArrayList<MgfPeak> peaks = peaklist.getPeaks();

        for (Peak peak : peaks) {
            System.out.print(peak.getMZ() + " ");
        } System.out.println();
        for (Peak peak : peaks) {
            System.out.print(peak.getIntensity() + " ");
        }

        System.out.println();



        ArrayList<Spectrum> spectrumList = xtandemfile.getSpectraList();

        // Test the number of spectra
        int specNumber = xtandemfile.getXTandemParser().getNumberOfSpectra();
        System.out.println("identfied spectra" + specNumber);

        // Test the first spectrum
        Spectrum spectrum_first = spectrumList.get(0);
        System.out.println("-----First Spectrum------");
        System.out.println(spectrum_first.getSpectrumId());
        System.out.println(spectrum_first.getPrecursorMh());
        System.out.println(spectrum_first.getPrecursorCharge());
        System.out.println(spectrum_first.getExpectValue());
        System.out.println(spectrum_first.getLabel());
        System.out.println(spectrum_first.getSummedScore());
        System.out.println(spectrum_first.getMaxFragIonIntensity());
        System.out.println(spectrum_first.getIntensityMultiplier());
        System.out.println();

        // Test the last spectrum
        Spectrum spectrum_last = spectrumList.get(specNumber - 1);
        System.out.println("-----Last Spectrum------");
        System.out.println(spectrum_last.getSpectrumId());
        System.out.println(spectrum_last.getPrecursorMh());
        System.out.println(spectrum_last.getPrecursorCharge());
        System.out.println(spectrum_last.getExpectValue());
        System.out.println(spectrum_last.getLabel());
        System.out.println(spectrum_last.getSummedScore());
        System.out.println(spectrum_last.getMaxFragIonIntensity());
        System.out.println(spectrum_last.getIntensityMultiplier());
        System.out.println();

        // Test the proteins
        ProteinMap protMap = xtandemfile.getProteinMap();
        Iterator keyIter = protMap.getProteinIDIterator();

        while(keyIter.hasNext()){
            String keyID = keyIter.next().toString();

            // Get a protein instance
            Protein protein = protMap.getProtein(keyID);
            System.out.println("ID: " + protein.getID());
            System.out.println("UID: " + protein.getUID());
            System.out.println("Label: " + protein.getLabel());
            System.out.println("ExpectValue: " + protein.getExpectValue());
            System.out.println("Summed Score: " + protein.getSummedScore());
        }

        // Test the peptides
        PeptideMap pepMap = xtandemfile.getPeptideMap();

        ArrayList<Peptide> pepList = pepMap.getAllPeptides(1);

        for (Peptide peptide : pepList) {
            System.out.println("start: " + peptide.getStart());
            System.out.println("end: " + peptide.getEnd());
            System.out.println("sequence: " + peptide.getSequence());
            System.out.println("domainid: " + peptide.getDomainID());
            System.out.println("domainstart: " + peptide.getDomainStart());
            System.out.println("domainend: " + peptide.getDomainEnd());
            System.out.println("domainExpect: " + peptide.getDomainExpect());
            System.out.println("domainMh: " + peptide.getDomainMh());
            System.out.println("domainDeltaMh: " + peptide.getDomainDeltaMh());
            System.out.println("domainHyperScore: " + peptide.getDomainHyperScore());
            System.out.println("domainNextScore: " + peptide.getDomainNextScore());
            ArrayList<Ion> ions = peptide.getIons();

            for(Ion ion : ions){
                System.out.println("Ion type: " + ion.getType());
                System.out.println("Score: " + ion.getIonScore());
                System.out.println("Ions: " + ion.getIonNumber());
            }

            System.out.println("pre: " + peptide.getUpFlankSequence());
            System.out.println("post: " + peptide.getDownFlankSequence());
            System.out.println("domain sequence: " + peptide.getDomainSequence());
            System.out.println("missed_cleav: " + peptide.getMissedCleavages());
        }



        PerformParams performParams = xtandemfile.getPerformParameters();
        System.out.println(performParams.getSequenceSource_1());
        System.out.println(performParams.getSequenceSource_2());
        System.out.println(performParams.getSequenceSource_3());
        System.out.println(performParams.getSequenceSourceDescription_1());
        System.out.println(performParams.getSequenceSourceDescription_2());
        System.out.println(performParams.getSequenceSourceDescription_3());
        System.out.println(performParams.getEstFalsePositives());
        System.out.println(performParams.getSpectrumNoiseSuppressionRatio());
        System.out.println(performParams.getTotalPeptidesUsed());
        System.out.println(performParams.getTotalProteinsUsed());
        System.out.println(performParams.getTotalSpectraAssigned());
        System.out.println(performParams.getTotalSpectraUsed());
        System.out.println(performParams.getTotalUniqueAssigned());
        System.out.println(performParams.getProcStartTime());
        System.out.println(performParams.getProcVersion());
        System.out.println(performParams.getQualityValues());
        System.out.println(performParams.getInputModelNumber());
        System.out.println(performParams.getInputSpectraNumber());
        System.out.println(performParams.getPartialCleavageNumber());
        System.out.println(performParams.getPointMutationsNumber());
        System.out.println(performParams.getPotentialC_termNumber());
        System.out.println(performParams.getPotentialN_termNumber());
        System.out.println(performParams.getUnanticipatedCleavageNumber());
        System.out.println(performParams.getInitModelTotalTiming());
        System.out.println(performParams.getInitModelSpecTiming());
        System.out.println(performParams.getLoadSeqModelsTiming());
        System.out.println(performParams.getRefinementTiming());

        InputParams inputParams = xtandemfile.getInputParameters();
        System.out.println(inputParams.getIDefaultParamPath());
        System.out.println(inputParams.getTaxonomyInfoPath());
        System.out.println(inputParams.getHistogramColWidth());
        System.out.println(inputParams.isHistogramExist());
        System.out.println(inputParams.getLogPath());
        System.out.println(inputParams.getMaxValidExpectValue());
        System.out.println(inputParams.getOutputMessage());
        System.out.println(inputParams.isOneSequenceCopy());
        System.out.println(inputParams.isOutputParams());
        System.out.println(inputParams.getOutputPath());
        System.out.println(inputParams.isOutputPathHash());
        System.out.println(inputParams.isOutputPerformance());
        System.out.println(inputParams.isOutputProteins());
        System.out.println(inputParams.getOutputResults());
        System.out.println(inputParams.getOutputSequencePath());
        System.out.println(inputParams.isOutputSequences());
        System.out.println(inputParams.getOutputSortResults());
        System.out.println(inputParams.isOutputSpectra());
        System.out.println(inputParams.getOutputXslPath());
        System.out.println(inputParams.getProteinC_termResModMass());
        System.out.println(inputParams.getProteinN_termResModMass());
        System.out.println(inputParams.getProteinC_termCleavMassChange());
        System.out.println(inputParams.getProteinN_termCleavMassChange());
        System.out.println(inputParams.getProteinCleavageSite());
        System.out.println(inputParams.isProteinHomologManagement());
        System.out.println(inputParams.getProteinModifiedResMassFile());
        System.out.println(inputParams.getProteinTaxon());
        System.out.println(inputParams.isRefine());
        System.out.println(inputParams.getRefineMaxValidExpectValue());
        System.out.println(inputParams.getRefineModMass());
        System.out.println(inputParams.isPointMutations());
        System.out.println(inputParams.getRefinePotC_termMods());
        System.out.println(inputParams.getRefinePotN_termMods());
        System.out.println(inputParams.getRefinePotModMass());
        System.out.println(inputParams.getRefinePotModMotif());
        System.out.println(inputParams.getRefineSequencePath());
        System.out.println(inputParams.isRefineSpectrumSynthesis());
        System.out.println(inputParams.getRefineTicPercent());
        System.out.println(inputParams.isRefineUnanticipatedCleavage());
        System.out.println(inputParams.isRefineUsePotentialModsForFullRefinement());
        System.out.println(inputParams.getResidueModMass());
        System.out.println(inputParams.getResiduePotModMass());
        System.out.println(inputParams.getResiduePotModMotiv());
        System.out.println(inputParams.isScoring_aIons());
        System.out.println(inputParams.isScoring_bIons());
        System.out.println(inputParams.isScoring_cIons());
        System.out.println(inputParams.isScoringCyclicPerm());
        System.out.println(inputParams.isScoringIncReverse());
        System.out.println(inputParams.getScoringMissCleavageSites());
        System.out.println(inputParams.getScoringMinIonCount());
        System.out.println(inputParams.isScoring_xIons());
        System.out.println(inputParams.isScoring_yIons());
        System.out.println(inputParams.isScoring_zIons());
        System.out.println(inputParams.getSpectrumDynRange());
        System.out.println(inputParams.getSpectrumFragMassType());
        System.out.println(inputParams.getSpectrumMonoIsoMassError());
        System.out.println(inputParams.getSpectrumMonoIsoMassErrorUnits());
        System.out.println(inputParams.getSpectrumMaxPrecursorCharge());
        System.out.println(inputParams.getSpectrumMinFragMz());
        System.out.println(inputParams.getSpectrumMinPrecursorMz());
        System.out.println(inputParams.getSpectrumMinPeaks());
        System.out.println(inputParams.getSpectrumParentMonoIsoMassErrorMinus());
        System.out.println(inputParams.getSpectrumParentMonoIsoMassErrorPlus());
        System.out.println(inputParams.getSpectrumParentMonoIsoMassErrorUnits());
        System.out.println(inputParams.isSpectrumParentMonoIsoMassIsoError());
        System.out.println(inputParams.getSpectrumPath());
        System.out.println(inputParams.getSpectrumSeqBatchSize());
        System.out.println(inputParams.getSpectrumThreads());
        System.out.println(inputParams.getSpectrumTotalPeakNumber());
        System.out.println(inputParams.isSpectrumUseNoiseCompression());
				
		}
}
