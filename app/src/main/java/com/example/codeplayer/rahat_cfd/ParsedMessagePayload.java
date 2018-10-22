package com.example.codeplayer.rahat_cfd;

import com.google.android.gms.nearby.connection.Payload;

public class ParsedMessagePayload {



        private String data;
        private  String sendStamp;
        private String receiveStamp;
        private boolean isAck;


        public String getData(){
            return this.data;
        }

        public String getSendStamp(){
            return this.sendStamp;
        }

        public String getReceiveStamp(){
            return this.receiveStamp;
        }
        public boolean isAck(){
            return this.isAck;
        }

        ParsedMessagePayload(){
            data=null;
            sendStamp=null;
            receiveStamp=null;
            isAck = false;
        }
        void parseData(Payload payload){

            String  payloadString = payload.asBytes().toString();
            String [] parsedPayload = payloadString.split("#");
            for(int i=0;i<parsedPayload.length;i++){

                if(i==0){

                    this.isAck = Boolean.parseBoolean(parsedPayload[0]);
                    if(this.isAck==true)
                        return;
                }
                if(i==1){
                    this.data = parsedPayload[1];
                }
                if(i==2){
                    this.sendStamp = parsedPayload[2];
                }
                if(i==3){
                    this.receiveStamp = parsedPayload[3];
                }
            }
        }



}