/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bag3d.netbeans.psml.filetype;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.apache.tools.ant.module.api.support.ActionUtils;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.support.ant.AntProjectHelper;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.IOProvider;
import org.uncc.psml.editortools.PSML2Java;

@ActionID(
        category = "Build",
        id = "org.bag3d.netbeans.psml.filetype.PSMLBuildActionListener"
)
@ActionRegistration(
        displayName = "#CTL_PSMLBuildActionListener"
)
@ActionReference(path = "Loaders/text/x-psml/Actions", position = 150)
@Messages("CTL_PSMLBuildActionListener=Build")
public final class PSMLBuildActionListener implements ActionListener {

    private final PSMLDataObject context;

    public PSMLBuildActionListener(PSMLDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        FileObject f = context.getPrimaryFile();
        Project project = FileOwnerQuery.getOwner(f);
        AntProjectHelper helper = project.getLookup().lookup(AntProjectHelper.class);
        String fileName = f.getName();
        String fileExt = f.getNameExt();
        String pathStr = f.getPath();        
        FileObject projectFile = project.getProjectDirectory();
        String projectPathStr = projectFile.getPath();
        String classFile = pathStr.replace(projectPathStr+"/psml/", "");
        String classFileJava = classFile.substring(0, classFile.lastIndexOf(".")) + ".java";
        String classnameExt = classFile.replaceAll("/", ".");
        String classname = classnameExt.substring(0,classnameExt.lastIndexOf("."));
        Properties filename = new Properties();
        filename.setProperty("destfile", f.getPath());
        filename.setProperty("classname", classname);
        filename.setProperty("javac.includes", classFileJava);
        try {
            FileObject buildImpl = helper.getProjectDirectory().getFileObject("build.xml");
            ActionUtils.runTarget(buildImpl, new String[]{"compile-single"}, filename);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
