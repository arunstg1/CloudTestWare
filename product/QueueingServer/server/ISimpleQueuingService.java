/**
 * 
 */
package server;

public interface ISimpleQueuingService {
	
	public void createQueue(String queueName);
	
	public void sendMessageToSQS(String message);
	
	public void receiveMessageFromSQS();
	
	public String getMessageAtPosition(int position);
	
	public void deleteMessage(int position);
	
}
