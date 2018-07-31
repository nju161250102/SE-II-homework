package vo;

public class MailVO {
	private String fromId;
	private String toId;
	private String content;
	private String time;
	private boolean hasRead;
	
	public MailVO(String fromId, String toId, String content, String time, boolean isRead) {
		this.fromId = fromId;
		this.toId = toId;
		this.content = content;
		this.time = time;
		this.hasRead = isRead;
	}

	public String getFromId() {return fromId;}

	public String getToId() {return toId;}

	public String getContent() {return content;}

	public String getTime() {return time;}

	public boolean isRead() {return hasRead;}
}
