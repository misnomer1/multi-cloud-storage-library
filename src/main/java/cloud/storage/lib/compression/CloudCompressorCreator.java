package cloud.storage.lib.compression;

import java.util.HashMap;
import java.util.Map;

public class CloudCompressorCreator {
    private static final Map<CloudCompressType, CloudCompressor> COMPRESS_TYPE_TO_COMPRESSOR = getCompressTypeToCompressorMap();

    public static CloudCompressor createCompressorFrom(final CloudCompressType compressType) {
        switch (compressType) {
            case NONE:
                return new NoneCloudCompressor();
            case ZSTD:
                return new ZstdCloudCompressor();
            case GZIP:
                return new GZipCloudCompressor();
            default:
                throw new UnsupportedOperationException(compressType.name() + " is an unsupported CloudCompressor name");
        }
    }

    private static Map<CloudCompressType, CloudCompressor> getCompressTypeToCompressorMap() {
        Map<CloudCompressType, CloudCompressor> map = new HashMap<>();
        for (CloudCompressType compressType : CloudCompressType.values()) {
            map.put(compressType, createCompressorFrom(compressType));
        }
        return map;
    }
}
