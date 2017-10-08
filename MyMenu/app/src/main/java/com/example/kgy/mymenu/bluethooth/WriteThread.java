package com.example.kgy.mymenu.bluethooth;

/**
 * Created by kgy on 2016-01-08.
 */
public class WriteThread extends Thread
{
    Socket mSocketThread = null;


    public WriteThread(Socket mSocketThread) {
        this.mSocketThread = mSocketThread;

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run()
    {
        while(true)
        {
            if(mSocketThread != null)
            {
                if( mSocketThread.isUseable())
                {
                    mSocketThread.write("01 0D");
                    try {
                        sleep(250L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mSocketThread.write("01 0C");
                    try {
                        sleep(250L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mSocketThread.write("01 10");
                }
            }
            try {
                sleep(250L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
