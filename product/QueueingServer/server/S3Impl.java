package server;

import java.io.File;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class S3Impl implements IS3 {
	
	AmazonS3 s3;
	Region usWest2;
	String bucketName;
	String objectName;
	
	public S3Impl() {
		s3 = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());
		usWest2 = Region.getRegion(Regions.US_WEST_2);
		s3.setRegion(usWest2);
	}

	@Override
	public void createBucket(String bucketName) {
		this.bucketName = bucketName;
		CannedAccessControlList acl = CannedAccessControlList.PublicRead;
        s3.createBucket(bucketName);
        s3.setBucketAcl(bucketName, acl);
	}

	@Override
	public void pushObject(String objectName) {
		this.objectName = objectName;
		PutObjectRequest por = new PutObjectRequest(bucketName,  objectName, new File(objectName)).withCannedAcl(CannedAccessControlList.PublicRead);
        s3.putObject(por);
	}

	@Override
	public void deleteObject() {
		s3.deleteObject(bucketName, objectName);
	}
	
	

}
