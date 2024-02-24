package cloud.storage.lib.cloudproviders;

import cloud.storage.lib.compression.CloudCompressType;
import cloud.storage.lib.compression.CloudCompressor;
import cloud.storage.lib.compression.CloudCompressorCreator;
import cloud.storage.lib.compression.CloudStorageLibCompressionException;
import cloud.storage.lib.encryption.EncryptionService;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.IOException;

public class GoogleCloudStorageClient {

    private final Storage storage;
    private final EncryptionService encryptionService;

    public GoogleCloudStorageClient(EncryptionService encryptionService) {
        this.storage = StorageOptions.getDefaultInstance().getService();
        this.encryptionService = encryptionService;
    }

    public byte[] getObject(String bucket, String key) throws IOException, CloudStorageLibCompressionException {
        Blob blob = storage.get(BlobId.of(bucket, key));
        byte[] encryptedData = blob.getContent();

        byte[] decryptedData = encryptionService.decrypt(encryptedData);
        // Assuming CloudCompressor and CloudCompressType are the same as in your AWS example
        CloudCompressType compressType = CloudCompressType.compressTypeFor(Util.getExtension(key), blob.getContentType(), null);
        CloudCompressor compressor = CloudCompressorCreator.createCompressorFrom(compressType);
        return compressor.decompress(decryptedData);
    }

    public String putObject(String bucket, String key, byte[] bytes, CloudCompressType cloudCompressType) {
        String keyName = key + "." + cloudCompressType.getExtension();
        BlobId blobId = BlobId.of(bucket, keyName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(cloudCompressType.getMimeType())
                .setContentEncoding(cloudCompressType.getContentEncoding())
                .build();

        CloudCompressor cloudCompressor = CloudCompressorCreator.createCompressorFrom(cloudCompressType);
        byte[] compressedData = cloudCompressor.compress(bytes);
        byte[] encryptedData = encryptionService.encrypt(compressedData);

        storage.create(blobInfo, encryptedData);
        return keyName;
    }

    public String putObject(String bucket, String key, byte[] bytes) {
        return putObject(bucket, key, bytes, CloudCompressType.GZIP);
    }
}
