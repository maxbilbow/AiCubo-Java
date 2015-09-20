//
//  Shader.vsh
//  AiGlubo
//
//  Created by Max Bilbow on 29/08/2015.
//  Copyright © 2015 Rattle Media Ltd. All rights reserved.
//

attribute vec4 position;
attribute vec3 normal;

varying vec4 colorVarying;
varying vec4 aPosition;

uniform vec4 colorVector;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;
//uniform mat4 viewProjectionMatrix;
//uniform mat3 normalMatrix;
uniform vec3 scaleVector;


mat4 modelViewProjectionMatrix(mat4 viewProjectionMatrix)
{
    return viewProjectionMatrix * modelMatrix;
}

void main()
{
    mat4 viewProjectionMatrix = viewMatrix * projectionMatrix;
    
    vec3 eyeNormal = normal;//normalize(normalMatrix * normal);
    vec3 lightPosition = vec3(0.5, 0.5, 1.0);
    vec4 diffuseColor = colorVector;
    
    float nDotVP = max(0.0, dot(eyeNormal, normalize(lightPosition)));
    
    colorVarying = diffuseColor * nDotVP;
    aPosition = modelMatrix * position;
    

    
    gl_Position = modelViewProjectionMatrix(viewProjectionMatrix) * position;
}
