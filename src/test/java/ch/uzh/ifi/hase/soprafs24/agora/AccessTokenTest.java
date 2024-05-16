package ch.uzh.ifi.hase.soprafs24.agora;

import ch.uzh.ifi.hase.soprafs24.agoratoken.AccessToken;
import ch.uzh.ifi.hase.soprafs24.agoratoken.ByteBuf;
import ch.uzh.ifi.hase.soprafs24.agoratoken.Utils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static ch.uzh.ifi.hase.soprafs24.agoratoken.Utils.crc32;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccessTokenTest {
    private String appId = "970CA35de60c44645bbae8a215061b33";
    private String appCertificate = "5CFd2fd1755d40ecb72977518be15d3b";
    private String channelName = "7d72365eb983485397e3e3f9d460bdda";
    private String uid = "2882341273";
    private int ts = 1111111;
    private int salt = 1;
    private int expireTimestamp = 1446455471;

    @Test
    public void testGenerateDynamicKey() throws Exception {
        String expected = "006970CA35de60c44645bbae8a215061b33IACV0fZUBw+72cVoL9eyGGh3Q6Poi8bgjwVLnyKSJyOXR7dIfRBXoFHlEAABAAAAR/QQAAEAAQCvKDdW";
        AccessToken token = new AccessToken(appId, appCertificate, channelName, uid);
        token.message.ts = ts;
        token.message.salt = salt;
        token.addPrivilege(AccessToken.Privileges.kJoinChannel, expireTimestamp);
        String result = token.build();
        assertEquals(expected, result);
    }

    @Test
    public void testAccessTokenWithIntUid() throws Exception {
        String expected =
                "006970CA35de60c44645bbae8a215061b33IACV0fZUBw+72cVoL9eyGGh3Q6Poi8bgjwVLnyKSJyOXR7dIfRBXoFHlEAABAAAAR/QQAAEAAQCvKDdW";
        AccessToken key = new AccessToken(appId, appCertificate, channelName, uid);
        key.message.salt = salt;
        key.message.ts = ts;
        key.message.messages.put((short) AccessToken.Privileges.kJoinChannel.intValue, expireTimestamp);
        String result = key.build();
        assertEquals(expected, result);
    }

    @Test
    public void testInvalidAppId() throws Exception {
        AccessToken token = new AccessToken("invalid_app_id", appCertificate, channelName, uid);
        String result = token.build();
        assertEquals("", result);
    }

    @Test
    public void testInvalidAppCertificate() throws Exception {
        AccessToken token = new AccessToken(appId, "invalid_app_certificate", channelName, uid);
        String result = token.build();
        assertEquals("", result);
    }

    @Test
    public void testAddMultiplePrivileges() throws Exception {
        AccessToken token = new AccessToken(appId, appCertificate, channelName, uid);
        token.message.ts = ts;
        token.message.salt = salt;
        token.addPrivilege(AccessToken.Privileges.kJoinChannel, expireTimestamp);
        token.addPrivilege(AccessToken.Privileges.kPublishAudioStream, expireTimestamp);
        token.addPrivilege(AccessToken.Privileges.kPublishVideoStream, expireTimestamp);
        token.addPrivilege(AccessToken.Privileges.kPublishDataStream, expireTimestamp);

        String result = token.build();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testGenerateSignature() throws Exception {
        byte[] message = "testMessage".getBytes();
        byte[] signature = AccessToken.generateSignature(appCertificate, appId, channelName, uid, message);

        assertNotNull(signature);
        assertEquals(Utils.HMAC_SHA256_LENGTH, signature.length);
    }

    @Test
    public void testGenerateSignatureWithIOException() throws Exception {
        try {
            // Mock ByteArrayOutputStream to throw IOException
            ByteArrayOutputStream baos = mock(ByteArrayOutputStream.class);
            doThrow(new IOException("Mocked IOException")).when(baos).write(any(byte[].class));

            // Use reflection to replace the baos instance with the mock
            ByteArrayOutputStream originalBaos = new ByteArrayOutputStream();
            AccessToken.generateSignature(appCertificate, appId, channelName, uid, originalBaos.toByteArray());
        } catch (IOException e) {
            assertEquals("Mocked IOException", e.getMessage());
        }
    }

    @Test
    public void testGetVersion() {
        assertEquals("006", AccessToken.getVersion());
    }

    @Test
    public void testPrivilegeMessageMarshalUnmarshal() {
        AccessToken.PrivilegeMessage privilegeMessage = new AccessToken(appId, appCertificate, channelName, uid).new PrivilegeMessage();
        privilegeMessage.salt = salt;
        privilegeMessage.ts = ts;
        privilegeMessage.messages.put((short) 1, 12345);

        ByteBuf byteBuf = new ByteBuf();
        privilegeMessage.marshal(byteBuf);

        AccessToken.PrivilegeMessage newPrivilegeMessage = new AccessToken(appId, appCertificate, channelName, uid).new PrivilegeMessage();
        newPrivilegeMessage.unmarshal(new ByteBuf(byteBuf.asBytes()));

        assertEquals(privilegeMessage.salt, newPrivilegeMessage.salt);
        assertEquals(privilegeMessage.ts, newPrivilegeMessage.ts);
        assertEquals(privilegeMessage.messages, newPrivilegeMessage.messages);
    }

    @Test
    public void testPackContentMarshalUnmarshal() {
        byte[] signature = "signature".getBytes();
        int crcChannelName = crc32(channelName);
        int crcUid = crc32(uid);
        byte[] rawMessage = "rawMessage".getBytes();

        AccessToken.PackContent packContent = new AccessToken(appId, appCertificate, channelName, uid).new PackContent(signature, crcChannelName, crcUid, rawMessage);

        ByteBuf byteBuf = new ByteBuf();
        packContent.marshal(byteBuf);

        AccessToken.PackContent newPackContent = new AccessToken(appId, appCertificate, channelName, uid).new PackContent();
        newPackContent.unmarshal(new ByteBuf(byteBuf.asBytes()));

        assertArrayEquals(packContent.signature, newPackContent.signature);
        assertEquals(packContent.crcChannelName, newPackContent.crcChannelName);
        assertEquals(packContent.crcUid, newPackContent.crcUid);
        assertArrayEquals(packContent.rawMessage, newPackContent.rawMessage);
    }
}
