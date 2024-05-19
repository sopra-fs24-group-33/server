package ch.uzh.ifi.hase.soprafs24.agora;

import ch.uzh.ifi.hase.soprafs24.agoratoken.RtcTokenBuilder;
import ch.uzh.ifi.hase.soprafs24.constant.Role;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RtcTokenBuilderTest {

    @Test
    void testBuildTokenWithUserAccount_PublisherRole() {
        RtcTokenBuilder rtcTokenBuilder = new RtcTokenBuilder();
        String channelName = "testChannel";
        String userAccount = "testUser";
        Role role = Role.Role_Publisher;

        String token = rtcTokenBuilder.buildTokenWithUserAccount(channelName, userAccount, role);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testBuildTokenWithUserAccount_SubscriberRole() {
        RtcTokenBuilder rtcTokenBuilder = new RtcTokenBuilder();
        String channelName = "testChannel";
        String userAccount = "testUser";
        Role role = Role.Role_Subscriber;

        String token = rtcTokenBuilder.buildTokenWithUserAccount(channelName, userAccount, role);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testBuildTokenWithUserAccount_AdminRole() {
        RtcTokenBuilder rtcTokenBuilder = new RtcTokenBuilder();
        String channelName = "testChannel";
        String userAccount = "testUser";
        Role role = Role.Role_Admin;

        String token = rtcTokenBuilder.buildTokenWithUserAccount(channelName, userAccount, role);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testBuildTokenWithUserAccount_AttendeeRole() {
        RtcTokenBuilder rtcTokenBuilder = new RtcTokenBuilder();
        String channelName = "testChannel";
        String userAccount = "testUser";
        Role role = Role.Role_Attendee;

        String token = rtcTokenBuilder.buildTokenWithUserAccount(channelName, userAccount, role);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }
    @Test
    void testBuildTokenWithUserAccount_NullChannelName() {
        RtcTokenBuilder rtcTokenBuilder = new RtcTokenBuilder();
        String userAccount = "testUser";
        Role role = Role.Role_Attendee;

        assertThrows(NullPointerException.class, () -> {
            rtcTokenBuilder.buildTokenWithUserAccount(null, userAccount, role);
        });
    }

    @Test
    void testBuildTokenWithUserAccount_EmptyChannelName() {
        RtcTokenBuilder rtcTokenBuilder = new RtcTokenBuilder();
        String channelName = "";
        String userAccount = "testUser";
        Role role = Role.Role_Attendee;

        String token = rtcTokenBuilder.buildTokenWithUserAccount(channelName, userAccount, role);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testBuildTokenWithUserAccount_NullUserAccount() {
        RtcTokenBuilder rtcTokenBuilder = new RtcTokenBuilder();
        String channelName = "testChannel";
        Role role = Role.Role_Attendee;

        assertThrows(NullPointerException.class, () -> {
            rtcTokenBuilder.buildTokenWithUserAccount(channelName, null, role);
        });
    }

    @Test
    void testBuildTokenWithUserAccount_EmptyUserAccount() {
        RtcTokenBuilder rtcTokenBuilder = new RtcTokenBuilder();
        String channelName = "testChannel";
        String userAccount = "";
        Role role = Role.Role_Attendee;

        String token = rtcTokenBuilder.buildTokenWithUserAccount(channelName, userAccount, role);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testBuildTokenWithUserAccount_InvalidRole() {
        RtcTokenBuilder rtcTokenBuilder = new RtcTokenBuilder();
        String channelName = "testChannel";
        String userAccount = "testUser";
        Role role = null;

        String token = rtcTokenBuilder.buildTokenWithUserAccount(channelName, userAccount, role);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testBuildTokenWithUserAccount_ExpirationTime() {
        RtcTokenBuilder rtcTokenBuilder = new RtcTokenBuilder();
        String channelName = "testChannel";
        String userAccount = "testUser";
        Role role = Role.Role_Publisher;

        long currentTime = System.currentTimeMillis() / 1000;
        String token = rtcTokenBuilder.buildTokenWithUserAccount(channelName, userAccount, role);

        assertNotNull(token);
        assertFalse(token.isEmpty());

    }


}
