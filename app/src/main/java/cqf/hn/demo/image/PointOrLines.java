package cqf.hn.demo.image;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import cqf.hn.demo.utils.MatrixState;
import cqf.hn.demo.widget.MyGLSurfaceView3;
import cqf.hn.demo.con.Constant;
import cqf.hn.demo.utils.ShaderUtil;

/**
 * @desc ${TODD}
 */

public class PointOrLines {
    private int vCount;
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mColorBuffer;
    private String mVertexShader;
    private String mFragShader;
    private int program;
    private int maPositionHandle;
    private int maColorHandle;
    private int mMVPMatrixHandle;

    public PointOrLines(MyGLSurfaceView3 view3) {
        initVertexData();
        initShader(view3);
    }

    private void initShader(MyGLSurfaceView3 view3) {
        //初始化shader
        //加载顶点着色器的脚本内容
        mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.sh", view3.getResources());
        //加载片元着色器的脚本内容
        mFragShader = ShaderUtil.loadFromAssetsFile("frag.sh", view3.getResources());
        //基于顶点着色器与片元着色器创建程序
        program = ShaderUtil.createProgram(mVertexShader, mFragShader);
        //获取程序中顶点位置属性引用id
        maPositionHandle = GLES30.glGetAttribLocation(program, "aPosition");
        //获取程序中顶点颜色属性引用id
        maColorHandle = GLES30.glGetAttribLocation(program, "aColor");
        //获取程序中总变换矩阵引用id
        mMVPMatrixHandle = GLES30.glGetUniformLocation(program, "uMVPMatrix");
    }

    private void initVertexData() {
        vCount = 5;
        float[] vertices = {0, 0, 0, Constant.UNIT_SIZE, Constant.UNIT_SIZE, 0,
                -Constant.UNIT_SIZE, Constant.UNIT_SIZE, 0,
                -Constant.UNIT_SIZE, -Constant.UNIT_SIZE, 0,
                Constant.UNIT_SIZE, -Constant.UNIT_SIZE, 0,};
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
        float[] colors = {
                1, 1, 0, 0,//黄色
                1, 1, 1, 0,//白色
                0, 1, 0, 0,//绿色
                1, 1, 1, 0,//白色
                1, 1, 0, 0//黄色
        };
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asFloatBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);
    }

    public void drawSelf(){
//指定使用某套着色器程序
        GLES30.glUseProgram(program);
        //将最终变换矩阵传入渲染管线
        GLES30.glUniformMatrix4fv(mMVPMatrixHandle, 1, false,
                MatrixState.getFinalMatrix(), 0);
        //将顶点位置数据送入渲染管线
        GLES30.glVertexAttribPointer(maPositionHandle, 3, GLES30.GL_FLOAT,
                false, 3 * 4, mVertexBuffer);
        //将顶点颜色数据送入渲染管线
        GLES30.glVertexAttribPointer(maColorHandle, 4, GLES30.GL_FLOAT, false,
                4 * 4, mColorBuffer);
        //启用顶点位置数据数组
        GLES30.glEnableVertexAttribArray(maPositionHandle);
        //启用顶点颜色数据数组
        GLES30.glEnableVertexAttribArray(maColorHandle);

        GLES30.glLineWidth(10);//设置线的宽度
        //绘制点或线
        switch (Constant.CURR_DRAW_MODE) {
            case Constant.GL_POINTS:// GL_POINTS方式
                GLES30.glDrawArrays(GLES30.GL_POINTS, 0, vCount);
                break;
            case Constant.GL_LINES:// GL_LINES方式
                GLES30.glDrawArrays(GLES30.GL_LINES, 0, vCount);
                break;
            case Constant.GL_LINE_STRIP:// GL_LINE_STRIP方式
                GLES30.glDrawArrays(GLES30.GL_LINE_STRIP, 0, vCount);
                break;
            case Constant.GL_LINE_LOOP:// GL_LINE_LOOP方式
                GLES30.glDrawArrays(GLES30.GL_LINE_LOOP, 0, vCount);
                break;
        }
    }
}
