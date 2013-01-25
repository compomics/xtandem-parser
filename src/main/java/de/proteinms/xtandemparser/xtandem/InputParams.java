package de.proteinms.xtandemparser.xtandem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class holds information of the given input parameters.
 *
 * @author Thilo Muth
 */
public class InputParams implements Serializable {

    // Input parameters
    private String iDefaultParamPath = null;
    private String iTaxonomyInfoPath = null;
    private String iHistogramColWidth = null;
    private boolean iHistogramExist = false;
    private String iLogPath = null;
    private double iMaxValidExpectValue;
    private String iOutputMessage = null;
    private boolean iOneSequenceCopy = false;
    private boolean iOutputParams = false;
    private String iOutputPath = null;
    private boolean iOutputPathHash = false;
    private boolean iOutputPerformance = false;
    private boolean iOutputProteins = false;
    private String iOutputResults = null;
    private String iOutputSequencePath = null;
    private boolean iOutputSequences = false;
    private String iOutputSortResults = null;
    private boolean iOutputSpectra = false;
    private String iOutputXslPath = null;
    private double iProteinC_termResModMass;
    private double iProteinN_termResModMass;
    private String iProteinC_termCleavMassChange = null;
    private String iProteinN_termCleavMassChange = null;
    private String iProteinCleavageSite = null;
    private boolean iProteinHomologManagement = false;
    private String iProteinModifiedResMassFile = null;
    private String iProteinTaxon = null;
    private boolean iRefine = false;
    private double iRefineMaxValidExpectValue;
    private boolean iPointMutations = false;
    private String iRefinePotC_termMods = null;
    private String iRefinePotN_termMods = null;
    private String iRefinePotModMass = null;
    private String iRefinePotModMotif = null;
    private String iRefineSequencePath = null;
    private boolean iRefineSpectrumSynthesis = false;
    private double iRefineTicPercent;
    private boolean iRefineUnanticipatedCleavage = false;
    private boolean iRefineUsePotentialModsForFullRefinement = false;
    private String iResidueModMass = null;
    private String iResiduePotModMass = null;
    private String iResiduePotModMotiv = null;
    private boolean iScoring_aIons = false;
    private boolean iScoring_bIons = true;
    private boolean iScoring_cIons = false;
    private boolean iScoringCyclicPerm = false;
    private boolean iScoringIncReverse = false;
    private int iScoringMissCleavageSites;
    private int iScoringMinIonCount;
    private boolean iScoring_xIons = false;
    private boolean iScoring_yIons = true;
    private boolean iScoring_zIons = false;
    private boolean iScoringPlugScoring = false;
    private double iSpectrumDynRange;
    private String iSpectrumFragMassType = null;
    private String iScoringAlgorithm = null;
    private double iSpectrumMonoIsoMassError;
    private String iSpectrumMonoIsoMassErrorUnits = null;
    private int iSpectrumMaxPrecursorCharge;
    private double iSpectrumMinFragMz;
    private double iSpectrumMinPrecursorMz;
    private int iSpectrumMinPeaks;
    private double iSpectrumParentMonoIsoMassErrorMinus;
    private double iSpectrumParentMonoIsoMassErrorPlus;
    private String iSpectrumParentMonoIsoMassErrorUnits = null;
    private boolean iSpectrumParentMonoIsoMassIsoError = false;
    // Path to the original spectrum file
    private String iSpectrumPath = null;
    private int iSpectrumSeqBatchSize;
    private int iSpectrumThreads;
    private int iSpectrumTotalPeakNumber;
    private boolean iSpectrumUseNoiseCompression = false;
    /**
     * Holds the values of residue, modification mass [1-n]
     */
    private ArrayList<String> residueModificationMass = new ArrayList<String>();
    /**
     * Holds the values of refine, potential modification mass [1-n]
     */
    private ArrayList<String> refinePotentialModificationMass = new ArrayList<String>();
    /**
     * Holds the values of refine, potential modification motif [1-n]
     */
    private ArrayList<String> refinePotentialModificationMotif = new ArrayList<String>();
    /**
     * Holds the values of residue, modification mass.
     */
    private String refineModificationMass;

