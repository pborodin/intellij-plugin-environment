package model;

import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.util.DisposeAwareRunnable;

import java.io.*;
import java.util.Properties;

public class Util {
    public static final String NOTIFICATIONS_TOPIC = "HolyProjectProcesses";

    public static void runWhenInitialized(final Project project, final Runnable r) {
        if (project.isDisposed()) return;

        if (!project.isInitialized()) {
            StartupManager.getInstance(project).registerPostStartupActivity(DisposeAwareRunnable.create(r, project));
            return;
        }
        runDumbAware(project, r);
    }

    public static void runDumbAware(final Project project, final Runnable r) {
        if (DumbService.isDumbAware(r)) {
            r.run();
        } else {
            DumbService.getInstance(project).runWhenSmart(DisposeAwareRunnable.create(r, project));
        }
    }

    public static String loadAppEnvProperty(Project project) {
        String env;
        try (InputStream in = new FileInputStream(project.getBasePath() + "/src/main/resources/application.properties")) {
            Properties prop = new Properties();
            prop.load(in);
            env = prop.getProperty("environment");
        } catch (IOException e) {
            return "undefined";
        }
        if(env == null) return "undefined";
        return env;
    }

    public static void saveAppEnvProperty(Project project, String env) {
        File file = new File(project.getBasePath() + "/src/main/resources/application.properties");
        Properties props = new Properties();

        try {
            file.createNewFile(); // if file already exists will do nothing
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileInputStream in = new FileInputStream(file)) {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (OutputStream out = new FileOutputStream(file)) {
            props.setProperty("environment", env);
            props.store(out, "EnvPlugin - store [environment] property: " + env);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
