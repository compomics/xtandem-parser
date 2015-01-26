package de.proteinms.xtandemparser.xtandem;

import java.util.HashMap;

/**
 * This class holds the performance parameters in map.
 *
 * @author Thilo Muth
 */
public class PerformParams {

    /**
     * Holds the first sequence source.
     */
    private String iSequenceSource_1 = null;
    /**
     * Holds the second sequence source.
     */
    private String iSequenceSource_2 = null;
    /**
     * Holds the third sequence source.
     */
    private String iSequenceSource_3 = null;
    /**
     * Holds the first sequence source description.
     */
    private String iSequenceSourceDescription_1 = null;
    /**
     * Holds the second sequence source description.
     */
    private String iSequenceSourceDescription_2 = null;
    /**
     * Holds the third sequence source description.
     */
    private String iSequenceSourceDescription_3 = null;
    /**
     * Holds the number of estimated false positives.
     */
    private long iEstFalsePositives;
    /**
     * This double holds the spectrum noise suppression ratio.
     */
    private double iSpectrumNoiseSuppressionRatio;
    /**
     * This Long holds the number of total peptides used.
     */
    private long iTotalPeptidesUsed;
    /**
     * This Long holds the number of total proteins used.
     */
    private long iTotalProteinsUsed;
    /**
     * This Long holds the number of total spectra assigned.
     */
    private long iTotalSpectraAssigned;
    /**
     * This Long holds the total number of spectra used.
     */
    private long iTotalSpectraUsed;
    /**
     * This Long holds the number of total uniques assigned.
     */
    private long iTotalUniqueAssigned;
    /**
     * This String holds the process start time.
     */
    private String iProcStartTime = null;
    /**
     * This String holds the process version.
     */
    private String iProcVersion = null;
    /**
     * This String holds the quality values.
     */
    private String iQualityValues = null;
    /**
     * This Integer holds the input model number.
     */
    private int iInputModelNumber;
    /**
     * This Integer holds the input spectra number.
     */
    private int iInputSpectraNumber;
    /**
     * This Integer holds the partial cleavage number.
     */
    private int iPartialCleavageNumber;
    /**
     * This Integer holds the point mutations number.
     */
    private int iPointMutationsNumber;
    /**
     * This Integer holds the potential c-terminal number.
     */
    private int iPotentialC_termNumber;
    /**
     * This Integer holds the potential n-terminal number.
     */
    private int iPotentialN_termNumber;
    /**
     * This Integer holds the unanticipated cleavage number.
     */
    private int iUnanticipatedCleavageNumber;
    /**
     * This double holds the initial model total timing.
     */
    private double iInitModelTotalTiming;
    /**
     * This double holds the initial model spectrum timing.
     */
    private double iInitModelSpecTiming;
    /**
     * This double holds the load sequence models timing.
     */
    private double iLoadSeqModelsTiming;
    /**
     * This double holds the refinement timing.
     */
    private double iRefinementTiming;