    /**
     * Constructor for the input parameters section.
     *
     * @param map
     */
    public InputParams(HashMap map) {
        if (map.get("DEFAULTPARAMPATH") != null) {
            iDefaultParamPath = map.get("DEFAULTPARAMPATH").toString();
        }
        if (map.get("TAXONOMYINFOPATH") != null) {
            iTaxonomyInfoPath = map.get("TAXONOMYINFOPATH").toString();
        }
        if (map.get("HISTOCOLWIDTH") != null) {
            iHistogramColWidth = map.get("HISTOCOLWIDTH").toString();
        }
        if (map.get("HISTOEXIST") != null) {
            iHistogramExist = convertStringToBool(map.get("HISTOEXIST").toString());
        }
        if (map.get("LOGPATH") != null) {
            iLogPath = map.get("LOGPATH").toString();
        }
        if (map.get("MAXVALIDEXPECT") != null) {
            iMaxValidExpectValue = Double.parseDouble(map.get("MAXVALIDEXPECT").toString());
        }
        if (map.get("OUTPUTMESSAGE") != null) {
            iOutputMessage = map.get("OUTPUTMESSAGE").toString();
        }
        if (map.get("ONESEQCOPY") != null) {
            iOneSequenceCopy = convertStringToBool(map.get("ONESEQCOPY").toString());
        }
        if (map.get("OUTPUTPARAMS") != null) {
            iOutputParams = convertStringToBool(map.get("OUTPUTPARAMS").toString());
        }
        if (map.get("OUTPUTPATH") != null) {
            iOutputPath = map.get("OUTPUTPATH").toString();
        }
        if (map.get("OUTPUTPATHHASH") != null) {
            iOutputPathHash = convertStringToBool(map.get("OUTPUTPATHHASH").toString());
        }
        if (map.get("OUTPUTPERFORMANCE") != null) {
            iOutputPerformance = convertStringToBool(map.get("OUTPUTPERFORMANCE").toString());
        }
        if (map.get("OUTPUTPROTEINS") != null) {
            iOutputProteins = convertStringToBool(map.get("OUTPUTPROTEINS").toString());
        }
        if (map.get("OUTPUTRESULTS") != null) {
            iOutputResults = map.get("OUTPUTRESULTS").toString();
        }
        if (map.get("OUTPUTSEQPATH") != null) {
            iOutputSequencePath = map.get("OUTPUTSEQPATH").toString();
        }
        if (map.get("OUTPUTSEQUENCES") != null) {
            iOutputSequences = convertStringToBool(map.get("OUTPUTSEQUENCES").toString());
        }
        if (map.get("OUTPUTSORTRESULTS") != null) {
            iOutputSortResults = map.get("OUTPUTSORTRESULTS").toString();
        }
        if (map.get("OUTPUTSPECTRA") != null) {
            iOutputSpectra = convertStringToBool(map.get("OUTPUTSPECTRA").toString());
        }
        if (map.get("OUTPUTSXSLPATH") != null) {
            iOutputXslPath = map.get("OUTPUTSXSLPATH").toString();
        }
        if (map.get("C_TERMRESMODMASS") != null) {
            iProteinC_termResModMass = Double.parseDouble(map.get("C_TERMRESMODMASS").toString());
        }
        if (map.get("N_TERMRESMODMASS") != null) {
            iProteinN_termResModMass = Double.parseDouble(map.get("N_TERMRESMODMASS").toString());
        }
        if (map.get("C_TERMCLEAVMASSCHANGE") != null) {
            iProteinC_termCleavMassChange = map.get("C_TERMCLEAVMASSCHANGE").toString();
        }
        if (map.get("N_TERMCLEAVMASSCHANGE") != null) {
            iProteinN_termCleavMassChange = map.get("N_TERMCLEAVMASSCHANGE").toString();
        }
        if (map.get("CLEAVAGESITE") != null) {
            iProteinCleavageSite = map.get("CLEAVAGESITE").toString();
        }
        if (map.get("HOMOLOGMANAGE") != null) {
            iProteinHomologManagement = convertStringToBool(map.get("HOMOLOGMANAGE").toString());
        }
        if (map.get("MODRESMASSFILE") != null) {
            iProteinModifiedResMassFile = map.get("MODRESMASSFILE").toString();
        }
        if (map.get("TAXON") != null) {
            iProteinTaxon = map.get("TAXON").toString();
        }
        if (map.get("REFINE") != null) {
            iRefine = convertStringToBool(map.get("REFINE").toString());
        }
        if (map.get("REFINEMAXVALIDEXPECT") != null) {
            iRefineMaxValidExpectValue = Double.parseDouble(map.get("REFINEMAXVALIDEXPECT").toString());
        }
        if (map.get("REFINEMODMASS") != null) {
            refineModificationMass = map.get("REFINEMODMASS").toString();
        }
        if (map.get("POINTMUTATIONS") != null) {
            iPointMutations = convertStringToBool(map.get("POINTMUTATIONS").toString());
        }
        if (map.get("POTC_TERMMODS") != null) {
            iRefinePotC_termMods = map.get("POTC_TERMMODS").toString();
        }
        if (map.get("POTN_TERMMODS") != null) {
            iRefinePotN_termMods = map.get("POTN_TERMMODS").toString();
        }
        if (map.get("POTMODMASS") != null) {
            iRefinePotModMass = map.get("POTMODMASS").toString();
        }
        for (int i = 1; i < 100; i++) {
            // break at the first motif that doesn't exist
            if (map.get("POTMODMASS_" + i) == null) {
                break;
            }

            refinePotentialModificationMass.add(map.get("POTMODMASS_" + i).toString());
        }
        if (map.get("POTMODMOTIF") != null) {
            iRefinePotModMotif = map.get("POTMODMOTIF").toString();
        }
        for (int i = 1; i < 100; i++) {
            // break at the first motif that doesn't exist
            if (map.get("POTMODMOTIF_" + i) == null) {
                break;
            }

            refinePotentialModificationMotif.add(map.get("POTMODMOTIF_" + i).toString());
        }
        if (map.get("REFINESEQPATH") != null) {
            iRefineSequencePath = map.get("REFINESEQPATH").toString();
        }
        if (map.get("REFINESPECSYTNH") != null) {
            iRefineSpectrumSynthesis = convertStringToBool(map.get("REFINESPECSYTNH").toString());
        }
        if (map.get("REFINETIC") != null) {
            iRefineTicPercent = Integer.parseInt(map.get("REFINETIC").toString());
        }
        if (map.get("REFINEUNANTICLEAV") != null) {
            iRefineUnanticipatedCleavage = convertStringToBool(map.get("REFINEUNANTICLEAV").toString());
        }
        if (map.get("POTMODSFULLREFINE") != null) {
            iRefineUsePotentialModsForFullRefinement = convertStringToBool(map.get("POTMODSFULLREFINE").toString());
        }
        if (map.get("RESIDUEMODMASS") != null) {
            iResidueModMass = map.get("RESIDUEMODMASS").toString();
        }
        for (int i = 1; i < 100; i++) {
            // break at the first motif that doesn't exist
            if (map.get("RESIDUEMODMASS_" + i) == null) {
                break;
            }

            residueModificationMass.add(map.get("RESIDUEMODMASS_" + i).toString());
        }
        if (map.get("RESIDUEPOTMODMASS") != null) {
            iResiduePotModMass = map.get("RESIDUEPOTMODMASS").toString();
        }
        if (map.get("RESIDUEPOTMODMOTIV") != null) {
            iResiduePotModMotiv = map.get("RESIDUEPOTMODMOTIV").toString();
        }
        if (map.get("SCORING_AIONS") != null) {
            iScoring_aIons = convertStringToBool(map.get("SCORING_AIONS").toString());
        }
        if (map.get("SCORING_BIONS") != null) {
            iScoring_bIons = convertStringToBool(map.get("SCORING_BIONS").toString());
        }
        if (map.get("SCORING_CIONS") != null) {
            iScoring_cIons = convertStringToBool(map.get("SCORING_CIONS").toString());
        }
        if (map.get("SCORINGCYCLPERM") != null) {
            iScoringCyclicPerm = convertStringToBool(map.get("SCORINGCYCLPERM").toString());
        }
        if (map.get("SCORINGINCREV") != null) {
            iScoringIncReverse = convertStringToBool(map.get("SCORINGINCREV").toString());
        }
        if (map.get("SCORINGMISSCLEAV") != null) {
            iScoringMissCleavageSites = Integer.parseInt(map.get("SCORINGMISSCLEAV").toString());
        }
        if (map.get("SCORINGMINIONCOUNT") != null) {
            iScoringMinIonCount = Integer.parseInt(map.get("SCORINGMINIONCOUNT").toString());
        }
        if (map.get("SCORING_XIONS") != null) {
            iScoring_xIons = convertStringToBool(map.get("SCORING_XIONS").toString());
        }
        if (map.get("SCORING_YIONS") != null) {
            iScoring_yIons = convertStringToBool(map.get("SCORING_YIONS").toString());
        }
        if (map.get("SCORING_ZIONS") != null) {
            iScoring_zIons = convertStringToBool(map.get("SCORING_ZIONS").toString());
        }
        if (map.get("SCORINGPLUGSCORING") != null) {
            iScoringPlugScoring = convertStringToBool(map.get("SCORINGPLUGSCORING").toString());
        }
        if (map.get("SCORING_ALGORITHM") != null) {
            iScoringAlgorithm = map.get("SCORING_ALGORITHM").toString();
        }
        if (map.get("SPECDYNRANGE") != null) {
            iSpectrumDynRange = Double.parseDouble(map.get("SPECDYNRANGE").toString());
        }
        if (map.get("SPECFRAGMASSTYPE") != null) {
            iSpectrumFragMassType = map.get("SPECFRAGMASSTYPE").toString();
        }
        if (map.get("SPECMONOISOMASSERROR") != null) {
            iSpectrumMonoIsoMassError = Double.parseDouble(map.get("SPECMONOISOMASSERROR").toString());
        }
        if (map.get("SPECMONOISOMASSERRORUNITS") != null) {
            iSpectrumMonoIsoMassErrorUnits = map.get("SPECMONOISOMASSERRORUNITS").toString();
        }
        if (map.get("SPECMAXPRECURSORCHANGE") != null) {
            iSpectrumMaxPrecursorCharge = Integer.parseInt(map.get("SPECMAXPRECURSORCHANGE").toString());
        }
        if (map.get("SPECMINFRAGMZ") != null) {
            iSpectrumMinFragMz = Double.parseDouble(map.get("SPECMINFRAGMZ").toString());
        }
        if (map.get("SPECMINPRECURSORMZ") != null) {
            iSpectrumMinPrecursorMz = Double.parseDouble(map.get("SPECMINPRECURSORMZ").toString());
        }
        if (map.get("SPECMINPEAKS") != null) {
            iSpectrumMinPeaks = Integer.parseInt(map.get("SPECMINPEAKS").toString());
        }
        if (map.get("SPECPARENTMASSERRORMINUS") != null) {
            iSpectrumParentMonoIsoMassErrorMinus = Double.parseDouble(map.get("SPECPARENTMASSERRORMINUS").toString());
        }
        if (map.get("SPECPARENTMASSERRORPLUS") != null) {
            iSpectrumParentMonoIsoMassErrorPlus = Double.parseDouble(map.get("SPECPARENTMASSERRORPLUS").toString());
        }
        if (map.get("SPECPARENTMASSERRORUNITS") != null) {
            iSpectrumParentMonoIsoMassErrorUnits = map.get("SPECPARENTMASSERRORUNITS").toString();
        }
        if (map.get("SPECPARENTMASSISOERROR") != null) {
            iSpectrumParentMonoIsoMassIsoError = convertStringToBool(map.get("SPECPARENTMASSISOERROR").toString());
        }
        if (map.get("SPECTRUMPATH") != null) {
            iSpectrumPath = map.get("SPECTRUMPATH").toString();
        }
        if (map.get("SPECBATCHSIZE") != null) {
            iSpectrumSeqBatchSize = Integer.parseInt(map.get("SPECBATCHSIZE").toString());
        }
        if (map.get("SPECTHREADS") != null) {
            iSpectrumThreads = Integer.parseInt(map.get("SPECTHREADS").toString());
        }
        if (map.get("SPECTOTALPEAK") != null) {
            iSpectrumTotalPeakNumber = Integer.parseInt(map.get("SPECTOTALPEAK").toString());
        }
        if (map.get("SPECUSENOISECOMP") != null) {
            iSpectrumUseNoiseCompression = convertStringToBool(map.get("SPECUSENOISECOMP").toString());
        }
    }

