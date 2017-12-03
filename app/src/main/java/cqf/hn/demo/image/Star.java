package cqf.hn.demo.image;

import android.opengl.GLES30;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import cqf.hn.demo.utils.MatrixState;
import cqf.hn.demo.utils.ShaderUtil;
import cqf.hn.demo.widget.MyGLSurfaceView1;

public class Star {

    public float xAngle;
    public float yAngle;
    public static final float UNIT_SIZE = 1;
    private String mVertexShader;
    private String mFragmentShader;
    private int mProgram;
    private int maPositionHandle;
    private int maColorHandle;
    private int muMVPMatrixHandle;
    private int vCount;
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mColorBuffer;
    private static float[] mMMatrix = new float[16];

    public Star(MyGLSurfaceView1 sv, float r, float R, float z) {
        MatrixState.setInitStack();
        initVertexData(R, r, z);
        initShader(sv);
    }

    private void initShader(MyGLSurfaceView1 sv) {
        // 加载顶点着色器的脚本内容
        mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.sh", sv.getResources());
        // 加载片元着色器的脚本内容
        mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.sh", sv.getResources());
        // 基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        if (mProgram == 0) {
            throw new RuntimeException("error loadShader");
        }
        // 获取程序中顶点位置属性引用 id
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        // 获取程序中顶点颜色属性引用 id
        maColorHandle = GLES30.glGetAttribLocation(mProgram, "aColor");
        // 获取程序中总变换矩阵引用 id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    private void initVertexData(float R, float r, float z) {
        ArrayList<Float> flist = new ArrayList<>();
        float tempAngle = 360.0f / 6;
        for (float angle = 0; angle < 360; angle += tempAngle) {//循环生成构成六角形中各三角形的顶点坐标
            // 第一个点的x、y、z坐标
            flist.add(0f);
            flist.add(0f);
            flist.add(z);
            // 第二个点的X、Y、Z坐标
            flist.add((float) (R * UNIT_SIZE * Math.cos(Math.toRadians(angle))));
            flist.add((float) (R * UNIT_SIZE * Math.sin(Math.toRadians(angle))));
            flist.add(z);
            // 第三个点的X、Y、Z坐标
            flist.add((float) (r * UNIT_SIZE * Math.cos(Math.toRadians(angle + tempAngle / 2))));
            flist.add((float) (r * UNIT_SIZE * Math.sin(Math.toRadians(angle + tempAngle / 2))));
            flist.add(z);
            // 第一个中心点的X、Y、Z坐标
            flist.add(0f);
            flist.add(0f);
            flist.add(z);
            // 第二个中心点的X、Y、Z坐标
            flist.add((float) (r * UNIT_SIZE * Math.cos(Math.toRadians(angle + tempAngle / 2))));
            flist.add((float) (r * UNIT_SIZE * Math.sin(Math.toRadians(angle + tempAngle / 2))));
            flist.add(z);
            // 第三个中心点的X、Y、Z坐标
            flist.add((float) (R * UNIT_SIZE * Math.cos(Math.toRadians(angle + tempAngle))));
            flist.add((float) (R * UNIT_SIZE * Math.sin(Math.toRadians(angle + tempAngle))));
            flist.add(z);
        }
        vCount = flist.size() / 3;
        float[] vertexArray = new float[flist.size()];// 顶点坐标数组
        for (int i = 0; i < vCount; i++) {
            vertexArray[i * 3] = flist.get(i * 3);
            vertexArray[i * 3 + 1] = flist.get(i * 3 + 1);
            vertexArray[i * 3 + 2] = flist.get(i * 3 + 2);
        }
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertexArray.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertexArray);
        mVertexBuffer.position(0);
        float[] colorArray = new float[vCount * 4];//顶点着色数据的初始化
        for (int i = 0; i < vCount; i++) {
            if (i % 3 == 0) {// 中心点为白色，rgba4个通道[1,1,1,0]
                colorArray[i * 4] = 1;
                colorArray[i * 4 + 1] = 1;
                colorArray[i * 4 + 2] = 1;
                colorArray[i * 4 + 3] = 0;
            } else {//边上的点为淡蓝色，RGBA4个通道[0.45，0.75，0.75，0]
                colorArray[i * 4] = 0.45f;
                colorArray[i * 4 + 1] = 0.75f;
                colorArray[i * 4 + 2] = 0.75f;
                colorArray[i * 4 + 3] = 0;
            }
        }
        ByteBuffer cbb = ByteBuffer.allocateDirect(colorArray.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asFloatBuffer();
        mColorBuffer.put(colorArray);
        mColorBuffer.position(0);
    }

    public void drawSelf() {
        GLES30.glUseProgram(mProgram);
        Matrix.setRotateM(MatrixState.getCurrMatrix(), 0, 0, 0, 1, 0);// 初始化变换矩阵
        Matrix.translateM(MatrixState.getCurrMatrix(), 0, 0, 0, 1);// 设置沿Z轴正向位移1
        Matrix.rotateM(MatrixState.getCurrMatrix(), 0, yAngle, 0, 1, 0);// 设置沿Y轴旋转yAngle度
        Matrix.rotateM(MatrixState.getCurrMatrix(), 0, xAngle, 1, 0, 0);// 设置沿X轴旋转xAngle度
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        GLES30.glVertexAttribPointer(maPositionHandle, 3, GLES30.GL_FLOAT, false, 3 * 4, mVertexBuffer);
        GLES30.glVertexAttribPointer(maColorHandle, 3, GLES30.GL_FLOAT, false, 4 * 4, mColorBuffer);
        GLES30.glEnableVertexAttribArray(maPositionHandle);
        GLES30.glEnableVertexAttribArray(maColorHandle);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);
    }
}