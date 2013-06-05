package edu.uci.util.S3;

public interface IS3 {

	public void createBucket();
	
	public void pushObject(String objectName);
	
	public void deleteObject(String objectName);
	
}