    /**
     * Converts the "yes"/"no"-strings to the corresponding boolean value
     *
     * @param aString
     * @return flag
     */
    private boolean convertStringToBool(String aString) {
        boolean flag = false;
        if (aString.equalsIgnoreCase("yes")) {
            flag = true;
        }
        if (aString.equalsIgnoreCase("no")) {
            flag = false;
        }
        return flag;
    }

    public String getIDefaultParamPath() {
        return iDefaultParamPath;
    }

    public void setDefaultParamPath(String aDefaultParamPath) {
        iDefaultParamPath = aDefaultParamPath;
    }

    public String getTaxonomyInfoPath() {
        return iTaxonomyInfoPath;
    }

    public void setTaxonomyInfoPath(String aTaxonomyInfoPath) {
        iTaxonomyInfoPath = aTaxonomyInfoPath;
    }

    public String getHistogramColWidth() {
        return iHistogramColWidth;
    }

    public void setHistogramColWidth(String aHistogramColWidth) {
        iHistogramColWidth = aHistogramColWidth;
    }

    public boolean isHistogramExist() {
        return iHistogramExist;
    }

    public void setHistogramExist(boolean aHistogramExist) {
        iHistogramExist = aHistogramExist;
    }

    public String getLogPath() {
        return iLogPath;
    }

