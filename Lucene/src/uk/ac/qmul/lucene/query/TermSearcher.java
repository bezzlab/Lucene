/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.qmul.lucene.query;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.surround.parser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import uk.ac.qmul.lucene.util.test.ArrayLocation;
import uk.ac.qmul.lucene.util.test.Paginator;

public class TermSearcher {

    Paginator paginator;
    final int pageNumber = 1;
    final int pageSize = 20;
    IndexSearcher is;

    public static void main(String[] args) throws IllegalArgumentException,
            IOException, ParseException, org.apache.lucene.queryParser.ParseException {
        TermSearcher ts = new TermSearcher();
        String indexDir = "/Users/sureshhewapathirana/Desktop/ExampleModIndex/";
        String field = "TagType";
        String queryString = "SpectrumIdentificationItem";

        ts.search(indexDir, queryString, field);
    }

    public void search(String indexDir, String queryString, String field)
            throws IOException, ParseException, org.apache.lucene.queryParser.ParseException {

        Paginator paginator = new Paginator();

        Directory dir = FSDirectory.open(new File(indexDir));
        System.out.println("dir:" + dir.toString());

        is = new IndexSearcher(dir);
        System.out.println("is:" + is.toString());
        System.out.println("is:" + is.getIndexReader().getFieldNames(IndexReader.FieldOption.ALL));

        Term t = new Term(field, queryString);
        Query query = new TermQuery(t);

        long start = System.currentTimeMillis();
        TopDocs hits = is.search(query, 10);
        long end = System.currentTimeMillis();

        System.out.println("Found " + hits.totalHits
                + " document(s) (in " + (end - start)
                + " milliseconds) that matched query '"
                + queryString + "':");

        System.out.println("Docs:" + hits.scoreDocs.length);

        ArrayLocation arrayLocation = paginator.calculateArrayLocation(hits.scoreDocs.length, pageNumber, pageSize);

        for (int i = arrayLocation.getStart() - 1; i < arrayLocation.getEnd(); i++) {
            int docId = hits.scoreDocs[i].doc;

            //load the document
            Document doc = is.doc(docId);

            System.out.println("------------------------------");
            System.out.println(doc.get("length"));
            System.out.println(doc.get("searchDatabase_ref"));
            System.out.println(doc.get("accession"));
            System.out.println(doc.get("id"));
            System.out.println(doc.get("ID"));
            System.out.println(doc.get("cvRef"));
            System.out.println(doc.get("accession"));
            System.out.println(doc.get("name"));
            System.out.println(doc.get("value"));
            System.out.println(doc.get("DocumentType"));
            System.out.println("----------------------------------");
            
            System.out.println("------------------------------");
//            
            System.out.println(doc.get("length"));
            System.out.println(doc.get("searchDatabase_ref"));
            System.out.println(doc.get("accession"));
            System.out.println(doc.get("id"));
            System.out.println(doc.get("ID"));
            System.out.println(doc.get("cvRef"));
            System.out.println(doc.get("accession"));
            System.out.println(doc.get("name"));
            System.out.println(doc.get("value"));
            System.out.println(doc.get("DocumentType"));
            System.out.println("----------------------------------");
        }
        is.close();
    }
}
