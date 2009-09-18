package parser;

import interfaces.Parser;
import interfaces.Peak;
import xtandem.MgfPeak;
import xtandem.MgfPeaklist;
import xtandem.Peptide;
import xtandem.PeptideMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * This class is an implementation of Parser and retrieves a peak list for a mgf file.
 * @author Thilo Muth
 *
 */
public class MgfFileParser implements Parser{

	/**
	 * Holds the mgf file which should be parsed.
	 */
	private File iMgfFile = null;

	/**
	 * Contains the total number of the spectra.
	 */
	private int iSpectraNumber = -1;

	private HashMap<Integer, MgfPeaklist> iPeaklistMap = null;

	private HashMap<String, Integer> iTitle2SpectrumIdMap = null;

	private PeptideMap iPepMap = null;

	/**
	 * The constructor gets the path to the mgf-file.
	 *
	 * @param aMgfFilePath
	 */
	public MgfFileParser(String aMgfFilePath, HashMap<String, Integer> aTitle2SpectrumIdMap, PeptideMap aPepMap) {
		iMgfFile = new File(aMgfFilePath);
		iTitle2SpectrumIdMap = aTitle2SpectrumIdMap;
		iPepMap = aPepMap;
	}

	public String[] getAllSpectraNames(){
		String spectra[] = null;
		ArrayList<String> list = null;
		try{

		BufferedReader br = new BufferedReader(new FileReader(iMgfFile));
		list = new ArrayList<String>();

		String line = null;
		while ((line = br.readLine()) != null) {

            if (line.startsWith("TITLE")) {
				// Retrieve the title.
				String title = line.substring(line.indexOf("=") + 1, line.length());

				if (title != null) {
					title = title.trim();
				}

				list.add(title);

            } else {
            	continue;
            }
		}
		} catch (Exception e){
			e.printStackTrace();
		}
		// Convert it to a string array.
		spectra = new String[list.size()];
		int i = 0;

		for (String string : list) {
			spectra[i++] = string;
		}
        return spectra;
	}
	/**
	 * This method returns a mgf peak list map with the spectrum number as key
	 * and the peaklist as value.
	 *
	 * @return iPeaklistMap
	 */
	public HashMap getPeakListMap() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(iMgfFile));
			// Get a vector for the peaks.
			ArrayList<MgfPeak> peakVector = new ArrayList();
			MgfPeaklist mgfPeaklist = new MgfPeaklist();
			iPeaklistMap = new HashMap<Integer, MgfPeaklist>();

			// Flag to show if it's inside the spectrum.
			boolean flagSpectrum = false;

			// Initialize the counters.
			int spectraCounter = 0;
			//int lineCounter = 0;
			String line = null;
			while ((line = br.readLine()) != null) {
				//lineCounter++;

				line = line.trim();
				if (line.equals("")) {
					continue;
				}

                /*if (lineCounter == 1 && line.startsWith("CHARGE")) {
                    continue;
                }*/

                if (line.equals("BEGIN IONS")) {
                    flagSpectrum = true;
                    // Empty the mgf peak list.
                    mgfPeaklist = new MgfPeaklist();
                    peakVector = new ArrayList<MgfPeak>();
                    spectraCounter++;
                    continue;
                }

                if(flagSpectrum){
                    if (line.startsWith("TITLE")) {
    					// Retrieve the title.
    					String title = line.substring(line.indexOf("=") + 1, line.length());

    					if (title != null) {
    						title = title.trim();
    					}
    					if(iTitle2SpectrumIdMap.get(title) != null){
    						mgfPeaklist.setIdentfied(true);
    						mgfPeaklist.setIdentifiedSpectrumNumber(iTitle2SpectrumIdMap.get(title));
    					}

    					// Set the title for the peaklist.
    					mgfPeaklist.setTitle(title);
    					continue;
                    } else if (line.startsWith("PEPMASS")) {
    						// Type is MS/MS.
    						mgfPeaklist.setMSType("MS/MS");

    						// Set the peptide mass.
                            String pepMass = line.substring((line.indexOf("=")) + 1).trim();
                            mgfPeaklist.setPepmass(Double.parseDouble(pepMass));
    						continue;
    					} else if (line.startsWith("CHARGE")) {
    						// Type is MS/MS.
    						mgfPeaklist.setMSType("MS/MS");
    						// Set the charge.
    						String charge = line.substring(line.indexOf("=") + 1, line.indexOf("=") + 2);
    						mgfPeaklist.setCharge(charge);
    						continue;
    					} else {
    						// Parse the masses and the intensities
    						try {
    								if (line.equals("")) {
    									continue;
    								}
    								if (!line.equals("END IONS")){
    									//if(!mgfPeaklist.isIdentfied()){
    										double peakMz;
    	    								double peakIntensity;
    	    								int peakCharge;
    	    								// Split on the white spaces.
    	    								String[] entries = line.split("\\s+");
    	    								try {
    	    									// The first entry is the m/z.
    	    									peakMz = Double.parseDouble(entries[0]);

    	    									// The second entry is the intensity.
    	    									peakIntensity = Double.parseDouble(entries[1]);
    	    								} catch (Exception e) {
    	    									return null;
    	    								}

    	    								// Get a mgf peak object.
    	    								MgfPeak mgfPeak = new MgfPeak();

    	    								// Set the peak mz.
    	    								mgfPeak.setPeakMz(peakMz);
    	    								// Set the peak intensity.
    	    								mgfPeak.setPeakIntensity(peakIntensity);

    	    								// Check if a charge is given.
    	    								if (entries.length == 3) {
    	    									// Delete the "+" from the original charge.
    	    									peakCharge = Integer.parseInt(entries[3].replace("+", ""));
    	    									mgfPeak.setPeakCharge(peakCharge);
    	    								}


    	    								// Add the peak to the list.
    	    								peakVector.add(mgfPeak);


    								} else {
    									// Add the peaks to the list.
    				                	flagSpectrum = false;

    				                	if(mgfPeaklist.isIdentfied()){
    				                		ArrayList<Peptide> peptides = iPepMap.getAllPeptides(mgfPeaklist.getIdentifiedSpectrumNumber());
    				                		mgfPeaklist.setIdentifiedPeptides(peptides);
    				                	}
    				                	// Add the peaks to the list.
    				        			mgfPeaklist.setPeaks(peakVector);

    				        			// Fill the peak list map.
    				        			iPeaklistMap.put(spectraCounter, mgfPeaklist);
    				                	continue;
    								}

    							} catch (Exception e) {
    								e.printStackTrace();
    							}
    					}
                }
			}
			// Set the total number of spectra.
			iSpectraNumber = spectraCounter;
			return iPeaklistMap;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * This method returns a peak list for a specific spectrum file.
	 * For example: orbitrapXXXXX.XXXX.XXXX.X.dta as spectrum file string.
	 *
	 * @return iMgfPeaklist
	 */
	public MgfPeaklist getPeaklist(String spectrumTitle) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(iMgfFile));

			ArrayList<Peak> list = new ArrayList<Peak>();
			MgfPeaklist mgfPeaklist = new MgfPeaklist();

			// Flag to show if inside the spectrum.
			int lineCounter = 0;
			String line = null;
			boolean goFlag = false;
			while ((line = br.readLine()) != null) {
				lineCounter++;
				line = line.trim();
				if (line.equals("")) {
					continue;
				}

                if (lineCounter == 1 && line.startsWith("CHARGE")) {
                    continue;
                }

                if (line.startsWith("TITLE")) {
					// Retrieve the title.
					String string = line.substring(line.indexOf("=") + 1, line.length());

					if (string != null) {
						string = string.trim();
					}
					if (string.equals(spectrumTitle)){
						goFlag = true;

						// Set the title for the peaklist.
						mgfPeaklist.setTitle(string);
						continue;

					}
                }

                // Go into it with a spectrum found.
				if(goFlag == true){
					if (line.startsWith("PEPMASS")) {
						// Type is MS/MS.
						mgfPeaklist.setMSType("MS/MS");
						// Set the peptide mass.
					     // PEPMASS line found.
                       String pepMass = line.substring((line.indexOf("=")) + 1).trim();
                       mgfPeaklist.setPepmass(Double.parseDouble(pepMass));
 					}

					if (line.startsWith("CHARGE")) {
						// Type is MS/MS.
						mgfPeaklist.setMSType("MS/MS");
						// Set the charge.
						mgfPeaklist.setCharge(line);
					}

					// Format check if line start with a number.
					if (line.charAt(0) >= '1' && line.charAt(0) <= '9') {
						// Go until the END IONS-Section and parse.
						for (; line.indexOf("END IONS") == -1 && line != null; line = br.readLine()) {
							try {
								if (line.equals("")) {
									continue;
								}
								double peakMz;
								double peakIntensity;
								int peakCharge;
								// Split on the white spaces.
								String[] entries = line.split("\\s+");
								try {
									// The first entry is the m/z.
									peakMz = Double.parseDouble(entries[0]);

									// The second entry is the intensity.
									peakIntensity = Double.parseDouble(entries[1]);
								} catch (Exception e) {
									return null;
								}

								// Get a mgf peak object.
								MgfPeak mgfPeak = new MgfPeak();

								// Set the peak mz.
								mgfPeak.setPeakMz(peakMz);
								// Set the peak intensity.
								mgfPeak.setPeakIntensity(peakIntensity);

								// Check if a charge is given.
								if (entries.length == 3) {
									// Delete the "+" from the original charge.
									peakCharge = Integer.parseInt(entries[3].replace("+", ""));
									mgfPeak.setPeakCharge(peakCharge);
								}

								// Add the peak to the list.
								list.add(mgfPeak);


							} catch (Exception e) {
								e.printStackTrace();
							}
						} break;
				    }
				}
			}
			// Add the peaks to the list.
			//mgfPeaklist.setPeaks(list.toArray(new Peak[0]));
			return mgfPeaklist;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the total number of spectra in the parsed mgf file.
	 * @return iSpectraNumber
	 */
	public int getSpectraNumber() {
		if(iSpectraNumber < 0){
			this.getPeakListMap();
		}
		return iSpectraNumber;
	}
}
