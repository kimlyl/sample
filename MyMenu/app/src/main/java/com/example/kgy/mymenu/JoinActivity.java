package com.example.kgy.mymenu;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class JoinActivity extends AppCompatActivity {


//    Trans trans = Trans.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_activity);

        Intent intent = getIntent();
        if(intent!=null){
            String title = intent.getStringExtra("title");
            Toast.makeText(getApplicationContext(),"전달 받은 값: " + title,Toast.LENGTH_LONG).show();
        }

        final EditText editText = (EditText)findViewById(R.id.join_name);
        final EditText editText1 = (EditText)findViewById(R.id.join_id);
        final EditText editText2 = (EditText)findViewById(R.id.join_password);
        final EditText editText3 = (EditText)findViewById(R.id.join_email);
        final EditText editText4 = (EditText)findViewById(R.id.join_phone);
        final EditText editText5 = (EditText)findViewById(R.id.join_id);
        final RadioButton radioButton1 = (RadioButton)findViewById(R.id.radioButton_driver);
        final RadioButton radioButton2 = (RadioButton)findViewById(R.id.radioButton_enterprise);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button button = (Button)findViewById(R.id.join_button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //버튼을 눌렀을때 어떤 작업을 할지 선언
                String name = editText.getText().toString();
                String userid = editText1.getText().toString();
                String password = editText2.getText().toString();
                String email = editText3.getText().toString();
                String phone = editText4.getText().toString();
                String admin = null;

                if(radioButton1.isChecked()){
                    admin="0";
                }
                if(radioButton2.isChecked()){
                    admin="1";
                }
//                ConnectThread thread = new ConnectThread(id, password);
//                thread.start();
//
//                 TCP/IP 소켓 서버 연결...

                //////////////////////HTTP WAS 연결/////////////////////////////

                String url = "http://192.168.0.13:8181/Tomcat_Http_Test/JoinServlet";
                HttpClient http = new DefaultHttpClient();
                try {

                    ArrayList<NameValuePair> nameValuePairs =
                            new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("name",name));
                    nameValuePairs.add(new BasicNameValuePair("userid",userid));
                    nameValuePairs.add(new BasicNameValuePair("pwd",password));
                    nameValuePairs.add(new BasicNameValuePair("email",email));
                    nameValuePairs.add(new BasicNameValuePair("phone",phone));
                    nameValuePairs.add(new BasicNameValuePair("admin",admin));

                    HttpParams params = http.getParams();
                    HttpConnectionParams.setConnectionTimeout(params, 5000);
                    HttpConnectionParams.setSoTimeout(params, 5000);

                    HttpPost httpPost = new HttpPost(url);
                    UrlEncodedFormEntity entityRequest =
                            new UrlEncodedFormEntity(nameValuePairs, "EUC-KR");

                    httpPost.setEntity(entityRequest);

                    HttpResponse responsePost = http.execute(httpPost);
                    HttpEntity resEntity = responsePost.getEntity();

                    Toast.makeText(JoinActivity.this,EntityUtils.toString(resEntity) , Toast.LENGTH_SHORT).show();
                   // tv_post.setText( EntityUtils.toString(resEntity));
                }catch(Exception e){e.printStackTrace();}


                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

                intent.putExtra("title", "회원가입 화면");

                //1001 요청코드가 중복되면 안됨
                //열었던 엑티비티에서 응답을 받기 위해선 startActivityForResult 를 써야함.
                //응답을 받을 필요가 없으면 startActivity 만 써도 됨.
                startActivityForResult(intent, 1002);


                Toast.makeText(JoinActivity.this, "회원가입을 요청합니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onButtonClicked(View v){
        //이전 화면으로 데이터 전달

        Intent intent = new Intent();
        intent.putExtra("name","티아라");
        setResult(RESULT_OK,intent);

        //엑티비티 끝냄
        finish();
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

    class HttpThread extends Thread{
        public void run(){}
    }

    class ConnectThread extends Thread{

        String id;
        String password;
        Socket socket;
        OutputStream outputStream;

        public ConnectThread(String id, String password){

            this.id=id;
            this.password=password;
        }

        public void run() {
//join type=1


            String host = "192.168.1.155";
            int port = 10042;

                try {
                    socket = new Socket(host, port);
                    System.out.println("서버로 연결");

                    outputStream = socket.getOutputStream();

                    byte[] pack = null;
                    int type = 1;
                    int headLength = 8;
                    int dataLength = 20;
                    String joindata = id + "/" + password;
                    StringTokenizer data = new StringTokenizer(joindata, "/");

                    if (id != null && password != null) {

                        pack = CreatePacket(type, headLength, data, dataLength);
                        outputStream.write(pack);

                        System.out.println("데이터를 서버로 보냄");
                    } else {
                        Toast.makeText(getApplicationContext(), "아이디와 비밀번호 모두 입력해주세요..", Toast.LENGTH_LONG).show();
                    }
                    Log.d("mainActivity", "connect");

                }catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            finally {
                    try{
                        socket.close();
                        outputStream.close();
                    }
                    catch(Exception e){

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
