package de.proteinms.xtandemparser.interfaces;

/**
 * Modification interface implemented by FixedModification and
 * VariableModification.
 *
 * @author Thilo Muth
 */
public interface Modification {

    /**
     * The method returns the name of the modification.
     *
     * @return String
     */
    String getName();

    /**
     * The method returns the mass of the modification.
     *
     * @return double
     */
    double getMass();

    /**
     * The method returns the location of the modification.
     *
     * @return String
     */
    String getLocation();

    /**
     * Method to test if it is a fixed or variable modification.
     *
     * @return boolean
     */
    boolean isFixed();

    /**
     * The method returns the number of the modification.
     *
     * @return int
     */
    int getNumber();
    
    /**
     * Returns true if the modification is a amino acid substitution. This is 
     * detected if the modification contains 'pm="X"' where 'X' is then the 
     * subsituted amino acid.
     * 
     * @return boolean
     */
    boolean isSubstitution();
    
    /**
     * Returns the subsituted amino acid (if any). This is detected if the 
     * modification contains 'pm="X"' where 'X' is then the subsituted amino 
     * acid. If the modification is not a subsitution null is returned.
     * 
     * @return the subsituted amino acid, or null if not a substutition
     */
    String getSubstitutedAminoAcid();
}
