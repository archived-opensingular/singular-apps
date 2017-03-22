package org.opensingular.server.commons.wicket.view.util.huffman;

import org.opensingular.server.commons.exception.SingularServerException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HuffmanUtil {

    public static byte[] compress(byte[] data) {
        ByteArrayOutputStream baos = null;
        BitOutputStream       out  = null;
        ByteArrayInputStream  bais = null;
        try {
            baos = new ByteArrayOutputStream(data.length);
            out = new BitOutputStream(baos);
            bais = new ByteArrayInputStream(data);
            AdaptiveHuffmanCompress.compress(bais, out);
            out.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw SingularServerException.rethrow(e.getMessage(), e);
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (bais != null) {
                    bais.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                throw SingularServerException.rethrow(e.getMessage(), e);
            }
        }
    }

    public static byte[] decompress(byte[] data) {
        ByteArrayOutputStream baos = null;
        BitInputStream        in   = null;
        ByteArrayInputStream  bais = null;
        try {
            bais = new ByteArrayInputStream(data);
            in = new BitInputStream(bais);
            baos = new ByteArrayOutputStream(data.length);
            AdaptiveHuffmanDecompress.decompress(in, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw SingularServerException.rethrow(e.getMessage(), e);
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (bais != null) {
                    bais.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                throw SingularServerException.rethrow(e.getMessage(), e);
            }
        }
    }
}
