package po;

import java.io.Serializable;

public class LogInfoPO implements Serializable{
	private static final long serialVersionUID = 5301482844230269082L;
	private String time, operatorId, operation, detail;
	
	public LogInfoPO(String time, String operatorId, String operation, String detail) {
		this.time = time;
		this.operatorId = operatorId;
		this.operation = operation;
		this.detail = detail;
	}
	
	public String getTime() {return time;}
	
	public String getOperatorId() {return operatorId;}
	
	public String getOperation() {return operation;}
	
	public String getDetail() {return detail;}
}