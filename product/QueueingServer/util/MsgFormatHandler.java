package util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import server.MsgFormat;

public class MsgFormatHandler {
	
	public static String marshallMsg(MsgFormat msgFormat) throws JAXBException {
		StringWriter writer = new StringWriter();
		JAXBContext context = JAXBContext.newInstance(MsgFormat.class);
		Marshaller m = context.createMarshaller();
		m.marshal(msgFormat, writer);
		return writer.toString();
	}
	
	public static MsgFormat unmarshallMsg(String message) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(MsgFormat.class);
		Unmarshaller um = context.createUnmarshaller();
		return (MsgFormat)um.unmarshal(new StringReader(message.toString()));
	}
	
	/*public static void main(String[] args) throws JAXBException {
		MsgFormat msg = new MsgFormat();
		msg.setPublisher("publisher");
		msg.setSubscriber("subscriber");
		msg.setS3URL("s3url");
		msg.setTestName("testName");
		msg.setTestTool("testTool");
		
		String str = MsgFormatHandler.marshallMsg(msg);
		System.out.println(str);
		
		MsgFormat msgF = MsgFormatHandler.unmarshallMsg(str);
		System.out.println(msgF.getSubscriber());
	}*/

}
