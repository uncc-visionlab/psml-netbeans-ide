/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bag3d.netbeans.psml.editortools.parser;

/**
 *
 * @author arwillis
 */
import java.util.Collection;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.parsing.spi.ParserFactory;

public class PSMParserFactory extends ParserFactory {

    @Override
    public Parser createParser(Collection<Snapshot> snapshots) {
        return new NBPSMParser();
    }
}
