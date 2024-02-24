package cloud.storage.lib.compression;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public interface CloudCompressor {

    CloudCompressType compressType();

    default String name() {
        return compressType().name();
    }

    default boolean performsCompression() {
        return true;
    }

    byte[] compress(byte[] src);
    byte[] decompress(byte[] src);
    InputStream decompress (InputStream in);
    OutputStream compress (OutputStream out);
    ByteBuffer compress (ByteBuffer src);
    ByteBuffer decompress (ByteBuffer src);
}
