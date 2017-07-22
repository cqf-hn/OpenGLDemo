package opengldemo.opengldemo.image;

import android.content.Context;
import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import opengldemo.opengldemo.GLView;
import opengldemo.opengldemo.con.Constant;

/**
 * @desc ${TODD}
 */

public class CirCle2 extends GLView {


    private ByteBuffer mIndexBuffer;
    private int iCount;

    public CirCle2(Context view) {
        super(view);
    }

    @Override
    protected void initVertexData() {
        float beginAngle = 0f;
        float endAngle = 360.0f;
        int n = 100;//被分割园的数量
        vCount = n + 1;
        int count = 0;
        float[] vertices = new float[vCount * 3];
        vertices[count++] = 0;
        vertices[count++] = 0;
        vertices[count++] = 0;
        float angleSpan = (endAngle - beginAngle) / n;
        for (float angle = beginAngle; angle < endAngle - beginAngle; angle += angleSpan) {
            double radians = Math.toRadians(angle);
            vertices[count++] = (float) (Constant.UNIT_SIZE * Math.cos(radians));
            vertices[count++] = (float) (Constant.UNIT_SIZE * Math.sin(radians));
            vertices[count++] = 0;
        }
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mPositionBuffer = vbb.asFloatBuffer();
        mPositionBuffer.put(vertices);
        mPositionBuffer.position(0);
        float[] color = new float[vCount * 4];
        count = 0;
        color[count++] = 1;
        color[count++] = 1;
        color[count++] = 1;
        color[count++] = 1;
        for (int i = 0; i < vCount - 1; i++) {
            color[count++] = 0;
            color[count++] = 1;
            color[count++] = 1;
            color[count++] = 1;
        }
        ByteBuffer cbb = ByteBuffer.allocateDirect(color.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asFloatBuffer();
        mColorBuffer.put(color);
        mColorBuffer.position(0);
        iCount = n * 3;
        byte[] index = new byte[iCount];
        count = 0;
        for (int i = 0; i < n; i++) {
            index[count++] = 0;
            index[count++] = (byte) (i + 1);
            if (i == n - 1) {
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
//        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, vCount);//执行绘制
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, iCount , GLES30.GL_UNSIGNED_BYTE, mIndexBuffer);
    }
}
