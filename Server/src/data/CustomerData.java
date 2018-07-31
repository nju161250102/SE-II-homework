package data;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dataservice.CustomerDataService;
import po.CustomerPO;

public class CustomerData extends UnicastRemoteObject implements CustomerDataService {
	private static final long serialVersionUID = -3647893693949919501L;
	private String tableName="CustomerInfo";
	private String idName="CusID";
	private String[] attributes={"CusID","CusName","CusRank","CusTel","CusAddress","CusCode",
			"CusMail","CusReceiRange","CusReceivable","CusPayment","CusSalesman","CusType","CusIsExist"};

	public CustomerData() throws RemoteException {
		super();
	}

	@Override
	public String getNewId() throws RemoteException {
		return SQLQueryHelper.getNewId(tableName, idName, "%06d");
	}

	@Override
	public CustomerPO findById(String id) throws RemoteException {
		try{
			ResultSet r=SQLQueryHelper.getRecordByAttribute(tableName, idName, id);
			r.next();
			return getCustomerPO(r);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean add(CustomerPO customer) throws RemoteException {
		return SQLQueryHelper.add(tableName, customer.getId(),customer.getName(),customer.getRank(),
				customer.getTelNumber(),customer.getAddress(),customer.getCode(),customer.getMail(),
				customer.getRecRange(),customer.getReceivable(),customer.getPayment(),customer.getSalesman(),
				customer.getType(),1);
	}

	@Override
	public boolean delete(String id) throws RemoteException {
		return SQLQueryHelper.getFalseDeleteResult(tableName, "CusIsExist",idName, id);	
	}

	@Override
	public boolean update(CustomerPO customer) throws RemoteException {
		Object[] values={customer.getId(),customer.getName(),customer.getRank(),
				customer.getTelNumber(),customer.getAddress(),customer.getCode(),customer.getMail(),
				customer.getRecRange(),customer.getReceivable(),customer.getPayment(),customer.getSalesman(),
				customer.getType(),customer.getExistFlag()};
		return SQLQueryHelper.update(tableName, attributes, values);
	}

	@Override
	public ArrayList<CustomerPO> getAllCustomer() throws RemoteException {
		ArrayList<CustomerPO> cpos=new ArrayList<CustomerPO>();
		try {
		    Statement s = DataHelper.getInstance().createStatement();
			ResultSet r = s.executeQuery("SELECT * FROM CustomerInfo");
			while(r.next()) if (r.getBoolean("CusIsExist")) cpos.add(getCustomerPO(r));
		}
		catch(Exception e) {
		   e.printStackTrace();
		   return null;
		}
		return cpos;
	}

	@Override
	public ArrayList<CustomerPO> getCustomersBy(String field, String content, boolean isfuzzy) throws RemoteException {
		ArrayList<CustomerPO> cpos=new ArrayList<CustomerPO>();
		ResultSet r=null;
		try{
			if(isfuzzy){
				Statement s = DataHelper.getInstance().createStatement();
				r = s.executeQuery("SELECT * FROM CustomerInfo WHERE "+field+" LIKE '%"+content+"%';"); 
			}
			else r = SQLQueryHelper.getRecordByAttribute(tableName, field, content);
			while(r.next()) if(r.getBoolean("CusIsExist")) cpos.add(getCustomerPO(r));	
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return cpos;
	}

	private CustomerPO getCustomerPO(ResultSet r) {
		try {
			
			//String id, String name, String telNumber, String address
            //, String mail, String code,String salesman, int rank, int type, double recRange
            //, double receivable, double payment, boolean isExist
			return new CustomerPO(r.getString(attributes[0]),r.getString(attributes[1]),r.getString(attributes[3]),
					r.getString(attributes[4]),r.getString(attributes[6]),r.getString(attributes[5]),r.getString(attributes[10]),
					r.getInt(attributes[2]),r.getInt(attributes[11]),r.getDouble(attributes[7]),r.getDouble(attributes[8]),
					r.getDouble(attributes[9]),r.getBoolean(attributes[12]));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
