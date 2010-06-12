package main.java;

import de.proteinms.xtandemparser.xtandem.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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
            xTandemFile = new XTandemFile("C:/Users/User/workspace/XTandem-Parser/orbitrap001756.xml");           
        } catch (Exception e) {
                    e.printStackTrace();
                    fail();
        }
    }

    /**
     * Test the SAMPLER peptide with the charge of 1.
     * --> Expected values are taken from protein prospector as reference tool
     */
    public void testSamplerPeptideCharged1() {
        try {
            Peptide peptide = new Peptide("1", 0, 6, "SAMPLER");
            peptide.setDomainSequence("SAMPLER");
            peptide.setDomainMh(803.4080);
            InSilicoDigester digester = new InSilicoDigester(peptide, null, XTandemFile.getMassesMap(), 1);           
            FragmentIon[] bIons = digester.getTheoreticIons(FragmentIon.B_ION);           
            FragmentIon[] yIons = digester.getTheoreticIons(FragmentIon.Y_ION);
            FragmentIon[] bH20Ions = digester.getTheoreticIons(FragmentIon.BH2O_ION);
            FragmentIon[] yNH3Ions = digester.getTheoreticIons(FragmentIon.YNH3_ION);
            FragmentIon[] mhIons = digester.getTheoreticIons(FragmentIon.MH_ION);
            FragmentIon[] mhH20Ions = digester.getTheoreticIons(FragmentIon.MHH2O_ION);
            FragmentIon[] mhNH3Ions = digester.getTheoreticIons(FragmentIon.MHNH3_ION);

            // MH Ions
            assertEquals(803.4080, mhIons[0].getMZ());
            assertEquals(785.3974, mhH20Ions[0].getMZ(), 0.001);
            assertEquals(786.3815, mhNH3Ions[0].getMZ(), 0.001);

            // Test all bIons
            assertEquals(159.076963, bIons[0].getMZ());
            assertEquals(290.117448, bIons[1].getMZ());
            assertEquals(387.170212, bIons[2].getMZ());
            assertEquals(500.254276, bIons[3].getMZ());
            assertEquals(629.296869, bIons[4].getMZ());
            assertEquals(785.397980, bIons[5].getMZ(), 0.000001);

            // Test all yIons
            assertEquals(175.119496, yIons[0].getMZ());
            assertEquals(304.162089, yIons[1].getMZ(), 0.000001);
            assertEquals(417.246153, yIons[2].getMZ(), 0.000001);
            assertEquals(514.298917, yIons[3].getMZ(), 0.000001);
            assertEquals(645.339402, yIons[4].getMZ(), 0.000001);
            assertEquals(716.376512, yIons[5].getMZ());

            // Test all bH20Ions
            assertEquals(141.066403, bH20Ions[0].getMZ());
            assertEquals(272.106888, bH20Ions[1].getMZ());
            assertEquals(369.159652, bH20Ions[2].getMZ());
            assertEquals(482.243716, bH20Ions[3].getMZ());
            assertEquals(611.286309, bH20Ions[4].getMZ());
            assertEquals(767.38742, bH20Ions[5].getMZ());

            // Test all yNH3Ions
            assertEquals(158.0924, yNH3Ions[0].getMZ(), 0.001);
            assertEquals(287.1350, yNH3Ions[1].getMZ(), 0.001);
            assertEquals(400.2191, yNH3Ions[2].getMZ(), 0.001);
            assertEquals(497.2718, yNH3Ions[3].getMZ(), 0.001);
            assertEquals(628.3123, yNH3Ions[4].getMZ(), 0.001);
            assertEquals(699.3494, yNH3Ions[5].getMZ(), 0.001);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
