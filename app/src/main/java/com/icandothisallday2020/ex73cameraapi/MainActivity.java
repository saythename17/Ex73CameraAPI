package com.icandothisallday2020.ex73cameraapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.PixelCopy;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    MyCameraView cv;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cv=findViewById(R.id.cv);
        iv=findViewById(R.id.iv);

        //동적퍼미션 2개 적용
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int permissionResult=checkSelfPermission(Manifest.permission.CAMERA);
            //int permissionResult2=checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                String[] permissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions,17);
            }
        }
    }//onCreate

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 10:
                if(grantResults[0]==PackageManager.PERMISSION_DENIED || grantResults[1]==PackageManager.PERMISSION_DENIED)//둘중하나의 퍼미션이라도 DENIED 일 때
                    Toast.makeText(this, "카메라 사용 불가", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void clickBtn(View view) {
        cv.camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //이 카메라는 취득된 이미지정보를 byte 배열로 전달
                Bitmap  bitmap= BitmapFactory.decodeByteArray(data,0,data.length);
                iv.setImageBitmap(bitmap);
                iv.setRotation(90);
                
                //카메라 하드웨어를 직접 사용하면 캡쳐된 이미지가 저장되어 있지 않아 직접 저장해야함
                //외장메모리에 하는 것이 일반적
                File path= Environment.getExternalStorageDirectory();//퍼미션 필요
                //파일명을 날짜를 이용하여 저장
                SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmss");
                String fileName="IMG_"+sdf.format(new Date())+".jpg";
                //파일명과 경로 결합
                File file=new File(path,fileName);//파일과 경로 결합
                //파일 경로에 데이터 저장 (외부메모리 저장 예제 참고)
                try {
                    FileOutputStream fos=new FileOutputStream(file);
                    fos.write(data);
                    fos.flush();
                    fos.close();
                    Toast.makeText(MainActivity.this, "saved", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
