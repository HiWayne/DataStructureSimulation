import java.util.Timer;
import java.util.TimerTask;

// 冒泡排序
public class BubbleSort {

  public BubbleSort(Double[] list) {
    this.sort(list);
  }

  private void sort(Double[] list) {
    /*
     * 原方法： 内层循环每次能找出当前乱序区的极值，它会冒泡到末尾 外层循环每次缩小乱序区的范围
     */
    // for (int l = list.length - 1; l > 0; l--) {
    // for (int i = 0; i < l; i++) {
    // if (list[i] > list[i + 1]) {
    // // 交换
    // double value = list[i];
    // list[i] = list[i + 1];
    // list[i + 1] = value;
    // }
    // }
    // }
    this.asyncSort(list);
  }

  // 异步排序
  // 分析：
  // 原本的排序算法是一个连续的、不可打断的过程，但需求要将每次排序的操作可视化（仿真）
  // 即每次操作都要映射成视图的变化，且速度可调节、可中断可恢复
  // 这就要求每个循环级别的原子操作是相互异步的，具体实现起来就是下一步操作作为这一步的异步回调
  private void asyncSort(Double[] list) {
    // 需要比较的乱序区终点
    final int[] endpoint = {list.length - 1};
    // 当前比较的位置
    final int[] cuurentIndex = {0};
    Timer timer = new Timer(true);
    TimerTask task = new TimerTask() {
      public void run() {
        // 这个逻辑分支是内层循环，在同一个乱序区冒泡
        if (cuurentIndex[0] < endpoint[0]) {
          this.exchange(list, cuurentIndex[0]);
          cuurentIndex[0]++;
          // FIXME:
//                    timer.schedule(task, 500);
        } else {
          // 这个逻辑分支是外层循环
          // 缩小乱序区的范围，因为内层循环结束后已经通过冒泡确定了一个极值，下次再找极值就不需要带上它了
          endpoint[0]--;
          // 初始化内层循环，为新的乱序区重新冒泡做准备
          cuurentIndex[0] = 0;
          if (endpoint[0] > 0) {
            // 在新的乱序区重新冒泡
            // FIXME:
//                        timer.schedule(task, 500);
          } else {
            // 乱序区已经没了，整个数组全部有序
            this.compete(list);
            return;
          }
        }
      }

      // 数组中元素交换
      // 它是冒泡排序的实质性步骤，也是可视化（仿真）视图变化的主要因素
      private void exchange(Double[] list, int i) {
        if (list[i] > list[i + 1]) {
          double value = list[i];
          list[i] = list[i + 1];
          list[i + 1] = value;
        }
      }

      // 结束
      private void compete(Double[] list) {

      }
    };

    timer.schedule(task, 500);

  }

}