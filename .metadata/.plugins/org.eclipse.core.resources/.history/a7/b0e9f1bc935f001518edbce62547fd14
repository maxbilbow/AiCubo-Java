//
//  Shader.vsh
//  AiGlubo
//
//  Created by Max Bilbow on 29/08/2015.
//  Copyright © 2015 Rattle Media Ltd. All rights reserved.
//

attribute vec4 position;
attribute vec3 normal;

varying lowp vec4 colorVarying;
varying lowp vec4 aPosition;

uniform vec4 colorVector;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform mat4 viewProjectionMatrix;
uniform mat3 normalMatrix;
uniform vec3 scaleVector;


mat4 modelViewProjectionMatrix()
{
    return viewProjectionMatrix * modelMatrix;
}

void main()
{
    vec3 eyeNormal = normalize(normalMatrix * normal);
    vec3 lightPosition = vec3(0.5, 0.5, 1.0);
    vec4 diffuseColor = colorVector; //vec4(0.4, 0.4, 1.0, 1.0);//vec4(sin(position.x), sin(position.y),sin(position.z), 1.0);//
    
    float nDotVP = max(0.0, dot(eyeNormal, normalize(lightPosition)));
    
    colorVarying = diffuseColor * nDotVP;
    aPosition = modelMatrix * position;
    gl_Position = modelViewProjectionMatrix() * position;
}
