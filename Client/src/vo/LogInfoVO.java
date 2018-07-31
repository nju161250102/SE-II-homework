package vo;

public class LogInfoVO {

	private String time;
	private String operatorId;
	private String operation;
	private String detail;
	
	/**
	 * 操作记录的构造函数
	 * @param time 操作时间
	 * @param operatorId 操作员id
	 * @param operation 操作名称
	 * @param detail 详情
	 */
	public LogInfoVO(String time, String operatorId, String operation, String detail) {
		this.time = time;
		this.operatorId = operatorId;
		this.operation = operation;
		this.detail = detail;
	}
	
	public String getTime() {
		return time;
	}
	
	public String getOperatorId() {
		return operatorId;
	}
	
	public String getOperation() {
		return operation;
	}
	
	public String getDetail() {
		return detail;
	}
}
