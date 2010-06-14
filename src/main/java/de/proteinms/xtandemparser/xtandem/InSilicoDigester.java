package de.proteinms.xtandemparser.xtandem;

import de.proteinms.xtandemparser.interfaces.Peak;

import java.util.HashMap;
import java.util.Vector;

/**
 * This class is used to do a calculation for the theoretical masses of
 * a, b, c, x, y and z ions (plus double charged ones) and do the matching
 * of experimental and theoretical masses.
 *
 * @author Thilo Muth
 */
public class InSilicoDigester {

    /**
     * This variable contains the peptide sequence.
     */
    private final String iSequence;
    /**
     * Contains the peptide object which should be digested.
     */
    private final Peptide iPeptide;
    /**
     * The masses map for knowledge of amino acid masses etc.
     */
    private final HashMap<String, Double> iMasses;
    /**
     * The a ions.
     */
    private final FragmentIon[] iAIons;
    /**
     * The a* ions.
     */
    private final FragmentIon[] iANH3Ions;
    /**
     * The a° ions.
     */
    private final FragmentIon[] iAH2OIons;
    /**
     * The b ions.
     */
    private final FragmentIon[] iBIons;
    /**
     * The b* ions.
     */
    private final FragmentIon[] iBNH3Ions;
    /**
     * The b° ions.
     */
    private final FragmentIon[] iBH2OIons;
    /**
     * The c ions.
     */
    private final FragmentIon[] iCIons;
    /**
     * The x ions.
     */
    private final FragmentIon[] iXIons;
    /**
     * The y ions.
     */
    private final FragmentIon[] iYIons;
    /**
     * The y* ions.
     */
    private final FragmentIon[] iYNH3Ions;
    /**
     * The y° ions.
     */
    private final FragmentIon[] iYH2OIons;
    /**
     * The z ions.
     */
    private final FragmentIon[] iZIons;
    /**
     * The MH ion.
     */
    private final FragmentIon[] iMH;
    /**
     * The MH-NH3 ion.
     */
    private final FragmentIon[] iMHNH3;
    /**
     * The MH-H2O ion.
     */
    private final FragmentIon[] iMHH2O;
    /**
     * The fragment mass error tolerance.
     */
    private final double iFragmentMassError;
    /**
     * The peptide charge
     */
    private final int iPeptideCharge;

    /**
     * Constructor get a peptide object, the modification map, the input parameters and the masses map.
     *
     * @param aPeptide A peptide object which should be "in silico" digested.
     * @param aModMap  Modification map to know where have been modifications on the peptide.
     * @param aMasses  Masses map to know which amino acid has which mass.
     * @param aCharge The charge of the given peptide.
     */
    public InSilicoDigester(Peptide aPeptide, ModificationMap aModMap, HashMap aMasses, int aCharge) {
        iPeptide = aPeptide;
        iSequence = iPeptide.getDomainSequence();
        ModificationMap iModMap=aModMap;
        iMasses = aMasses;
        iPeptideCharge = aCharge;
        int length = iSequence.length() * iPeptideCharge;
        iAIons = new FragmentIon[length];
        iAH2OIons = new FragmentIon[length];
        iANH3Ions = new FragmentIon[length];
        iBIons = new FragmentIon[length];
        iBH2OIons = new FragmentIon[length];
        iBNH3Ions = new FragmentIon[length];
        iCIons = new FragmentIon[length];
        iXIons = new FragmentIon[length];
        iYIons = new FragmentIon[length];
        iYNH3Ions = new FragmentIon[length];
        iYH2OIons = new FragmentIon[length];
        iZIons = new FragmentIon[length];
        iMH = new FragmentIon[iPeptideCharge];
        iMHNH3 = new FragmentIon[iPeptideCharge];
        iMHH2O = new FragmentIon[iPeptideCharge];
        iFragmentMassError = Parameters.FRAGMENTMASSERROR;
        calculateIons();
    }

