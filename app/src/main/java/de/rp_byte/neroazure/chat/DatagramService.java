package de.rp_byte.neroazure.chat;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by neroazure on 08-12-16.
 */

public class DatagramService {
    private final static String TAG = "RPB/DatagramService";
    private IReceiveListener rcv;


    private DatagramSocket dtgms;
    private DatagramSocket ddtgms;

    private boolean runs = false;

    private Activity caller;

    AsyncTask<Void,Void,Void> receiver = new AsyncTask<Void, Void, Void>(){
        protected Void doInBackground(Void... params) {
            try {
                ddtgms.connect(InetAddress.getByName("255.255.255.255"),8888);
            } catch (UnknownHostException e) {
                Log.e(TAG, "Receiver: ", e);
            }
            while(runs){
                try {
                    byte[] buffer = new byte[1024];
                    DatagramPacket dp = new DatagramPacket(buffer,buffer.length);
                    dtgms.receive(dp);
                    final byte[] data = dp.getData();
                    caller.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rcv.handleMessage(data);
                        }
                    });
                } catch (IOException e) {
                    Log.e(TAG, "Receiver: ", e);
                }
            }
            return null;
        }
    };

    public DatagramService(IReceiveListener ircv, Activity caller) throws SocketException {
        rcv = ircv;
        dtgms = new DatagramSocket(8888);
        dtgms.setBroadcast(true);
        ddtgms = new DatagramSocket();
        ddtgms.setBroadcast(true);
        this.caller = caller;
        Log.i(TAG, "DatagramService: Service created!");
    }

    public boolean send(final byte[] msg){
        if (runs){
            AsyncTaskCompat.executeParallel(new AsyncTask<Void,Void,Void>(){
                @Override
                protected Void doInBackground(Void... params) {
                    Log.i(TAG, "send: trying to send a Message...");
                    try {
                        ddtgms.send(new DatagramPacket(msg,msg.length));
                        Log.i(TAG, "send: A Message was sent.");
                    } catch (IOException e) {
                        Log.e(TAG, "send: an Exception occurred.", e);
                    }
                    return null;
                }
            });
        }
        return runs;
    }

    public void start(){
        runs = true;
        AsyncTaskCompat.executeParallel(receiver);
        Log.i(TAG, "start: Service Started!");
    }

    public void stop(){
        runs = false;
        dtgms.close();
        ddtgms.close();
        Log.i(TAG,"stop: Service stopped!");
    }
}
