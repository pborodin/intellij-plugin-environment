package ui;

import com.intellij.icons.AllIcons;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.IndexComparator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.treeStructure.*;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.InputEvent;
import java.io.File;
import java.util.Comparator;

import static ui.EnvironmentSate.ACTIVE;
import static ui.EnvironmentSate.INACTIVE;

public class EnvironmentTreeStructure extends SimpleTreeStructure {
    private final RootNode rootNode;
    private final SimpleTreeBuilder myTreeBuilder;

    public EnvironmentTreeStructure(Project project, SimpleTree tree) {
        this.rootNode = new RootNode(project);
        myTreeBuilder = new SimpleTreeBuilder(tree, (DefaultTreeModel) tree.getModel(), this, new Comparator<SimpleNode>() {
            @Override
            public int compare(SimpleNode o1, SimpleNode o2) {
                if (o1 instanceof NamedNode && o2 instanceof NamedNode)
                    return o1.getName().compareTo(o2.getName());
                else
                    return IndexComparator.INSTANCE.compare(o1, o2);
            }
        });
        configureTree(tree);
        Disposer.register(project, myTreeBuilder);
        myTreeBuilder.initRoot();
        myTreeBuilder.expand(rootNode, null);
    }

    private void configureTree(final SimpleTree tree) {
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);

        MavenUIUtil.installCheckboxRenderer(tree, new MavenUIUtil.CheckboxHandler() {
            @Override
            public void toggle(TreePath treePath, InputEvent e) {
                FileNode node = (FileNode) tree.getNodeFor(treePath);
                if (node != null) {
                    node.setState(ACTIVE);
                    tree.updateUI();
                }
            }

            @Override
            public boolean isVisible(Object userObject) {
                return userObject instanceof FileNode;
            }

            @Override
            public MavenUIUtil.CheckBoxState getState(Object userObject) {
                EnvironmentSate state = ((FileNode) userObject).getState();
                switch (state) {
                    case INACTIVE:
                        return MavenUIUtil.CheckBoxState.UNCHECKED;
                    case ACTIVE:
                        return MavenUIUtil.CheckBoxState.CHECKED;
                }
                return MavenUIUtil.CheckBoxState.UNCHECKED;
            }
        });
    }

    @Override
    public Object getRootElement() {
        return rootNode;
    }

    public abstract class NamedNode extends CachingSimpleNode {

        protected NamedNode(SimpleNode aParent, String name) {
            super(aParent);
            myName = name;
        }

        protected NamedNode(Project project, SimpleNode aParent, String name) {
            super(project, aParent);
            myName = name;
        }
    }

    public class RootNode extends NamedNode {
        private Project project;
        private FileNode activeNode;

        public RootNode(Project project) {
            super(project, null, "Root");
            this.project = project;
        }

        @Override
        protected SimpleNode[] buildChildren() {
            if (project == null) return new SimpleNode[0];
            File folder = new File(project.getBasePath() + "/src/main/resources/environments");
            return buildNodes(folder, this);
        }

        public FileNode getActiveNode() {
            return activeNode;
        }

        public void setActiveNode(FileNode activeNode) {
            this.activeNode = activeNode;
        }
    }

    private SimpleNode[] buildNodes(File folder, SimpleNode parentNode) {
        SimpleNode[] emptyNode = new SimpleNode[0];

        File[] files = folder.listFiles();
        if (files == null || files.length == 0) return emptyNode;

        SimpleNode[] nodes = new SimpleNode[files.length];
        for (int i = 0; i < nodes.length; i++) {
            if (files[i].isDirectory()) {
                nodes[i] = new FolderNode(parentNode, files[i].getName(), files[i]);
            } else {
                nodes[i] = new FileNode(parentNode, files[i].getName());
            }
        }
        return nodes;
    }

    public class FolderNode extends NamedNode {
        private File folder;

        public FolderNode(SimpleNode aParent, String name, File folder) {
            super(aParent, name);
            this.folder = folder;
            myClosedIcon = AllIcons.Nodes.Folder;
        }

        @Override
        protected SimpleNode[] buildChildren() {
            return buildNodes(folder, this);
        }
    }

    public class FileNode extends NamedNode {
        private EnvironmentSate state = INACTIVE;

        public FileNode(SimpleNode aParent, String name) {
            super(aParent, name);
            myName = myName.replace(".properties", "");
            updatePresentation();
        }

        private void updatePresentation() {
            PresentationData presentation = getPresentation();
            presentation.clear();
            presentation.addText(myName, SimpleTextAttributes.REGULAR_ATTRIBUTES);
        }

        @Override
        public void handleDoubleClickOrEnter(SimpleTree tree, InputEvent inputEvent) {
            updatePresentation();
        }

        @Override
        protected SimpleNode[] buildChildren() {
            return NO_CHILDREN;
        }

        EnvironmentSate getState() {
            return state;
        }

        void setState(EnvironmentSate state) {
            this.state = state;
            if (state == ACTIVE) {
                FileNode rootActiveChildNode = rootNode.getActiveNode();
                if (rootActiveChildNode != null && rootActiveChildNode != this) {
                    rootActiveChildNode.setState(INACTIVE);
                    rootActiveChildNode.updatePresentation();
                }
                rootNode.setActiveNode(this);
                StatusBar statusBar = WindowManager.getInstance().getStatusBar(myProject);
                EnvStatusBarWidget widget = (EnvStatusBarWidget) statusBar.getWidget(EnvStatusBarWidget.class.getName());
                if (widget != null) {
                    ((JLabel) widget.getComponent()).setText(" [" + this.getName() + "] ");
                }
            }
            updatePresentation();
        }
    }
}
