package data;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import dataservice.MailDataService;
import po.MailPO;
import po.UserPO;

public class MailData extends UnicastRemoteObject implements MailDataService {

	public MailData() throws RemoteException {
		super();
	}

	@Override
	public boolean saveMail(MailPO mail) throws RemoteException {
		return SQLQueryHelper.add("MailInfo", mail.getFromId(), mail.getToId(), mail.getTime(), mail.getContent(), mail.isRead()?1:0);
	}

	@Override
	public ArrayList<MailPO> getMailList(UserPO user) throws RemoteException {
		ArrayList<MailPO> mails=new ArrayList<MailPO>();
		try{
			String sql="SELECT * FROM MailInfo WHERE MIToID='"+user.getUserId()+"';";
			Statement s=DataHelper.getInstance().createStatement();
			ResultSet r=s.executeQuery(sql);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			while(r.next()){
				MailPO mail=new MailPO(
						r.getString("MIFromID"),
						r.getString("MIToID"),
						r.getString("MIContent"),
						sdf.format(r.getTimestamp("MITime")),
						r.getBoolean("MIIsRead")
						);
				mails.add(mail);
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return mails;
	}

	@Override
	public boolean readMail(MailPO mail) throws RemoteException {
		try{
			String sql="UPDATE MailInfo SET MIIsRead="+1;
			Statement s=DataHelper.getInstance().createStatement();
			int r=s.executeUpdate(sql);
			if(r>0)return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return false;
	}

}
