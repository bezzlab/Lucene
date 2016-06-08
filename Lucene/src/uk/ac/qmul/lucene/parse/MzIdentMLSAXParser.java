/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.qmul.lucene.parse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.lucene.ant.DocumentHandlerException;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MzIdentMLSAXParser extends DefaultHandler {

//    private StringBuilder elementBuffer = new StringBuilder();
//    private Map<String, String> attributeMap = new HashMap<String, String>();
    private Document doc;

    private boolean isDBsequence = false;
    private boolean isPeptideEvidence = false;

    Document DBSequenceDocument;
    Document PeptideEvidenceDocument;

    public Document getDocument(String dataDir)
            throws DocumentHandlerException, FileNotFoundException {

        FileInputStream fis = new FileInputStream(dataDir);
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            SAXParser parser = spf.newSAXParser();
            parser.parse(fis, this);
        } catch (Exception e) {
            throw new DocumentHandlerException("Cannot parse XML document");
        }
        return DBSequenceDocument;
    }

    public void startDocument() {
        doc = new Document();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {

        if (qName.equals("DBSequence")) {
            // each time we found a new DBSequence, we re-initialize document
            DBSequenceDocument = new Document();

            for (int i = 0; i < atts.getLength(); i++) {
                DBSequenceDocument.add(new Field(atts.getQName(i), atts.getValue(i), Field.Store.YES, Field.Index.NOT_ANALYZED));
            }
            DBSequenceDocument.add(new Field("ID", atts.getValue("id"), Field.Store.YES, Field.Index.NOT_ANALYZED));
            DBSequenceDocument.add(new Field("DocumentType", qName, Field.Store.YES, Field.Index.NOT_ANALYZED));
            isDBsequence = true;
        } else if ((qName.equals("cvParam")) && (isDBsequence)) {
            for (int i = 0; i < atts.getLength(); i++) {
                DBSequenceDocument.add(new Field(atts.getQName(i), atts.getValue(i), Field.Store.YES, Field.Index.NOT_ANALYZED));
            }
        } else if (qName.equals("PeptideEvidence")) {
            // each time we found a new DBSequence, we re-initialize document
            PeptideEvidenceDocument = new Document();

            for (int i = 0; i < atts.getLength(); i++) {
                PeptideEvidenceDocument.add(new Field(atts.getQName(i), atts.getValue(i), Field.Store.YES, Field.Index.NOT_ANALYZED));
            }
            DBSequenceDocument.add(new Field("DocumentType", qName, Field.Store.YES, Field.Index.NOT_ANALYZED));
            PeptideEvidenceDocument.add(new Field("ID", atts.getValue("id"), Field.Store.YES, Field.Index.NOT_ANALYZED));

        }
    }

    @Override
    public void characters(char[] text, int start, int length) {
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

//        System.out.println("end: "+ qName);
//        String attributeString = "";
//        for (Map.Entry<String, String> attribute : attributeMap.entrySet()) {
//
//            String attName = attribute.getKey();
//            String attValue = attribute.getValue();
//            
//            attributeString += attName + "=" + attValue + ";";
//            System.out.println(qName + " -> " + attName + " -> " + attValue);
//            
//            doc.add(new Field(attName, attValue, Field.Store.YES,
//                    Field.Index.NOT_ANALYZED));
//        }
//       
//        System.out.println("attributeString:"+ attributeString);
        if (qName.equals("DBSequence")) {

            System.out.println(DBSequenceDocument);
            System.out.println("--------------------------------------------------");
            isDBsequence = false;
        } else if (qName.equals("PeptideEvidence")) {

            System.out.println(PeptideEvidenceDocument);
            System.out.println("--------------------------------------------------");
        }
    }
}
