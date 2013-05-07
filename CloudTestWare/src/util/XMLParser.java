package util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser {

	private String fileName;
	private Document msgFormatDoc;

	XMLParser() {
		fileName = "MessageFormat.xml";
	}

	private void readFile() throws ParserConfigurationException, SAXException,
			IOException {
		File msgFormat = new File(fileName);
		DocumentBuilderFactory docbuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docuBuilder = docbuilderFactory.newDocumentBuilder();
		msgFormatDoc = docuBuilder.parse(msgFormat);
		msgFormatDoc.getDocumentElement().normalize();
	}

	private void addTagDesc(String tagName, String textContent) {
		NodeList nodes = msgFormatDoc.getElementsByTagName(tagName);
		nodes.item(0).setTextContent(textContent);
	}

	private String convertXmlToString() throws TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(msgFormatDoc), new StreamResult(
				writer));

		String xmlConvertedToString = writer.getBuffer().toString();
		System.out.println(xmlConvertedToString);
		return xmlConvertedToString;
	}

	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException, TransformerException {
		XMLParser p = new XMLParser();
		p.readFile();
		p.addTagDesc("BucketName", "cs237");
		p.convertXmlToString();
	}

}
