package main.java;

import core.simulation.Simulation;
import view.InterfaceRender;

public class Main {

    public static void main(String[] args) {
        // 逻辑层
        Simulation simulation = new Simulation();
        // 渲染层
        new InterfaceRender(simulation);
    }
}