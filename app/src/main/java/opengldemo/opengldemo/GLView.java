package opengldemo.opengldemo;

import android.content.Context;
import android.opengl.GLES30;

import java.nio.FloatBuffer;

import opengldemo.opengldemo.utils.MatrixState;
import opengldemo.opengldemo.utils.ShaderUtil;


/**
 * @desc ${TODD}
 */

public abstract class GLView {
    public int mProgram;
    private String mVertexStr;
    private String mFragStr;
    private int mauMVPMatrixHandle;
    private int maPositionHandle;
    private int maColorHandle;
    protected int vCount;
    protected FloatBuffer mPositionBuffer;
    protected FloatBuffer mColorBuffer;

    public GLView(Context view) {
        MatrixState.setInitStack();
        initVertexData();
        initShader(view);
    }

    protected void initShader(Context view) {
        mVertexStr = ShaderUtil.loadFromAssetsFile("vertex.sh", view.getResources());
        mFragStr = ShaderUtil.loadFromAssetsFile("frag.sh", view.getResources());
        mProgram = ShaderUtil.createProgram(mVertexStr, mFragStr);
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        maColorHandle = GLES30.glGetAttribLocation(mProgram, "aColor");
        mauMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    protected abstract void initVertexData();

    public void drawSelf() {
        //使用指定的着色器程序进行渲染
        GLES30.glUseProgram(mProgram);
        // 矩阵变换
        changeMatrix();
        //将变换矩阵传入渲染管线
        GLES30.glUniformMatrix4fv(mauMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        //将顶点位置数据（浮点型数据）传送进渲染管线
        GLES30.glVertexAttribPointer(maPositionHandle, 3, GLES30.GL_FLOAT, false, 3 * 4, mPositionBuffer);
        //将顶点颜色数据传送进渲染管线
        // GLES30.glVertexAttribPointer(maColorHandle, 4, GLES30.GL_FLOAT, false, 4 * 4, mColorBuffer);
        float[] colors = new float[]{1f, 1f, 0f};

        GLES30.glVertexAttrib4f(maColorHandle, colors[0], colors[1], colors[2], 1.0f);
        //启用顶点位置数据
        GLES30.glEnableVertexAttribArray(maPositionHandle);
        //启用顶点着色数据
       // GLES30.glEnableVertexAttribArray(maColorHandle);
        //执行绘制
        draw();
    }

    protected abstract void draw();

    protected void changeMatrix() {
        // default empty implement
    }
}
