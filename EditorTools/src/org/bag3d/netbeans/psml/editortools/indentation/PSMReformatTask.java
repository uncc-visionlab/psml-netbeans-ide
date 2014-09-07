/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bag3d.netbeans.psml.editortools.indentation;

import java.util.Stack;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.mimelookup.MimePath;
import org.netbeans.api.lexer.LanguagePath;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.modules.editor.indent.api.IndentUtils;
import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.ExtraLock;
import org.netbeans.modules.editor.indent.spi.ReformatTask;
import org.bag3d.netbeans.psml.editortools.lexer.PSML2Lexer;
import org.bag3d.netbeans.psml.editortools.lexer.PSMTokenId;

/**
 *
 * @author arwillis
 */
public class PSMReformatTask implements ReformatTask {

    private Context context;
    int baseIndent = 0;
    int offset = 0;
    boolean nestedIndent = false;
    boolean isNewLine = true;
    int indentSize = 0;

    public PSMReformatTask(Context context) {
        this.context = context;
    }

    @Override
    public void reformat() throws BadLocationException {
        StyledDocument doc = (StyledDocument) this.context.document();
        indentSize = IndentUtils.indentLevelSize(doc);
//        int start = context.startOffset();
//        start = context.lineStartOffset(start);
//        int end = context.endOffset();
//        int lineNum = NbDocument.findLineNumber(doc, end) + 1;
//        try {
//            end = NbDocument.findLineOffset(doc, lineNum) - 1;
//        } catch (IndexOutOfBoundsException ex) {
//            end = doc.getLength() - 1;
//        }
        int start = 0;
        int end = doc.getLength() - 1;
        int length = end - start;

        MimePath mimePath = MimePath.parse(context.mimePath());
        TokenHierarchy tokenHierarchy = TokenHierarchy.get(doc);
        LanguagePath languagePath = LanguagePath.get(org.netbeans.api.lexer.Language.find(mimePath.getMimeType(0)));
        TokenSequence<PSMTokenId> sequence = (TokenSequence) tokenHierarchy.tokenSequenceList(languagePath, start, end).get(0);
        int caretPos = context.caretOffset();
        sequence.move(caretPos);
        Token<PSMTokenId> currentToken = null;
        if (sequence.moveNext()) {
            currentToken = sequence.token();
        }

        int lineStartOffset = context.lineStartOffset(start);
        baseIndent = context.lineIndent(lineStartOffset);

        StringBuilder sb = new StringBuilder(length);
        boolean isForStatement = false;
        int lastTokenOrdinal = 0;
        int lastBreakIndex = -1;
        int maxLineSize = 85;
        int forParenCount = 0;
        Stack<Integer> braceCounts = new Stack<Integer>();
        try {
            sequence.moveStart();
            while (sequence.moveNext()) {
                Token<PSMTokenId> t = sequence.token();
                insert(null, sb);
                Integer count = 0;
                switch (t.id().ordinal()) {
                    case 56:
                        count = braceCounts.pop();
                        count--;

                        if (count == 0) {
                            offset--;
                            sb.delete(sb.length() - indentSize, sb.length());
                            sb.append(t.text());
                            sb.append('\n');
                            lastBreakIndex = sb.length();
                            isNewLine = true;
                        } else {
                            sb.append(t.text());
                            braceCounts.push(count);
                        }
                        break;
                    case 13:
                        count = braceCounts.pop();
                        if (count == 0) {
                            sb.append(' ');
                            sb.append(t.text());
                            sb.append('\n');
                            lastBreakIndex = sb.length();
                            isNewLine = true;
                            offset++;
                        } else {
                            if (lastTokenOrdinal == 42
                                    || lastTokenOrdinal == 25) {
                                if (sb.length() - lastBreakIndex + t.text().length() > maxLineSize) {
                                    nestedIndent = true;
                                    isNewLine = true;
                                    sb.append('\n');
                                    lastBreakIndex = sb.length();
                                } else {
                                    isNewLine = false;
                                    sb.append(' ');
                                }
                            }
                            insert(t, sb);
                        }
                        braceCounts.push(count + 1);
                        break;
//                    case PSMLexer.GRAMMAR:
//                    case PSMLexer.METHOD:
//                    case PSMLexer.RULES:
//                        sb.append(t.text());
//                        braceCounts.push(0);
//                        break;
                    case 47:
                        if (lastTokenOrdinal == 63) {
                            sb.append(' ');
                        } else {
                            braceCounts.push(0);
                        }
                        sb.append(t.text());
                        break;
                    case 63:
                        int lastBreak = sb.lastIndexOf("\n");
                        sb.delete(lastBreak, sb.length());
                        sb.append(' ');
                        sb.append(t.text());
                        braceCounts.push(0);
                        break;
                    case 74:
                        isForStatement = true;
                        forParenCount = 0;
                        sb.append(t.text());
                        braceCounts.push(0);
                        break;
                    case 45:
                        if (isForStatement) {
                            forParenCount--;
                            isForStatement = forParenCount != 0;
                        }
                        sb.append(t.text());
                        break;
                    case 22:
//                        switch (lastTokenOrdinal) {
//                            case PSMLexer.AND:
//                            case PSMLexer.OR:
//                            case PSMLexer.PLUS:
//                            case PSMLexer.MINUS:
//                            case PSMLexer.ASTERISK:
//                            case PSMLexer.EEQUAL:
//                            case PSMLexer.EQUAL:
//                            case PSMLexer.NOTEQUAL:
//                            case PSMLexer.GREATERTHAN:
//                            case PSMLexer.LESSTHAN:
//                            case PSMLexer.PERCENT:
//                            case PSMLexer.Exponent:
//                            case PSMLexer.OVER:
//                            case PSMLexer.COMMA:
//                            case PSMLexer.LESSTHANEQ:
//                            case PSMLexer.GREATERTHANEQ:
//                                if (sb.length() - lastBreakIndex + t.text().length() > maxLineSize) {
//                                    nestedIndent = true;
//                                    isNewLine = true;
//                                    sb.append('\n');
//                                    lastBreakIndex = sb.length();
//                                } else {
//                                    isNewLine = false;
//                                    sb.append(' ');
//                                }
//                                break;
//                        }
//                        if (isForStatement) {
//                            forParenCount++;
//                        }
//                        insert(t, sb);
//                        break;
//                    case PSMLexer.AND:
//                    case PSMLexer.OR:
//                    case PSMLexer.PLUS:
//                    case PSMLexer.MINUS:
//                    case PSMLexer.ASTERISK:
//                    case PSMLexer.EEQUAL:
//                    case PSMLexer.EQUAL:
//                    case PSMLexer.NOTEQUAL:
//                    case PSMLexer.GREATERTHAN:
//                    case PSMLexer.LESSTHAN:
//                    case PSMLexer.PERCENT:
//                    case PSMLexer.Exponent:
//                    case PSMLexer.OVER:
//                    case PSMLexer.INSTANCEOF:
//                    case PSMLexer.LESSTHANEQ:
//                    case PSMLexer.GREATERTHANEQ:
//                        sb.append(' ');
//                        sb.append(t.text());
//                        break;
//                    case PSMLexer.SEMICOLON:
//                        sb.append(t.text());
//                        if (!isForStatement) {
//                            sb.append('\n');
//                            lastBreakIndex = sb.length();
//                            nestedIndent = false;
//                            isNewLine = true;
//                        }
//                        break;
//                    case PSMLexer.WS:
//                        int breakCount = 0;
//                        for (int i = 0; i < t.text().length(); i++) {
//                            if (t.text().charAt(i) == '\n') {
//                                breakCount++;
//                            }
//                        }
//                        if (breakCount > 2) {
//                            for (int i = 0; i < breakCount - 1; i++) {
//                                sb.append('\n');
//                                lastBreakIndex = sb.length();
//                            }
//                            isNewLine = true;
//                        }
//                        break;
//                    case PSMLexer.LINE_COMMENT:
//                        sb.append(t.text());
//                        if (t.text().toString().endsWith("\n")) {
//                            isNewLine = true;
//                            lastBreakIndex = sb.length();
//                        }
//                        break;
//                    case PSMLexer.COMMENT:
//                        sb.append(t.text());
//                        sb.append('\n');
//                        lastBreakIndex = sb.length();
//                        isNewLine = true;
//                        break;
//                    case PSMLexer.IDENTIFIER:
//                    case PSMLexer.FloatingPointLiteral:
//                    case PSMLexer.StringLiteral:
//                    case PSMLexer.SHAPE3D:
//                    case PSMLexer.NAMESPACE:
//                    case PSMLexer.MYSHAPE:
//                    case PSMLexer.TYPE_MATH:
//                        switch (lastTokenOrdinal) {
//                            case PSMLexer.LEFTBRACKET:
//                            case PSMLexer.LEFTPAREN:
//                            case PSMLexer.LEFTBRACE:
//                            case PSMLexer.COLON:
//                            case PSMLexer.SEMICOLON:
//                            case PSMLexer.DOT:
//                                break;
//                            default:
//                                if (sb.length() - lastBreakIndex + t.text().length() > maxLineSize) {
//                                    nestedIndent = true;
//                                    isNewLine = true;
//                                    sb.append('\n');
//                                    lastBreakIndex = sb.length();
//                                } else {
//                                    isNewLine = false;
//                                    sb.append(' ');
//                                }
//                                break;
//                        }
//                        insert(t, sb);
//                        break;
                    default:
                        sb.append(t.text());
                        break;

                }
                if (t.id().ordinal() != PSML2Lexer.WS
                        && t.id().ordinal() != PSML2Lexer.COMMENT
                        && t.id().ordinal() != PSML2Lexer.LINE_COMMENT) {
                    lastTokenOrdinal = t.id().ordinal();
                }
            }
            doc.remove(start, length);
            doc.insertString(start, sb.toString().trim(), null);
        } catch (Exception ex) {
            System.out.println("Cannot format document. Please check the validity of your script.");
        }
    }

    private void insert(Token<PSMTokenId> t, StringBuilder sb) {

        if (isNewLine) {
            int indent = baseIndent + (nestedIndent ? offset + 1 : offset) * indentSize;
            for (int i = 0; i < indent; i++) {
                sb.append(' ');
            }
            isNewLine = false;
        }
        if (t != null) {
            sb.append(t.text());
        }
    }

    @Override
    public ExtraLock reformatLock() {
        return null;
    }
}
