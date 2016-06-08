/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.qmul.lucene.util.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXParserTest extends DefaultHandler {

    private boolean isCollecting = false;
    final List<String> selectedTagList;
    String currentCopyingSection;

    public static void main(String[] argv) throws SAXException, ParserConfigurationException, IOException {
        long start = System.currentTimeMillis();
       // SAXParserTest ps = new SAXParserTest("/Users/sureshhewapathirana/Dropbox/TestData/1/ExampleMod.mzid");
        SAXParserTest ps = new SAXParserTest("/Users/sureshhewapathirana/Dropbox/TestData/1/ExampleMod.mzid");
          long end = System.currentTimeMillis();
        System.out.println("Parsing took " + (end - start) + " milliseconds");
    }

    public SAXParserTest(String dataDir) throws FileNotFoundException, SAXException, ParserConfigurationException, IOException {

        this.selectedTagList = Arrays.asList(
                "DBSequence",
                "ProteinAmbiguityGroup",
                "SpectrumIdentificationItem",
                "PeptideEvidence",
                "Peptide",
                "EnzymeName"
        );
        FileInputStream fis = new FileInputStream(dataDir);
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser parser = spf.newSAXParser();
        parser.parse(fis, this);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {

        if (selectedTagList.contains(qName)) {
            currentCopyingSection = "";
            isCollecting = true;
        }
        // get attributes each tag 
        for (int i = 0; i < atts.getLength(); i++) {
            currentCopyingSection += atts.getQName(i) + "=" + atts.getValue(i) + ";";
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // do someething
    }
    
    

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

          if (selectedTagList.contains(qName)) {
            isCollecting = false;
            System.out.println(qName + ": " + currentCopyingSection);
            System.out.println("------------------------------------------------------");
        }
    }
}
