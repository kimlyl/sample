package com.example.kgy.mymenu.bluethooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by kgy on 2016-01-08.
 */
public class Server extends Thread {

    static final String  BLUE_NAME = "BlueToothOBD";
    static final UUID BLUE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private Activity activity=null;

    private BluetoothServerSocket mmSSocket = null;

    // 서버 소켓 생성
    public Server(Activity activity, BluetoothAdapter mBA) {
        try {
            mmSSocket = mBA.listenUsingInsecureRfcommWithServiceRecord(BLUE_NAME, BLUE_UUID);

        } catch(IOException e) {

        }
    }

    public void run() {
        BluetoothSocket cSocket = null;

        // 원격 디바이스에서 접속을 요청할 때까지 기다린다
        try {
            if(mmSSocket!=null) {
                cSocket = mmSSocket.accept();
                onConnected(cSocket);
                // 원격 디바이스와 접속되었으면 데이터 송수신 스레드를 시작
            }
        } catch(IOException e){
            return;
        }
    }

    // 서버 소켓 중지
    public void cancel() {
        try {
            mmSSocket.close();
        } catch (IOException e) {

        }
    }

    private void onConnected(BluetoothSocket socket)
    {
        Socket socketThread = null;

        socketThread = new Socket(activity,socket);
        socketThread.start();
    }
}

