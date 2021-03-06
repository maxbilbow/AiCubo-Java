package click.rmx.engine.math;

/*
 * The MIT License (MIT)
 *
 * Copyright © 2015, Heiko Brumme
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


import java.nio.FloatBuffer;

import javax.vecmath.Vector2f;

import org.lwjgl.BufferUtils;

/**
 * This class represents a (x,y)-Vector. GLSL equivalent to vec2.
 *
 * @author Heiko Brumme
 */
public class Vector2 extends Vector2f {


    /**
	 * 
	 */
	private static final long serialVersionUID = 6601387796955602745L;

	/**
     * Creates a default 2-tuple vector with all values set to 0.
     */
    public Vector2() {
    	super();
    }

    /**
     * Creates a 2-tuple vector with specified values.
     *
     * @param x x value
     * @param y y value
     */
    public Vector2(float x, float y) {
        super(x,y);
    }

    /**
     * Calculates the squared length of the vector.
     *
     * @return Squared length of this vector
     */
    public float getLengthSquared() {
        return x * x + y * y;
    }

    /**
     * Calculates the length of the vector.
     *
     * @return Length of this vector
     */
    public float getLength() {
        return (float) Math.sqrt(lengthSquared());
    }

    /**
     * Normalizes the vector.
     *
     * @return Normalized vector
     */
    public Vector2 getNormalized() {
        float length = length();
        return divide(length);
    }

    /**
     * Adds this vector to another vector.
     *
     * @param other The other vector
     * @return Sum of this + other
     */
    public Vector2 getSum(Vector2f other) {
        float x = this.x + other.x;
        float y = this.y + other.y;
        return new Vector2(x, y);
    }

    /**
     * Negates this vector.
     *
     * @return Negated vector
     */
    public Vector2 getNegated() {
        return getScaled(-1f);
    }

    /**
     * Subtracts this vector from another vector.
     *
     * @param other The other vector
     * @return Difference of this - other
     */
    public Vector2 getSubtraction(Vector2 other) {
        return getSum(other.getNegated());
    }

    /**
     * Multiplies a vector by a scalar.
     *
     * @param scalar Scalar to multiply
     * @return Scalar product of this * scalar
     */
    public Vector2 getScaled(float scalar) {
        float x = this.x * scalar;
        float y = this.y * scalar;
        return new Vector2(x, y);
    }

    /**
     * Divides a vector by a scalar.
     *
     * @param scalar Scalar to multiply
     * @return Scalar quotient of this / scalar
     */
    public Vector2 divide(float scalar) {
        return getScaled(1f / scalar);
    }

    /**
     * Calculates the dot product of this vector with another vector.
     *
     * @param other The other vector
     * @return Dot product of this * other
     */
    public float dot(Vector2 other) {
        return this.x * other.x + this.y * other.y;
    }

    /**
     * Calculates a linear interpolation between this vector with another
     * vector.
     *
     * @param other The other vector
     * @param alpha The alpha value, must be between 0.0 and 1.0
     * @return Linear interpolated vector
     */
    public Vector2 lerp(Vector2 other, float alpha) {
        return this.getScaled(1f - alpha).getSum(other.getScaled(alpha));
    }

    /**
     * Returns the Buffer representation of this vector.
     *
     * @return Vector as FloatBuffer
     */
    public FloatBuffer getBuffer() {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(2);
        buffer.put(x).put(y);
        buffer.flip();
        return buffer;
    }
}
