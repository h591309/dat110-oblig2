package no.hvl.dat110.messages;

public class SubscribeMsg extends Message {
	
	private String topic;
	private String user;
	private MessageType type;
	
	public SubscribeMsg(MessageType type, String user, String topic) {
		super();
		this.type = MessageType.SUBSCRIBE;
		
	}
	
	// message sent from client to subscribe on a topic 

	// TODO:
	// Implement object variables - a topic is required

	// Constructor, get/set-methods, and toString method
	// as described in the project text
		
}
