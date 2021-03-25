package constant;

public class Action {
    // 行为类型
    public Type type;
    // 初始值
    public Integer[] list;
    // 新增目标的位置索引 / 当前走到的索引 / 被比较的索引
    public Integer index;
    // 要新增的值
    public Integer value;
    // 交换行为中的第一个索引
    public Integer fromIndex;
    // 交换行为中的第一个值
    public Integer fromValue;
    // 交换行为中的第二个索引
    public Integer toIndex;
    // 交换行为中的第二个值
    public Integer toValue;

    // INIT
    public Action(Type type, Integer[] list) {
        this.type = type;
        this.list = list;
    }

    // ADD, REMOVE, MOVETOANDCOMPARE
    public Action(Type type, Integer index, Integer value) {
        this.type = type;
        if (type == Type.MOVETOANDCOMPARE) {
            final Integer fromIndex = index;
            final Integer toIndex = value;
            this.fromIndex = fromIndex;
            this.toIndex = toIndex;
        } else {
            this.index = index;
            this.value = value;
        }
    }

    // SWAP
    public Action(Type type, Integer fromIndex, Integer toIndex, Integer fromValue, Integer toValue) {
        this.type = type;
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
        this.fromValue = fromValue;
        this.toValue = toValue;
    }
}
