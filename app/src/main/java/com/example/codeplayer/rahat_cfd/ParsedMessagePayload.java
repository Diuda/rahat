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

        private int messageType;


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

        ParsedMessagePayload(){
            data=null;
            sendStamp=null;
            receiveStamp=null;
            messageType = 0;
        }
        void parseData(Payload payload){

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

                    if(this.messageType==4){


                        return;
                    }
                    if(this.messageType==3){

                        this.sendStamp = parsedPayload[1];
                        this.receiveStamp =  Long.toString( System.currentTimeMillis());

                    }

                    if(this.messageType==0) {
                        this.data = parsedPayload[1];
                    }
                    //ACK Message
                    if(this.messageType==1)
                        return;

                    if(this.messageType==2){

                        LocationParser.parseLocation(parsedPayload);

                    }


            }


        }

        abstract class LocationParser{

                private static String Longitude;
                private static String Latitude;

            public static String getLongitude() {
                return Longitude;
            }

            public static String getLatitude() {
                return Latitude;
            }

            static void parseLocation(String[] data){

                    Latitude = data[1];
                    Longitude = data[2];
                }
        }
