/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author emahfoud
 */
package org.bag3d.netbeans.psml.editortools.codecompletion;

import org.netbeans.api.editor.mimelookup.MimePath;
import org.netbeans.spi.editor.typinghooks.DeletedTextInterceptor;
import static org.bag3d.netbeans.psml.editortools.PSMLanguageProvider.PSML_MIME_SUBTYPE_STR;
import static org.bag3d.netbeans.psml.editortools.PSMLanguageProvider.PSML_MIME_TYPE_STR;

public class PSMDeletedTextInterceptorFactory implements DeletedTextInterceptor.Factory {

    @Override
    public DeletedTextInterceptor createDeletedTextInterceptor(MimePath mp) {
        if(MimePath.validate(PSML_MIME_TYPE_STR, PSML_MIME_SUBTYPE_STR))
            return new PSMDeletedTextInterceptor();
        
        return null;
    }
    
}
