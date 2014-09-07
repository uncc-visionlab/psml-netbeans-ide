/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author emahfoud
 */
package org.bag3d.netbeans.psml.editortools.codecompletion;

import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.netbeans.spi.editor.typinghooks.DeletedTextInterceptor;

public class PSMDeletedTextInterceptor implements DeletedTextInterceptor{

    @Override
    public boolean beforeRemove(Context cntxt) throws BadLocationException {
        return false;
    }

    @Override
    public void remove(Context cntxt) throws BadLocationException {
    }

    @Override
    public void afterRemove(Context cntxt) throws BadLocationException {
        String text = cntxt.getText();
        StyledDocument doc = (StyledDocument)cntxt.getDocument();
        int offset = cntxt.getOffset();
        String textAfter = doc.getText(offset-1, 1);
        
        if(text.equals("(") && textAfter.equals(")") ||
           text.equals("[") && textAfter.equals("]") ||
                text.equals("{") && textAfter.equals("}") ||
//                text.equals("\"") && textAfter.equals("\"") ||
                text.equals("'") && textAfter.equals("'")){
            doc.remove(offset-1, 1);
        }
    }

    @Override
    public void cancelled(Context cntxt) {
    }
    
}
