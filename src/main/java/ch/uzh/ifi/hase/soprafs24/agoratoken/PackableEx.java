package ch.uzh.ifi.hase.soprafs24.agoratoken;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}