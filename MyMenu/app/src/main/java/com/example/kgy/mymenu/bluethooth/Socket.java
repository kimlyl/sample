package com.example.kgy.mymenu.bluethooth;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.example.kgy.mymenu.R;

/**
 * Created by kgy on 2016-01-08.
 */
public class Socket extends Thread {
    private final BluetoothSocket mmSocket; // 클라이언트 소켓
    private InputStream mmInStream; // 입력 스트림
    private OutputStream mmOutStream; // 출력 스트림

    private boolean useable = false;

    private String strDate = null;
    private Double total = 0.0;

    private Activity activity = null;

    private int countspeed = 0;
    private int totalspeed = 0;

    private int countrpm = 0;

    public Socket(Activity activity,BluetoothSocket socket) {

        this.activity=activity;
        total = 0.0;

        mmSocket = socket;

        // 입력 스트림과 출력 스트림을 구한다
        try {
            mmInStream = socket.getInputStream();
            mmOutStream = socket.getOutputStream();
        } catch (IOException e) {
            showMessage("Get Stream error");
        }
    }

    // 소켓에서 수신된 데이터를 화면에 표시한다
    public void run() {
        while (true) {
            try {
                // 입력 스트림에서 데이터를 읽는다
                StringBuilder res = new StringBuilder();

                byte b1;
                while((char)(b1 = (byte)mmInStream.read()) != 62) {
                    if((char)b1 != 32) {
                        res.append((char)b1);
                    }
                }

                String strBuf = res.toString().trim();

                if(strBuf.contains("SUCCESS") || strBuf.contains("CONNECTED") )
                {
                    useable = true;
                    showMessage("OBD-II 연결 성공");
                    SystemClock.sleep(100L);
                }
                else {
                    showMessage(strBuf);
                }
                SystemClock.sleep(10);
            } catch (IOException e) {
                useable = false;
                showMessage("Socket disconneted");
                break;
            }
        }
    }

    // 데이터를 소켓으로 전송한다
    public void write(String strBuf) {
        try {
            // 출력 스트림에 데이터를 저장한다
            strBuf = strBuf + "\r";
            byte[] buffer = strBuf.getBytes();
            mmOutStream.write(buffer);
            mmOutStream.flush();
            sleep(200L);

        } catch (IOException e) {
            showMessage("Socket write error");
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void showMessage(String strMsg) {
        // 메시지 텍스트를 핸들러에 전달
        Message msg = Message.obtain(mHandler, 0, strMsg);
        mHandler.sendMessage(msg);
        Log.d("tag1", strMsg);
    }

    Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String strMsg = (String)msg.obj;

                if (strMsg.contains("410D"))
                {
                    TextView tv2 = (TextView)activity.findViewById(R.id.speed);
                    TextView totaltv = (TextView)activity.findViewById(R.id.distance);
                    Log.d("MESSAGE",strMsg);
                    String res = strMsg;
                    if(!"NODATA".equals(res)) {
                        try{
                            countspeed = countspeed+1;
                            String s = res.substring(9, 11);
                            Integer i = Integer.parseInt(s, 16);
                            res = i.toString() ;

                            total += i.doubleValue() / 18000.0;

                            String res2 = String.format("%.2f",total);

                            if((totalspeed + i) >= Integer.MAX_VALUE )
                            {
                                //만약 21억이 넘는 숫자가 대입된다면 다시 카운트를 시작함
                                totalspeed = (totalspeed/countspeed)+i;
                                countspeed = 2;
                            }
                            else totalspeed += i;

                            int speedavg = totalspeed/countspeed;       // 평균 속력을 구하는 공식

                        }catch(Exception e){Log.e("Error",e.getMessage());}
                    }
                    String res2 = String.format("%.2f",total);
                    totaltv.setText(res2);
                    tv2.setText(res);
                }
                else if (strMsg.contains("410C"))
                {
                    TextView tv3 = (TextView)activity.findViewById(R.id.rpm);
                    String res = strMsg;
                    if(!res.contains("NO"))
                    {
                        String s = res.substring(9,11);
                        String s2 = res.substring(11,13);

                        Integer a = Integer.parseInt(s, 16);
                        Integer b = Integer.parseInt( s2, 16 );

                        Integer sum = (a * 256 + b) / 4;

                        res = sum.toString();

                    }
                    tv3.setText(res);
                }
                else if(strMsg.contains("4110"))
                {
                    TextView tv4 = (TextView)activity.findViewById(R.id.ifef);
                    String res = strMsg;
                    if(!res.contains("NO")) {
                        String s = res.substring(9, 11);
                        String s2 = res.substring(11, 13);

                        Integer a = Integer.parseInt(s, 16);
                        Integer b = Integer.parseInt(s2, 16);

                        float f = (float) (a * 256 + b) / 100.0F;

                        f = f/(10731.0F);
                        res = String.format("%.4f",f);

                    }
                    tv4.setText(res);
                }
            }

        }
    };


    public boolean isUseable(){
        return this.useable;
    }
}
