package ui;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.ui.treeStructure.SimpleTreeStructure;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;
import model.ProcessesDataKeys;

public class EnvironmentPanel extends SimpleToolWindowPanel {
    public static final String ID = "Environment";

    private SimpleTree myTree;
    private SimpleTreeStructure myStructure;

    public EnvironmentPanel(Project project) {
        super(true, true);
        final ActionManager actionManager = ActionManager.getInstance();
        ActionToolbar actionToolbar = actionManager.createActionToolbar("Environment Processes Toolbar",
                (DefaultActionGroup) actionManager.getAction("Environment.ProcessesToolbar"), true);
        setToolbar(actionToolbar.getComponent());
        myTree = new SimpleTree();
        myTree.setRootVisible(false);
        myStructure = new EnvironmentTreeStructure(project,myTree);
        setContent(ScrollPaneFactory.createScrollPane(myTree));

    }

    @Nullable
    public Object getData(@NonNls String dataId) {
        if (ProcessesDataKeys.PROCESSES_PROJECTS_TREE.is(dataId)) {
            return myTree;
        }
        if (ProcessesDataKeys.PROCESSES_PROJECTS_TREE_STRUCTURE.is(dataId)) {
            return myStructure;
        }
        return super.getData(dataId);
    }
}
