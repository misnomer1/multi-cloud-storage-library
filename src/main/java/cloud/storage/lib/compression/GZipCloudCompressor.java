package cloud.storage.lib.compression;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZipCloudCompressor implements CloudCompressor {
    private int bufferSize = 512;

    @Override
    public CloudCompressType compressType() {
        return CloudCompressType.GZIP;
    }

    @Override
    public byte[] compress(byte[] src) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            GZIPOutputStream gzipOutputStream = (GZIPOutputStream) compress(outputStream);
            gzipOutputStream.write(src);
            gzipOutputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] decompress(byte[] src) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(src);
             GZIPInputStream gis = (GZIPInputStream) decompress(byteArrayInputStream);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {
            byte[] buffer = new byte[bufferSize];
            int len;
            while ((len = gis.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream decompress(InputStream in) {
        try {
            return new GZIPInputStream(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OutputStream compress(OutputStream out) {
        try {
            return new GZIPOutputStream(out, bufferSize, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ByteBuffer compress(ByteBuffer src) {
        byte[] srcBytes = new byte[src.remaining()];
        src.get(srcBytes, 0, srcBytes.length);
        return ByteBuffer.wrap(compress(srcBytes));
    }

    @Override
    public ByteBuffer decompress(ByteBuffer src) {
        byte[] srcBytes = new byte[src.remaining()];
        src.get(srcBytes, 0, srcBytes.length);
        return ByteBuffer.wrap(decompress(srcBytes));
    }
}
