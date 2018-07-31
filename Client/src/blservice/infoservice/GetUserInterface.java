package blservice.infoservice;

import vo.UserVO;

public interface GetUserInterface {

	/**
	 * 根据完整ID得到用户VO对象
	 * @param id 完整的id
	 * @return
	 */
	public UserVO getUser(String id);
}
