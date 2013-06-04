package spike;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class CustomListener extends RunListener{

	@Override
	public void testFailure(Failure failure) throws Exception {
		System.out.println("Failure: " + failure.getMessage());
    }
	
	@Override
	public void testRunFinished(Result result) throws Exception {
		System.out.println(result.toString());
    }
	
	@Override
	 public void testFinished(Description description) throws Exception {
		System.out.println(description.toString());
    }

}
