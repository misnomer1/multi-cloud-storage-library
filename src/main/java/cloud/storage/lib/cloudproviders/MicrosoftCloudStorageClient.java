package cloud.storage.lib.cloudproviders;

import cloud.storage.lib.compression.CloudCompressType;
import cloud.storage.lib.compression.CloudCompressor;
import cloud.storage.lib.compression.CloudCompressorCreator;
import cloud.storage.lib.compression.CloudStorageLibCompressionException;
import cloud.storage.lib.encryption.EncryptionService;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MicrosoftCloudStorageClient {

    private final EncryptionService encryptionService;

    public MicrosoftCloudStorageClient(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    public byte[] getObject(String connectionString, String containerName, String blobName) throws IOException, CloudStorageLibCompressionException {
        BlobClient blobClient = new BlobClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .blobName(blobName)
                .buildClient();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blobClient.download(outputStream);
        byte[] encryptedData = outputStream.toByteArray();

        byte[] decryptedData = encryptionService.decrypt(encryptedData);
        // Assuming CloudCompressor and CloudCompressType are the same as in your AWS example
        CloudCompressType compressType = CloudCompressType.compressTypeFor(Util.getExtension(blobName), blobClient.getProperties().getContentType(), null);
        CloudCompressor compressor = CloudCompressorCreator.createCompressorFrom(compressType);
        return compressor.decompress(decryptedData);
    }

    public String putObject(String connectionString, String containerName, String blobName, byte[] bytes, CloudCompressType cloudCompressType) {
        String keyName = blobName + "." + cloudCompressType.getExtension();
        BlobClient blobClient = new BlobClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .blobName(keyName)
                .buildClient();

        BlobHttpHeaders headers = new BlobHttpHeaders()
                .setContentType(cloudCompressType.getMimeType())
                .setContentEncoding(cloudCompressType.getContentEncoding());

        CloudCompressor cloudCompressor = CloudCompressorCreator.createCompressorFrom(cloudCompressType);
        byte[] compressedData = cloudCompressor.compress(bytes);
        byte[] encryptedData = encryptionService.encrypt(compressedData);

        blobClient.upload(new ByteArrayInputStream(encryptedData), encryptedData.length, true);
        blobClient.setHttpHeaders(headers);

        return keyName;
    }

    public String putObject(String connectionString, String containerName, String blobName, byte[] bytes) {
        return putObject(connectionString, containerName, blobName, bytes, CloudCompressType.GZIP);
    }
}
