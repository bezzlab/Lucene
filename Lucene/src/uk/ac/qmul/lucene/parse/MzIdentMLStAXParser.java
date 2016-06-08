/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.qmul.lucene.parse;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/**
 *
 * <pre>
 *      <Peptide>                    START_ELEMENT
 *          RKDDHDKEM                CHARACTERS
 *      </Peptide>                   END_ELEMENT
 * </pre>
 *
 * @author Suresh Hewapathirana
 */
public class MzIdentMLStAXParser {

    // XML reading objects
    FileReader file;
    XMLInputFactory factory;
    XMLStreamReader streamReader;

    // Lucene Document to collect all the fields along with their values
    List<Document> documents;
    final List<String> selectedTagList;

    public MzIdentMLStAXParser(String filepath) {

        documents = new ArrayList<Document>();
        this.selectedTagList = Arrays.asList(
                "DBSequence",
                "ProteinAmbiguityGroup",
                "SpectrumIdentificationItem",
                "PeptideEvidence",
                "Peptide",
                "EnzymeName"
        );

        try {
            // creating a file reader from the file path
            file = new FileReader(filepath);

            // first, initialise XMLInputFactory object
            factory = XMLInputFactory.newInstance();

            // then, creating an streamReader
            streamReader = factory.createXMLStreamReader(file);

        } catch (XMLStreamException | FileNotFoundException ex) {
            Logger.getLogger(MzIdentMLStAXParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Document> getDocument(String filepath) throws XMLStreamException {

        try {
            streamReader.nextTag();
            read(streamReader);
        } catch (XMLStreamException ex) {
            Logger.getLogger(MzIdentMLStAXParser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            streamReader.close();
        }

        // everything done
        return documents;
    }

    private void read(XMLStreamReader reader) throws XMLStreamException {

        // reader must be on START_ELEMENT upon entry, and will be on matching END_ELEMENT on return
        assert reader.getEventType() == XMLStreamConstants.START_ELEMENT;
        while (reader.nextTag() == XMLStreamConstants.START_ELEMENT) {
            String name = reader.getLocalName();
            if (selectedTagList.contains(name)) {
                Map<String, String> attributes = new LinkedHashMap<>();
                getAttributes(reader, attributes);
                print(name, attributes);
            } else {
                read(reader);
            }
        }
    }

    private void getAttributes(XMLStreamReader reader, Map<String, String> attributes) throws XMLStreamException {

        // reader must be on START_ELEMENT upon entry, and will be on matching END_ELEMENT on return
        assert reader.getEventType() == XMLStreamConstants.START_ELEMENT;
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            attributes.put(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
        }
        String name = reader.getLocalName();
        switch (name) {
            case "PeptideSequence":
                attributes.put(name, reader.getElementText());
                break;
            default:
                while (reader.nextTag() == XMLStreamConstants.START_ELEMENT) {
                    getAttributes(reader, attributes);
                }
        }
    }

    private void print(String name, Map<String, String> attributes) {

        Document document = new Document();
        for (Map.Entry<String, String> kv : attributes.entrySet()) {
            document.add(new Field(kv.getKey(), kv.getValue(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        }
        document.add(new Field("TagType", name, Field.Store.YES, Field.Index.NOT_ANALYZED));

        documents.add(document);
         System.out.println(name);
    }

}
