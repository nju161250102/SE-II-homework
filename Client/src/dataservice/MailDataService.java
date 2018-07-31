package dataservice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import po.MailPO;
import po.UserPO;

public interface MailDataService extends Remote{
	/**向数据库保存一封邮件*/
	public boolean saveMail(MailPO mail) throws RemoteException;
	/**获得某位用户的所有邮件信息*/
	public ArrayList<MailPO> getMailList(UserPO user) throws RemoteException;
	/**将某封邮件设为已读*/
	public boolean readMail(MailPO mail) throws RemoteException;
}
