package com.example.listener;

import android.app.ActivityManager;
import android.app.Application;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.util.Log;

import com.example.bean.BlueTooth;
import com.example.bean.SysUser;
import com.example.db.SysUserDb;
import com.example.util.ActivityCollector;
import com.example.util.MyApplication;
import com.example.util.OkhttpSendBlueMessage;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BluetoothListenerInit {
    private static boolean status = false ;      //用于判断线程是否继续，当退出活动时，结束子线程
    private BluetoothClient mClient;
    private List<String> macLIst;
    private BlueTooth blueTooth;
    private List<BlueTooth> blueToothList;
    //A和n的值，需要根据实际环境进行检测得出
    private static final double A_Value=59;/**A - 发射端和接收端相隔1米时的信号强度*/
    private static final double n_Value=2.5;/** n - 环境衰减因子*/
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
//    private BlueToothValueReceiver blueToothValueReceiver;

    public BluetoothListenerInit() {
        //当搜索未启动才进行

//        Log.d("蓝牙", "status = "+ status);
        if(status == false) {
            mClient = new BluetoothClient(MyApplication.getContext());
            status = true;
            if(mClient.isBluetoothOpened()) {
                new MyThread().start();
            } else {

                boolean isOpen = mClient.openBluetooth();
                if(isOpen) {
                    try {
                        Thread.sleep(10000);
                        new MyThread().start();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    status = false;
                    ActivityCollector.finishAll();
                }
//                try {
//                    Thread.sleep(5000);
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

//                if(mClient.openBluetooth()) {
//                   new Thread() {
//                       @Override
//                       public void run() {
//                           super.run();
//                           try {
//                               Thread.sleep(2000);//休眠2秒再搜索
//                               if(mClient.isBluetoothOpened()) {
//                                   new MyThread().start();
//                               }
//
//                           } catch (InterruptedException e) {
//                               e.printStackTrace();
//                           }
//                           /**
//                            * 要执行的操作
//                            */
//                       }
//                   }.start();
//               } else {
//
//               }

            }
        }
    }

    public void seacherBlue() {
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(6000, 2)   // 先扫BLE设备3次，每次3s
                .searchBluetoothClassicDevice(5000,2) // 再扫经典蓝牙5s
                .searchBluetoothLeDevice(4000)      // 再扫BLE设备2s
                .build();

        mClient.search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {
//                Log.d("蓝牙", "搜索开始");
            }

            @Override
            public void onDeviceFounded(SearchResult device) {

                SysUser user = new SysUserDb().getSysUser();

                if(blueToothList.size()==0) {
                    blueTooth = new BlueTooth();
                    if(device.device.getBluetoothClass().getMajorDeviceClass() == BluetoothClass.Device.PHONE_UNCATEGORIZED) {
                        macLIst.add(device.getAddress());
                        double distance = (Math.abs(device.rssi)-A_Value)/(10*n_Value);
                        distance = Double.parseDouble(String.format("%.2f", Math.pow(10,distance)));
                        blueTooth.setBlueDistance((float)distance);
                        blueTooth.setUserId(user.getUserId());
                        if(user.getUserMacAddress() == null) {
                            blueTooth.setUserMacAddress("00:00:00:00:00:00");
                        } else {
                            blueTooth.setUserMacAddress(user.getUserMacAddress());
                        }
                        blueTooth.setFriendMacAddress(device.getAddress());
                        blueToothList.add(blueTooth);
                        sendBluetoothMessage(blueTooth);
//                        Log.d("蓝牙", device.getAddress()+"=>手机 "+device.getName() +"  距离："+distance);
//                        Log.d("蓝牙", device.getAddress()+"=>手机 "+device.getName() +"  float距离："+distance);
//                        Log.d("蓝牙","蓝牙个数 = "+ blueToothList.size());
//                        Log.d("蓝牙","地址个数 = "+ macLIst.size());
                    }
                }
                for (int i=0; i<blueToothList.size(); i++) {
                    blueTooth = new BlueTooth();
                    if (blueToothList.get(i).getFriendMacAddress().equals(device.getAddress())) {
                        break;
                    }
                    if(i==blueToothList.size()-1) {

                        if(device.device.getBluetoothClass().getMajorDeviceClass() == BluetoothClass.Device.PHONE_UNCATEGORIZED) {
                            macLIst.add(device.getAddress());
                            double distance = (Math.abs(device.rssi)-A_Value)/(10*n_Value);
                            distance = Double.parseDouble(String.format("%.2f", Math.pow(10,distance)));
                            blueTooth.setBlueDistance((float) distance);
                            blueTooth.setUserId(user.getUserId());
                            if(user.getUserMacAddress() == null) {
                                blueTooth.setUserMacAddress("00:00:00:00:00:00");
                            } else {
                                blueTooth.setUserMacAddress(user.getUserMacAddress());
                            }

                            blueTooth.setFriendMacAddress(device.getAddress());
                            blueToothList.add(blueTooth);
                            sendBluetoothMessage(blueTooth);
//                            Log.d("蓝牙", device.getAddress()+"=>手机 "+device.getName() +"  距离："+distance);
//                            Log.d("蓝牙", device.getAddress()+"=>手机 "+device.getName() +"  float距离："+distance);
//                            Log.d("蓝牙","蓝牙个数 = "+ blueToothList.size());
//                            Log.d("蓝牙","地址个数 = "+ macLIst.size());
                        }
                    }

                }

//                Log.d("蓝牙","蓝牙个数 = "+ blueToothList.size());
            }

            @Override
            public void onSearchStopped() {
//                Log.d("蓝牙", "搜索停止");
            }

            @Override
            public void onSearchCanceled() {
//                Log.d("蓝牙", "搜索取消");
                onSearchStopped();
                status = false;
                ActivityCollector.finishAll();



//                setStatus(false);
//                new BluetoothListenerInit();
//                resetOpenblue();
            }
        });
    }

    class MyThread extends Thread{
        public void run() {
            while (status) {
                try {
                    macLIst = new ArrayList<>();
                    blueToothList = new ArrayList<>();
                    new SearchBluetooth().start();
                    Thread.sleep(1000 * 60);//每隔60s执行一次
//                    Log.d("蓝牙", "个数 = "+blueToothList.size());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class SearchBluetooth extends Thread {
        public void run() {
            seacherBlue();

        }
    }


    public BluetoothClient getmClient() {
        return mClient;
    }

    public void setmClient(BluetoothClient mClient) {
        this.mClient = mClient;
    }

    public static boolean getStatus() {
        return status;
    }

    public static void setStatus(boolean status) {
        BluetoothListenerInit.status = status;
    }


    public void sendBluetoothMessage(BlueTooth blueTooth) {
        try {
            new OkhttpSendBlueMessage(){}.execute(blueTooth).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
