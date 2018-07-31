package dataservice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import po.billpo.SalesReturnBillPO;

public interface SalesReturnBillDataService extends Remote{
    
    boolean saveBill(SalesReturnBillPO bill) throws RemoteException;
    
    boolean deleteBill(String id) throws RemoteException;
    
    String getNewId() throws RemoteException;
    
    SalesReturnBillPO getBillById(String id) throws RemoteException;
    
    ArrayList<SalesReturnBillPO> getBillsByDate(String from, String to) throws RemoteException;

}
