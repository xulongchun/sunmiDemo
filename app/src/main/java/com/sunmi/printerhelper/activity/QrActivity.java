package com.sunmi.printerhelper.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sunmi.externalprinterlibrary.api.ConnectCallback;
import com.sunmi.externalprinterlibrary.api.SunmiPrinter;
import com.sunmi.externalprinterlibrary.api.SunmiPrinterApi;
import com.sunmi.printerhelper.R;
import com.sunmi.printerhelper.utils.BitmapUtil;
import com.sunmi.printerhelper.utils.BluetoothUtil;
import com.sunmi.printerhelper.utils.ESCUtil;
import com.sunmi.printerhelper.utils.SunmiNetPrintHelper;
import com.sunmi.printerhelper.utils.SunmiPrintHelper;

import sunmi.sunmiui.dialog.DialogCreater;
import sunmi.sunmiui.dialog.EditTextDialog;
import sunmi.sunmiui.dialog.ListDialog;

/**
 *  Example of printing a QR code
 *  In order to comply with the standard ESC instruction, printing QR code through Bluetooth
 *  instruction mode does not support side-by-side printing!
 *  @author kaltin
 */
public class QrActivity extends BaseActivity {
    private ImageView mImageView;
    private TextView mTextView1, mTextView2, mTextView3, mTextView4;
    private int print_size = 8;
    private int error_level = 3;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        setMyTitle(R.string.qr_title);
        setBack();
        initNet(this);

        mImageView = findViewById(R.id.qr_image);
        mTextView1 = findViewById(R.id.qr_text_content);
        mTextView2 = findViewById(R.id.qr_text_size);
        mTextView3 = findViewById(R.id.qr_text_el);
        mTextView4 = findViewById(R.id.qr_align_info);

        findViewById(R.id.qr_content).setOnClickListener(new View.OnClickListener() {
            EditTextDialog mDialog;

            @Override
            public void onClick(View v) {
                mDialog = DialogCreater.createEditTextDialog(QrActivity.this, getResources().getString(R.string.cancel), getResources().getString(R.string.confirm), getResources().getString(R.string.input_qrcode), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.cancel();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTextView1.setText(mDialog.getEditText().getText());
                        mDialog.cancel();
                    }
                }, null);
                mDialog.setHintText("www.sunmi.com");
                mDialog.show();
            }
        });

        findViewById(R.id.qr_size).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ListDialog listDialog = DialogCreater.createListDialog(QrActivity.this, getResources().getString(R.string.size_qrcode), getResources().getString(R.string.cancel), new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"});
                listDialog.setItemClickListener(new ListDialog.ItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        position += 1;
                        mTextView2.setText("" + position);
                        print_size = position;
                        listDialog.cancel();
                    }
                });
                listDialog.show();
            }
        });

        findViewById(R.id.qr_el).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] el = new String[]{"????????????L (7%)", "????????????M (15%)", "????????????Q (25%)", "????????????H (30%)"};
                final ListDialog listDialog = DialogCreater.createListDialog(QrActivity.this, getResources().getString(R.string.error_qrcode), getResources().getString(R.string.cancel), el);
                listDialog.setItemClickListener(new ListDialog.ItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        mTextView3.setText(el[position]);
                        error_level = position;
                        listDialog.cancel();
                    }
                });
                listDialog.show();
            }
        });

        findViewById(R.id.qr_align).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String[] pos = new String[]{getResources().getString(R.string.align_left),getResources().getString(R.string.align_mid), getResources().getString(R.string.align_right)};
                final ListDialog listDialog =  DialogCreater.createListDialog(QrActivity.this, getResources().getString(R.string.align_form), getResources().getString(R.string.cancel), pos);
                listDialog.setItemClickListener(new ListDialog.ItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        mTextView4.setText(pos[position]);
                        if(!BluetoothUtil.isBlueToothPrinter){
                            SunmiPrintHelper.getInstance().setAlign(position);
                        }else{
                            byte[] send;
                            if(position == 0){
                                send = ESCUtil.alignLeft();
                            }else if(position == 1){
                                send = ESCUtil.alignCenter();
                            }else {
                                send = ESCUtil.alignRight();
                            }
                            BluetoothUtil.sendData(send);
                        }
                        listDialog.cancel();
                    }
                });
                listDialog.show();
            }
        });
    }



    public void onClick(final View view) {

        try{
            System.out.println( "????????????????????? "+SunmiPrinterApi.getInstance().isConnected());
        }catch (Exception e){

        }
        SunmiNetPrintHelper printHelper=  SunmiNetPrintHelper.getInstance(view.getContext());
        printHelper.initPrinterService("192.168.232.106");
        printHelper.printText("??????");



    }

    private void  printText(Context context){

        try {
            SunmiPrinterApi.getInstance().setFontZoom(2,2);
            SunmiPrinterApi.getInstance().setAlignMode(1);
            for (int i = 0; i < 20; i++) {
                SunmiPrinterApi.getInstance().printText("?????????????????? " + i + "\n");
            }
//            SunmiPrinterApi.getInstance().cutPaper(20, 5);
//            SunmiPrinterApi.getInstance().disconnectPrinter(context);
        }
        catch (Exception e) {
            System.out.println( e);
        }

    }



    private void initNet(Context context) {
        try {
            SunmiPrinterApi.getInstance().setPrinter(SunmiPrinter.SunmiNetPrinter,"192.168.232.106");
            //2??????????????????
            SunmiPrinterApi.getInstance().connectPrinter(context, new ConnectCallback() {
                @Override
                public void onFound() {
                    System.out.println("onfund");
                    //?????????????????????????????????
                }

                @Override
                public void onUnfound() {
                    System.out.println("not found print");
                    //??????????????????????????????????????????
                }

                @Override
                public void onConnect() {
                    //???????????????????????????????????????????????????
                    try{
                        System.out.println("connect print");


                    }catch (Exception e){
                        System.out.println("connect print error");
                    }
                }

                @Override
                public void onDisconnect() {
                    System.out.println("onDisconnect print");
                    //??????????????????????????????????????????????????????????????????

                }
            });
        } catch (Exception e) {
         System.out.println( e);
        }
    }


}
