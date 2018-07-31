package ds_stub;

import java.rmi.RemoteException;
import java.util.ArrayList;

import dataservice.AccountDataService;
import po.AccountPO;

public class AccountDs_stub implements AccountDataService {

	private static ArrayList<AccountPO> result = new ArrayList<AccountPO>();
	
	public AccountDs_stub() {
		if (result.size() == 0) {
			result.add(new AccountPO("6209111100001111","马云",8000.0,true));
			result.add(new AccountPO("6209111100002222","马化腾",40000.0,true));
			result.add(new AccountPO("6209111100003333","许家印",200000.0,true));
		}
	}
	@Override
	public AccountPO findById(String id) throws RemoteException {
		System.out.println("account find in database: " + id);
        for (int i = 0; i < result.size(); i++) {
        	if (id.equals(result.get(i).getId())) return result.get(i);
        }
		return null;
	}

	@Override
	public boolean add(AccountPO account) throws RemoteException {
		System.out.println("account added in database: " + account.getId());
        result.add(account);
        return true;
	}

	@Override
	public boolean delete(String id) throws RemoteException {
		System.out.println("account deleted in database: " + id);
        for (int i = 0; i < result.size(); i++) {
        	if (id.equals(result.get(i).getId())) {result.remove(i);break;}
        }
        return true;
	}

	@Override
	public boolean update(AccountPO account) throws RemoteException {
		System.out.println("account updated in database: " + account.getId());
        return true;
	}

	@Override
	public ArrayList<AccountPO> getAllAccount() throws RemoteException {
		return result;
	}

	@Override
	public ArrayList<AccountPO> getAccountsBy(String field, String content, boolean isfuzzy) throws RemoteException {
		return null;
	}

}
