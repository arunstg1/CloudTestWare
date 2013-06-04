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
	
	public static void main(String args[]){
		
		if(args.length < 3){
			System.out.println("Usage: TestClient.jar <run type> <test library jar> <optional source and dependent jars>");
			System.out.println("run type is either \"TestInvoker\" or \"TestRunner\"");
			return;
		}
		if(args[0] == "TestInvoker")
			try {
				new TestInvoker().process(args);
			} catch (IOException e) {
				System.out.println("Error: " + e.getMessage());
			}
		else
			processTestRunner(args);
	   }

	private static void processTestRunner(String[] args) {
		throw new NotImplementedException();
	}
}
