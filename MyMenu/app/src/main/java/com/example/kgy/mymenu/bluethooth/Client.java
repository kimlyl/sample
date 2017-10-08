package com.example.kgy.mymenu.bluethooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by kgy on 2016-01-08.
 */
public class Client extends Thread {

    static final UUID BLUE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private Activity activity = null;

    private Socket socket = null;
    private BluetoothSocket mmCSocket = null;

    // 원격 디바이스와 접속을 위한 클라이언트 소켓 생성
    public Client(Activity activity,BluetoothDevice device) {

        this.activity=activity;

        try {
            mmCSocket = device.createInsecureRfcommSocketToServiceRecord(BLUE_UUID);
        } catch(IOException e) {
            return;
        }
    }

    public void run() {
        // 원격 디바이스와 접속 시도
        try {
            if(mmCSocket!=null) {
                mmCSocket.connect();
                onConnected(mmCSocket);
            }
        }catch(IOException e){

             // 접속이 실패했으면 소켓을 닫는다
            try {
                mmCSocket.close();

            } catch (IOException e2){

            }
            return;
        }

    }

    // 클라이언트 소켓 중지
    public void cancel() {
        try {
            mmCSocket.close();

        } catch (IOException e) {

        }
    }

    private void onConnected(BluetoothSocket socket)
    {
        Socket socketThread = null;

        socketThread = new Socket(activity,socket);
        socketThread.start();

        WriteThread write = new WriteThread(socketThread);
        write.start();
    }
}