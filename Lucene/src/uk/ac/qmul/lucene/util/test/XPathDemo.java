/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.qmul.lucene.util.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import javax.xml.xpath.*;
import org.xml.sax.InputSource;

public class XPathDemo {

   

    public static void main(String[] args) throws Exception {
      
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xPath = xpf.newXPath();
        
        File file = new File("/Users/sureshhewapathirana/Dropbox/TestData/1/ExampleMod.mzid");
        InputStream in = new FileInputStream(file);
        InputSource inputSource = new InputSource(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
"\n" +
"<bookstore>\n" +
"\n" +
"<book>\n" +
"  <title lang=\"en\">Harry Potter</title>\n" +
"  <price>29.99</price>\n" +
"</book>\n" +
"\n" +
"<book>\n" +
"  <title lang=\"en\">Learning XML</title>\n" +
"  <price>39.95</price>\n" +
"</book>\n" +
"\n" +
"</bookstore>"));
      
        String result = (String) xPath.evaluate("//book", inputSource, XPathConstants.STRING);
        System.out.println(result);
    }

}