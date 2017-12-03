package cqf.hn.demo.image;

import android.content.Context;
import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import cqf.hn.demo.GLView;
import cqf.hn.demo.con.Constant;

/**
 * @desc ${TODD}
 */

public class Star2 extends GLView {
    private int iCount;
    private ByteBuffer mIndexBuffer;

    public Star2(Context context) {
        super(context);
    }

    @Override
    protected void initVertexData() {
        int angleCount = 6;
        float R = 1f;
        float r = 0.5f;
        vCount = angleCount * 2 + 1;
        float[] vertices = new float[vCount * 3];
        int count = 0;
        float tempAngle = 360.0f / angleCount;
        for (float begin = 0; begin < 360; begin += tempAngle) {
            if (begin == 0) {
                vertices[count++] = 0;
                vertices[count++] = 0;
                vertices[count++] = 0;
            }
            vertices[count++] = (float) (R * Constant.UNIT_SIZE * Math.cos(Math.toRadians(begin)));
            vertices[count++] = (float) (R * Constant.UNIT_SIZE * Math.sin(Math.toRadians(begin)));
            vertices[count++] = 0;
            vertices[count++] = (float) (r * Constant.UNIT_SIZE * Math.cos(Math.toRadians(begin + tempAngle / 2)));
            vertices[count++] = (float) (r * Constant.UNIT_SIZE * Math.sin(Math.toRadians(begin + tempAngle / 2)));
            vertices[count++] = 0;
        }
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mPositionBuffer = vbb.asFloatBuffer();
        mPositionBuffer.put(vertices);
        mPositionBuffer.position(0);
//        float[] colors = new float[vCount * 4];
//        count = 0;
//        for (int i = 0; i < vCount; i++) {
//            if (i == 0) {
//                colors[count++] = 0;
//                colors[count++] = 0;
//                colors[count++] = 0;
//                colors[count++] = 0;
//            } else {
//                colors[count++] = 1;
//                colors[count++] = 1;
//                colors[count++] = 0;
//                colors[count++] = 0;
//            }
//        }
//
//        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
//        cbb.order(ByteOrder.nativeOrder());
//        mColorBuffer = cbb.asFloatBuffer();
//        mColorBuffer.put(colors);
//        mColorBuffer.position(0);

        iCount = angleCount * 2 * 3;
        byte[] index = new byte[iCount];
        count = 0;
        for (int i = 0; i < angleCount * 2; i++) {
            index[count++] = 0;
            index[count++] = (byte) (i+1);
            if (i == angleCount * 2 - 1) {
                index[count++] = 1;
            } else {
                index[count++] = (byte) (i + 2);
            }
        }
        mIndexBuffer = ByteBuffer.allocateDirect(index.length);
        mIndexBuffer.put(index);
        mIndexBuffer.position(0);
    }

    @Override
    protected void draw() {
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, iCount , GLES30.GL_UNSIGNED_BYTE, mIndexBuffer);
    }
}
