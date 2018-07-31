package ds_stub;

import java.rmi.RemoteException;
import java.util.ArrayList;

import dataservice.PurchaseReturnBillDataService;
import po.billpo.BillPO;
import po.billpo.SalesItemsPO;
import po.billpo.PurchaseReturnBillPO;


public class PurchaseReturnBillDs_stub implements PurchaseReturnBillDataService {
    
    private static final ArrayList<PurchaseReturnBillPO> BILLS
        = new ArrayList<>();
    
    static{
        ArrayList<SalesItemsPO> items1 = new ArrayList<>();
        items1.add(new SalesItemsPO("000001", "", 20, 50.0, 1000.0));
        items1.add(new SalesItemsPO("000002", "", 40, 60.0, 2400.0));
        BILLS.add(new PurchaseReturnBillPO(
            "2017-12-03", "21:03:17", "01324", "0002"
            , BillPO.PASS, "000001", "", 3400.0, items1
        ));

        ArrayList<SalesItemsPO> items2 = new ArrayList<>();
        items2.add(new SalesItemsPO("000003", "", 10, 60.0, 600.0));
        items2.add(new SalesItemsPO("000004", "", 50, 80.0, 4000.0));
        BILLS.add(new PurchaseReturnBillPO(
            "2017-12-13", "18:33:47", "01921", "0007"
            , BillPO.COMMITED, "000003", "", 4600.0, items2
        ));

        ArrayList<SalesItemsPO> items3 = new ArrayList<>();
        items3.add(new SalesItemsPO("000001", "", 50, 100.0, 5000.0));
        items3.add(new SalesItemsPO("000004", "", 70, 300.0, 21000.0));
        BILLS.add(new PurchaseReturnBillPO(
            "2017-11-23", "14:38:47", "21171", "0007"
            , BillPO.PASS, "000002", "", 26000.0, items3
        ));
        
        ArrayList<SalesItemsPO> items4 = new ArrayList<>();
        items4.add(new SalesItemsPO("000003", "", 10, 60.0, 600.0));
        items4.add(new SalesItemsPO("000004", "", 50, 80.0, 4000.0));
        BILLS.add(new PurchaseReturnBillPO(
                "2017-12-02", "18:33:47", "01921", "0007"
                , BillPO.COMMITED, "000003", "", 4600.0, items2
            ));
        
        ArrayList<SalesItemsPO> items5 = new ArrayList<>();
        items5.add(new SalesItemsPO("000001", "", 50, 100.0, 5000.0));
        items5.add(new SalesItemsPO("000004", "", 70, 300.0, 21000.0));
        BILLS.add(new PurchaseReturnBillPO(
                "2017-12-02", "19:38:47", "21171", "0007"
                , BillPO.COMMITED, "000002", "", 26000.0, items3
            ));
    }

    @Override
    public boolean deleteBill(String id) throws RemoteException {
        for(int i = 0; i < BILLS.size(); i++){
            if(BILLS.get(i).getId().equals(id)){
                BILLS.remove(i);
                break;
            }
        }
        System.out.println("Purchase Return Bill deleted in database: " + id);
        return true;
    }

    @Override
    public String getNewId() throws RemoteException {
        return "98756";
    }

    @Override
    public boolean saveBill(PurchaseReturnBillPO purchaseReturnBill) throws RemoteException {
        BILLS.add(purchaseReturnBill);
        System.out.println("Purchase Return Bill added in database: " + purchaseReturnBill.getId());
        return true;
    }

    @Override
    public PurchaseReturnBillPO getBillById(String id) throws RemoteException {
        System.out.println("Purchase Return Bill found in database: " + id);
        return BILLS.get(4);
    }

    @Override
    public ArrayList<PurchaseReturnBillPO> getBillsByDate(String from, String to) throws RemoteException {
        ArrayList<PurchaseReturnBillPO> result = new ArrayList<>();
        result.add(BILLS.get(1));
        result.add(BILLS.get(2));
        System.out.println("Purchase Return Bill found by date in database");
        return result;
    }

}
