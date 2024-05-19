package ch.uzh.ifi.hase.soprafs24.agoratoken;

import ch.uzh.ifi.hase.soprafs24.constant.Role;
import io.agora.media.*;
import org.springframework.stereotype.Service;

@Service
public class RtcTokenBuilder {

    private static String appId = "bb103a1b0b3c477ea5bae0cc1c32525f";
    private static String appCertificate = "e40a7e58667048c4b1b8af90096fc124";
    private static int expirationTimeInSeconds = 7200;
    public String buildTokenWithUserAccount(String channelName, String account, Role role) {
        if (channelName == null) {
            throw new NullPointerException("channelName cannot be null");
        }
        if (account == null) {
            throw new NullPointerException("Account cannot be null");
        }
        int privilegeTs = (int)(System.currentTimeMillis() / 1000 + expirationTimeInSeconds);
        AccessToken builder = new AccessToken(appId, appCertificate, channelName, account);
        builder.addPrivilege(AccessToken.Privileges.kJoinChannel, privilegeTs);
        if (role == Role.Role_Publisher || role == Role.Role_Subscriber || role == Role.Role_Admin) {
            builder.addPrivilege(AccessToken.Privileges.kPublishAudioStream, privilegeTs);
            builder.addPrivilege(AccessToken.Privileges.kPublishVideoStream, privilegeTs);
            builder.addPrivilege(AccessToken.Privileges.kPublishDataStream, privilegeTs);
        }

        try {
            return builder.build();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public AccessToken createAccessToken(String appId, String appCertificate, String channelName, String account) {
        return new AccessToken(appId, appCertificate, channelName, account);
    }
}