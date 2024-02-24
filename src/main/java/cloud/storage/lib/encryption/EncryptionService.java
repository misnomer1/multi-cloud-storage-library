package cloud.storage.lib.encryption;

public interface EncryptionService {

    public byte[] encrypt(byte[] bytes);
    public byte[] decrypt(byte[] bytes);
}
