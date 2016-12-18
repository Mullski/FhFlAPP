package de.rp_byte.neroazure.chat;

/**
 * einfache Repräsentation einer Nachricht
 * Created by neroazure
 */

public class Message {
    public String sender = "";
    public String message = "";

    public Message(String s, String m){
        sender = s;
        message = m;
    }
}
