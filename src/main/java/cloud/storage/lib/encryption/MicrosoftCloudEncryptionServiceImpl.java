package cloud.storage.lib.encryption;

import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.security.keyvault.keys.cryptography.CryptographyClient;
import com.azure.security.keyvault.keys.cryptography.CryptographyClientBuilder;
import com.azure.security.keyvault.keys.cryptography.models.EncryptResult;
import com.azure.security.keyvault.keys.cryptography.models.DecryptResult;
import com.azure.security.keyvault.keys.cryptography.models.EncryptionAlgorithm;

public class MicrosoftCloudEncryptionServiceImpl implements EncryptionService {

    private final CryptographyClient cryptographyClient;

    public MicrosoftCloudEncryptionServiceImpl(String keyIdentifier, String clientId, String clientSecret, String tenantId) {
        this.cryptographyClient = new CryptographyClientBuilder()
                .keyIdentifier(keyIdentifier)
                .credential(new ClientSecretCredentialBuilder()
                        .clientId(clientId)
                        .clientSecret(clientSecret)
                        .tenantId(tenantId)
                        .build())
                .buildClient();
    }

    @Override
    public byte[] encrypt(byte[] bytes) {
        EncryptResult result = cryptographyClient.encrypt(EncryptionAlgorithm.RSA_OAEP, bytes);
        return result.getCipherText();
    }

    @Override
    public byte[] decrypt(byte[] bytes) {
        DecryptResult result = cryptographyClient.decrypt(EncryptionAlgorithm.RSA_OAEP, bytes);
        return result.getPlainText();
    }
}

