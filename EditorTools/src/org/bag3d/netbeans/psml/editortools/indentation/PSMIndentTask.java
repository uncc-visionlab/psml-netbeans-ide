/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bag3d.netbeans.psml.editortools.indentation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.text.BadLocationException;
import javax.swing.text.Position;
import javax.swing.text.StyledDocument;
import org.netbeans.api.lexer.LanguagePath;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.modules.editor.indent.api.IndentUtils;
import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.ExtraLock;
import org.netbeans.modules.editor.indent.spi.IndentTask;
import org.openide.ErrorManager;
import org.openide.text.NbDocument;
import org.bag3d.netbeans.psml.editortools.lexer.PSMTokenId;

/**
 *
 * @author arwillis
 */
public class PSMIndentTask implements IndentTask {

    private Context context;

    PSMIndentTask(Context context) {
        this.context = context;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void reindent() throws BadLocationException {
        StyledDocument doc = (StyledDocument) context.document();
        try {
            TokenHierarchy<?> tokenHierarchy = TokenHierarchy.get(doc);
            LanguagePath languagePath = (LanguagePath) tokenHierarchy.languagePaths().toArray()[0];
            Iterator<Context.Region> it = context.indentRegions().iterator();
            while (it.hasNext()) {
                Context.Region region = it.next();

                Map<Position, Integer> indentMap = new HashMap<Position, Integer>();
                int ln = NbDocument.findLineNumber(doc, region.getStartOffset());
                int endLineNumber = NbDocument.findLineNumber(doc, region.getEndOffset());
                //if (!Utils.getTokenSequence (doc, context.lineStartOffset (region.getStartOffset ())).language ().mimeType ().equals (mimeType))

//                int prevLineOffset = NbDocument.findLineOffset(doc, ln - 1);
//                prevLineOffset = prevLineOffset < 0 ? 0 : prevLineOffset;


                TokenSequence<PSMTokenId> sequence = (TokenSequence) tokenHierarchy.tokenSequenceList(languagePath, 0, region.getEndOffset()-1).get(0);

                int off = 0;
                while (sequence.moveNext()) {
                    Token<PSMTokenId> t = sequence.token();
                    if (t.id().ordinal() == 56) {
                        off++;
                    } else if (t.id().ordinal() == 13) {
                        off--;
                    }
                }
                int indent = off * IndentUtils.indentLevelSize(doc);

                context.modifyIndent(region.getStartOffset(), indent);
            }
        } catch (Exception ex) {
            ErrorManager.getDefault().notify(ex);
        }
    }

    @Override
    public ExtraLock indentLock() {
        return null;
    }
}
