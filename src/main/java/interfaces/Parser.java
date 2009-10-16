package interfaces;

import java.util.HashMap;


/**
 * Interface for the parser, which has only the method to retrieve the peak list hash map.
 * @author Thilo Muth
 *
 */
public interface Parser {
	/**
     * This method returns the peaklist map.
     * @return HashMap
     */
	public HashMap getPeakListMap();
}
