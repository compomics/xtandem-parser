package de.proteinms.xtandemparser.xtandem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class holds the information about the supporting data:
 * 1) Hyperscore expectation function
 * 2) Convolution survival function
 * 3) Ion histograms (a,b,c + x,y,z ions)
 * 4) fragment ion mass spectrum: m/z and intensity
 *
 * @author Thilo Muth
 */
public class SupportData implements Serializable {

    private String iHyperlabel = "";
    private String iConvolLabel = "";
    private String iA_ionLabel = "";
    private String iB_ionLabel = "";
    private String iC_ionLabel = "";
    private String iX_ionLabel = "";
    private String iY_ionLabel = "";
    private String iZ_ionLabel = "";
    private String iSpectrumLabel = "";
    private String iFragIonSpectrumDescription = "";
    private double iFragIonMz = 0;
    private int iFragIonCharge = 0;
    private double iHyper_a0 = 0;
    private double iHyper_a1 = 0;
    private ArrayList<Integer> iXValuesHyperscore = null;
    private ArrayList<Integer> iYValuesHyperscore = null;
    private ArrayList<Integer> iXValuesConvolute = null;
    private ArrayList<Integer> iYValuesConvolute = null;
    private ArrayList<Integer> iXValuesaIons = null;
    private ArrayList<Integer> iYValuesaIons = null;
    private ArrayList<Integer> iXValuesbIons = null;
    private ArrayList<Integer> iYValuesbIons = null;
    private ArrayList<Integer> iXValuescIons = null;
    private ArrayList<Integer> iYValuescIons = null;
    private ArrayList<Integer> iXValuesxIons = null;
    private ArrayList<Integer> iYValuesxIons = null;
    private ArrayList<Integer> iXValuesyIons = null;
    private ArrayList<Integer> iYValuesyIons = null;
    private ArrayList<Integer> iXValueszIons = null;
    private ArrayList<Integer> iYValueszIons = null;
    private ArrayList<Double> iXValuesFragIonMass2Charge = null;
    private ArrayList<Double> iYValuesFragIonMass2Charge = null;

