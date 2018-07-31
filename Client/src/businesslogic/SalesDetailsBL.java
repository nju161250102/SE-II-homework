package businesslogic;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;

import blservice.SalesDetailsBLService;
import blservice.infoservice.GetCommodityInterface;
import dataservice.BillSearchDataService;
import ds_stub.BillSearchDs_stub;
import po.billpo.BillPO;
import po.billpo.SalesBillPO;
import po.billpo.SalesItemsPO;
import rmi.Rmi;
import vo.CommodityVO;
import vo.MyTableModel;


public class SalesDetailsBL implements SalesDetailsBLService {
    
    private BillSearchDataService billSearchDs;
    private GetCommodityInterface commodityBl;

    public SalesDetailsBL() {
        billSearchDs = Rmi.flag ? Rmi.getRemote(BillSearchDataService.class) : new BillSearchDs_stub();
        commodityBl = new CommodityBL();
    }

    @Override
    public MyTableModel filter(String from, String to, String commodityId
        , String store, String customerId, String operatorId) {
        try{
            ArrayList<SalesBillPO> bills = billSearchDs.searchSalesBills(from, to, customerId, operatorId, BillPO.PASS);
            LinkedList<Item> items = new LinkedList<>();
            bills.forEach(b -> items.addAll(toLinkedList(b, commodityId, store)));
            String[] columnNames = {"销售日期", "商品编号", "商品名称", "商品型号", "数量", "单价", "总额"};
            String[][] data = new String[items.size()][];
            for(int i = 0; i < data.length; i++){
                data[i] = items.pop().toStringArray();
            }
            return new MyTableModel(data, columnNames);
        }catch(RemoteException e){
            e.printStackTrace();
            return null;
        }
    }
    
    private LinkedList<Item> toLinkedList(SalesBillPO bill, String commodityId, String store){
        String date = bill.getDate();
        ArrayList<SalesItemsPO> billItems = bill.getSalesBillItems();
        LinkedList<Item> items = new LinkedList<>();
        billItems.forEach(i -> {
            if(isValid(i, commodityId, store)){
                items.add(new Item(date, i.getComId(), i.getComQuantity(), i.getComPrice(), i.getComSum()));
            }
        });
        return items;
    }
    
    private boolean isValid(SalesItemsPO item, String commodityId, String store){
        boolean idValid = commodityId == null || 
                        item.getComId().equals(commodityId);
        boolean storeValid = store == null || 
                        commodityBl.getCommodity(item.getComId()).getStore().equals(store);
        return idValid && storeValid;
    }
    
    private class Item{
        private String date, id, name, type;
        int amount;
        double price, sum;
        public Item(String date, String id, int amount, double price, double sum){
            this.date = date;
            this.id = id;
            CommodityVO commodity = commodityBl.getCommodity(id);
            this.name = commodity.getName();
            this.type = commodity.getType();
            this.amount = amount;
            this.price = price;
            this.sum = sum;
        }
        
        public String[] toStringArray(){
            return new String[]{date, id, name, type, amount + "", price + "", sum + ""};
        }
    }

}