package ch.uzh.ifi.hase.soprafs24.agora;

import ch.uzh.ifi.hase.soprafs24.agoratoken.Utils;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void testHmacSign() throws InvalidKeyException, NoSuchAlgorithmException {
        String key = "testKey";
        byte[] message = "testMessage".getBytes(StandardCharsets.UTF_8);
        byte[] signature = Utils.hmacSign(key, message);

        assertNotNull(signature);
        assertEquals(Utils.HMAC_SHA256_LENGTH, signature.length);
    }

    @Test
    void testBase64EncodeDecode() {
        byte[] data = "testData".getBytes(StandardCharsets.UTF_8);
        String encoded = Utils.base64Encode(data);
        byte[] decoded = Utils.base64Decode(encoded);

        assertTrue(Arrays.equals(data, decoded));
    }

    @Test
    void testCrc32String() {
        String data = "testData";
        int crc = Utils.crc32(data);

        assertTrue(crc != 0);
    }

    @Test
    void testCrc32Bytes() {
        byte[] data = "testData".getBytes(StandardCharsets.UTF_8);
        int crc = Utils.crc32(data);

        assertTrue(crc != 0);
    }

    @Test
    void testGetTimestamp() {
        int timestamp = Utils.getTimestamp();
        int currentTimestamp = (int) (new Date().getTime() / 1000);

        assertTrue(Math.abs(currentTimestamp - timestamp) < 2);
    }

    @Test
    void testRandomInt() {
        int randomInt1 = Utils.randomInt();
        int randomInt2 = Utils.randomInt();

        assertNotEquals(randomInt1, randomInt2);
    }

    @Test
    void testIsUUID() {
        assertTrue(Utils.isUUID("12345678123456781234567812345678"));
        assertFalse(Utils.isUUID("1234567812345678123456781234567G"));
    }

    @Test
    void testCompressDecompress() {
        byte[] data = "testData".getBytes(StandardCharsets.UTF_8);
        byte[] compressed = Utils.compress(data);
        byte[] decompressed = Utils.decompress(compressed);

        assertTrue(Arrays.equals(data, decompressed));
    }

    @Test
    void testMd5() {
        String data = "testData";
        String md5Hash = Utils.md5(data);

        assertNotNull(md5Hash);
        assertEquals(32, md5Hash.length());
    }
}
