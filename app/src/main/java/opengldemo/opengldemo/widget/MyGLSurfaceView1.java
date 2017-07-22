package opengldemo.opengldemo.widget;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import opengldemo.opengldemo.image.Star;
import opengldemo.opengldemo.utils.MatrixState;

/**
 * @desc ${TODD}
 */

public class MyGLSurfaceView1 extends GLSurfaceView {

    public static final float TOUCH_SCALE_FACTOR = 180.0f / 320;//角度缩放比例
    private SceneRenderer renderer;//场景渲染器
    private float mPreY;//上次的触控位置Y坐标
    private float mPreX;//上次的触控位置X坐标
    private int starsCount = 6;


    public MyGLSurfaceView1(Context context) {
        this(context, null);
    }

    public MyGLSurfaceView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setEGLContextClientVersion(3);
        renderer = new SceneRenderer();
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPreY = event.getY();
                mPreX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                float x = event.getX();
                float dy = y - mPreY;
                float dx = x - mPreX;
                // 设置各个六角星绕X轴、Y轴旋转的角度
                for (int i = 0; i < renderer.getStars().size(); i++) {
                    renderer.getStars().get(i).yAngle += dx * TOUCH_SCALE_FACTOR;
                    renderer.getStars().get(i).xAngle += dy * TOUCH_SCALE_FACTOR;
                }
                mPreY = y;
                mPreX = x;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                break;
        }

        return true;
    }

    private class SceneRenderer implements Renderer {

        private ArrayList<Star> stars;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            stars = new ArrayList<>();
            GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1);//设置屏幕背景色
            for (int i = 0; i < starsCount; i++) {
                stars.add(new Star(MyGLSurfaceView1.this, 0.4f, 1f, -1f * i));
                GLES30.glEnable(GLES30.GL_DEPTH_TEST);// 打开深度检测
            }
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES30.glViewport(0, 0, width, height);
            float ratio = width * 1.0f / height;
            MatrixState.setProjectFrustum(-ratio * 0.4f, ratio * 0.4f, -1 * 0.4f, 1 * 0.4f, 1, 50);// 设置正交投影
            MatrixState.setCamera(
                    0f, 0f, 6f,
                    0f, 0f, 0f,
                    0f, 1f, 0f);//设置摄像机
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);//清楚深度缓冲与颜色缓冲
            for (int i = 0; i < stars.size(); i++) {
                stars.get(i).drawSelf();
            }
        }

        public ArrayList<Star> getStars() {
            return stars;
        }
    }
}
