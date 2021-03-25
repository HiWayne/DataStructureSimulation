# 数据结构虚拟仿真实验项目的研发——交换类排序

## 目录结构

algorithum 算法逻辑
core.simulation 核心的仿真控制代码，向算法中提供了 hooks，通过定义 action，解耦了算法和渲染，是算法逻辑层和视图渲染层之间的中间层
constant 常量相关，主要是定义了视图的行为规则
view 视图相关逻辑
main 入口
