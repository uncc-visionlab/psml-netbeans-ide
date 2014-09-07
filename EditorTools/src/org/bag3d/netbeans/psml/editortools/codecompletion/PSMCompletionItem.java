/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bag3d.netbeans.psml.editortools.codecompletion;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.swing.Action;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.spi.editor.completion.CompletionDocumentation;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;

/**
 *
 * @author emahfoud
 */
public class PSMCompletionItem implements CompletionItem {

    private String text;
    private Color color;
    private String desc;
    private int caretOffset;
    private int startOffset;

    public PSMCompletionItem(String text, Color color, String description, int startOffset, int offset) {
        this.text = text;
        this.color = color;
        this.caretOffset = offset;
        this.startOffset = startOffset;
        this.desc = description;
    }

    @Override
    public void defaultAction(JTextComponent jtc) {
        StyledDocument doc = (StyledDocument) jtc.getDocument();
        try {
            doc.remove(startOffset, caretOffset - startOffset);
            doc.insertString(startOffset, text, null);
            Completion.get().hideAll();
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void processKeyEvent(KeyEvent ke) {
    }

    @Override
    public int getPreferredWidth(Graphics grphcs, Font font) {
        return CompletionUtilities.getPreferredWidth(text, null, grphcs, font);
    }

    @Override
    public void render(Graphics grphcs, Font font, Color color, Color color1, int width, int height, boolean selected) {
        CompletionUtilities.renderHtml(null, text, null, grphcs, font, this.color, width, height, selected);
    }

    @Override
    public CompletionTask createDocumentationTask() {
        if(PSMCompletionItem.this.desc == null || PSMCompletionItem.this.desc.isEmpty())
            return null;
        
        return new AsyncCompletionTask(new AsyncCompletionQuery() {

            @Override
            protected void query(CompletionResultSet crs, Document document, int i) {
                crs.setDocumentation(new CompletionDocumentation() {

                    @Override
                    public String getText() {
                        PSMCompletionItem word = PSMCompletionItem.this;
                        StringBuilder sb = new StringBuilder();
                        sb.append("<h2>" + word.text + "</h2> \n");
                        sb.append("<p>" + word.desc + "</p>");
                        return sb.toString();
                    }

                    @Override
                    public URL getURL() {
                        return null;
                    }

                    @Override
                    public CompletionDocumentation resolveLink(String string) {
                        return null;
                    }

                    @Override
                    public Action getGotoSourceAction() {
                        return null;
                    }
                });
                crs.finish();
            }
        });
    }

    @Override
    public CompletionTask createToolTipTask() {
        return null;
    }

    @Override
    public boolean instantSubstitution(JTextComponent jtc) {
        defaultAction(jtc);
        return true;
    }

    @Override
    public int getSortPriority() {
        return 0;
    }

    @Override
    public CharSequence getSortText() {
        return text;
    }

    @Override
    public CharSequence getInsertPrefix() {
        return text;
    }
}
