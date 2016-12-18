package de.rp_byte.neroazure.chat;

/**
 * Created by neroazure on 08-12-16.
 */
public interface IReceiveListener {
    /**
     * @param message Is the received data, the first 24 byte are the name of the sending user
     *                the rest is the written message, both are basically strings
     */
   void handleMessage(byte[] message);
}
