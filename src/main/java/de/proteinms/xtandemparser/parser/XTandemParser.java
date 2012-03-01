package de.proteinms.xtandemparser.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class extracts information from the xtandem output xml.
 *
 * @author Thilo Muth
 */
public class XTandemParser implements Serializable {

    /**
     * Pattern to extract the modification mass number if multiple modification
     * masses are given
     */
    private static Pattern resModificationMassPattern = Pattern.compile("label=\"residue, modification mass (\\d+)\"");
    private static Pattern refPotModificationMassPattern = Pattern.compile("label=\"refine, potential modification mass (\\d+)\"");
    private static Pattern refPotModificationMotifPattern = Pattern.compile("label=\"refine, potential modification motif (\\d+)\"");
    /**
     * This variable holds the total number of spectra in the xtandem file
     */
    private int iNumberOfSpectra = 0;
    /**
     * This map contains the key/value pairs of the input parameters.
     */
    private HashMap<String, String> iInputParamMap = null;
    /**
     * This map contains the key/value pairs of the perform parameters.
     */
    private HashMap<String, String> iPerformParamMap = null;
    /**
     * This map contains the key/value pairs of the modification information.
     */
    private HashMap<String, String> iRawModMap = null;
    /**
     * This map contains the key/value pairs of the spectra information.
     */
    private HashMap<String, String> iRawSpectrumMap = null;
    /**
     * This map contains the key/value pairs of the protein information.
     */
    private HashMap<String, String> iRawProteinMap = null;
    /**
     * This map contains the key/value pairs of the peptide information.
     */
    private HashMap<String, String> iRawPeptideMap = null;
    /**
     * This map contains the key/value pairs of the support data information.
     */
    private HashMap<String, String> iSupportDataMap = null;
    /**
     * This list contains a list with all the protein ids.
     */
    private ArrayList<String> iProteinIDList = null;
    private HashMap<String, Integer> iTitle2SpectrumIDMap;

    /**
     * Constructor for parsing a result file stored locally.
     *
     * @param aFile The input XML file.
     * @exception IOException
     * @exception SAXException
     */
    public XTandemParser(File aFile) throws IOException, SAXException {
        this.parseXTandemFile(aFile);
    }

