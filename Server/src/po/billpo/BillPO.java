package po.billpo;

import java.io.Serializable;

public abstract class BillPO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5233390848113316495L;
	public static int DRAFT = 0;
	public static int SAVED = 1;
	public static int COMMITED = 2;
	public static int PASS = 3;
	public static int NOTPASS = 4;
	
	private String date;
	private String time;
	private String id;
	private String operator;
	private int state;
	
	/**
	 * BillVO的构造方法
	 * @param date 制定日期
	 * @param time 制定时间
	 * @param id 每一天之内的编号
	 * @param operator 操作人员的id
	 */
	public BillPO(){};
	public BillPO(String date, String time, String id, String operatorId, int state) {
		super();
		this.date = date;
		this.time = time;
		this.id = id;
		this.operator = operatorId;
		this.state = state;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getTime() {
		return time;
	}
	
	public String getId() {
		return id;
	}
	
	public String getOperator() {
		return operator;
	}
	
	public int getState() {
		return state;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setOperatorId(String operatorId) {
		this.operator = operatorId;
	}
	
	public void setState(int n) {
		state = n;
	}
	/**
	 * 修改此处名称->修改MainPanel中能否删除的判断
	 * @return 单据状态的中文名称
	 */
	public String getStateName() {
		switch(state) {
			case 0 : return "草稿";
			case 1 : return "已保存";
			case 2 : return "已提交";
			case 3 : return "审批通过";
			case 4 : return "审批未通过";
		}
		return "未知状态";
	}
	/**
	 * 
	 * @return 返回PO对象完整的Id
	 */
	public abstract String getAllId();
}
