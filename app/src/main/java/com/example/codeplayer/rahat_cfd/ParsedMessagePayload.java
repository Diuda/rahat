package com.example.codeplayer.rahat_cfd;

import android.util.Log;

import com.google.android.gms.nearby.connection.Payload;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;

public class ParsedMessagePayload {




        private String data;
        private  String sendStamp;
        private String receiveStamp;
        private String messageUUID;
        private int messageType;
        private String by;

    public String getMessageUUID() {
        return messageUUID;
    }

    public String getData(){
            return this.data;
        }

        public String getSendStamp(){
            return this.sendStamp;
        }

        public String getReceiveStamp(){
            return this.receiveStamp;
        }

        public int getMessageType(){
            return this.messageType;
        }
        public String getBy(){return this.by;};

        ParsedMessagePayload(){
            data=null;
            sendStamp=null;
            receiveStamp=null;
            messageType = 0;
        }
        void parseData(Payload payload,LocationParser locationParser){

            String  payloadString = null;
            try {
                payloadString = new String(payload.asBytes(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String [] parsedPayload = payloadString.split("#");
            Log.i("PayloadReceived",payloadString);
            Log.i("PayloadReceived", Arrays.toString(parsedPayload));




                    this.messageType = Integer.parseInt(parsedPayload[0]);

                    if(this.messageType==5){
                        return;
                    }
                    if(this.messageType==6){

                        this.data = payloadString.split("6#")[1];
                    }
                    if(this.messageType==4){


                        return;
                    }
                    if(this.messageType==3){

                        this.sendStamp = parsedPayload[1];
                        this.receiveStamp =  Long.toString( Math.abs(System.nanoTime()));

                    }

                    if(this.messageType==0) {
                        this.by = parsedPayload[3];
                        this.data = parsedPayload[2];
                        this.messageUUID=parsedPayload[1];

                    }
                    //ACK Message
                    if(this.messageType==1)
                        return;

                    if(this.messageType==2){

                        locationParser.parseLocation(parsedPayload);

                    }


            }


        }

         class LocationParser{

                private  String Longitude;
                private  String Latitude;

            public  String getLongitude() {
                return Longitude;
            }

            public  String getLatitude() {
                return Latitude;
            }

             void parseLocation(String[] data){

                    Latitude = data[2];
                    Longitude = data[3];
                }
        }
