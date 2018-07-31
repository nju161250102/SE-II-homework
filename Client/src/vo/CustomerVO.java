package vo;

import po.CustomerPO;

public class CustomerVO {
	private String name;
	private String id;
	private String telNumber;
	private String address;
	private String code;
	private String mail;
	private String salesman;
	private int type;
	private int rank;
	private double recRange;
	private double receivable;
	private double payment;
	/**
	 * 构造函数……
	 * @param id 编号
	 * @param name 姓名
	 * @param type 用户类别
	 * @param rank 用户级别
	 * @param telNumber 电话
	 * @param address 地址
	 * @param code 邮编
	 * @param mail 邮箱
	 * @param recRange 应收额度
	 * @param receivable 应收
	 * @param payment 应付
	 * @param salesman 默认业务员
	 */
	public CustomerVO (String id, String name, int type, int rank, String telNumber, String address, String code, String mail, double recRange, double receivable, double payment, String salesman) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.rank = rank;
		this.telNumber = telNumber;
		this.address = address;
		this.code = code;
		this.mail = mail;
		this.recRange = recRange;
		this.receivable = receivable;
		this.payment = payment;
		this.salesman = salesman;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getType() {
		return this.type;
	}
	
	public int getRank() {
		return this.rank;
	}
	
	public String getTelNumber() {
		return this.telNumber;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public String getCode() {
		return this.getCode();
	}
	
	public String getMail() {
		return this.mail;
	}
	
	public double getRecRange() {
		return this.recRange;
	}
	
	public double getReceivable() {
		return this.receivable;
	}
	
	public double getPayment() {
		return this.payment;
	}
	
	public String getSalesman() {
		return this.salesman;
	}
	
	public CustomerPO toPO() {
		CustomerPO customerPO = new CustomerPO(id, name, telNumber, address, mail, code, salesman,
				rank, type, recRange, receivable, payment, true);
		return customerPO;
	}
}
