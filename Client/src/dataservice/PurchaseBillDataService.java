package dataservice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import po.billpo.PurchaseBillPO;

public interface PurchaseBillDataService extends Remote{
    
    boolean saveBill(PurchaseBillPO purchaseBill) throws RemoteException;
    
    boolean deleteBill(String id) throws RemoteException;
    
    String getNewId() throws RemoteException;
    
    PurchaseBillPO getBillById(String id) throws RemoteException;
    
    ArrayList<PurchaseBillPO> getBillsBy(String field, String key, boolean isfuzzy) throws RemoteException;
    
    ArrayList<PurchaseBillPO> getBillsByDate(String from, String to) throws RemoteException;

}
