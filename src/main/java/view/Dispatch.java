package view;

import constant.Action;

// 负责解耦逻辑层和渲染层，通过dispatch action描绘页面
public class Dispatch {
    private SimulationRender simulationRender;
    private String type = "DEFAULT";

    public Dispatch() {
        this.simulationRender = new SimulationRender();
    }

    public void dispatch(Action action) {
        this.simulationRender.render(action, this.type);
    }

    public void setType(String type) {
        this.type = type;
    }

}
