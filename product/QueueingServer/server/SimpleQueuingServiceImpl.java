package server;

import java.util.List;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class SimpleQueuingServiceImpl implements ISimpleQueuingService {
	
	AmazonSQS sqs;
	Region usWest2;
	String myQueueURL;
	List<Message> messages;
	
	public SimpleQueuingServiceImpl() {
		sqs = new AmazonSQSClient(new ClasspathPropertiesFileCredentialsProvider());
		usWest2 = Region.getRegion(Regions.US_WEST_2);
		sqs.setRegion(usWest2);
	}

	@Override
	public void createQueue(String queueName) {
		CreateQueueRequest createQueueRequest = new CreateQueueRequest("MyQueue");
        myQueueURL = sqs.createQueue(createQueueRequest).getQueueUrl();
	}

	@Override
	public void sendMessageToSQS(String message) {
		sqs.sendMessage(new SendMessageRequest(myQueueURL, message));
	}

	@Override
	public void receiveMessageFromSQS() {
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueURL);
		messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
	}
	
	public String getMessageAtPosition(int position) {
		return messages.get(position).getBody();
	}

	@Override
	public void deleteMessage(int position) {
		String messageRecieptHandle = messages.get(position).getReceiptHandle();
        sqs.deleteMessage(new DeleteMessageRequest(myQueueURL, messageRecieptHandle));
	}

}
