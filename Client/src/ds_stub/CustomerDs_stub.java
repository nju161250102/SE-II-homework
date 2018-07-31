package ds_stub;

import java.rmi.RemoteException;
import java.util.ArrayList;

import dataservice.CustomerDataService;
import po.CustomerPO;

public class CustomerDs_stub implements CustomerDataService {

	private static ArrayList<CustomerPO> result = new ArrayList<CustomerPO>();
	
	public CustomerDs_stub() {
		if (result.size() == 0) {
			result.add(new CustomerPO("000001","清流","11011011011","deep dark ♂ fantasy","qinliu@google.com","223300","Van",5,0,4000,0,0,true));
			result.add(new CustomerPO("000002","浊流","11011011000","幻想♂乡","zhuoliu@163.com","223301","比利",4,0,4000,200,1000,true));
			result.add(new CustomerPO("000003","中流","11011011101","新日暮里","zhongliu@qq.com","223302","木吉",3,1,3000,1000,1000,true));
			result.add(new CustomerPO("000004","电子流","10011011101","新日暮里","e_flow@bilibili.com","223302","木吉",3,1,3000,1000,1000,true));
		}
	}
	@Override
	public String getNewId() throws RemoteException {
		return String.format("%06d", result.size()+1);
	}

	@Override
	public CustomerPO findById(String id) throws RemoteException {
		System.out.println("customer deleted in database: " + id);
        for (int i = 0; i < result.size(); i++) {
        	if (id.equals(result.get(i).getId())) return result.get(i);
        }
		return null;
	}

	@Override
	public boolean add(CustomerPO customer) throws RemoteException {
		System.out.println("customer added in database: " + customer.getId());
        result.add(customer);
        return true;
	}

	@Override
	public boolean delete(String id) throws RemoteException {
		System.out.println("customer deleted in database: " + id);
        for (int i = 0; i < result.size(); i++) {
        	if (id.equals(result.get(i).getId())) {result.remove(i);break;}
        }
        return true;
	}

	@Override
	public boolean update(CustomerPO customer) throws RemoteException {
		System.out.println("customer updated in database: " + customer.getId());
        return true;
	}

	@Override
	public ArrayList<CustomerPO> getAllCustomer() throws RemoteException {
		return result;
	}

	@Override
	public ArrayList<CustomerPO> getCustomersBy(String field, String content, boolean isfuzzy) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
