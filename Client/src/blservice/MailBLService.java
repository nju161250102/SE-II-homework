package blservice;

import java.util.ArrayList;

import vo.MailVO;
import vo.UserVO;

public interface MailBLService {
	/**
	 * 保存邮件
	 * @param fromId 发件人id编号
	 * @param toId 收件人id编号
	 * @param content 邮件正文内容
	 * @return 是否发送成功
	 */
	public boolean saveMail(String fromId, String toId, String content);
	/**
	 * 群发邮件
	 * @param fromId 发件人id编号
	 * @param type 收件人类别
	 * @param content 邮件正文内容
	 * @return 是否发送成功
	 */
	public boolean saveMail(String fromId, int type, String content);
	/**
	 * 将一封邮件设为已读
	 * @param mail 已读的邮件
	 * @return 是否设置成功
	 */
	public boolean readMail(MailVO mail);
	/**
	 * 得到一个用户收到的所有邮件列表（包括已读和未读）
	 * @param user 持久化的 用户
	 * @return 邮件列表
	 */
	public ArrayList<MailVO> getMailList(UserVO user);
}
