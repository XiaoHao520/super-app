package com.school.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.BuildConfig;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.school.Manifest;
import com.school.R;

import java.io.File;
import java.util.Map;
import java.util.UUID;


public class MyStoresActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ALBUM = 0x01;

    private static final int REQUEST_CODE_CAMERA = 0x02;

    private static final int REQUEST_CODE_PERMISSION_CAMERA = 0x03;

    WebView myStore;
    String url = "http://www.sinbel.top/ui/web/user/mystore.html";
    private ValueCallback<Uri> uploadMessage;

    private ValueCallback<Uri[]> uploadMessageAboveL;

    private String mCurrentPhotoPath;

    private String mLastPhothPath;

    private Thread mThread;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stores);
        mContext = getApplicationContext();
        initView();

    }

    private void initView() {
        Intent intent = getIntent();
        myStore = (WebView) findViewById(R.id.myStore);
        WebSettings setting = myStore.getSettings();
        setting.setJavaScriptEnabled(true);
        myStore.addJavascriptInterface(this, "android");
        System.out.println(url + "?username=" + intent.getStringExtra("username"));
        myStore.loadUrl(url + "?username=" + intent.getStringExtra("username"));
        myStore.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                myStore.loadUrl(url);
                return true;
            }
        });
        myStore.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {

                uploadMessageAboveL = filePathCallback;
                uploadPicture();
                return true;
            }
        });
    }

    @JavascriptInterface
    public void back() {
        this.finish();
    }

    @JavascriptInterface
    public void loadUrl(String url) {


    }


    @JavascriptInterface
    public void getDetailAddress() {
        System.out.println("打开地图");
        Intent intent = new Intent(MyStoresActivity.this, MapActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {
                Map<String, String> map = (Map<String, String>) data.getSerializableExtra("map");
                if (map != null) {
                    System.out.println(map.toString());
                    myStore.loadUrl("javascript:setAddress('" + map.get("address") + "','" + map.get("lat") + "','" + map.get("lon") + "')");
                }
            }
        }

        if (requestCode == REQUEST_CODE_ALBUM || requestCode == REQUEST_CODE_CAMERA) {
            if (uploadMessage == null && uploadMessageAboveL == null) {
                return;
            }

            //取消拍照或者图片选择时

            if (resultCode != RESULT_OK) {

                //一定要返回null,否则<input file> 就是没有反应

                if (uploadMessage != null) {

                    uploadMessage.onReceiveValue(null);

                    uploadMessage = null;

                }

                if (uploadMessageAboveL != null) {
                    uploadMessageAboveL.onReceiveValue(null);
                    uploadMessageAboveL = null;
                }
            }

            //拍照成功和选取照片时
            if (resultCode == RESULT_OK) {
                Uri imageUri = null;
                switch (requestCode) {

                    case REQUEST_CODE_ALBUM:



                        if (data != null) {

                            imageUri = data.getData();

                        }



                        break;

                    case REQUEST_CODE_CAMERA:



                        if (!TextUtils.isEmpty(mCurrentPhotoPath)) {

                            File file = new File(mCurrentPhotoPath);

                            Uri localUri = Uri.fromFile(file);

                            Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);

                            sendBroadcast(localIntent);

                            imageUri = Uri.fromFile(file);

                            mLastPhothPath = mCurrentPhotoPath;

                        }

                        break;

                }





                //上传文件

                if (uploadMessage != null) {

                    uploadMessage.onReceiveValue(imageUri);

                    uploadMessage = null;

                }

                if (uploadMessageAboveL != null) {

                    uploadMessageAboveL.onReceiveValue(new Uri[]{imageUri});

                    uploadMessageAboveL = null;



                }



            }



        }



    }


    /**
     * 选择相机或者相册
     */

    public void uploadPicture() {


        AlertDialog.Builder builder = new AlertDialog.Builder(MyStoresActivity.this);

        builder.setTitle("请选择图片上传方式");
        //取消对话框

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override

            public void onCancel(DialogInterface dialog) {


                //一定要返回null,否则<input type='file'>

                if (uploadMessage != null) {

                    uploadMessage.onReceiveValue(null);

                    uploadMessage = null;

                }

                if (uploadMessageAboveL != null) {

                    uploadMessageAboveL.onReceiveValue(null);

                    uploadMessageAboveL = null;


                }

            }

        });


        final Handler mHandler = new Handler() {

            @Override

            public void handleMessage(Message msg) {

                super.handleMessage(msg);

                takePhoto();

            }

        };


        builder.setPositiveButton("相机", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {


                if (!TextUtils.isEmpty(mLastPhothPath)) {

                    //上一张拍照的图片删除

                    mThread = new Thread(new Runnable() {

                        @Override

                        public void run() {


                            File file = new File(mLastPhothPath);

                            if (file != null) {

                                file.delete();

                            }

                            mHandler.sendEmptyMessage(1);


                        }

                    });

                    mThread.start();

                } else {


                    //请求拍照权限

                 /*   if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                        takePhoto();

                    } else {

                        ActivityCompat.requestPermissions(MyStoresActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION_CAMERA);

                    }*/

                }

            }

        });

        builder.setNegativeButton("相册", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {


                chooseAlbumPic();


            }

        });


        builder.create().show();


    }

    /**
     * 选择相册照片
     */

    private void chooseAlbumPic() {

        Intent i = new Intent(Intent.ACTION_GET_CONTENT);

        i.addCategory(Intent.CATEGORY_OPENABLE);

        i.setType("image/*");

        startActivityForResult(Intent.createChooser(i, "Image Chooser"), REQUEST_CODE_ALBUM);


    }

    /**
     * 拍照
     */

    private void takePhoto() {

        StringBuilder fileName = new StringBuilder();

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        fileName.append(UUID.randomUUID()).append("_upload.png");


        File tempFile = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Uri uri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileProvider", tempFile);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        } else {

            Uri uri = Uri.fromFile(tempFile);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        }
        mCurrentPhotoPath = tempFile.getAbsolutePath();
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }
}
