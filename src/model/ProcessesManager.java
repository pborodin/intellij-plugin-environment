package model;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.openapi.wm.ex.ToolWindowManagerEx;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;
import ui.EnvStatusBarWidget;
import ui.EnvironmentPanel;

import java.io.*;
import java.util.Properties;

public class ProcessesManager extends AbstractProjectComponent {
    private final EnvironmentPanel panel;

    protected ProcessesManager(@NotNull final Project project) {
        super(project);
        panel = new EnvironmentPanel(project);
    }

    @Override
    public void initComponent() {
        super.initComponent();
        Util.runWhenInitialized(myProject, this::initToolWindow);
    }

    private void initToolWindow() {
        final ToolWindowManagerEx manager = ToolWindowManagerEx.getInstanceEx(myProject);
        ToolWindowEx myToolWindow = (ToolWindowEx) manager.registerToolWindow(EnvironmentPanel.ID, false, ToolWindowAnchor.RIGHT, myProject, true);
        myToolWindow.setIcon(AllIcons.Gutter.Colors);
        final ContentFactory contentFactory = ServiceManager.getService(ContentFactory.class);
        final Content content = contentFactory.createContent(panel, "", false);
        ContentManager contentManager = myToolWindow.getContentManager();
        contentManager.addContent(content);

        StatusBar statusBar = WindowManager.getInstance().getStatusBar(myProject);
        statusBar.addWidget(new EnvStatusBarWidget(Util.loadAppEnvProperty(myProject)), "after Encoding");
    }
}
