package cqf.hn.demo;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cqf.hn.demo.widget.MyGLSurfaceView;
import cqf.hn.demo.widget.MyGLSurfaceView3;
import cqf.hn.demo.widget.MyGLSurfaceView6;
import cqf.hn.demo.image.Star2;
import cqf.hn.demo.widget.MyGLSurfaceView1;
import cqf.hn.demo.widget.MyGLSurfaceView2;
import cqf.hn.demo.widget.MyGLSurfaceView4;
import cqf.hn.demo.widget.MyGLSurfaceView5;
import cqf.hn.demo.widget.SubGLSurfaceView;

import static android.opengl.GLSurfaceView.RENDERMODE_CONTINUOUSLY;

public class MainActivity extends AppCompatActivity {

    private MyGLSurfaceView glSurfaceView;
    private MyGLSurfaceView1 view1;
    private MyGLSurfaceView2 view2;
    private MyGLSurfaceView3 view3;
    private MyGLSurfaceView4 view4;
    private MyGLSurfaceView5 view5;
    private MyGLSurfaceView6 view6;
    private SubGLSurfaceView view;
    private SceneRenderer renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        setContentView(R.layout.activity_main);
//        glSurfaceView = (MyGLSurfaceView) findViewById(R.id.myGLSurfaceView);
//        glSurfaceView.requestFocus();
//        glSurfaceView.setFocusableInTouchMode(true);
        //设置为竖屏模式
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        mview = new MyTDView(this); //创建 MyTDView 类的对象
//        mview.requestFocus(); //获取焦点
//        mview.setFocusableInTouchMode(true); //设置为可触控
//        setContentView(mview); //跳转到相关界面

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        view5 = new MyGLSurfaceView5(this);
//        view5.requestFocus();
//        view5.setFocusableInTouchMode(true);

        view = new SubGLSurfaceView(this);
        renderer = new SceneRenderer();
        renderer.setListener(new SceneRenderer.OnSurfaceCreatedListener() {
            @Override
            public GLView OnSurfaceCreated() {
                return new Star2(MainActivity.this);
            }
        });
        view.setRenderer(renderer);
        view.setRenderMode(RENDERMODE_CONTINUOUSLY);
        view.requestFocus();
        view.setFocusableInTouchMode(true);
        setContentView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        view5.onResume();
//        glSurfaceView.onResume();
        view.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        view5.onPause();
//        glSurfaceView.onPause();
        view.onPause();
    }
}
