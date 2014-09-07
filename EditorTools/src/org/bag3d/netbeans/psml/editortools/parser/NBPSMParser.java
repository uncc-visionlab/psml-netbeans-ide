/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bag3d.netbeans.psml.editortools.parser;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.api.Task;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.parsing.spi.SourceModificationEvent;
import org.bag3d.netbeans.psml.editortools.lexer.PSML2Lexer;

/**
 *
 * @author arwillis
 */
public class NBPSMParser extends Parser {

    private Snapshot snapshot;
    private PSML2Parser psmParser;
    public List<SyntaxError> syntaxErrors = new ArrayList<SyntaxError>();

    public static class SyntaxError {

        public RecognitionException exception;
        public String message;
        public int line;
        public int charPositionInLine;
        public int start, stop;

        SyntaxError(Object offendingSymbol, int line, int charPositionInLine, String msg,
                RecognitionException e) {
            this.exception = e;
            this.message = msg;
            this.line = line;
            this.charPositionInLine = charPositionInLine;
             if (offendingSymbol instanceof org.antlr.v4.runtime.Token) {
                org.antlr.v4.runtime.Token t = (org.antlr.v4.runtime.Token) offendingSymbol;
                start = t.getStartIndex();
                stop = t.getStopIndex() + 1;
            }
        }
    }

    class PSMParseErrorListener extends ConsoleErrorListener {

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, 
                int line, int charPositionInLine, String msg, RecognitionException e) {
            syntaxErrors.add(new SyntaxError( offendingSymbol, line, charPositionInLine, msg, e));
        }
    }

    @Override
    public void parse(Snapshot snapshot, Task task, SourceModificationEvent event) {
        this.snapshot = snapshot;
        ANTLRInputStream input = new ANTLRInputStream(snapshot.getText().toString());

        Lexer lexer = new PSML2Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        psmParser = new PSML2Parser(tokens);
        syntaxErrors.clear();
        psmParser.addErrorListener(new PSMParseErrorListener());
        try {
            psmParser.compilationUnit();
        } catch (RecognitionException ex) {
            ex.printStackTrace();
        }
    }

    PSML2Parser getANTLRParser() {
        return psmParser;
    }

    @Override
    public Result getResult(Task task) {
        return new PSMEditorParserResult(snapshot, this);
    }

    @Override
    public void addChangeListener(ChangeListener changeListener) {
    }

    @Override
    public void removeChangeListener(ChangeListener changeListener) {
    }

}