    /**
     * The constructor gets a hash map containing all the performance
     * parameters.
     *
     * @param map the performance map
     */
    public PerformParams(HashMap map) {

        if (map.get("SEQSRC1") != null) {
            iSequenceSource_1 = map.get("SEQSRC1").toString();
        }
        if (map.get("SEQSRC2") != null) {
            iSequenceSource_2 = map.get("SEQSRC2").toString();
        }
        if (map.get("SEQSRC3") != null) {
            iSequenceSource_3 = map.get("SEQSRC3").toString();
        }
        if (map.get("SEQSRCDESC1") != null) {
            iSequenceSourceDescription_1 = map.get("SEQSRCDESC1").toString();
        }
        if (map.get("SEQSRCDESC2") != null) {
            iSequenceSourceDescription_2 = map.get("SEQSRCDESC2").toString();
        }
        if (map.get("SEQSRCDESC3") != null) {
            iSequenceSourceDescription_3 = map.get("SEQSRCDESC3").toString();
        }
        if (map.get("ESTFP") != null) {
            iEstFalsePositives = Long.parseLong(map.get("ESTFP").toString());
        }
        if (map.get("NOISESUPP") != null) {
            iSpectrumNoiseSuppressionRatio = Double.parseDouble(map.get("NOISESUPP").toString());
        }
        if (map.get("TOTALPEPUSED") != null) {
            iTotalPeptidesUsed = Long.parseLong(map.get("TOTALPEPUSED").toString());
        }
        if (map.get("TOTALPROTUSED") != null) {
            iTotalProteinsUsed = Long.parseLong(map.get("TOTALPROTUSED").toString());
        }
        if (map.get("TOTALSPECASS") != null) {
            iTotalSpectraAssigned = Long.parseLong(map.get("TOTALSPECASS").toString());
        }
        if (map.get("TOTALSPECUSED") != null) {
            iTotalSpectraUsed = Long.parseLong(map.get("TOTALSPECUSED").toString());
        }
        if (map.get("TOTALUNIQUEASS") != null) {
            iTotalUniqueAssigned = Long.parseLong(map.get("TOTALUNIQUEASS").toString());
        }
        if (map.get("PROCSTART") != null) {
            iProcStartTime = map.get("PROCSTART").toString();
        }
        if (map.get("PROCVER") != null) {
            iProcVersion = map.get("PROCVER").toString();
        }
        if (map.get("QUALVAL") != null) {
            iQualityValues = map.get("QUALVAL").toString();
        }
        if (map.get("INPUTMOD") != null) {
            iInputModelNumber = Integer.parseInt(map.get("INPUTMOD").toString());
        }
        if (map.get("INPUTSPEC") != null) {
            iInputSpectraNumber = Integer.parseInt(map.get("INPUTSPEC").toString());
        }
        if (map.get("PARTCLEAV") != null) {
            iPartialCleavageNumber = Integer.parseInt(map.get("PARTCLEAV").toString());
        }
        if (map.get("POINTMUT") != null) {
            iPointMutationsNumber = Integer.parseInt(map.get("POINTMUT").toString());
        }
        if (map.get("POTC_TERM") != null) {
            iPotentialC_termNumber = Integer.parseInt(map.get("POTC_TERM").toString());
        }
        if (map.get("POTN_TERM") != null) {
            iPotentialN_termNumber = Integer.parseInt(map.get("POTN_TERM").toString());
        }
        if (map.get("UNANTICLEAV") != null) {
            iUnanticipatedCleavageNumber = Integer.parseInt(map.get("UNANTICLEAV").toString());
        }
        if (map.get("INITMODELTOTALTIME") != null) {
            iInitModelTotalTiming = Double.parseDouble(map.get("INITMODELTOTALTIME").toString());
        }
        if (map.get("INITMODELSPECTIME") != null) {
            iInitModelSpecTiming = Double.parseDouble(map.get("INITMODELSPECTIME").toString());
        }
        if (map.get("LOADSEQMODELTIME") != null) {
            iLoadSeqModelsTiming = Double.parseDouble(map.get("LOADSEQMODELTIME").toString());
        }
        if (map.get("REFINETIME") != null) {
            iRefinementTiming = Double.parseDouble(map.get("REFINETIME").toString());
        }
    }

    public String getSequenceSource_1() {
        return iSequenceSource_1;
    }

    public void setSequenceSource_1(String sequenceSource_1) {
        this.iSequenceSource_1 = sequenceSource_1;
    }

    public String getSequenceSource_2() {
        return iSequenceSource_2;
    }

    public void setSequenceSource_2(String sequenceSource_2) {
        this.iSequenceSource_2 = sequenceSource_2;
    }

    public String getSequenceSource_3() {
        return iSequenceSource_3;
    }

    public void setSequenceSource_3(String sequenceSource_3) {
        this.iSequenceSource_3 = sequenceSource_3;
    }

    public String getSequenceSourceDescription_1() {
        return iSequenceSourceDescription_1;
    }

    public void setSequenceSourceDescription_1(String sequenceSourceDescription_1) {
        this.iSequenceSourceDescription_1 = sequenceSourceDescription_1;
    }

    public String getSequenceSourceDescription_2() {
        return iSequenceSourceDescription_2;
    }

    public void setSequenceSourceDescription_2(String sequenceSourceDescription_2) {
        this.iSequenceSourceDescription_2 = sequenceSourceDescription_2;
    }

    public String getSequenceSourceDescription_3() {
        return iSequenceSourceDescription_3;
    }

    public void setSequenceSourceDescription_3(String sequenceSourceDescription_3) {
        this.iSequenceSourceDescription_3 = sequenceSourceDescription_3;
    }

    public long getEstFalsePositives() {
        return iEstFalsePositives;
    }

    public void setEstFalsePositives(long estFalsePositives) {
        this.iEstFalsePositives = estFalsePositives;
    }

    public double getSpectrumNoiseSuppressionRatio() {
        return iSpectrumNoiseSuppressionRatio;
    }

