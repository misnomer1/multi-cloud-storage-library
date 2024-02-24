package cloud.storage.lib.cloudproviders;

public class Util {
    public static String getExtension(String key) {
        String[] parts = key.split("\\.");
        String lastPart = parts.length > 0 ? parts[parts.length - 1]: null;
        return (lastPart !=null && !lastPart.isEmpty()) ? lastPart : "";
    }
}
