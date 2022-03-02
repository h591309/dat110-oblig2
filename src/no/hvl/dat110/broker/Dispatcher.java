package no.hvl.dat110.broker;

import java.util.Set;
import java.util.Collection;

import no.hvl.dat110.common.TODO;
import no.hvl.dat110.common.Logger;
import no.hvl.dat110.common.Stopable;
import no.hvl.dat110.messages.*;
import no.hvl.dat110.messagetransport.Connection;

public class Dispatcher extends Stopable {

	private Storage storage;

	public Dispatcher(Storage storage) {
		super("Dispatcher");
		this.storage = storage;

	}

	@Override
	public void doProcess() {

		Collection<ClientSession> clients = storage.getSessions();

		Logger.lg(".");
		for (ClientSession client : clients) {

			Message msg = null;

			if (client.hasData()) {
				msg = client.receive();
			}

			// a message was received
			if (msg != null) {
				dispatch(client, msg);
			}
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void dispatch(ClientSession client, Message msg) {

		MessageType type = msg.getType();

		// invoke the appropriate handler method
		switch (type) {

		case DISCONNECT:
			onDisconnect((DisconnectMsg) msg);
			break;

		case CREATETOPIC:
			onCreateTopic((CreateTopicMsg) msg);
			break;

		case DELETETOPIC:
			onDeleteTopic((DeleteTopicMsg) msg);
			break;

		case SUBSCRIBE:
			onSubscribe((SubscribeMsg) msg);
			break;

		case UNSUBSCRIBE:
			onUnsubscribe((UnsubscribeMsg) msg);
			break;

		case PUBLISH:
			onPublish((PublishMsg) msg);
			break;

		default:
			Logger.log("broker dispatch - unhandled message type");
			break;

		}
	}

	// called from Broker after having established the underlying connection
	public void onConnect(ConnectMsg msg, Connection connection) {

		String user = msg.getUser();

		Logger.log("onConnect:" + msg.toString());

		storage.addClientSession(user, connection);

	}

	// called by dispatch upon receiving a disconnect message
	public void onDisconnect(DisconnectMsg msg) {

		String user = msg.getUser();

		Logger.log("onDisconnect:" + msg.toString());

		storage.removeClientSession(user);

	}

	public void onCreateTopic(CreateTopicMsg msg) {

		String topic = msg.getTopic();
		if(!storage.getTopics().contains(topic)) {
			storage.createTopic(topic);
			Logger.log("onCreateTopic:" + msg.toString());
		} else {
			System.out.println("Non-critical error: Topic already exists!");
		}
			
		// TODO: create the topic in the broker storage
		// the topic is contained in the create topic message

//		throw new UnsupportedOperationException(TODO.method());

	}

	public void onDeleteTopic(DeleteTopicMsg msg) {

		String topic = msg.getTopic();
		storage.deleteTopic(topic);
		Logger.log("onDeleteTopic:" + msg.toString());

		// TODO: delete the topic from the broker storage
		// the topic is contained in the delete topic message
		
//		throw new UnsupportedOperationException(TODO.method());
	}

	public void onSubscribe(SubscribeMsg msg) {
		String user = msg.getUser();
		String topic = msg.getTopic();
		if(storage.getTopics().contains(topic)) {
			storage.addSubscriber(user, topic);
			Logger.log("onSubscribe:" + msg.toString());
			System.out.println("Topic: " + topic + " - Subscribers: " + storage.getSubscribers(topic).size());
		} else {
			System.out.println("Non-critical error: Topic does not exist!");
		}
			
		
		// TODO: subscribe user to the topic
		// user and topic is contained in the subscribe message
		
//		throw new UnsupportedOperationException(TODO.method());

	}

	public void onUnsubscribe(UnsubscribeMsg msg) {
		String user = msg.getUser();
		String topic = msg.getTopic();
		
		if(storage.getTopics().contains(topic)) {
			storage.removeSubscriber(user, topic);
			Logger.log("onUnsubscribe:" + msg.toString());
			System.out.println("Topic: " + topic + " - Subscribers: " + storage.getSubscribers(topic).size());
		} else {
			System.out.println("Non-critical error: Topic does not exist!");
		}
		// TODO: unsubscribe user to the topic
		// user and topic is contained in the unsubscribe message
		
//		throw new UnsupportedOperationException(TODO.method());
	}

	public void onPublish(PublishMsg msg) {
		
		String user = msg.getUser();
		String topic = msg.getTopic();
		if(storage.getTopics().contains(topic)) {
			Set<String> subscribers = storage.getSubscribers(topic);
			for(String s : subscribers) {
				ClientSession session = storage.getSession(s);
				session.send(msg);
			}
			System.out.println("Published: " + msg.getMessage() + " - Topic : " + topic);
		}
		
		Logger.log("onPublish:" + msg.toString());

		// TODO: publish the message to clients subscribed to the topic
		// topic and message is contained in the subscribe message
		// messages must be sent using the corresponding client session objects
		
//		throw new UnsupportedOperationException(TODO.method());

	}
}
