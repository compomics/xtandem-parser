package de.proteinms.xtandemparser.interfaces;

/**
 * Modification interface implemented by FixedModification and VariableModification.
 * @author Thilo Muth
 *
 */
public interface Modification {

    /**
     * The method returns the name of the modification.
     * @return String
     */
    String getName();

    /**
     * The method returns the mass of the modification.
     * @return double
     */
    double getMass();

    /**
     * The method returns the location of the modification.
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
     * The method returns the domain id as identification for the modification.
     * @return String
     */
    String getDomainID();
}
