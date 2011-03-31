package main.java;

import de.proteinms.xtandemparser.xtandem.Domain;
import de.proteinms.xtandemparser.xtandem.Peptide;
import de.proteinms.xtandemparser.xtandem.PeptideMap;
import de.proteinms.xtandemparser.xtandem.XTandemFile;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Thilo Muth
 * Date: 31.03.2011
 * Time: 11:58:57
 * To change this template use File | Settings | File Templates.
 */
public class MultipleDomainsTest extends TestCase {

    private XTandemFile xTandemFile;

    public MultipleDomainsTest() {
        try {
            xTandemFile = new XTandemFile("C:/Users/User/workspace/XTandem-Parser/merge55.xml");
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    /**
     * Test XML test file merge55.xml
     */
    public void testFile55() {
        try {
                PeptideMap pepMap = xTandemFile.getPeptideMap();
                ArrayList<Peptide> pepList = pepMap.getAllPeptides(156);
                for (Peptide peptide : pepList){
                    System.out.println(peptide.toString());
                    assertEquals(156, peptide.getSpectrumNumber());

                    List<Domain> domains = peptide.getDomains();
                    for (Domain domain : domains){
                        System.out.println("domainID: " + domain.getDomainID());
                        System.out.println("domainStart: " + domain.getDomainStart());
                        System.out.println("domainEnd: " + domain.getDomainEnd());
                        System.out.println("domainSequence: " + domain.getDomainSequence());
                    }
                }
            } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
