package cqf.hn.demo;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cqf.hn.demo.utils.MatrixState;

/**
 * @desc ${TODD}
 */
public class SceneRenderer implements GLSurfaceView.Renderer {

    private GLView glView;
    private float ratio;
    private OnSurfaceCreatedListener listener;

    public void setGlView(GLView glView) {
        this.glView = glView;
    }

    public SceneRenderer() {
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        setBackgroundColor(1, 1, 1, 1);
        if (listener!=null){
            glView = listener.OnSurfaceCreated();
        }
        GLES30.glEnable(GLES30.GL_DEPTH_TEST); // 打开深度检测
        GLES30.glEnable(GLES30.GL_CULL_FACE);// 打开背面裁剪
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        ratio = width * 1.0f / height;
        //初始化变换矩阵
        setProject(true, -ratio * 0.4f, ratio * 0.4f, -1 * 0.4f, 1 * 0.4f, 1, 50);
        setCamera(
                0, 0, 6,
                0, 0, 0,
                0, 1, 0);
        MatrixState.setInitStack();
    }

    public void setCamera(float eyeX, float eyeY, float eyeZ,
                          float centerX, float centerY, float centerZ,
                          float upX, float upY, float upZ) {
        MatrixState.setCamera(
                eyeX, eyeY, eyeZ,
                centerX, centerY, centerZ,
                upX, upY, upZ);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);//清除颜色缓冲和深度缓存
        if (glView != null) {
            glView.drawSelf();
        }
    }

    public void setBackgroundColor(int red, int green, int blue, int alpha) {
        GLES30.glClearColor(red, green, blue, alpha);
    }

    public void setProject(boolean isFrustum, float left, float right, float bottom, float top, float near, float far) {
        if (isFrustum) {
            MatrixState.setProjectFrustum(left, right, bottom, top, near, far);
        } else {
            MatrixState.setProjectOrtho(left, right, bottom, top, near, far);
        }
    }

    public void glViewport(int startX, int startY, int width, int height) {
        GLES30.glViewport(startX, startY, width, height);
    }

    public void setListener(OnSurfaceCreatedListener listener) {
        this.listener = listener;
    }

    public interface OnSurfaceCreatedListener {
        GLView OnSurfaceCreated();
    }
}
