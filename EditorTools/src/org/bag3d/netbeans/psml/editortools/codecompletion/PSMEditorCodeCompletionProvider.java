/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bag3d.netbeans.psml.editortools.codecompletion;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import org.bag3d.netbeans.psml.editortools.lexer.PSML2Lexer;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.openide.util.Exceptions;
import static org.bag3d.netbeans.psml.editortools.PSMLanguageProvider.PSML_MIME_STR;
import org.bag3d.netbeans.psml.editortools.lexer.PSMTokenId;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
//import org.xml.sax.helpers.XMLReaderFactory;
import javax.xml.parsers.SAXParserFactory;

/**
 *
 * @author emahfoud
 */
@MimeRegistration(mimeType = PSML_MIME_STR, service = CompletionProvider.class)
//@MimeRegistration(mimeType = "text/x-psml", service = CompletionProvider.class)
public class PSMEditorCodeCompletionProvider implements CompletionProvider {

    private static ArrayList<CodeCompletionWord> keywords = null;

    public PSMEditorCodeCompletionProvider() {
        if (true) {
            keywords = new ArrayList<CodeCompletionWord>(15);
            try {
                //XMLReader reader = XMLReaderFactory.createXMLReader();
                SAXParserFactory parserFactory = SAXParserFactory.newInstance();
                SAXParser parser = parserFactory.newSAXParser();
                XMLReader reader = parser.getXMLReader();
                reader.setContentHandler(new PSMLanguageReader(keywords));
                reader.parse(new InputSource(PSMCompletionItem.class.getResourceAsStream("PSMLanguage.xml")));
            } catch (ParserConfigurationException ex) {
                Exceptions.printStackTrace(ex);
            } catch (SAXException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    //@SuppressWarnings("unchecked")
    @Override
    public CompletionTask createTask(int queryType, final JTextComponent jtc) {
        if (queryType != CompletionProvider.COMPLETION_QUERY_TYPE) {
            return null;
        }

        return new AsyncCompletionTask(new AsyncCompletionQuery() {

            @Override
            protected void query(CompletionResultSet completionResultSet, Document document, int caretOffset) {
                String filter = null;
                int startOffset = caretOffset - 1;

                try {
                    final StyledDocument bDoc = (StyledDocument) document;
                    final int lineStartOffset = getRowFirstNonWhite(bDoc, caretOffset);
                    final char[] line = bDoc.getText(lineStartOffset, caretOffset - lineStartOffset).toCharArray();
                    final int whiteOffset = indexOfWhite(line);
                    filter = new String(line, whiteOffset + 1, line.length - whiteOffset - 1);
                    if (whiteOffset > 0) {
                        startOffset = lineStartOffset + whiteOffset + 1;
                    } else {
                        startOffset = lineStartOffset;
                    }
                } catch (BadLocationException ex) {
                    Exceptions.printStackTrace(ex);
                }

                for (int i = 0; i < keywords.size(); i++) {
                    CodeCompletionWord word = keywords.get(i);
                    if (word.Name.toLowerCase().startsWith(filter.toLowerCase())) {
                        completionResultSet.addItem(new PSMCompletionItem(word.Name, word.Color,
                                word.Description, startOffset, caretOffset));
                    }
                }

                StyledDocument doc = (StyledDocument) jtc.getDocument();
                TokenHierarchy<StyledDocument> th = TokenHierarchy.get(doc);
                TokenSequence<?> sequence = th.tokenSequence();
                ArrayList<String> identifiers = new ArrayList<String>();
                while (sequence.moveNext()) {
                    Token<?> t = sequence.token();
                    if (t.id().ordinal() == PSML2Lexer.Identifier) {
                        CharSequence cs = t.text();
                        String text = t.text().toString();
                        if (!text.equalsIgnoreCase(filter)
                                && text.toLowerCase().startsWith(filter.toLowerCase())
                                && !identifiers.contains(text)) {
                            identifiers.add(text);
                        }
                    }
                }

                for (String id : identifiers) {
                    CodeCompletionWord word = new CodeCompletionWord();
                    word.Name = id;
                    if (!keywords.contains(word)) {
                        completionResultSet.addItem(new PSMCompletionItem(id, Color.BLACK,
                                "", startOffset, caretOffset));
                    }
                }
                completionResultSet.finish();
            }
        }, jtc);
    }

    @Override
    public int getAutoQueryTypes(JTextComponent jtc, String string) {
        if (string.equals(".") || string.equals(":")) {
            try {
                int offset = jtc.getCaretPosition();
                String previous = jtc.getText(offset - 2, 1);
                if (!Character.isDigit(previous.charAt(0))) {
                    return CompletionProvider.COMPLETION_QUERY_TYPE;
                }
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else if (string.equals(";")) {
            Completion.get().hideAll();
        }
        return 0;
    }

    static int getRowFirstNonWhite(StyledDocument doc, int offset)
            throws BadLocationException {
        Element lineElement = doc.getParagraphElement(offset);
        int start = lineElement.getStartOffset();
        while (start + 1 < lineElement.getEndOffset()) {
            try {
                if (doc.getText(start, 1).charAt(0) != ' ') {
                    break;
                }
            } catch (BadLocationException ex) {
                throw (BadLocationException) new BadLocationException(
                        "calling getText(" + start + ", " + (start + 1)
                        + ") on doc of length: " + doc.getLength(), start).initCause(ex);
            }
            start++;
        }
        return start;
    }

    static int indexOfWhite(char[] line) {
        int i = line.length;
        while (--i > -1) {
            final char c = line[i];
            if (!Character.isJavaIdentifierPart(c)) {
                return i;
            }
        }
        return -1;
    }
}
