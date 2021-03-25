package view.SimulationRenderStrategy;

import constant.Action;
import constant.Type;
import core.simulation.Simulation;
import view.RenderAbstract;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// 默认仿真样式
public class DefaultSimulationRender extends RenderAbstract {
    private Boolean isInit = false;
    private Rect rect = null;

    public DefaultSimulationRender() {
    }

    public void render(Action action, JFrame frame) {
        switch (action.type) {
            case INIT:
                if (this.isInit == false) {
                    Integer[] list = action.list;
                    Rect rect = new Rect(list, Color.decode("#2db7f5"));
                    this.rect = rect;
                    frame.add(rect);
                    this.isInit = true;
                }
                break;
            default:
                this.rect.animate(action);
                break;
        }
        return;
    }

    public void uninstall(JFrame frame) {
        if (this.rect != null) {
            frame.remove(this.rect);
        }
    }
}

class RectInfo {
    private Integer x = null;
    private Integer y = null;
    private Integer width = null;
    private Integer height = null;
    private Color color = null;

    public RectInfo(Integer x, Integer y, Integer width, Integer height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public Integer getX() {
        return this.x;
    }

    public Integer getY() {
        return this.y;
    }

    public Integer getWidth() {
        return this.width;
    }

    public Integer getHeight() {
        return this.height;
    }

    public Color getColor() {
        return this.color;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}

// 绘制长方形
class Rect extends JPanel {
    static Color moveToColor = Color.decode("#FF5733");
    static Color compareColor = Color.decode("#FEA592");
    Integer[] list;
    ArrayList<RectInfo> rectInfoList = new ArrayList<RectInfo>();
    Color color;

    public Rect(Integer[] list, Color color) {
        super();
        this.setList(list, color);
        this.color = color;
    }

    public void paint(Graphics ctx) {

        super.paint(ctx);
        Graphics2D ctx2d = (Graphics2D) ctx;
        Integer length = this.rectInfoList.size();
        for (Integer i = 0; i < length; i++) {
            RectInfo rectInfo = this.rectInfoList.get(i);
            Integer x = rectInfo.getX();
            Integer y = rectInfo.getY();
            Integer width = rectInfo.getWidth();
            Integer height = rectInfo.getHeight();
            Color color = rectInfo.getColor();
            ctx2d.setColor(color);
            ctx2d.fillRect(x, y, width, height);
        }

    }

    public void animate(Action action) {
        Type type = action.type;
        switch (type) {
            case MOVETOANDCOMPARE:
                for (Integer i = 0; i < this.list.length; i++) {
                    RectInfo rectInfo = this.rectInfoList.get(i);
                    rectInfo.setColor(this.color);
                }
                Integer moveToIndex = action.fromIndex;
                Integer compareIndex = action.toIndex;
                RectInfo moveToRect = this.rectInfoList.get(moveToIndex);
                RectInfo compareRect = this.rectInfoList.get(compareIndex);
                moveToRect.setColor(Rect.moveToColor);
                compareRect.setColor(Rect.compareColor);
                this.repaint();
                break;
            case SWAP:
                Long intervalTime = Simulation.intervalTime;
                Long time = intervalTime / 2;
                final Integer fps = 60;
                Integer firstIndex = action.fromIndex;
                Integer secondIndex = action.toIndex;
                RectInfo rectInfo1 = this.rectInfoList.get(firstIndex);
                RectInfo rectInfo2 = this.rectInfoList.get(secondIndex);
                ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
                timer.scheduleAtFixedRate(new PositionOffset(timer, time, fps, rectInfo1.getX(), rectInfo2.getX()), 0, 1000 / 60, TimeUnit.MILLISECONDS);
        }
    }

    private class PositionOffset implements Runnable {
        private Long time;
        private Integer fps;
        private Integer currentX1;
        private Integer currentX2;
        private Integer distance;
        private Double step;
        private ScheduledExecutorService timer;

        public PositionOffset(ScheduledExecutorService timer, Long time, Integer fps, Integer x1, Integer x2) {
            this.timer = timer;
            this.time = time;
            this.fps = fps;
            this.currentX1 = x1;
            this.currentX2 = x2;
            this.distance = Math.abs(x2 - x1);
            Long count = this.time / (1000 / this.fps);
            this.step = Double.valueOf(this.distance / count);
        }

        @Override
        public void run() {
            if (this.currentX1 < this.currentX2) {
                this.currentX1 += this.step;
            }
        }
    }

    public void setList(Integer[] list, Color color) {
        // 长方形宽度
        final Integer rectWidth = 40;
        // 基础高度
        final Integer basicHeight = 20;
        // 长方形之间间隙
        final Integer space = 10;
        // 底部间隙
        final Integer bottomSpace = 20;
        // 1152是整个窗口宽度，为了保持整体居中，要算出基础x
        final Integer basicX = (1152 - (list.length * (rectWidth + space) - space)) / 2;

        this.list = list;
        for (Integer i = 0; i < this.list.length; i++) {
            Integer number = list[i];
            Integer width = rectWidth;
            Integer height = number * basicHeight;
            Integer x = basicX + i * (rectWidth + space);
            // 488是画布区域的高度
            Integer y = 488 - bottomSpace - height;
            RectInfo rectInfo = new RectInfo(x, y, width, height, color);
            this.rectInfoList.add(rectInfo);
        }
    }

    private void setColor(Color color, Integer index) {
        RectInfo rectInfo = this.rectInfoList.get(index);
        rectInfo.setColor(color);
    }

    private void setPosition(Integer x, Integer y, Integer index) {
        RectInfo rectInfo = this.rectInfoList.get(index);
        rectInfo.setX(x);
        rectInfo.setY(y);
    }
}