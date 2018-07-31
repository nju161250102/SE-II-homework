package blservice;

import vo.UserVO;

/**
 * 登录时使用的接口
 * @author 钱美缘
 */
public interface LoginBLService {
	
	/**
	 * 根据用户输入的ID和密码进行身份验证
	 * @param id 用户的登录名
	 * @param password 用户输入的密码
	 * @return 登录失败返回null，成功返回对应的UserVO
	 */
	public UserVO getUser(String id, String password);
	
}
