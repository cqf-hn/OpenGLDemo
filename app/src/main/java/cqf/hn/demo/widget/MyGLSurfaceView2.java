package cqf.hn.demo.widget;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cqf.hn.demo.image.Cube;
import cqf.hn.demo.utils.MatrixState;
import cqf.hn.demo.con.Constant;

/**
 * @desc ${TODD}
 */

public class MyGLSurfaceView2 extends GLSurfaceView {

    private SceneRenderer renderer;
    private Cube cube;

    public MyGLSurfaceView2(Context context) {
        this(context, null);
    }

    public MyGLSurfaceView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setEGLContextClientVersion(3);
        renderer = new SceneRenderer();
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private class SceneRenderer implements Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1f);//设置屏幕背景色RGBA
            cube = new Cube(MyGLSurfaceView2.this);
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);// 打开深度检测
            GLES30.glEnable(GLES30.GL_CULL_FACE);// 打开背面裁剪
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES30.glViewport(0, 0, width, height);//设置视口
            Constant.ratio = width * 1.0f / height;
            MatrixState.setProjectFrustum(-Constant.ratio * 0.8f,
                    Constant.ratio * 1.2f, -1, 1, 20, 100);
            MatrixState.setCamera(-16f, 8f, 98, 0, 0, 0, 0, 1, 0);
            MatrixState.setInitStack();//初始化变换矩阵
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);// 清楚深度缓冲
            // 绘制原立方体
            MatrixState.pushMatrix();// 保护现场
            cube.drawSelf();
            MatrixState.popMatrix();// 恢复现场
            MatrixState.pushMatrix();// 保护现场
            // 绘制变换后的立方体
            MatrixState.translate(3.5f, 0, 0);// 沿X轴方向平移3.5f
            MatrixState.rotate(30, 1, 0, 1);// 沿Z轴旋转30°
            //MatrixState.scale(0.4f, 2f, 0.6f);// 沿Z轴旋转30°

            cube.drawSelf();
            MatrixState.popMatrix();


        }
    }
}