    /**
     * This method calculates the masses of the peptide, including the masses of the aminoacids plus
     * the masses of the modifications (fixed/variable and N- resp. C-term included)
     *
     * @return double[] Contains the mass of the part of the sequence. The amino acid position is the index.
     */
    double[] calculatePeptideMasses() {
        double mass;
        double[] peptideMasses = new double[iSequence.length()];
        for (int i = 0; i < iSequence.length(); i++) {
            mass = 0.0;

            // Add the fixed modifications masses (N and C term included)
//            ArrayList<Modification> fixModList = iModMap.getFixedModifications(iPeptide.getDomainID());
//            if (fixModList.size() > 0) {
//                for (Modification fixMod : fixModList) {
//                    int modIndex = (Integer.parseInt(fixMod.getLocation()) - iPeptide.getDomainStart());
//                    if (modIndex == i) {
//                        mass += fixMod.getMass();
//                    }
//                }
//            }
//
//            // Add the the variable modification masses (N and C term included)
//            ArrayList<Modification> varModList = iModMap.getVariableModifications(iPeptide.getDomainID());
//            if (varModList.size() > 0) {
//                for (Modification varMod : varModList) {
//                    int modIndex = (Integer.parseInt(varMod.getLocation()) - iPeptide.getDomainStart());
//                    if (modIndex == i) {
//                        mass += varMod.getMass();
//                    }
//                }
//            }

            // For each amino acid add the specific mass
            String aa = String.valueOf(iSequence.charAt(i));
            mass += iMasses.get(aa);

            // Add each specific mass to the array
            peptideMasses[i] = mass;
        }
        return peptideMasses;
    }

    /**
     * This method calculates the theoretical masses of the ions of the peptide.
     * The fragment ion are stored as objects, for example yIons[0] is the y1 ion.
     */
    private void calculateIons() {
        double[] peptideMasses = calculatePeptideMasses();
        double hydrogenMass = Masses.Hydrogen;
        double oxygenMass = Masses.Oxygen;
        double nitrogenMass = Masses.Nitrogen;
        double carbonMass = Masses.Carbon;
        double c_termMass = iMasses.get("C_term");

        // Calculate ions masses for each charge
        int length = iSequence.length();
        if (iSequence.compareTo("LHYFNAR") == 0) {
            int a = 0;
        }
        int cptb = 0;
        int cpty = 0;
        for (int charge = 1; charge <= iPeptideCharge; charge++) {
            iMH[charge - 1] = new FragmentIon((iPeptide.getDomainMh() + (charge - 1) * hydrogenMass) / charge, FragmentIon.MH_ION, 0, charge, iFragmentMassError);            
            iMHH2O[charge - 1] = new FragmentIon((iPeptide.getDomainMh() - oxygenMass - 2 * hydrogenMass + (charge - 1) * hydrogenMass) / charge, FragmentIon.MHH2O_ION, 0, charge, iFragmentMassError);
            iMHNH3[charge - 1] = new FragmentIon((iPeptide.getDomainMh() - nitrogenMass - 3 * hydrogenMass + (charge - 1) * hydrogenMass) / charge, FragmentIon.MHNH3_ION, 0, charge, iFragmentMassError);

            for (int i = 0; i < length; i++) {
                double bMass = 0.0;
                double yMass = 0.0;

                // Each peptide mass is added to the b ion mass
                for (int j = 0; j <= i; j++) {
                    bMass += peptideMasses[j];
                }
                // Create an instance for each fragment ion
                if (i > 0 && charge <= iPeptideCharge) {
                    iAIons[cptb] = new FragmentIon((bMass - oxygenMass - carbonMass + charge * hydrogenMass) / charge, FragmentIon.A_ION, i + 1, charge, iFragmentMassError);
                    iANH3Ions[cptb] = new FragmentIon((bMass - oxygenMass - carbonMass - nitrogenMass - 3 * hydrogenMass + charge * hydrogenMass) / charge, FragmentIon.ANH3_ION, i + 1, charge, iFragmentMassError);
                    iAH2OIons[cptb] = new FragmentIon((bMass - 2 * oxygenMass - carbonMass - 2 * hydrogenMass + charge * hydrogenMass) / charge, FragmentIon.AH2O_ION, i + 1, charge, iFragmentMassError);
                    iBIons[cptb] = new FragmentIon((bMass + charge * hydrogenMass) / charge, FragmentIon.B_ION, i + 1, charge, iFragmentMassError);      
                    iBNH3Ions[cptb] = new FragmentIon((bMass - nitrogenMass - 3 * hydrogenMass + charge * hydrogenMass) / charge, FragmentIon.BNH3_ION, i + 1, charge, iFragmentMassError);
                    iBH2OIons[cptb] = new FragmentIon((bMass - oxygenMass - 2 * hydrogenMass + charge * hydrogenMass) / charge, FragmentIon.BH2O_ION, i + 1, charge, iFragmentMassError);
                    iCIons[cptb] = new FragmentIon((bMass + nitrogenMass + 3 * hydrogenMass + charge * hydrogenMass) / charge, FragmentIon.C_ION, i + 1, charge, iFragmentMassError);
                    cptb++;
                }

                // Each peptide mass is added to the y ion mass, taking the reverse direction (from the C terminal end)
                for (int j = 0; j <= i; j++) {
                    yMass += peptideMasses[(length - 1) - j];
                }
                // Add two extra hydrogen on the N terminal end and one hydroxyl at the C terminal end
                yMass = yMass + c_termMass + hydrogenMass;

                // Create an instance of the fragment y ion
                iXIons[cpty] = new FragmentIon((yMass + carbonMass + oxygenMass - 2 * hydrogenMass + charge * hydrogenMass) / charge, FragmentIon.X_ION, i + 1, charge, iFragmentMassError);
                iYIons[cpty] = new FragmentIon((yMass + charge * hydrogenMass) / charge, FragmentIon.Y_ION, i + 1, charge, iFragmentMassError);
                iYNH3Ions[cpty] = new FragmentIon((yMass - nitrogenMass - 3 * hydrogenMass + charge * hydrogenMass) / charge, FragmentIon.YNH3_ION, i + 1, charge, iFragmentMassError);
                iYH2OIons[cpty] = new FragmentIon((yMass - 2 * hydrogenMass - oxygenMass + charge * hydrogenMass) / charge, FragmentIon.YH2O_ION, i + 1, charge, iFragmentMassError);
                iZIons[cpty] = new FragmentIon((yMass - nitrogenMass - 2 * hydrogenMass + charge * hydrogenMass) / charge, FragmentIon.Z_ION, i + 1, charge, iFragmentMassError);
                cpty++;
            }
        }
    }