    public void setLogPath(String aLogPath) {
        iLogPath = aLogPath;
    }

    public double getMaxValidExpectValue() {
        return iMaxValidExpectValue;
    }

    public void setMaxValidExpectValue(double aMaxValidExpectValue) {
        iMaxValidExpectValue = aMaxValidExpectValue;
    }

    public String getOutputMessage() {
        return iOutputMessage;
    }

    public void setOutputMessage(String aOutputMessage) {
        iOutputMessage = aOutputMessage;
    }

    public boolean isOneSequenceCopy() {
        return iOneSequenceCopy;
    }

    public void setOneSequenceCopy(boolean aOneSequenceCopy) {
        iOneSequenceCopy = aOneSequenceCopy;
    }

    public boolean isOutputParams() {
        return iOutputParams;
    }

    public void setOutputParams(boolean aOutputParams) {
        iOutputParams = aOutputParams;
    }

    public String getOutputPath() {
        return iOutputPath;
    }

    public void setOutputPath(String aOutputPath) {
        iOutputPath = aOutputPath;
    }

    public boolean isOutputPathHash() {
        return iOutputPathHash;
    }

    public void setOutputPathHash(boolean aOutputPathHash) {
        iOutputPathHash = aOutputPathHash;
    }

    public boolean isOutputPerformance() {
        return iOutputPerformance;
    }

