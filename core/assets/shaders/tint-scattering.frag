#ifdef GL_ES
precision mediump float;
#endif
varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float scatterIntensity;
uniform float tintIntensity;
uniform vec4 scatterColor;
uniform vec4 tintColor;

void main()
{
    vec4 pixel = v_color * texture2D(u_texture, v_texCoords);
    float t = dot(v_texCoords, vec2(0.0, 1.0));
    if (pixel.a == 0.0) {
        discard;
    }
    vec4 tinted = mix(pixel.rgba, pixel.rgba * tintColor, tintIntensity);
    gl_FragColor = mix(tinted, scatterColor, clamp((t * t * t) * (1.5 * scatterIntensity), 0.0, 1.0));
}