package dataservice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import po.billpo.PaymentBillPO;

public interface PaymentBillDataService extends Remote{

	public boolean saveBill(PaymentBillPO bill) throws RemoteException;

	public boolean deleteBill(String billid) throws RemoteException;

	public String getNewId() throws RemoteException;
	
	public PaymentBillPO getBillById(String id) throws RemoteException;
	
    ArrayList<PaymentBillPO> getBillsByDate(String from, String to) throws RemoteException;

}
