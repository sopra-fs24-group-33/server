package ch.uzh.ifi.hase.soprafs24.constant;

public enum Role {
    /**
     * DEPRECATED. Role_Attendee has the same privileges as Role_Publisher.
     */
    Role_Attendee(0),
    /**
     *    RECOMMENDED. Use this role for a voice/video call or a live broadcast, if your scenario does not require authentication for [Hosting-in](https://docs.agora.io/en/Agora%20Platform/terms?platform=All%20Platforms#hosting-in).
     */
    Role_Publisher(1),
    /**
     * Only use this role if your scenario require authentication for [Hosting-in](https://docs.agora.io/en/Agora%20Platform/terms?platform=All%20Platforms#hosting-in).
     *
     * @note In order for this role to take effect, please contact our support team to enable authentication for Hosting-in for you. Otherwise, Role_Subscriber still has the same privileges as Role_Publisher.
     */
    Role_Subscriber(2),
    /**
     * DEPRECATED. Role_Attendee has the same privileges as Role_Publisher.
     */
    Role_Admin(101);

    public int initValue;

    Role(int initValue) {
        this.initValue = initValue;
    }
}
