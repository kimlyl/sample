package com.example.kgy.mymenu.repair_request;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kgy.mymenu.HomeActivity;
import com.example.kgy.mymenu.JoinActivity;
import com.example.kgy.mymenu.MainActivity;
import com.example.kgy.mymenu.R;
import com.example.kgy.mymenu.Trans;

import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by kgy on 2016-02-19.
 */
public class RepairActivity extends AppCompatActivity{

    ListView listView;
    CheckBox option1;
    CheckBox option2;
    CheckBox option3;
    EditText editText;

    Trans module = Trans.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repair_activity);

        option1 = (CheckBox)findViewById(R.id.insurance_check);
        option2 = (CheckBox)findViewById(R.id.rent_check);
        option3 = (CheckBox)findViewById(R.id.pickup_check);

        editText = (EditText)findViewById(R.id.requestText);

        listView = (ListView)findViewById(R.id.listView);
        final InnerRepairAdapter adapter = new InnerRepairAdapter();
        listView.setAdapter(adapter);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button button = (Button)findViewById(R.id.repair_button);

        Intent intent = getIntent();
        if(intent!=null){
            String title = intent.getStringExtra("title");
            Toast.makeText(getApplicationContext(),"전달 받은 값: " + title,Toast.LENGTH_LONG).show();
        }

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //버튼을 눌렀을때 어떤 작업을 할지 선언

//                ConnectThread thread = new ConnectThread(option1, option2, option3, adapter);
//                thread.start();

                String insurance;
                String pickup;
                String rent;
                String fuelError="true";
                String request = editText.getText().toString();

                if(option1.isChecked()){
                    insurance="true";
                }
                else
                    insurance="false";

                if(option2.isChecked()){
                    rent="true";
                }
                else
                    rent="false";

                if(option3.isChecked()){
                    pickup="true";
                }
                else
                    pickup="false";

                module.sendRequest(insurance,pickup,rent,fuelError,request);

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

                intent.putExtra("title", "땡땡땡");


                //1001 요청코드가 중복되면 안됨
                //열었던 엑티비티에서 응답을 받기 위해선 startActivityForResult 를 써야함.
                //응답을 받을 필요가 없으면 startActivity 만 써도 됨.
                startActivityForResult(intent, 1004);

                Toast.makeText(RepairActivity.this, "수리를 요청 합니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class InnerRepairAdapter extends BaseAdapter{

        String[] list = {"타이어고장", "엔진고장"};

        //ArrayList list = new ArrayList();

        @Override
        //
        public int getCount() {
            //리스트의 갯수 리턴.
            return list.length;
        }

        @Override
        public Object getItem(int position) {
            //각각의 아이템을 리턴
            return list[position];
        }

        @Override
        public long getItemId(int position) {
            //각 아이템의 id를 리턴
            //아이템의 id 는 사용자 설정..

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //각각의 아이템에 대한 뷰를 만듬.

            TextView view = new TextView(getApplicationContext());
            view.setText(list[position]);
            view.setTextSize(20.0f);
            view.setTextColor(Color.BLACK);

            return view;
        }
    }

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
        CheckBox insurance;
        CheckBox rent;
        CheckBox pickup;

        InnerRepairAdapter adapter;

        Socket socket;
        OutputStream outputStream;

        public ConnectThread(CheckBox insurance, CheckBox rent, CheckBox pickup, InnerRepairAdapter adapter){
            this.insurance = insurance;
            this.rent = rent;
            this.pickup = pickup;
            this.adapter=adapter;
        }

        public void run(){
//login type=1

            String host = "192.168.1.155";
            int port = 10042;

            try{

                byte[] pack = null;
                int type = 2;
                int headLength=8;
                int dataLength=5;

                String repair_data="";

                socket = new Socket(host, port);
                System.out.println("서버로 연결");

                outputStream = socket.getOutputStream();


                if(insurance.isChecked()){
                    repair_data+="true/";
                }
                else
                   repair_data+="false/";

                if(rent.isChecked()){
                    repair_data+="true/";
                }
                else
                    repair_data+="false/";

                if(pickup.isChecked()){
                    repair_data+="true";
                }
                else
                    repair_data+="false";

                for(int i=0; i<adapter.getCount(); i++){
                    repair_data+="/"+adapter.getItem(i);
                }

                repair_data.trim();

                StringTokenizer data = new StringTokenizer(repair_data,"/");

                pack = CreatePacket(type,headLength, data, dataLength);
                outputStream.write(pack);

                System.out.println("데이터를 서버로 보냄");
                Log.d("mainActivity", "connect");


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
