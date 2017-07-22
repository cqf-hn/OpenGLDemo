package opengldemo.opengldemo.widget;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.AttributeSet;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import opengldemo.opengldemo.image.Triangle;

/**
 * @desc ${TODD}
 */

public class MyGLSurfaceView extends GLSurfaceView {
    public static final float ANGLE_SPAN = 0.375f;//每次三角形旋转的角度
    private SceneRenderer mRenderer;
    private boolean isCircle = true;
    private Runnable runnable;

    public MyGLSurfaceView(Context context) {
        this(context, null);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(3);//使用OpenGL ES 3.0需设置该值为3
        runnable = new Runnable() {
            @Override
            public void run() {
                while (isCircle) {
                    if (mRenderer != null) {
                        mRenderer.triangle.xAngle = mRenderer.triangle.xAngle + ANGLE_SPAN;
                        try {
                            Thread.sleep(20); //线程休眠 20ms
                        } catch (Exception e) { //捕获并打印异常信息
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        mRenderer = new SceneRenderer();//创建SceneRenderer实例
        setRenderer(mRenderer);//设置渲染器
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染器模式->连续不断
    }

    public boolean isCircle() {
        return isCircle;
    }

    public void setCircle(boolean circle) {
        isCircle = circle;
    }

    private class SceneRenderer implements Renderer {

        Triangle triangle;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES30.glClearColor(0, 0, 0, 1);//设置屏幕背景色
            triangle = new Triangle(MyGLSurfaceView.this);
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);//打开深度检测
            post(runnable);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //设置视口
            GLES30.glViewport(
                    0,// 为视口矩形左下侧点在视口用屏幕坐标系内的X坐标(原点位于GLSurfaceView的左下角，X轴向右，Y轴向上)
                    0,// 为视口矩形左下侧点在视口用屏幕坐标系内的Y坐标（原点位于GLSurfaceView的左下角，X轴向右，Y轴向上）
                    width,// 视口的宽度
                    height);// 视口的高度
            float ratio = width * 1.0f / height;//计算屏幕的宽度和高度比例
            Matrix.frustumM(Triangle.mProMatrix, 0,
                    -ratio, ratio,
                    -1, 1,
                    1, 10);
            /**
             * 正交投影
             * Matrix.orthoM(
             *      Triangle.mProMatrix,//存储生成矩阵元素的float[]类型数组
             *      0,//填充其实偏移量
             *      -ratio, ratio, // near面的left、right
             *      -1, 1,// near面的bottom、top
             *      1, 10);// near面、far面与视点的距离
             */
            //设置摄像机
            Matrix.setLookAtM(
                    Triangle.mVMatrix,//存储生成矩阵元素的float[]类型数据
                    0,//填充起始偏移量
                    0, 0, 3,// 摄像机的X、Y、Z坐标
                    0, 0, 0,// 观察目标点X、Y、Z坐标
                    1, 1, 0);// up向量在X、Y、Z轴上的分量
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);//清楚颜色缓冲和深度缓存
            triangle.drawSelf();
        }


    }
}
