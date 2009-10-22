package interfaces;
/**
 * This class represents the Peak interface.
 * @author Thilo Muth
 *
 */
public interface Peak {
	/**
     * This method returns the m/z.
     * @return double
     */
    public double getMZ();

    /**
     * This method returns the intensity.
     * @return double
     */
    public double getIntensity();

    /**
     * Returns charge information for this peak.
     * @return int
     */
    public int getCharge();
}
