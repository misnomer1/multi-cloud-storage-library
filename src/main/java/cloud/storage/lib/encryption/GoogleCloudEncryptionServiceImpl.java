package cloud.storage.lib.encryption;

import com.google.cloud.kms.v1.CryptoKeyName;
import com.google.cloud.kms.v1.KeyManagementServiceClient;
import com.google.protobuf.ByteString;

public class GoogleCloudEncryptionServiceImpl implements EncryptionService {

    private final String keyName;

    public GoogleCloudEncryptionServiceImpl(String keyName) {
        this.keyName = keyName;
    }

    @Override
    public byte[] encrypt(byte[] bytes) {
        try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
            ByteString plaintext = ByteString.copyFrom(bytes);
            CryptoKeyName cryptoKeyName = CryptoKeyName.parse(keyName);

            return client.encrypt(cryptoKeyName, plaintext).getCiphertext().toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    @Override
    public byte[] decrypt(byte[] bytes) {
        try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
            ByteString ciphertext = ByteString.copyFrom(bytes);
            CryptoKeyName cryptoKeyName = CryptoKeyName.parse(keyName);

            return client.decrypt(cryptoKeyName, ciphertext).getPlaintext().toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}
