/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bag3d.netbeans.psml.editortools;

import org.bag3d.netbeans.psml.editortools.lexer.PSMEditorLexer;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.netbeans.api.lexer.Language;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;
import org.bag3d.netbeans.psml.editortools.lexer.PSMTokenId;
import org.bag3d.netbeans.psml.editortools.utils.ANTLRTokenReader;

/**
 *
 * @author arwillis
 */
public class PSMLanguageHierarchy extends LanguageHierarchy<PSMTokenId> {
    public static List<PSMTokenId> tokens;
    // Map ANTLR integer to ANTLR token types computed from contents of PSM.tokens
    public static Map<Integer, PSMTokenId> idToToken;
    public static final Language<PSMTokenId> language = new PSMLanguageHierarchy().language();

    public static Language<PSMTokenId> getLanguage() {
        return language;
    }

    /**
     * Initializes the list of tokens with IDs generated from the ANTLR
     * token file.
     */
    private static void init() {
        ANTLRTokenReader reader = new ANTLRTokenReader();
        tokens = reader.readTokenFile();
        idToToken = new HashMap<Integer, PSMTokenId>();
        for (PSMTokenId token : tokens) {
            idToToken.put(token.ordinal(), token);
        }
    }

    /**
     * Returns an actual CMinusTokenId from an id. This essentially allows
     * the syntax highlighter to decide the color of specific words.
     * @param id
     * @return
     */
    public static synchronized PSMTokenId getToken(int id) {
        if (idToToken == null) {
            init();
        }
        return idToToken.get(id);
    }

    /**
     * Initializes the tokens in use.
     *
     * @return
     */
    @Override
    protected synchronized Collection<PSMTokenId> createTokenIds() {
        if (tokens == null) {
            init();
        }
        return tokens;
    }

   /**
     * Creates a lexer object for use in syntax highlighting.
     *
     * @param info
     * @return
     */
    @Override
    protected synchronized Lexer<PSMTokenId> createLexer(LexerRestartInfo<PSMTokenId> info) {
        return new PSMEditorLexer(info);
    }

    /**
     * Returns the mime type of this programming language ("text/x-psml). This
     * allows NetBeans to load the appropriate editors and file loaders when
     * a file with the cm file extension is loaded.
     *
     * @return
     */
    @Override
    protected String mimeType() {
        return PSMLanguageProvider.PSML_MIME_STR;
    }

}
