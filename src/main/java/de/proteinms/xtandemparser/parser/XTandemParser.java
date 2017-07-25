package de.proteinms.xtandemparser.parser;

import java.io.File;
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
     * masses are given.
     */
    private static Pattern resModificationMassPattern = Pattern.compile("label=\"residue, modification mass (\\d+)\"");
    /**
     * Pattern to extract the modification mass number if multiple modification
     * masses are given.
     */
    private static Pattern refPotModificationMassPattern = Pattern.compile("label=\"refine, potential modification mass (\\d+)\"");
    /**
     * Pattern to extract the modification mass number if multiple modification
     * masses are given.
     */
    private static Pattern refPotModificationMotifPattern = Pattern.compile("label=\"refine, potential modification motif (\\d+)\"");
    /**
     * This variable holds the total number of spectra in the xtandem file.
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
    private ArrayList<String> iProteinKeyList = null;
    /**
     * Spectrum title to X!Tandem id map.
     */
    private HashMap<String, Integer> iTitle2SpectrumIDMap;
    /**
     * X!Tandem id to spectrum title map
     */
    private HashMap<Integer, String> idToSpectrumMap;

    /**
     * Constructor for parsing a result file stored locally.
     *
     * @param aFile the input XML file
     *
     * @exception IOException if an IOException occurs
     * @exception SAXException if a SAXException occurs
     * @throws ParserConfigurationException if a ParserConfigurationException
     * occurs
     */
    public XTandemParser(File aFile) throws IOException, SAXException, ParserConfigurationException {
        this(aFile, false);
    }

    /**
     * Constructor for parsing a result file stored locally.
     *
     * @param aFile the input XML file
     * @param skipDetails if true only the spectrum identifiers, the peptides
     * sequences, modifications and matches e-values will be loaded. Plus the
     * input and performance parameters.
     *
     * @exception IOException if an IOException occurs
     * @exception SAXException if a SAXException occurs
     * @throws ParserConfigurationException if a ParserConfigurationException
     * occurs
     */
    public XTandemParser(File aFile, boolean skipDetails) throws IOException, SAXException, ParserConfigurationException {
        this.parseXTandemFile(aFile, skipDetails);
    }

    /**
     * In this method the X!Tandem file gets parsed.
     *
     * @param aInputFile the file which will be parsed
     * @param skipDetails if true only the spectrum identifiers, the peptides
     * sequences, modifications and matches e-values will be loaded. Plus the
     * input and performance parameters.
     *
     * @exception IOException if an IOException occurs
     * @exception SAXException if a SAXException occurs
     * @throws ParserConfigurationException if a ParserConfigurationException
     * occurs
     */
    private void parseXTandemFile(File aInputFile, boolean skipDetails) throws IOException, SAXException, ParserConfigurationException {

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
        idToSpectrumMap = new HashMap<Integer, String>();

        // List of all the protein ids
        iProteinKeyList = new ArrayList<String>();
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
                        boolean stayInLoop = true;
                        for (int m = 0; m < parameterNodes.getLength() && stayInLoop; m++) {
                            NamedNodeMap attributes = parameterNodes.item(m).getAttributes();
                            String itemContent = parameterNodes.item(m).getTextContent();
                            
                            if (attributes != null && !itemContent.equals("")) {
                                
                                String label = attributes.getNamedItem("label").toString().toLowerCase();
                                
                                switch (label){
                                
                                    case "label=\"spectrum, path\"":
                                        iInputParamMap.put("SPECTRUMPATH", itemContent);
                                        stayInLoop = false;
                                        break;
                                        
                                    case "label=\"list path, default parameters\"":
                                        iInputParamMap.put("DEFAULTPARAMPATH", itemContent);
                                        break;
                                        
                                    case "label=\"list path, taxonomy information\"":
                                        iInputParamMap.put("TAXONOMYINFOPATH", itemContent);
                                        break;
                                        
                                    case "label=\"output, histogram column width\"":
                                        iInputParamMap.put("HISTOCOLWIDTH", itemContent);
                                        break;
                                        
                                    case "label=\"output, histograms\"":
                                        iInputParamMap.put("HISTOEXIST", itemContent);
                                        break;
                                        
                                    case "label=\"output, logpath\"":
                                        iInputParamMap.put("LOGPATH", itemContent);
                                        break;
                                        
                                    case "label=\"output, maximum valid expectation value\"":
                                        iInputParamMap.put("MAXVALIDEXPECT", itemContent);
                                        break;
                                        
                                    case "label=\"output, message\"":
                                        iInputParamMap.put("OUTPUTMESSAGE", itemContent);
                                        break;
                                        
                                    case "label=\"output, one sequence copy\"":
                                        iInputParamMap.put("ONESEQCOPY", itemContent);
                                        break;
                                        
                                    case "label=\"output, parameters\"":
                                        iInputParamMap.put("OUTPUTPARAMS", itemContent);
                                        break;
                                        
                                    case "label=\"output, path\"":
                                        iInputParamMap.put("OUTPUTPATH", itemContent);
                                        break;
                                        
                                    case "label=\"output, path hashing\"":
                                        iInputParamMap.put("OUTPUTPATHHASH", itemContent);
                                        break;
                                        
                                    case "label=\"output, performance\"":
                                        iInputParamMap.put("OUTPUTPERFORMANCE", itemContent);
                                        break;
                                        
                                    case "label=\"output, proteins\"":
                                        iInputParamMap.put("OUTPUTPROTEINS", itemContent);
                                        break;
                                        
                                    case "label=\"output, results\"":
                                        iInputParamMap.put("OUTPUTRESULTS", itemContent);
                                        break;
                                        
                                    case "label=\"output, sequence path\"":
                                        iInputParamMap.put("OUTPUTSEQPATH", itemContent);
                                        break;
                                        
                                    case "label=\"output, sequences\"":
                                        iInputParamMap.put("OUTPUTSEQUENCES", itemContent);
                                        break;
                                        
                                    case "label=\"output, sort results by\"":
                                        iInputParamMap.put("OUTPUTSORTRESULTS", itemContent);
                                        break;
                                        
                                    case "label=\"output, spectra\"":
                                        iInputParamMap.put("OUTPUTSPECTRA", itemContent);
                                        break;
                                        
                                    case "label=\"output, xsl path\"":
                                        iInputParamMap.put("OUTPUTSXSLPATH", itemContent);
                                        break;
                                        
                                    case "label=\"protein, c-terminal residue modification mass\"":
                                        iInputParamMap.put("C_TERMRESMODMASS", itemContent);
                                        break;
                                        
                                    case "label=\"protein, n-terminal residue modification mass\"":
                                        iInputParamMap.put("N_TERMRESMODMASS", itemContent);
                                        break;
                                        
                                    case "label=\"protein, cleavage c-terminal mass change\"":
                                        iInputParamMap.put("C_TERMCLEAVMASSCHANGE", itemContent);
                                        break;
                                        
                                    case "label=\"protein, cleavage n-terminal mass change\"":
                                        iInputParamMap.put("N_TERMCLEAVMASSCHANGE", itemContent);
                                        break;
                                        
                                    case "label=\"protein, cleavage site\"":
                                        iInputParamMap.put("CLEAVAGESITE", itemContent);
                                        break;
                                        
                                    case "label=\"protein, homolog management\"":
                                        iInputParamMap.put("HOMOLOGMANAGE", itemContent);
                                        break;
                                        
                                    case "label=\"protein, modified residue mass file\"":
                                        iInputParamMap.put("MODRESMASSFILE", itemContent);
                                        break;
                                        
                                    case "label=\"protein, taxon\"":
                                        iInputParamMap.put("TAXON", itemContent);
                                        break;
                                        
                                    case "label=\"refine\"":
                                        iInputParamMap.put("REFINE", itemContent);
                                        break;
                                        
                                    case "label=\"refine, maximum valid expectation value\"":
                                        iInputParamMap.put("REFINEMAXVALIDEXPECT", itemContent);
                                        break;
                                        
                                    case "label=\"refine, modification mass\"":
                                        iInputParamMap.put("REFINEMODMASS", itemContent);
                                        break;
                                        
                                    case "label=\"refine, point mutations\"":
                                        iInputParamMap.put("POINTMUTATIONS", itemContent);
                                        break;
                                        
                                    case "label=\"refine, potential c-terminus modifications\"":
                                        iInputParamMap.put("POTC_TERMMODS", itemContent);
                                        break;
                                        
                                    case "label=\"refine, potential n-terminus modifications\"":
                                        iInputParamMap.put("POTN_TERMMODS", itemContent);
                                        break;
                                        
                                    case "label=\"refine, potential modification mass\"":
                                        iInputParamMap.put("POTMODMASS", itemContent);
                                        break;
                                        
                                    case "label=\"refine, potential modification motif\"":
                                        iInputParamMap.put("POTMODMOTIF", itemContent);
                                        break;
                                        
                                    case "label=\"refine, sequence path\"":
                                        iInputParamMap.put("REFINESEQPATH", itemContent);
                                        break;
                                        
                                    case "label=\"refine, spectrum synthesis\"":
                                        iInputParamMap.put("REFINESPECSYTNH", itemContent);
                                        break;
                                        
                                    case "label=\"refine, tic percent\"":
                                        iInputParamMap.put("REFINETIC", itemContent);
                                        break;
                                        
                                    case "label=\"refine, unanticipated cleavage\"":
                                        iInputParamMap.put("REFINEUNANTICLEAV", itemContent);
                                        break;
                                        
                                    case "label=\"refine, use potential modifications for full refinement\"":
                                        iInputParamMap.put("POTMODSFULLREFINE", itemContent);
                                        break;
                                        
                                    case "label=\"residue, modification mass\"":
                                        iInputParamMap.put("RESIDUEMODMASS", itemContent);
                                        break;
                                        
                                    case "label=\"residue, potential modification mass\"":
                                        iInputParamMap.put("RESIDUEPOTMODMASS", itemContent);
                                        break;
                                        
                                    case "label=\"residue, potential modification motif\"":
                                        iInputParamMap.put("RESIDUEPOTMODMOTIV", itemContent);
                                        break;

                                    case "label=\"scoring, a ions\"":
                                        iInputParamMap.put("SCORING_AIONS", itemContent);
                                        aIonFlag = itemContent.equals("yes");
                                        break;
                                        
                                    case "label=\"scoring, b ions\"":
                                        iInputParamMap.put("SCORING_BIONS", itemContent);
                                        bIonFlag = itemContent.equals("yes");
                                        break;
                                        
                                    case "label=\"scoring, c ions\"":
                                        iInputParamMap.put("SCORING_CIONS", itemContent);
                                        cIonFlag = itemContent.equals("yes");
                                        break;
                                            
                                    case "label=\"scoring, cyclic permutation\"":
                                        iInputParamMap.put("SCORINGCYCLPERM", itemContent);
                                        break;
                                        
                                    case "label=\"scoring, include reverse\"":
                                        iInputParamMap.put("SCORINGINCREV", itemContent);
                                        break;
                                        
                                    case "label=\"scoring, maximum missed cleavage sites\"":
                                        iInputParamMap.put("SCORINGMISSCLEAV", itemContent);
                                        break;
                                        
                                    case "label=\"scoring, minimum ion count\"":
                                        iInputParamMap.put("SCORINGMINIONCOUNT", itemContent);
                                        break;
                                        
                                    case "label=\"scoring, pluggable scoring\"":
                                        iInputParamMap.put("SCORINGPLUGSCORING", itemContent);
                                        break;
                                        
                                    case "label=\"scoring, x ions\"":
                                            iInputParamMap.put("SCORING_XIONS", itemContent);
                                            xIonFlag = itemContent.equals("yes");
                                        break;
                                        
                                    case "label=\"scoring, y ions\"":
                                        iInputParamMap.put("SCORING_YIONS", itemContent);
                                        yIonFlag = itemContent.equals("yes");
                                        break;
                                        
                                    case "label=\"scoring, z ions\"":
                                        iInputParamMap.put("SCORING_ZIONS", itemContent);
                                        zIonFlag = itemContent.equals("yes");
                                        break;
                                        
                                    case "label=\"scoring, algorithm\"":
                                        iInputParamMap.put("SCORING_ALGORITHM", itemContent);
                                        break;
                                        
                                    case "label=\"spectrum, dynamic range\"":
                                        iInputParamMap.put("SPECDYNRANGE", itemContent);
                                        break;
                                        
                                    case "label=\"spectrum, fragment mass type\"":
                                        iInputParamMap.put("SPECFRAGMASSTYPE", itemContent);
                                        break;
                                        
                                    case "label=\"spectrum, fragment monoisotopic mass error\"":
                                        iInputParamMap.put("SPECMONOISOMASSERROR", itemContent);
                                        break;
                                        
                                    case "label=\"spectrum, fragment monoisotopic mass error units\"":
                                        iInputParamMap.put("SPECMONOISOMASSERRORUNITS", itemContent);
                                        break;
                                        
                                    case "label=\"spectrum, maximum parent charge\"":
                                        iInputParamMap.put("SPECMAXPRECURSORCHANGE", itemContent);
                                        break;
                                        
                                    case "label=\"spectrum, minimum fragment mz\"":
                                        iInputParamMap.put("SPECMINFRAGMZ", itemContent);
                                        break;
                                        
                                    case "label=\"spectrum, minimum parent m+h\"":
                                        iInputParamMap.put("SPECMINPRECURSORMZ", itemContent);
                                        break;
                                        
                                    case "label=\"spectrum, minimum peaks\"":
                                        iInputParamMap.put("SPECMINPEAKS", itemContent);
                                        break;
                                        
                                    case "label=\"spectrum, parent monoisotopic mass error minus\"":
                                        iInputParamMap.put("SPECPARENTMASSERRORMINUS", itemContent);
                                        break;
                                        
                                    case "label=\"spectrum, parent monoisotopic mass error plus\"":
                                        iInputParamMap.put("SPECPARENTMASSERRORPLUS", itemContent);
                                        break;
                                        
                                    case "label=\"spectrum, parent monoisotopic mass error units\"":
                                        iInputParamMap.put("SPECPARENTMASSERRORUNITS", itemContent);
                                        break;
                                        
                                    case "label=\"spectrum, parent monoisotopic mass isotope error\"":
                                        iInputParamMap.put("SPECPARENTMASSISOERROR", itemContent);
                                        break;
                                        
                                    case "label=\"spectrum, sequence batch size\"":
                                        iInputParamMap.put("SPECBATCHSIZE", itemContent);
                                        break;
                                        
                                    case "label=\"spectrum, threads\"":
                                        iInputParamMap.put("SPECTHREADS", itemContent);
                                        break;
                                        
                                    case "label=\"spectrum, total peaks\"":
                                        iInputParamMap.put("SPECTOTALPEAK", itemContent);
                                        break;
                                        
                                    case "label=\"spectrum, use noise suppression\"":
                                        iInputParamMap.put("SPECUSENOISECOMP", itemContent);
                                        break;
                                        
                                    default:
                                        if (label.startsWith("label=\"refine, potential modification mass ")){
                                            // get the mod number
                                            Matcher matcher = refPotModificationMassPattern.matcher(label);
                                            if (matcher.find()){
                                                iInputParamMap.put("POTMODMASS_" + matcher.group(1), itemContent);
                                            }
                                        }
                                        else if (label.startsWith("label=\"refine, potential modification motif ")){
                                            Matcher matcher = refPotModificationMotifPattern.matcher(label);

                                            if (matcher.find()) {
                                                iInputParamMap.put("POTMODMOTIF_" + matcher.group(1), itemContent);
                                            }
                                        }
                                        // parse residue, modification mass [1-n]
                                        else if (label.startsWith("label=\"residue, modification mass ")){
                                            // get the mod number
                                            Matcher matcher = resModificationMassPattern.matcher(label);

                                            if (matcher.find()) {
                                                iInputParamMap.put("RESIDUEMODMASS_" + matcher.group(1), itemContent);
                                            }
                                        }
                                        break;
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
                            if (parameterNodes.item(m).getAttributes() != null && !parameterNodes.item(m).getTextContent().equals("")) {
                                String paramLabel = parameterNodes.item(m).getAttributes().getNamedItem("label").toString().toLowerCase();
                                String content = parameterNodes.item(m).getTextContent();
                                
                                switch (paramLabel){
                                    case "label=\"list path, sequence source #1\"":
                                        iPerformParamMap.put("SEQSRC1", content);
                                        break;
                                    
                                    case "label=\"list path, sequence source #2\"":
                                        iPerformParamMap.put("SEQSRC2", content);
                                        break;
                                    
                                    case "label=\"list path, sequence source #3\"":
                                        iPerformParamMap.put("SEQSRC3", content);
                                        break;
                                    
                                    case "label=\"list path, sequence source description #1\"":
                                        iPerformParamMap.put("SEQSRCDESC1", content);
                                        break;
                                    
                                    case "label=\"list path, sequence source description #2\"":
                                        iPerformParamMap.put("SEQSRCDESC2", content);
                                        break;
                                    
                                    case "label=\"list path, sequence source description #3\"":
                                        iPerformParamMap.put("SEQSRCDESC3", content);
                                        break;
                                    
                                    case "label=\"modelling, estimated false positives\"":
                                        iPerformParamMap.put("ESTFP", content);
                                        break;
                                    
                                    case "label=\"modelling, spectrum noise suppression ratio\"":
                                        iPerformParamMap.put("NOISESUPP", content);
                                        break;
                                    
                                    case "label=\"modelling, total peptides used\"":
                                        iPerformParamMap.put("TOTALPEPUSED", content);
                                        break;
                                    
                                    case "label=\"modelling, total proteins used\"":
                                        iPerformParamMap.put("TOTALPROTUSED", content);
                                        break;
                                    
                                    case "label=\"modelling, total spectra assigned\"":
                                        iPerformParamMap.put("TOTALSPECASS", content);
                                        break;
                                    
                                    case "label=\"modelling, total spectra used\"":
                                        iPerformParamMap.put("TOTALSPECUSED", content);
                                        break;
                                    
                                    case "label=\"modelling, total unique assigned\"":
                                        iPerformParamMap.put("TOTALUNIQUEASS", content);
                                        break;
                                    
                                    case "label=\"process, start time\"":
                                        iPerformParamMap.put("PROCSTART", content);
                                        break;
                                    
                                    case "label=\"process, version\"":
                                        iPerformParamMap.put("PROCVER", content);
                                        break;
                                    
                                    case "label=\"quality values\"":
                                        iPerformParamMap.put("QUALVAL", content);
                                        break;
                                    
                                    case "label=\"refining, # input models\"":
                                        iPerformParamMap.put("INPUTMOD", content);
                                        break;
                                    
                                    case "label=\"refining, # input spectra\"":
                                        iPerformParamMap.put("INPUTSPEC", content);
                                        break;
                                    
                                    case "label=\"refining, # partial cleavage\"":
                                        iPerformParamMap.put("PARTCLEAV", content);
                                        break;
                                    
                                    case "label=\"refining, # point mutations\"":
                                        iPerformParamMap.put("POINTMUT", content);
                                        break;
                                    
                                    case "label=\"refining, # potential c-terminii\"":
                                        iPerformParamMap.put("POTC_TERM", content);
                                        break;
                                    
                                    case "label=\"refining, # potential n-terminii\"":
                                        iPerformParamMap.put("POTN_TERM", content);
                                        break;
                                    
                                    case "label=\"refining, # unanticipated cleavage\"":
                                        iPerformParamMap.put("UNANTICLEAV", content);
                                        break;
                                    
                                    case "label=\"timing, initial modelling total (sec)\"":
                                        iPerformParamMap.put("INITMODELTOTALTIME", content);
                                        break;
                                    
                                    case "label=\"timing, initial modelling/spectrum (sec)\"":
                                        iPerformParamMap.put("INITMODELSPECTIME", content);
                                        break;
                                    
                                    case "label=\"timing, load sequence models (sec)\"":
                                        iPerformParamMap.put("LOADSEQMODELTIME", content);
                                        break;
                                    
                                    case "label=\"timing, refinement/spectrum (sec)\"":
                                        iPerformParamMap.put("REFINETIME", content);
                                        break;
                                    
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }

        // Iterate over all the nodes
        for (int i = 0; i < nodes.getLength(); i++) {
            NamedNodeMap nodeAttributes = nodes.item(i).getAttributes();
            if (nodeAttributes != null) {
                if (nodeAttributes.getNamedItem("type") != null) {

                    // The model group contains all information about a single peptide identification
                    if (nodeAttributes.getNamedItem("type").getNodeValue().equalsIgnoreCase("model")) {
                        spectraCounter++;

                        // id is the number associated with the mass spectrum that was identified
                        if (nodeAttributes.getNamedItem("id") != null) {
                            iRawSpectrumMap.put("id" + spectraCounter, nodeAttributes.getNamedItem("id").getNodeValue());
                        }
                        // z is the parent/precursor ion charge
                        if (nodeAttributes.getNamedItem("z") != null) {
                            iRawSpectrumMap.put("z" + spectraCounter, nodeAttributes.getNamedItem("z").getNodeValue());
                        }
                        if (!skipDetails) {
                            // expect is the expectation value for the top ranked protein identfied with this spectrum
                            if (nodeAttributes.getNamedItem("expect") != null) {
                                iRawSpectrumMap.put("expect" + spectraCounter, nodeAttributes.getNamedItem("expect").getNodeValue());
                            }
                            // mh is the parent/precursor ion mass from the spectrum
                            if (nodeAttributes.getNamedItem("mh") != null) {
                                iRawSpectrumMap.put("mh" + spectraCounter, nodeAttributes.getNamedItem("mh").getNodeValue());
                            }
                            // rt is the parent/precursor retention time
                            if (nodeAttributes.getNamedItem("rt") != null) {
                                iRawSpectrumMap.put("rt" + spectraCounter, nodeAttributes.getNamedItem("rt").getNodeValue());
                            }
                            // label is the text from the protein sequence FASTA file description line for the top ranked protein identified
                            if (nodeAttributes.getNamedItem("label") != null) {
                                iRawSpectrumMap.put("label" + spectraCounter, nodeAttributes.getNamedItem("label").getNodeValue());
                            }
                            // sumI is the log10-value of the sum of all of the fragment ion intensities
                            if (nodeAttributes.getNamedItem("sumI") != null) {
                                iRawSpectrumMap.put("sumI" + spectraCounter, nodeAttributes.getNamedItem("sumI").getNodeValue());
                            }
                            // maxI is the maximum fragment ion intensity
                            if (nodeAttributes.getNamedItem("maxI") != null) {
                                iRawSpectrumMap.put("maxI" + spectraCounter, nodeAttributes.getNamedItem("maxI").getNodeValue());
                            }
                            // fI is a multiplier to convert the normalized spectrum back to the original intensity values
                            if (nodeAttributes.getNamedItem("fI") != null) {
                                iRawSpectrumMap.put("fI" + spectraCounter, nodeAttributes.getNamedItem("fI").getNodeValue());
                            }
                        }
                    }
                }
            }

            // the number of spectra.
            iNumberOfSpectra = spectraCounter;

            // Get the identifications
            idNodes = nodes.item(i).getChildNodes();

            int p_counter = 0;
            // Iterate over all the child nodes
            for (int j = 0; j < idNodes.getLength(); j++) {

                if (idNodes.item(j).getNodeName().equalsIgnoreCase("protein")) {
                    p_counter++;
                    NamedNodeMap idNodesAttributes = idNodes.item(j).getAttributes();
                    // the identifier of this particular identification (spectrum#).(id#)
                    String protID = idNodesAttributes.getNamedItem("id").getNodeValue();

                    // Since the ID is not unique to the protein, we will use the label to reference it. That will be dirty for some files.
                    String proteinKey = idNodesAttributes.getNamedItem("label").getNodeValue();
                    if (!skipDetails) {
                        iProteinKeyList.add(proteinKey);

                        // a unique number of this protein, calculated by the search engine. Well unique. Most often yes.
                        iRawProteinMap.put("uid" + proteinKey, idNodesAttributes.getNamedItem("uid").getNodeValue());

                        // the log10 value of the expection value of the protein
                        iRawProteinMap.put("expect" + proteinKey, idNodesAttributes.getNamedItem("expect").getNodeValue());

                        // the description line from the FASTA file
                        iRawProteinMap.put("label" + proteinKey, idNodesAttributes.getNamedItem("label").getNodeValue());

                        // the sum of all of the fragment ions that identify this protein
                        iRawProteinMap.put("sumI" + proteinKey, idNodesAttributes.getNamedItem("sumI").getNodeValue());
                    }

                    proteinNodes = idNodes.item(j).getChildNodes();

                    // Iterate over all the protein nodes
                    for (int k = 0; k < proteinNodes.getLength(); k++) {
                        if (!skipDetails) {
                            if (proteinNodes.item(k).getNodeName().equalsIgnoreCase("file")) {
                                // the path used to the original fasta file
                                iRawPeptideMap.put("URL" + "_s" + spectraCounter + "_p" + p_counter, proteinNodes.item(k).getAttributes().getNamedItem("URL").getNodeValue());
                            }

                            if (proteinNodes.item(k).getNodeName().equalsIgnoreCase("note") && proteinNodes.item(k).getAttributes().getNamedItem("label") != null
                                    && proteinNodes.item(k).getAttributes().getNamedItem("label").getNodeValue().equalsIgnoreCase("description")) {
                                // the protein description (xml tag: note label="description")
                                String test = proteinNodes.item(k).getTextContent();
                                iRawProteinMap.put("description" + proteinKey, test);
                            }

                        }
                        // the the sum of all the fragment ions that identify this protein
                        if (proteinNodes.item(k).getNodeName().equalsIgnoreCase("peptide")) {
                            iRawPeptideMap.put("s" + spectraCounter + "_p" + p_counter, protID);
                            if (!skipDetails) {
                                iRawPeptideMap.put("start" + "_s" + spectraCounter + "_p"
                                        + p_counter, proteinNodes.item(k).getAttributes().getNamedItem("start").getNodeValue());
                                iRawPeptideMap.put("end" + "_s" + spectraCounter + "_p"
                                        + p_counter, proteinNodes.item(k).getAttributes().getNamedItem("end").getNodeValue());
                                iRawPeptideMap.put("seq" + "_s" + spectraCounter + "_p"
                                        + p_counter, proteinNodes.item(k).getTextContent());
                            }

                            peptideNodes = proteinNodes.item(k).getChildNodes();

                            // Domain counter
                            int dCount = 1;

                            // Iterate over all the peptide nodes
                            for (int m = 0; m < peptideNodes.getLength(); m++) {
                                
                                NamedNodeMap attributes = peptideNodes.item(m).getAttributes();

                                // Get the domain entries
                                if (peptideNodes.item(m).getNodeName().equalsIgnoreCase("domain")) {

                                    // Get the domainid
                                    String domainKey = "s" + spectraCounter + "_p" + p_counter + "_d" + dCount;

                                    // verify that the same domain key is not already in use
                                    while (iRawPeptideMap.containsKey("proteinkey" + "_" + domainKey)) {
                                        domainKey = "s" + spectraCounter + "_p" + p_counter + "_d" + ++dCount;
                                    }

                                    iRawPeptideMap.put("domainid" + "_" + domainKey, attributes.getNamedItem("id").getNodeValue());

                                    // the start position of the peptide
                                    iRawPeptideMap.put("domainstart" + "_" + domainKey, attributes.getNamedItem("start").getNodeValue());

                                    if (!skipDetails) {

                                        // Store the protein key a la Thilo. There should be only one protein key per domain.
                                        iRawPeptideMap.put("proteinkey" + "_" + domainKey, proteinKey);

                                        // the end position of the peptide
                                        iRawPeptideMap.put("domainend" + "_" + domainKey, attributes.getNamedItem("end").getNodeValue());

                                        // the mass + a proton
                                        iRawPeptideMap.put("mh" + "_" + domainKey, attributes.getNamedItem("mh").getNodeValue());

                                        // the mass delta
                                        iRawPeptideMap.put("delta" + "_" + domainKey, attributes.getNamedItem("delta").getNodeValue());

                                        // the hyper score
                                        iRawPeptideMap.put("hyperscore" + "_" + domainKey, attributes.getNamedItem("hyperscore").getNodeValue());

                                        // the next score
                                        iRawPeptideMap.put("nextscore" + "_" + domainKey, attributes.getNamedItem("nextscore").getNodeValue());

                                        if (xIonFlag) {
                                            // the x score
                                            iRawPeptideMap.put("x_score" + "_" + domainKey, attributes.getNamedItem("y_score").getNodeValue());

                                            // the x ion number
                                            iRawPeptideMap.put("x_ions" + "_" + domainKey, attributes.getNamedItem("y_ions").getNodeValue());
                                        }

                                        if (yIonFlag) {
                                            // the y score
                                            iRawPeptideMap.put("y_score" + "_" + domainKey, attributes.getNamedItem("y_score").getNodeValue());

                                            // the y ion number
                                            iRawPeptideMap.put("y_ions" + "_" + domainKey, attributes.getNamedItem("y_ions").getNodeValue());
                                        }

                                        if (zIonFlag) {
                                            // the z score
                                            iRawPeptideMap.put("z_score" + "_" + domainKey, attributes.getNamedItem("y_score").getNodeValue());

                                            // the z ion number
                                            iRawPeptideMap.put("z_ions" + "_" + domainKey, attributes.getNamedItem("y_ions").getNodeValue());
                                        }

                                        if (aIonFlag) {
                                            // the a score
                                            iRawPeptideMap.put("a_score" + "_" + domainKey, attributes.getNamedItem("b_score").getNodeValue());

                                            // the a ion number
                                            iRawPeptideMap.put("a_ions" + "_" + domainKey, attributes.getNamedItem("b_ions").getNodeValue());
                                        }

                                        if (bIonFlag) {
                                            // the b score
                                            iRawPeptideMap.put("b_score" + "_" + domainKey, attributes.getNamedItem("b_score").getNodeValue());

                                            // the b ion number
                                            iRawPeptideMap.put("b_ions" + "_" + domainKey, attributes.getNamedItem("b_ions").getNodeValue());
                                        }

                                        if (cIonFlag) {
                                            // the c score
                                            iRawPeptideMap.put("c_score" + "_" + domainKey, attributes.getNamedItem("b_score").getNodeValue());

                                            // the c ion number
                                            iRawPeptideMap.put("c_ions" + "_" + domainKey, attributes.getNamedItem("b_ions").getNodeValue());
                                        }

                                        // the upstream flanking sequence
                                        iRawPeptideMap.put("pre" + "_" + domainKey, attributes.getNamedItem("pre").getNodeValue());

                                        // the downstream flanking sequence
                                        iRawPeptideMap.put("post" + "_" + domainKey, attributes.getNamedItem("post").getNodeValue());

                                        // the number of missed cleavages
                                        iRawPeptideMap.put("missed_cleavages" + "_" + domainKey, attributes.getNamedItem("missed_cleavages").getNodeValue());
                                    }

                                    // the expectation value
                                    iRawPeptideMap.put("expect" + "_" + domainKey, attributes.getNamedItem("expect").getNodeValue());

                                    // the domain sequence
                                    iRawPeptideMap.put("domainseq" + "_" + domainKey, attributes.getNamedItem("seq").getNodeValue());

                                    int modCounter = 0;
                                    for (int n = 0; n < peptideNodes.item(m).getChildNodes().getLength(); n++) {

                                        // Get the specific modifications (aa)
                                        if (peptideNodes.item(m).getChildNodes().item(n).getNodeName().equalsIgnoreCase("aa")) {
                                            modCounter++;

                                            modificationMap = peptideNodes.item(m).getChildNodes().item(n).getAttributes();
                                            modificationName = modificationMap.getNamedItem("type").getNodeValue();

                                            // use the old calculation with domainStart!
                                            //modificationMap.getNamedItem("at").getNodeValue()) - domainStart + 1)
                                            iRawModMap.put("at" + "_" + domainKey + "_m" + modCounter, modificationMap.getNamedItem("at").getNodeValue());

                                            // modified is the residue mass change caused by the modification
                                            modificationMass = Double.parseDouble(modificationMap.getNamedItem("modified").getNodeValue());
                                            iRawModMap.put("modified" + "_" + domainKey + "_m" + modCounter, modificationMap.getNamedItem("modified").getNodeValue());

                                            modificationName = modificationMass + "@" + modificationName;

                                            // type is the single letter abbreviation for the modified residue
                                            iRawModMap.put("name" + "_" + domainKey + "_m" + modCounter, modificationName);

                                            // get the substituted amino acid (if any)
                                            if (modificationMap.getNamedItem("pm") != null) {
                                                iRawModMap.put("pm" + "_" + domainKey + "_m" + modCounter, modificationMap.getNamedItem("pm").getNodeValue());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Go to the group node inside the other group node (support)
                if (idNodes.item(j).getNodeName().equalsIgnoreCase("group")) {
                    if (!skipDetails) {
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
                                        if (supportDataNodes.item(a).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase("b ion histogram")) {
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
                                        if (supportDataNodes.item(a).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase("b ion histogram")) {
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
                                        if (supportDataNodes.item(a).getAttributes().getNamedItem("type").getNodeValue().equals("y ion histogram")) {
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
                                        if (supportDataNodes.item(a).getAttributes().getNamedItem("type").getNodeValue().equals("y ion histogram")) {
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
                    }
                    if (idNodes.item(j).getAttributes().getNamedItem("label").getNodeValue().equalsIgnoreCase("fragment ion mass spectrum")) {

                        supportDataNodes = idNodes.item(j).getChildNodes();
                        // Iterate over all the support data nodes
                        for (int a = 0; a < supportDataNodes.getLength(); a++) {
                            if (supportDataNodes.item(a).getNodeName().equalsIgnoreCase("note")) {
                                String title = supportDataNodes.item(a).getTextContent().trim();
                                idToSpectrumMap.put(spectraCounter, title);
                                if (!skipDetails) {
                                    iSupportDataMap.put("FRAGIONSPECDESC" + "_s" + spectraCounter, title);
                                    iTitle2SpectrumIDMap.put(title, spectraCounter);
                                } else {
                                    break;
                                }
                            }
                            if (!skipDetails) {
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
     * Returns the protein id list.
     *
     * @return iProteinIDList ArrayList with the protein keys
     */
    public ArrayList<String> getProteinIDList() {
        return iProteinKeyList;
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

    /**
     * Returns the X!Tandem id to spectrum title map.
     *
     * @return X!Tandem id to spectrum title map
     */
    public HashMap<Integer, String> getIdToSpectrumMap() {
        return idToSpectrumMap;
    }
}
