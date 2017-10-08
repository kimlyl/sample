package com.example.kgy.mymenu;

import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * Created by kgy on 2016-02-20.
 */
public class Trans {
    private static Trans trans = null;


    Socket socket;
    String ip;
    int port;
    String userid;
    String password;


    public static Trans getInstance() {
        if (trans == null) {
            trans = new Trans();
        }

        return trans;
    }

    private Trans() {

    }

    Queue queue;

    void init(String ip, int port)
    {
        //db connection 등을 생성
        this.ip= ip;
        this.port=port;
    }

    void connect()
    {
        try{
            socket = new Socket(ip,port);
        }catch(Exception e){

        }
    }

//    boolean login() {
//
//        int count = 0;
//        Date date = new Date();
//
//        OutputStream out = null;
//
//        try {
//            out = socket.getOutputStream();
//
//            while (count < 5)//트랜잭션 id queue 를 확인.
//            {
//                //트랜잭션 id queue 를 확인
//                if (!queue.isEmpty()) {
//                    if (queue.element().equals(date)) {
//                        return true;
//                    }
//                } else {
//                    Thread.sleep(1000);
//                    count++;
//                }
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        finally {
//            try {
//                out.close();
//
//            } catch (Exception e) {
//
//                System.out.println(e.getMessage());
//            }
//        }
//        return false;
//    }

    class ReadThread extends Thread{

        ByteBuffer buffer = ByteBuffer.allocate(512);
        byte[] pack = new byte[256];
        int readcount;


        public void run(){

            try{
               InputStream in = socket.getInputStream();

                while(true){

                }
            }
            catch (Exception e){

            }

        }
    }


    Trans(Socket socket, String ip, int port){
        this.socket=socket;
        this.ip=ip;
        this.port=port;
    }


    public void Init(){
        try {
            socket = new Socket(ip, port);
            System.out.println("서버로 연결");
        }catch(Exception e){

        }
    }

    public int login(String id, String password){
        int result = 0;

        this.userid=id;
        this.password=password;

        return result;
    }

    public int sendOBDdata(String rpm,String speed, String distance, String ifef){
        int result = 0;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Date currentTime = new Date();
        String transactionID = formatter.format(currentTime);

        String url = "http://192.168.0.13:8181/Tomcat_Http_Test/ObdServlet";
        HttpClient http = new DefaultHttpClient();

        try {

            ArrayList<NameValuePair> nameValuePairs =
                    new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("transactionID",transactionID));
            nameValuePairs.add(new BasicNameValuePair("userid",userid));
            nameValuePairs.add(new BasicNameValuePair("rpm",rpm));
            nameValuePairs.add(new BasicNameValuePair("speed",speed));
            nameValuePairs.add(new BasicNameValuePair("distance", distance));
            nameValuePairs.add(new BasicNameValuePair("ifef", ifef));

            HttpPost httpPost = new HttpPost(url);
            UrlEncodedFormEntity entityRequest =
                    new UrlEncodedFormEntity(nameValuePairs, "EUC-KR");

            httpPost.setEntity(entityRequest);

            HttpResponse responsePost = http.execute(httpPost);
            HttpEntity resEntity = responsePost.getEntity();

//            Toast.makeText(HomeActivit, EntityUtils.toString(resEntity), Toast.LENGTH_SHORT).show();
            // tv_post.setText( EntityUtils.toString(resEntity));

        }catch(Exception e){e.printStackTrace();}

        return result;
    }

    public int sendRequest(String insurance,String pickup, String rent, String fuelError, String request){
        int result = 0;
        String num="1";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Date currentTime = new Date();
        String transactionID = formatter.format(currentTime);

        String url = "http://192.168.0.13:8181/Tomcat_Http_Test/RepairRequestServlet";
        HttpClient http = new DefaultHttpClient();

        try {

            ArrayList<NameValuePair> nameValuePairs =
                    new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("transactionID",transactionID));
            nameValuePairs.add(new BasicNameValuePair("userid",userid));
            nameValuePairs.add(new BasicNameValuePair("insurance",insurance));
            nameValuePairs.add(new BasicNameValuePair("pickup",pickup));
            nameValuePairs.add(new BasicNameValuePair("rent", rent));
            nameValuePairs.add(new BasicNameValuePair("fuelError", fuelError));
            nameValuePairs.add(new BasicNameValuePair("request", request));
            nameValuePairs.add(new BasicNameValuePair("num",num ));

            HttpPost httpPost = new HttpPost(url);
            UrlEncodedFormEntity entityRequest =
                    new UrlEncodedFormEntity(nameValuePairs, "EUC-KR");

            httpPost.setEntity(entityRequest);

            HttpResponse responsePost = http.execute(httpPost);
            HttpEntity resEntity = responsePost.getEntity();

//            Toast.makeText(HomeActivit, EntityUtils.toString(resEntity), Toast.LENGTH_SHORT).show();
            // tv_post.setText( EntityUtils.toString(resEntity));

        }catch(Exception e){e.printStackTrace();}

        return result;
    }

}
