package vo;

import java.util.ArrayList;

import po.GroupDiscountPO;

/**
 * 商品组合降价的促销策略VO
 * 
 * @author 恽叶霄
 */
public class GroupDiscountVO extends PromotionVO {
    
    private MyTableModel group;
    private double singleReduction;

    public GroupDiscountVO(String id, String from, String to, double reduction, MyTableModel group) {
        super(id, from, to, null);
        this.group = group;
        this.singleReduction = reduction;
    }
    
    public MyTableModel getGroup(){
        return group;
    }
    
    public ArrayList<String> getGroupComIds(){
        int rows = group.getRowCount();
        ArrayList<String> comIds = new ArrayList<>(rows);
        for(int i = 0; i < rows; i++){
            comIds.add(group.getValueAt(i, 0).toString());
        }
        return comIds;
    }

    @Override
    public void setReduction(int num){
        reduction = singleReduction * num;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(super.toString());
        buffer.append("；商品组合：");
        getGroupComIds().forEach(e->buffer.append(e + ","));
        buffer.delete(buffer.length() - 1, buffer.length());
        buffer.append("；减价总额：");
        buffer.append(singleReduction);
        return buffer.toString();
    }

    @Override
    public GroupDiscountPO toPO() {
        return new GroupDiscountPO(this.getId(), this.getFromDate(), this.getToDate(), getGroupComIds(), singleReduction);
    }

}
