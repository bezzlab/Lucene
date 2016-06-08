/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.qmul.lucene.util.test;


import java.io.FileReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
/**
 *
 * @author sureshhewapathirana
 */
public class StAXParserTestEdit {
    
          final static List<String> selectedTagList = Arrays.asList(
                "DBSequence",
                "ProteinAmbiguityGroup",
                "SpectrumIdentificationItem",
                "PeptideEvidence",
                "Peptide",
                "EnzymeName"
        );
    
    public static void main(String[] args) throws Exception {
    String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                 "<MzIdentML id=\"MS-GF+\">\n" +
                 "    <SequenceCollection xmlns=\"http://psidev.info/psi/pi/mzIdentML/1.1\">\n" +
                 "        <DBSequence length=\"146\" id=\"DBSeq143\">\n" +
                 "            <cvParam cvRef=\"PSI-MS\" accession=\"MS:1001088\"></cvParam>\n" +
                 "        </DBSequence>\n" +
                 "        <Peptide id=\"Pep7\">\n" +
                 "            <PeptideSequence>MFLSFPTTK</PeptideSequence>\n" +
                 "            <Modification location=\"1\" monoisotopicMassDelta=\"15.994915\">\n" +
                 "                <cvParam cvRef=\"UNIMOD\" accession=\"UNIMOD:35\" name=\"Oxidation\"></cvParam>\n" +
                 "            </Modification>\n" +
                 "        </Peptide>\n" +
                 "        <PeptideEvidence dBSequence_ref=\"DBSeq143\" id=\"PepEv_160_1_18\"></PeptideEvidence>\n" +
                 "        <PeptideEvidence dBSequence_ref=\"DBSeq143\" id=\"PepEv_275_8_133\"></PeptideEvidence>\n" +
                 "    </SequenceCollection>\n" +
                 "</MzIdentML>";
    

    XMLStreamReader reader = XMLInputFactory.newFactory().createXMLStreamReader(new StringReader(xml));
    try {
        reader.nextTag();
        search(reader);
    } finally {
        reader.close();
    }
}
private static void search(XMLStreamReader reader) throws XMLStreamException {
    // reader must be on START_ELEMENT upon entry, and will be on matching END_ELEMENT on return
    assert reader.getEventType() == XMLStreamConstants.START_ELEMENT;
    while (reader.nextTag() == XMLStreamConstants.START_ELEMENT) {
        String name = reader.getLocalName();
        if(selectedTagList.contains(name)){
                Map<String, String> props = new LinkedHashMap<>();
                collectProps(reader, props);
                System.out.println(name + ": " + props);
        }else{
                search(reader);
        }
    }
}
private static void collectProps(XMLStreamReader reader, Map<String, String> props) throws XMLStreamException {
    // reader must be on START_ELEMENT upon entry, and will be on matching END_ELEMENT on return
    assert reader.getEventType() == XMLStreamConstants.START_ELEMENT;
    for (int i = 0; i < reader.getAttributeCount(); i++)
        props.put(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
    String name = reader.getLocalName();
    switch (name) {
        case "PeptideSequence":
            props.put(name, reader.getElementText());
            break;
        default:
            while (reader.nextTag() == XMLStreamConstants.START_ELEMENT)
                collectProps(reader, props);
    }
}
    
}
