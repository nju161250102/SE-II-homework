package vo;

public enum UserType {
	ADMIN("系统管理员", 4),KEEPER("库存管理员", 0),SALESMAN("进货销售人员",1),ACCOUNTANT("财务人员",2),GM("总经理",3);
	private String name;
	private int num;
	
	private UserType(String name, int num) {
		this.name = name;
		this.num = num;
	}
	
	public int getNum() {
		return this.num;
	}
	
	public String getName() {
		return this.name;
	}
	/**
	 * 将int转化为UserType
	 * @param n 数据库里存储的类型标识
	 * @return UserType枚举类对象
	 */
	public static UserType getType(int n) {
		switch (n) {
		case 0 : return KEEPER;
		case 1 : return SALESMAN;
		case 2 : return ACCOUNTANT;
		case 3 : return GM;
		case 4 : return ADMIN;
		default : return null;
		}
	}
}
