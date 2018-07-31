package businesslogic;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import blservice.MailBLService;
import dataservice.MailDataService;
import dataservice.UserDataService;
import ds_stub.MailDataDs_stub;
import ds_stub.UserDs_stub;
import po.MailPO;
import po.UserPO;
import rmi.Rmi;
import vo.MailVO;
import vo.UserVO;

public class MailBL implements MailBLService {

	private MailDataService mailDs = Rmi.flag ? Rmi.getRemote(MailDataService.class) : new MailDataDs_stub();
	private UserDataService userData = Rmi.flag ? Rmi.getRemote(UserDataService.class) : new UserDs_stub();
	@Override
	public boolean saveMail(String fromId, String toId, String content) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		MailPO mail = new MailPO(fromId, toId, content, time, false);
		try {
			return mailDs.saveMail(mail);
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean saveMail(String fromId, int type, String content) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sdf.format(new Date());
			ArrayList<UserPO> list = userData.getAllUser();
			boolean b = true;
			for (UserPO user : list) {
				if (user.getUsertype() == type) {
					MailPO mail = new MailPO(fromId, user.getUserId(), content, time, false);
					b = b && mailDs.saveMail(mail);
				}
			}
			return b;
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean readMail(MailVO mail) {
		try {
			return mailDs.readMail(new MailPO(mail.getFromId(), mail.getToId(), mail.getContent(), mail.getTime(), mail.isRead()));
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public ArrayList<MailVO> getMailList(UserVO user) {
		try {
			ArrayList<MailPO> listpo = mailDs.getMailList(user.toPO());
			ArrayList<MailVO> listvo = new ArrayList<MailVO>();
			for (MailPO mail : listpo) listvo.add(new MailVO(mail.getFromId(), mail.getToId(), mail.getContent(), mail.getTime(), mail.isRead()));
			Collections.sort(listvo, new Comparator<MailVO>() {  
	            @Override  
	            public int compare(MailVO o1, MailVO o2) {
	            	if ((!o1.isRead()) && o2.isRead() ) return -1;
	            	if ((!o2.isRead()) && o1.isRead() ) return 1;
	            	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            	try {
						Date date1 = df.parse(o1.getTime());
						Date date2 = df.parse(o2.getTime());
						return date1.after(date2) ? -1 : 1;
					} catch (ParseException e) {
						e.printStackTrace();
						return 1;
					}
	            }
			});
			return listvo;
		} catch (RemoteException e) {
			e.printStackTrace();
			return new ArrayList<MailVO>();
		}
	}
}
