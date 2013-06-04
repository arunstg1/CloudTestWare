package edu.uci.testclient;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;


public class Main {
	
	public static void main(String args[]) throws IOException{
		
		if(args.length < 3){
			System.out.println("Usage: TestClient.jar <run type> <test library jar> <optional source and dependent jars>");
			System.out.println("run type is either \"TestInvoker\" or \"TestRunner\"");
			return;
		}
		if(args[0] == "Testinvoker")
			processTestInvoker(args);
		else
			processTestRunner(args);
	   }

	private static void processTestRunner(String[] args) {
		throw new NotImplementedException();
	}

	private static void processTestInvoker(String[] args) throws IOException {
	    JarFile jarFile = new JarFile(args[0]);
	    Enumeration<JarEntry> e = jarFile.entries();

	    URL[] urls = new URL[args.length];
	    int argIndex = 0;
	    for (String string : args) {
			urls[argIndex] = new URL("jar:file:" + args[argIndex++] +"!/");
		}
	    URLClassLoader cl = URLClassLoader.newInstance(urls);

	    while (e.hasMoreElements()) {
	       JarEntry je = (JarEntry) e.nextElement();
           if(je.isDirectory() || !je.getName().endsWith(".class") || je.getName().contains("$")){
               continue;
           }	
	
	       String className = je.getName().substring(0,je.getName().length()-6);
		   className = className.replace('/', '.');
		   try {
			   Class c = cl.loadClass(className);
			   System.out.println(c.getName());
			   TestClass testClass = new TestClass(c);
			   List<FrameworkMethod> testList = testClass.getAnnotatedMethods(org.junit.Test.class);
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