    public void setOutputPerformance(boolean aOutputPerformance) {
        iOutputPerformance = aOutputPerformance;
    }

    public boolean isOutputProteins() {
        return iOutputProteins;
    }

    public void setOutputProteins(boolean aOutputProteins) {
        iOutputProteins = aOutputProteins;
    }

    public String getOutputResults() {
        return iOutputResults;
    }

    public void setOutputResults(String aOutputResults) {
        iOutputResults = aOutputResults;
    }

    public String getOutputSequencePath() {
        return iOutputSequencePath;
    }

    public void setOutputSequencePath(String aOutputSequencePath) {
        iOutputSequencePath = aOutputSequencePath;
    }

    public boolean isOutputSequences() {
        return iOutputSequences;
    }

    public void setOutputSequences(boolean aOutputSequences) {
        iOutputSequences = aOutputSequences;
    }

    public String getOutputSortResults() {
        return iOutputSortResults;
    }

    public void setOutputSortResults(String outputSortResults) {
        iOutputSortResults = outputSortResults;
    }

    public boolean isOutputSpectra() {
        return iOutputSpectra;
    }

    public void setOutputSpectra(boolean outputSpectra) {
        iOutputSpectra = outputSpectra;
    }

    public String getOutputXslPath() {
        return iOutputXslPath;
    }

    public void setOutputXslPath(String outputXslPath) {
        iOutputXslPath = outputXslPath;
    }

    public double getProteinC_termResModMass() {
        return iProteinC_termResModMass;
    }

    public void setProteinC_termResModMass(double proteinC_termResModMass) {
        iProteinC_termResModMass = proteinC_termResModMass;
    }

    public double getProteinN_termResModMass() {
        return iProteinN_termResModMass;
    }

    public void setProteinN_termResModMass(double proteinN_termResModMass) {
        iProteinN_termResModMass = proteinN_termResModMass;
    }

    public String getProteinC_termCleavMassChange() {
        return iProteinC_termCleavMassChange;
    }

    public void setProteinC_termCleavMassChange(String proteinC_termCleavMassChange) {
        iProteinC_termCleavMassChange = proteinC_termCleavMassChange;
    }

    public String getProteinN_termCleavMassChange() {
        return iProteinN_termCleavMassChange;
    }

    public void setProteinN_termCleavMassChange(
            String proteinN_termCleavMassChange) {
        iProteinN_termCleavMassChange = proteinN_termCleavMassChange;
    }

    public String getProteinCleavageSite() {
        return iProteinCleavageSite;
    }

    public void setProteinCleavageSite(String proteinCleavageSite) {
        iProteinCleavageSite = proteinCleavageSite;
    }

