package cqf.hn.demo.image;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import cqf.hn.demo.widget.MyGLSurfaceView4;
import cqf.hn.demo.con.Constant;
import cqf.hn.demo.utils.MatrixState;
import cqf.hn.demo.utils.ShaderUtil;

/**
 * @desc ${TODD}
 */

public class Belt {
    private int vCount;//顶点数量
    private FloatBuffer mPositionBuffer;
    private FloatBuffer mColorBuffer;
    private String mVertexStr;
    private String mFragStr;
    private int program;
    private int maPositionHandle;
    private int maColorHandle;
    private int muMVPMatrixHandle;

    public Belt(MyGLSurfaceView4 view4) {
        initVertexData();
        initShader(view4);
    }

    private void initShader(MyGLSurfaceView4 view4) {
        //获取顶点着色器String
        mVertexStr = ShaderUtil.loadFromAssetsFile("vertex.sh", view4.getResources());
        //换取片元着色器String
        mFragStr = ShaderUtil.loadFromAssetsFile("frag.sh", view4.getResources());
        //加载程序
        program = ShaderUtil.createProgram(mVertexStr, mFragStr);
        //获取顶点着色器位置索引id
        maPositionHandle = GLES30.glGetAttribLocation(program, "aPosition");
        //获取顶点着色器颜色索引id
        maColorHandle = GLES30.glGetAttribLocation(program, "aColor");
        //获取顶点着色器总变换索引id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(program, "uMVPMatrix");
    }

    private void initVertexData() {
        int n = 1000;//被分割的成的四边形数量
        vCount = 2 * (n + 1);
        //确定顶点位置
        float beginAngle = 0;
        float endAngle = 180.0f;
        float angleSpan = (endAngle - beginAngle) / n;
        float[] vertices = new float[vCount * 3];
        int count = 0;
        for (float angle = beginAngle; angle <= endAngle - beginAngle; angle += angleSpan) {
            double radians = Math.toRadians(angle);
            vertices[count++] = (float) (0.6f * Constant.UNIT_SIZE * Math.cos(radians));
            vertices[count++] = (float) (0.6f * Constant.UNIT_SIZE * Math.sin(radians));
            vertices[count++] = 0;
            vertices[count++] = (float) (Constant.UNIT_SIZE * Math.cos(radians));
            vertices[count++] = (float) (Constant.UNIT_SIZE * Math.sin(radians));
            vertices[count++] = 0;
        }
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mPositionBuffer = vbb.asFloatBuffer();
        mPositionBuffer.put(vertices);
        mPositionBuffer.position(0);
        //设置顶点颜色值
        float[] color = new float[vCount * 4];
        int r;
        int g;
        int b;
        int a;
        for (int i = 0; i < vCount; i++) {
            if (i % 2 == 0) {//白色
                r = 1;
                g = 1;
                b = 1;
                a = 1;
            } else {//黄色
                r = 1;
                g = 1;
                b = 0;
                a = 1;
            }
            color[i * 4] = r;
            color[i * 4 + 1] = g;
            color[i * 4 + 2] = b;
            color[i * 4 + 3] = a;
        }
        ByteBuffer cbb = ByteBuffer.allocateDirect(color.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asFloatBuffer();
        mColorBuffer.put(color);
        mColorBuffer.position(0);
    }

    public void drawSelf(){
        GLES30.glUseProgram(program);//使用指定的着色器程序进行渲染
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);//将变换矩阵传入渲染管线
        //将顶点位置数据（浮点型数据）传送进渲染管线
        GLES30.glVertexAttribPointer(
                maPositionHandle,// 顶点位置属性索引
                3, // 每顶点一组的数据个数（这里是X、Y、Z坐标，因此为3）
                GLES30.GL_FLOAT,//数据类型：GL_BYTE、GL_UNSIGNED_BYTE、GL_SHORT、GL_UNSIGNED_SHORT、GL_INT、GL_UNSIGNED_INT 、GL_FLOAT 和 GL_FIXED
                false,// 非浮点类型数据转化为浮点数据时，是否需要规范化
                3 * 4,// 每组数据的尺寸，这里每组3个浮点数值（X、Y、Z坐标），每个浮点数4个字节，公3*4=12个字节
                mPositionBuffer// 存放数据的缓冲
        );
        GLES30.glVertexAttribPointer(maColorHandle, 4, GLES30.GL_FLOAT, false, 4 * 4, mColorBuffer);//将顶点颜色数据传送进渲染管线
        GLES30.glEnableVertexAttribArray(maPositionHandle);//启用顶点位置数据
        GLES30.glEnableVertexAttribArray(maColorHandle);//启用顶点着色数据
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, vCount);//执行绘制
    }
}
