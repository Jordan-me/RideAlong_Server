package demo;

import java.util.Date;

public class Message {
	private String message;
	private Date messageTimestamp;
	
	public Message() {
		this.messageTimestamp = new Date();
	}

	public Message(String message) {
		this();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public Date getMessageTimestamp() {
		return messageTimestamp;
	}

	public void setMessageTimestamp(Date messageTimestamp) {
		this.messageTimestamp = messageTimestamp;
	}

	@Override
	public String toString() {
		return "Message [message=" + message + "]";
	}
}
