
package org.bag3d.netbeans.psml.editortools.codecompletion;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.netbeans.spi.editor.typinghooks.TypedTextInterceptor;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author emahfoud
 */
public class PSMTypedTextInterceptor implements TypedTextInterceptor{

    @Override
    public boolean beforeInsert(Context context) throws BadLocationException {
        String text = context.getText();
        if(")]}".contains(text)){
            StyledDocument doc = (StyledDocument)context.getDocument();
            int offset=  context.getOffset();
            String nextChar = doc.getText(offset, 1);
            if(nextChar.equals(text)){
                context.getComponent().setCaretPosition(offset+1);
                return true;
            }
        }
        return false;
    }

    @Override
    public void insert(MutableContext mc) throws BadLocationException {
    }

    @Override
    public void afterInsert(Context context) throws BadLocationException {
        String text = context.getText();
        StyledDocument doc = (StyledDocument)context.getDocument();
        int offset = context.getOffset() + 1;
        if(text.equals("("))
            doc.insertString(offset, ")", null);
        else if(text.equals("["))
            doc.insertString(offset, "]", null);
        else if(text.equals("{"))
            doc.insertString(offset, "}", null);
        else if("'\"".contains(text))
            doc.insertString(offset, text, null);
        context.getComponent().setCaretPosition(offset);
    }

    @Override
    public void cancelled(Context cntxt) {
    }
    
}
