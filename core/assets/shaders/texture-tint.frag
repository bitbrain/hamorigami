#ifdef GL_ES
precision mediump float;
#endif
varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float intensity;
uniform vec4 color;

void main()
{
    vec4 pixel = v_color * texture2D(u_texture, v_texCoords);
    float t = dot(v_texCoords, vec2(0.0, 1.0));
    if (pixel.a == 0.0) {
        discard;
    }
    gl_FragColor = mix(pixel.rgba, color, clamp((t * t * t) * (1.5f * intensity), 0.0, 1.0));
}