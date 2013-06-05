package edu.uci.testclient;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import edu.uci.util.MsgFormat;
import edu.uci.util.MsgFormatHandler;
import edu.uci.util.SQS.SimpleQueuingServiceImpl;

public class TestInvoker {

	private ZipFileCreator zipfileCreator;
	private S3Uploader s3Uploader;
	private SimpleQueuingServiceImpl simpleQueueingService = new SimpleQueuingServiceImpl("Active");
	private MsgFormatHandler msgFormatHandler;

	public TestInvoker(){
		zipfileCreator = new ZipFileCreator();
		s3Uploader = new S3Uploader();
		msgFormatHandler = new MsgFormatHandler();
	}
	
	public void process(String[] args) throws IOException {
			UUID testId = UUID.randomUUID();
			zipfileCreator.zip(args, testId);
			s3Uploader.upload(testId);
			FileUtils.deleteQuietly(new File(testId + ".zip"));
			
		 	JarFile jarFile = new JarFile(args[1]);
		    Enumeration<JarEntry> e = jarFile.entries();

		    URL[] urls = new URL[args.length];
		    int argIndex = 0;
		    for (String string : args) {
				urls[argIndex] = new URL("jar:file:" + args[argIndex++] +"!/");
			}
		    URLClassLoader cl = URLClassLoader.newInstance(urls);

		    while (e.hasMoreElements()) {
		       JarEntry je = (JarEntry) e.nextElement();
	           if(je.isDirectory() || !je.getName().endsWith("Test.class") || je.getName().contains("$")){
	               continue;
	           }	
		
		       String className = je.getName().substring(0,je.getName().length()-6);
			   className = className.replace('/', '.');
			   try {
				   Class c = cl.loadClass(className);
				   System.out.println(c.getName());
				   TestClass testClass = new TestClass(c);
				   List<FrameworkMethod> testList = testClass.getAnnotatedMethods(org.junit.Test.class);
				   for (FrameworkMethod frameworkMethod : testList) {
					   MsgFormat msg = new MsgFormat();
					   msg.setTestName(c.getName() + "." + frameworkMethod.getName());
					   msg.setS3URL(s3Uploader.bucketUrl() + testId + ".zip");
					   String marshalledMsg = msgFormatHandler.marshallMsg(msg);
					   simpleQueueingService.sendMessageToSQS(marshalledMsg);
				   }
			   } 
			   catch (ClassNotFoundException e1) {
				   e1.printStackTrace();
			   } 
			   catch (Exception e1) {
				   e1.printStackTrace();
			   }
			
		}
		jarFile.close();
	}

}
