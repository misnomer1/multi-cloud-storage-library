package cloud.storage.lib.compression;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class NoneCloudCompressor implements CloudCompressor {
    @Override
    public CloudCompressType compressType() {
        return null;
    }

    @Override
    public byte[] compress(byte[] src) {
        return new byte[0];
    }

    @Override
    public byte[] decompress(byte[] src) {
        return new byte[0];
    }

    @Override
    public InputStream decompress(InputStream in) {
        return null;
    }

    @Override
    public OutputStream compress(OutputStream out) {
        return null;
    }

    @Override
    public ByteBuffer compress(ByteBuffer src) {
        return null;
    }

    @Override
    public ByteBuffer decompress(ByteBuffer src) {
        return null;
    }
}
