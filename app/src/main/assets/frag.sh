#version 300 es
precision mediump float;//设置浮点精度
in vec4 vColor;//接受从顶点着色器过来的参数
out vec4 fragColor;//输出的片元颜色
void main(){
    fragColor = vColor;//给此片元赋颜色值
}