package opengldemo.opengldemo.widget;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import opengldemo.opengldemo.image.CirCle2;
import opengldemo.opengldemo.utils.MatrixState;

/**
 * @desc ${TODD}
 */

public class MyGLSurfaceView5 extends GLSurfaceView {

    private SceneRenderer renderer;
    private CirCle2 circle;

    public MyGLSurfaceView5(Context context) {
        this(context, null);
    }

    public MyGLSurfaceView5(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //设置版本
        setEGLContextClientVersion(3);
        // 实例化Renderer
        renderer = new SceneRenderer();
        // 设置Renderer
        setRenderer(renderer);
        // 设置Renderer模式
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private class SceneRenderer implements Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //设置屏幕背景色RGBA
            GLES30.glClearColor(1, 1, 1, 1);
            //实例化图形对象
            //circle = new CirCle2(MyGLSurfaceView5.this);
            // 打开深度检测
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //设置视口
            GLES30.glViewport(0, 0, width, height);
            //设置比例
            float ratio = width * 1.0f / height;
            //设置正交投影还是透视投影
            MatrixState.setProjectFrustum(-ratio * 0.4f, ratio * 0.4f, -1 * 0.4f, 1 * 0.4f, 1, 50);// 设置正交投影
            //设置摄像机位置
            MatrixState.setCamera(0, 0, 6, 0, 0, 0, 0, 1, 0);
            //初始化变换矩阵
            MatrixState.setInitStack();
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            // 清楚深度缓冲
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            // 绘制
            circle.drawSelf();
        }
    }
}
