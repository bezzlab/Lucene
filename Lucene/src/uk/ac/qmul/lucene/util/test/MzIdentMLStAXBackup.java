///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package uk.ac.qmul.lucene.util.test;
//
//import uk.ac.qmul.lucene.parse.*;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import javax.xml.stream.XMLEventReader;
//import javax.xml.stream.XMLInputFactory;
//import javax.xml.stream.XMLStreamConstants;
//import javax.xml.stream.XMLStreamException;
//import javax.xml.stream.events.Attribute;
//import javax.xml.stream.events.Characters;
//import javax.xml.stream.events.EndElement;
//import javax.xml.stream.events.StartElement;
//import javax.xml.stream.events.XMLEvent;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Field;
//import org.apache.lucene.util.Version;
//import uk.ac.ebi.jmzidml.model.mzidml.DBSequence;
//import uk.ac.qmul.lucene.util.AnalyzerUtils;
//
///**
// *
// * <pre>
// *      <Peptide>                    START_ELEMENT
// *          RKDDHDKEM                CHARACTERS
// *      </Peptide>                   END_ELEMENT
// * </pre>
// *
// * @author Suresh Hewapathirana
// */
//public class MzIdentMLStAXBackup {
//
//    // XML reading objects
//    FileReader file;
//    XMLInputFactory factory;
//    XMLEventReader eventReader;
//    List<DBSequence> DBSequenceList = new ArrayList<>();
//    
//
//    // a flag to identify whether a peptide tag was found or not   
//    boolean isPeptide = false;
//
//    // Lucene Document to collect all the fields along with their values
//    Document document;
//    HashMap<String, Document> documents;
//    final List<String> selectedTagList;
//
//    public MzIdentMLStAXBackup(String filepath) {
//
//        this.selectedTagList = Arrays.asList(
//                "DBSequence",
//                "ProteinAmbiguityGroup",
//                "SpectrumIdentificationItem",
//                "PeptideEvidence",
//                "Peptide",
//                "EnzymeName"
//        );
//
//        try {
//            // creating a file reader from the file path
//            file = new FileReader(filepath);
//
//            // first, initialise XMLInputFactory object
//            factory = XMLInputFactory.newInstance();
//
//            // then, creating an event reader (stax pull parsing - event driven)
//            this.eventReader = factory.createXMLEventReader(file);
//
//        } catch (XMLStreamException | FileNotFoundException ex) {
//            Logger.getLogger(MzIdentMLStAXBackup.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public Document getDocument(String filepath) {
//
//        try {
//            this.read();
//
//        } catch (IOException ex) {
//            Logger.getLogger(MzIdentMLStAXBackup.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        // everything done
//        return document;
//    }
//
//    private void read() throws IOException {
//
//        try {
//            // if there are events
//            while (eventReader.hasNext()) {
//
//                // get the next event
//                XMLEvent event = eventReader.nextEvent();
//
//                // take actions according to their event type
//                switch (event.getEventType()) {
//
//                    case XMLStreamConstants.START_DOCUMENT:
//                        // initialize Document object here
//                        documents = new HashMap<>();
//                        document = new Document();
//                        break;
//
//                    case XMLStreamConstants.START_ELEMENT:
//
//                        StartElement startElement = event.asStartElement();
//                        String tagname = startElement.getName().getLocalPart();
//
//                        if ((!documents.containsKey(tagname)) && (selectedTagList.contains(tagname))) {
//                            System.out.println("New docunemt should be created called : " + tagname);
//                            documents.put(tagname, document);
//                        }
//                        startElementAction(eventReader,event,startElement);
//                        break;
//
//                    case XMLStreamConstants.CHARACTERS:
//
//                        Characters characters = event.asCharacters();
//                        characterEventAction(characters);
//                        break;
//
//                    case XMLStreamConstants.END_ELEMENT:
//
//                        EndElement endElement = event.asEndElement();
//                        endElementAction(endElement);
//                        break;
//                }
//            }
//        } catch (XMLStreamException e) {
//            System.err.println("XML Parsing Error: " + e.getMessage());
//        }
//    }
//
//    private void startElementAction(XMLEventReader eventReader,XMLEvent event, StartElement startElement) throws XMLStreamException, IOException {
//
//        String tagName = startElement.getName().getLocalPart();
//        
//        String fieldValue;
//        String tagValue = "";
//
//        
//        if (tagName.equals("DBSequence")) {
//           
//            DBSequenceParser.parse(eventReader, event, startElement);
//
////            //  System.out.println("Start Element :" + tagName);
////            // look for attributes of the current tag
////            Iterator<Attribute> attributes = startElement.getAttributes();
////
////            // travel through each attribute
////            while (attributes.hasNext()) {
////
////                // get the next attribute
////                Attribute attribute = attributes.next();
////                System.out.println("Document :" + tagName + " Field:" + attribute.getName() + "=>" + attribute.getValue());
////            } 
////            
////            // lets add tagName as field and tagValue as value
////            //System.out.println("Field:" + tagName + "=>" + tagValue);
////            //document.add(new Field(tagName, tagValue, Field.Store.YES, Field.Index.NOT_ANALYZED));
////
////            // lets visually see how tagValue is broken into tokens. 
////            // lets use StandardAnalyzer for this. Display mode can be either "TokensOnly"" or "TokensFullDetails"
////            AnalyzerUtils.displayTokens(new StandardAnalyzer(Version.LUCENE_30), tagValue, "TokensOnly");
//
//        } 
//    }
//
//    private void characterEventAction(Characters characters) {
//
//        if (isPeptide) {
////             System.out.println("PeptideSequence => " + characters.getData());
//            isPeptide = false;
//        }
//    }
//
//    private void endElementAction(EndElement endElement) {
//
//        String tagName = endElement.getName().getLocalPart();
//
//        if (selectedTagList.contains(tagName)) {
//            System.out.println("\n-----------------------------------------------");
//        }
//    }
//
//}
