package data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import po.PromotionPO;
import po.billpo.BillPO;

public class SQLQueryHelper {
	
	public static boolean add(String tableName, Object... values) {
		try {
			Statement s = DataHelper.getInstance().createStatement();
			String str = "INSERT INTO " + tableName + " VALUES ('";
			for (int i = 0; i < values.length; i++){
				if(i == 0) str += values[i];
				else str += "','" + values[i];
			}
			System.out.println(str + "')");
			if(s.executeUpdate(str + "')") > 0) return true;
			else return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean update(String tableName, String[] attributes,Object[] values){
		try{
			Statement s=DataHelper.getInstance().createStatement();
			String str="UPDATE "+tableName+" SET ";
			for(int i=1;i<attributes.length;i++){
				str=str+attributes[i]+"='"+values[i]+"'";
				if(i!=attributes.length-1) str+=",";
			}
			str=str+" WHERE "+attributes[0]+"='"+values[0]+"';";
			if(s.executeUpdate(str)>0)return true;
			else return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	//按顺序获得普通id
	public static String getNewId(String tableName, String attributeName, String format){
		String newId=null;
		int max=0,res=0;
		try{
			Statement s=DataHelper.getInstance().createStatement();
			ResultSet r=s.executeQuery("SELECT "+attributeName+" FROM "+tableName+";");
			while(r.next()) max++;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		res=max+1;
		newId=String.format(format, res);
		return newId;
	}
	
	public static boolean getTrueDeleteResult(String tableName, String attributeName, String value){
		try{
			Statement s = DataHelper.getInstance().createStatement();
			int r=s.executeUpdate("DELETE FROM "+tableName+" WHERE "+attributeName+"='"+value+"';");
			if(r>0)return true;
		}catch(Exception e){
			  e.printStackTrace();
			   return false;
		}
		return false;		
	}
	
	public static boolean getFalseDeleteResult(String tableName, String flagName, String attributeName, String value){
		
		try{
			Statement s = DataHelper.getInstance().createStatement();
			int r=s.executeUpdate("UPDATE "+tableName+" SET "+flagName+"=0 WHERE "+attributeName+"='"+value+"';");
			if(r>0)return true;
		}catch(Exception e){
			  e.printStackTrace();
			   return false;
		}
		return false;
		
	}
	
	public static ResultSet getRecordByAttribute(String tableName, String attributeName, String value){
		
		try{
			Statement s = DataHelper.getInstance().createStatement();
			ResultSet r = s.executeQuery("SELECT * FROM "+tableName+" WHERE "+attributeName+"='" + value +"';");	 
			return r;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
    }
	
	public static boolean isPromotionExist(String billName,String idName,PromotionPO pro){
		int num=0;
		try{
			Statement s=DataHelper.getInstance().createStatement();
			ResultSet r=s.executeQuery("SELECT COUNT(*) AS num FROM "+billName+" WHERE "+idName+"='"+pro.getId()+"';");
			while(r.next())
			{
				num=r.getInt("num");
			}
			if(num>0)return true;
			else return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	
}
