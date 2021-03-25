package view;

import java.util.HashMap;
import constant.Action;
import view.SimulationRenderStrategy.DefaultSimulationRender;

import javax.swing.*;

// 仿真过程渲染类
public class SimulationRender {
    private HashMap<String, RenderAbstract> simulationRenderHashMap = new HashMap();
    private RenderAbstract currentRenderInstance = null;
    private String type = null;

    public SimulationRender() {
        DefaultSimulationRender defaultSimulationRender = new DefaultSimulationRender();

        this.simulationRenderHashMap.put("DEFAULT", defaultSimulationRender);
    }

    public void render(Action action, String type) {

        // 不是第一次渲染并且渲染类型被改变了，此时需要卸载之前的JPanel
        if (this.type != null && this.type != type) {
            this.currentRenderInstance.uninstall(SimulationRender.frame);
        }
        this.type = type;
        RenderAbstract renderInstance = this.simulationRenderHashMap.get(type);
        this.currentRenderInstance = renderInstance;
        renderInstance.render(action, SimulationRender.frame);
    }

    static JFrame frame;
    static void setFrame(JFrame frame) {
        SimulationRender.frame = frame;
    }
}


