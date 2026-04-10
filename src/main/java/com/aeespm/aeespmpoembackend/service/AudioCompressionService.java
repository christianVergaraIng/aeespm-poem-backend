package com.aeespm.aeespmpoembackend.service;

import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Service
public class AudioCompressionService {

    public byte[] compress(byte[] data) {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream)) {
            gzipStream.write(data);
            gzipStream.finish();
            return byteStream.toByteArray();
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo comprimir el audio", exception);
        }
    }

    public byte[] decompress(byte[] data) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
             GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[8192];
            int read;
            while ((read = gzipInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            return outputStream.toByteArray();
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo descomprimir el audio", exception);
        }
    }
}
