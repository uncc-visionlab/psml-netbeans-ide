/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bag3d.netbeans.psml.editortools.parser;

import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.spi.ParseException;
import org.netbeans.modules.parsing.spi.Parser;

/**
 *
 * @author arwillis
 */
public class PSMEditorParserResult extends Parser.Result {

    private NBPSMParser psmlParser;
    private boolean valid = true;

    PSMEditorParserResult(Snapshot snapshot, NBPSMParser oracleParser) {
        super(snapshot);
        this.psmlParser = oracleParser;
    }

    public NBPSMParser getNBPSMParser()
            throws ParseException {
        if (!valid) {
            throw new ParseException();
        }
        return psmlParser;
    }

    @Override
    protected void invalidate() {
        valid = false;
    }
}
