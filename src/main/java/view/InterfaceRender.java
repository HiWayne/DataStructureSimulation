package view;

import core.simulation.Simulation;
import view.components.Input;

import javax.swing.*;
import java.awt.*;

// 主界面渲染类
public class InterfaceRender {
    private Simulation simulation;
    private Integer[] inputArray;
    // 窗口实体
    private JFrame frame;

    // 控制反转
    public InterfaceRender(Simulation simulation) {
        // 仿真类实体
        this.simulation = simulation;
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowInterfaceGUI();
            }
        });
    }

    /**
     * { 创建并显示GUI。出于线程安全的考虑， 这个方法在事件调用线程中调用。
     */
    private void createAndShowInterfaceGUI() {
        final Integer frameWidth = 1152;
        final Integer frameHeight = 648;
        final Integer toolBarHeight = 80;

        // 创建及设置窗口
        JFrame frame = new JFrame("DataStructure Simulation");
        // 确保一个漂亮的外观风格
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置窗口大小
        frame.setPreferredSize(new Dimension(frameWidth, frameHeight));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel centerPanel = new JPanel();
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        frame.add(topPanel, BorderLayout.NORTH);
//        frame.add(centerPanel, BorderLayout.CENTER);
//        frame.add(bottomPanel, BorderLayout.SOUTH);

        // 头部区域
        JComponent arrayInput = new Input("数组", 20, "确定").get();
        topPanel.add(arrayInput);
        topPanel.setPreferredSize(new Dimension(frameWidth, toolBarHeight));

        // 中间区域
        centerPanel.setPreferredSize(new Dimension(frameWidth, frameHeight - 2 * toolBarHeight));

        // 底部区域
        JComponent input = new Input("数组", 20, "确定").get();
        bottomPanel.setPreferredSize(new Dimension(frameWidth, toolBarHeight));
        bottomPanel.add(input);

        this.frame = frame;
        // 依赖注入，仿真渲染的类们需要用到父组件实体
        SimulationRender.setFrame(frame);

        // 显示窗口
        frame.pack();
        frame.setVisible(true);

        // test
        Integer[] list = {1, 2, 3, 4, 5, 6};
        this.setInputArray(list);
        this.play();
    }

    // 输入待排序数组
    private void setInputArray(Integer[] inputArray) {
        this.inputArray = inputArray;
        this.simulation.setList(this.inputArray);
        this.initShow();
    }

    // 设置速度
    private void setSpeed(Long intervalTime) {
        this.simulation.setSpeed(intervalTime);
    }

    // 开始仿真
    private void play() {
        this.simulation.play();
    }

    // 暂停仿真
    private void pause() {
        this.simulation.pause();
    }

    // 初始化仿真
    private void initShow() {
        this.simulation.init();
    }
}
