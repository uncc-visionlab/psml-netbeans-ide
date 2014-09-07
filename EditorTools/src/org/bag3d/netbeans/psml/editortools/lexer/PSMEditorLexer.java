/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bag3d.netbeans.psml.editortools.lexer;

import org.antlr.v4.runtime.CharStream;
import org.netbeans.api.lexer.Token;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;
import org.bag3d.netbeans.psml.editortools.PSMLanguageHierarchy;
import org.bag3d.netbeans.psml.editortools.utils.ANTLRCharStream;

/**
 *
 * @author arwillis
 */
public class PSMEditorLexer implements Lexer<PSMTokenId> {

    private final LexerRestartInfo<PSMTokenId> info;
    private final PSML2Lexer lexer;

    public PSMEditorLexer(LexerRestartInfo<PSMTokenId> info) {
        this.info = info;
        ANTLRCharStream charStream = new ANTLRCharStream(info.input(), "PSMEditorLexer", false);
        lexer = new PSML2Lexer((CharStream) charStream);
    }

    @Override
    public org.netbeans.api.lexer.Token<PSMTokenId> nextToken() {
        org.antlr.v4.runtime.Token token = lexer.nextToken();

        Token<PSMTokenId> createdToken = null;

        if (token.getType() != -1) {
            PSMTokenId tokenId = PSMLanguageHierarchy.getToken(token.getType());
            createdToken = info.tokenFactory().createToken(tokenId);
        } else if (info.input().readLength() > 0) {
            PSMTokenId tokenId = PSMLanguageHierarchy.getToken(PSML2Lexer.WS);
            createdToken = info.tokenFactory().createToken(tokenId);
        }

        return createdToken;
    }

    @Override
    public Object state() {
        return null;
    }

    @Override
    public void release() {
    }
}
