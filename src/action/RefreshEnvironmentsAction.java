package action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.Project;

/**
 * Обновляет список доступных окружений из папки /resources/environments
 */
public class RefreshEnvironmentsAction extends ToggleAction {
    public static boolean selected;

    @Override
    public boolean isSelected(AnActionEvent e) {
        return selected;
    }

    @Override
    public void setSelected(AnActionEvent e, boolean state) {
        Project project = e.getData(PlatformDataKeys.PROJECT);

//        EnvironmentTreeStructure treeStructure = e.getData(ProcessesDataKeys.PROCESSES_PROJECTS_TREE_STRUCTURE);
//        if (treeStructure != null) treeStructure.updateTreeFromProject();
//        if (project != null) {
//            File file = new File(project.getBasePath() + "/src/main/resources/environments");
//            String[] directories = file.list();
//
//            Messages.showMessageDialog(project,
//                    "Project name: " + project.getName() +
//                            "\nBasePath: " + project.getBasePath() +
//                            "\n" + Arrays.toString(directories),
//                    "Information", Messages.getInformationIcon());
//        }
        selected = false;
//        Notifications.Bus.notify(new Notification(NOTIFICATIONS_TOPIC, "header", "New state is " + state, NotificationType.INFORMATION));
    }
}