    /**
     * Constructor gets the raw support map and the spectrum number.
     *
     * @param map The raw support map
     * @param spectrumNumber The spectrum number
     */
    public SupportData(HashMap map, int spectrumNumber) {
        if (map.get("HYPERLABEL" + "_s" + spectrumNumber) != null) {
            iHyperlabel = map.get("HYPERLABEL" + "_s" + spectrumNumber).toString();
        }
        if (map.get("CONVOLLABEL" + "_s" + spectrumNumber) != null) {
            iConvolLabel = map.get("CONVOLLABEL" + "_s" + spectrumNumber).toString();
        }
        if (map.get("A_IONLABEL" + "_s" + spectrumNumber) != null) {
            iA_ionLabel = map.get("A_IONLABEL" + "_s" + spectrumNumber).toString();
        }
        if (map.get("B_IONLABEL" + "_s" + spectrumNumber) != null) {
            iB_ionLabel = map.get("B_IONLABEL" + "_s" + spectrumNumber).toString();
        }
        if (map.get("C_IONLABEL" + "_s" + spectrumNumber) != null) {
            iC_ionLabel = map.get("C_IONLABEL" + "_s" + spectrumNumber).toString();
        }
        if (map.get("X_IONLABEL" + "_s" + spectrumNumber) != null) {
            iX_ionLabel = map.get("X_IONLABEL" + "_s" + spectrumNumber).toString();
        }
        if (map.get("Y_IONLABEL" + "_s" + spectrumNumber) != null) {
            iY_ionLabel = map.get("Y_IONLABEL" + "_s" + spectrumNumber).toString();
        }
        if (map.get("Z_IONLABEL" + "_s" + spectrumNumber) != null) {
            iZ_ionLabel = map.get("Z_IONLABEL" + "_s" + spectrumNumber).toString();
        }
        if (map.get("SPECTRUMLABEL" + "_s" + spectrumNumber) != null) {
            iSpectrumLabel = map.get("SPECTRUMLABEL" + "_s" + spectrumNumber).toString();
        }
        if (map.get("FRAGIONSPECDESC" + "_s" + spectrumNumber) != null) {
            iFragIonSpectrumDescription = map.get("FRAGIONSPECDESC" + "_s" + spectrumNumber).toString();
        }
        if (map.get("FRAGIONMZ" + "_s" + spectrumNumber) != null) {
            iFragIonMz = Double.parseDouble(map.get("FRAGIONMZ" + "_s" + spectrumNumber).toString());
        }
        if (map.get("FRAGIONCHARGE" + "_s" + spectrumNumber) != null) {
            iFragIonCharge = Integer.parseInt(map.get("FRAGIONCHARGE" + "_s" + spectrumNumber).toString());
        }
        if (map.get("HYPER_A0" + "_s" + spectrumNumber) != null) {
            iHyper_a0 = Double.parseDouble(map.get("HYPER_A0" + "_s" + spectrumNumber).toString());
        }
        if (map.get("HYPER_A1" + "_s" + spectrumNumber) != null) {
            iHyper_a1 = Double.parseDouble(map.get("HYPER_A1" + "_s" + spectrumNumber).toString());
        }
        if (map.get("XVAL_HYPER" + "_s" + spectrumNumber) != null) {
            iXValuesHyperscore = splitStringToIntegerList(map.get("XVAL_HYPER" + "_s" + spectrumNumber).toString());
        }
        if (map.get("YVAL_HYPER" + "_s" + spectrumNumber) != null) {
            iYValuesHyperscore = splitStringToIntegerList(map.get("YVAL_HYPER" + "_s" + spectrumNumber).toString());
        }
        if (map.get("XVAL_CONVOL" + "_s" + spectrumNumber) != null) {
            iXValuesConvolute = splitStringToIntegerList(map.get("XVAL_CONVOL" + "_s" + spectrumNumber).toString());
        }
        if (map.get("YVAL_CONVOL" + "_s" + spectrumNumber) != null) {
            iYValuesConvolute = splitStringToIntegerList(map.get("YVAL_CONVOL" + "_s" + spectrumNumber).toString());
        }
        if (map.get("XVAL_AIONS" + "_s" + spectrumNumber) != null) {
            iXValuesaIons = splitStringToIntegerList(map.get("XVAL_AIONS" + "_s" + spectrumNumber).toString());
        }
        if (map.get("YVAL_AIONS" + "_s" + spectrumNumber) != null) {
            iYValuesaIons = splitStringToIntegerList(map.get("YVAL_AIONS" + "_s" + spectrumNumber).toString());
        }
        if (map.get("XVAL_BIONS" + "_s" + spectrumNumber) != null) {
            iXValuesbIons = splitStringToIntegerList(map.get("XVAL_BIONS" + "_s" + spectrumNumber).toString());
        }
        if (map.get("YVAL_BIONS" + "_s" + spectrumNumber) != null) {
            iYValuesbIons = splitStringToIntegerList(map.get("YVAL_BIONS" + "_s" + spectrumNumber).toString());
        }
        if (map.get("XVAL_CIONS" + "_s" + spectrumNumber) != null) {
            iXValuescIons = splitStringToIntegerList(map.get("XVAL_CIONS" + "_s" + spectrumNumber).toString());
        }
        if (map.get("YVAL_CIONS" + "_s" + spectrumNumber) != null) {
            iYValuescIons = splitStringToIntegerList(map.get("YVAL_CIONS" + "_s" + spectrumNumber).toString());
        }
        if (map.get("XVAL_XIONS" + "_s" + spectrumNumber) != null) {
            iXValuesxIons = splitStringToIntegerList(map.get("XVAL_XIONS" + "_s" + spectrumNumber).toString());
        }
        if (map.get("YVAL_XIONS" + "_s" + spectrumNumber) != null) {
            iYValuesxIons = splitStringToIntegerList(map.get("YVAL_XIONS" + "_s" + spectrumNumber).toString());
        }
        if (map.get("XVAL_YIONS" + "_s" + spectrumNumber) != null) {
            iXValuesyIons = splitStringToIntegerList(map.get("XVAL_YIONS" + "_s" + spectrumNumber).toString());
        }
        if (map.get("YVAL_YIONS" + "_s" + spectrumNumber) != null) {
            iYValuesyIons = splitStringToIntegerList(map.get("YVAL_YIONS" + "_s" + spectrumNumber).toString());
        }
        if (map.get("XVAL_ZIONS" + "_s" + spectrumNumber) != null) {
            iXValueszIons = splitStringToIntegerList(map.get("XVAL_ZIONS" + "_s" + spectrumNumber).toString());
        }
        if (map.get("YVAL_ZIONS" + "_s" + spectrumNumber) != null) {
            iYValueszIons = splitStringToIntegerList(map.get("YVAL_ZIONS" + "_s" + spectrumNumber).toString());
        }
        if (map.get("XVAL_FRAGIONMZ" + "_s" + spectrumNumber) != null) {
            iXValuesFragIonMass2Charge = splitStringToDoubleList(map.get("XVAL_FRAGIONMZ" + "_s" + spectrumNumber).toString());
        }
        if (map.get("YVAL_FRAGIONMZ" + "_s" + spectrumNumber) != null) {
            iYValuesFragIonMass2Charge = splitStringToDoubleList(map.get("YVAL_FRAGIONMZ" + "_s" + spectrumNumber).toString());
        }
    }

