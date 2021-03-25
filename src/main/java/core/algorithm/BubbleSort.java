package core.algorithm;

import constant.Action;
import constant.Type;

import java.util.ArrayList;
import java.util.Arrays;
// import java.util.Timer;
// import java.util.TimerTask;

// 冒泡排序
public class BubbleSort {
    private Integer[] sortedList;

    // actionList控制反转，维护了用来描述视图变化的action数组
    public BubbleSort(Integer[] list, ArrayList<Action> actionList) {
        Integer[] sortedList = this.sort(list, actionList);
        this.sortedList = sortedList;
    }

    private Integer[] sort(Integer[] list, ArrayList<Action> actionList) {
        /*
         * 内层循环每次能找出当前乱序区的极值，它会冒泡到末尾。外层循环每次缩小乱序区的范围
         */
        System.out.println("排序前：" + Arrays.toString(list));
        for (int l = list.length - 1; l > 0; l--) {
            for (int i = 0; i < l; i++) {
                // 当前走到i并和i+1比较 MOVETOANDCOMPARE
                Action moveAndCompareAction = new Action(Type.MOVETOANDCOMPARE, i, i + 1);
                actionList.add(moveAndCompareAction);
                if (list[i] > list[i + 1]) {
                    // 交换 SWAP
                    Integer value = list[i];
                    list[i] = list[i + 1];
                    list[i + 1] = value;
                    Action swapAction = new Action(Type.SWAP, i, i + 1, list[i], list[i + 1]);
                    actionList.add(swapAction);
                }
            }
        }
        System.out.println("排序后：" + Arrays.toString(list));
        return list;
        // this.asyncSort(list);
    }

    public Integer[] getList() {
        return this.sortedList;
    }

    // 异步排序
    // 分析：
    // 原本的排序算法是一个连续的、不可打断的过程，但需求要将每次排序的操作可视化（仿真）
    // 即每次操作都要映射成视图的变化，且速度可调节、可中断可恢复
    // 这就要求每个循环级别的原子操作是相互异步的，具体实现起来就是下一步操作作为这一步的异步回调
    // private void asyncSort(Integer[] list) {
    // // 需要比较的乱序区终点
    // int endpoint = list.length - 1;
    // // 当前比较的位置
    // int cuurentIndex = 0;
    // Timer timer = new Timer(true);
    // TimerTask task = new TimerTask() {
    // public void run() {
    // // 这个逻辑分支是内层循环，在同一个乱序区冒泡
    // if (cuurentIndex < endpoint) {
    // this.exchange(list, cuurentIndex);
    // cuurentIndex++;
    // timer.schedule(task, 500);
    // } else {
    // // 这个逻辑分支是外层循环
    // // 缩小乱序区的范围，因为内层循环结束后已经通过冒泡确定了一个极值，下次再找极值就不需要带上它了
    // endpoint--;
    // // 初始化内层循环，为新的乱序区重新冒泡做准备
    // cuurentIndex = 0;
    // if (endpoint > 0) {
    // // 在新的乱序区重新冒泡
    // timer.schedule(task, 500);
    // } else {
    // // 乱序区已经没了，整个数组全部有序
    // this.compete(list);
    // return;
    // }
    // }
    // }

    // // 数组中元素交换
    // // 它是冒泡排序的实质性步骤，也是可视化（仿真）视图变化的主要因素
    // private void exchange(Integer[] list, int i) {
    // if (list[i] > list[i + 1]) {
    // Integer value = list[i];
    // list[i] = list[i + 1];
    // list[i + 1] = value;
    // }
    // }

    // // 结束
    // private void compete(Integer[] list) {

    // }
    // };

    // timer.schedule(task, 500);

    // }

}