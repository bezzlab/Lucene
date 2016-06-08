
package uk.ac.qmul.lucene.util;

import java.io.IOException;
import java.io.StringReader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/**
 *
 * @author sureshhewapathirana
 */
public class AnalyzerUtils {

    public static void displayTokens(Analyzer analyzer,String text, String displayMode) throws IOException {
        if(displayMode.equals("TokensOnly")){
            displayOnlyTokens(analyzer, text);
        }else if(displayMode.equals("TokensFullDetails")){
            displayOnlyTokens(analyzer, text);
            displayTokensWithFullDetails(analyzer, text);
        }

    }

    private static void displayOnlyTokens(Analyzer analyzer,String text)throws IOException {
        TokenStream stream = analyzer.tokenStream("contents", new StringReader(text));
        CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
        while (stream.incrementToken()) {
            System.out.print("[" + term.toString() + "] ");
        }
    }

    private static void displayTokensWithFullDetails(Analyzer analyzer,
            String text)
            throws IOException {
        
        TokenStream stream = analyzer.tokenStream("contents",new StringReader(text));
        CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
        PositionIncrementAttribute posIncr = stream.addAttribute(PositionIncrementAttribute.class);
        OffsetAttribute offset = stream.addAttribute(OffsetAttribute.class);
        TypeAttribute type = stream.addAttribute(TypeAttribute.class);
        PayloadAttribute payload = stream.addAttribute(PayloadAttribute.class);
        
        
        int position = 0;
        while (stream.incrementToken()) {
            int increment = posIncr.getPositionIncrement();
            if (increment > 0) {
                position = position + increment;
                System.out.println();
                System.out.print(position + ": ");
            }
            System.out.print("["
                    + term.toString() + ":"
                    + offset.startOffset() + "->"
                    + offset.endOffset() + ":"
                    + payload.toString() + ":"
                    + type.type() + "] ");
        }
        System.out.println();
    }

}
