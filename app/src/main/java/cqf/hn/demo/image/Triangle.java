package cqf.hn.demo.image;

import android.opengl.GLES30;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import cqf.hn.demo.widget.MyGLSurfaceView;
import cqf.hn.demo.utils.ShaderUtil;

/**
 * @desc 创建三角形的类
 * 初始化顶点数据
 * 初始化着色器
 * 设置相应的平移矩阵及旋转矩阵
 */

public class Triangle {
    public static float[] mProMatrix = new float[16];// 4*4投影矩阵
    public static float[] mVMatrix = new float[16];// 摄像机位置朝向的参数矩阵
    public static float[] mMVPMatrix;// 总变换矩阵

    private int mProgram;//自定义渲染管线着色器程序id
    private int muMVPMatrixHandle;//总变换矩阵引用
    private int maPositionHandle;//顶点位置属性引用
    private int maColorHandle;//顶点颜色属性引用
    private String mVertexShader;//顶点着色器代码脚本
    private String mFragmentShader;//片元着色器代码脚本

    private static float[] mMMatrix = new float[16]; // 具体物体的3D变换矩阵
    private FloatBuffer mVertexBuffer;//顶点坐标数据缓冲
    private FloatBuffer mColorBuffer;//顶点着色数据缓冲

    private int vCount;//顶点数量
    public float xAngle;//绕X轴旋转的角度

    public Triangle(MyGLSurfaceView glSurfaceView) {
        initVertexData();// 初始化顶点数据
        iniShader(glSurfaceView);// 初始化着色器
    }

    private void initVertexData() {
        vCount = 3;
        final float UNIT_SIZE = 0.2f;//设置单位长度
        float[] vertices = {
                -4 * UNIT_SIZE, 0, 0,
                0, -4 * UNIT_SIZE, 0,
                4 * UNIT_SIZE, 0, 0};//顶点坐标数组
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);// 开辟对应容量的缓冲（*4：1个浮点数4个字节）
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序为本地操作系统顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为浮点（Float）型缓冲
        mVertexBuffer.put(vertices);//在缓冲区内写入数据
        mVertexBuffer.position(0);//设置缓冲区起始位置
        float colors[] = new float[]{
                1, 1, 1,
                1, 0, 0,
                1, 0, 0,
                1, 0, 0};//顶点颜色数组
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asFloatBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);
    }

    public static float[] getFinalMatrix(float[] spec) {// 产生最终变换矩阵
        mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }

    private void iniShader(MyGLSurfaceView glSurfaceView) {
        // 加载顶点着色器的脚本内容
        mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.sh", glSurfaceView.getResources());
        // 加载片元着色器的脚本内容
        mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.sh", glSurfaceView.getResources());
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

    public void drawSelf() {//自定义绘制三角形的方法
        GLES30.glUseProgram(mProgram);//使用指定的着色器程序进行渲染
        Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);//初始化变换矩阵
        Matrix.translateM(mMMatrix, 0, 0, 0, 1);//设置沿Z轴正向平移
        Matrix.rotateM(mMMatrix, 0, xAngle, 1, 0, 0);//设置绕X轴旋转
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, Triangle.getFinalMatrix(mMMatrix), 0);//将变换矩阵传入渲染管线
        //将顶点位置数据（浮点型数据）传送进渲染管线
        GLES30.glVertexAttribPointer(
                maPositionHandle,// 顶点位置属性索引
                3, // 每顶点一组的数据个数（这里是X、Y、Z坐标，因此为3）
                GLES30.GL_FLOAT,//数据类型：GL_BYTE、GL_UNSIGNED_BYTE、GL_SHORT、GL_UNSIGNED_SHORT、GL_INT、GL_UNSIGNED_INT 、GL_FLOAT 和 GL_FIXED
                false,// 非浮点类型数据转化为浮点数据时，是否需要规范化
                3 * 4,// 每组数据的尺寸，这里每组3个浮点数值（X、Y、Z坐标），每个浮点数4个字节，公3*4=12个字节
                mVertexBuffer// 存放数据的缓冲
        );
        GLES30.glVertexAttribPointer(maColorHandle, 4, GLES30.GL_FLOAT, false, 4 * 4, mColorBuffer);//将顶点颜色数据传送进渲染管线
        GLES30.glEnableVertexAttribArray(maPositionHandle);//启用顶点位置数据
        GLES30.glEnableVertexAttribArray(maColorHandle);//启用顶点着色数据
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);//执行绘制
    }
}
