package no.hvl.dat110.messages;

public class SubscribeMsg extends Message {
	
	private String topic;
	private String user;
	
	
	public SubscribeMsg(String topic, String user) {
		super(MessageType.SUBSCRIBE, user);
		this.topic = topic;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "SubscribeMsg [topic=" + topic + ", user=" + user + "]";
	}
	
	
	
	// message sent from client to subscribe on a topic 

	// TODO:
	// Implement object variables - a topic is required

	// Constructor, get/set-methods, and toString method
	// as described in the project text
		
}
