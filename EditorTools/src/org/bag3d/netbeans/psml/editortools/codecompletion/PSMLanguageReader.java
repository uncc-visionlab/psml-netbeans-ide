/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bag3d.netbeans.psml.editortools.codecompletion;

import java.awt.Color;
import java.util.ArrayList;
import org.openide.util.Exceptions;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 *
 * @author emahfoud
 */
public class PSMLanguageReader implements ContentHandler{
    private ArrayList<CodeCompletionWord> keywords;
    private Color currentColor = Color.black;
    private CodeCompletionWord currentWord = null;
    private String currentElement = null;
    
    public PSMLanguageReader(ArrayList<CodeCompletionWord> keywords){
        this.keywords = keywords;
    }
    @Override
    public void setDocumentLocator(Locator locator) {
    }

    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        currentElement = localName;
        if (localName.equalsIgnoreCase("wordGroup")){
            String colorString = atts.getValue("color");
            if(colorString.startsWith("0x"))
                currentColor = Color.decode(colorString);
            else 
                try {
                currentColor = (Color)(Color.class.getField(colorString.toUpperCase()).get(null));
            } catch (NoSuchFieldException ex) {
                Exceptions.printStackTrace(ex);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        } else if(localName.equalsIgnoreCase("word")){
            currentWord = new CodeCompletionWord();
            currentWord.Color = currentColor;
            currentWord.Name  = atts.getValue("name");
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(localName.equalsIgnoreCase("word"))
            keywords.add(currentWord);
        currentElement = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(currentElement != null && currentElement.equalsIgnoreCase("desc")){
            String desc = new String(ch, start, length);
            currentWord.Description = desc;
        }
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
    }
    
}
