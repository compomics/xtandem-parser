package main.java;

import de.proteinms.xtandemparser.xtandem.*;
import de.proteinms.xtandemparser.interfaces.Modification;
import junit.framework.TestCase;
import static junit.framework.Assert.fail;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class XTandemFileTest extends TestCase {

    XTandemFile xTandemFile;
    public XTandemFileTest(){
        try {
            xTandemFile = new XTandemFile("testFiles/orbitrap003956.xml");
            //xTandemFile = new XTandemFile("C:/Users/User/IntelliJ_workspace/XTandem-Parser/orbitrap001888.xml");

        } catch (SAXException saxException) {
                    saxException.getMessage();
        }
    }

    /**
     * Test the support data.
     */
	public void testSupportData() {
		try {
		SupportData supportData =  xTandemFile.getSupportData(962);
        String spectrumName = supportData.getFragIonSpectrumDescription();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

    public void testFixedModificationMap() {
        try {
            ModificationMap modMap = xTandemFile.getModificationMap();
            ArrayList<Modification> fixedModList = modMap.getAllFixedModifications();
            if (fixedModList.size() > 0) {
                for (Modification fixedMod : fixedModList) {
                    System.out.println("fixedMod name: " + fixedMod.getName());
                    System.out.println("fixedMod mass: " + fixedMod.getMass());
                    System.out.println("fixedMod location: " + fixedMod.getLocation());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testVariableModificationMap() {
        try {
            ModificationMap modMap = xTandemFile.getModificationMap();
            ArrayList<Modification> varModList = modMap.getAllVariableModifications();
            if (varModList.size() > 0) {
                for (Modification varMod : varModList) {
                    System.out.println("varMod name: " + varMod.getName());
                    System.out.println("varMod mass: " + varMod.getMass());
                    System.out.println("varMod location: " + varMod.getLocation());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }  

    public void testInputParamsModifications() {
        try {
            InputParams inputParams = xTandemFile.getInputParameters();            
            System.out.println("modification mass: " + inputParams.getResidueModMass());
            String modificationMasses = inputParams.getResidueModMass();
            StringTokenizer tokenizer = new StringTokenizer(modificationMasses, ",");
            while (tokenizer.hasMoreTokens()){                    
                    String[] tokens = tokenizer.nextToken().split("@");
                    System.out.println("token1: " + tokens[0]);
                    System.out.println("token2: " + tokens[1]);
            }

            
            System.out.println("potential modification mass: " + inputParams.getResiduePotModMass());
            String potentialModificationMasses = inputParams.getResiduePotModMass();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}