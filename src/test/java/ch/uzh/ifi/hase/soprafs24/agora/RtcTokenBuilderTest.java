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
}
