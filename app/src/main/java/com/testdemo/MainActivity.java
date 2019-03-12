package com.testdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.testdemo.util.RootCmd;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int AC_LISTENER = 0;//开始监控
    private static final int AC_RECORD_END = 1;//监控结束

    private boolean isRecording = false;//是否正在录制
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.arg1 == AC_RECORD_END){
                isRecording = false;
                mHandler.sendEmptyMessageDelayed(AC_LISTENER,5000);
            }else if (msg.what == AC_LISTENER){
                adbDumpsysActivityTop();
            }


            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void climbStairs(int n) {
        // base cases
        String commend = "chmod 777 /dev/bus/usb/ -R \n";
        String result = RootCmd.execRootCmd(commend);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_screen:
                adbScreen();
                break;
            case R.id.btn_mp4:
                adbMP4();
                break;
            case R.id.btn_atmp4:
                mHandler.sendEmptyMessageDelayed(AC_LISTENER,1000);
                break;
        }
    }

    public void adbps() {
        // base cases
        String commend = "ps \n";
        String result = RootCmd.execRootCmd(commend);
        Log.i("adb",result);
    }

    public void adbDumpsysActivityTop(){
        String commend = "dumpsys activity top \n";
        String result = RootCmd.execRootCmd(commend);
//        Log.i("adb",result);
        if (result.contains(".activity.login.tradlogin.LoginActivity") || result.contains(".ui.account.LoginHistoryUI")){
            //执行录像
            Log.i(TAG," >>> 监控到民生银行登录页面");
            adbMP4();
        }else{
            Log.i(TAG," >>> 没有监控到民生银行登录页面");
            mHandler.sendEmptyMessageDelayed(AC_LISTENER,2000);
        }
    }

    public void adbScreen(){
        String cmd = "screencap -p /sdcard/adbtest/"+System.currentTimeMillis()+".png";
        String commend = cmd+" \n";
        String result = RootCmd.execRootCmd(commend);
        Log.i("adb",result);

    }

    public void adbMP4(){
        if (isRecording){
            Log.i(TAG," >>> 已经执行录像，直接返回");
            return;
        }
        Log.i(TAG," >>> 开始执行录像");
        String cmd = "screenrecord --time-limit 10 /sdcard/adbtest/"+System.currentTimeMillis()+".mp4";
        String commend = cmd+" \n";
        isRecording = true;
        mHandler.sendEmptyMessageDelayed(AC_RECORD_END,1000*10);
        String result = RootCmd.execRootCmd(commend);
        Log.i("adb",result);

    }
}
