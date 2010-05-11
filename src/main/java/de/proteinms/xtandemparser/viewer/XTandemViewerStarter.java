package de.proteinms.xtandemparser.viewer;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.SkyKrupp;
import java.io.*;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * This bootstrap class is used to start the jar file with memory options
 * extracted from the JavaOptions file in the Properties folder.
 *
 * @author Thilo Muth
 */
public class XTandemViewerStarter {

    /**
     * The name of the xtandem parser jar file.
     */
    private String filename = "xtandem-parser-" + XTandemViewer.VERSION + ".jar";
    /**
     * If set to true debug output will be written to the screen.
     */
    private boolean debug = false;

    /**
     * Starts the launcher by calling the launch method.
     */
    public XTandemViewerStarter() {

        // set the look and feel
        try {
            PlasticLookAndFeel.setPlasticTheme(new SkyKrupp());
            UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            // ignore exception
        }

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

        String path = this.getClass().getResource("XTandemViewerStarter.class").getPath();
        path = path.substring(5, path.indexOf(filename));
        path = path.replace("%20", " ");

        File javaOptions = new File(path + "Properties/JavaOptions.txt");

        String options = "", currentOption;

        if (javaOptions.exists()) {

            try {
                FileReader f = new FileReader(javaOptions);
                BufferedReader b = new BufferedReader(f);

                currentOption = b.readLine();

                while (currentOption != null) {
                    if (!currentOption.startsWith("#")) {
                        options += currentOption + " ";
                    }
                    currentOption = b.readLine();
                }

                b.close();
                f.close();

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            options = "-Xms256M -Xmx1024M";
        }

        File file = new File(path);

        String javaHome = System.getProperty("java.home") + File.separator + "bin" + File.separator;

        String quote = "";

        if(System.getProperty("os.name").lastIndexOf("Windows") != -1){
            quote = "\"";
        }

        String cmdLine = javaHome + "java " + options + " -cp " +  quote
                + new File(file, filename).getAbsolutePath() + quote
                + " de.proteinms.xtandemparser.viewer.FileSelector";

        try {
            Process p = Runtime.getRuntime().exec(cmdLine);

            InputStream stderr = p.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;

            String errorMessage = "<ERROR>\n\n";

            if (debug) {
                System.out.println("<ERROR>");
            }

            line = br.readLine();

            boolean error = false;

            while (line != null) {

                if (debug) {
                    System.out.println(line);
                }

                errorMessage += line + "\n";
                line = br.readLine();
                error = true;
            }

            if (debug) {
                System.out.println("</ERROR>");
            }

            errorMessage += "\nThe command line executed:\n";
            errorMessage += cmdLine + "\n";
            errorMessage += "\n</ERROR>\n";
            int exitVal = p.waitFor();

            if (debug) {
                System.out.println("Process exitValue: " + exitVal);
            }

            if (error) {

                File logFile = new File(System.getProperty("user.home") +
                        File.separator + "xtandem_viewer.log");

                FileWriter f = new FileWriter(logFile);
                f.write(errorMessage);
                f.close();

                javax.swing.JOptionPane.showMessageDialog(null,
                        "Failed to start XTandemViewer.\n\n" +
                        "Make sure that XTandemViewer is installed in a path not containing special\n" +
                        "characters. On Linux it has to be run from a path without spaces.\n\n" +
                        "The upper memory limit used may be too high for your computer to handle.\n" +
                        "Try reducing it (see Properties\\JavaOptions.txt) and see if this helps.\n\n" +
                        "For more details see:\n" +
                        System.getProperty("user.home") +
                        File.separator + "xtandem_viewer.log",
                        "XTandemViewer - Startup Failed", JOptionPane.OK_OPTION);

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
