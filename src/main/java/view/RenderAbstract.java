package view;

import constant.Action;

import javax.swing.*;

public abstract class RenderAbstract {
    public RenderAbstract() {
    }

    public abstract void render(Action action, JFrame frame);
    public abstract void uninstall(JFrame frame);
}