    /**
     * In this method the X!Tandem file gets parsed.
     *
     * @param aInputFile The file which will be parsed.
     * @exception IOException
     * @exception SAXException
     */
    private void parseXTandemFile(File aInputFile) throws IOException, SAXException {

        NodeList idNodes, proteinNodes, peptideNodes, nodes, parameterNodes, supportDataNodes, xDataNodes, yDataNodes;
        NodeList hyperNodes, convolNodes, aIonNodes, bIonNodes, cIonNodes, xIonNodes, yIonNodes, zIonNodes, fragIonNodes;
        DocumentBuilderFactory dbf;
        DocumentBuilder db;
        Document dom;
        Element docEle;


        // Modifications: Specific to residues within a domain
        String modificationName;
        double modificationMass;
        NamedNodeMap modificationMap;

        try {

            // Get the document builder factory
            dbf = DocumentBuilderFactory.newInstance();

            dbf.setValidating(false);
            dbf.setAttribute("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            dbf.setAttribute("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            dbf.setAttribute("http://xml.org/sax/features/validation", false);

            // Using factory to get an instance of document builder
            db = dbf.newDocumentBuilder();

            // Parse using builder to get DOM representation of the XML file
            dom = db.parse(aInputFile);

            // Get the root elememt
            docEle = dom.getDocumentElement();

            // Get all the nodes
            nodes = docEle.getChildNodes();

            // Initialize the maps
            iInputParamMap = new HashMap<String, String>();
            iPerformParamMap = new HashMap<String, String>();
            iRawModMap = new HashMap<String, String>();
            iRawSpectrumMap = new HashMap<String, String>();
            iRawPeptideMap = new HashMap<String, String>();
            iRawProteinMap = new HashMap<String, String>();
            iSupportDataMap = new HashMap<String, String>();
            iTitle2SpectrumIDMap = new HashMap<String, Integer>();

            // List of all the protein ids
            iProteinIDList = new ArrayList<String>();
            boolean aIonFlag = false;
            boolean bIonFlag = false;
            boolean cIonFlag = false;
            boolean xIonFlag = false;
            boolean yIonFlag = false;
            boolean zIonFlag = false;

            int spectraCounter = 0;

            // Parse the parameters first
            for (int i = 0; i < nodes.getLength(); i++) {
                if (nodes.item(i).getAttributes() != null) {
                    if (nodes.item(i).getAttributes().getNamedItem("type") != null) {
                        // Parse the input parameters
                        if (nodes.item(i).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase("parameters")
                                && (nodes.item(i).getAttributes().getNamedItem("label").getNodeValue().equalsIgnoreCase("input parameters")
                                || nodes.item(i).getAttributes().getNamedItem("label").getNodeValue().equalsIgnoreCase("unused input parameters"))) {
                            parameterNodes = nodes.item(i).getChildNodes();

                            // Iterate over all the parameter nodes
                            for (int m = 0; m < parameterNodes.getLength(); m++) {
                                if (parameterNodes.item(m).getAttributes() != null) {
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"list path, default parameters\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("DEFAULTPARAMPATH", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"list path, taxonomy information\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("TAXONOMYINFOPATH", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"output, histogram column width\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("HISTOCOLWIDTH", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"output, histograms\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("HISTOEXIST", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"output, logpath\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("LOGPATH", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"output, maximum valid expectation value\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("MAXVALIDEXPECT", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"output, message\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("OUTPUTMESSAGE", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"output, one sequence copy\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("ONESEQCOPY", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"output, parameters\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("OUTPUTPARAMS", parameterNodes.item(m).getTextContent());
                                        }
                                    }

                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"output, path\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("OUTPUTPATH", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"output, path hashing\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("OUTPUTPATHHASH", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"output, performance\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("OUTPUTPERFORMANCE", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"output, proteins\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("OUTPUTPROTEINS", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"output, results\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("OUTPUTRESULTS", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"output, sequence path\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("OUTPUTSEQPATH", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"output, sequences\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("OUTPUTSEQUENCES", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"output, sort results by\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("OUTPUTSORTRESULTS", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"output, spectra\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("OUTPUTSPECTRA", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"output, xsl path\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("OUTPUTSXSLPATH", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"protein, C-terminal residue modification mass\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("C_TERMRESMODMASS", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"protein, N-terminal residue modification mass\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("N_TERMRESMODMASS", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"protein, cleavage C-terminal mass change\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("C_TERMCLEAVMASSCHANGE", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"protein, cleavage N-terminal mass change\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("N_TERMCLEAVMASSCHANGE", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"protein, cleavage site\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("CLEAVAGESITE", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"protein, homolog management\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("HOMOLOGMANAGE", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"protein, modified residue mass file\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("MODRESMASSFILE", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"protein, taxon\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("TAXON", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"refine\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("REFINE", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"refine, maximum valid expectation value\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("REFINEMAXVALIDEXPECT", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"refine, modification mass\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("REFINEMODMASS", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"refine, point mutations\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("POINTMUTATIONS", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"refine, potential C-terminus modifications\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("POTC_TERMMODS", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"refine, potential N-terminus modifications\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("POTN_TERMMODS", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"refine, potential modification mass\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("POTMODMASS", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    // parse refine, potential modification mass [1-n]
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().toLowerCase().startsWith(
                                            "label=\"refine, potential modification mass ")) {
                                        // get the mod number
                                        Matcher matcher = refPotModificationMassPattern.matcher(parameterNodes.item(m).getAttributes().getNamedItem("label").toString().toLowerCase());

                                        if (matcher.find() && !parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("POTMODMASS_" + matcher.group(1), parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"refine, potential modification motif\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("POTMODMOTIF", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    // parse refine, potential modification motif [1-n]
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().toLowerCase().startsWith(
                                            "label=\"refine, potential modification motif ")) {
                                        // get the mod number
                                        Matcher matcher = refPotModificationMotifPattern.matcher(parameterNodes.item(m).getAttributes().getNamedItem("label").toString().toLowerCase());

                                        if (matcher.find() && !parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("POTMODMOTIF_" + matcher.group(1), parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"refine, sequence path\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("REFINESEQPATH", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"refine, spectrum synthesis\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("REFINESPECSYTNH", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"refine, tic percent\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("REFINETIC", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"refine, unanticipated cleavage\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("REFINEUNANTICLEAV", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"refine, use potential modifications for full refinement\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("POTMODSFULLREFINE", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"residue, modification mass\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("RESIDUEMODMASS", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    // parse residue, modification mass [1-n]
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().toLowerCase().startsWith(
                                            "label=\"residue, modification mass ")) {
                                        // get the mod number
                                        Matcher matcher = resModificationMassPattern.matcher(parameterNodes.item(m).getAttributes().getNamedItem("label").toString().toLowerCase());

                                        if (matcher.find() && !parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("RESIDUEMODMASS_" + matcher.group(1), parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"residue, potential modification mass\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("RESIDUEPOTMODMASS", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"residue, potential modification motif\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("RESIDUEPOTMODMOTIV", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"scoring, a ions\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SCORING_AIONS", parameterNodes.item(m).getTextContent());
                                            if (parameterNodes.item(m).getTextContent().equals("yes")) {
                                                aIonFlag = true;
                                            }
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"scoring, b ions\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SCORING_BIONS", parameterNodes.item(m).getTextContent());
                                            if (parameterNodes.item(m).getTextContent().equals("yes")) {
                                                bIonFlag = true;
                                            }

                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"scoring, c ions\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SCORING_CIONS", parameterNodes.item(m).getTextContent());
                                            if (parameterNodes.item(m).getTextContent().equals("yes")) {
                                                cIonFlag = true;
                                            }
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"scoring, cyclic permutation\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SCORINGCYCLPERM", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"scoring, include reverse\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SCORINGINCREV", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"scoring, maximum missed cleavage sites\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SCORINGMISSCLEAV", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"scoring, minimum ion count\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SCORINGMINIONCOUNT", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"scoring, pluggable scoring\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SCORINGPLUGSCORING", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"scoring, x ions\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SCORING_XIONS", parameterNodes.item(m).getTextContent());
                                            if (parameterNodes.item(m).getTextContent().equals("yes")) {
                                                xIonFlag = true;
                                            }

                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"scoring, y ions\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SCORING_YIONS", parameterNodes.item(m).getTextContent());
                                            if (parameterNodes.item(m).getTextContent().equals("yes")) {
                                                yIonFlag = true;
                                            }
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"scoring, z ions\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SCORING_ZIONS", parameterNodes.item(m).getTextContent());
                                            if (parameterNodes.item(m).getTextContent().equals("yes")) {
                                                zIonFlag = true;
                                            }
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                    		"label=\"scoring, algorithm\"")) {
                                    	if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SCORING_ALGORITHM", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"spectrum, dynamic range\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SPECDYNRANGE", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"spectrum, fragment mass type\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SPECFRAGMASSTYPE", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"spectrum, fragment monoisotopic mass error\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SPECMONOISOMASSERROR", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"spectrum, fragment monoisotopic mass error units\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SPECMONOISOMASSERRORUNITS", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"spectrum, maximum parent charge\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SPECMAXPRECURSORCHANGE", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"spectrum, minimum fragment mz\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SPECMINFRAGMZ", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"spectrum, minimum parent m+h\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SPECMINPRECURSORMZ", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"spectrum, minimum peaks\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SPECMINPEAKS", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"spectrum, parent monoisotopic mass error minus\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SPECPARENTMASSERRORMINUS", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"spectrum, parent monoisotopic mass error plus\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SPECPARENTMASSERRORPLUS", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"spectrum, parent monoisotopic mass error units\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SPECPARENTMASSERRORUNITS", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"spectrum, parent monoisotopic mass isotope error\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SPECPARENTMASSISOERROR", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"spectrum, path\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SPECTRUMPATH", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"spectrum, sequence batch size\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SPECBATCHSIZE", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"spectrum, threads\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SPECTHREADS", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"spectrum, total peaks\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SPECTOTALPEAK", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"spectrum, use noise suppression\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iInputParamMap.put("SPECUSENOISECOMP", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                }
                            }
                        }
                        // Parse the performance parameters
                        if (nodes.item(i).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase("parameters")
                                && nodes.item(i).getAttributes().getNamedItem("label").getNodeValue().equalsIgnoreCase("performance parameters")) {
                            parameterNodes = nodes.item(i).getChildNodes();

                            // Iterate over all the parameter nodes
                            for (int m = 0; m < parameterNodes.getLength(); m++) {
                                if (parameterNodes.item(m).getAttributes() != null) {
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"list path, sequence source #1\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("SEQSRC1", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"list path, sequence source #2\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("SEQSRC2", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"list path, sequence source #3\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("SEQSRC3", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"list path, sequence source description #1\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("SEQSRCDESC1", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"list path, sequence source description #2\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("SEQSRCDESC2", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"list path, sequence source description #3\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("SEQSRCDESC3", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"modelling, estimated false positives\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("ESTFP", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"modelling, spectrum noise suppression ratio\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("NOISESUPP", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"modelling, total peptides used\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("TOTALPEPUSED", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"modelling, total proteins used\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("TOTALPROTUSED", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"modelling, total spectra assigned\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("TOTALSPECASS", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"modelling, total spectra used\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("TOTALSPECUSED", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"modelling, total unique assigned\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("TOTALUNIQUEASS", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"process, start time\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("PROCSTART", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"process, version\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("PROCVER", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"quality values\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("QUALVAL", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"refining, # input models\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("INPUTMOD", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"refining, # input spectra\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("INPUTSPEC", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"refining, # partial cleavage\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("PARTCLEAV", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"refining, # point mutations\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("POINTMUT", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"refining, # potential C-terminii\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("POTC_TERM", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"refining, # potential N-terminii\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("POTN_TERM", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"refining, # unanticipated cleavage\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("UNANTICLEAV", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"timing, initial modelling total (sec)\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("INITMODELTOTALTIME", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"timing, initial modelling/spectrum (sec)\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("INITMODELSPECTIME", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"timing, load sequence models (sec)\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("LOADSEQMODELTIME", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                    if (parameterNodes.item(m).getAttributes().getNamedItem("label").toString().equalsIgnoreCase(
                                            "label=\"timing, refinement/spectrum (sec)\"")) {
                                        if (!parameterNodes.item(m).getTextContent().equals("")) {
                                            iPerformParamMap.put("REFINETIME", parameterNodes.item(m).getTextContent());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Iterate over all the nodes
            for (int i = 0; i < nodes.getLength(); i++) {
                if (nodes.item(i).getAttributes() != null) {
                    if (nodes.item(i).getAttributes().getNamedItem("type") != null) {

                        // The model group contains all information about a single peptide identification
                        if (nodes.item(i).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase("model")) {
                            spectraCounter++;

                            // id is the number associated with the mass spectrum that was identified
                            if (nodes.item(i).getAttributes().getNamedItem("id") != null) {
                                iRawSpectrumMap.put("id" + spectraCounter, nodes.item(i).getAttributes().getNamedItem("id").getNodeValue());
                            }
                            // mh is the parent/precursor ion mass from the spectrum
                            if (nodes.item(i).getAttributes().getNamedItem("mh") != null) {
                                iRawSpectrumMap.put("mh" + spectraCounter, nodes.item(i).getAttributes().getNamedItem("mh").getNodeValue());
                            }
                            // z is the parent/precursor ion charge
                            if (nodes.item(i).getAttributes().getNamedItem("z") != null) {
                                iRawSpectrumMap.put("z" + spectraCounter, nodes.item(i).getAttributes().getNamedItem("z").getNodeValue());
                            }
                            // rt is the parent/precursor retention time
                            if (nodes.item(i).getAttributes().getNamedItem("rt") != null) {
                                iRawSpectrumMap.put("rt" + spectraCounter, nodes.item(i).getAttributes().getNamedItem("rt").getNodeValue());
                            }
                            // expect is the expectation value for the top ranked protein identfied with this spectrum
                            if (nodes.item(i).getAttributes().getNamedItem("expect") != null) {
                                iRawSpectrumMap.put("expect" + spectraCounter, nodes.item(i).getAttributes().getNamedItem("expect").getNodeValue());
                            }
                            // label is the text from the protein sequence FASTA file description line for the top ranked protein identified
                            if (nodes.item(i).getAttributes().getNamedItem("label") != null) {
                                iRawSpectrumMap.put("label" + spectraCounter, nodes.item(i).getAttributes().getNamedItem("label").getNodeValue());
                            }
                            // sumI is the log10-value of the sum of all of the fragment ion intensities
                            if (nodes.item(i).getAttributes().getNamedItem("sumI") != null) {
                                iRawSpectrumMap.put("sumI" + spectraCounter, nodes.item(i).getAttributes().getNamedItem("sumI").getNodeValue());
                            }
                            // maxI is the maximum fragment ion intensity
                            if (nodes.item(i).getAttributes().getNamedItem("maxI") != null) {
                                iRawSpectrumMap.put("maxI" + spectraCounter, nodes.item(i).getAttributes().getNamedItem("maxI").getNodeValue());
                            }
                            // fI is a multiplier to convert the normalized spectrum back to the original intensity values
                            if (nodes.item(i).getAttributes().getNamedItem("fI") != null) {
                                iRawSpectrumMap.put("fI" + spectraCounter, nodes.item(i).getAttributes().getNamedItem("fI").getNodeValue());
                            }
                        }
                    }
                }

                // Calculate the number of spectra.
                iNumberOfSpectra = spectraCounter;

                // Get the identifications
                idNodes = nodes.item(i).getChildNodes();

                int p_counter = 0;
                // Iterate over all the child nodes
                for (int j = 0; j < idNodes.getLength(); j++) {

                    if (idNodes.item(j).getNodeName().equalsIgnoreCase("protein")) {
                        p_counter++;
                        // the identifier of this particular identification (spectrum#).(id#)
                        String protID = idNodes.item(j).getAttributes().getNamedItem("id").getNodeValue();
                        iProteinIDList.add(protID);

                        // a unique number of this protein, calculated by the search engine
                        iRawProteinMap.put("uid" + protID, idNodes.item(j).getAttributes().getNamedItem("uid").getNodeValue());

                        // the log10 value of the expection value of the protein
                        iRawProteinMap.put("expect" + protID, idNodes.item(j).getAttributes().getNamedItem("expect").getNodeValue());

                        // the description line from the FASTA file
                        iRawProteinMap.put("label" + protID, idNodes.item(j).getAttributes().getNamedItem("label").getNodeValue());

                        // the sum of all of the fragment ions that identify this protein
                        iRawProteinMap.put("sumI" + protID, idNodes.item(j).getAttributes().getNamedItem("sumI").getNodeValue());

                        proteinNodes = idNodes.item(j).getChildNodes();

                        // Iterate over all the protein nodes
                        for (int k = 0; k < proteinNodes.getLength(); k++) {
                            if (proteinNodes.item(k).getNodeName().equalsIgnoreCase("file")) {
                                // the path used to the original fasta file
                                iRawPeptideMap.put("URL" + "_s" + spectraCounter + "_p" + p_counter, proteinNodes.item(k).getAttributes().getNamedItem("URL").getNodeValue());
                            }

                            // the the sum of all the fragment ions that identify this protein
                            if (proteinNodes.item(k).getNodeName().equalsIgnoreCase("peptide")) {
                                iRawPeptideMap.put("s" + spectraCounter + "_p" + p_counter, protID);
                                iRawPeptideMap.put("start" + "_s" + spectraCounter + "_p"
                                        + p_counter, proteinNodes.item(k).getAttributes().getNamedItem("start").getNodeValue());
                                iRawPeptideMap.put("end" + "_s" + spectraCounter + "_p"
                                        + p_counter, proteinNodes.item(k).getAttributes().getNamedItem("end").getNodeValue());
                                iRawPeptideMap.put("seq" + "_s" + spectraCounter + "_p"
                                        + p_counter, proteinNodes.item(k).getTextContent());

                                peptideNodes = proteinNodes.item(k).getChildNodes();

                                // Domain counter
                                int dCount = 0;

                                // Iterate over all the peptide nodes
                                for (int m = 0; m < peptideNodes.getLength(); m++) {
                                    // Get the domain entries
                                    if (peptideNodes.item(m).getNodeName().equalsIgnoreCase("domain")) {
                                        dCount++;
                                        // Get the domainid
                                        String domainID = peptideNodes.item(m).getAttributes().getNamedItem("id").getNodeValue();
                                        iRawPeptideMap.put("s" + spectraCounter + "_p" + p_counter + "_d" + dCount, domainID);

                                        iRawPeptideMap.put("domainid" + "_s" + spectraCounter + "_p"
                                                + p_counter + "_d" + dCount, peptideNodes.item(m).getAttributes().getNamedItem("id").getNodeValue());

                                        // the start position of the peptide
                                        iRawPeptideMap.put("domainstart" + "_s" + spectraCounter + "_p"
                                                + p_counter + "_d" + dCount, peptideNodes.item(m).getAttributes().getNamedItem("start").getNodeValue());

                                        // the end position of the peptide
                                        iRawPeptideMap.put("domainend" + "_s" + spectraCounter + "_p"
                                                + p_counter + "_d" + dCount, peptideNodes.item(m).getAttributes().getNamedItem("end").getNodeValue());

                                        // the expect value
                                        iRawPeptideMap.put("expect" + "_s" + spectraCounter + "_p"
                                                + p_counter + "_d" + dCount, peptideNodes.item(m).getAttributes().getNamedItem("expect").getNodeValue());

                                        // the mass + a proton
                                        iRawPeptideMap.put("mh" + "_s" + spectraCounter + "_p"
                                                + p_counter + "_d" + dCount, peptideNodes.item(m).getAttributes().getNamedItem("mh").getNodeValue());

                                        // the mass delta
                                        iRawPeptideMap.put("delta" + "_s" + spectraCounter + "_p"
                                                + p_counter + "_d" + dCount, peptideNodes.item(m).getAttributes().getNamedItem("delta").getNodeValue());

                                        // the hyper score
                                        iRawPeptideMap.put("hyperscore" + "_s" + spectraCounter + "_p"
                                                + p_counter + "_d" + dCount, peptideNodes.item(m).getAttributes().getNamedItem("hyperscore").getNodeValue());

                                        // the next score
                                        iRawPeptideMap.put("nextscore" + "_s" + spectraCounter + "_p"
                                                + p_counter + "_d" + dCount, peptideNodes.item(m).getAttributes().getNamedItem("nextscore").getNodeValue());

                                        if (xIonFlag) {
                                            // the x score
                                            iRawPeptideMap.put("x_score" + "_s" + spectraCounter + "_p"
                                                    + p_counter + "_d" + dCount, peptideNodes.item(m).getAttributes().getNamedItem("x_score").getNodeValue());

                                            // the x ion number
                                            iRawPeptideMap.put("x_ions" + "_s" + spectraCounter + "_p"
                                                    + p_counter + "_d" + dCount, peptideNodes.item(m).getAttributes().getNamedItem("x_ions").getNodeValue());
                                        }

                                        if (yIonFlag) {
                                            // the y score
                                            iRawPeptideMap.put("y_score" + "_s" + spectraCounter + "_p"
                                                    + p_counter + "_d" + dCount, peptideNodes.item(m).getAttributes().getNamedItem("y_score").getNodeValue());

                                            // the y ion number
                                            iRawPeptideMap.put("y_ions" + "_s" + spectraCounter + "_p"
                                                    + p_counter + "_d" + dCount, peptideNodes.item(m).getAttributes().getNamedItem("y_ions").getNodeValue());
                                        }

                                        if (zIonFlag) {
                                            // the z score
                                            iRawPeptideMap.put("z_score" + "_s" + spectraCounter + "_p"
                                                    + p_counter + "_d" + dCount, peptideNodes.item(m).getAttributes().getNamedItem("z_score").getNodeValue());

                                            // the z ion number
                                            iRawPeptideMap.put("z_ions" + "_s" + spectraCounter + "_p"
                                                    + p_counter + "_d" + dCount, peptideNodes.item(m).getAttributes().getNamedItem("z_ions").getNodeValue());
                                        }

                                        if (aIonFlag) {
                                            // the a score
                                            iRawPeptideMap.put("a_score" + "_s" + spectraCounter + "_p"
                                                    + p_counter + "_d" + dCount, peptideNodes.item(m).getAttributes().getNamedItem("a_score").getNodeValue());

                                            // the a ion number
                                            iRawPeptideMap.put("a_ions" + "_s" + spectraCounter + "_p"
                                                    + p_counter + "_d" + dCount, peptideNodes.item(m).getAttributes().getNamedItem("a_ions").getNodeValue());
                                        }

                                        if (bIonFlag) {
                                            // the b score
                                            iRawPeptideMap.put("b_score" + "_s" + spectraCounter + "_p"
                                                    + p_counter + "_d" + dCount, peptideNodes.item(m).getAttributes().getNamedItem("b_score").getNodeValue());

                                            // the b ion number
                                            iRawPeptideMap.put("b_ions" + "_s" + spectraCounter + "_p"
                                                    + p_counter + "_d" + dCount, peptideNodes.item(m).getAttributes().getNamedItem("b_ions").getNodeValue());
                                        }

                                        if (cIonFlag) {
                                            // the c score
                                            iRawPeptideMap.put("c_score" + "_s" + spectraCounter + "_p"
                                                    + p_counter + "_d" + dCount, peptideNodes.item(m).getAttributes().getNamedItem("c_score").getNodeValue());

                                            // the c ion number
                                            iRawPeptideMap.put("c_ions" + "_s" + spectraCounter + "_p"
                                                    + p_counter + "_d" + dCount, peptideNodes.item(m).getAttributes().getNamedItem("c_ions").getNodeValue());
                                        }

                                        // the upstream flanking sequence
                                        iRawPeptideMap.put("pre" + "_s" + spectraCounter + "_p"
                                                + p_counter + "_d" + dCount, peptideNodes.item(m).getAttributes().getNamedItem("pre").getNodeValue());

                                        // the downstream flanking sequence
                                        iRawPeptideMap.put("post" + "_s" + spectraCounter + "_p"
                                                + p_counter + "_d" + dCount, peptideNodes.item(m).getAttributes().getNamedItem("post").getNodeValue());

                                        // the domain sequence
                                        iRawPeptideMap.put("domainseq" + "_s" + spectraCounter + "_p"
                                                + p_counter + "_d" + dCount, peptideNodes.item(m).getAttributes().getNamedItem("seq").getNodeValue());

                                        // the number of missed cleavages
                                        iRawPeptideMap.put("missed_cleavages" + "_s" + spectraCounter + "_p"
                                                + p_counter + "_d" + dCount, peptideNodes.item(m).getAttributes().getNamedItem("missed_cleavages").getNodeValue());

                                        int modCounter = 0;
                                        for (int n = 0; n < peptideNodes.item(m).getChildNodes().getLength(); n++) {

                                            // Get the specific modifications (aa)
                                            if (peptideNodes.item(m).getChildNodes().item(n).getNodeName().equalsIgnoreCase("aa")) {
                                                modCounter++;

                                                modificationMap = peptideNodes.item(m).getChildNodes().item(n).getAttributes();
                                                modificationName = modificationMap.getNamedItem("type").getNodeValue();


                                                // use the old calculation with domainStart!
                                                //modificationMap.getNamedItem("at").getNodeValue()) - domainStart + 1)
                                                iRawModMap.put("at" + "_s" + spectraCounter + "_p"
                                                        + p_counter + "_d" + dCount + "_m" + modCounter, modificationMap.getNamedItem("at").getNodeValue());

                                                // modified is the residue mass change caused by the modification
                                                modificationMass = Double.parseDouble(modificationMap.getNamedItem("modified").getNodeValue());
                                                iRawModMap.put("modified" + "_s" + spectraCounter + "_p"
                                                        + p_counter + "_d" + dCount + "_m" + modCounter, modificationMap.getNamedItem("modified").getNodeValue());

                                                modificationName = modificationMass + "@" + modificationName;

                                                // type is the single letter abbreviation for the modified residue
                                                iRawModMap.put("name" + "_s" + spectraCounter + "_p" + p_counter + "_d" + dCount + "_m" + modCounter, modificationName);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Go to the group node inside the other group node (support)
                    if (idNodes.item(j).getNodeName().equalsIgnoreCase("group")) {
                        // Start parsing the support data part (GAML histograms)
                        if (idNodes.item(j).getAttributes().getNamedItem("label").getNodeValue().equalsIgnoreCase("supporting data")) {
                            supportDataNodes = idNodes.item(j).getChildNodes();
                            // Iterate over all the support data nodes
                            for (int a = 0; a < supportDataNodes.getLength(); a++) {
                                if (supportDataNodes.item(a).getNodeName().equalsIgnoreCase("GAML:trace")) {
                                    // Parse the hyperscore expectation function values
                                    if (supportDataNodes.item(a).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase(
                                            "hyperscore expectation function")) {
                                        iSupportDataMap.put("HYPERLABEL" + "_s" + spectraCounter,
                                                supportDataNodes.item(a).getAttributes().getNamedItem("label").getNodeValue());

                                        hyperNodes = supportDataNodes.item(a).getChildNodes();
                                        // Iterate over the hyperscore nodes
                                        for (int b = 0; b < hyperNodes.getLength(); b++) {
                                            if (hyperNodes.item(b).getNodeName().equalsIgnoreCase("GAML:attribute")) {
                                                // Get the a0 value
                                                if (hyperNodes.item(b).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase("a0")) {
                                                    iSupportDataMap.put("HYPER_A0" + "_s" + spectraCounter, hyperNodes.item(b).getTextContent());
                                                }
                                                // Get the a1 value
                                                if (hyperNodes.item(b).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase("a1")) {
                                                    iSupportDataMap.put("HYPER_A1" + "_s" + spectraCounter, hyperNodes.item(b).getTextContent());
                                                }
                                            }
                                            // Get the Xdata
                                            if (hyperNodes.item(b).getNodeName().equalsIgnoreCase("GAML:Xdata")) {
                                                xDataNodes = hyperNodes.item(b).getChildNodes();
                                                for (int d = 0; d < xDataNodes.getLength(); d++) {
                                                    if (xDataNodes.item(d).getNodeName().equalsIgnoreCase("GAML:values")) {
                                                        iSupportDataMap.put("XVAL_HYPER" + "_s" + spectraCounter, xDataNodes.item(d).getTextContent());
                                                    }
                                                }
                                            }
                                            // Get the Ydata
                                            if (hyperNodes.item(b).getNodeName().equalsIgnoreCase("GAML:Ydata")) {
                                                yDataNodes = hyperNodes.item(b).getChildNodes();
                                                for (int e = 0; e < yDataNodes.getLength(); e++) {
                                                    if (yDataNodes.item(e).getNodeName().equalsIgnoreCase("GAML:values")) {
                                                        iSupportDataMap.put("YVAL_HYPER" + "_s" + spectraCounter, yDataNodes.item(e).getTextContent());
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // Parse the convolution survival funtion values
                                    if (supportDataNodes.item(a).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase(
                                            "convolution survival function")) {
                                        iSupportDataMap.put("CONVOLLABEL" + "_s" + spectraCounter,
                                                supportDataNodes.item(a).getAttributes().getNamedItem("label").getNodeValue());
                                        supportDataNodes.item(a).getChildNodes();

                                        convolNodes = supportDataNodes.item(a).getChildNodes();
                                        // Iterate over the convolution nodes
                                        for (int b = 0; b < convolNodes.getLength(); b++) {
                                            // Get the Xdata
                                            if (convolNodes.item(b).getNodeName().equalsIgnoreCase("GAML:Xdata")) {
                                                xDataNodes = convolNodes.item(b).getChildNodes();
                                                for (int d = 0; d < xDataNodes.getLength(); d++) {
                                                    if (xDataNodes.item(d).getNodeName().equalsIgnoreCase("GAML:values")) {
                                                        iSupportDataMap.put("XVAL_CONVOL" + "_s" + spectraCounter, xDataNodes.item(d).getTextContent());
                                                    }
                                                }
                                            }
                                            // Get the Ydata
                                            if (convolNodes.item(b).getNodeName().equalsIgnoreCase("GAML:Ydata")) {
                                                yDataNodes = convolNodes.item(b).getChildNodes();
                                                for (int e = 0; e < yDataNodes.getLength(); e++) {
                                                    if (yDataNodes.item(e).getNodeName().equalsIgnoreCase("GAML:values")) {
                                                        iSupportDataMap.put("YVAL_CONVOL" + "_s" + spectraCounter, yDataNodes.item(e).getTextContent());
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // Parse the a ion histogram values
                                    if (aIonFlag) {
                                        if (supportDataNodes.item(a).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase("a ion histogram")) {
                                            iSupportDataMap.put("A_IONLABEL" + "_s" + spectraCounter,
                                                    supportDataNodes.item(a).getAttributes().getNamedItem("label").getNodeValue());
                                            supportDataNodes.item(a).getChildNodes();

                                            aIonNodes = supportDataNodes.item(a).getChildNodes();

                                            // Iterate over the a ion nodes
                                            for (int b = 0; b < aIonNodes.getLength(); b++) {
                                                // Get the Xdata
                                                if (aIonNodes.item(b).getNodeName().equalsIgnoreCase("GAML:Xdata")) {
                                                    xDataNodes = aIonNodes.item(b).getChildNodes();
                                                    for (int d = 0; d < xDataNodes.getLength(); d++) {
                                                        if (xDataNodes.item(d).getNodeName().equalsIgnoreCase("GAML:values")) {
                                                            iSupportDataMap.put("XVAL_AIONS" + "_s" + spectraCounter, xDataNodes.item(d).getTextContent());
                                                        }
                                                    }
                                                }
                                                // Get the Ydata
                                                if (aIonNodes.item(b).getNodeName().equalsIgnoreCase("GAML:Ydata")) {
                                                    yDataNodes = aIonNodes.item(b).getChildNodes();
                                                    for (int e = 0; e < yDataNodes.getLength(); e++) {
                                                        if (yDataNodes.item(e).getNodeName().equalsIgnoreCase("GAML:values")) {
                                                            iSupportDataMap.put("YVAL_AIONS" + "_s"
                                                                    + spectraCounter, yDataNodes.item(e).getTextContent());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // Parse the b ion histogram values
                                    if (bIonFlag) {
                                        if (supportDataNodes.item(a).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase("b ion histogram")) {
                                            iSupportDataMap.put("B_IONLABEL" + "_s" + spectraCounter,
                                                    supportDataNodes.item(a).getAttributes().getNamedItem("label").getNodeValue());
                                            supportDataNodes.item(a).getChildNodes();

                                            bIonNodes = supportDataNodes.item(a).getChildNodes();

                                            // Iterate over the b ion nodes
                                            for (int b = 0; b < bIonNodes.getLength(); b++) {
                                                // Get the Xdata
                                                if (bIonNodes.item(b).getNodeName().equalsIgnoreCase("GAML:Xdata")) {
                                                    xDataNodes = bIonNodes.item(b).getChildNodes();
                                                    for (int d = 0; d < xDataNodes.getLength(); d++) {
                                                        if (xDataNodes.item(d).getNodeName().equalsIgnoreCase("GAML:values")) {
                                                            iSupportDataMap.put("XVAL_BIONS" + "_s"
                                                                    + spectraCounter, xDataNodes.item(d).getTextContent());
                                                        }
                                                    }
                                                }
                                                // Get the Ydata
                                                if (bIonNodes.item(b).getNodeName().equalsIgnoreCase("GAML:Ydata")) {
                                                    yDataNodes = bIonNodes.item(b).getChildNodes();
                                                    for (int e = 0; e < yDataNodes.getLength(); e++) {
                                                        if (yDataNodes.item(e).getNodeName().equalsIgnoreCase("GAML:values")) {
                                                            iSupportDataMap.put("YVAL_BIONS" + "_s"
                                                                    + spectraCounter, yDataNodes.item(e).getTextContent());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // Parse the c ion histogram values
                                    if (cIonFlag) {
                                        if (supportDataNodes.item(a).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase("c ion histogram")) {
                                            iSupportDataMap.put("C_IONLABEL" + "_s" + spectraCounter,
                                                    supportDataNodes.item(a).getAttributes().getNamedItem("label").getNodeValue());
                                            supportDataNodes.item(a).getChildNodes();

                                            cIonNodes = supportDataNodes.item(a).getChildNodes();

                                            // Iterate over the a ion nodes
                                            for (int b = 0; b < cIonNodes.getLength(); b++) {
                                                // Get the Xdata
                                                if (cIonNodes.item(b).getNodeName().equalsIgnoreCase("GAML:Xdata")) {
                                                    xDataNodes = cIonNodes.item(b).getChildNodes();
                                                    for (int d = 0; d < xDataNodes.getLength(); d++) {
                                                        if (xDataNodes.item(d).getNodeName().equalsIgnoreCase("GAML:values")) {
                                                            iSupportDataMap.put("XVAL_CIONS" + "_s"
                                                                    + spectraCounter, xDataNodes.item(d).getTextContent());
                                                        }
                                                    }
                                                }
                                                // Get the Ydata
                                                if (cIonNodes.item(b).getNodeName().equalsIgnoreCase("GAML:Ydata")) {
                                                    yDataNodes = cIonNodes.item(b).getChildNodes();
                                                    for (int e = 0; e < yDataNodes.getLength(); e++) {
                                                        if (yDataNodes.item(e).getNodeName().equalsIgnoreCase("GAML:values")) {
                                                            iSupportDataMap.put("YVAL_CIONS" + "_s"
                                                                    + spectraCounter, yDataNodes.item(e).getTextContent());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if (xIonFlag) {
                                        // Parse the x ion histogram values
                                        if (supportDataNodes.item(a).getAttributes().getNamedItem("type").getNodeValue().equals("x ion histogram")) {
                                            iSupportDataMap.put("X_IONLABEL" + "_s" + spectraCounter,
                                                    supportDataNodes.item(a).getAttributes().getNamedItem("label").getNodeValue());
                                            supportDataNodes.item(a).getChildNodes();

                                            xIonNodes = supportDataNodes.item(a).getChildNodes();
                                            // Iterate over the y ion nodes
                                            for (int b = 0; b < xIonNodes.getLength(); b++) {
                                                // Get the Xdata
                                                if (xIonNodes.item(b).getNodeName().equalsIgnoreCase("GAML:Xdata")) {
                                                    xDataNodes = xIonNodes.item(b).getChildNodes();
                                                    for (int d = 0; d < xDataNodes.getLength(); d++) {
                                                        if (xDataNodes.item(d).getNodeName().equalsIgnoreCase("GAML:values")) {
                                                            iSupportDataMap.put("XVAL_XIONS" + "_s"
                                                                    + spectraCounter, xDataNodes.item(d).getTextContent());
                                                        }
                                                    }
                                                }
                                                // Get the Ydata
                                                if (xIonNodes.item(b).getNodeName().equalsIgnoreCase("GAML:Ydata")) {
                                                    yDataNodes = xIonNodes.item(b).getChildNodes();
                                                    for (int e = 0; e < yDataNodes.getLength(); e++) {
                                                        if (yDataNodes.item(e).getNodeName().equalsIgnoreCase("GAML:values")) {
                                                            iSupportDataMap.put("YVAL_XIONS" + "_s"
                                                                    + spectraCounter, yDataNodes.item(e).getTextContent());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if (yIonFlag) {
                                        // Parse the y ion histogram values
                                        if (supportDataNodes.item(a).getAttributes().getNamedItem("type").getNodeValue().equals("y ion histogram")) {
                                            iSupportDataMap.put("Y_IONLABEL" + "_s" + spectraCounter,
                                                    supportDataNodes.item(a).getAttributes().getNamedItem("label").getNodeValue());
                                            supportDataNodes.item(a).getChildNodes();

                                            yIonNodes = supportDataNodes.item(a).getChildNodes();
                                            // Iterate over the y ion nodes
                                            for (int b = 0; b < yIonNodes.getLength(); b++) {
                                                // Get the Xdata
                                                if (yIonNodes.item(b).getNodeName().equalsIgnoreCase("GAML:Xdata")) {
                                                    xDataNodes = yIonNodes.item(b).getChildNodes();
                                                    for (int d = 0; d < xDataNodes.getLength(); d++) {
                                                        if (xDataNodes.item(d).getNodeName().equalsIgnoreCase("GAML:values")) {
                                                            iSupportDataMap.put("XVAL_YIONS" + "_s"
                                                                    + spectraCounter, xDataNodes.item(d).getTextContent());
                                                        }
                                                    }
                                                }
                                                // Get the Ydata
                                                if (yIonNodes.item(b).getNodeName().equalsIgnoreCase("GAML:Ydata")) {
                                                    yDataNodes = yIonNodes.item(b).getChildNodes();
                                                    for (int e = 0; e < yDataNodes.getLength(); e++) {
                                                        if (yDataNodes.item(e).getNodeName().equalsIgnoreCase("GAML:values")) {
                                                            iSupportDataMap.put("YVAL_YIONS" + "_s"
                                                                    + spectraCounter, yDataNodes.item(e).getTextContent());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if (zIonFlag) {
                                        // Parse the x ion histogram values
                                        if (supportDataNodes.item(a).getAttributes().getNamedItem("type").getNodeValue().equals("z ion histogram")) {
                                            iSupportDataMap.put("Z_IONLABEL" + "_s" + spectraCounter,
                                                    supportDataNodes.item(a).getAttributes().getNamedItem("label").getNodeValue());
                                            supportDataNodes.item(a).getChildNodes();

                                            zIonNodes = supportDataNodes.item(a).getChildNodes();
                                            // Iterate over the y ion nodes
                                            for (int b = 0; b < zIonNodes.getLength(); b++) {
                                                // Get the Xdata
                                                if (zIonNodes.item(b).getNodeName().equalsIgnoreCase("GAML:Xdata")) {
                                                    xDataNodes = zIonNodes.item(b).getChildNodes();
                                                    for (int d = 0; d < xDataNodes.getLength(); d++) {
                                                        if (xDataNodes.item(d).getNodeName().equalsIgnoreCase("GAML:values")) {
                                                            iSupportDataMap.put("XVAL_ZIONS" + "_s"
                                                                    + spectraCounter, xDataNodes.item(d).getTextContent());
                                                        }
                                                    }
                                                }
                                                // Get the Ydata
                                                if (zIonNodes.item(b).getNodeName().equalsIgnoreCase("GAML:Ydata")) {
                                                    yDataNodes = zIonNodes.item(b).getChildNodes();
                                                    for (int e = 0; e < yDataNodes.getLength(); e++) {
                                                        if (yDataNodes.item(e).getNodeName().equalsIgnoreCase("GAML:values")) {
                                                            iSupportDataMap.put("YVAL_ZIONS" + "_s"
                                                                    + spectraCounter, yDataNodes.item(e).getTextContent());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (idNodes.item(j).getAttributes().getNamedItem("label").getNodeValue().equalsIgnoreCase("fragment ion mass spectrum")) {

                            supportDataNodes = idNodes.item(j).getChildNodes();
                            // Iterate over all the support data nodes
                            for (int a = 0; a < supportDataNodes.getLength(); a++) {
                                if (supportDataNodes.item(a).getNodeName().equalsIgnoreCase("note")) {
                                    iSupportDataMap.put("FRAGIONSPECDESC" + "_s" + spectraCounter, supportDataNodes.item(a).getTextContent().trim());
                                    iTitle2SpectrumIDMap.put(supportDataNodes.item(a).getTextContent().trim(), spectraCounter);
                                }
                                if (supportDataNodes.item(a).getNodeName().equalsIgnoreCase("GAML:trace")) {
                                    // Parse the tandem mass spectrum values
                                    if (supportDataNodes.item(a).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase("tandem mass spectrum")) {
                                        iSupportDataMap.put("SPECTRUMLABEL" + "_s" + spectraCounter,
                                                supportDataNodes.item(a).getAttributes().getNamedItem("label").getNodeValue());
                                        supportDataNodes.item(a).getChildNodes();

                                        fragIonNodes = supportDataNodes.item(a).getChildNodes();
                                        // Iterate over the fragment ion nodes
                                        for (int b = 0; b < fragIonNodes.getLength(); b++) {
                                            if (fragIonNodes.item(b).getNodeName().equalsIgnoreCase("GAML:attribute")) {
                                                // Get the a0 value
                                                if (fragIonNodes.item(b).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase("M+H")) {
                                                    iSupportDataMap.put("FRAGIONMZ" + "_s"
                                                            + spectraCounter, fragIonNodes.item(b).getTextContent());
                                                }
                                                // Get the a1 value
                                                if (fragIonNodes.item(b).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase("charge")) {
                                                    iSupportDataMap.put("FRAGIONCHARGE" + "_s"
                                                            + spectraCounter, fragIonNodes.item(b).getTextContent());
                                                }
                                            }
                                            // Get the Xdata
                                            if (fragIonNodes.item(b).getNodeName().equalsIgnoreCase("GAML:Xdata")) {
                                                xDataNodes = fragIonNodes.item(b).getChildNodes();
                                                for (int d = 0; d < xDataNodes.getLength(); d++) {
                                                    if (xDataNodes.item(d).getNodeName().equalsIgnoreCase("GAML:values")) {
                                                        iSupportDataMap.put("XVAL_FRAGIONMZ" + "_s"
                                                                + spectraCounter, xDataNodes.item(d).getTextContent());
                                                    }
                                                }
                                            }
                                            // Get the Ydata
                                            if (fragIonNodes.item(b).getNodeName().equalsIgnoreCase("GAML:Ydata")) {
                                                yDataNodes = fragIonNodes.item(b).getChildNodes();
                                                for (int e = 0; e < yDataNodes.getLength(); e++) {
                                                    if (yDataNodes.item(e).getNodeName().equalsIgnoreCase("GAML:values")) {
                                                        iSupportDataMap.put("YVAL_FRAGIONMZ" + "_s"
                                                                + spectraCounter, yDataNodes.item(e).getTextContent());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the total number of spectra.
     *
     * @return iNumberOfSpectra
     */
    public int getNumberOfSpectra() {
        return iNumberOfSpectra;
    }

    /**
     * Returns the raw spectrum map.
     *
     * @return iRawSpectrumMap
     */
    public HashMap<String, String> getRawSpectrumMap() {
        return iRawSpectrumMap;
    }

    /**
     * Returns the raw peptide map.
     *
     * @return iRawPeptideMap
     */
    public HashMap<String, String> getRawPeptideMap() {
        return iRawPeptideMap;
    }

    /**
     * Returns the raw protein map.
     *
     * @return iRawProteinMap
     */
    public HashMap<String, String> getRawProteinMap() {
        return iRawProteinMap;
    }

    /**
     * Returns the protein id list
     *
     * @return iProteinIDList ArrayList<String>
     */
    public ArrayList<String> getProteinIDList() {
        return iProteinIDList;
    }

    /**
     * Returns the raw modification map.
     *
     * @return iRawModMap
     */
    public HashMap<String, String> getRawModMap() {
        return iRawModMap;
    }

    /**
     * Returns the performance parameters map.
     *
     * @return iPerformParamMap
     */
    public HashMap<String, String> getPerformParamMap() {
        return iPerformParamMap;
    }

    /**
     * Returns the input parameter map.
     *
     * @return iInputParamMap
     */
    public HashMap<String, String> getInputParamMap() {
        return iInputParamMap;
    }

    /**
     * Returns the support data map
     *
     * @return iSupportDataMap
     */
    public HashMap<String, String> getSupportDataMap() {
        return iSupportDataMap;
    }

    /**
     * Returns the title2spectrum id map.
     *
     * @return iTitle2SpectrumIDMap.
     */
    public HashMap<String, Integer> getTitle2SpectrumIDMap() {
        return iTitle2SpectrumIDMap;
    }
}
