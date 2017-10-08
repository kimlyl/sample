package com.example.kgy.mymenu.bluethooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by kgy on 2016-01-08.
 */
public class BluetoothService{

    ListView mListDevice;
    ArrayList<String> mArDevice;

    private BluetoothAdapter mBA = null;

    private Server mSThread = null;
    private Client mCThread = null;

    private String stateMsg = null;

    private Activity activity = null;

    public BluetoothService(Activity activity) {
        this.activity = activity;
    }

    public boolean startService()
    {
        if(canUseBluetooth()) {
            return true;
        }
        return false;
    }

    public boolean canUseBluetooth() {
        // 블루투스 어댑터를 구한다
        mBA = BluetoothAdapter.getDefaultAdapter();
        // 블루투스 어댑터가 null 이면 블루투스 장비가 존재하지 않는다.
        if (mBA == null) {
            stateMsg = "디바이스를 찾을 수 없습니다.";
            return false;
        }
        // 블루투스 활성화 상태라면 함수 탈출
        if (mBA.isEnabled()) {
            stateMsg = "블루투스 연결이 가능합니다.";

            getParedDevice();
            return true;
        }

        // 사용자에게 블루투스 활성화를 요청한다
        stateMsg = "블루투스를 연결 해야합니다.";

        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, 101);
        return false;
    }

    public void startFindDevice() {
        // 원격 디바이스 검색 중지
        stopFindDevice();
        // 디바이스 검색 시작
        mBA.startDiscovery();
        // 원격 디바이스 검색 이벤트 리시버 등록
        activity.registerReceiver(mBlueRecv, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    // 디바이스 검색 중지
    public void stopFindDevice() {
        // 현재 디바이스 검색 중이라면 취소한다
        if (mBA.isDiscovering()) {
            mBA.cancelDiscovery();
            // 브로드캐스트 리시버를 등록 해제한다
            activity.unregisterReceiver(mBlueRecv);
        }
    }

    // 원격 디바이스 검색 이벤트 수신
    BroadcastReceiver mBlueRecv = new BroadcastReceiver() {
        public void  onReceive(Context context, Intent intent) {
            if( intent.getAction() == BluetoothDevice.ACTION_FOUND ) {
                BluetoothDevice device = intent.getParcelableExtra( BluetoothDevice.EXTRA_DEVICE );

                if( device.getBondState() != BluetoothDevice.BOND_BONDED )
                {
                    if(device.getAddress().equals("AA:BB:CC:11:22:33"))
                    {
                        stopFindDevice();

                        mSThread.cancel();
                        mSThread = null;

                        if( mCThread != null ) return;

                        mCThread = new Client(activity,device);
                        mCThread.start();
                    }
                }
            }
        }
    };

    // 페어링된 원격 디바이스 목록 구하기
    public void getParedDevice() {
        if (mSThread != null) return;
        // 서버 소켓 접속을 위한 스레드 생성 & 시작
        mSThread = new Server(activity, mBA);
        mSThread.start();

        // 블루투스 어댑터에서 페어링된 원격 디바이스 목록을 구한다
        Set<BluetoothDevice> devices = mBA.getBondedDevices();
        // 디바이스 목록에서 하나씩 추출
        for (BluetoothDevice device : devices) {
            if (device.getAddress().equals("AA:BB:CC:11:22:33")) {

                stopFindDevice();

                mSThread.cancel();
                mSThread = null;

                if (mCThread != null) return;

                mCThread = new Client(activity, device);
                mCThread.start();

                return;
            }

        }

        // 원격 디바이스 검색 시작
        startFindDevice();
    }
}