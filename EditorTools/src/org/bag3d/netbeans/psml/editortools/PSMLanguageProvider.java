/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bag3d.netbeans.psml.editortools;

import org.netbeans.api.lexer.InputAttributes;
import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.LanguagePath;
import org.netbeans.api.lexer.Token;
import org.netbeans.spi.lexer.LanguageEmbedding;
import org.netbeans.spi.lexer.LanguageProvider;

/**
 *
 * @author arwillis
 */
@org.openide.util.lookup.ServiceProvider(service = org.netbeans.spi.lexer.LanguageProvider.class)
public class PSMLanguageProvider extends LanguageProvider {

    public static final String PSML_MIME_TYPE_STR = "text";
    public static final String PSML_MIME_SUBTYPE_STR = "x-psml";
    public static final String PSML_MIME_STR = PSML_MIME_TYPE_STR + "/" + PSML_MIME_SUBTYPE_STR;

    @Override
    public Language<?> findLanguage(String mimeType) {
        if (mimeType.equals(PSML_MIME_STR)) {
            return new PSMLanguageHierarchy().language();
        }

        return null;
    }

    @Override
    public LanguageEmbedding<?> findLanguageEmbedding(Token<?> arg0, LanguagePath arg1, InputAttributes arg2) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return null;
    }

}
