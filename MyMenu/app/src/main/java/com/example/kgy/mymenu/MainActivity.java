package com.example.kgy.mymenu;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    //public static Trans trans= new Trans();
    Trans module = Trans.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText editText1 = (EditText)findViewById(R.id.id);
        final EditText editText2 = (EditText)findViewById(R.id.password);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button button = (Button)findViewById(R.id.login_button);

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //버튼을 눌렀을때 어떤 작업을 할지 선언

                    String id = editText1.getText().toString();
                    String password = editText2.getText().toString();

//                    ConnectThread thread = new ConnectThread(id, password);
//                    thread.start();

//                    TCP/IP 소켓 서버 연결...

                    //////////////////////HTTP WAS 연결/////////////////////////////

                    String url = "http://192.168.0.13:8181/Tomcat_Http_Test/LoginServlet";
                    HttpClient http = new DefaultHttpClient();
                    try {

                        ArrayList<NameValuePair> nameValuePairs =
                                new ArrayList<NameValuePair>();
                        nameValuePairs.add(new BasicNameValuePair("userid",id));
                        nameValuePairs.add(new BasicNameValuePair("pwd",password));
                        module.userid=id;
                        module.password=password;
                        HttpParams params = http.getParams();
                        HttpConnectionParams.setConnectionTimeout(params, 5000);
                        HttpConnectionParams.setSoTimeout(params, 5000);

                        HttpPost httpPost = new HttpPost(url);
                        UrlEncodedFormEntity entityRequest =
                                new UrlEncodedFormEntity(nameValuePairs, "EUC-KR");

                        httpPost.setEntity(entityRequest);

                        HttpResponse responsePost = http.execute(httpPost);
                        HttpEntity resEntity = responsePost.getEntity();

//                        if(EntityUtils.toString(resEntity).equals("login Success")){
//                            module.userid=id;
//                            module.password=password;
//                            Toast.makeText(MainActivity.this, EntityUtils.toString(resEntity) , Toast.LENGTH_SHORT).show();
//                        }
//                        else
//                            Toast.makeText(MainActivity.this, EntityUtils.toString(resEntity) , Toast.LENGTH_SHORT).show();



                        // tv_post.setText( EntityUtils.toString(resEntity));
                    }catch(Exception e){e.printStackTrace();}

                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

                    intent.putExtra("title", "땡땡땡");

                    //1001 요청코드가 중복되면 안됨
                    //열었던 엑티비티에서 응답을 받기 위해선 startActivityForResult 를 써야함.
                    //응답을 받을 필요가 없으면 startActivity 만 써도 됨.
                    startActivityForResult(intent, 1003);

                    Toast.makeText(MainActivity.this, "로그인을 요청합니다.", Toast.LENGTH_SHORT).show();
                }
            });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(data!=null){
            String name = data.getStringExtra("name");
            Toast.makeText(getApplicationContext(),"전달 받은 값: " + name,Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
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
    class ConnectThread extends Thread{
/*
        String id;
        String password;

        public ConnectThread(String id, String password){

            this.id=id;
            this.password=password;
        }
*/
        EditText editText1;
        EditText editText2;

        Socket socket;
        OutputStream outputStream;

        public ConnectThread(EditText editText1, EditText editText2){
            this.editText1=editText1;
            this.editText2=editText2;
        }

        public void run(){

            String host = "192.168.1.155";
            int port = 10042;

            try{

                String id = editText1.getText().toString();
                String password = editText2.getText().toString();

                socket = new Socket(host, port);
                System.out.println("서버로 연결");

                outputStream = socket.getOutputStream();

                byte[] pack = null;
                int type = 0;
                int headLength=8;
                int dataLength=20;
                String logindata = id+"/"+ password;
                StringTokenizer data = new StringTokenizer(logindata,"/");

                Log.d("mainActivity", "connect1");

                if(id !=null && password !=null) {

                    Log.d("mainActivity", "connect2");

                    pack = CreatePacket(type,headLength, data, dataLength);

                    Log.d("mainActivity", "connect3");

                    outputStream.write(pack);

                    System.out.println("데이터를 서버로 보냄");
                }
                else{
                    Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 모두 입력 해주세요.", Toast.LENGTH_LONG).show();
                }
                Log.d("mainActivity", "connect4");

            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            finally {
                try{
                    outputStream.close();
                    socket.close();

                }catch (Exception e){

                }
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