    public String getHyperlabel() {
        return iHyperlabel;
    }

    public void setHyperlabel(String aHyperlabel) {
        iHyperlabel = aHyperlabel;
    }

    public String getConvolLabel() {
        return iConvolLabel;
    }

    public void setConvolLabel(String aConvolLabel) {
        iConvolLabel = aConvolLabel;
    }

    public String getA_ionLabel() {
        return iA_ionLabel;
    }

    public void setA_ionLabel(String aA_ionLabel) {
        iA_ionLabel = aA_ionLabel;
    }

    public String getB_ionLabel() {
        return iB_ionLabel;
    }

    public void setB_ionLabel(String aB_ionLabel) {
        iB_ionLabel = aB_ionLabel;
    }

    public String getC_ionLabel() {
        return iC_ionLabel;
    }

    public void setC_ionLabel(String aC_ionLabel) {
        iC_ionLabel = aC_ionLabel;
    }

    public String getX_ionLabel() {
        return iX_ionLabel;
    }

    public void setX_ionLabel(String aX_ionLabel) {
        iX_ionLabel = aX_ionLabel;
    }

    public String getY_ionLabel() {
        return iY_ionLabel;
    }

    public void setY_ionLabel(String aY_ionLabel) {
        iY_ionLabel = aY_ionLabel;
    }

    public String getZ_ionLabel() {
        return iZ_ionLabel;
    }

    public void setZ_ionLabel(String aZ_ionLabel) {
        iZ_ionLabel = aZ_ionLabel;
    }

    public String getSpectrumLabel() {
        return iSpectrumLabel;
    }

    public void setSpectrumLabel(String aSpectrumLabel) {
        iSpectrumLabel = aSpectrumLabel;
    }

    public String getFragIonSpectrumDescription() {
        return iFragIonSpectrumDescription;
    }

    public void setFragIonSpectrumDescription(String aFragIonSpectrumDescription) {
        iFragIonSpectrumDescription = aFragIonSpectrumDescription;
    }

    public double getFragIonMz() {
        return iFragIonMz;
    }

    public void setIFragIonMz(double aFragIonMz) {
        iFragIonMz = aFragIonMz;
    }

    public int getFragIonCharge() {
        return iFragIonCharge;
    }

    public void setFragIonCharge(int aFragIonCharge) {
        iFragIonCharge = aFragIonCharge;
    }

    public double getHyper_a0() {
        return iHyper_a0;
    }

    public void setHyper_a0(double aHyper_a0) {
        iHyper_a0 = aHyper_a0;
    }

    public double getHyper_a1() {
        return iHyper_a1;
    }

    public void setHyper_a1(double aHyper_a1) {
        iHyper_a1 = aHyper_a1;
    }

    public ArrayList<Integer> getXValuesHyperscore() {
        return iXValuesHyperscore;
    }

    public void setXValuesHyperscore(ArrayList<Integer> valuesHyperscore) {
        iXValuesHyperscore = valuesHyperscore;
    }

    public ArrayList<Integer> getYValuesHyperscore() {
        return iYValuesHyperscore;
    }

    public void setYValuesHyperscore(ArrayList<Integer> valuesHyperscore) {
        iYValuesHyperscore = valuesHyperscore;
    }

    public ArrayList<Integer> getXValuesConvolute() {
        return iXValuesConvolute;
    }

    public void setXValuesConvolute(ArrayList<Integer> valuesConvolute) {
        iXValuesConvolute = valuesConvolute;
    }

    public ArrayList<Integer> getYValuesConvolute() {
        return iYValuesConvolute;
    }

    public void setYValuesConvolute(ArrayList<Integer> valuesConvolute) {
        iYValuesConvolute = valuesConvolute;
    }

    public ArrayList<Integer> getXValuesaIons() {
        return iXValuesaIons;
    }

    public void setXValuesaIons(ArrayList<Integer> aValuesaIons) {
        iXValuesaIons = aValuesaIons;
    }

