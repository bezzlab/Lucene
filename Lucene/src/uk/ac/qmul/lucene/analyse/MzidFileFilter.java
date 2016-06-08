
package uk.ac.qmul.lucene.analyse;

import java.io.File;
import java.io.FileFilter;

/**
 * This class filter out only XML files.
 * 
 * @author Suresh Hewapathirana
 */
public class MzidFileFilter implements FileFilter {

        /**
         * Evaluate whether the file format is mzIdentML or not
         * 
         * @param filePath Full file path including filename with file extension
         * For example "C:/Users/Your Name/Desktop/index/sample.mzid"
         * 
         * @return If the file format is mzIdentML, accept method returns true; otherwise false;
         */
        @Override
        public boolean accept(File filePath) {
            return filePath.getName().toLowerCase().endsWith(".mzid");
        }
    }
