# XTandem Parser #

  * [News](#News.md)
  * [What is XTandem Parser?](#What_is_XTandemParser?.md)
  * [Libraries](#Libraries.md)
  * [Maven Dependency](#Maven_Dependency.md)
  * [Converting XTandem Files to PRIDE XML](#Converting_XTandem_Files_to_PRIDE_XML.md)
  * [Result Analysis](#Result_Analysis.md)
  * [Screenshots](#Screenshots.md)

**XTandem Parser Publications:**
  * [Muth et al: Proteomics 2010 Apr;10(7):1522-4](http://www.ncbi.nlm.nih.gov/pubmed/20140905).
  * If you use **XTandem Parser** as part of a paper, please include the reference above.

**SearchGUI and PeptideShaker:**
  * To run X!Tandem searches we recommend the use of [SearchGUI](http://searchgui.googlecode.com).
  * To visualize and analyze X!Tandem results we recommend the use of [PeptideShaker](http://peptide-shaker.googlecode.com).


---


<table border='0'>
<blockquote><tr>
<blockquote><td width='200'><a href='http://genesis.ugent.be/maven2/de/proteinms/xtandemparser/xtandem-parser/1.7.18/xtandem-parser-1.7.18.zip'><img src='http://xtandem-parser.googlecode.com/svn/wiki/images/download_button.png' /></a></td>
<td width='150'><i>v1.7.18 - All platforms</i></td>
<td width='150'><i><a href='http://code.google.com/p/xtandem-parser/wiki/ReleaseNotes'>ReleaseNotes</a></i></td>
</blockquote></tr>
</table></blockquote>


---


## What is XTandemParser? ##
**XTandem Parser** is a Java project for extracting information from X!Tandem output xml files.  [X!Tandem software](http://www.thegpm.org/tandem/index.html) can match tandem mass spectra with peptide sequences and works as a search engine for protein identification.

The output xml format can be found [here](http://www.thegpm.org/docs/X_series_output_form.pdf) (PDF).

And the input xml format is explained here [here](http://www.thegpm.org/tandem/api/index.html).

If you need an example file to play with, one is provided as a zip file in the downloads section on the right.

The parser is developed by Thilo Muth under the guidance of Marc Vaudel, Prof. Dr. Albert Sickmann and Prof. Dr. Lennart Martens. Great help came from Harald Barsnes who was involved in the development of [OMSSA Parser](http://code.google.com/p/omssa-parser).
Thanks to Steffi Wortelkamp for persistent testing.

[Go to top of page](#XTandem_Parser.md)


---


## Libraries ##
The following libraries were used for the general user interface (spectrum viewer):
  * [SwingX](https://swingx.dev.java.net/)
  * [JGoodies](http://www.jgoodies.com/)
  * [Utilities](http://genesis.ugent.be/utilities/)

[Go to top of page](#XTandem_Parser.md)


---


## Maven Dependency ##
**XTandem Parser** is available for use in Maven projects:

```
<dependency>
    <groupId>de.proteinms.xtandemparser</groupId>
    <artifactId>xtandem-parser</artifactId>
    <version>X.Y.Z</version>
</dependency>

<repository>
    <id>genesis-maven2-repository</id>
    <name>Genesis maven2 repository</name>
    <url>http://genesis.UGent.be/maven2</url>
    <layout>default</layout>
</repository>
```

Update the version number to latest released version.

[Go to top of page](#XTandem_Parser.md)


---


## Converting XTandem Files to PRIDE XML ##
If you want to convert your XTandem file into PRIDE XML for submission to the online [PRIDE](http://www.ebi.ac.uk/pride/) repository, please check out the [PRIDE Converter](http://code.google.com/p/pride-converter/).

[Go to top of page](#XTandem_Parser.md)


---


## Result Analysis ##

To visualize and analyze X!Tandem results we recommend the use of [PeptideShaker](http://peptide-shaker.googlecode.com). **PeptideShaker** is a search engine independent platform for visualization of peptide and protein identification results from multiple search engines.

[Go to top of page](#XTandem_Parser.md)


---


## Screenshots ##

(Click on the screenshot to see the full size version)

[http://xtandem-parser.googlecode.com/svn/wiki/images/screenshots/xTandemParser\_small.PNG](http://xtandem-parser.googlecode.com/svn/wiki/images/screenshots/xTandemParser.PNG)

[Go to top of page](#XTandem_Parser.md)