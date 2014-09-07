/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bag3d.netbeans.psml.filetype;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Properties;
import org.apache.tools.ant.module.api.support.ActionUtils;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.support.ant.AntProjectHelper;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "org.bag3d.netbeans.psml.filetype.PSMLRunActionListener"
)
@ActionRegistration(
        displayName = "#CTL_PSMLRunActionListener"
)
@ActionReference(path = "Loaders/text/x-psml/Actions", position = 175)
@Messages("CTL_PSMLRunActionListener=Run")
public final class PSMLRunActionListener implements ActionListener {

    private final PSMLDataObject context;

    public PSMLRunActionListener(PSMLDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        FileObject f = context.getPrimaryFile();
        Project project = FileOwnerQuery.getOwner(f);
        String fileName = f.getName();
        String fileExt = f.getNameExt();
        String pathStr = f.getPath();        
        FileObject projectFile = project.getProjectDirectory();
        String projectPathStr = projectFile.getPath();
        String classFile = pathStr.replace(projectPathStr+"/psml/", "");
        String classFileJava = classFile.substring(0, classFile.lastIndexOf(".")) + ".java";
        String classnameExt = classFile.replaceAll("/", ".");
        String classname = classnameExt.substring(0,classnameExt.lastIndexOf("."));
        AntProjectHelper helper = project.getLookup().lookup(AntProjectHelper.class);
        Properties filename = new Properties();
        filename.setProperty("destfile", f.getPath());
        filename.setProperty("classname", classname);
        filename.setProperty("javac.includes", classFileJava);
        try {
            FileObject buildImpl = helper.getProjectDirectory().getFileObject("build.xml");
            ActionUtils.runTarget(buildImpl, new String[]{"run-single"}, filename);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
