package cloud.storage.lib.compression;

import java.util.Objects;
import static cloud.storage.lib.compression.CloudCompressionConstants.*;

public enum CloudCompressType {

    NONE("", "", ""),

    ZSTD(ZSTD_EXTENSION, ZSTD_CONTENTTYPE, ZSTD_CONTENTENCODING),

    GZIP(GZIP_EXTENSION, GZIP_CONTENTTYPE, GZIP_CONTENTENCODING);

    private final String extension;
    private final String mimeType;
    private final String contentEncoding;

    private CloudCompressType(String extension, String mimeType, String contentEncoding) {
        this.extension = extension;
        this.mimeType = mimeType;
        this.contentEncoding = contentEncoding;
    }

    public String getExtension() {
        return extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getContentEncoding() {
        return contentEncoding;

    }

    public static CloudCompressType compressTypeFor(String extension) throws CloudStorageLibCompressionException {
        Objects.requireNonNull(extension);
        switch (extension) {
            case ZSTD_EXTENSION:
                return CloudCompressType.ZSTD;
            case GZIP_EXTENSION:
                return CloudCompressType.GZIP;
            case NONE_EXTENSION:
                return CloudCompressType.NONE;
            default:
                throw new CloudStorageLibCompressionException("Illegal Compression Extension provided extension:");
        }
    }

    public static CloudCompressType compressTypeFor(String extension, String contentType, String contentEncoding) throws CloudStorageLibCompressionException {
        if (isNullOrEmptyString(contentType) || isNullOrEmptyString(contentEncoding)) {
            return CloudCompressType.NONE;
        }

        for (CloudCompressType compressType : CloudCompressType.values()) {
            if (compressType.extension.equals(extension) && compressType.contentEncoding.equals(contentEncoding) && compressType.mimeType.equals(contentEncoding)) {
                return compressType;
            }
        }
        throw new CloudStorageLibCompressionException("Illegal CloudCompressType arguments");
    }


    private static boolean isNullOrEmptyString(String s) {
        return Objects.isNull(s) || s.isEmpty();
    }
}
