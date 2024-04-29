package ch.uzh.ifi.hase.soprafs24.agoratoken;

/**
 * Created by Li on 10/1/2016.
 */
public interface Packable {
    ByteBuf marshal(ByteBuf out);
}