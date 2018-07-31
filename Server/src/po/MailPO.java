package po;

import java.io.Serializable;



public class MailPO implements Serializable{

	private static final long serialVersionUID = -5509083836562333554L;
	
	private String fromId;
	private String toId;
	private String content;
	private String time;
	private boolean hasRead;

	public MailPO(String fromId, String toId, String content, String time, boolean hasRead) {

		this.fromId = fromId;
		this.toId = toId;
		this.content = content;
		this.time = time;
		this.hasRead = hasRead;
	}

	public String getFromId() {return fromId;}

	public String getToId() {return toId;}

	public String getContent() {return content;}

	public String getTime() {return time;}

	public boolean isRead() {return hasRead;}

	public void setRead(boolean b) {this.hasRead = b;}

}