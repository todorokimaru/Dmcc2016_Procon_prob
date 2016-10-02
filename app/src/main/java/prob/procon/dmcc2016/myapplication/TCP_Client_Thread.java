package prob.procon.dmcc2016.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Maeda Hiromu on 2016/09/30.
 */

public class TCP_Client_Thread {
    private Socket      socket;
    private InputStream in;
    private OutputStream out;

    //接続時に使う変数
    private String  mIP;
    private int mPort;
    private boolean mRetConnect;

    //受信時に使う
    //byte[] recv(int size)
    private byte[] mRetRecv;
    private int mSizeRecv;

    //接続
    private boolean connect(String ip, int port){
        Log.v("connect", "connect start");
        try{
            //ソケット接続
            socket = new Socket(ip, port);

            if(socket.isConnected() && socket != null){
                in = socket.getInputStream();
                out = socket.getOutputStream();
                Log.v("connect", "接続成功");
                return true;
            }
            else{
                Log.v("connect", "接続失敗");
                return false;
            }
        }
        catch (Exception e){
            Log.v("connect", e.toString());
        }
        return false;
    }
    public boolean connectTh(String ip,int port)
    {
        mIP = ip;
        mPort = port;
        //スレッドの生成
        Thread thread=new Thread(){
            public void run() {
                mRetConnect = connect( mIP , mPort );//戻り値をメンバ変数に代入
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.d("TCPIP.connectTh",e.toString());
        }
        return mRetConnect;//戻り値を戻す
    }

    //切断
    public void close() {
        try {
            socket.close();
            socket=null;
        } catch (Exception e) {
        }
    }

    private byte [] mBufSend;
    private boolean mRetSend;

    public boolean sendTh(byte[] Buf)
    {
        mBufSend = Buf;
        Thread thread=new Thread(){
            public void run() {
                mRetSend = send( mBufSend );//戻り値をメンバ変数に代入
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.d("TCPIP.sendTh",e.toString());
        }

        return mRetSend;
    }

    //送信用メソッド
    private boolean send(byte[] Buf)
    {
        boolean ret = true;

/*      Log.d("TCPIP.send",String.valueOf(Buf.length));
        for( int i = 0 ; i<Buf.length ; i++)
            Log.d("TCPIP.send",String.valueOf( ((char)Buf[i]) ));
*/
        try {
            out.write(Buf , 0 , Buf.length);//送信
            out.flush();//強制的に送信させる
        } catch (Exception e) {

            Log.d("TCPIP.send",e.toString());
            ret = false;//なにかエラーがあったらfalseになる
        }
        Log.d("TCPIP.send","END");
        return ret;
    }

    public  byte[] recvTh(int size)
    {
        mSizeRecv = size;
        Thread thread=new Thread(){
            public void run() {
                mRetRecv = recv( mSizeRecv );//戻り値をメンバ変数に代入
                Log.d("TCPIP.recvTh",String.valueOf(mRetRecv.length)+"(th)");
                Log.d("TCPIP.recvTh",String.valueOf((char)mRetRecv[0]) +"(th)");

            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.d("TCPIP.recvTh",e.toString());
        }

        Log.d("TCPIP.recvTh",String.valueOf(mRetRecv.length));
        //Log.d("TCPIP.recvTh",String.valueOf(mRetRecv[0]));
        return mRetRecv;
    }

    private byte[] recv(int size)
    {
        // ソケット受信
        int readDataSize = 0;//読み込んだデータのサイズ
        int temp;

        byte[] dat = new byte[ size ];//受信予定容量分確保
        while (size > readDataSize)
        {
            //Log.i("recv","readDataSize:"+readDataSize);
            try {
                if (in.available() > 0)//受信可能なとき
                {
                    //temp = in.available();//objSck.Availableは受信してしまうと0になるため、受信前に取っておく必要がある
                    Log.i("TCPIP.recv","in.available():"+in.available());
                    readDataSize += in.read(dat, readDataSize, dat.length - readDataSize);//読み込んだ容量分ずらす。　読み込み可能容量だけ、読み込む

                    //if (in.available() > (size - readDataSize))//もし、必要な容量より、受信可能容量が大きかった場合必要以上に受信しない
                    //{
                    //    readDataSize += (size - readDataSize);
                    //}
                    //else
                    //{
                    //    readDataSize += (temp);
                    //}
                }
            }catch (Exception e) {
                Log.d("TCPIP.recv",e.toString());
            }
        }
        return dat;
    }
    public static boolean netWorkCheck(Context context){
        ConnectivityManager cm =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if( info != null ){
            return info.isConnected();
        } else {
            return false;
        }
    }
}
