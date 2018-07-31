package vo;

import po.UserPO;

public class UserVO {
	private String name;
	private String id;
	private String sex;
	private String telNumber;
	private int age;
	private String key;
	private UserType type;
	private int rank;
	/**
	 * 构造函数……
	 * @param name 姓名
	 * @param key 密码
	 * @param type 用户类别
	 * @param id 编号
	 * @param sex 性别
	 * @param telNumber 电话
	 * @param age 年龄
	 */
	public UserVO(String name, String key, UserType type, int rank, String id, String sex, String telNumber, int age) {
		this.name = name;
		this.key = key;
		this.type = type;
		this.rank = rank;
		this.id = id;
		this.sex = sex;
		this.telNumber = telNumber;
		this.age = age;
	}
	
	public UserType getType() {
		return this.type;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getRank(){
	    return this.rank;
	}
	
	public String getPwd() {
		return this.key;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getSex() {
		return this.sex;
	}
	
	public String getTelNumber() {
		return this.telNumber;
	}
	
	public int getAge() {
		return this.age;
	}
	
	public UserPO toPO() {
		UserPO userPO = new UserPO(id, name, key, sex, telNumber, type.getNum(), rank, age, true);
		return userPO;
	}

	public String getRankName() {
		if (type == UserType.SALESMAN) {
    		if (rank == 0) return "普通销售员";
    		if (rank == 1) return "销售经理";
    	}
    	else if (type == UserType.ACCOUNTANT) {
    		if (rank == 0) return "普通财务人员";
    		if (rank == 1) return "最高权限财务人员";
    	}
    	return "默认";
	}
}
