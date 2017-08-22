package de.proteinms.xtandemparser.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.stream.*;

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
    private static Pattern resModificationMassPattern = Pattern.compile("residue, modification mass (\\d+)");
    /**
     * Pattern to extract the modification mass number if multiple modification
     * masses are given.
     */
    private static Pattern refPotModificationMassPattern = Pattern.compile("refine, potential modification mass (\\d+)");
    /**
     * Pattern to extract the modification mass number if multiple modification
     * masses are given.
     */
    private static Pattern refPotModificationMotifPattern = Pattern.compile("refine, potential modification motif (\\d+)");
    /**
     * This variable holds the total number of spectra in the xtandem file.
     */
    private int iNumberOfSpectra = 0;
    /**
     * This map contains the key/value pairs of the input parameters.
     */
    private HashMap<String, String> iInputParamMap = new HashMap<>();
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
    
    private boolean aIonFlag = false;
    private boolean bIonFlag = false;
    private boolean cIonFlag = false;
    private boolean xIonFlag = false;
    private boolean yIonFlag = false;
    private boolean zIonFlag = false;

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
    
    
    
    
    
    private void readInputParameter(XMLStreamReader parser) throws XMLStreamException {
        boolean continueing = true;
        String key = "";
        String itemContent = "";
        boolean put = false;
        while (continueing && parser.hasNext()){
            switch ( parser.getEventType() ){
                case XMLStreamConstants.CHARACTERS:
                    itemContent = parser.getText();
                    if (put){
                        switch (key.toLowerCase()){
                                
                            case "spectrum, path":
                                iInputParamMap.put("SPECTRUMPATH", itemContent);
                                continueing = false;
                                break;

                            case "list path, default parameters":
                                iInputParamMap.put("DEFAULTPARAMPATH", itemContent);
                                break;

                            case "list path, taxonomy information":
                                iInputParamMap.put("TAXONOMYINFOPATH", itemContent);
                                break;

                            case "output, histogram column width":
                                iInputParamMap.put("HISTOCOLWIDTH", itemContent);
                                break;

                            case "output, histograms":
                                iInputParamMap.put("HISTOEXIST", itemContent);
                                break;

                            case "output, logpath":
                                iInputParamMap.put("LOGPATH", itemContent);
                                break;

                            case "output, maximum valid expectation value":
                                iInputParamMap.put("MAXVALIDEXPECT", itemContent);
                                break;

                            case "output, message":
                                iInputParamMap.put("OUTPUTMESSAGE", itemContent);
                                break;

                            case "output, one sequence copy":
                                iInputParamMap.put("ONESEQCOPY", itemContent);
                                break;

                            case "output, parameters":
                                iInputParamMap.put("OUTPUTPARAMS", itemContent);
                                break;

                            case "output, path":
                                iInputParamMap.put("OUTPUTPATH", itemContent);
                                break;

                            case "output, path hashing":
                                iInputParamMap.put("OUTPUTPATHHASH", itemContent);
                                break;

                            case "output, performance":
                                iInputParamMap.put("OUTPUTPERFORMANCE", itemContent);
                                break;

                            case "output, proteins":
                                iInputParamMap.put("OUTPUTPROTEINS", itemContent);
                                break;

                            case "output, results":
                                iInputParamMap.put("OUTPUTRESULTS", itemContent);
                                break;

                            case "output, sequence path":
                                iInputParamMap.put("OUTPUTSEQPATH", itemContent);
                                break;

                            case "output, sequences":
                                iInputParamMap.put("OUTPUTSEQUENCES", itemContent);
                                break;

                            case "output, sort results by":
                                iInputParamMap.put("OUTPUTSORTRESULTS", itemContent);
                                break;

                            case "output, spectra":
                                iInputParamMap.put("OUTPUTSPECTRA", itemContent);
                                break;

                            case "output, xsl path":
                                iInputParamMap.put("OUTPUTSXSLPATH", itemContent);
                                break;

                            case "protein, c-terminal residue modification mass":
                                iInputParamMap.put("C_TERMRESMODMASS", itemContent);
                                break;

                            case "protein, n-terminal residue modification mass":
                                iInputParamMap.put("N_TERMRESMODMASS", itemContent);
                                break;

                            case "protein, cleavage c-terminal mass change":
                                iInputParamMap.put("C_TERMCLEAVMASSCHANGE", itemContent);
                                break;

                            case "protein, cleavage n-terminal mass change":
                                iInputParamMap.put("N_TERMCLEAVMASSCHANGE", itemContent);
                                break;

                            case "protein, cleavage site":
                                iInputParamMap.put("CLEAVAGESITE", itemContent);
                                break;

                            case "protein, homolog management":
                                iInputParamMap.put("HOMOLOGMANAGE", itemContent);
                                break;

                            case "protein, modified residue mass file":
                                iInputParamMap.put("MODRESMASSFILE", itemContent);
                                break;

                            case "protein, taxon":
                                iInputParamMap.put("TAXON", itemContent);
                                break;

                            case "refine":
                                iInputParamMap.put("REFINE", itemContent);
                                break;

                            case "refine, maximum valid expectation value":
                                iInputParamMap.put("REFINEMAXVALIDEXPECT", itemContent);
                                break;

                            case "refine, modification mass":
                                iInputParamMap.put("REFINEMODMASS", itemContent);
                                break;

                            case "refine, point mutations":
                                iInputParamMap.put("POINTMUTATIONS", itemContent);
                                break;

                            case "refine, potential c-terminus modifications":
                                iInputParamMap.put("POTC_TERMMODS", itemContent);
                                break;

                            case "refine, potential n-terminus modifications":
                                iInputParamMap.put("POTN_TERMMODS", itemContent);
                                break;

                            case "refine, potential modification mass":
                                iInputParamMap.put("POTMODMASS", itemContent);
                                break;

                            case "refine, potential modification motif":
                                iInputParamMap.put("POTMODMOTIF", itemContent);
                                break;

                            case "refine, sequence path":
                                iInputParamMap.put("REFINESEQPATH", itemContent);
                                break;

                            case "refine, spectrum synthesis":
                                iInputParamMap.put("REFINESPECSYTNH", itemContent);
                                break;

                            case "refine, tic percent":
                                iInputParamMap.put("REFINETIC", itemContent);
                                break;

                            case "refine, unanticipated cleavage":
                                iInputParamMap.put("REFINEUNANTICLEAV", itemContent);
                                break;

                            case "refine, use potential modifications for full refinement":
                                iInputParamMap.put("POTMODSFULLREFINE", itemContent);
                                break;

                            case "residue, modification mass":
                                iInputParamMap.put("RESIDUEMODMASS", itemContent);
                                break;

                            case "residue, potential modification mass":
                                iInputParamMap.put("RESIDUEPOTMODMASS", itemContent);
                                break;

                            case "residue, potential modification motif":
                                iInputParamMap.put("RESIDUEPOTMODMOTIV", itemContent);
                                break;

                            case "scoring, a ions":
                                iInputParamMap.put("SCORING_AIONS", itemContent);
                                aIonFlag = itemContent.equals("yes");
                                break;

                            case "scoring, b ions":
                                iInputParamMap.put("SCORING_BIONS", itemContent);
                                bIonFlag = itemContent.equals("yes");
                                break;

                            case "scoring, c ions":
                                iInputParamMap.put("SCORING_CIONS", itemContent);
                                cIonFlag = itemContent.equals("yes");
                                break;

                            case "scoring, cyclic permutation":
                                iInputParamMap.put("SCORINGCYCLPERM", itemContent);
                                break;

                            case "scoring, include reverse":
                                iInputParamMap.put("SCORINGINCREV", itemContent);
                                break;

                            case "scoring, maximum missed cleavage sites":
                                iInputParamMap.put("SCORINGMISSCLEAV", itemContent);
                                break;

                            case "scoring, minimum ion count":
                                iInputParamMap.put("SCORINGMINIONCOUNT", itemContent);
                                break;

                            case "scoring, pluggable scoring":
                                iInputParamMap.put("SCORINGPLUGSCORING", itemContent);
                                break;

                            case "scoring, x ions":
                                    iInputParamMap.put("SCORING_XIONS", itemContent);
                                    xIonFlag = itemContent.equals("yes");
                                break;

                            case "scoring, y ions":
                                iInputParamMap.put("SCORING_YIONS", itemContent);
                                yIonFlag = itemContent.equals("yes");
                                break;

                            case "scoring, z ions":
                                iInputParamMap.put("SCORING_ZIONS", itemContent);
                                zIonFlag = itemContent.equals("yes");
                                break;

                            case "scoring, algorithm":
                                iInputParamMap.put("SCORING_ALGORITHM", itemContent);
                                break;

                            case "spectrum, dynamic range":
                                iInputParamMap.put("SPECDYNRANGE", itemContent);
                                break;

                            case "spectrum, fragment mass type":
                                iInputParamMap.put("SPECFRAGMASSTYPE", itemContent);
                                break;

                            case "spectrum, fragment monoisotopic mass error":
                                iInputParamMap.put("SPECMONOISOMASSERROR", itemContent);
                                break;

                            case "spectrum, fragment monoisotopic mass error units":
                                iInputParamMap.put("SPECMONOISOMASSERRORUNITS", itemContent);
                                break;

                            case "spectrum, maximum parent charge":
                                iInputParamMap.put("SPECMAXPRECURSORCHANGE", itemContent);
                                break;

                            case "spectrum, minimum fragment mz":
                                iInputParamMap.put("SPECMINFRAGMZ", itemContent);
                                break;

                            case "spectrum, minimum parent m+h":
                                iInputParamMap.put("SPECMINPRECURSORMZ", itemContent);
                                break;

                            case "spectrum, minimum peaks":
                                iInputParamMap.put("SPECMINPEAKS", itemContent);
                                break;

                            case "spectrum, parent monoisotopic mass error minus":
                                iInputParamMap.put("SPECPARENTMASSERRORMINUS", itemContent);
                                break;

                            case "spectrum, parent monoisotopic mass error plus":
                                iInputParamMap.put("SPECPARENTMASSERRORPLUS", itemContent);
                                break;

                            case "spectrum, parent monoisotopic mass error units":
                                iInputParamMap.put("SPECPARENTMASSERRORUNITS", itemContent);
                                break;

                            case "spectrum, parent monoisotopic mass isotope error":
                                iInputParamMap.put("SPECPARENTMASSISOERROR", itemContent);
                                break;

                            case "spectrum, sequence batch size":
                                iInputParamMap.put("SPECBATCHSIZE", itemContent);
                                break;

                            case "spectrum, threads":
                                iInputParamMap.put("SPECTHREADS", itemContent);
                                break;

                            case "spectrum, total peaks":
                                iInputParamMap.put("SPECTOTALPEAK", itemContent);
                                break;

                            case "spectrum, use noise suppression":
                                iInputParamMap.put("SPECUSENOISECOMP", itemContent);
                                break;

                            default:
                                if (key.startsWith("refine, potential modification mass ")){
                                    // get the mod number
                                    Matcher matcher = refPotModificationMassPattern.matcher(key);
                                    if (matcher.find()){
                                        iInputParamMap.put("POTMODMASS_" + matcher.group(1), itemContent);
                                    }
                                }
                                else if (key.startsWith("refine, potential modification motif ")){
                                    Matcher matcher = refPotModificationMotifPattern.matcher(key);

                                    if (matcher.find()) {
                                        iInputParamMap.put("POTMODMOTIF_" + matcher.group(1), itemContent);
                                    }
                                }
                                // parse residue, modification mass [1-n]
                                else if (key.startsWith("residue, modification mass ")){
                                    // get the mod number
                                    Matcher matcher = resModificationMassPattern.matcher(key);

                                    if (matcher.find()) {
                                        iInputParamMap.put("RESIDUEMODMASS_" + matcher.group(1), itemContent);
                                    }
                                }
                                break;
                        }
                    }
                    put = false;
                    break;
                
                case XMLStreamConstants.END_ELEMENT:
                    if (!parser.getLocalName().equals("note")) continueing = false;
                    break;
                
                case XMLStreamConstants.START_ELEMENT:
                    String elementi = parser.getLocalName();
                    if (elementi.equals("note") &&
                        parser.getAttributeValue("", "type") != null &&
                        parser.getAttributeValue("", "type").equalsIgnoreCase("input") &&
                        parser.getAttributeValue("", "label") != null){
                        key = parser.getAttributeValue("", "label");
                        put = true;
                    }                        
                    break;
                default: continueing = false; break;

            }
            if (continueing) parser.next();
        }
    }
    
    
    private void readPerformanceParameter(XMLStreamReader parser) throws XMLStreamException {
        boolean continueing = true;
        String key = "";
        String content = "";
        boolean put = false;
        while (continueing && parser.hasNext()){
            switch ( parser.getEventType() ){
                case XMLStreamConstants.CHARACTERS:
                    content = parser.getText();
                    if (put){
                        switch (key.toLowerCase()){
                            case "list path, sequence source #1":
                                iPerformParamMap.put("SEQSRC1", content);
                                break;

                            case "list path, sequence source #2":
                                iPerformParamMap.put("SEQSRC2", content);
                                break;

                            case "list path, sequence source #3":
                                iPerformParamMap.put("SEQSRC3", content);
                                break;

                            case "list path, sequence source description #1":
                                iPerformParamMap.put("SEQSRCDESC1", content);
                                break;

                            case "list path, sequence source description #2":
                                iPerformParamMap.put("SEQSRCDESC2", content);
                                break;

                            case "list path, sequence source description #3":
                                iPerformParamMap.put("SEQSRCDESC3", content);
                                break;

                            case "modelling, estimated false positives":
                                iPerformParamMap.put("ESTFP", content);
                                break;

                            case "modelling, spectrum noise suppression ratio":
                                iPerformParamMap.put("NOISESUPP", content);
                                break;

                            case "modelling, total peptides used":
                                iPerformParamMap.put("TOTALPEPUSED", content);
                                break;

                            case "modelling, total proteins used":
                                iPerformParamMap.put("TOTALPROTUSED", content);
                                break;

                            case "modelling, total spectra assigned":
                                iPerformParamMap.put("TOTALSPECASS", content);
                                break;

                            case "modelling, total spectra used":
                                iPerformParamMap.put("TOTALSPECUSED", content);
                                break;

                            case "modelling, total unique assigned":
                                iPerformParamMap.put("TOTALUNIQUEASS", content);
                                break;

                            case "process, start time":
                                iPerformParamMap.put("PROCSTART", content);
                                break;

                            case "process, version":
                                iPerformParamMap.put("PROCVER", content);
                                break;

                            case "quality values":
                                iPerformParamMap.put("QUALVAL", content);
                                break;

                            case "refining, # input models":
                                iPerformParamMap.put("INPUTMOD", content);
                                break;

                            case "refining, # input spectra":
                                iPerformParamMap.put("INPUTSPEC", content);
                                break;

                            case "refining, # partial cleavage":
                                iPerformParamMap.put("PARTCLEAV", content);
                                break;

                            case "refining, # point mutations":
                                iPerformParamMap.put("POINTMUT", content);
                                break;

                            case "refining, # potential c-terminii":
                                iPerformParamMap.put("POTC_TERM", content);
                                break;

                            case "refining, # potential n-terminii":
                                iPerformParamMap.put("POTN_TERM", content);
                                break;

                            case "refining, # unanticipated cleavage":
                                iPerformParamMap.put("UNANTICLEAV", content);
                                break;

                            case "timing, initial modelling total (sec)":
                                iPerformParamMap.put("INITMODELTOTALTIME", content);
                                break;

                            case "timing, initial modelling/spectrum (sec)":
                                iPerformParamMap.put("INITMODELSPECTIME", content);
                                break;

                            case "timing, load sequence models (sec)":
                                iPerformParamMap.put("LOADSEQMODELTIME", content);
                                break;

                            case "timing, refinement/spectrum (sec)":
                                iPerformParamMap.put("REFINETIME", content);
                                break;

                            default:
                                break;
                        }
                        put = false;
                    }
                    break;
                
                case XMLStreamConstants.END_ELEMENT:
                    if (!parser.getLocalName().equals("note")) continueing = false;
                    break;
                
                case XMLStreamConstants.START_ELEMENT:
                    String element = parser.getLocalName();
                    if (element.equals("note") && parser.getAttributeValue("", "label") != null){
                        key = parser.getAttributeValue("", "label");
                        put = true;
                    }                        
                    break;
                default: continueing = false; break;

            }
            if (continueing) parser.next();
        }
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
        
        // Initialize the maps
        iPerformParamMap = new HashMap<>();
        iRawModMap = new HashMap<>();
        iRawSpectrumMap = new HashMap<>();
        iRawPeptideMap = new HashMap<>();
        iRawProteinMap = new HashMap<>();
        iSupportDataMap = new HashMap<>();
        iTitle2SpectrumIDMap = new HashMap<>();
        idToSpectrumMap = new HashMap<>();

        // List of all the protein ids
        iProteinKeyList = new ArrayList<>();

        // run xml document first to read all parameters 
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader parser = factory.createXMLStreamReader(new FileInputStream(aInputFile));
            while (parser.hasNext()) {
              switch (parser.getEventType()) {
                case XMLStreamConstants.START_DOCUMENT: break;
                  
                case XMLStreamConstants.END_DOCUMENT: parser.close(); break;

                case XMLStreamConstants.NAMESPACE: break;

                case XMLStreamConstants.CHARACTERS: break;
                
                case XMLStreamConstants.END_ELEMENT: break;

                case XMLStreamConstants.START_ELEMENT:
                    String element = parser.getLocalName();
                    if (element.equals("group") &&
                        parser.getAttributeValue("", "type") != null &&
                        parser.getAttributeValue("", "type").equalsIgnoreCase("parameters") &&
                        parser.getAttributeValue("", "label") != null){
                        if (parser.getAttributeValue("", "label").equalsIgnoreCase("input parameters") ||
                            parser.getAttributeValue("", "label").equalsIgnoreCase("unused input parameters")){
                            readInputParameter(parser);
                        }
                        else if (parser.getAttributeValue("", "label").equalsIgnoreCase("performance parameters")){
                            readPerformanceParameter(parser);
                        }
                    }                        
                    break;
                  

                default: break;
              }
              parser.next();
            }
        
        
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        
        // run xml document a second time to read all data
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader parser = factory.createXMLStreamReader(new FileInputStream(aInputFile));

            while (parser.hasNext()) {

                switch (parser.getEventType()) {
                    case XMLStreamConstants.START_DOCUMENT: break;

                    case XMLStreamConstants.END_DOCUMENT: parser.close(); break;

                    case XMLStreamConstants.NAMESPACE: break;

                    case XMLStreamConstants.CHARACTERS: break;

                    case XMLStreamConstants.END_ELEMENT: break;

                    case XMLStreamConstants.START_ELEMENT:
                        String element = parser.getLocalName();
                        if (element.equalsIgnoreCase("group") &&
                            parser.getAttributeValue("", "type") != null &&
                            "model".equalsIgnoreCase(parser.getAttributeValue("", "type"))) {
                            iNumberOfSpectra++;
                            
                            // id is the number associated with the mass spectrum that was identified
                            if (parser.getAttributeValue("", "id") != null) {
                                iRawSpectrumMap.put("id" + iNumberOfSpectra, parser.getAttributeValue("", "id"));
                            }
                            // z is the parent/precursor ion charge
                            if (parser.getAttributeValue("", "z") != null) {
                                iRawSpectrumMap.put("z" + iNumberOfSpectra, parser.getAttributeValue("", "z"));
                            }
                            if (!skipDetails) {
                                // expect is the expectation value for the top ranked protein identfied with this spectrum
                                if (parser.getAttributeValue("", "expect") != null) {
                                    iRawSpectrumMap.put("expect" + iNumberOfSpectra, parser.getAttributeValue("", "expect"));
                                }
                                // mh is the parent/precursor ion mass from the spectrum
                                if (parser.getAttributeValue("", "mh") != null) {
                                    iRawSpectrumMap.put("mh" + iNumberOfSpectra, parser.getAttributeValue("", "mh"));
                                }
                                // rt is the parent/precursor retention time
                                if (parser.getAttributeValue("", "rt") != null) {
                                    iRawSpectrumMap.put("rt" + iNumberOfSpectra, parser.getAttributeValue("", "rt"));
                                }
                                // label is the text from the protein sequence FASTA file description line for the top ranked protein identified
                                if (parser.getAttributeValue("", "label") != null) {
                                    iRawSpectrumMap.put("label" + iNumberOfSpectra, parser.getAttributeValue("", "label"));
                                }
                                // sumI is the log10-value of the sum of all of the fragment ion intensities
                                if (parser.getAttributeValue("", "sumI") != null) {
                                    iRawSpectrumMap.put("sumI" + iNumberOfSpectra, parser.getAttributeValue("", "sumI"));
                                }
                                // maxI is the maximum fragment ion intensity
                                if (parser.getAttributeValue("", "maxI") != null) {
                                    iRawSpectrumMap.put("maxI" + iNumberOfSpectra, parser.getAttributeValue("", "maxI"));
                                }
                                // fI is a multiplier to convert the normalized spectrum back to the original intensity values
                                if (parser.getAttributeValue("", "fI") != null) {
                                    iRawSpectrumMap.put("fI" + iNumberOfSpectra, parser.getAttributeValue("", "fI"));
                                }
                            }
                            

                            readGroupOrProtein(parser, skipDetails);

                        }
                        break;


                    default: break;
                }
                parser.next();
            }
        
        
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        
        
        
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

        
        int spectraCounter = 0;
        
        HashMap<String, String> iRawModMapOld = new HashMap<>();
        HashMap<String, String> iRawSpectrumMapOld = new HashMap<>();
        HashMap<String, String> iRawPeptideMapOld = new HashMap<>();
        HashMap<String, String> iRawProteinMapOld = new HashMap<>();
        HashMap<String, String> iSupportDataMapOld = new HashMap<>();
        HashMap<String, Integer> iTitle2SpectrumIDMapOld = new HashMap<>();
        HashMap<Integer, String> idToSpectrumMapOld = new HashMap<>();

        // List of all the protein ids
        iProteinKeyList = new ArrayList<>();
        
        // Iterate over all the nodes
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getAttributes() != null) {
                if (nodes.item(i).getAttributes().getNamedItem("type") != null) {

                    // The model group contains all information about a single peptide identification
                    if (nodes.item(i).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase("model")) {
                        spectraCounter++;

                        // id is the number associated with the mass spectrum that was identified
                        if (nodes.item(i).getAttributes().getNamedItem("id") != null) {
                            iRawSpectrumMapOld.put("id" + spectraCounter, nodes.item(i).getAttributes().getNamedItem("id").getNodeValue());
                        }
                        // z is the parent/precursor ion charge
                        if (nodes.item(i).getAttributes().getNamedItem("z") != null) {
                            iRawSpectrumMapOld.put("z" + spectraCounter, nodes.item(i).getAttributes().getNamedItem("z").getNodeValue());
                        }
                        if (!skipDetails) {
                            // expect is the expectation value for the top ranked protein identfied with this spectrum
                            if (nodes.item(i).getAttributes().getNamedItem("expect") != null) {
                                iRawSpectrumMapOld.put("expect" + spectraCounter, nodes.item(i).getAttributes().getNamedItem("expect").getNodeValue());
                            }
                            // mh is the parent/precursor ion mass from the spectrum
                            if (nodes.item(i).getAttributes().getNamedItem("mh") != null) {
                                iRawSpectrumMapOld.put("mh" + spectraCounter, nodes.item(i).getAttributes().getNamedItem("mh").getNodeValue());
                            }
                            // rt is the parent/precursor retention time
                            if (nodes.item(i).getAttributes().getNamedItem("rt") != null) {
                                iRawSpectrumMapOld.put("rt" + spectraCounter, nodes.item(i).getAttributes().getNamedItem("rt").getNodeValue());
                            }
                            // label is the text from the protein sequence FASTA file description line for the top ranked protein identified
                            if (nodes.item(i).getAttributes().getNamedItem("label") != null) {
                                iRawSpectrumMapOld.put("label" + spectraCounter, nodes.item(i).getAttributes().getNamedItem("label").getNodeValue());
                            }
                            // sumI is the log10-value of the sum of all of the fragment ion intensities
                            if (nodes.item(i).getAttributes().getNamedItem("sumI") != null) {
                                iRawSpectrumMapOld.put("sumI" + spectraCounter, nodes.item(i).getAttributes().getNamedItem("sumI").getNodeValue());
                            }
                            // maxI is the maximum fragment ion intensity
                            if (nodes.item(i).getAttributes().getNamedItem("maxI") != null) {
                                iRawSpectrumMapOld.put("maxI" + spectraCounter, nodes.item(i).getAttributes().getNamedItem("maxI").getNodeValue());
                            }
                            // fI is a multiplier to convert the normalized spectrum back to the original intensity values
                            if (nodes.item(i).getAttributes().getNamedItem("fI") != null) {
                                iRawSpectrumMapOld.put("fI" + spectraCounter, nodes.item(i).getAttributes().getNamedItem("fI").getNodeValue());
                            }
                        }
                    }
                }
            }

            // Get the identifications
            idNodes = nodes.item(i).getChildNodes();

            int p_count = 0;
            // Iterate over all the child nodes
            int idNodesLength = idNodes.getLength();
            for (int j = 0; j < idNodesLength; j++) {
                Node idNode = idNodes.item(j);
                NamedNodeMap idNodesAttributes = idNode.getAttributes();

                switch (idNode.getNodeName().toLowerCase()){
                    case "protein":
                    
                        p_count++;
                        // the identifier of this particular identification (spectrum#).(id#)
                        String protID = idNodesAttributes.getNamedItem("id").getNodeValue();

                        // Since the ID is not unique to the protein, we will use the label to reference it. That will be dirty for some files.
                        String proteinKey = idNodesAttributes.getNamedItem("label").getNodeValue();
                        if (!skipDetails) {
                            iProteinKeyList.add(proteinKey);

                            // a unique number of this protein, calculated by the search engine. Well unique. Most often yes.
                            iRawProteinMapOld.put("uid" + proteinKey, idNodesAttributes.getNamedItem("uid").getNodeValue());

                            // the log10 value of the expection value of the protein
                            iRawProteinMapOld.put("expect" + proteinKey, idNodesAttributes.getNamedItem("expect").getNodeValue());

                            // the description line from the FASTA file
                            iRawProteinMapOld.put("label" + proteinKey, idNodesAttributes.getNamedItem("label").getNodeValue());

                            // the sum of all of the fragment ions that identify this protein
                            iRawProteinMapOld.put("sumI" + proteinKey, idNodesAttributes.getNamedItem("sumI").getNodeValue());
                        }

                        proteinNodes = idNode.getChildNodes();

                        // Iterate over all the protein nodes
                        int proteinNodesLength = proteinNodes.getLength();
                        for (int k = 0; k < proteinNodesLength; k++) {
                            String proteinNodeName = proteinNodes.item(k).getNodeName();
                            NamedNodeMap proteinAttributes = proteinNodes.item(k).getAttributes();
                            String p_cnt = "_p" + p_count;
                            if (!skipDetails) {
                                if ("file".equalsIgnoreCase(proteinNodeName)) {
                                    // the path used to the original fasta file
                                    iRawPeptideMapOld.put("URL_s" + spectraCounter + p_cnt, proteinAttributes.getNamedItem("URL").getNodeValue());
                                }

                                else if ("note".equalsIgnoreCase(proteinNodeName) && proteinAttributes.getNamedItem("label") != null
                                        && "description".equalsIgnoreCase(proteinAttributes.getNamedItem("label").getNodeValue())) {
                                    // the protein description (xml tag: note label="description")
                                    String test = proteinNodes.item(k).getTextContent();
                                    iRawProteinMapOld.put("description" + proteinKey, test);
                                }

                            }
                            // the the sum of all the fragment ions that identify this protein
                            if ("peptide".equalsIgnoreCase(proteinNodeName)) {
                                iRawPeptideMapOld.put("s" + spectraCounter + p_cnt, protID);
                                if (!skipDetails) {
                                    iRawPeptideMapOld.put("start" + "_s" + spectraCounter + p_cnt, proteinAttributes.getNamedItem("start").getNodeValue());
                                    iRawPeptideMapOld.put("end" + "_s" + spectraCounter + p_cnt, proteinAttributes.getNamedItem("end").getNodeValue());
                                    iRawPeptideMapOld.put("seq" + "_s" + spectraCounter + p_cnt, proteinNodes.item(k).getTextContent());
                                }

                                peptideNodes = proteinNodes.item(k).getChildNodes();

                                // Domain counter
                                int dCount = 1;

                                // Iterate over all the peptide nodes
                                int peptideNodesLength = peptideNodes.getLength();
                                for (int m = 0; m < peptideNodesLength; m++) {

                                    Node peptideNode = peptideNodes.item(m);
                                    NamedNodeMap attributes = peptideNode.getAttributes();

                                    // Get the domain entries
                                    if ("domain".equalsIgnoreCase(peptideNode.getNodeName())) {

                                        // Get the domainid
                                        String domainKey = "s" + spectraCounter + p_cnt + "_d" + dCount++;

                                        // verify that the same domain key is not already in use
                                        // while (iRawPeptideMapOld.containsKey("proteinkey" + "_" + domainKey)) {
                                        //     domainKey = "s" + spectraCounter + "_p" + p_counter + "_d" + ++dCount;
                                        // }

                                        iRawPeptideMapOld.put("domainid_" + domainKey, attributes.getNamedItem("id").getNodeValue());

                                        // the start position of the peptide
                                        iRawPeptideMapOld.put("domainstart_" + domainKey, attributes.getNamedItem("start").getNodeValue());

                                        if (!skipDetails) {

                                            // Store the protein key a la Thilo. There should be only one protein key per domain.
                                            iRawPeptideMapOld.put("proteinkey_" + domainKey, proteinKey);

                                            // the end position of the peptide
                                            iRawPeptideMapOld.put("domainend_" + domainKey, attributes.getNamedItem("end").getNodeValue());

                                            // the mass + a proton
                                            iRawPeptideMapOld.put("mh_" + domainKey, attributes.getNamedItem("mh").getNodeValue());

                                            // the mass delta
                                            iRawPeptideMapOld.put("delta_" + domainKey, attributes.getNamedItem("delta").getNodeValue());

                                            // the hyper score
                                            iRawPeptideMapOld.put("hyperscore_" + domainKey, attributes.getNamedItem("hyperscore").getNodeValue());

                                            // the next score
                                            iRawPeptideMapOld.put("nextscore_" + domainKey, attributes.getNamedItem("nextscore").getNodeValue());

                                            if (xIonFlag) {
                                                // the x score
                                                iRawPeptideMapOld.put("x_score_" + domainKey, attributes.getNamedItem("y_score").getNodeValue());

                                                // the x ion number
                                                iRawPeptideMapOld.put("x_ions_" + domainKey, attributes.getNamedItem("y_ions").getNodeValue());
                                            }

                                            if (yIonFlag) {
                                                // the y score
                                                iRawPeptideMapOld.put("y_score_" + domainKey, attributes.getNamedItem("y_score").getNodeValue());

                                                // the y ion number
                                                iRawPeptideMapOld.put("y_ions_" + domainKey, attributes.getNamedItem("y_ions").getNodeValue());
                                            }

                                            if (zIonFlag) {
                                                // the z score
                                                iRawPeptideMapOld.put("z_score_" + domainKey, attributes.getNamedItem("y_score").getNodeValue());

                                                // the z ion number
                                                iRawPeptideMapOld.put("z_ions_" + domainKey, attributes.getNamedItem("y_ions").getNodeValue());
                                            }

                                            if (aIonFlag) {
                                                // the a score
                                                iRawPeptideMapOld.put("a_score_" + domainKey, attributes.getNamedItem("b_score").getNodeValue());

                                                // the a ion number
                                                iRawPeptideMapOld.put("a_ions_" + domainKey, attributes.getNamedItem("b_ions").getNodeValue());
                                            }

                                            if (bIonFlag) {
                                                // the b score
                                                iRawPeptideMapOld.put("b_score_" + domainKey, attributes.getNamedItem("b_score").getNodeValue());

                                                // the b ion number
                                                iRawPeptideMapOld.put("b_ions_" + domainKey, attributes.getNamedItem("b_ions").getNodeValue());
                                            }

                                            if (cIonFlag) {
                                                // the c score
                                                iRawPeptideMapOld.put("c_score_" + domainKey, attributes.getNamedItem("b_score").getNodeValue());

                                                // the c ion number
                                                iRawPeptideMapOld.put("c_ions_" + domainKey, attributes.getNamedItem("b_ions").getNodeValue());
                                            }

                                            // the upstream flanking sequence
                                            iRawPeptideMapOld.put("pre_" + domainKey, attributes.getNamedItem("pre").getNodeValue());

                                            // the downstream flanking sequence
                                            iRawPeptideMapOld.put("post_" + domainKey, attributes.getNamedItem("post").getNodeValue());

                                            // the number of missed cleavages
                                            iRawPeptideMapOld.put("missed_cleavages_" + domainKey, attributes.getNamedItem("missed_cleavages").getNodeValue());
                                        }

                                        // the expectation value
                                        iRawPeptideMapOld.put("expect_" + domainKey, attributes.getNamedItem("expect").getNodeValue());

                                        // the domain sequence
                                        iRawPeptideMapOld.put("domainseq_" + domainKey, attributes.getNamedItem("seq").getNodeValue());

                                        int modCounter = 0;
                                        NodeList peptideChildNodes = peptideNode.getChildNodes();
                                        int childNodesLength = peptideChildNodes.getLength();

                                        for (int n = 0; n < childNodesLength; n++) {

                                            Node pepChild = peptideChildNodes.item(n);

                                            // Get the specific modifications (aa)
                                            if ("aa".equalsIgnoreCase(pepChild.getNodeName())) {
                                                modCounter++;

                                                modificationMap = pepChild.getAttributes();
                                                modificationName = modificationMap.getNamedItem("type").getNodeValue();

                                                // use the old calculation with domainStart!
                                                //modificationMap.getNamedItem("at").getNodeValue()) - domainStart + 1)
                                                iRawModMapOld.put("at_" + domainKey + "_m" + modCounter, modificationMap.getNamedItem("at").getNodeValue());

                                                // modified is the residue mass change caused by the modification
                                                modificationMass = Double.parseDouble(modificationMap.getNamedItem("modified").getNodeValue());
                                                iRawModMapOld.put("modified_" + domainKey + "_m" + modCounter, modificationMap.getNamedItem("modified").getNodeValue());

                                                modificationName = modificationMass + "@" + modificationName;

                                                // type is the single letter abbreviation for the modified residue
                                                iRawModMapOld.put("name_" + domainKey + "_m" + modCounter, modificationName);

                                                // get the substituted amino acid (if any)
                                                if (modificationMap.getNamedItem("pm") != null) {
                                                    iRawModMapOld.put("pm_" + domainKey + "_m" + modCounter, modificationMap.getNamedItem("pm").getNodeValue());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                
                    // Go to the group node inside the other group node (support)
                    case "group":
                        // Start parsing the support data part (GAML histograms)
                        if (!skipDetails && "supporting data".equalsIgnoreCase(idNodesAttributes.getNamedItem("label").getNodeValue())) {
                            supportDataNodes = idNode.getChildNodes();

                            // Iterate over all the support data nodes
                            int supportDataNodesLength = supportDataNodes.getLength();
                            for (int a = 0; a < supportDataNodesLength; a++) {
                                Node supportDataNode = supportDataNodes.item(a);
                                NamedNodeMap supportDataAttributes = supportDataNode.getAttributes();


                                if ("GAML:trace".equalsIgnoreCase(supportDataNode.getNodeName())) {
                                    switch (supportDataAttributes.getNamedItem("type").getNodeValue().toLowerCase()){
                                        // Parse the hyperscore expectation function values
                                        case "hyperscore expectation function":
                                            iSupportDataMapOld.put("HYPERLABEL" + "_s" + spectraCounter,
                                                    supportDataAttributes.getNamedItem("label").getNodeValue());

                                            hyperNodes = supportDataNode.getChildNodes();
                                            // Iterate over the hyperscore nodes
                                            int hyperNodesLength = hyperNodes.getLength();
                                            for (int b = 0; b < hyperNodesLength; b++) {
                                                Node hyperNode = hyperNodes.item(b);

                                                String xyData = "";
                                                switch (hyperNode.getNodeName().toLowerCase()) {
                                                    case "gaml:attribute":
                                                        // Get the a0 value
                                                        switch (hyperNode.getAttributes().getNamedItem("type").getNodeValue().toLowerCase()) {
                                                            case "a0":  iSupportDataMapOld.put("HYPER_A0_s" + spectraCounter, hyperNode.getTextContent()); break;
                                                            case "a1": iSupportDataMapOld.put("HYPER_A1_s" + spectraCounter, hyperNode.getTextContent()); break;
                                                            default: break;
                                                        }
                                                        break;
                                                    case "gaml:xdata": xyData = "XVAL_HYPER_s" + spectraCounter; break;
                                                    case "gaml:ydata": xyData = "YVAL_HYPER_s" + spectraCounter; break;
                                                    default: break;
                                                }



                                                // Get the Xdata or Ydata
                                                if (xyData.length() > 0) {
                                                    NodeList dataNodes = hyperNodes.item(b).getChildNodes();
                                                    int dataNodesLength = dataNodes.getLength();
                                                    for (int d = 0; d < dataNodesLength; d++) {
                                                        Node dataNode = dataNodes.item(d);
                                                        if ("GAML:values".equalsIgnoreCase(dataNode.getNodeName())) iSupportDataMapOld.put(xyData, dataNode.getTextContent());
                                                    }
                                                }
                                            }
                                            break;

                                        // Parse the convolution survival funtion values
                                        case"convolution survival function":
                                            iSupportDataMapOld.put("CONVOLLABEL_s" + spectraCounter, supportDataAttributes.getNamedItem("label").getNodeValue());
                                            supportDataNodes.item(a).getChildNodes();

                                            convolNodes = supportDataNode.getChildNodes();
                                            // Iterate over the convolution nodes
                                            int convolNodesLength = convolNodes.getLength();
                                            for (int b = 0; b < convolNodesLength; b++) {
                                                Node convolNode = convolNodes.item(b);

                                                // Get the Xdata or Ydata                                            
                                                String xyData = "";
                                                switch(convolNode.getNodeName().toLowerCase()){
                                                    case "gaml:xdata": xyData = "XVAL_CONVOL_s" + spectraCounter; break;
                                                    case "gaml:ydata": xyData = "YVAL_CONVOL_s" + spectraCounter; break;
                                                    default: break;
                                                }

                                                if (xyData.length() > 0) {
                                                    NodeList dataNodes = convolNodes.item(b).getChildNodes();

                                                    int dataNodesLength = dataNodes.getLength();
                                                    for (int d = 0; d < dataNodesLength; d++) {
                                                        Node dataNode = dataNodes.item(d);
                                                        if ("GAML:values".equalsIgnoreCase(dataNode.getNodeName())) iSupportDataMapOld.put(xyData, dataNode.getTextContent());
                                                    }
                                                }
                                            }
                                            break;

                                        default: break;
                                    }




                                    if ("b ion histogram".equalsIgnoreCase(supportDataAttributes.getNamedItem("type").getNodeValue())) {
                                        if (aIonFlag) iSupportDataMapOld.put("A_IONLABEL_s" + spectraCounter, supportDataAttributes.getNamedItem("label").getNodeValue());
                                        if (bIonFlag) iSupportDataMapOld.put("B_IONLABEL_s" + spectraCounter, supportDataAttributes.getNamedItem("label").getNodeValue());
                                        if (cIonFlag) iSupportDataMapOld.put("C_IONLABEL_s" + spectraCounter, supportDataAttributes.getNamedItem("label").getNodeValue());

                                        NodeList ionNodes = supportDataNode.getChildNodes();

                                        // Iterate over the a ion nodes
                                        int ionNodesLength = ionNodes.getLength();
                                        for (int b = 0; b < ionNodesLength; b++) {
                                            // Get the Xdata
                                            Node ionNode = ionNodes.item(b);
                                            if ("GAML:Xdata".equalsIgnoreCase(ionNode.getNodeName())) {
                                                xDataNodes = ionNode.getChildNodes();
                                                int xDataNodesLength = xDataNodes.getLength();
                                                for (int d = 0; d < xDataNodesLength; d++) {
                                                    Node xDataNode = xDataNodes.item(d);
                                                    if ("GAML:values".equalsIgnoreCase(xDataNode.getNodeName())) {
                                                        if (aIonFlag) iSupportDataMapOld.put("XVAL_AIONS_s" + spectraCounter, xDataNode.getTextContent());
                                                        if (bIonFlag) iSupportDataMapOld.put("XVAL_BIONS_s" + spectraCounter, xDataNode.getTextContent());
                                                        if (cIonFlag) iSupportDataMapOld.put("XVAL_CIONS_s" + spectraCounter, xDataNode.getTextContent());
                                                    }
                                                }
                                            }
                                            // Get the Ydata
                                            else if ("GAML:Ydata".equalsIgnoreCase(ionNode.getNodeName())) {
                                                yDataNodes = ionNode.getChildNodes();
                                                int yDataNodesLength = yDataNodes.getLength();
                                                for (int e = 0; e < yDataNodesLength; e++) {
                                                    Node yDataNode = yDataNodes.item(e);
                                                    if ("GAML:values".equalsIgnoreCase(yDataNode.getNodeName())) {
                                                        if (aIonFlag) iSupportDataMapOld.put("YVAL_AIONS_s" + spectraCounter, yDataNode.getTextContent());
                                                        if (bIonFlag) iSupportDataMapOld.put("YVAL_BIONS_s" + spectraCounter, yDataNode.getTextContent());
                                                        if (cIonFlag) iSupportDataMapOld.put("YVAL_CIONS_s" + spectraCounter, yDataNode.getTextContent());
                                                    }
                                                }
                                            }
                                        }
                                    }


                                    else if ("b ion histogram".equalsIgnoreCase(supportDataAttributes.getNamedItem("type").getNodeValue())) {
                                        if (xIonFlag) iSupportDataMapOld.put("X_IONLABEL_s" + spectraCounter, supportDataAttributes.getNamedItem("label").getNodeValue());
                                        if (yIonFlag) iSupportDataMapOld.put("Y_IONLABEL_s" + spectraCounter, supportDataAttributes.getNamedItem("label").getNodeValue());
                                        if (zIonFlag) iSupportDataMapOld.put("Z_IONLABEL_s" + spectraCounter, supportDataAttributes.getNamedItem("label").getNodeValue());

                                        NodeList ionNodes = supportDataNode.getChildNodes();

                                        // Iterate over the a ion nodes
                                        int ionNodesLength = ionNodes.getLength();
                                        for (int b = 0; b < ionNodesLength; b++) {
                                            // Get the Xdata
                                            Node ionNode = ionNodes.item(b);
                                            if ("GAML:Xdata".equalsIgnoreCase(ionNode.getNodeName())) {
                                                xDataNodes = ionNode.getChildNodes();
                                                int xDataNodesLength = xDataNodes.getLength();
                                                for (int d = 0; d < xDataNodesLength; d++) {
                                                    Node xDataNode = xDataNodes.item(d);
                                                    if ("GAML:values".equalsIgnoreCase(xDataNode.getNodeName())) {
                                                        if (xIonFlag) iSupportDataMapOld.put("XVAL_XIONS_s" + spectraCounter, xDataNode.getTextContent());
                                                        if (yIonFlag) iSupportDataMapOld.put("XVAL_YIONS_s" + spectraCounter, xDataNode.getTextContent());
                                                        if (zIonFlag) iSupportDataMapOld.put("XVAL_ZIONS_s" + spectraCounter, xDataNode.getTextContent());
                                                    }
                                                }
                                            }
                                            // Get the Ydata
                                            else if ("GAML:Ydata".equalsIgnoreCase(ionNode.getNodeName())) {
                                                yDataNodes = ionNode.getChildNodes();
                                                int yDataNodesLength = yDataNodes.getLength();
                                                for (int e = 0; e < yDataNodesLength; e++) {
                                                    Node yDataNode = yDataNodes.item(e);
                                                    if ("GAML:values".equalsIgnoreCase(yDataNode.getNodeName())) {
                                                        if (xIonFlag) iSupportDataMapOld.put("YVAL_XIONS_s" + spectraCounter, yDataNode.getTextContent());
                                                        if (yIonFlag) iSupportDataMapOld.put("YVAL_YIONS_s" + spectraCounter, yDataNode.getTextContent());
                                                        if (zIonFlag) iSupportDataMapOld.put("YVAL_ZIONS_s" + spectraCounter, yDataNode.getTextContent());
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }

                        if ("fragment ion mass spectrum".equalsIgnoreCase(idNodesAttributes.getNamedItem("label").getNodeValue())) {

                            supportDataNodes = idNode.getChildNodes();
                            // Iterate over all the support data nodes
                            int supportDataNodesLength = supportDataNodes.getLength();
                            for (int a = 0; a < supportDataNodesLength; a++) {
                                Node supportDataNode = supportDataNodes.item(a);
                                if ("note".equalsIgnoreCase(supportDataNode.getNodeName())) {
                                    String title = supportDataNode.getTextContent().trim();
                                    idToSpectrumMapOld.put(spectraCounter, title);
                                    if (!skipDetails) {
                                        iSupportDataMapOld.put("FRAGIONSPECDESC_s" + spectraCounter, title);
                                        iTitle2SpectrumIDMapOld.put(title, spectraCounter);
                                    } else {
                                        break;
                                    }
                                }
                                if (!skipDetails && "GAML:trace".equalsIgnoreCase(supportDataNode.getNodeName()) && "tandem mass spectrum".equalsIgnoreCase(supportDataNode.getAttributes().getNamedItem("type").getNodeValue())) {
                                    iSupportDataMapOld.put("SPECTRUMLABEL_s" + spectraCounter, supportDataNode.getAttributes().getNamedItem("label").getNodeValue());

                                    fragIonNodes = supportDataNode.getChildNodes();
                                    // Iterate over the fragment ion nodes
                                    int fragIonNodesLength = fragIonNodes.getLength();
                                    for (int b = 0; b < fragIonNodesLength; b++) {
                                        Node fragIonNode = fragIonNodes.item(b);

                                        String xyData = "";
                                        switch (fragIonNode.getNodeName().toLowerCase()){
                                            case "gaml:attribute":
                                                // Get the a0 value
                                                switch (fragIonNode.getAttributes().getNamedItem("type").getNodeValue().toLowerCase()){
                                                    case "m+h": iSupportDataMapOld.put("FRAGIONMZ_s" + spectraCounter, fragIonNode.getTextContent()); break;
                                                    case "charge": iSupportDataMapOld.put("FRAGIONCHARGE_s" + spectraCounter, fragIonNode.getTextContent()); break;
                                                    default: break;
                                                }
                                                break;
                                            case "gaml:xdata": xyData = "XVAL_FRAGIONMZ_s" + spectraCounter; break;
                                            case "gaml:ydata": xyData = "YVAL_FRAGIONMZ_s" + spectraCounter; break;
                                            default: break;
                                        }

                                        if (xyData.length() > 0){
                                            NodeList dataNodes = fragIonNode.getChildNodes();
                                            int dataNodesLength = dataNodes.getLength();
                                            for (int d = 0; d < dataNodesLength; d++) {
                                                Node dataNode = dataNodes.item(d);
                                                if (dataNode.getNodeName().equalsIgnoreCase("GAML:values")) {
                                                    iSupportDataMapOld.put(xyData, dataNode.getTextContent());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    default: break;
                }
            }
        }
        
        /*
        HashMap<String, String> iRawSpectrumMapOld = new HashMap<>();
        HashMap<String, String> iRawPeptideMapOld = new HashMap<>();
        HashMap<String, String> iRawProteinMapOld = new HashMap<>();
        
        
        HashMap<String, String> iRawModMapOld = new HashMap<>();
        HashMap<String, String> iSupportDataMapOld = new HashMap<>();
        HashMap<String, Integer> iTitle2SpectrumIDMapOld = new HashMap<>();
        HashMap<Integer, String> idToSpectrumMapOld = new HashMap<>();*/
        
        
        System.out.println("start comparison with " + iSupportDataMapOld.size() + " vs. " + iSupportDataMap.size());
        
        for (String key : iSupportDataMapOld.keySet()){
            boolean contains = iSupportDataMap.containsKey(key);
            boolean equal = contains ? iSupportDataMap.get(key).equals(iSupportDataMapOld.get(key)) : false;
            if (!contains){
                System.out.println("!contains + " + key);
            }
            else if (!equal){
                System.out.println("!equal for key: " + key + " -> '" + iSupportDataMapOld.get(key) + "' vs. '" + iSupportDataMap.get(key) + "'");
            }
        }
        
        System.out.println("end comparison");
        
        
        
        
        
        
        
        
        
        
        System.exit(0);
    }
    
    
    private void readGroupOrProtein(XMLStreamReader parser, boolean skipDetails) throws XMLStreamException{
        int p_counter = 0;
        while (parser.hasNext()) {
            switch (parser.getEventType()) {
                case XMLStreamConstants.START_DOCUMENT: return;
                case XMLStreamConstants.END_DOCUMENT: return;
                case XMLStreamConstants.NAMESPACE: break;
                case XMLStreamConstants.CHARACTERS: break;
                case XMLStreamConstants.END_ELEMENT:
                    if ("group".equalsIgnoreCase(parser.getLocalName())) return;
                    break;

                case XMLStreamConstants.START_ELEMENT:
                    switch(parser.getLocalName().toLowerCase()){
                        case "group":
                            if (!skipDetails && parser.getAttributeValue("", "label") != null && "supporting data".equalsIgnoreCase(parser.getAttributeValue("", "label"))) {
                                readGroupSupport(parser, skipDetails);
                            }
                            else if  (!skipDetails && parser.getAttributeValue("", "label") != null && "fragment ion mass spectrum".equalsIgnoreCase(parser.getAttributeValue("", "label"))) {
                                readGroupFragment(parser, skipDetails);
                            }
                            break;
                        
                        case "protein":
                            p_counter++;
                            // the identifier of this particular identification (spectrum#).(id#)
                            String protID = parser.getAttributeValue("", "id");

                            // Since the ID is not unique to the protein, we will use the label to reference it. That will be dirty for some files.
                            String proteinKey = parser.getAttributeValue("", "label");
                            if (!skipDetails) {
                                iProteinKeyList.add(proteinKey);

                                // a unique number of this protein, calculated by the search engine. Well unique. Most often yes.
                                iRawProteinMap.put("uid" + proteinKey, parser.getAttributeValue("", "uid"));

                                // the log10 value of the expection value of the protein
                                iRawProteinMap.put("expect" + proteinKey, parser.getAttributeValue("", "expect"));

                                // the description line from the FASTA file
                                iRawProteinMap.put("label" + proteinKey, parser.getAttributeValue("", "label"));

                                // the sum of all of the fragment ions that identify this protein
                                iRawProteinMap.put("sumI" + proteinKey, parser.getAttributeValue("", "sumI"));
                            }
                            
                            
                            
                            readProtein(parser, proteinKey, protID, p_counter, skipDetails);
                            break;
                            
                        default: break;
                    }
                    break;


                default: break;
            }
            parser.next();
        }
    }
    
    
    
    
        
    private void readGroupFragment(XMLStreamReader parser, boolean skipDetails) throws XMLStreamException{
        boolean write = false;
        while (parser.hasNext()) {
            switch (parser.getEventType()) {
                case XMLStreamConstants.START_DOCUMENT: return;
                case XMLStreamConstants.END_DOCUMENT: return;
                case XMLStreamConstants.NAMESPACE: break;
                case XMLStreamConstants.CHARACTERS:
                    if (write){
                        iSupportDataMap.put("FRAGIONSPECDESC_s" + iNumberOfSpectra, parser.getText());
                        iTitle2SpectrumIDMap.put(parser.getText(), iNumberOfSpectra);
                        write = false;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if ("group".equalsIgnoreCase(parser.getLocalName()))  return;
                    break;

                case XMLStreamConstants.START_ELEMENT:
                    switch(parser.getLocalName().toLowerCase()){
                        case "note":
                            String title = parser.getLocalName().trim();
                            idToSpectrumMap.put(iNumberOfSpectra, title);
                            if (!skipDetails) {
                                write = true;
                            }
                            break;
                    
                        case "trace":
                            if (parser.getAttributeValue("", "type") != null && "tandem mass spectrum".equalsIgnoreCase(parser.getAttributeValue("", "type"))){
                                iSupportDataMap.put("SPECTRUMLABEL_s" + iNumberOfSpectra, parser.getAttributeValue("", "label"));
                                readGroupFragmentTrace(parser);
                            }
                            break;
                            
                        default:
                            break;
                    }
                    break;


                default: break;
            }
            parser.next();
        }
    }
    
    private void readGroupFragmentTrace(XMLStreamReader parser) throws XMLStreamException{
        String supportData = "";
        boolean value = false;
        String xyData = "";
        while (parser.hasNext()) {
            switch (parser.getEventType()) {
                case XMLStreamConstants.START_DOCUMENT: return;
                case XMLStreamConstants.END_DOCUMENT: return;
                case XMLStreamConstants.NAMESPACE: break;
                case XMLStreamConstants.CHARACTERS:
                    switch (supportData.toLowerCase()){
                        case "m+h": iSupportDataMap.put("FRAGIONMZ_s" + iNumberOfSpectra, parser.getText()); break;
                        case "charge": iSupportDataMap.put("FRAGIONCHARGE_s" + iNumberOfSpectra, parser.getText()); break;
                        default: break;
                    }
                    if (xyData.length() > 0 && value){
                        iSupportDataMap.put(xyData, parser.getText());
                        xyData = "";
                        value = false;
                    }
                    supportData = "";
                    break;
                    
                case XMLStreamConstants.END_ELEMENT:
                    if ("trace".equalsIgnoreCase(parser.getLocalName())) return;
                    break;

                case XMLStreamConstants.START_ELEMENT:
                    switch(parser.getLocalName().toLowerCase()){
                        case "attribute":
                            supportData = parser.getAttributeValue("", "type");
                            break;
                        case "xdata": xyData = "XVAL_FRAGIONMZ_s" + iNumberOfSpectra; break;
                        case "ydata": xyData = "YVAL_FRAGIONMZ_s" + iNumberOfSpectra; break;
                        case "values": value = true; break;
                        default: break;
                    }
                    break;


                default: break;
            }
            parser.next();
        }
    }
    
    private void readGroupSupportValues(XMLStreamReader parser, boolean abc, boolean convolution) throws XMLStreamException{
        boolean xData = false;
        boolean write = false;
        while (parser.hasNext()) {
            switch (parser.getEventType()) {
                case XMLStreamConstants.START_DOCUMENT: return;
                case XMLStreamConstants.END_DOCUMENT: return;
                case XMLStreamConstants.NAMESPACE: break;
                case XMLStreamConstants.CHARACTERS:
                    if (write){
                        if (convolution){
                            if (xData){
                                iSupportDataMap.put("XVAL_CONVOL_s" + iNumberOfSpectra, parser.getText());
                            }
                            else {
                                iSupportDataMap.put("YVAL_CONVOL_s" + iNumberOfSpectra, parser.getText());
                            }
                        }
                        else {
                            if (abc){
                                if (xData){
                                    if (aIonFlag) iSupportDataMap.put("XVAL_AIONS_s" + iNumberOfSpectra, parser.getText());
                                    if (bIonFlag) iSupportDataMap.put("XVAL_BIONS_s" + iNumberOfSpectra, parser.getText());
                                    if (cIonFlag) iSupportDataMap.put("XVAL_CIONS_s" + iNumberOfSpectra, parser.getText());
                                }
                                else {
                                    if (aIonFlag) iSupportDataMap.put("YVAL_AIONS_s" + iNumberOfSpectra, parser.getText());
                                    if (bIonFlag) iSupportDataMap.put("YVAL_BIONS_s" + iNumberOfSpectra, parser.getText());
                                    if (cIonFlag) iSupportDataMap.put("YVAL_CIONS_s" + iNumberOfSpectra, parser.getText());
                                }
                            }
                            else {
                                if (xData){
                                    if (xIonFlag) iSupportDataMap.put("XVAL_AIONS_s" + iNumberOfSpectra, parser.getText());
                                    if (yIonFlag) iSupportDataMap.put("XVAL_BIONS_s" + iNumberOfSpectra, parser.getText());
                                    if (zIonFlag) iSupportDataMap.put("XVAL_CIONS_s" + iNumberOfSpectra, parser.getText());
                                }
                                else {
                                    if (xIonFlag) iSupportDataMap.put("YVAL_AIONS_s" + iNumberOfSpectra, parser.getText());
                                    if (yIonFlag) iSupportDataMap.put("YVAL_BIONS_s" + iNumberOfSpectra, parser.getText());
                                    if (zIonFlag) iSupportDataMap.put("YVAL_CIONS_s" + iNumberOfSpectra, parser.getText());
                                }
                            }
                        }
                        write = false;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if ("value".equalsIgnoreCase(parser.getLocalName())) return;
                    break;

                case XMLStreamConstants.START_ELEMENT:
                    switch (parser.getLocalName().toLowerCase()){
                        case "values": write = true; break;
                        case "xdata": xData = true; break;
                        case "ydata": xData = false; break;
                        default: break;
                    }
                    break;
                default: break;
            }
            parser.next();
        }            
    }
    
    
    
    private void readGroupSupportHyperScore(XMLStreamReader parser, boolean skipDetails) throws XMLStreamException{
        String xyData = "", attribute = "";
        boolean write = false;
        while (parser.hasNext()) {
            switch (parser.getEventType()) {
                case XMLStreamConstants.START_DOCUMENT: return;
                case XMLStreamConstants.END_DOCUMENT: return;
                case XMLStreamConstants.NAMESPACE: break;
                case XMLStreamConstants.CHARACTERS:
                    if (write){
                        iSupportDataMap.put((attribute.length() > 0) ? attribute : xyData, parser.getText());
                        attribute = "";
                        xyData = "";
                        write = false;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if ("trace".equalsIgnoreCase(parser.getLocalName())) return;
                    break;

                case XMLStreamConstants.START_ELEMENT:
                    switch (parser.getLocalName().toLowerCase()) {
                        case "attribute":
                            // Get the a0 value
                            switch (parser.getAttributeValue("", "type").toLowerCase()) {
                                case "a0": attribute = "HYPER_A0_s" + iNumberOfSpectra; write = true; break;
                                case "a1": attribute = "HYPER_A1_s" + iNumberOfSpectra; write = true; break;
                                default: break;
                            }
                            break;
                        case "xdata": xyData = "XVAL_HYPER_s" + iNumberOfSpectra; break;
                        case "ydata": xyData = "YVAL_HYPER_s" + iNumberOfSpectra; break;
                        case "values": write = true; break;
                        default: break;
                    }
                    break;
                default: break;
            }
            parser.next();
        }            
    }
    
    private void readGroupSupport(XMLStreamReader parser, boolean skipDetails) throws XMLStreamException{
        while (parser.hasNext()) {

            switch (parser.getEventType()) {
                case XMLStreamConstants.START_DOCUMENT: return;
                case XMLStreamConstants.END_DOCUMENT: return;
                case XMLStreamConstants.NAMESPACE: break;
                case XMLStreamConstants.CHARACTERS: break;
                case XMLStreamConstants.END_ELEMENT:
                    if ("trace".equalsIgnoreCase(parser.getLocalName())) return;
                    break;

                case XMLStreamConstants.START_ELEMENT:
                    // Start parsing the support data part (GAML histograms)
                    if ("trace".equalsIgnoreCase(parser.getLocalName()) && parser.getAttributeValue("", "type") != null){
                        switch(parser.getAttributeValue("", "type").toLowerCase()){
                            case "hyperscore expectation function":
                                iSupportDataMap.put("HYPERLABEL" + "_s" + iNumberOfSpectra, parser.getAttributeValue("", "label"));
                                readGroupSupportHyperScore(parser, skipDetails);
                                break;
                            
                            case "convolution survival function":
                                iSupportDataMap.put("CONVOLLABEL_s" + iNumberOfSpectra, parser.getAttributeValue("", "label"));
                                readGroupSupportValues(parser, false, true);
                                break;
                                
                            case "b ion histogram":
                                if (aIonFlag) iSupportDataMap.put("A_IONLABEL_s" + iNumberOfSpectra, parser.getAttributeValue("", "label"));
                                if (bIonFlag) iSupportDataMap.put("B_IONLABEL_s" + iNumberOfSpectra, parser.getAttributeValue("", "label"));
                                if (cIonFlag) iSupportDataMap.put("C_IONLABEL_s" + iNumberOfSpectra, parser.getAttributeValue("", "label"));
                                readGroupSupportValues(parser, true, false);
                                break;
                                
                            case "y ion histogram":
                                if (xIonFlag) iSupportDataMap.put("X_IONLABEL_s" + iNumberOfSpectra, parser.getAttributeValue("", "label"));
                                if (yIonFlag) iSupportDataMap.put("Y_IONLABEL_s" + iNumberOfSpectra, parser.getAttributeValue("", "label"));
                                if (zIonFlag) iSupportDataMap.put("Z_IONLABEL_s" + iNumberOfSpectra, parser.getAttributeValue("", "label"));
                                readGroupSupportValues(parser, false, false);
                                break;
                        }
                    }
                    break;


                default: break;
            }
            parser.next();
        }
    }
    
    
    private void readProtein(XMLStreamReader parser, String proteinKey, String protID, int p_counter, boolean skipDetails) throws XMLStreamException{
        String noteKey = "";
        String p_cnt = "_p" + p_counter;
        while (parser.hasNext()) {
            switch (parser.getEventType()) {
                case XMLStreamConstants.START_DOCUMENT: return;
                case XMLStreamConstants.END_DOCUMENT: return;
                case XMLStreamConstants.NAMESPACE: break;
                case XMLStreamConstants.CHARACTERS:
                    if (noteKey.length() > 0){
                        iRawProteinMap.put(noteKey, parser.getText());
                        noteKey = "";
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if ("protein".equalsIgnoreCase(parser.getLocalName())) return;
                    break;

                case XMLStreamConstants.START_ELEMENT:
                    switch(parser.getLocalName().toLowerCase()){
                        case "file":
                            if (!skipDetails) iRawPeptideMap.put("URL_s" + iNumberOfSpectra + p_cnt, parser.getAttributeValue("", "URL"));
                            break;
                        case "note":
                            if (!skipDetails && parser.getAttributeValue("", "label") != null && "description".equalsIgnoreCase(parser.getAttributeValue("", "label"))){
                                noteKey = "description" + proteinKey;
                            }
                            break;
                        case "peptide":
                            
                            iRawPeptideMap.put("s" + iNumberOfSpectra + p_cnt, protID);
                            if (!skipDetails) {
                                iRawPeptideMap.put("start" + "_s" + iNumberOfSpectra + p_cnt, parser.getAttributeValue("", "start"));
                                iRawPeptideMap.put("end" + "_s" + iNumberOfSpectra + p_cnt, parser.getAttributeValue("", "end"));
                                iRawPeptideMap.put("seq" + "_s" + iNumberOfSpectra + p_cnt, "");
                            }
                            readPeptide(parser, proteinKey, p_cnt, skipDetails);
                            break;
                        default: break;
                    }
                    break;


                default: break;
            }
            parser.next();
        }
    }
    
    private void readPeptide(XMLStreamReader parser, String proteinKey, String p_cnt, boolean skipDetails) throws XMLStreamException{
        int dCount = 1;
        while (parser.hasNext()) {
            switch (parser.getEventType()) {
                case XMLStreamConstants.START_DOCUMENT: return;
                case XMLStreamConstants.END_DOCUMENT: return;
                case XMLStreamConstants.NAMESPACE: break;
                case XMLStreamConstants.CHARACTERS: break;
                case XMLStreamConstants.END_ELEMENT:
                    if ("peptide".equalsIgnoreCase(parser.getLocalName())) return;
                    break;

                case XMLStreamConstants.START_ELEMENT:
                    if ("domain".equalsIgnoreCase(parser.getLocalName().toLowerCase())){

                        // Get the domainid
                        String domainKey = "s" + iNumberOfSpectra + p_cnt + "_d" + dCount++;

                        iRawPeptideMap.put("domainid_" + domainKey, parser.getAttributeValue("", "id"));

                        // the start position of the peptide
                        iRawPeptideMap.put("domainstart_" + domainKey, parser.getAttributeValue("", "start"));

                        if (!skipDetails) {

                            // Store the protein key a la Thilo. There should be only one protein key per domain.
                            iRawPeptideMap.put("proteinkey_" + domainKey, proteinKey);

                            // the end position of the peptide
                            iRawPeptideMap.put("domainend_" + domainKey, parser.getAttributeValue("", "end"));

                            // the mass + a proton
                            iRawPeptideMap.put("mh_" + domainKey, parser.getAttributeValue("", "mh"));

                            // the mass delta
                            iRawPeptideMap.put("delta_" + domainKey, parser.getAttributeValue("", "delta"));

                            // the hyper score
                            iRawPeptideMap.put("hyperscore_" + domainKey, parser.getAttributeValue("", "hyperscore"));

                            // the next score
                            iRawPeptideMap.put("nextscore_" + domainKey, parser.getAttributeValue("", "nextscore"));

                            if (xIonFlag) {
                                // the x score
                                iRawPeptideMap.put("x_score_" + domainKey, parser.getAttributeValue("", "y_score"));

                                // the x ion number
                                iRawPeptideMap.put("x_ions_" + domainKey, parser.getAttributeValue("", "y_ions"));
                            }

                            if (yIonFlag) {
                                // the y score
                                iRawPeptideMap.put("y_score_" + domainKey, parser.getAttributeValue("", "y_score"));

                                // the y ion number
                                iRawPeptideMap.put("y_ions_" + domainKey, parser.getAttributeValue("", "y_ions"));
                            }

                            if (zIonFlag) {
                                // the z score
                                iRawPeptideMap.put("z_score_" + domainKey, parser.getAttributeValue("", "y_score"));

                                // the z ion number
                                iRawPeptideMap.put("z_ions_" + domainKey, parser.getAttributeValue("", "y_ions"));
                            }

                            if (aIonFlag) {
                                // the a score
                                iRawPeptideMap.put("a_score_" + domainKey, parser.getAttributeValue("", "b_score"));

                                // the a ion number
                                iRawPeptideMap.put("a_ions_" + domainKey, parser.getAttributeValue("", "b_ions"));
                            }

                            if (bIonFlag) {
                                // the b score
                                iRawPeptideMap.put("b_score_" + domainKey, parser.getAttributeValue("", "b_score"));

                                // the b ion number
                                iRawPeptideMap.put("b_ions_" + domainKey, parser.getAttributeValue("", "b_ions"));
                            }

                            if (cIonFlag) {
                                // the c score
                                iRawPeptideMap.put("c_score_" + domainKey, parser.getAttributeValue("", "b_score"));

                                // the c ion number
                                iRawPeptideMap.put("c_ions_" + domainKey, parser.getAttributeValue("", "b_ions"));
                            }

                            // the upstream flanking sequence
                            iRawPeptideMap.put("pre_" + domainKey, parser.getAttributeValue("", "pre"));

                            // the downstream flanking sequence
                            iRawPeptideMap.put("post_" + domainKey, parser.getAttributeValue("", "post"));

                            // the number of missed cleavages
                            iRawPeptideMap.put("missed_cleavages_" + domainKey, parser.getAttributeValue("", "missed_cleavages"));
                        }

                        // the expectation value
                        iRawPeptideMap.put("expect_" + domainKey, parser.getAttributeValue("", "expect"));

                        // the domain sequence
                        iRawPeptideMap.put("domainseq_" + domainKey, parser.getAttributeValue("", "seq"));
                        readPeptideDomain(parser, domainKey, skipDetails);
                    }
                    break;


                default: break;
            }
            parser.next();
        }
    }

    private void readPeptideDomain(XMLStreamReader parser, String domainKey, boolean skipDetails) throws XMLStreamException{
        int modCounter = 0;
        while (parser.hasNext()) {
            switch (parser.getEventType()) {
                case XMLStreamConstants.START_DOCUMENT: return;
                case XMLStreamConstants.END_DOCUMENT: return;
                case XMLStreamConstants.NAMESPACE: break;
                case XMLStreamConstants.CHARACTERS: break;
                case XMLStreamConstants.END_ELEMENT:
                    if ("domain".equalsIgnoreCase(parser.getLocalName())) return;
                    break;

                case XMLStreamConstants.START_ELEMENT:
                    if ("aa".equalsIgnoreCase(parser.getLocalName().toLowerCase())){
                        modCounter++;

                        String modificationName = parser.getAttributeValue("", "type");

                        iRawModMap.put("at_" + domainKey + "_m" + modCounter, parser.getAttributeValue("", "at"));

                        // modified is the residue mass change caused by the modification
                        double modificationMass = Double.parseDouble(parser.getAttributeValue("", "modified"));
                        iRawModMap.put("modified_" + domainKey + "_m" + modCounter, parser.getAttributeValue("", "modified"));

                        modificationName = modificationMass + "@" + modificationName;

                        // type is the single letter abbreviation for the modified residue
                        iRawModMap.put("name_" + domainKey + "_m" + modCounter, modificationName);

                        // get the substituted amino acid (if any)
                        if (parser.getAttributeValue("", "pm") != null) {
                            iRawModMap.put("pm_" + domainKey + "_m" + modCounter, parser.getAttributeValue("", "pm"));
                        }
                    }
                    break;


                default: break;
            }
            parser.next();
        }
    }

/*
        

            // the number of spectra.
            iNumberOfSpectra = spectraCounter;

            // Get the identifications
            idNodes = nodes.item(i).getChildNodes();

            int p_counter = 0;
            // Iterate over all the child nodes
            int idNodesLength = idNodes.getLength();
            for (int j = 0; j < idNodesLength; j++) {
                Node idNode = idNodes.item(j);
                NamedNodeMap idNodesAttributes = idNode.getAttributes();

                switch (idNode.getNodeName().toLowerCase()){
                    case "protein":
                    
                        p_counter++;
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

                        proteinNodes = idNode.getChildNodes();

                        // Iterate over all the protein nodes
                        int proteinNodesLength = proteinNodes.getLength();
                        for (int k = 0; k < proteinNodesLength; k++) {
                            String proteinNodeName = proteinNodes.item(k).getNodeName();
                            NamedNodeMap proteinAttributes = proteinNodes.item(k).getAttributes();
                                String p_cnt = "_p" + p_counter;
                            if (!skipDetails) {
                                if ("file".equalsIgnoreCase(proteinNodeName)) {
                                    // the path used to the original fasta file
                                    iRawPeptideMap.put("URL_s" + spectraCounter + p_cnt, proteinAttributes.getNamedItem("URL").getNodeValue());
                                }

                                else if ("note".equalsIgnoreCase(proteinNodeName) && proteinAttributes.getNamedItem("label") != null
                                        && "description".equalsIgnoreCase(proteinAttributes.getNamedItem("label").getNodeValue())) {
                                    // the protein description (xml tag: note label="description")
                                    String test = proteinNodes.item(k).getTextContent();
                                    iRawProteinMap.put("description" + proteinKey, test);
                                }

                            }
                            // the the sum of all the fragment ions that identify this protein
                            if ("peptide".equalsIgnoreCase(proteinNodeName)) {
                                iRawPeptideMap.put("s" + spectraCounter + p_cnt, protID);
                                if (!skipDetails) {
                                    iRawPeptideMap.put("start" + "_s" + spectraCounter + p_cnt, proteinAttributes.getNamedItem("start").getNodeValue());
                                    iRawPeptideMap.put("end" + "_s" + spectraCounter + p_cnt, proteinAttributes.getNamedItem("end").getNodeValue());
                                    iRawPeptideMap.put("seq" + "_s" + spectraCounter + p_cnt, proteinNodes.item(k).getTextContent());
                                }

                                peptideNodes = proteinNodes.item(k).getChildNodes();

                                // Domain counter
                                int dCount = 1;

                                // Iterate over all the peptide nodes
                                int peptideNodesLength = peptideNodes.getLength();
                                for (int m = 0; m < peptideNodesLength; m++) {

                                    Node peptideNode = peptideNodes.item(m);
                                    NamedNodeMap attributes = peptideNode.getAttributes();

                                    // Get the domain entries
                                    if ("domain".equalsIgnoreCase(peptideNode.getNodeName())) {

                                        // Get the domainid
                                        String domainKey = "s" + spectraCounter + p_cnt + "_d" + dCount++;

                                        // verify that the same domain key is not already in use
                                        // while (iRawPeptideMap.containsKey("proteinkey" + "_" + domainKey)) {
                                        //     domainKey = "s" + spectraCounter + "_p" + p_counter + "_d" + ++dCount;
                                        // }

                                        iRawPeptideMap.put("domainid_" + domainKey, attributes.getNamedItem("id").getNodeValue());

                                        // the start position of the peptide
                                        iRawPeptideMap.put("domainstart_" + domainKey, attributes.getNamedItem("start").getNodeValue());

                                        if (!skipDetails) {

                                            // Store the protein key a la Thilo. There should be only one protein key per domain.
                                            iRawPeptideMap.put("proteinkey_" + domainKey, proteinKey);

                                            // the end position of the peptide
                                            iRawPeptideMap.put("domainend_" + domainKey, attributes.getNamedItem("end").getNodeValue());

                                            // the mass + a proton
                                            iRawPeptideMap.put("mh_" + domainKey, attributes.getNamedItem("mh").getNodeValue());

                                            // the mass delta
                                            iRawPeptideMap.put("delta_" + domainKey, attributes.getNamedItem("delta").getNodeValue());

                                            // the hyper score
                                            iRawPeptideMap.put("hyperscore_" + domainKey, attributes.getNamedItem("hyperscore").getNodeValue());

                                            // the next score
                                            iRawPeptideMap.put("nextscore_" + domainKey, attributes.getNamedItem("nextscore").getNodeValue());

                                            if (xIonFlag) {
                                                // the x score
                                                iRawPeptideMap.put("x_score_" + domainKey, attributes.getNamedItem("y_score").getNodeValue());

                                                // the x ion number
                                                iRawPeptideMap.put("x_ions_" + domainKey, attributes.getNamedItem("y_ions").getNodeValue());
                                            }

                                            if (yIonFlag) {
                                                // the y score
                                                iRawPeptideMap.put("y_score_" + domainKey, attributes.getNamedItem("y_score").getNodeValue());

                                                // the y ion number
                                                iRawPeptideMap.put("y_ions_" + domainKey, attributes.getNamedItem("y_ions").getNodeValue());
                                            }

                                            if (zIonFlag) {
                                                // the z score
                                                iRawPeptideMap.put("z_score_" + domainKey, attributes.getNamedItem("y_score").getNodeValue());

                                                // the z ion number
                                                iRawPeptideMap.put("z_ions_" + domainKey, attributes.getNamedItem("y_ions").getNodeValue());
                                            }

                                            if (aIonFlag) {
                                                // the a score
                                                iRawPeptideMap.put("a_score_" + domainKey, attributes.getNamedItem("b_score").getNodeValue());

                                                // the a ion number
                                                iRawPeptideMap.put("a_ions_" + domainKey, attributes.getNamedItem("b_ions").getNodeValue());
                                            }

                                            if (bIonFlag) {
                                                // the b score
                                                iRawPeptideMap.put("b_score_" + domainKey, attributes.getNamedItem("b_score").getNodeValue());

                                                // the b ion number
                                                iRawPeptideMap.put("b_ions_" + domainKey, attributes.getNamedItem("b_ions").getNodeValue());
                                            }

                                            if (cIonFlag) {
                                                // the c score
                                                iRawPeptideMap.put("c_score_" + domainKey, attributes.getNamedItem("b_score").getNodeValue());

                                                // the c ion number
                                                iRawPeptideMap.put("c_ions_" + domainKey, attributes.getNamedItem("b_ions").getNodeValue());
                                            }

                                            // the upstream flanking sequence
                                            iRawPeptideMap.put("pre_" + domainKey, attributes.getNamedItem("pre").getNodeValue());

                                            // the downstream flanking sequence
                                            iRawPeptideMap.put("post_" + domainKey, attributes.getNamedItem("post").getNodeValue());

                                            // the number of missed cleavages
                                            iRawPeptideMap.put("missed_cleavages_" + domainKey, attributes.getNamedItem("missed_cleavages").getNodeValue());
                                        }

                                        // the expectation value
                                        iRawPeptideMap.put("expect_" + domainKey, attributes.getNamedItem("expect").getNodeValue());

                                        // the domain sequence
                                        iRawPeptideMap.put("domainseq_" + domainKey, attributes.getNamedItem("seq").getNodeValue());

                                        int modCounter = 0;
                                        NodeList peptideChildNodes = peptideNode.getChildNodes();
                                        int childNodesLength = peptideChildNodes.getLength();

                                        for (int n = 0; n < childNodesLength; n++) {

                                            Node pepChild = peptideChildNodes.item(n);

                                            // Get the specific modifications (aa)
                                            if ("aa".equalsIgnoreCase(pepChild.getNodeName())) {
                                                modCounter++;

                                                modificationMap = pepChild.getAttributes();
                                                modificationName = modificationMap.getNamedItem("type").getNodeValue();

                                                // use the old calculation with domainStart!
                                                //modificationMap.getNamedItem("at").getNodeValue()) - domainStart + 1)
                                                iRawModMap.put("at_" + domainKey + "_m" + modCounter, modificationMap.getNamedItem("at").getNodeValue());

                                                // modified is the residue mass change caused by the modification
                                                modificationMass = Double.parseDouble(modificationMap.getNamedItem("modified").getNodeValue());
                                                iRawModMap.put("modified_" + domainKey + "_m" + modCounter, modificationMap.getNamedItem("modified").getNodeValue());

                                                modificationName = modificationMass + "@" + modificationName;

                                                // type is the single letter abbreviation for the modified residue
                                                iRawModMap.put("name_" + domainKey + "_m" + modCounter, modificationName);

                                                // get the substituted amino acid (if any)
                                                if (modificationMap.getNamedItem("pm") != null) {
                                                    iRawModMap.put("pm_" + domainKey + "_m" + modCounter, modificationMap.getNamedItem("pm").getNodeValue());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                
                    // Go to the group node inside the other group node (support)
                    case "group":
                        // Start parsing the support data part (GAML histograms)
                        if (!skipDetails && "supporting data".equalsIgnoreCase(idNodesAttributes.getNamedItem("label").getNodeValue())) {
                            supportDataNodes = idNode.getChildNodes();

                            // Iterate over all the support data nodes
                            int supportDataNodesLength = supportDataNodes.getLength();
                            for (int a = 0; a < supportDataNodesLength; a++) {
                                Node supportDataNode = supportDataNodes.item(a);
                                NamedNodeMap supportDataAttributes = supportDataNode.getAttributes();


                                if ("GAML:trace".equalsIgnoreCase(supportDataNode.getNodeName())) {
                                    switch (supportDataAttributes.getNamedItem("type").getNodeValue().toLowerCase()){
                                        // Parse the hyperscore expectation function values
                                        case "hyperscore expectation function":
                                            iSupportDataMap.put("HYPERLABEL" + "_s" + spectraCounter,
                                                    supportDataAttributes.getNamedItem("label").getNodeValue());

                                            hyperNodes = supportDataNode.getChildNodes();
                                            // Iterate over the hyperscore nodes
                                            int hyperNodesLength = hyperNodes.getLength();
                                            for (int b = 0; b < hyperNodesLength; b++) {
                                                Node hyperNode = hyperNodes.item(b);

                                                String xyData = "";
                                                switch (hyperNode.getNodeName().toLowerCase()) {
                                                    case "gaml:attribute":
                                                        // Get the a0 value
                                                        switch (hyperNode.getAttributes().getNamedItem("type").getNodeValue().toLowerCase()) {
                                                            case "a0":  iSupportDataMap.put("HYPER_A0_s" + spectraCounter, hyperNode.getTextContent()); break;
                                                            case "a1": iSupportDataMap.put("HYPER_A1_s" + spectraCounter, hyperNode.getTextContent()); break;
                                                            default: break;
                                                        }
                                                        break;
                                                    case "gaml:xdata": xyData = "XVAL_HYPER_s" + spectraCounter; break;
                                                    case "gaml:ydata": xyData = "YVAL_HYPER_s" + spectraCounter; break;
                                                    default: break;
                                                }



                                                // Get the Xdata or Ydata
                                                if (xyData.length() > 0) {
                                                    NodeList dataNodes = hyperNodes.item(b).getChildNodes();
                                                    int dataNodesLength = dataNodes.getLength();
                                                    for (int d = 0; d < dataNodesLength; d++) {
                                                        Node dataNode = dataNodes.item(d);
                                                        if ("GAML:values".equalsIgnoreCase(dataNode.getNodeName())) iSupportDataMap.put(xyData, dataNode.getTextContent());
                                                    }
                                                }
                                            }
                                            break;

                                        // Parse the convolution survival funtion values
                                        case"convolution survival function":
                                            iSupportDataMap.put("CONVOLLABEL_s" + spectraCounter, supportDataAttributes.getNamedItem("label").getNodeValue());
                                            supportDataNodes.item(a).getChildNodes();

                                            convolNodes = supportDataNode.getChildNodes();
                                            // Iterate over the convolution nodes
                                            int convolNodesLength = convolNodes.getLength();
                                            for (int b = 0; b < convolNodesLength; b++) {
                                                Node convolNode = convolNodes.item(b);

                                                // Get the Xdata or Ydata                                            
                                                String xyData = "";
                                                switch(convolNode.getNodeName().toLowerCase()){
                                                    case "gaml:xdata": xyData = "XVAL_CONVOL_s" + spectraCounter; break;
                                                    case "gaml:ydata": xyData = "YVAL_CONVOL_s" + spectraCounter; break;
                                                    default: break;
                                                }

                                                if (xyData.length() > 0) {
                                                    NodeList dataNodes = convolNodes.item(b).getChildNodes();

                                                    int dataNodesLength = dataNodes.getLength();
                                                    for (int d = 0; d < dataNodesLength; d++) {
                                                        Node dataNode = dataNodes.item(d);
                                                        if ("GAML:values".equalsIgnoreCase(dataNode.getNodeName())) iSupportDataMap.put(xyData, dataNode.getTextContent());
                                                    }
                                                }
                                            }
                                            break;

                                        default: break;
                                    }




                                    if ("b ion histogram".equalsIgnoreCase(supportDataAttributes.getNamedItem("type").getNodeValue())) {
                                        if (aIonFlag) iSupportDataMap.put("A_IONLABEL_s" + spectraCounter, supportDataAttributes.getNamedItem("label").getNodeValue());
                                        if (bIonFlag) iSupportDataMap.put("B_IONLABEL_s" + spectraCounter, supportDataAttributes.getNamedItem("label").getNodeValue());
                                        if (cIonFlag) iSupportDataMap.put("C_IONLABEL_s" + spectraCounter, supportDataAttributes.getNamedItem("label").getNodeValue());

                                        NodeList ionNodes = supportDataNode.getChildNodes();

                                        // Iterate over the a ion nodes
                                        int ionNodesLength = ionNodes.getLength();
                                        for (int b = 0; b < ionNodesLength; b++) {
                                            // Get the Xdata
                                            Node ionNode = ionNodes.item(b);
                                            if ("GAML:Xdata".equalsIgnoreCase(ionNode.getNodeName())) {
                                                xDataNodes = ionNode.getChildNodes();
                                                int xDataNodesLength = xDataNodes.getLength();
                                                for (int d = 0; d < xDataNodesLength; d++) {
                                                    Node xDataNode = xDataNodes.item(d);
                                                    if ("GAML:values".equalsIgnoreCase(xDataNode.getNodeName())) {
                                                        if (aIonFlag) iSupportDataMap.put("XVAL_AIONS_s" + spectraCounter, xDataNode.getTextContent());
                                                        if (bIonFlag) iSupportDataMap.put("XVAL_BIONS_s" + spectraCounter, xDataNode.getTextContent());
                                                        if (cIonFlag) iSupportDataMap.put("XVAL_CIONS_s" + spectraCounter, xDataNode.getTextContent());
                                                    }
                                                }
                                            }
                                            // Get the Ydata
                                            else if ("GAML:Ydata".equalsIgnoreCase(ionNode.getNodeName())) {
                                                yDataNodes = ionNode.getChildNodes();
                                                int yDataNodesLength = yDataNodes.getLength();
                                                for (int e = 0; e < yDataNodesLength; e++) {
                                                    Node yDataNode = yDataNodes.item(e);
                                                    if ("GAML:values".equalsIgnoreCase(yDataNode.getNodeName())) {
                                                        if (aIonFlag) iSupportDataMap.put("YVAL_AIONS_s" + spectraCounter, yDataNode.getTextContent());
                                                        if (bIonFlag) iSupportDataMap.put("YVAL_BIONS_s" + spectraCounter, yDataNode.getTextContent());
                                                        if (cIonFlag) iSupportDataMap.put("YVAL_CIONS_s" + spectraCounter, yDataNode.getTextContent());
                                                    }
                                                }
                                            }
                                        }
                                    }


                                    else if ("b ion histogram".equalsIgnoreCase(supportDataAttributes.getNamedItem("type").getNodeValue())) {
                                        if (xIonFlag) iSupportDataMap.put("X_IONLABEL_s" + spectraCounter, supportDataAttributes.getNamedItem("label").getNodeValue());
                                        if (yIonFlag) iSupportDataMap.put("Y_IONLABEL_s" + spectraCounter, supportDataAttributes.getNamedItem("label").getNodeValue());
                                        if (zIonFlag) iSupportDataMap.put("Z_IONLABEL_s" + spectraCounter, supportDataAttributes.getNamedItem("label").getNodeValue());

                                        NodeList ionNodes = supportDataNode.getChildNodes();

                                        // Iterate over the a ion nodes
                                        int ionNodesLength = ionNodes.getLength();
                                        for (int b = 0; b < ionNodesLength; b++) {
                                            // Get the Xdata
                                            Node ionNode = ionNodes.item(b);
                                            if ("GAML:Xdata".equalsIgnoreCase(ionNode.getNodeName())) {
                                                xDataNodes = ionNode.getChildNodes();
                                                int xDataNodesLength = xDataNodes.getLength();
                                                for (int d = 0; d < xDataNodesLength; d++) {
                                                    Node xDataNode = xDataNodes.item(d);
                                                    if ("GAML:values".equalsIgnoreCase(xDataNode.getNodeName())) {
                                                        if (xIonFlag) iSupportDataMap.put("XVAL_XIONS_s" + spectraCounter, xDataNode.getTextContent());
                                                        if (yIonFlag) iSupportDataMap.put("XVAL_YIONS_s" + spectraCounter, xDataNode.getTextContent());
                                                        if (zIonFlag) iSupportDataMap.put("XVAL_ZIONS_s" + spectraCounter, xDataNode.getTextContent());
                                                    }
                                                }
                                            }
                                            // Get the Ydata
                                            else if ("GAML:Ydata".equalsIgnoreCase(ionNode.getNodeName())) {
                                                yDataNodes = ionNode.getChildNodes();
                                                int yDataNodesLength = yDataNodes.getLength();
                                                for (int e = 0; e < yDataNodesLength; e++) {
                                                    Node yDataNode = yDataNodes.item(e);
                                                    if ("GAML:values".equalsIgnoreCase(yDataNode.getNodeName())) {
                                                        if (xIonFlag) iSupportDataMap.put("YVAL_XIONS_s" + spectraCounter, yDataNode.getTextContent());
                                                        if (yIonFlag) iSupportDataMap.put("YVAL_YIONS_s" + spectraCounter, yDataNode.getTextContent());
                                                        if (zIonFlag) iSupportDataMap.put("YVAL_ZIONS_s" + spectraCounter, yDataNode.getTextContent());
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }

                        if ("fragment ion mass spectrum".equalsIgnoreCase(idNodesAttributes.getNamedItem("label").getNodeValue())) {

                            supportDataNodes = idNode.getChildNodes();
                            // Iterate over all the support data nodes
                            int supportDataNodesLength = supportDataNodes.getLength();
                            for (int a = 0; a < supportDataNodesLength; a++) {
                                Node supportDataNode = supportDataNodes.item(a);
                                if ("note".equalsIgnoreCase(supportDataNode.getNodeName())) {
                                    String title = supportDataNode.getTextContent().trim();
                                    idToSpectrumMap.put(spectraCounter, title);
                                    if (!skipDetails) {
                                        iSupportDataMap.put("FRAGIONSPECDESC_s" + spectraCounter, title);
                                        iTitle2SpectrumIDMap.put(title, spectraCounter);
                                    } else {
                                        break;
                                    }
                                }
                                if (!skipDetails && "GAML:trace".equalsIgnoreCase(supportDataNode.getNodeName()) && "tandem mass spectrum".equalsIgnoreCase(supportDataNode.getAttributes().getNamedItem("type").getNodeValue())) {
                                    iSupportDataMap.put("SPECTRUMLABEL_s" + spectraCounter, supportDataNode.getAttributes().getNamedItem("label").getNodeValue());

                                    fragIonNodes = supportDataNode.getChildNodes();
                                    // Iterate over the fragment ion nodes
                                    int fragIonNodesLength = fragIonNodes.getLength();
                                    for (int b = 0; b < fragIonNodesLength; b++) {
                                        Node fragIonNode = fragIonNodes.item(b);

                                        String xyData = "";
                                        switch (fragIonNode.getNodeName().toLowerCase()){
                                            case "gaml:attribute":
                                                // Get the a0 value
                                                switch (fragIonNode.getAttributes().getNamedItem("type").getNodeValue().toLowerCase()){
                                                    case "m+h": iSupportDataMap.put("FRAGIONMZ_s" + spectraCounter, fragIonNode.getTextContent()); break;
                                                    case "charge": iSupportDataMap.put("FRAGIONCHARGE_s" + spectraCounter, fragIonNode.getTextContent()); break;
                                                    default: break;
                                                }
                                                break;
                                            case "gaml:xdata": xyData = "XVAL_FRAGIONMZ_s" + spectraCounter; break;
                                            case "gaml:ydata": xyData = "YVAL_FRAGIONMZ_s" + spectraCounter; break;
                                            default: break;
                                        }

                                        if (xyData.length() > 0){
                                            NodeList dataNodes = fragIonNode.getChildNodes();
                                            int dataNodesLength = dataNodes.getLength();
                                            for (int d = 0; d < dataNodesLength; d++) {
                                                Node dataNode = dataNodes.item(d);
                                                if (dataNode.getNodeName().equalsIgnoreCase("GAML:values")) {
                                                    iSupportDataMap.put(xyData, dataNode.getTextContent());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    default: break;
                }
            }
        }
    }
    */

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
