package ch.uzh.ifi.hase.soprafs24.agoratoken;

import ch.uzh.ifi.hase.soprafs24.constant.Role;

public class RtcTokenBuilder {

    private static String appId = "6784c587dc6d4e5594afbbe295d65245";
    private static String appCertificate = "d016d99bc4d8434aa063a0efe54412f1";
    private static int expirationTimeInSeconds = 7200;
    public String buildTokenWithUserAccount(String channelName, String account, Role role) {

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
}