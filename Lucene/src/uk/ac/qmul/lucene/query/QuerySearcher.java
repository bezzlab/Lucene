/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.qmul.lucene.query;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.surround.parser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class QuerySearcher {

    public static void main(String[] args) throws IllegalArgumentException,
            IOException, ParseException, org.apache.lucene.queryParser.ParseException {

        String indexDir = "/Users/sureshhewapathirana/Desktop/Galaxy36Index/";
        String field = "id";
        String query = "DBSeq1";        
//String query = "Zane Pasolini";

        search(indexDir, field, query);
    }

    public static void search(String indexDir, String field, String queryString)
            throws IOException, ParseException, org.apache.lucene.queryParser.ParseException {

        Directory dir = FSDirectory.open(new File(indexDir));
        IndexReader reader = IndexReader.open(dir);
         System.out.println("Field Names:" + reader.getFieldNames(IndexReader.FieldOption.ALL));
       
        try (IndexSearcher indexSearcher = new IndexSearcher(reader)) {
           
            QueryParser parser = new QueryParser(Version.LUCENE_30,
                    field, new StandardAnalyzer(Version.LUCENE_30));

            System.out.println("QueryParser Field:" + parser.getField());

            Query query = parser.parse(queryString);

            System.out.println("query:" + query.toString());

            long start = System.currentTimeMillis();
            TopDocs hits = indexSearcher.search(query, 10);
            long end = System.currentTimeMillis();

            System.out.println("Found " + hits.totalHits
                    + " records(s) (in " + (end - start)
                    + " milliseconds) that matched query '"
                    + queryString + "':");

            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                Document doc = indexSearcher.doc(scoreDoc.doc);
                System.out.println(doc);
            }
        } catch (Exception ex) {
            System.out.println("Error:IndexSearcher creation error! "
                    + "Make sure your index directory is correct!"
                    + ex.getMessage());
        }
    }
}
