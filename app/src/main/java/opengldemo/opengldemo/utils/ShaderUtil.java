package opengldemo.opengldemo.utils;

import android.content.res.Resources;
import android.opengl.GLES30;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @desc 将着色器（Shader）脚本加载进显卡并编译。
 */

public class ShaderUtil {// 加载顶点与片元着色器的类

    public static int loadShader(int shaderType, String source) {// 加载指定着色器的方法
        int shader = GLES30.glCreateShader(shaderType);// 创建一个shader,并记录id
        if (shader != 0) {// 创建成功，加载着色器
            GLES30.glShaderSource(shader, source);//加载着色器的源代码
            GLES30.glCompileShader(shader);// 编译着色器
            int[] compiled = new int[1];
            // 获取Shader的编译情况
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                LogUtils.e("Could not compile shader" + shaderType + ":");
                LogUtils.e(GLES30.glGetShaderInfoLog(shader));
                GLES30.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    /**
     * 创建着色器程序的方法
     */
    public static int createProgram(String vertexSource, String fragmentSource) {
        // 加载顶点着色器
        int vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0){// 加载失败
            return 0;
        }
        // 加载片元着色器
        int pixelShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentSource);
        if (pixelShader == 0){// 加载失败
            return 0;
        }
        int program = GLES30.glCreateProgram();// 创建程序
        if (program!=0){//创建成功，往程序中加入顶点着色器与片元着色器
            GLES30.glAttachShader(program,vertexShader);//加入顶点着色器
            checkGlError("glAttachShader");
            GLES30.glAttachShader(program,pixelShader);//加入片元着色器
            checkGlError("glAttachShader");
            GLES30.glLinkProgram(program);//连接程序
            int[] linkStatus = new int[1];//用以存放连接成功program状态值
            GLES30.glGetProgramiv(program,GLES30.GL_LINK_STATUS,linkStatus,0);
            if (linkStatus[0]!= GLES30.GL_TRUE){// 连接失败
                LogUtils.e("Could not link program:");
                LogUtils.e(GLES30.glGetProgramInfoLog(program));
                GLES30.glDeleteProgram(program);// 删除程序
                program = 0;
            }
        }
        return program;
    }

    /**
     * 检查每一步操作是否有错误的方法
     */
    public static void checkGlError(String op) {
        int error;
        while ((error = GLES30.glGetError()) != GLES30.GL_NO_ERROR) {
            LogUtils.e(op + ":glError" + error);
            throw new RuntimeException(op + ":glError" + error);
        }
    }

    /**
     * 从sh脚本中加载着色器内容的方法
     */
    public static String loadFromAssetsFile(String name, Resources res) {
        String result = null;
        InputStream in = null;
        ByteArrayOutputStream stream = null;
        try {
            int ch = 0;
            in = res.getAssets().open(name);
            stream = new ByteArrayOutputStream();
            while ((ch = in.read()) != -1) {
                stream.write(ch);
            }
            byte[] buff = stream.toByteArray();
            result = new String(buff, "UTF-8");
            result = result.replaceAll("\\r\\n", "\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }
}
