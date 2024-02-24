package cloud.storage.lib.encryption;

import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.caching.CachingCryptoMaterialsManager;
import com.amazonaws.encryptionsdk.caching.LocalCryptoMaterialsCache;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;
import software.amazon.awssdk.services.kms.KmsClientBuilder;

import java.util.concurrent.TimeUnit;

public class AmazonCloudEncryptionServiceImpl implements EncryptionService {

    private final AwsCrypto awsCrypto;
    private final CachingCryptoMaterialsManager cachingCryptoMaterialsManager;
    public AmazonCloudEncryptionServiceImpl(String kmsArn, KmsClientBuilder kmsClientBuilder) {
        this.cachingCryptoMaterialsManager = CachingCryptoMaterialsManager.newBuilder()
                .withMasterKeyProvider(KmsMasterKeyProvider.builder().buildStrict(kmsArn))
                .withCache(new LocalCryptoMaterialsCache(10000))
                .withMaxAge(30, TimeUnit.MINUTES)
                .withMessageUseLimit(1000L).build();
        this.awsCrypto = AwsCrypto.standard();
    }

    @Override
    public byte[] encrypt(byte[] bytes) {
        return awsCrypto.encryptData(cachingCryptoMaterialsManager, bytes).getResult();
    }

    @Override
    public byte[] decrypt(byte[] bytes) {
        return awsCrypto.decryptData(cachingCryptoMaterialsManager, bytes).getResult();
    }
}