    public boolean isProteinHomologManagement() {
        return iProteinHomologManagement;
    }

    public void setProteinHomologManagement(boolean proteinHomologManagement) {
        iProteinHomologManagement = proteinHomologManagement;
    }

    public String getProteinModifiedResMassFile() {
        return iProteinModifiedResMassFile;
    }

    public void setProteinModifiedResMassFile(String proteinModifiedResMassFile) {
        iProteinModifiedResMassFile = proteinModifiedResMassFile;
    }

    public String getProteinTaxon() {
        return iProteinTaxon;
    }

    public void setProteinTaxon(String proteinTaxon) {
        iProteinTaxon = proteinTaxon;
    }

    public boolean isRefine() {
        return iRefine;
    }

    public void setRefine(boolean refine) {
        iRefine = refine;
    }

    public double getRefineMaxValidExpectValue() {
        return iRefineMaxValidExpectValue;
    }

    public void setRefineMaxValidExpectValue(double refineMaxValidExpectValue) {
        iRefineMaxValidExpectValue = refineMaxValidExpectValue;
    }

    public String getRefineModMass() {
        return refineModificationMass;
    }

    public void setRefineModMass(String refineModificationMass) {
        this.refineModificationMass = refineModificationMass;
    }

    public boolean isPointMutations() {
        return iPointMutations;
    }

    public void setPointMutations(boolean pointMutations) {
        iPointMutations = pointMutations;
    }

    public String getRefinePotC_termMods() {
        return iRefinePotC_termMods;
    }

    public void setRefinePotC_termMods(String refinePotC_termMods) {
        iRefinePotC_termMods = refinePotC_termMods;
    }

    public String getRefinePotN_termMods() {
        return iRefinePotN_termMods;
    }

    public void setRefinePotN_termMods(String refinePotN_termMods) {
        iRefinePotN_termMods = refinePotN_termMods;
    }

    public String getRefinePotModMass() {
        return iRefinePotModMass;
    }

    public void setRefinePotModMass(String refinePotModMass) {
        iRefinePotModMass = refinePotModMass;
    }

    public String getRefinePotModMotif() {
        return iRefinePotModMotif;
    }

    public void setRefinePotModMotif(String refinePotModMotif) {
        iRefinePotModMotif = refinePotModMotif;
    }

    public String getRefineSequencePath() {
        return iRefineSequencePath;
    }

    public void setRefineSequencePath(String refineSequencePath) {
        iRefineSequencePath = refineSequencePath;
    }

    public boolean isRefineSpectrumSynthesis() {
        return iRefineSpectrumSynthesis;
    }

    public void setRefineSpectrumSynthesis(boolean refineSpectrumSynthesis) {
        iRefineSpectrumSynthesis = refineSpectrumSynthesis;
    }

    public double getRefineTicPercent() {
        return iRefineTicPercent;
    }

    public void setRefineTicPercent(double refineTicPercent) {
        iRefineTicPercent = refineTicPercent;
    }

    public boolean isRefineUnanticipatedCleavage() {
        return iRefineUnanticipatedCleavage;
    }

    public void setRefineUnanticipatedCleavage(boolean refineUnanticipatedCleavage) {
        iRefineUnanticipatedCleavage = refineUnanticipatedCleavage;
    }

    public boolean isRefineUsePotentialModsForFullRefinement() {
        return iRefineUsePotentialModsForFullRefinement;
    }

    public void setRefineUsePotentialModsForFullRefinement(
            boolean refineUsePotentialModsForFullRefinement) {
        iRefineUsePotentialModsForFullRefinement = refineUsePotentialModsForFullRefinement;
    }

    public String getResidueModMass() {
        return iResidueModMass;
    }

    public void setResidueModMass(String residueModMass) {
        iResidueModMass = residueModMass;
    }

    public String getResiduePotModMass() {
        return iResiduePotModMass;
    }

    public void setResiduePotModMass(String aResiduePotModMass) {
        iResiduePotModMass = aResiduePotModMass;
    }

    public String getResiduePotModMotiv() {
        return iResiduePotModMotiv;
    }

    public void setResiduePotModMotiv(String residuePotModMotiv) {
        iResiduePotModMotiv = residuePotModMotiv;
    }

    public boolean isScoring_aIons() {
        return iScoring_aIons;
    }

    public void setScoring_aIons(boolean scoring_aIons) {
        iScoring_aIons = scoring_aIons;
    }

