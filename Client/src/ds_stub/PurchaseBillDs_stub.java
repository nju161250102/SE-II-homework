package ds_stub;

import java.rmi.RemoteException;
import java.util.ArrayList;

import dataservice.PurchaseBillDataService;
import po.billpo.BillPO;
import po.billpo.SalesItemsPO;
import po.billpo.PurchaseBillPO;


public class PurchaseBillDs_stub implements PurchaseBillDataService {
    
    private static final ArrayList<PurchaseBillPO> BILLS = new ArrayList<>();

    public PurchaseBillDs_stub() {
        if(BILLS.size() > 0) return;
        
        ArrayList<SalesItemsPO> items1 = new ArrayList<>();
        items1.add(new SalesItemsPO("000001", "", 50, 100, 5000));
        items1.add(new SalesItemsPO("000002", "", 100, 100, 10000));
        
        BILLS.add(new PurchaseBillPO(
            "2017-12-05", "19:23:55", "12345", "0007"
            , BillPO.COMMITED, "000001", "fa¡áq", 15000, items1
        ));
        
        ArrayList<SalesItemsPO> items2 = new ArrayList<>();
        items2.add(new SalesItemsPO("000003", "", 50, 200, 10000));
        
        BILLS.add(new PurchaseBillPO(
            "2017-12-05", "21:30:02", "12000", "0002"
            , BillPO.COMMITED, "000002", "hello susie", 10000, items2
        ));
        
        ArrayList<SalesItemsPO> items3 = new ArrayList<>();
        items3.add(new SalesItemsPO("000004", "", 100, 100, 10000));
        items3.add(new SalesItemsPO("000003", "", 200, 200, 40000));
        
        BILLS.add(new PurchaseBillPO(
            "2017-12-08", "11:24:51", "32100", "0002"
            , BillPO.COMMITED, "000003", "rarara", 50000, items3
        ));
        
        ArrayList<SalesItemsPO> items4 = new ArrayList<>();
        items4.add(new SalesItemsPO("000002", "", 30, 100, 3000));
        
        BILLS.add(new PurchaseBillPO(
            "2017-12-09", "12:08:21", "98765", "0007"
            , BillPO.SAVED, "000004", "°Â¶÷", 3000, items4
        ));
    }

    @Override
    public boolean saveBill(PurchaseBillPO purchaseBill) {
        BILLS.add(purchaseBill);
        System.out.println("purchase bill saved in database: " + purchaseBill.getId());
        return true;
    }

    @Override
    public boolean deleteBill(String id) {
        for(PurchaseBillPO bill: BILLS){
            if(bill.getId().equals(id))
                BILLS.remove(bill);
        }
        System.out.println("purchase bill deleted in database: " + id);
        return true;
    }

    @Override
    public String getNewId() {
        return "00123";
    }

    @Override
    public PurchaseBillPO getBillById(String id) {
        return BILLS.get(0);
    }

    @Override
    public ArrayList<PurchaseBillPO> getBillsBy(String field, String key, boolean isfuzzy) throws RemoteException {
        ArrayList<PurchaseBillPO> result = new ArrayList<>();
        result.add(BILLS.get(1));
        result.add(BILLS.get(2));
        return result;
    }

    @Override
    public ArrayList<PurchaseBillPO> getBillsByDate(String from, String to) throws RemoteException {
        ArrayList<PurchaseBillPO> result = new ArrayList<>();
        result.add(BILLS.get(0));
        result.add(BILLS.get(3));
        return result;
    }

}
