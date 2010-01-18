package de.proteinms.xtandemparser.xtandem;

import de.proteinms.xtandemparser.interfaces.Modification;
import java.io.Serializable;

/**
 * This class implements modification and represents a variable modification object.
 *
 * @author Thilo Muth
 */
public class VariableModification implements Modification, Serializable {

    /**
     * This String holds the name of the modification.
     */
    private String iName;
    /**
     * This variable contains the mass of the modification.
     */
    private double iMass;
    /**
     * This variable contains the location of the modification.
     */
    private String iLocation;
    
    /**
     * This variable holds number of the modification from one peptide.
     */
    private int iNumber;

    /**
     * This variables holds the modified residue.
     */
    private String iModifiedResidue;

    /**
     * Constructor for building a variable modification.
     *
     * @param aName The modification name
     * @param aMass The modification mass
     * @param aLocation The modification location
     * @param aNumber The number of the modification
     */
    public VariableModification(String aName, double aMass, String aLocation, int aNumber) {
        iName = aName;
        iMass = aMass;
        iLocation = aLocation;
        iNumber = aNumber;
    }

    /**
     * Returns the modification name.
     *
     * @return the modification name
     */
    public String getName() {
        return iName;
    }

    /**
     * Sets the modification name.
     *
     * @param aName The modification name
     */
    public void setName(String aName) {
        iName = aName;
    }

    /**
     * Returns the modification mass.
     *
     * @return the modification mass
     */
    public double getMass() {
        return iMass;
    }

    /**
     * Sets the modification mass.
     *
     * @param aMass The modification mass
     */
    public void setMass(double aMass) {
        iMass = aMass;
    }

    /**
     * Returns the modification location.
     *
     * @return the modification location
     */
    public String getLocation() {
        return iLocation;
    }

    /**
     * Sets the modification location.
     *
     * @param aLocation The modification location
     */
    public void setLocation(String aLocation) {
        iLocation = aLocation;
    }

    /**
     * Always returns false because it's a variable modification.
     *
     * @return false because it's a variable modification
     */
    public boolean isFixed() {
        return false;
    }

    /**
     * Returns the domain id.
     *
     * @return the number of modifcation
     */
    public int getNumber(){
        return iNumber;
    }

    /**
     * Sets the domain id.
     *
     * @param aNumber The number of the modification.
     */
    public void setNumber(int aNumber) {
        iNumber = aNumber;
    }

    /**
     * Returns the modified residue (a peptide letter).
     * 
     * @return iModifiedResidue
     */
    public String getModifiedResidue() {
        String[] values = iName.split("@");
        iModifiedResidue = values[1];
        return iModifiedResidue;
    }
}
