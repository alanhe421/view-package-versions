package cn.alanhe;

import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PackageActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Browser browser = new Browser();
        ComponentPopupBuilder componentPopupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(browser, null);
        componentPopupBuilder
                .setResizable(true)
                .setMovable(true);
        JBPopup popup = componentPopupBuilder.createPopup();
        popup.setMinimumSize(new Dimension(500, 500));
        popup.showInFocusCenter();
    }
}