    public ArrayList<Integer> getYValuesaIons() {
        return iYValuesaIons;
    }

    public void setYValuesaIons(ArrayList<Integer> aValuesaIons) {
        iYValuesaIons = aValuesaIons;
    }

    public ArrayList<Integer> getXValuesbIons() {
        return iXValuesbIons;
    }

    public void setXValuesbIons(ArrayList<Integer> valuesbIons) {
        iXValuesbIons = valuesbIons;
    }

    public ArrayList<Integer> getYValuesbIons() {
        return iYValuesbIons;
    }

    public void setYValuesbIons(ArrayList<Integer> valuesbIons) {
        iYValuesbIons = valuesbIons;
    }

    public ArrayList<Integer> getXValuesyIons() {
        return iXValuesyIons;
    }

    public void setXValuesyIons(ArrayList<Integer> valuesyIons) {
        iXValuesyIons = valuesyIons;
    }

    public ArrayList<Integer> getYValuesyIons() {
        return iYValuesyIons;
    }

    public void setYValuesyIons(ArrayList<Integer> valuesyIons) {
        iYValuesyIons = valuesyIons;
    }

    public ArrayList<Integer> getYValuescIons() {
        return iYValuescIons;
    }

    public void setYValuescIons(ArrayList<Integer> iYValuescIons) {
        this.iYValuescIons = iYValuescIons;
    }

    public ArrayList<Integer> getXValuescIons() {
        return iXValuescIons;
    }

    public void setXValuescIons(ArrayList<Integer> iXValuescIons) {
        this.iXValuescIons = iXValuescIons;
    }

    public ArrayList<Integer> getXValuesxIons() {
        return iXValuesxIons;
    }

    public void setXValuesxIons(ArrayList<Integer> iXValuesxIons) {
        this.iXValuesxIons = iXValuesxIons;
    }

    public ArrayList<Integer> getYValuesxIons() {
        return iYValuesxIons;
    }

    public void setYValuesxIons(ArrayList<Integer> iYValuesxIons) {
        this.iYValuesxIons = iYValuesxIons;
    }

    public ArrayList<Integer> getXValueszIons() {
        return iXValueszIons;
    }

    public void setXValueszIons(ArrayList<Integer> iXValueszIons) {
        this.iXValueszIons = iXValueszIons;
    }

    public ArrayList<Integer> getYValueszIons() {
        return iYValueszIons;
    }

    public void setYValueszIons(ArrayList<Integer> iYValueszIons) {
        this.iYValueszIons = iYValueszIons;
    }

    public ArrayList<Double> getXValuesFragIonMass2Charge() {
        return iXValuesFragIonMass2Charge;
    }

    public void setXValuesFragIonMass2Charge(
            ArrayList<Double> valuesFragIonMass2Charge) {
        iXValuesFragIonMass2Charge = valuesFragIonMass2Charge;
    }

    public ArrayList<Double> getYValuesFragIonMass2Charge() {
        return iYValuesFragIonMass2Charge;
    }

    public void setYValuesFragIonMass2Charge(ArrayList<Double> aValuesFragIonMass2Charge) {
        iYValuesFragIonMass2Charge = aValuesFragIonMass2Charge;
    }

    /**
     * Splits a given number string into a separate Integer values,
     * which are stored in an array list.
     *
     * @param aNumberString Number string
     * @return NumberList
     */
    public ArrayList<Integer> splitStringToIntegerList(String aNumberString) {
        ArrayList<Integer> NumberList = new ArrayList<Integer>();
        String[] temp;
        aNumberString = aNumberString.trim();
        aNumberString = aNumberString.replaceAll("\n", " ");
        temp = aNumberString.split(" ");

        for (String string : temp) {
            if(string.length()>0){
                NumberList.add(Integer.parseInt((string)));
            }            
        }
        return NumberList;
    }

    /**
     * Splits a given number string into a separate Double values,
     * which are stored in an array list.
     * 
     * @param aNumberString Number string
     * @return NumberList
     */
    public ArrayList<Double> splitStringToDoubleList(String aNumberString) {
        ArrayList<Double> NumberList = new ArrayList<Double>();
        String[] temp;
        aNumberString = aNumberString.trim();
        aNumberString = aNumberString.replaceAll("\n", " ");
        temp = aNumberString.split(" ");

        for (String string : temp) {
            NumberList.add(Double.parseDouble((string)));
        }
        return NumberList;
    }
}
