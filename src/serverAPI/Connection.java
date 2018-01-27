package serverAPI;

//An abstract representation of a network connection (either a server or a client)
public abstract class Connection {
	MessageReceiver receiver;
	
	public Connection(MessageReceiver rec){
		receiver = rec;
	}
	
	public final void setReceiver(MessageReceiver rec){
		receiver = rec;
	}
	
	public abstract boolean isClosed();
	
	public abstract void close();
	
	public abstract void println(String message);
}