//
//  Shader.fsh
//  AiGlubo
//
//  Created by Max Bilbow on 29/08/2015.
//  Copyright © 2015 Rattle Media Ltd. All rights reserved.
//
//precision mediump float;
varying vec4 colorVarying;
varying vec4 aPosition;
//uniform float time;
//precision mediump float;
void main()
{
    
    gl_FragColor = colorVarying;
    //    vec4(
    //                        colorVarying.x,
    //                        colorVarying.y,// + diff,
    //                        colorVarying.z,
    //                        colorVarying.w
    //                        );
}
