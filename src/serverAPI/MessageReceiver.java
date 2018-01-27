package serverAPI;

//A receiver to send incoming messages to
public interface MessageReceiver{
	void receiveMessage(String message);
}