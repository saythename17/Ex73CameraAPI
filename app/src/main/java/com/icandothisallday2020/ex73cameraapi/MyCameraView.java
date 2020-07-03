package com.icandothisallday2020.ex73cameraapi;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class MyCameraView extends SurfaceView implements SurfaceHolder.Callback {
    //SurfaceView 가 보여줄 뷰들을 관리하는 관리자 객체
    SurfaceHolder holder;//고속으로 뷰를 그려내는 객체-SurfaceView(고속버퍼뷰) 에 미리보기를 빠르게 그려냄
    Camera camera;//hardware
    public MyCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);

        holder=getHolder();
        holder.addCallback(this);
    }

    @Override//이 뷰가 Activity 에 보여질 때 발동
    public void surfaceCreated(SurfaceHolder holder) {
        //카메라 객체 셔터 열기(셔터를 열면 객체가 리턴됨)
        camera=Camera.open(0);//[0:back, 1:front]
        //미리보기 설정
        try {
            camera.setPreviewDisplay(holder);
            //카메라는 무조건 가로방향
            //->프리뷰를 90도 회전
            camera.setDisplayOrientation(90);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override//surfaceCreated() 실행 후에 뷰의 사이즈가 결정되었을 때 발동
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //카메라 미리보기 시작
        camera.startPreview();
    }

    @Override//이 뷰가 화면에 보여지지 않을 때 발동
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();//미리보기 종료
        camera.release();//카메라 셔터 닫기
        camera=null;//카메라 없애기

    }
}
