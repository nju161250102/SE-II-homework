package ds_stub;

import java.rmi.RemoteException;
import java.util.ArrayList;

import dataservice.GiftBillDataService;
import po.billpo.BillPO;
import po.billpo.GiftBillPO;
import po.billpo.GiftItem;


public class GiftBillDs_stub implements GiftBillDataService {
    
    public static final ArrayList<GiftBillPO> BILLS = new ArrayList<>();

    static{
        ArrayList<GiftItem> gifts0 = new ArrayList<>();
        gifts0.add(new GiftItem("000001", 10, 100.0));
        BILLS.add(new GiftBillPO("20171212", "12:21:22", "00001", "000000", BillPO.COMMITED, gifts0, "XSD-20171212-00123", "000002"));

        ArrayList<GiftItem> gifts1 = new ArrayList<>();
        gifts1.addAll(gifts0);
        gifts1.add(new GiftItem("000002", 10, 120.0));
        BILLS.add(new GiftBillPO("20171221", "14:54:35", "12345", "000000", BillPO.PASS, gifts1, "XSD-20171221-00123", "000003"));
        
        ArrayList<GiftItem> gifts2 = new ArrayList<>();
        gifts2.add(new GiftItem("000003", 10, 80.0));
        BILLS.add(new GiftBillPO("20171130", "08:08:08", "54321", "000000", BillPO.NOTPASS, gifts2, "XSD-20171130-54321", "000004"));
    }

    public GiftBillDs_stub() {}

    @Override
    public boolean add(GiftBillPO bill) throws RemoteException {
        return BILLS.add(bill);
    }

    @Override
    public String getNewId() throws RemoteException {
        return "12234";
    }

    @Override
    public GiftBillPO getById(String id) throws RemoteException {
        return BILLS.get(0);
    }

}
