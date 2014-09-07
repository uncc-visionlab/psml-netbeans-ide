/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bag3d.netbeans.psml.editortools.parser;

import java.util.ArrayList;
import java.util.List;
import javax.swing.text.Document;
import org.netbeans.modules.parsing.spi.ParserResultTask;
import org.netbeans.modules.parsing.spi.Scheduler;
import org.netbeans.modules.parsing.spi.SchedulerEvent;
import org.netbeans.spi.editor.hints.ErrorDescription;
import org.netbeans.spi.editor.hints.ErrorDescriptionFactory;
import org.netbeans.spi.editor.hints.HintsController;
import org.netbeans.spi.editor.hints.Severity;
import static org.bag3d.netbeans.psml.editortools.PSMLanguageProvider.PSML_MIME_SUBTYPE_STR;
import org.bag3d.netbeans.psml.editortools.parser.NBPSMParser.SyntaxError;

/**
 *
 * @author arwillis
 */
public class SyntaxErrorsHighlightingTask extends ParserResultTask<PSMEditorParserResult> {

    public SyntaxErrorsHighlightingTask() {
    }

    @Override
    public void run(PSMEditorParserResult result, SchedulerEvent event) {
        try {
            List<SyntaxError> syntaxErrors = result.getNBPSMParser().syntaxErrors;
            Document document = result.getSnapshot().getSource().getDocument(false);
            List<ErrorDescription> errors = new ArrayList<ErrorDescription>();
            for (SyntaxError syntaxError : syntaxErrors) {
                if (syntaxError.line <= 0) {
                    continue;
                }
                ErrorDescription errorDescription = ErrorDescriptionFactory.createErrorDescription(
                        Severity.ERROR,
                        syntaxError.message,
                        document,
                        document.createPosition(syntaxError.start),
                        document.createPosition(syntaxError.stop));
                errors.add(errorDescription);
            }
            HintsController.setErrors(document, PSML_MIME_SUBTYPE_STR, errors);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public Class<? extends Scheduler> getSchedulerClass() {
        return Scheduler.EDITOR_SENSITIVE_TASK_SCHEDULER;
    }

    @Override
    public void cancel() {
    }

}
