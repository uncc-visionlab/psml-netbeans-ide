/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bag3d.netbeans.psml.editortools.indentation;

import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.IndentTask;

/**
 *
 * @author arwillis
 */
public class PSMIndentTaskFactory implements IndentTask.Factory {

    @Override
    public IndentTask createTask(Context context) {
        return new PSMIndentTask(context);
    }
}
