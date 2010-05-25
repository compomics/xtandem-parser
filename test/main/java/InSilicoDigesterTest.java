package main.java;

import de.proteinms.xtandemparser.xtandem.*;
import org.xml.sax.SAXException;
import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Thilo Muth
 * Date: 23.05.2010
 * Time: 14:49:36
 * To change this template use File | Settings | File Templates.
 */
public class InSilicoDigesterTest extends TestCase {

    XTandemFile xTandemFile;
    
    public InSilicoDigesterTest(){
        try {
            xTandemFile = new XTandemFile("C:/Users/User/workspace/XTandem-Parser/test.xml");           
        } catch (Exception e) {
                    e.printStackTrace();
                    fail();
        }
    }

      /**
     * Test the support data.
     */
	public void testDigester() {
		try {
		 PeptideMap pepMap = xTandemFile.getPeptideMap();
             ArrayList<Peptide> pepList = pepMap.getAllPeptides(1);
                    for (Peptide peptide : pepList) {
                        InSilicoDigester digester = new InSilicoDigester(peptide, xTandemFile.getModificationMap(), xTandemFile.getMassesMap());
                        System.out.println(peptide.getDomainSequence());
                        FragmentIon[] bIons = digester.getBIons();
                        FragmentIon[] bH20Ions = digester.getBH2Oions();
                        FragmentIon[] bNH3Ions = digester.getBNH3ions();
                        System.out.println("b-Ions");
                        for(FragmentIon bIon : bIons){
                            System.out.println("m/z: " + bIon.getMZ());
                            System.out.println("intensity: " + bIon.getIntensity());
                        }
                        System.out.println("b-H20Ions");
                        for(FragmentIon bH20Ion : bH20Ions){
                            System.out.println("m/z: " + bH20Ion.getMZ());
                            System.out.println("intensity: " + bH20Ion.getIntensity());
                        }
                        System.out.println("b-NH3Ions");
                        for(FragmentIon bNH3Ion : bNH3Ions){
                            System.out.println("m/z: " + bNH3Ion.getMZ());
                            System.out.println("intensity: " + bNH3Ion.getIntensity());
                        }
                    }
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


}
