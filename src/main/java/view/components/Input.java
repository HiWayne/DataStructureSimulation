package view.components;

import javax.swing.*;

public class Input extends JComponent {
    JLabel label;
    JTextField textField;
    JButton button;
    JComponent wrapper = new JPanel();

    public Input(String label, Integer limit, String button) {
        this.label = new JLabel(label);
        this.textField = new JTextField(limit);
        this.button = new JButton(button);
        this.wrapper.add(this.label);
        this.wrapper.add(this.textField);
        this.wrapper.add(this.button);
    }

    public JComponent get() {
        return this.wrapper;
    }

    public void addTo(JComponent component) {
        component.add(this.wrapper);
    }
}
