package ui;

import com.intellij.openapi.wm.CustomStatusBarWidget;
import com.intellij.openapi.wm.StatusBar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by Pavel on 07.05.2017.
 */
public class EnvStatusBarWidget implements CustomStatusBarWidget {
    public static final String id = EnvStatusBarWidget.class.getName();
    private JLabel myLabel = new JLabel(" [sbrf.rtk.proxy] ");

    @Override
    public JComponent getComponent() {
        return myLabel;
    }

    @NotNull
    @Override
    public String ID() {
        return id;
    }

    @Nullable
    @Override
    public WidgetPresentation getPresentation(@NotNull PlatformType platformType) {
        return null;
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {

    }

    @Override
    public void dispose() {

    }
}