    /**
     * This method tries to match the theoretical masses of the ions with the
     * masses of the experimental peaks.
     *
     * @param ionType The ion type.
     * @param aPeaks  The experimental peaks.
     * @return matchedIons A Vector containing all the matched fragment ions.
     */
    public Vector getMatchedIons(int ionType, Peak[] aPeaks) {
        Vector matchedIons = new Vector();
        FragmentIon[] theoreticIons = getTheoreticIons(ionType);

        for (FragmentIon fragIon : theoreticIons) {
            if (fragIon != null) {
                if (fragIon.isMatch(aPeaks, iFragmentMassError)) {
                    matchedIons.add(fragIon);
                }
            }
        }


        return matchedIons;
    }

    /*
     * Returns the corresponding array of theoretic ions.
     */
    public FragmentIon[] getTheoreticIons(int type) {
        switch (type) {
            case FragmentIon.A_ION:
                return iAIons;
            case FragmentIon.AH2O_ION:
                return iAH2OIons;
            case FragmentIon.ANH3_ION:
                return iANH3Ions;
            case FragmentIon.B_ION:
                return iBIons;
            case FragmentIon.BH2O_ION:
                return iBH2OIons;
            case FragmentIon.BNH3_ION:
                return iBNH3Ions;
            case FragmentIon.C_ION:
                return iCIons;
            case FragmentIon.X_ION:
                return iXIons;
            case FragmentIon.Y_ION:
                return iYIons;
            case FragmentIon.YH2O_ION:
                return iYH2OIons;
            case FragmentIon.YNH3_ION:
                return iYNH3Ions;
            case FragmentIon.Z_ION:
                return iZIons;
            case FragmentIon.MH_ION:
                return iMH;
            case FragmentIon.MHNH3_ION:
                return iMHNH3;
            case FragmentIon.MHH2O_ION:
                return iMHH2O;
        }
        return null;
    }
}
