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
                    Rect rect = new Rect(list, Rect.initialColor);
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

class RectInfo implements Cloneable {
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

    @Override
    public RectInfo clone() {
        RectInfo rectInfo = null;
        try {
            rectInfo = (RectInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return rectInfo;
    }
}

// 绘制长方形
class Rect extends JPanel {
    static Color initialColor = Color.decode("#2db7f5");
    static Color moveToColor = Color.decode("#FF5733");
    static Color compareColor = Color.decode("#FEA592");
    Integer[] list;
    ArrayList<RectInfo> rectInfoList = new ArrayList<RectInfo>();
    Color color;

    public Rect(Integer[] list, Color color) {
        super();
        this.list = list;
        this.color = color;
        this.createPaintInfo(list, color);
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
        ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
        PositionOffset positionOffset = new PositionOffset(timer, this, this.rectInfoList);
        System.out.println(type);
        switch (type) {
            case MOVETOANDCOMPARE:
                // 在改变当前节点颜色之前，先把所有的还原成初始颜色
                this.resetColors();
                Integer moveToIndex = action.fromIndex;
                Integer compareIndex = action.toIndex;
                RectInfo moveToRect = this.rectInfoList.get(moveToIndex);
                RectInfo compareRect = this.rectInfoList.get(compareIndex);
                moveToRect.setColor(Rect.moveToColor);
                compareRect.setColor(Rect.compareColor);
                this.repaint();
                break;
            case SWAP:
                Integer firstIndex = action.fromIndex;
                Integer secondIndex = action.toIndex;
                positionOffset.setIndexes(firstIndex, secondIndex);
                timer.scheduleAtFixedRate(positionOffset, 0, 1000 / 60, TimeUnit.MILLISECONDS);
                break;
            case END:
                this.resetColors();
                this.repaint();
                break;
        }
    }

    public void createPaintInfo(Integer[] list, Color color) {
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

    private void resetColors() {
        for (Integer i = 0; i < this.list.length; i++) {
            RectInfo rectInfo = this.rectInfoList.get(i);
            rectInfo.setColor(this.color);
        }
    }
}

// 每隔一帧的时间位置偏移一次（负责交换动画）
class PositionOffset implements Runnable {
    private ScheduledExecutorService timer;
    private ArrayList<RectInfo> rectInfoList;
    private JPanel jpanel;
    private Integer firstIndex;
    private Integer secondIndex;
    private RectInfo firstRect;
    private RectInfo secondRect;
    private Integer currentX1;
    private Integer currentX2;
    private Integer distance;
    private Integer step;
    private Integer count;
    private Integer currentCount = 0;
    private final Integer fps = 60;
    private final Integer intervalTime = Simulation.intervalTime;
    private final Integer time = Simulation.intervalTime / 2;
    // firstIndex到secondIndex的方向
    private Boolean positiveOrder;
    // 原始数据
    private RectInfo originFirstRect;
    private RectInfo originSecondRect;
    // 判断是否已经停止
    private Boolean hasStop = false;

    public PositionOffset(ScheduledExecutorService timer, JPanel jpanel, ArrayList<RectInfo> rectInfoList) {
        this.timer = timer;
        this.rectInfoList = rectInfoList;
        this.jpanel = jpanel;
    }

    public void setIndexes(Integer firstIndex, Integer secondIndex) {
        this.firstIndex = firstIndex;
        this.secondIndex = secondIndex;
        this.firstRect = rectInfoList.get(firstIndex);
        this.secondRect = rectInfoList.get(secondIndex);
        this.originFirstRect = this.firstRect.clone();
        this.originSecondRect = this.secondRect.clone();
        this.currentX1 = this.firstRect.getX();
        this.currentX2 = this.secondRect.getX();
        // 第一个位置在第二个左边即记为正向
        if (this.currentX2 > this.currentX1) {
            this.positiveOrder = true;
        } else {
            this.positiveOrder = false;
        }
        this.distance = Math.abs(this.currentX2 - this.currentX1);
        Integer count = this.time / (1000 / this.fps);
        this.count = count;
        this.step = Math.round(this.distance / count);
    }

    @Override
    public void run() {
        if (this.currentCount < this.count) {
            if (this.positiveOrder) {
                this.currentX1 += this.step;
                this.currentX2 -= this.step;
            } else {
                this.currentX1 -= this.step;
                this.currentX2 += this.step;
            }
            this.firstRect.setX(this.currentX1);
            this.secondRect.setX(this.currentX2);
            this.currentCount++;
        } else {
            if (this.hasStop) {
                return;
            }
            this.timer.shutdownNow();
            this.originFirstRect.setColor(Rect.initialColor);
            this.originSecondRect.setColor(Rect.initialColor);
            Integer secondX = this.originSecondRect.getX();
            Integer firstX = this.originFirstRect.getX();
            this.originFirstRect.setX(secondX);
            this.originSecondRect.setX(firstX);
            // 上面只是交换了位置信息，这里要将交换过位置的元素在数组中的位置也交换一下
            this.rectInfoList.set(this.firstIndex, this.originSecondRect);
            this.rectInfoList.set(this.secondIndex, this.originFirstRect);
            this.hasStop = true;
        }
        this.jpanel.repaint();
    }
}