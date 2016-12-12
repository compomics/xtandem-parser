# XTandem Parser #

  * [Introduction](#introduction)
  * [Libraries](#libraries)
  * [Maven Dependency](#maven-dependency)
  * [Result Analysis](#result-analysis)
  * [Screenshots](#screenshots)

**XTandem Parser Publications:**

  * [Muth et al: Proteomics 2010 Apr;10(7):1522-4](http://www.ncbi.nlm.nih.gov/pubmed/20140905).
  * If you use **XTandem Parser** as part of a paper, please include the reference above.

**SearchGUI and PeptideShaker:**

  * To run X!Tandem searches we recommend the use of [SearchGUI](http://compomics.github.io/projects/searchgui.html).
  * To visualize and analyze X!Tandem results we recommend the use of [PeptideShaker](http://compomics.github.io/projects/peptide-shaker.html).

---

|   |   |   |
| :------------------------- | :---------------: | :--: |
| <a href="http://genesis.ugent.be/maven2/de/proteinms/xtandemparser/xtandem-parser/1.12.0/xtandem-parser-1.12.0.zip" onclick="trackOutboundLink('usage','download','xtandem-parser','http://genesis.ugent.be/maven2/de/proteinms/xtandemparser/xtandem-parser/1.12.0/xtandem-parser-1.12.0.zip'); return false;"><img src="https://github.com/compomics/xtandem-parser/wiki/images/download_button.png" alt="download" /></a> | *v1.12.0 - All platforms* | [ReleaseNotes](https://github.com/compomics/xtandem-parser/wiki/ReleaseNotes) |

---

## Introduction ##
**XTandem Parser** is a Java project for extracting information from X!Tandem output xml files.  [X!Tandem software](http://www.thegpm.org/tandem/index.html) can match tandem mass spectra with peptide sequences and works as a search engine for protein identification.

The output xml format can be found [here](http://www.thegpm.org/docs/X_series_output_form.pdf) (PDF).

And the input xml format is explained here [here](http://www.thegpm.org/tandem/api/index.html).

If you need an example file to play with, one is provided as a zip file in the downloads section on the right.

The parser is developed by Thilo Muth under the guidance of Marc Vaudel, Prof. Dr. Albert Sickmann and Prof. Dr. Lennart Martens. Great help came from Harald Barsnes who was involved in the development of [OMSSA Parser](http://compomics.github.io/projects/omssa-parser.html).
Thanks to Steffi Wortelkamp for persistent testing.

[Go to top of page](#xtandem-parser)

---

## Libraries ##
The following libraries were used for the general user interface (spectrum viewer):
  * [SwingX](https://swingx.dev.java.net/)
  * [JGoodies](http://www.jgoodies.com/)
  * [Utilities](http://compomics.github.io/projects/compomics-utilities.html)

[Go to top of page](#xtandem-parser)

---

## Maven Dependency ##
**XTandem Parser** is available for use in Maven projects:

```
<dependencies>
    <dependency>
        <groupId>de.proteinms.xtandemparser</groupId>
        <artifactId>xtandem-parser</artifactId>
        <version>X.Y.Z</version>
    </dependency>
</dependencies>
```
```
<repositories>
    <!-- Compomics Genesis Maven 2 repository -->
    <repository>
        <id>genesis-maven2-repository</id>
        <name>Genesis maven2 repository</name>
        <url>http://genesis.UGent.be/maven2</url>
        <layout>default</layout>
    </repository>

    <!-- EBI Maven 2 repository -->
    <repository>
        <id>ebi-repo</id>
        <name>The EBI Maven2 repository</name>
        <url>http://www.ebi.ac.uk/~maven/m2repo</url>
        <layout>default</layout>
    </repository>
</repositories>
```

Update the version number to latest released version.

[Go to top of page](#xtandem-parser)

---

## Result Analysis ##

To visualize and analyze X!Tandem results we recommend the use of [PeptideShaker](http://compomics.github.io/projects/peptide-shaker.html). **PeptideShaker** is a search engine independent platform for visualization of peptide and protein identification results from multiple search engines.

[Go to top of page](#xtandem-parser)

---

## Screenshots ##

(Click on the screenshot to see the full size version)

[![](https://github.com/compomics/xtandem-parser/wiki/images/screenshots/xTandemParser_small.PNG)](https://github.com/compomics/xtandem-parser/wiki/images/screenshots/xTandemParser.PNG)

[Go to top of page](#xtandem-parser)
