package dataservice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import po.billpo.PurchaseReturnBillPO;

public interface PurchaseReturnBillDataService extends Remote{
    
    boolean saveBill(PurchaseReturnBillPO purchaseReturnBill) throws RemoteException;
    
    boolean deleteBill(String id) throws RemoteException;
    
    String getNewId() throws RemoteException;
    
    PurchaseReturnBillPO getBillById(String id) throws RemoteException;
    
    ArrayList<PurchaseReturnBillPO> getBillsByDate(String from, String to) throws RemoteException;

}
