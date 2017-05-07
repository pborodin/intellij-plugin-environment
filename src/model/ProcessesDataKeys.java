package model;

import com.intellij.openapi.actionSystem.DataKey;
import ui.EnvironmentTreeStructure;

import javax.swing.JTree;

public class ProcessesDataKeys {
    public static final DataKey<JTree> PROCESSES_PROJECTS_TREE = DataKey.create("PROCESSES_PROJECTS_TREE");
    public static final DataKey<EnvironmentTreeStructure> PROCESSES_PROJECTS_TREE_STRUCTURE = DataKey.create("PROCESSES_PROJECTS_TREE_STRUCTURE");
}