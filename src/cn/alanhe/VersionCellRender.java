package cn.alanhe;

import javax.swing.*;
import java.awt.*;

public class VersionCellRender implements ListCellRenderer {
    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
                isSelected, cellHasFocus);
        if (index == 0) {
            renderer.setText(value + "  (latest)");
        }
        return renderer;
    }
}