    public boolean isScoring_bIons() {
        return iScoring_bIons;
    }

    public void setScoring_bIons(boolean scoring_bIons) {
        iScoring_bIons = scoring_bIons;
    }

    public boolean isScoring_cIons() {
        return iScoring_cIons;
    }

    public void setScoring_cIons(boolean scoring_cIons) {
        iScoring_cIons = scoring_cIons;
    }

    public boolean isScoringCyclicPerm() {
        return iScoringCyclicPerm;
    }

    public void setScoringCyclicPerm(boolean scoringCyclicPerm) {
        iScoringCyclicPerm = scoringCyclicPerm;
    }

    public boolean isScoringIncReverse() {
        return iScoringIncReverse;
    }

    public void setScoringIncReverse(boolean scoringIncReverse) {
        iScoringIncReverse = scoringIncReverse;
    }

    public int getScoringMissCleavageSites() {
        return iScoringMissCleavageSites;
    }

    public void setScoringMissCleavageSites(int scoringMissCleavageSites) {
        iScoringMissCleavageSites = scoringMissCleavageSites;
    }

    public int getScoringMinIonCount() {
        return iScoringMinIonCount;
    }

    public void setScoringMinIonCount(int scoringMinIonCount) {
        iScoringMinIonCount = scoringMinIonCount;
    }

    public boolean isScoring_xIons() {
        return iScoring_xIons;
    }

    public void setScoring_xIons(boolean scoring_xIons) {
        iScoring_xIons = scoring_xIons;
    }

    public boolean isScoring_yIons() {
        return iScoring_yIons;
    }

    public void setScoring_yIons(boolean scoring_yIons) {
        iScoring_yIons = scoring_yIons;
    }

    public boolean isScoring_zIons() {
        return iScoring_zIons;
    }

    public void setScoring_zIons(boolean scoring_zIons) {
        iScoring_zIons = scoring_zIons;
    }

    public boolean isScoringPlugScoring() {
        return iScoringPlugScoring;
    }

    public void setScoringPlugScoring(boolean scoringPlugScoring) {
        iScoringPlugScoring = scoringPlugScoring;
    }

    public double getSpectrumDynRange() {
        return iSpectrumDynRange;
    }

    public void setSpectrumDynRange(double spectrumDynRange) {
        iSpectrumDynRange = spectrumDynRange;
    }

    public String getSpectrumFragMassType() {
        return iSpectrumFragMassType;
    }

    public void setSpectrumFragMassType(String spectrumFragMassType) {
        iSpectrumFragMassType = spectrumFragMassType;
    }
    
    public String getScoringAlgorithm() {
        return iScoringAlgorithm;
    }

    public void setScoringAlgorithm(String scoringAlgorithm) {
        iScoringAlgorithm = scoringAlgorithm;
    }

    public double getSpectrumMonoIsoMassError() {
        return iSpectrumMonoIsoMassError;
    }

    public void setSpectrumMonoIsoMassError(double spectrumMonoIsoMassError) {
        iSpectrumMonoIsoMassError = spectrumMonoIsoMassError;
    }

    public String getSpectrumMonoIsoMassErrorUnits() {
        return iSpectrumMonoIsoMassErrorUnits;
    }

    public void setSpectrumMonoIsoMassErrorUnits(
            String spectrumMonoIsoMassErrorUnits) {
        iSpectrumMonoIsoMassErrorUnits = spectrumMonoIsoMassErrorUnits;
    }

    public int getSpectrumMaxPrecursorCharge() {
        return iSpectrumMaxPrecursorCharge;
    }

    public void setSpectrumMaxPrecursorCharge(int spectrumMaxPrecursorCharge) {
        iSpectrumMaxPrecursorCharge = spectrumMaxPrecursorCharge;
    }

    public double getSpectrumMinFragMz() {
        return iSpectrumMinFragMz;
    }

    public void setSpectrumMinFragMz(double spectrumMinFragMz) {
        iSpectrumMinFragMz = spectrumMinFragMz;
    }

    public double getSpectrumMinPrecursorMz() {
        return iSpectrumMinPrecursorMz;
    }

    public void setSpectrumMinPrecursorMz(double spectrumMinPrecursorMz) {
        iSpectrumMinPrecursorMz = spectrumMinPrecursorMz;
    }

    public int getSpectrumMinPeaks() {
        return iSpectrumMinPeaks;
    }

    public void setSpectrumMinPeaks(int spectrumMinPeaks) {
        iSpectrumMinPeaks = spectrumMinPeaks;
    }

