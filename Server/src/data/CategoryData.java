package data;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dataservice.CategoryDataService;
import po.CategoryPO;

public class CategoryData extends UnicastRemoteObject implements CategoryDataService{
	private static final long serialVersionUID = 2679649485153805573L;
	private String tableName="CategoryInfo";
	private String idName="CateID";
	private String[] attributes={"CateID","CateName","CateIsExist","FatherID"};

	public CategoryData() throws RemoteException {
		super();
	}

	@Override
	public String getNewId() throws RemoteException {
		return SQLQueryHelper.getNewId(tableName, idName, "%06d");
	}

	@Override
	public CategoryPO findById(String id) throws RemoteException {
		try{
			ResultSet r = SQLQueryHelper.getRecordByAttribute(tableName, idName, id);
			r.next();
			return getCategoryPO(r);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean add(CategoryPO category) throws RemoteException {
		return SQLQueryHelper.add(tableName, category.getId(), category.getName(), 1, category.getFatherId());
	}

	@Override
	public boolean delete(String id) throws RemoteException {
		return SQLQueryHelper.getFalseDeleteResult(tableName, "CateIsExist",idName, id);
	}

	@Override
	public boolean update(CategoryPO category) throws RemoteException {
		Object[] values={category.getId(),category.getName(),category.getExistFlag(),category.getFatherId()};
		return SQLQueryHelper.update(tableName, attributes, values);
	}

	@Override
	public ArrayList<CategoryPO> getAllCategory() throws RemoteException {
		ArrayList<CategoryPO> cpos = new ArrayList<CategoryPO>();
		try{
			 Statement s = DataHelper.getInstance().createStatement();
			 ResultSet r = s.executeQuery("SELECT * FROM CategoryInfo");
			 while(r.next()) if(r.getBoolean("CateIsExist")) cpos.add(getCategoryPO(r));
		}catch(Exception e){
			e.printStackTrace();
		}
		return cpos;
	}

	@Override
	public ArrayList<CategoryPO> getCategorysBy(String field, String content, boolean isfuzzy) throws RemoteException {
		ArrayList<CategoryPO> cpos = new ArrayList<CategoryPO>();
		ResultSet r=null;
		try{
			if(isfuzzy){
				Statement s = DataHelper.getInstance().createStatement();
				r = s.executeQuery("SELECT * FROM CategoryInfo WHERE "+field+" LIKE '%"+content+"%';");
			}
			else r = SQLQueryHelper.getRecordByAttribute(tableName, field, content);
			while(r.next()) if(r.getBoolean("CateIsExist")) cpos.add(getCategoryPO(r));
			return cpos;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	private CategoryPO getCategoryPO(ResultSet r) {
		try {
			return new CategoryPO(r.getString("CateID"), r.getString("CateName"), r.getString("FatherID"), r.getBoolean("CateIsExist"));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
