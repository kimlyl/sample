package com.example.kgy.mymenu;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.kgy.mymenu.bluethooth.*;
import com.example.kgy.mymenu.repair_request.RepairActivity;

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

import java.io.OutputStream;
import java.net.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;


public class HomeActivity extends AppCompatActivity {

    static final int ACTION_ENABLE_BT = 101;
    BluetoothService blueToothService = null;
    Socket socket;
    OutputStream outputStream;

    TextView speed;
    TextView rpm;
    TextView distance;
    TextView ifef;

    Trans module = Trans.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.obd_view_activity);

        speed = (TextView)findViewById(R.id.speed);
        rpm = (TextView)findViewById(R.id.rpm);
        distance = (TextView)findViewById(R.id.distance);
        ifef = (TextView)findViewById(R.id.ifef);

        Intent intent = getIntent();
        if(intent!=null){
            String title = intent.getStringExtra("title");
            Toast.makeText(getApplicationContext(),"전달 받은 값: " + title,Toast.LENGTH_LONG).show();
        }

        blueToothService = new BluetoothService(this);

        blueToothService.startService();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    public void onButtonClicked(View v){

        int result = 0;

        String rpm_value = rpm.getText().toString();
        String speed_value = speed.getText().toString();
        String distance_value = distance.getText().toString();
        String ifef_value = ifef.getText().toString();

//        Thread thread = new ConnectThread(rpm_value,speed_value,distance_value,ifef_value);
//        thread.start();

       result = module.sendOBDdata(rpm_value, speed_value, distance_value, ifef_value);

        //////////////////////HTTP WAS 연결/////////////////////////////

        //1001 요청코드가 중복되면 안됨
        //열었던 엑티비티에서 응답을 받기 위해선 startActivityForResult 를 써야함.
        //응답을 받을 필요가 없으면 startActivity 만 써도 됨.

        //엑티비티 끝냄
        //finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Home) {
            Toast.makeText(getApplicationContext(), "홈 화면 입니다.", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);

            intent.putExtra("title", "소녀시대");

            //1001 요청코드가 중복되면 안됨
            //열었던 엑티비티에서 응답을 받기 위해선 startActivityForResult 를 써야함.
            //응답을 받을 필요가 없으면 startActivity 만 써도 됨.
            startActivityForResult(intent, 1001);
            return true;
        }
        else if(id==R.id.action_join){
            Toast.makeText(getApplicationContext(), "회원가입 메뉴 입니다.", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getApplicationContext(),JoinActivity.class);

            intent.putExtra("title", "소녀시대");

            //1001 요청코드가 중복되면 안됨
            //열었던 엑티비티에서 응답을 받기 위해선 startActivityForResult 를 써야함.
            //응답을 받을 필요가 없으면 startActivity 만 써도 됨.
            startActivityForResult(intent, 1002);
            return true;
        }
        else if(id==R.id.action_login){
            Toast.makeText(getApplicationContext(), "로그인 메뉴 입니다.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);

            intent.putExtra("title", "소녀시대");

            //1001 요청코드가 중복되면 안됨
            //열었던 엑티비티에서 응답을 받기 위해선 startActivityForResult 를 써야함.
            //응답을 받을 필요가 없으면 startActivity 만 써도 됨.
            startActivityForResult(intent, 1003);
            return true;
        }

        else if(id==R.id.action_repair){
            Toast.makeText(getApplicationContext(), "수리요청 메뉴 입니다.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(),RepairActivity.class);

            intent.putExtra("title", "소녀시대");


            //1001 요청코드가 중복되면 안됨
            //열었던 엑티비티에서 응답을 받기 위해선 startActivityForResult 를 써야함.
            //응답을 받을 필요가 없으면 startActivity 만 써도 됨.
            startActivityForResult(intent, 1004);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == ACTION_ENABLE_BT ) {
            // 사용자가 블루투스 활성화 승인했을때
            if( resultCode == RESULT_OK ) {
                // 페어링된 원격 디바이스 목록 구하기
                blueToothService.getParedDevice();
            }
            // 사용자가 블루투스 활성화 취소했을때
            else {
            }
        }
    }

    class ConnectThread extends Thread{

        String rpm;
        String speed;
        String distance;
        String ifef;

        public ConnectThread(String rpm, String speed, String distance, String ifef){
            this.rpm=rpm;
            this.speed=speed;
            this.distance=distance;
            this.ifef=ifef;
        }

        public void run(){

            String host = "192.168.1.155";
            int port = 10042;

            try{

                byte[] pack = null;
                int type = 3;
                int headLength=8;
                int dataLength=5;
                String OBDdata = rpm+"/"+ speed+"/"+distance+"/"+ifef;
                StringTokenizer data = new StringTokenizer(OBDdata,"/");

                socket = new java.net.Socket(host, port);
                System.out.println("서버로 연결");

                outputStream = socket.getOutputStream();

                pack = CreatePacket(type,headLength, data, dataLength);
                outputStream.write(pack);

                System.out.println("데이터를 서버로 보냄");

                Toast.makeText(getApplicationContext(), "서버로 데이터를 보냈습니다.", Toast.LENGTH_LONG).show();

                outputStream.close();
                socket.close();

                Log.d("mainActivity", "connect");

            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        public byte[] CreatePacket(int type,int headLength, StringTokenizer data, int dataLength){

            byte[] head_factory;
            ByteBuffer buffer = ByteBuffer.allocate(512);
            byte[] change;
            int length=0;
            int i=0;

            head_factory = intToByteArray(type);
            buffer.put(head_factory);
            buffer.mark();
            buffer.position(headLength);

            while(data.hasMoreTokens()) {
                String value=null;
                value=data.nextToken().toString();
                change = value.getBytes();
                Log.d("mainActivity", value);
                buffer.put(change);
                i++;
                length=headLength+(i*dataLength);
                buffer.position(length);
            }

            byte[] pack = new byte[length];
            buffer.reset();
            head_factory=intToByteArray(length);
            buffer.put(head_factory,0,head_factory.length);

            int size = buffer.remaining();
            if(pack.length<size){
                size=pack.length;
            }
            buffer.clear();

            buffer.get(pack,0,size);

            return pack;

        }

        public  byte[] intToByteArray(int value) {
            byte[] byteArray = new byte[4];
            byteArray[0] = (byte)(value >> 24);
            byteArray[1] = (byte)(value >> 16);
            byteArray[2] = (byte)(value >> 8);
            byteArray[3] = (byte)(value);
            return byteArray;
        }

        public int ByteToInt(byte[] src) {
            int s1 = src[0] & 0xFF;
            int s2 = src[1] & 0xFF;
            int s3 = src[2] & 0xFF;
            int s4 = src[3] & 0xFF;

            return ((s1 << 24) + (s2 << 16) + (s3 << 8) + (s4 << 0));
        }
    }
}
