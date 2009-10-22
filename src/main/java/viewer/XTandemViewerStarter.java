package viewer;

import java.io.*;
import javax.swing.JOptionPane;

/**
 * This bootstrap class is used to start the jar file special memory options.
 *
 * @author  Thilo Muth
 */
public class XTandemViewerStarter {   
    /**
     * The name of the xtandem parser jar file.
     */
    private String filename = "xtandemparser-" + XTandemViewer.VERSION.substring(3) + ".jar";

    /**
     * Starts the launcher by calling the launch method.
     */
    public XTandemViewerStarter() {
        try {
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the file by launching the jar file on the JVM.
     *
     * @throws java.lang.Exception
     */
    private void start() throws Exception {
        String cmdLine, path, errorString = "";

        path = this.getClass().getResource("XTandemViewerStarter.class").getPath();
        path = path.substring(5, path.indexOf(filename));
        path = path.replace("%20", " ");

        String memoryOptions = "-Xms128M -Xmx1024M";

        File file = new File(path);

        String javaHome = System.getProperty("java.home") + File.separator +
                "bin" + File.separator;

        cmdLine = javaHome + "java " + memoryOptions + " -cp "
                + new File(file, filename).getAbsolutePath()
                + " viewer.FileSelector";

        try {
            Process p = Runtime.getRuntime().exec(cmdLine);
            InputStream stderr = p.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            line = br.readLine();
            boolean errorFlag = false;

            while (line != null) {

                errorString += line + "\n";
                line = br.readLine();
                errorFlag = true;
            }

            errorString += cmdLine + "\n";

            if (errorFlag) {                
                javax.swing.JOptionPane.showMessageDialog(null,
                        "Failed to start XTandem Viewer.\n\n" + errorString,
                        "XTandemViewer Viewer - Error!", JOptionPane.OK_OPTION);

                System.exit(0);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Starts the main application. That's the main method for the jar file.
     *
     * @param args
     */
    public static void main(String[] args) {
        new XTandemViewerStarter();
    }
}
