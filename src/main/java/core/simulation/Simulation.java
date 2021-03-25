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
    public static Long intervalTime;

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
            this.scheduler.setPause(true);
        }
        // actionList依赖注入
        BubbleSort bubbleSort = new BubbleSort(this.list, this.actionList);
        Integer[] sortedList = bubbleSort.getList();
        this.sortedList = sortedList;
        Scheduler scheduler = new Scheduler(this.actionList, this.dispatch);
        scheduler.setNextExecutedInstance(scheduler);
        this.scheduler = scheduler;
        this.initShow();
    }

    // 展示初始数组
    public void initShow() {
        this.scheduler.setPause(true);
        this.scheduler.initShow(this.sortedList);
    }

    // 播放
    public void play() {
        this.scheduler.setPause(false);
        this.scheduler.run();
    }

    // 暂停
    public void pause() {
        this.scheduler.setPause(true);
    }

    // 重新设置数组
    public void setList(Integer[] list) {
        this.list = list;
    }

    // 设置仿真样式
    public void setRenderType(String type) {
        this.dispatch.setType(type);
    }

    public void setSpeed(Long intervalTime) {
        this.scheduler.setIntervalTime(intervalTime);
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
    // 播放动画频率，默认间隔800ms一次
    private long intervalTime = 800;
    // 是否暂停
    private Boolean isPause = false;
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
        if (this.currentIndex + 1 > actionListSize || this.isPause == true) {
            if (this.isPause == true) {
                System.out.println("暂停");
            } else {
                System.out.println("结束");
            }
            return;
        }
        Action action = this.actionList.get(this.currentIndex);
        this.dispatch.dispatch(action);
        this.currentIndex++;
        Scheduler scheduler = new Scheduler(this.actionList, this.dispatch);
        scheduler.setCurrentIndex(this.currentIndex);
        ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
        if (this.nextExecutedInstance != null) {
            timer.schedule(this.nextExecutedInstance, this.intervalTime, TimeUnit.MILLISECONDS);
        }
    }

    public void setCurrentIndex(Integer currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void setPause(Boolean isPause) {
        this.isPause = isPause;
    }

    public void setNextExecutedInstance(Scheduler nextExecutedInstance) {
        this.nextExecutedInstance = nextExecutedInstance;
    }

    public void setIntervalTime(Long intervalTime) {
        this.intervalTime = intervalTime;
    }
}
