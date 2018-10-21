package com.example.codeplayer.rahat_cfd;

import com.google.android.gms.nearby.connection.Payload;

public class AckParser {



    private Long t1;
    private Long t2;
    private Long t3;
    private Long t4;



    private void getTimes(String t1,String t2,String t3,String t4){

        this.t1 = Long.parseLong(t1);
        this.t2 = Long.parseLong(t2);
        this.t3 = Long.parseLong(t3);
        this.t4 = Long.parseLong(t4);

    }
    void  parseAckMessage(Payload payload){

        String  payloadString = payload.asBytes().toString();
        String [] parsedPayload = payloadString.split("#");
        getTimes(parsedPayload[1],parsedPayload[2],parsedPayload[3],parsedPayload[4]);

    }

    public double findDistance(){

        return (((t4-t1)-(t3-t2))*299792458)/2;
    }
}
