package com.sunmi.printerhelper.utils;

import android.content.Context;
import android.util.Log;

import com.sunmi.externalprinterlibrary.api.ConnectCallback;
import com.sunmi.externalprinterlibrary.api.SunmiPrinter;
import com.sunmi.externalprinterlibrary.api.SunmiPrinterApi;

public class SunmiNetPrintHelper {

    private static Context contexts;

    private static SunmiNetPrintHelper helper = new SunmiNetPrintHelper();

    private SunmiNetPrintHelper() {
    }

    public static SunmiNetPrintHelper getInstance(Context context) {
        contexts = context;
        return helper;
    }


    /**
     * 打印机是否已经连接
     * @return
     */
    public boolean isConnected(){
        try {
          return   SunmiPrinterApi.getInstance().isConnected();
        } catch (Exception e) {
            Log.d("setFontZoom", e.getMessage());
        }
        return false;
    }
    /**
     * init sunmi print service
     */
    public void initPrinterService(String ip) {

        try {
            SunmiPrinterApi.getInstance().setPrinter(SunmiPrinter.SunmiNetPrinter, ip);
            //2、连接打印机
            SunmiPrinterApi.getInstance().connectPrinter(contexts, new ConnectCallback() {
                @Override
                public void onFound() {
                    //发现打印机会回调此⽅法
                    Log.d("int.print", "onfund");
                }

                @Override
                public void onUnfound() {
                    Log.d("int.print", "onfund");
                }

                @Override
                public void onConnect() {
                    //连接成功后会回调此⽅法，则可以打印
                    try {

                        Log.d("int.print", "connect print");
                    } catch (Exception e) {
                        Log.d("int.print", "connect print error");
                    }
                }

                @Override
                public void onDisconnect() {
                    Log.d("int.print", "onDisconnect print");
                    //连接中打印机断开会回调此⽅法，此时将中断打印
                }
            });
        } catch (Exception e) {
            Log.d("int.print.error", e.getMessage());
        }

    }

    /**
     * 设置字体大小
     *
     * @param hori
     * @param veri
     */
    public void setFontZoom(int hori, int veri) {
        try {
            SunmiPrinterApi.getInstance().setFontZoom(hori, veri);
        } catch (Exception e) {
            Log.d("setFontZoom", e.getMessage());
        }

    }

    /**
     * 设置对齐方式
     *
     * @param type
     */
    public void setAlignMode(int type) {
        try {
            SunmiPrinterApi.getInstance().setAlignMode(type);
        } catch (Exception e) {
            Log.d("setAlignMode", e.getMessage());
        }
    }

    /**
     * 切刀切纸
     * m 切纸模式 0：全切 1：半切 2：进纸切纸
     * n 进纸距离 此参数只有在设置 m=2时有效， 由于打印机型号不同切⼑到打印头距离不同，当n=0
     * 时将⾃动⾛纸此距离，n>0将⾛额外设置距离
     */
    public void cutPaper(int m, int n) {
        try {
            SunmiPrinterApi.getInstance().cutPaper(m, n);
        } catch (Exception e) {
            Log.d("setAlignMode", e.getMessage());
        }
    }

    public void printText(final String text) {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
                try {
//                    setAlignMode(1);
//                    setFontZoom(2,2);
                    SunmiPrinterApi.getInstance().printText(text + "\n");
//                    setFontZoom(0,0);
//                    setAlignMode(2);
//                    SunmiPrinterApi.getInstance().printText(text + "\n");
//                    cutPaper(2,8);

                } catch (Exception e) {
                    Log.d("printText", e.getMessage());
                }
//
//            }
//        }).start();
    }

}
