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
    
	public ArrayList<PurchaseBillPO> getBillsBy(String field, String key, boolean isFuzzy) throws RemoteException;
	
	public ArrayList<PurchaseBillPO> getBillByDate(String from, String to) throws RemoteException;
}