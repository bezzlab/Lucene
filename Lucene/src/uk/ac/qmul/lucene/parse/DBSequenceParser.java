/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.qmul.lucene.parse;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/**
 * <pre>
 * <DBSequence length="146" searchDatabase_ref="SearchDB_1" accession="sp|P14391|HBB_PTEAL" id="DBSeq143">
 *    <cvParam cvRef="PSI-MS"
 *             accession="MS:1001088"
 *             name="protein description"
 *             value="sp|P14391|HBB_PTEAL Hemoglobin subunit beta OS=Pteropus alecto GN=HBB PE=1 SV=1">
 *    </cvParam>
 * </DBSequence>
 * </pre>
 *
 *
 * @author sureshhewapathirana
 */
public class DBSequenceParser {

    public static Document parse(XMLEventReader eventReader, XMLEvent event, StartElement startElement) {

        Document document = new Document();
        String attributesString = "";
        String cvParamsString = "";

        // look for attributes of the current tag
        Iterator<Attribute> attributes = startElement.getAttributes();

        // travel through each attribute
        while (attributes.hasNext()) {

            // get the next attribute
            Attribute attribute = attributes.next();

            attributesString += attribute.getName() + "=" + attribute.getValue() + ";";

        }

        if (eventReader.hasNext()) {
            try {
                event = eventReader.nextEvent();
            } catch (XMLStreamException ex) {
                Logger.getLogger(DBSequenceParser.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (event.asStartElement().getName().getLocalPart().equals("cvParam")) {
                cvParamsString += event.asCharacters().getData();
            }
        }
            System.out.println("attributesString: " + attributesString);
            System.out.println("cvParamsString: " + cvParamsString);
//         document.add(new Field("tag", "DBSequence", Field.Store.YES, Field.Index.NOT_ANALYZED));
//         document.add(new Field(attribute.getName().toString(), attribute.getValue(), Field.Store.YES, Field.Index.NOT_ANALYZED));
//         document.add(new Field(attribute.getName().toString(), attribute.getValue(), Field.Store.YES, Field.Index.NOT_ANALYZED));
//         
            return document;
        }

    }
