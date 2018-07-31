package data;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import dataservice.UserDataService;
import po.UserPO;

public class UserData extends UnicastRemoteObject implements UserDataService {
	private static final long serialVersionUID = 1724052352464005222L;
	private String tableName="SystemUser";
	private String idName="SUID";
	private String[] attributes={"SUID","SUName","SUPwd","SUDept","SURank","SUSex",
			"SUBirth","SUTel","SUIsExist"};

	public UserData() throws RemoteException {
		super();
	}

	@Override
	public String getNewId() throws RemoteException {
		return SQLQueryHelper.getNewId(tableName, idName, "%04d");
	}

	@Override
	public UserPO findById(String id) throws RemoteException {
		try {
			ResultSet r = SQLQueryHelper.getRecordByAttribute(tableName, idName, id);
			r.next();
			return getUserPO(r);
		}
		catch(Exception e) {
		    e.printStackTrace();
		    return null;
		}
	}

	@Override
	public boolean add(UserPO user) throws RemoteException {
		return SQLQueryHelper.add(tableName, user.getUserId(), user.getUserName(), user.getUserPwd(), 
				user.getUsertype(), user.getUserRank(), user.getUserSex(), 
				Calendar.getInstance().get(Calendar.YEAR)-user.getUserAge(), user.getUserTelNumber(), 1);	
	}

	@Override
	public boolean delete(String id) throws RemoteException {
		return SQLQueryHelper.getFalseDeleteResult(tableName, "SUIsExist",idName, id);
	}

	@Override
	public boolean update(UserPO user) throws RemoteException {
		Calendar now = Calendar.getInstance(); 
		int userBirth = now.get(Calendar.YEAR)-user.getUserAge();
		Object[] values={user.getUserId(),user.getUserName(),user.getUserPwd(),user.getUsertype(),
				user.getUserRank(),user.getUserSex(),userBirth,user.getUserTelNumber(),user.getExistFlag()};
		return SQLQueryHelper.update(tableName, attributes, values);
		
	}

	@Override
	public ArrayList<UserPO> getAllUser() throws RemoteException {
		ArrayList<UserPO> upos=new ArrayList<UserPO>();
		try {
		    Statement s = DataHelper.getInstance().createStatement();
			ResultSet r = s.executeQuery("SELECT * FROM SystemUser");
			while(r.next()) if(r.getBoolean("SUIsExist")) upos.add(getUserPO(r));
			return upos;
		}
		catch(Exception e) {
		    e.printStackTrace();
		    return null;
		}
	}

	@Override
	public ArrayList<UserPO> getUsersBy(String field, String content, boolean isfuzzy) throws RemoteException {
		ArrayList<UserPO> upos=new ArrayList<UserPO>();
		ResultSet r = null;
		try{
			if(isfuzzy){
				Statement s = DataHelper.getInstance().createStatement();
				r = s.executeQuery("SELECT * FROM SystemUser WHERE "+field+" LIKE '%"+content+"%';");
			}
			else r = SQLQueryHelper.getRecordByAttribute(tableName, field, content);
			while(r.next()) if(r.getBoolean("SUIsExist")) upos.add(getUserPO(r));
			return upos;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	private UserPO getUserPO(ResultSet r) {
		try {
			return new UserPO(r.getString("SUID"),
					r.getString("SUName"),
					r.getString("SUPwd"),
					r.getString("SUSex"),
					r.getString("SUTel"),
					r.getInt("SUDept"),
					r.getInt("SURank"),
					Calendar.getInstance().get(Calendar.YEAR)-r.getInt("SUBirth"),
					r.getBoolean("SUIsExist"));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
