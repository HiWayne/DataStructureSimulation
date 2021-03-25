package core.simulation;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import constant.Action;
import constant.Type;
import core.algorithm.*;
import view.Dispatch;

public class Simulation {
    private Dispatch dispatch = null;
    private ArrayList<Action> actionList = new ArrayList<Action>();
    // 待排序的数组
    private Integer[] list;
    // 已排序的数组
    private Integer[] sortedList;
    // 调度实例
    private Scheduler scheduler = null;
    // 播放动画频率，默认间隔800ms一次
    public static Integer intervalTime = 800;
    // 是否暂停
    public static Boolean isPause = false;
    // 用来作为定时器
    public static ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();

    public Simulation() {
        this.dispatch = new Dispatch();
    }

    public void setActionList(ArrayList<Action> actionList) {
        this.actionList = actionList;
    }

    // 输入数组后初始化。对数组完成排序并生成排序过程中的所有行为，并以此创建调度对象
    public void init() {
        // 如果之前已经有了scheduler，再重新初始化之前要先停止它
        if (this.scheduler != null) {
            this.setPause(true);
        }
        // 排序不影响原数组
        Integer[] clonedList = (Integer[]) this.list.clone();
        // actionList依赖注入
        BubbleSort bubbleSort = new BubbleSort(clonedList, this.actionList);
        Integer[] sortedList = bubbleSort.getList();
        this.sortedList = sortedList;
        // 准备对生成的actionList调度
        Scheduler scheduler = new Scheduler(this.actionList, this.dispatch);
        scheduler.setNextExecutedInstance(scheduler);
        this.scheduler = scheduler;
    }

    // 展示初始数组
    public void show() {
        this.scheduler.initShow(this.list);
    }

    // 播放
    public void play() {
        if (Simulation.isPause == true) {
            this.setPause(false);
        }
        this.scheduler.run();
    }

    // 暂停
    public void setPause(Boolean isPause) {
        Simulation.isPause = isPause;
    }

    // 重新设置数组
    public void setList(Integer[] list) {
        this.list = list;
    }

    // 设置仿真样式
    public void setRenderType(String type) {
        this.dispatch.setType(type);
    }

    public void setSpeed(Integer intervalTime) {
        Simulation.intervalTime = intervalTime;
    }
}

// 根据各种条件：速度、暂停等，调度actions依次"播放"
class Scheduler implements Runnable {
    // 描述一系列动画形式的列表
    private ArrayList<Action> actionList;
    // 当前播放进度
    private Integer currentIndex = 0;
    // 负责派发渲染的实例
    private Dispatch dispatch;
    private Scheduler nextExecutedInstance = null;


    public Scheduler(ArrayList<Action> actionList, Dispatch dispatch) {
        this.actionList = actionList;
        this.dispatch = dispatch;
    }

    public void initShow(Integer[] list) {
        Action action = new Action(Type.INIT, list);
        this.dispatch.dispatch(action);
    }

    public void run() {
        Integer actionListSize = this.actionList.size();
        if (this.currentIndex + 1 > actionListSize || Simulation.isPause == true) {
            if (Simulation.isPause == true) {
                System.out.println("暂停");
            } else {
                // 通知结束
                Action endAction = new Action(Type.END);
                this.dispatch.dispatch(endAction);
                System.out.println("结束");
            }
            return;
        }
        Action action = this.actionList.get(this.currentIndex);
        // 交给渲染层渲染action
        this.dispatch.dispatch(action);
        this.currentIndex++;

        if (this.nextExecutedInstance != null) {
            // 每隔 Simulation.intervalTime ms run方法被调用一次，即每隔一定时间遍历渲染action一次
            Simulation.timer.schedule(this.nextExecutedInstance, Simulation.intervalTime, TimeUnit.MILLISECONDS);
        }
    }

    public void setCurrentIndex(Integer currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void setNextExecutedInstance(Scheduler nextExecutedInstance) {
        this.nextExecutedInstance = nextExecutedInstance;
    }
}
