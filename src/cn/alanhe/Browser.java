package cn.alanhe;

import com.sun.javafx.application.PlatformImpl;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.swing.*;

public class Browser extends JPanel {

    private Stage stage;
    private WebView browser;
    private JFXPanel jfxPanel;
    private WebEngine webEngine;

    public Browser() {
        initComponents();
    }


    private void initComponents() {
        jfxPanel = new JFXPanel();
        createScene();
        add(jfxPanel);
    }

    private void createScene() {
        PlatformImpl.startup(() -> {
            stage = new Stage();
            stage.setResizable(true);

            Group root = new Group();
            Scene scene = new Scene(root, 80, 20);
            stage.setScene(scene);

            // Set up the embedded browser:
            browser = new WebView();
            webEngine = browser.getEngine();
            webEngine.load("http://www.google.com");

            ObservableList<Node> children = root.getChildren();
            children.add(browser);
            jfxPanel.setScene(scene);
        });
    }
}
