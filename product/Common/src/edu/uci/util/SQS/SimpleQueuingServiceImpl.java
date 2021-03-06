package edu.uci.util.SQS;

import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class SimpleQueuingServiceImpl implements ISimpleQueuingService {
	
	AmazonSQS sqs;
	Region usWest2;
	String myQueueURL;
	String queueName;
	List<Message> messages;
	
	public SimpleQueuingServiceImpl(String queueName) {
		sqs = new AmazonSQSClient(new AWSCredentials() {
			
			@Override
			public String getAWSSecretKey() {
				return "oSSzTsoesWeF6I7NX2yMaBbP1D0Z6gNJAY86C8pi";
			}
			
			@Override
			public String getAWSAccessKeyId() {
				return "AKIAJLTYIAVJS3IP2QIA";
			}
		});
		usWest2 = Region.getRegion(Regions.US_WEST_2);
		sqs.setRegion(usWest2);
		this.queueName = queueName;
	}

	@Override
	public void createQueue() {
		CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName);
        myQueueURL = sqs.createQueue(createQueueRequest).getQueueUrl();
        System.out.println(myQueueURL);
	}

	@Override
	public void sendMessageToSQS(String message) {
		GetQueueUrlResult queueUrl = sqs.getQueueUrl(new GetQueueUrlRequest(queueName));
		sqs.sendMessage(new SendMessageRequest(queueUrl.getQueueUrl(), message));
	}

	@Override
	public List<Message> receiveMessageFromSQS() {
		GetQueueUrlResult queueUrl = sqs.getQueueUrl(new GetQueueUrlRequest(queueName));
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl.getQueueUrl());
		return sqs.receiveMessage(receiveMessageRequest).getMessages();
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
