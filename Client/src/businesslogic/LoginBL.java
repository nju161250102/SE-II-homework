package businesslogic;

import blservice.LoginBLService;
import dataservice.UserDataService;
import ds_stub.UserDs_stub;
import po.UserPO;
import rmi.Rmi;
import vo.UserType;
import vo.UserVO;

public class LoginBL implements LoginBLService {

	@Override
	public UserVO getUser(String id, String password){
		UserDataService userDataService = Rmi.flag ? Rmi.getRemote(UserDataService.class) : new UserDs_stub();
		
		try {
			UserPO user = userDataService.findById(id);
			if (user != null && password.equals(user.getUserPwd())) {
				UserVO userVO =  new UserVO(
						user.getUserName(), 
						user.getUserPwd(),
						UserType.getType(user.getUsertype()),
						user.getUserRank(),
						user.getUserId(),
						user.getUserSex(),
						user.getUserTelNumber(),
						user.getUserAge());
				return userVO;
			}
			else return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
