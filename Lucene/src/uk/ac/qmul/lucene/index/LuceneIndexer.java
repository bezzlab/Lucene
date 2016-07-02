package uk.ac.qmul.lucene.index;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.xml.sax.helpers.DefaultHandler;
import uk.ac.qmul.lucene.analyse.MzidFileFilter;
import uk.ac.qmul.lucene.parse.MzIdentMLStAXParser;

/**
 *
 * @author Suresh Hewapathirana
 */
public class LuceneIndexer extends DefaultHandler {

    IndexWriter writer;
    IndexWriterConfig config;
    StandardAnalyzer analyser;
    Directory dir;

    public static void main(String args[]) throws Exception {

        String mzIdFilepath = "/Users/sureshhewapathirana/Google Drive/TestData/1/ExampleMod.mzid";
//        String mzIdFilepath = "/Users/sureshhewapathirana/Dropbox/TestData/2/Galaxy36.mzid";
//        String indexSavedDirectory = "/Users/sureshhewapathirana/Desktop/Galaxy36Index/";
        String indexSavedDirectory = "/Users/sureshhewapathirana/Desktop/ExampleModIndex/";
        int numIndexed;

        long start = System.currentTimeMillis();
        LuceneIndexer indexer = new LuceneIndexer(indexSavedDirectory);
        MzidFileFilter fileFilter = new MzidFileFilter();

        try {
            numIndexed = indexer.index(mzIdFilepath, fileFilter);
        } finally {
            indexer.close();
        }
        long end = System.currentTimeMillis();
        System.out.println("Indexing " + numIndexed + " files took " + (end - start) + " milliseconds");
    }

    public void close() throws IOException {
        writer.close();
    }

    public LuceneIndexer(String indexDir) throws IOException {

        // initialise variables
        dir = FSDirectory.open(new File(indexDir));
        analyser = new StandardAnalyzer(Version.LUCENE_33);
        config = new IndexWriterConfig(Version.LUCENE_33, analyser);
        writer = new IndexWriter(dir, config);
    }

    public int index(String mzIdFilepath, FileFilter filter) throws FileNotFoundException, Exception {

        List<Document> documents;
        MzIdentMLStAXParser xmlParseHandler = new MzIdentMLStAXParser(mzIdFilepath);
//        MzIdentMLSAXParser xmlParseHandler = new MzIdentMLSAXParser();

        File file = new File(mzIdFilepath);

        // make sure file type is mzid
        if (filter.accept(file)) {

            // parse the file and load data into Document object
            documents = xmlParseHandler.getDocument(mzIdFilepath);

            System.out.println("No of Documents:" + documents.size());
            for (Document document : documents) {
                writer.addDocument(document);
            }
            
            

        } else {
            System.err.println("Error: " + mzIdFilepath + " is not a mzIdentML file");
        }
        return writer.numDocs();
    }
}
