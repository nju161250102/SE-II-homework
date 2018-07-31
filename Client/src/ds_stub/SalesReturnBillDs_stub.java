package ds_stub;

import java.rmi.RemoteException;
import java.util.ArrayList;

import dataservice.SalesReturnBillDataService;
import po.billpo.BillPO;
import po.billpo.SalesItemsPO;
import po.billpo.SalesReturnBillPO;


public class SalesReturnBillDs_stub implements SalesReturnBillDataService {
    
    private static final ArrayList<SalesReturnBillPO> BILLS = new ArrayList<>();
    
    public SalesReturnBillDs_stub(){
        if(BILLS.size() > 0) return;
        
        ArrayList<SalesItemsPO> items1 = new ArrayList<>();
        items1.add(new SalesItemsPO("000001", "", 100, 50.0, 5000.0));
        items1.add(new SalesItemsPO("000002", "", 20, 100.0, 2000.0));
        BILLS.add(new SalesReturnBillPO(
            "2017-12-09", "21:03:23", "00123", "0002", BillPO.PASS,
            "000001", "", "", "00012", 7000.0, 6800.0, items1
        ));
        
        ArrayList<SalesItemsPO> items2 = new ArrayList<>();
        items2.add(new SalesItemsPO("000003", "", 30, 80.0, 2400.0));
        items2.add(new SalesItemsPO("000004", "", 40, 60.0, 2400.0));
        BILLS.add(new SalesReturnBillPO(
            "2017-12-11", "12:23:28", "08193", "0007", BillPO.COMMITED,
            "000002", "", "", "00312", 4800.0, 3800.0, items2
        ));
        
        ArrayList<SalesItemsPO> items3 = new ArrayList<>();
        items3.add(new SalesItemsPO("000002", "", 10, 100.0, 1000.0));
        items3.add(new SalesItemsPO("000003", "", 70, 120.0, 8400.0));
        BILLS.add(new SalesReturnBillPO(
            "2017-11-11", "09:22:32", "02143", "0007", BillPO.PASS,
            "000001", "", "", "00112", 9400.0, 9000.0, items3
        ));
    }

    @Override
    public boolean deleteBill(String id) throws RemoteException {
        int length = id.length();
        String realId = id.substring(length - 5, length);
        for(int i = 0; i < BILLS.size(); i++){
            if(BILLS.get(i).getId().equals(realId)){
                BILLS.remove(i);
                break;
            }
        }
        System.out.println("sale return bill deleted in database: " + id);
        return true;
    }

    @Override
    public String getNewId() throws RemoteException {
        return "12345";
    }

    @Override
    public boolean saveBill(SalesReturnBillPO bill) throws RemoteException {
        BILLS.add(bill);
        System.out.println("sale return bill saved in database: " + bill.getId());
        return true;
    }

    @Override
    public SalesReturnBillPO getBillById(String id) throws RemoteException {
        return BILLS.get(0);
    }

    @Override
    public ArrayList<SalesReturnBillPO> getBillsByDate(String from, String to) throws RemoteException {
        ArrayList<SalesReturnBillPO> result = new ArrayList<>();
        result.add(BILLS.get(2));
        result.add(BILLS.get(1));
        return result;
    }

}
