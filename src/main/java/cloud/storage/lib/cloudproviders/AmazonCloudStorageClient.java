package cloud.storage.lib.cloudproviders;


import cloud.storage.lib.compression.CloudCompressType;
import cloud.storage.lib.compression.CloudCompressor;
import cloud.storage.lib.compression.CloudCompressorCreator;
import cloud.storage.lib.compression.CloudStorageLibCompressionException;
import cloud.storage.lib.encryption.EncryptionService;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

public class AmazonCloudStorageClient {

    private final S3Client s3Client;
    private final EncryptionService encryptionService;

    public AmazonCloudStorageClient(S3Client s3Client, EncryptionService encryptionService) {
        this.s3Client = s3Client;
        this.encryptionService = encryptionService;
    }

    public byte[] getObject(String bucket, String key) throws IOException, CloudStorageLibCompressionException {
        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(GetObjectRequest.builder().bucket(bucket).key(key).build());
        GetObjectResponse responseMetadata = response.response();

        byte[] body = encryptionService.decrypt(response.readAllBytes());
        CloudCompressType compressType = CloudCompressType.compressTypeFor(Util.getExtension(key), responseMetadata.contentType(), responseMetadata.contentEncoding());
        CloudCompressor compressor = CloudCompressorCreator.createCompressorFrom(compressType);
        return compressor.compress(body);
    }

    public String putObject(String bucket, String key, byte[] bytes, CloudCompressType cloudCompressType) {
        String keyName = key;
        if (cloudCompressType != CloudCompressType.NONE) {
            keyName = keyName + "." + cloudCompressType.getExtension();
        }

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(keyName)
                .contentType(cloudCompressType.getMimeType())
                .contentEncoding(cloudCompressType.getContentEncoding())
                .build();

        CloudCompressor cloudCompressor = CloudCompressorCreator.createCompressorFrom(cloudCompressType);
        byte[] encryptedBody = encryptionService.encrypt(cloudCompressor.compress(bytes));
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));
        return keyName;
    }

    public String putObject(String bucket, String key, byte[] bytes) {
        return putObject(bucket, key, bytes, CloudCompressType.GZIP);
    }
}
