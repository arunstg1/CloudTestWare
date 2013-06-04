/**
 * 
 */
package server;

import java.util.List;

import com.amazonaws.services.sqs.model.Message;

public interface ISimpleQueuingService {
	
	public void createQueue();
	
	public void sendMessageToSQS(String message);
	
	public List<Message> receiveMessageFromSQS();
	
	public String getMessageAtPosition(int position);
	
	public void deleteMessage(int position);
	
}
