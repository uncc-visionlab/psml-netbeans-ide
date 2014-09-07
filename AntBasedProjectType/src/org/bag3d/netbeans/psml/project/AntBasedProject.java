/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bag3d.netbeans.psml.project;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.apache.tools.ant.module.api.support.ActionUtils;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.support.ant.AntBasedProjectRegistration;
import org.netbeans.spi.project.support.ant.AntProjectHelper;
import org.netbeans.spi.project.ui.support.DefaultProjectOperations;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author arwillis
 */
@AntBasedProjectRegistration(
        type = AntBasedProject.TYPE,
        iconResource = AntBasedProject.ICON_RESOURCE,
        sharedName = AntBasedProject.NAME_SHARED,
        sharedNamespace = AntBasedProject.NAME_SPACE_SHARED,
        privateName = AntBasedProject.NAME_PRIVATE,
        privateNamespace = AntBasedProject.NAME_SPACE_PRIVATE
)
public class AntBasedProject implements Project {

    public static final String TYPE = "org.bag3d.netbeans.psml.project";
    public static final String NAME_SPACE_SHARED = "http://visionlab.homelinux.org/ns/psml-project/1";
    public static final String NAME_SHARED = "data";
    public static final String NAME_PRIVATE = "project-private";
    public static final String NAME_SPACE_PRIVATE = "http://visionlab.homelinux.org/ns/psml-project-private/1";
    public static final String ICON_RESOURCE = "org/bag3d/netbeans/psml/project/resources/project_icon.png";

    public static final String PROJECT_PSML_SRCDIR = "psml";
    public static final String PROJECT_JAVA_SRCDIR = "java/src";
    final AntProjectHelper helper;
    
    public AntBasedProject(AntProjectHelper helper) {
        this.helper = helper;
    }

    @Override
    public Lookup getLookup() {
        return Lookups.fixed(new Object[]{
            this,
            helper,
            new Info(),
            new AntBasedProjectLogicalView(this),
            new AntBasedActionProvider()
        });
    }

    @Override
    public FileObject getProjectDirectory() {
        return helper.getProjectDirectory();
    }

    FileObject getSubFolder(String foldername, boolean create) {
        FileObject result
                = getProjectDirectory().getFileObject(foldername);
        if (result == null && create) {
            try {
                result = getProjectDirectory().createFolder(foldername);
            } catch (IOException ioe) {
                Exceptions.printStackTrace(ioe);
            }
        }
        return result;
    }

    private final class Info implements ProjectInformation {

        @Override
        public String getName() {
            return helper.getProjectDirectory().getName();
        }

        @Override
        public String getDisplayName() {
            return getName();
        }

        @Override
        public Icon getIcon() {
            return new ImageIcon(ImageUtilities.loadImage(
                    ICON_RESOURCE));
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener pcl) {
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener pcl) {
        }

        @Override
        public Project getProject() {
            return AntBasedProject.this;
        }
    }

    private final class AntBasedActionProvider implements ActionProvider {

        private final String[] supported = new String[]{
            ActionProvider.COMMAND_DELETE,
            ActionProvider.COMMAND_COPY,
            ActionProvider.COMMAND_BUILD,
            ActionProvider.COMMAND_CLEAN,
            ActionProvider.COMMAND_COMPILE_SINGLE,
            ActionProvider.COMMAND_RUN,
            ActionProvider.COMMAND_RUN_SINGLE
        };

        @Override
        public String[] getSupportedActions() {
            return supported;
        }

        @Override
        public void invokeAction(String string, Lookup lookup) throws IllegalArgumentException {
            if (string.equals(ActionProvider.COMMAND_DELETE)) {
                DefaultProjectOperations.performDefaultDeleteOperation(AntBasedProject.this);
            }
            if (string.equals(ActionProvider.COMMAND_COPY)) {
                DefaultProjectOperations.performDefaultCopyOperation(AntBasedProject.this);
            }
            //Here we find the Ant script and call the target we need!
            if (string.equals(ActionProvider.COMMAND_BUILD)) {
                try {
                    FileObject buildImpl = helper.getProjectDirectory().getFileObject("build.xml");
                    ActionUtils.runTarget(buildImpl, new String[]{"compile"}, null);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            if (string.equals(ActionProvider.COMMAND_CLEAN)) {
                try {
                    FileObject buildImpl = helper.getProjectDirectory().getFileObject("build.xml");
                    ActionUtils.runTarget(buildImpl, new String[]{"clean"}, null);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }            
            if (string.equals(ActionProvider.COMMAND_COMPILE_SINGLE)) {
                try {
                    FileObject buildImpl = helper.getProjectDirectory().getFileObject("build.xml");
                    ActionUtils.runTarget(buildImpl, new String[]{"compile-single"}, null);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }            
            if (string.equals(ActionProvider.COMMAND_RUN)) {
                try {
                    FileObject buildImpl = helper.getProjectDirectory().getFileObject("build.xml");
                    ActionUtils.runTarget(buildImpl, new String[]{"run"}, null);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }            
            if (string.equals(ActionProvider.COMMAND_RUN_SINGLE)) {
                try {
                    FileObject buildImpl = helper.getProjectDirectory().getFileObject("build.xml");
                    ActionUtils.runTarget(buildImpl, new String[]{"run-single"}, null);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }            
        }

        @Override
        public boolean isActionEnabled(String command, Lookup lookup) throws IllegalArgumentException {
            if ((command.equals(ActionProvider.COMMAND_DELETE))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_COPY))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_BUILD))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_CLEAN))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_COMPILE_SINGLE))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_RUN))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_RUN_SINGLE))) {
                return true;
            } else {
                throw new IllegalArgumentException(command);
            }
        }
    }
}
