/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bag3d.netbeans.psml.editortools.codecompletion;

import java.awt.Color;

/**
 *
 * @author emahfoud
 */
public class CodeCompletionWord {
    public String Name;
    public Color Color;
    public String Description;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CodeCompletionWord){
            CodeCompletionWord other = (CodeCompletionWord)obj;
            return Name.equals(other.Name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.Name != null ? this.Name.hashCode() : 0);
        return hash;
    }
    
    
}
