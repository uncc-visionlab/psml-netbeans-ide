/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bag3d.netbeans.psml.project;

import java.awt.Image;
import java.io.File;
import javax.swing.Action;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.netbeans.spi.project.ui.support.ProjectSensitiveActions;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author arwillis
 */
public class AntBasedProjectLogicalView implements LogicalViewProvider {

    private final AntBasedProject project;

    public AntBasedProjectLogicalView(AntBasedProject project) {
        this.project = project;
    }

    @Override
    public Node createLogicalView() {
        try {
            //Get the Text directory, creating if deleted
//            FileObject text2 = project.getSubFolder(AntBasedProject.PROJECT_JAVA_SRCDIR, true);
            FileObject text = project.getSubFolder(AntBasedProject.PROJECT_PSML_SRCDIR, true);

            //Get the DataObject that represents it
            DataFolder textDataObject = DataFolder.findFolder(text);

            //Get its default node-we'll wrap our node around it to change the
            //display name, icon, etc
            Node realTextFolderNode = textDataObject.getNodeDelegate();

            //This FilterNode will be our project node
            return new ProjectNode(realTextFolderNode, project);
        } catch (DataObjectNotFoundException donfe) {
            Exceptions.printStackTrace(donfe);
            //Fallback-the directory couldn't be created -
            //read-only filesystem or something evil happened
            return new AbstractNode(Children.LEAF);
        }
    }

    /**
     * This is the node you actually see in the project tab for the project
     */
    private static final class ProjectNode extends FilterNode {

        final AntBasedProject project;

        public ProjectNode(Node node, AntBasedProject project) throws DataObjectNotFoundException {
            super(node, new FilterNode.Children(node),
                    //The projects system wants the project in the Node's lookup.
                    //NewAction and friends want the original Node's lookup.
                    //Make a merge of both
                    new ProxyLookup(new Lookup[]{Lookups.singleton(project),
                        node.getLookup()
                    }));
            this.project = project;
        }

        @Override
        public Action[] getActions(boolean arg0) {
            Action[] nodeActions = new Action[]{
                CommonProjectActions.newFileAction(),
                //The 'null' indicates that the default icon will be used:
                ProjectSensitiveActions.projectCommandAction(ActionProvider.COMMAND_BUILD, "Build Project", null),
                CommonProjectActions.copyProjectAction(),
                CommonProjectActions.deleteProjectAction(),
                CommonProjectActions.setAsMainProjectAction(),
                CommonProjectActions.closeProjectAction()
            };
            return nodeActions;
        }

//        @Override
//        public Action[] getActions(boolean arg0) {
//            return new Action[]{
//                CommonProjectActions.newFileAction(),
//                CommonProjectActions.copyProjectAction(),
//                CommonProjectActions.deleteProjectAction(),
//                CommonProjectActions.closeProjectAction()
//            };
//        }

        @Override
        public Image getIcon(int type) {
            return ImageUtilities.loadImage(AntBasedProject.ICON_RESOURCE);
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }

        @Override
        public String getDisplayName() {
            return project.getProjectDirectory().getName();
        }

    }

//    @Override
//    public org.openide.nodes.Node createLogicalView() {
//        return new RootNode(project);
//    }
    private static final class RootNode extends AbstractNode {

        public static final String REGISTERED_NODE_LOCATION
                = "Projects/org-bag3d-netbeans-psml-project/Nodes";
        final AntBasedProject project;

        public RootNode(AntBasedProject project) {
            super(NodeFactorySupport.createCompositeChildren(project, REGISTERED_NODE_LOCATION), Lookups.singleton(project));
            this.project = project;
            setIconBaseWithExtension(AntBasedProject.ICON_RESOURCE);
        }

        @Override
        public Action[] getActions(boolean arg0) {
            Action[] nodeActions = new Action[7];
            nodeActions[0] = CommonProjectActions.newFileAction();
            //The 'null' indicates that the default icon will be used:
            nodeActions[1] = ProjectSensitiveActions.projectCommandAction(ActionProvider.COMMAND_BUILD, "Build Project", null);
            nodeActions[2] = CommonProjectActions.copyProjectAction();
            nodeActions[3] = CommonProjectActions.deleteProjectAction();
            nodeActions[5] = CommonProjectActions.setAsMainProjectAction();
            nodeActions[6] = CommonProjectActions.closeProjectAction();
            return nodeActions;
        }

        @Override
        public Image getIcon(int type) {
            return ImageUtilities.loadImage(AntBasedProject.ICON_RESOURCE);
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }

        @Override
        public String getDisplayName() {
            return project.getProjectDirectory().getName();
        }
    }

    @Override
    public Node findPath(Node root, Object target) {
        //leave unimplemented for now
        return null;
    }

}
