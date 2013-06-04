package spike;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import junit.framework.Test;

import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

public class Testfetcher {

	public static void main (String args[]) throws IOException
    {
      
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
          // -6 because of .class
          String className = je.getName().substring(0,je.getName().length()-6);
          className = className.replace('/', '.');
          try {
			Class c = cl.loadClass(className);
			System.out.println(c.getName());
			 TestClass testClass = new TestClass(c);
	    	  List<FrameworkMethod> testList = testClass.getAnnotatedMethods(org.junit.Test.class);
	    	  List<FrameworkMethod> BeforeList = testClass.getAnnotatedMethods(org.junit.Before.class);
	    	  List<FrameworkMethod> AfterList = testClass.getAnnotatedMethods(org.junit.After.class);
	    	  
	    	  for (FrameworkMethod frameworkMethod : testList) {
	    		  String name = testClass.getName();
	    	      String method = frameworkMethod.getName();
	    	      System.out.println(name + "\t" + method + "\t");
	    	      RunNotifier notifier = new RunNotifier();
	    	      RunListener listener = new CustomListener();
	    	      notifier.addListener(listener);
	    	      new CustomRunner(c).runMethod(method, notifier);
	    	      System.out.println(notifier.toString());
			}
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
          catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

      }
      jarFile.close();
    }

}
