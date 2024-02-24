package cloud.storage.lib.multicloud;

import cloud.storage.lib.cloudproviders.AmazonCloudStorageClient;
import cloud.storage.lib.cloudproviders.GoogleCloudStorageClient;
import cloud.storage.lib.cloudproviders.MicrosoftCloudStorageClient;
import cloud.storage.lib.compression.CloudCompressType;
import cloud.storage.lib.compression.CloudStorageLibCompressionException;

import java.io.IOException;

public class MultiCloudStorageService {

    private AmazonCloudStorageClient amazonStorageClient;
    private GoogleCloudStorageClient googleStorageClient;
    private MicrosoftCloudStorageClient microsoftStorageClient;

    public MultiCloudStorageService(AmazonCloudStorageClient amazonStorageClient,
                                    GoogleCloudStorageClient googleStorageClient,
                                    MicrosoftCloudStorageClient microsoftStorageClient) {
        this.amazonStorageClient = amazonStorageClient;
        this.googleStorageClient = googleStorageClient;
        this.microsoftStorageClient = microsoftStorageClient;
    }

    public String putObject(String bucketName, String objectKey, byte[] data, CloudCompressType compressType) throws IOException {
        // AWS S3
        amazonStorageClient.putObject(bucketName, objectKey, data, compressType);

        // Google Cloud Storage
        googleStorageClient.putObject(bucketName, objectKey, data, compressType);

        // Azure Blob Storage
        microsoftStorageClient.putObject("connection", bucketName, objectKey, data, compressType);

        return objectKey;
    }

    public byte[] getObject(String bucketName, String objectKey, CloudProvider cloudProvider) throws IOException, CloudStorageLibCompressionException {

        switch (cloudProvider) {
            case AMAZON:
                return amazonStorageClient.getObject(bucketName, objectKey);
            case GOOGLE:
                return googleStorageClient.getObject(bucketName, objectKey);
            case MICROSOFT:
                return microsoftStorageClient.getObject("connection", bucketName, objectKey);
            default:
                throw new IllegalArgumentException("Unsupported cloud provider: " + cloudProvider);
        }
    }

    public enum CloudProvider {
        AMAZON,
        GOOGLE,
        MICROSOFT
    }
}

