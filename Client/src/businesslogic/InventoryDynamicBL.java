package businesslogic;

import java.util.ArrayList;
import java.util.TreeMap;

import blservice.InventoryDynamicBLService;
import blservice.infoservice.GetCommodityInterface;
import po.billpo.PurchaseBillPO;
import po.billpo.PurchaseReturnBillPO;
import po.billpo.SalesBillPO;
import po.billpo.SalesItemsPO;
import po.billpo.SalesReturnBillPO;
import vo.CommodityVO;
import vo.MyTableModel;

/**
 * @author 恽叶霄
 */
public class InventoryDynamicBL implements InventoryDynamicBLService {
    
    private String[] columnNames = {"商品编号", "名称", "型号", "库存", "进货数量", "进货总额"
                    , "进货退货数量", "进货退货总额", "销售数量", "销售总额", "销售退货数量", "销售退货总额"
                    , "总入库数量", "总入库金额", "总出库数量", "总出库金额", "数量合计", "金额合计"};

    public InventoryDynamicBL() {}

    @Override
    public MyTableModel getDynamic(String from, String to) {
        ArrayList<PurchaseBillPO> purchaseBills 
            = new PurchaseBillBL().getBillPOsByDate(from, to);
        ArrayList<PurchaseReturnBillPO> purchaseReturnBills 
            = new PurchaseReturnBillBL().getPurchaseReturnBillPOsByDate(from, to);
        ArrayList<SalesBillPO> salesBills
            = new SalesBillBL().getBillPOsByDate(from, to);
        ArrayList<SalesReturnBillPO> salesReturnBills
            = new SalesReturnBillBL().getBillPOsByDate(from, to);

        ItemMap items = new ItemMap();
        purchaseBills.forEach(b-> b.getPurchaseBillItems().forEach(
            i-> items.addItem(i, SalesType.PURCHASE)));
        purchaseReturnBills.forEach(b-> b.getPurchaseReturnBillItems().forEach(
            i-> items.addItem(i, SalesType.PURCHASE_RETURN)));
        salesBills.forEach(b-> b.getSalesBillItems().forEach(
            i -> items.addItem(i, SalesType.SALES)));
        salesReturnBills.forEach(b-> b.getSalesReturnBillItems().forEach(
            i-> items.addItem(i, SalesType.SALES_RETURN)));

        return items.toModel();
    }
    
    @Override
    public MyTableModel getDefault(){
        return new MyTableModel(null, columnNames);
    }
    
    private static class Item {

        private static final GetCommodityInterface COMMODITYBL = new CommodityBL();
        private String id, name, type, store;
        private int[] nums;
        private double[] sums;
        
        public Item(String id){
            this.id = id;
            CommodityVO commodity = COMMODITYBL.getCommodity(id);
            this.name = commodity.getName();
            this.type = commodity.getType();
            this.store = commodity.getStore();
            nums = new int[7];
            sums = new double[7];
        }
        
        public void addItem(int num, double sum, SalesType type){
            nums[type.n] += num;
            sums[type.n] += sum;
        }
        
        private void summary(){
            nums[4] = nums[0] + nums[3];
            nums[5] = nums[1] + nums[2];
            nums[6] = nums[5] - nums[4];
            sums[4] = sums[0] + sums[3];
            sums[5] = sums[1] + sums[2];
            sums[6] = sums[5] - sums[4];
        }
        
        public String[] toStringArray(){
            summary();
            return new String[]{id, name, type, store, nums[0] + "", sums[0] + ""
                    , nums[1] + "", sums[1] + "", nums[2] + "", sums[2] + ""
                    , nums[3] + "", sums[3] + "", nums[4] + "", sums[4] + ""
                    , nums[5] + "", sums[5] + "", nums[6] + "", sums[6] + ""};
        }

    }

    private class ItemMap{

        private TreeMap<String, Item> map;
        
        public ItemMap(){
            map = new TreeMap<>();
        }
        
        public void addItem(SalesItemsPO billItem, SalesType type){
            String id = billItem.getComId();
            if(map.containsKey(id)){
                map.get(id).addItem(billItem.getComQuantity(), billItem.getComSum(), type);
            } else {
                Item item = new Item(id);
                item.addItem(billItem.getComQuantity(), billItem.getComSum(), type);
                map.put(id, item);
            }
        }
        
        public MyTableModel toModel(){
            String[][] data = new String[map.size()][];
            for(int i = 0; i < data.length; i++){
                data[i] = map.pollFirstEntry().getValue().toStringArray();
            }
            return new MyTableModel(data, columnNames);
        }

    }

    private enum SalesType{
        PURCHASE(0), PURCHASE_RETURN(1), SALES(2), SALES_RETURN(3);
        int n;
        SalesType(int n){
            this.n = n;
        }
    }

}
