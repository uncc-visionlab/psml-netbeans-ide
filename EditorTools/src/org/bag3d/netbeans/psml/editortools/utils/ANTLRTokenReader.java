/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bag3d.netbeans.psml.editortools.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.openide.util.Exceptions;
import org.bag3d.netbeans.psml.editortools.lexer.PSML2Lexer;
import org.bag3d.netbeans.psml.editortools.lexer.PSMTokenId;

/**
 *
 * @author James Reid
 */
public class ANTLRTokenReader {

    // Map<String,String> of ANTLR Tokens to Syntactical Types used to generate different colored text
    private final HashMap<String, String> tokenTypes = new HashMap<String, String>();

    // List of String tokens from ANTLR and the ANTLR-assigned digit for the token (as the token id)
    private final ArrayList<PSMTokenId> tokens = new ArrayList<PSMTokenId>();

    public ANTLRTokenReader() {
        init();
    }

    /**
     * Initializes the map to include any keywords in the Hop Programming
     * language.
     */
    private void init() {
        tokenTypes.put("StringLiteral", "string");
        tokenTypes.put("CharacterLiteral", "string");
        tokenTypes.put("OctalLiteral", "number");
        tokenTypes.put("FloatingPointLiteral", "number");
        tokenTypes.put("DecimalLiteral", "number");
        tokenTypes.put("HexLiteral", "number");

        tokenTypes.put("Identifier", "identifier");

        tokenTypes.put("COMMENT", "comment");
        tokenTypes.put("LINE_COMMENT", "comment");

        String[] javaKeywords = {"'abstract'", "'continue'", "'for'", "'new'", "'switch'",
            "'assert'", "'default'", "'goto'", "'package'",
            "'synchronized'", "'boolean'", "'do'", "'if'", "'private'",
            "'this'", "'break'", "'double'", "'implements'", "'protected'", "'throw'",
            "'byte'", "'else'", "'import'", "'public'", "'throws'",
            "'case'", "'enum'", "'instanceof'", "'return'", "'transient'",
            "'catch'", "'extends'", "'int'", "'short'", "'try'",
            "'char'", "'final'", "'interface'", "'static'", "'void'",
            "'class'", "'finally'", "'long'", "'strictfp'", "'volatile'",
            "'const'", "'float'", "'native'", "'super'", "'while'"};
        for (String keyword : javaKeywords) {
            tokenTypes.put(keyword, "keyword");
        }
        String[] java_reservedLiteralValues = {"'true'", "'false'", "'null'"};
        for (String literal : java_reservedLiteralValues) {
            tokenTypes.put(literal, "string");
        }
        // PSML Syntax
        String[] psmlKeywords = {"'rules'"};
        for (String keyword : psmlKeywords) {
            tokenTypes.put(keyword, "keyword");
        }
        String[] psml_reservedLiteralValues = {"'terminal'", "'j3d.terminal'"};
        for (String literal : psml_reservedLiteralValues) {
            tokenTypes.put(literal, "terminal");
        }
        tokenTypes.put("COLON", "colon");
    }

    /**
     * Reads the token file from the ANTLR parser and generates appropriate
     * tokens.
     *
     * @return
     */
    public List<PSMTokenId> readTokenFile() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inp = classLoader.getResourceAsStream("org/bag3d/netbeans/psml/editortools/utils/PSML2.tokens");
        BufferedReader input = new BufferedReader(new InputStreamReader(inp));
        readTokenFile(input);
        return tokens;
    }

    /**
     * Reads in the token file.
     *
     * @param buff
     */
    private void readTokenFile(BufferedReader buff) {
        String line;
        String[] psml2TokenStrings = PSML2Lexer.tokenNames;
        try {
            while ((line = buff.readLine()) != null) {
                int idEqual = line.lastIndexOf("=");
                String[] splLine = {line.substring(0, idEqual),
                    line.substring(idEqual + 1, line.length())
                };
                if (!splLine[0].contains("'")) {
                    String name = splLine[0];
                    int tok = Integer.parseInt(splLine[1].trim());
                    PSMTokenId id;
                    String tokenCategory = null;
                    if (tok < psml2TokenStrings.length) {
                        tokenCategory = tokenTypes.get(psml2TokenStrings[tok]);
                    } else {
                        tokenCategory = tokenTypes.get(name);
                    }
                    if (tokenCategory != null) {
                        //if the value exists, put it in the correct category
                        id = new PSMTokenId(name, tokenCategory, tok);
                    } else {
                        //if we don't recognize the token, consider it to a separator
                        id = new PSMTokenId(name, "separator", tok);
                    }
                    //add it into the vector of tokens
                    tokens.add(id);
                }
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
