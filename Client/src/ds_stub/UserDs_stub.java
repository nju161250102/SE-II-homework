package ds_stub;

import java.rmi.RemoteException;
import java.util.ArrayList;

import dataservice.UserDataService;
import po.UserPO;
import vo.UserType;

public class UserDs_stub implements UserDataService {

	private static ArrayList<UserPO> result = new ArrayList<UserPO>();
	
	public UserDs_stub() {
		if (result.size() == 0) {
			result.add(new UserPO("0001","他", "", "男", "11122233344", UserType.KEEPER.getNum(), 0, 91,true));
			result.add(new UserPO("0002","香港记者", "", "女", "12345678900", UserType.SALESMAN.getNum(), 0, 55,true));
			result.add(new UserPO("0003","长者", "", "男", "12345432112", UserType.ACCOUNTANT.getNum(), 0, 88,true));
			result.add(new UserPO("0004","比利海灵顿", "", "男", "18851826666", UserType.GM.getNum(), 0, 31,true));
			result.add(new UserPO("0005","梁逸峰", "", "男", "18851827777", UserType.ADMIN.getNum(), 0, 46,true));
			result.add(new UserPO("0006","金坷垃", "", "男", "13245432112", UserType.ACCOUNTANT.getNum(), 1, 55,true));
			result.add(new UserPO("0007","西方记者", "", "男", "12345600088", UserType.SALESMAN.getNum(), 1, 35,true));
		}
	}
	@Override
	public String getNewId() throws RemoteException {
		return String.format("%04d", result.size()+1);
	}

	@Override
	public UserPO findById(String id) throws RemoteException {
		System.out.println("user found in database: " + id);
		for (int i = 0; i < result.size(); i++) {
        	if (id.equals(result.get(i).getUserId())) return result.get(i);
        }
		return null;
	}

	@Override
	public boolean add(UserPO user) throws RemoteException {
		System.out.println("user added in database: " + user.getUserId());
        result.add(user);
        return true;
	}

	@Override
	public boolean delete(String id) throws RemoteException {
		System.out.println("user deleted in database: " + id);
		for (int i = 0; i < result.size(); i++) {
        	if (id.equals(result.get(i).getUserId())) {result.remove(i);break;}
        }
        return true;
	}

	@Override
	public boolean update(UserPO user) throws RemoteException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public ArrayList<UserPO> getAllUser() throws RemoteException {
		System.out.println("all users in database returned");
        return result;
	}

	@Override
	public ArrayList<UserPO> getUsersBy(String field, String content, boolean isfuzzy) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
