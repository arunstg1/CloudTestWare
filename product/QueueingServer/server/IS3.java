package server;

public interface IS3 {

	public void createBucket(String bucketName);
	
	public void pushObject(String objectName);
	
	public void deleteObject();
	
}
