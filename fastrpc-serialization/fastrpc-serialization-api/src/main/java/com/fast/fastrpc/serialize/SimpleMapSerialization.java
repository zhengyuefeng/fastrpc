package com.fast.fastrpc.serialize;

import com.fast.fastrpc.common.buffer.IoBuffer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author yiji
 * @version : SimpleMapSerialization.java, v 0.1 2020-08-19
 */
public class SimpleMapSerialization {

    public Map<String, String> decodeAttachment(IoBuffer buffer, int length) throws IOException {
        Map<String, String> attachment = new HashMap<>();
        try {
            while (length > 0) {
                length -= 8;
                attachment.put(readString(buffer), readString(buffer));
            }
        } catch (Throwable e) {
            throw new IOException("Failed to decode attachment.", e);
        }
        return attachment;
    }

    public int encodeAttachment(IoBuffer buffer, Map<String, String> attachment) throws IOException {

        if (attachment == null || attachment.isEmpty()) {
            return 0;
        }

        int encodedBytes = 0;

        try {
            for (Iterator<Map.Entry<String, String>> iterator = attachment.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, String> item = iterator.next();
                if (item.getKey() == null || item.getValue() == null) {
                    continue;
                }

                encodedBytes += item.getKey().length();
                buffer.writeInt(item.getKey().length());
                buffer.writeCharSequence(item.getKey());

                encodedBytes += item.getValue().length();
                buffer.writeInt(item.getValue().length());
                buffer.writeCharSequence(item.getValue());

                encodedBytes += 8;
            }
        } catch (Throwable e) {
            throw new IOException("Failed to encode attachment.", e);
        }

        return encodedBytes;
    }


    private String readString(IoBuffer buffer) {
        int len = buffer.readInt();
        if (len > 0) {
            return buffer.readCharSequence(len).toString();
        }
        return "";
    }

}
