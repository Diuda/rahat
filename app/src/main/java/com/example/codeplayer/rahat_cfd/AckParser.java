package com.example.codeplayer.rahat_cfd;

import android.util.Log;

import com.google.android.gms.nearby.connection.Payload;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;

public class AckParser {



    private Long t1;
    private Long t2;
    private Long t3;
    private Long t4;



    private void getTimes(String t1,String t2,String t3){

        this.t1 = Long.parseLong(t1);
        this.t2 = Long.parseLong(t2);
        this.t3 = Long.parseLong(t3);
        this.t4 = ( System.currentTimeMillis());
    }
    void  parseAckMessage(Payload payload){

        String  payloadString = null;
        try {
            payloadString = new String(payload.asBytes(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String [] parsedPayload = payloadString.split("#");

        Log.i("PARSEACK", Arrays.toString(parsedPayload));
        getTimes(parsedPayload[1],parsedPayload[2],parsedPayload[3]);

    }

    public double findDistance(){

        return (((t4-t1)-(t3-t2))*299792458)/2;
    }
}
