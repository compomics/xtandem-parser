package main.java;

import de.proteinms.xtandemparser.xtandem.*;
import de.proteinms.xtandemparser.interfaces.Modification;
import org.junit.Test;
import static org.junit.Assert.fail;
import org.xml.sax.SAXException;

import java.util.ArrayList;

public class XTandemFileTest {

    XTandemFile xTandemFile;
    public XTandemFileTest(){
        try {
            xTandemFile = new XTandemFile("C:/Users/User/IntelliJ_workspace/XTandem-Parser/orbitrap001756.xml");
            //xTandemFile = new XTandemFile("C:/Users/User/IntelliJ_workspace/XTandem-Parser/orbitrap001888.xml");

        } catch (SAXException saxException) {
                    saxException.getMessage();
        }
    }

    /**
     * Test the support data.
     */
    @Test
	public void testSupportData() {
		try {
		xTandemFile.getSupportData(1);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

    @Test
    public void testVariableModificationMap() {
        try {
            ModificationMap modMap = xTandemFile.getModificationMap();
            ArrayList<Modification> varModList = modMap.getVariableModifications("2433.1.1");
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

     @Test
    public void testFixedModificationMap() {
        try {
            ModificationMap modMap = xTandemFile.getModificationMap();
            ArrayList<Modification> fixedModList = modMap.getFixedModifications("2433.1.1");
            if (fixedModList.size() > 0) {
                for (Modification fixedMod : fixedModList) {
                    System.out.println("varMod name: " + fixedMod.getName());
                    System.out.println("varMod mass: " + fixedMod.getMass());
                    System.out.println("varMod location: " + fixedMod.getLocation());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testInputParams() {
        try {
            InputParams inputParams = xTandemFile.getInputParameters();            
            System.out.println("modification mass: " + inputParams.getResidueModMass());
            System.out.println("potential modification mass: " + inputParams.getResiduePotModMass());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}