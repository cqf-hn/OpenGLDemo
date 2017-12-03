package cqf.hn.demo.widget;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * @desc ${TODD}
 */

public class SubGLSurfaceView extends GLSurfaceView {
    public SubGLSurfaceView(Context context) {
        this(context, null);
    }

    public SubGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(3);
    }
}