    public double getSpectrumParentMonoIsoMassErrorMinus() {
        return iSpectrumParentMonoIsoMassErrorMinus;
    }

    public void setSpectrumParentMonoIsoMassErrorMinus(
            double spectrumParentMonoIsoMassErrorMinus) {
        iSpectrumParentMonoIsoMassErrorMinus = spectrumParentMonoIsoMassErrorMinus;
    }

    public double getSpectrumParentMonoIsoMassErrorPlus() {
        return iSpectrumParentMonoIsoMassErrorPlus;
    }

    public void setSpectrumParentMonoIsoMassErrorPlus(
            double spectrumParentMonoIsoMassErrorPlus) {
        iSpectrumParentMonoIsoMassErrorPlus = spectrumParentMonoIsoMassErrorPlus;
    }

    public String getSpectrumParentMonoIsoMassErrorUnits() {
        return iSpectrumParentMonoIsoMassErrorUnits;
    }

    public void setSpectrumParentMonoIsoMassErrorUnits(
            String spectrumParentMonoIsoMassErrorUnits) {
        iSpectrumParentMonoIsoMassErrorUnits = spectrumParentMonoIsoMassErrorUnits;
    }

    public boolean isSpectrumParentMonoIsoMassIsoError() {
        return iSpectrumParentMonoIsoMassIsoError;
    }

    public void setSpectrumParentMonoIsoMassIsoError(
            boolean spectrumParentMonoIsoMassIsoError) {
        iSpectrumParentMonoIsoMassIsoError = spectrumParentMonoIsoMassIsoError;
    }

    public String getSpectrumPath() {
        return iSpectrumPath;
    }

    public void setSpectrumPath(String spectrumPath) {
        iSpectrumPath = spectrumPath;
    }

    public int getSpectrumSeqBatchSize() {
        return iSpectrumSeqBatchSize;
    }

    public void setSpectrumSeqBatchSize(int spectrumSeqBatchSize) {
        iSpectrumSeqBatchSize = spectrumSeqBatchSize;
    }

    public int getSpectrumThreads() {
        return iSpectrumThreads;
    }

    public void setSpectrumThreads(int spectrumThreads) {
        iSpectrumThreads = spectrumThreads;
    }

    public int getSpectrumTotalPeakNumber() {
        return iSpectrumTotalPeakNumber;
    }

    public void setSpectrumTotalPeakNumber(int spectrumTotalPeakNumber) {
        iSpectrumTotalPeakNumber = spectrumTotalPeakNumber;
    }

    public boolean isSpectrumUseNoiseCompression() {
        return iSpectrumUseNoiseCompression;
    }

    public void setSpectrumUseNoiseCompression(boolean spectrumUseNoiseCompression) {
        iSpectrumUseNoiseCompression = spectrumUseNoiseCompression;
    }

    /**
     * Returns the values of "residue, modification mass [1-n]" as an ArrayList
     * of Strings. The zero-based index in the ArrayList corresponds to the
     * 1-based index in the file (f.e. "residue, modification mass 2" is at
     * position 1)
     *
     * @return An ArrayList of modification mass values
     */
    public ArrayList<String> getResidueModificationMass() {
        return residueModificationMass;
    }

    public void setResidueModificationMass(ArrayList<String> residueModificationMass) {
        this.residueModificationMass = residueModificationMass;
    }

    /**
     * Returns the values of "refine, potential modification mass [1-n]" as an
     * ArrayList of Strings. The zero-based index in the ArrayList corresponds
     * to the 1-based index in the file
     *
     * @return An ArrayList of potential modification mass values
     */
    public ArrayList<String> getRefinePotentialModificationMass() {
        return refinePotentialModificationMass;
    }

    public void setRefinePotentialModificationMass(ArrayList<String> refinePotentialModificationMass) {
        this.refinePotentialModificationMass = refinePotentialModificationMass;
    }

    /**
     * Returns the values of "refine, potential modification motif [1-n]" as an
     * ArrayList of Strings. The zero-based index in the ArrayList corresponds
     * to the 1-based index in the file
     *
     * @return An ArrayList of potential modification motifs
     */
    public ArrayList<String> getRefinePotentialModificationMotif() {
        return refinePotentialModificationMotif;
    }

    public void setRefinePotentialModificationMotif(ArrayList<String> refinePotentialModificationMotif) {
        this.refinePotentialModificationMotif = refinePotentialModificationMotif;
    }
}