    public void setSpectrumNoiseSuppressionRatio(
            double spectrumNoiseSuppressionRatio) {
        this.iSpectrumNoiseSuppressionRatio = spectrumNoiseSuppressionRatio;
    }

    public long getTotalPeptidesUsed() {
        return iTotalPeptidesUsed;
    }

    public void setTotalPeptidesUsed(long totalPeptidesUsed) {
        this.iTotalPeptidesUsed = totalPeptidesUsed;
    }

    public long getTotalProteinsUsed() {
        return iTotalProteinsUsed;
    }

    public void setTotalProteinsUsed(long totalProteinsUsed) {
        this.iTotalProteinsUsed = totalProteinsUsed;
    }

    public long getTotalSpectraAssigned() {
        return iTotalSpectraAssigned;
    }

    public void setTotalSpectraAssigned(long totalSpectraAssigned) {
        this.iTotalSpectraAssigned = totalSpectraAssigned;
    }

    public long getTotalSpectraUsed() {
        return iTotalSpectraUsed;
    }

    public void setTotalSpectraUsed(long totalSpectraUsed) {
        this.iTotalSpectraUsed = totalSpectraUsed;
    }

    public long getTotalUniqueAssigned() {
        return iTotalUniqueAssigned;
    }

    public void setTotalUniqueAssigned(long totalUniqueAssigned) {
        this.iTotalUniqueAssigned = totalUniqueAssigned;
    }

    public String getProcStartTime() {
        return iProcStartTime;
    }

    public void setProcStartTime(String procStartTime) {
        this.iProcStartTime = procStartTime;
    }

    public String getProcVersion() {
        return iProcVersion;
    }

    public void setProcVersion(String procVersion) {
        this.iProcVersion = procVersion;
    }

    public String getQualityValues() {
        return iQualityValues;
    }

    public void setQualityValues(String qualityValues) {
        this.iQualityValues = qualityValues;
    }

    public int getInputModelNumber() {
        return iInputModelNumber;
    }

    public void setInputModelNumber(int inputModelNumber) {
        this.iInputModelNumber = inputModelNumber;
    }

    public int getInputSpectraNumber() {
        return iInputSpectraNumber;
    }

    public void setInputSpectraNumber(int inputSpectraNumber) {
        this.iInputSpectraNumber = inputSpectraNumber;
    }

    public int getPartialCleavageNumber() {
        return iPartialCleavageNumber;
    }

    public void setPartialCleavageNumber(int partialCleavageNumber) {
        this.iPartialCleavageNumber = partialCleavageNumber;
    }

    public int getPointMutationsNumber() {
        return iPointMutationsNumber;
    }

    public void setPointMutationsNumber(int pointMutationsNumber) {
        this.iPointMutationsNumber = pointMutationsNumber;
    }

    public int getPotentialC_termNumber() {
        return iPotentialC_termNumber;
    }

    public void setPotentialC_termNumber(int potentialC_termNumber) {
        this.iPotentialC_termNumber = potentialC_termNumber;
    }

    public int getPotentialN_termNumber() {
        return iPotentialN_termNumber;
    }

    public void setPotentialN_termNumber(int potentialN_termNumber) {
        this.iPotentialN_termNumber = potentialN_termNumber;
    }

    public int getUnanticipatedCleavageNumber() {
        return iUnanticipatedCleavageNumber;
    }

    public void setUnanticipatedCleavageNumber(int unanticipatedCleavageNumber) {
        this.iUnanticipatedCleavageNumber = unanticipatedCleavageNumber;
    }

    public double getInitModelTotalTiming() {
        return iInitModelTotalTiming;
    }

    public void setInitModelTotalTiming(double initModelTotalTiming) {
        this.iInitModelTotalTiming = initModelTotalTiming;
    }

    public double getInitModelSpecTiming() {
        return iInitModelSpecTiming;
    }

    public void setInitModelSpecTiming(double initModelSpecTiming) {
        this.iInitModelSpecTiming = initModelSpecTiming;
    }

    public double getLoadSeqModelsTiming() {
        return iLoadSeqModelsTiming;
    }

    public void setLoadSeqModelsTiming(double loadSeqModelsTiming) {
        this.iLoadSeqModelsTiming = loadSeqModelsTiming;
    }

    public double getRefinementTiming() {
        return iRefinementTiming;
    }

    public void setRefinementTiming(double refinementTiming) {
        this.iRefinementTiming = refinementTiming;
    }
}
