package cloud.storage.lib.compression;

import com.github.luben.zstd.Zstd;
import com.github.luben.zstd.ZstdInputStream;
import com.github.luben.zstd.ZstdOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class ZstdCloudCompressor implements CloudCompressor {
    private int level = 5;

    @Override
    public CloudCompressType compressType() {
        return CloudCompressType.ZSTD;
    }

    @Override
    public byte[] compress(final byte[] src) {
        return Zstd.compress(src);
    }

    @Override
    public byte[] decompress(final byte[] src) {
        final int originalSize = (int) Zstd.decompressedSize(src);
        return Zstd.decompress(src, originalSize);
    }

    @Override
    public InputStream decompress(final InputStream in) {
        try {
            return new ZstdInputStream(in);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OutputStream compress(final OutputStream out) {
        try {
            return new ZstdOutputStream(out, level);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ByteBuffer compress(ByteBuffer src) {
        ByteBuffer dstBuffer = ByteBuffer.allocateDirect(src.capacity());
        Zstd.compress(dstBuffer, src, level);
        dstBuffer.flip();
        return dstBuffer;
    }

    @Override
    public ByteBuffer decompress(ByteBuffer src) {
        final int originalSize = (int) Zstd.decompressedSize(src);
        return Zstd.decompress(src, originalSize);
    }
}
