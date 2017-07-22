package opengldemo.opengldemo.utils;

import android.opengl.Matrix;

/**
 * @desc ${TODD}
 */

public class MatrixState {

    private static float[] mProMatrix = new float[16]; //4x4 矩阵 投影用
    private static float[] mVMatrix = new float[16]; //摄像机位置朝向 9 参数矩阵
    private static float[] mMVPMatrix; //最终的总变换矩阵
    private static float[] currMatrix;
    private static int stackTop = -1;
    private static float[][] mStack = new float[10][16];

    public static float[] getCurrMatrix() {
        return currMatrix;
    }

    /**
     * 正交投影
     */
    public static void setProjectOrtho(float left, float right,
                                       float bottom, float top,
                                       float near, float far) {
        Matrix.orthoM(
                mProMatrix,//存储生成矩阵元素的float[]类型数组
                0,//填充其实偏移量
                left, right, // near面的left、right
                bottom, top,// near面的bottom、top
                near, far);// near面、far面与视点的距离
    }

    /**
     * 透视投影
     */
    public static void setProjectFrustum(float left, float right,
                                         float bottom, float top,
                                         float near, float far) {
        Matrix.frustumM(
                mProMatrix,//存储生成矩阵元素的float[]类型数组
                0,//填充其实偏移量
                left, right, // near面的left、right
                bottom, top,// near面的bottom、top
                near, far);// near面、far面与视点的距离
    }

    public static void setCamera(float eyeX, float eyeY, float eyeZ,
                                 float centerX, float centerY, float centerZ,
                                 float upX, float upY, float upZ) {
        Matrix.setLookAtM(
                mVMatrix,//存储生成矩阵元素的float[]类型数据
                0,//填充起始偏移量
                eyeX, eyeY, eyeZ,// 摄像机的X、Y、Z坐标
                centerX, centerY, centerZ,// 观察目标点X、Y、Z坐标
                upX, upY, upZ);// up向量在X、Y、Z轴上的分量
    }


    public static float[] getFinalMatrix() {// 产生最终变换矩阵
        mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, currMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }

    /**
     * 产生无任何变化的初始矩阵
     */
    public static void setInitStack() {
        currMatrix = new float[16];
        Matrix.setRotateM(currMatrix, 0, 0, 1, 0, 0);
    }

    /**
     * 将当前变换矩阵存入栈中
     */
    public static void pushMatrix() {
        stackTop++;
        for (int i = 0; i < 16; i++) {
            mStack[stackTop][i] = currMatrix[i];
        }
    }

    /**
     * 从栈顶取出变换矩阵
     */
    public static void popMatrix() {
        for (int i = 0; i < 16; i++) {
            currMatrix[i] = mStack[stackTop][i];
        }
        stackTop--;
    }

    /**
     * 变换矩阵
     */
    public static void translate(float x, float y, float z) {
        Matrix.translateM(currMatrix, 0, x, y, z);
    }

    /**
     * 变换矩阵
     */
    public static void rotate(float angle, float x, float y, float z) {
        Matrix.rotateM(currMatrix, 0, angle, x, y, z);
    }

    public static void scale(float x, float y, float z) {
        Matrix.scaleM(currMatrix, 0, x, y, z);
    }
}
