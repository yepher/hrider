package hrider.ui.design;

import hrider.hbase.Connection;

import java.awt.Color;
import java.awt.Component;
import java.io.IOException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class JListRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = 8219559461829225540L;

    private Connection connection;

    public JListRenderer(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof String) {
            try {
                boolean enabled = connection.tableEnabled((String)value);
                if (!enabled) {
                    setForeground(Color.gray);
                }
            }
            catch (IOException ignore) {
            }
        }
        return component;
    }
}
