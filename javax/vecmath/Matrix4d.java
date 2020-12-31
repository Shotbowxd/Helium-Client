package javax.vecmath;

import java.io.Serializable;

public class Matrix4d
        implements Serializable {
    public double m00;
    public double m01;
    public double m02;
    public double m03;
    public double m10;
    public double m11;
    public double m12;
    public double m13;
    public double m20;
    public double m21;
    public double m22;
    public double m23;
    public double m30;
    public double m31;
    public double m32;
    public double m33;

    public Matrix4d(double m00, double m01, double m02, double m03, double m10, double m11, double m12, double m13, double m20, double m21, double m22, double m23, double m30, double m31, double m32, double m33) {
        this.set(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
    }

    public Matrix4d(double[] v) {
        this.set(v);
    }

    public Matrix4d(Quat4d q1, Vector3d t1, double s) {
        this.set(q1, t1, s);
    }

    public Matrix4d(Quat4f q1, Vector3d t1, double s) {
        this.set(q1, t1, s);
    }

    public Matrix4d(Matrix4d m1) {
        this.set(m1);
    }

    public Matrix4d(Matrix4f m1) {
        this.set(m1);
    }

    public Matrix4d(Matrix3f m1, Vector3d t1, double s) {
        this.set(m1);
        this.mulRotationScale(s);
        this.setTranslation(t1);
        this.m33 = 1.0;
    }

    public Matrix4d(Matrix3d m1, Vector3d t1, double s) {
        this.set(m1, t1, s);
    }

    public Matrix4d() {
        this.setZero();
    }

    public String toString() {
        String nl = System.getProperty("line.separator");
        return "[" + nl + "  [" + this.m00 + "\t" + this.m01 + "\t" + this.m02 + "\t" + this.m03 + "]" + nl + "  [" + this.m10 + "\t" + this.m11 + "\t" + this.m12 + "\t" + this.m13 + "]" + nl + "  [" + this.m20 + "\t" + this.m21 + "\t" + this.m22 + "\t" + this.m23 + "]" + nl + "  [" + this.m30 + "\t" + this.m31 + "\t" + this.m32 + "\t" + this.m33 + "] ]";
    }

    public final void setIdentity() {
        this.m00 = 1.0;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m03 = 0.0;
        this.m10 = 0.0;
        this.m11 = 1.0;
        this.m12 = 0.0;
        this.m13 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 1.0;
        this.m23 = 0.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }

    public final void setElement(int row, int column, double value) {
        if (row == 0) {
            if (column == 0) {
                this.m00 = value;
            } else if (column == 1) {
                this.m01 = value;
            } else if (column == 2) {
                this.m02 = value;
            } else {
                if (column != 3) {
                    throw new ArrayIndexOutOfBoundsException("column must be 0 to 3 and is " + column);
                }
                this.m03 = value;
            }
        } else if (row == 1) {
            if (column == 0) {
                this.m10 = value;
            } else if (column == 1) {
                this.m11 = value;
            } else if (column == 2) {
                this.m12 = value;
            } else {
                if (column != 3) {
                    throw new ArrayIndexOutOfBoundsException("column must be 0 to 3 and is " + column);
                }
                this.m13 = value;
            }
        } else if (row == 2) {
            if (column == 0) {
                this.m20 = value;
            } else if (column == 1) {
                this.m21 = value;
            } else if (column == 2) {
                this.m22 = value;
            } else {
                if (column != 3) {
                    throw new ArrayIndexOutOfBoundsException("column must be 0 to 3 and is " + column);
                }
                this.m23 = value;
            }
        } else {
            if (row != 3) {
                throw new ArrayIndexOutOfBoundsException("row must be 0 to 2 and is " + row);
            }
            if (column == 0) {
                this.m30 = value;
            } else if (column == 1) {
                this.m31 = value;
            } else if (column == 2) {
                this.m32 = value;
            } else {
                if (column != 3) {
                    throw new ArrayIndexOutOfBoundsException("column must be 0 to 3 and is " + column);
                }
                this.m33 = value;
            }
        }
    }

    public final double getElement(int row, int column) {
        if (row == 0) {
            if (column == 0) {
                return this.m00;
            }
            if (column == 1) {
                return this.m01;
            }
            if (column == 2) {
                return this.m02;
            }
            if (column == 3) {
                return this.m03;
            }
            throw new ArrayIndexOutOfBoundsException("column must be 0 to 3 and is " + column);
        }
        if (row == 1) {
            if (column == 0) {
                return this.m10;
            }
            if (column == 1) {
                return this.m11;
            }
            if (column == 2) {
                return this.m12;
            }
            if (column == 3) {
                return this.m13;
            }
            throw new ArrayIndexOutOfBoundsException("column must be 0 to 3 and is " + column);
        }
        if (row == 2) {
            if (column == 0) {
                return this.m20;
            }
            if (column == 1) {
                return this.m21;
            }
            if (column == 2) {
                return this.m22;
            }
            if (column == 3) {
                return this.m23;
            }
            throw new ArrayIndexOutOfBoundsException("column must be 0 to 3 and is " + column);
        }
        if (row != 3) {
            throw new ArrayIndexOutOfBoundsException("row must be 0 to 3 and is " + row);
        }
        if (column == 0) {
            return this.m30;
        }
        if (column == 1) {
            return this.m31;
        }
        if (column == 2) {
            return this.m32;
        }
        if (column == 3) {
            return this.m33;
        }
        throw new ArrayIndexOutOfBoundsException("column must be 0 to 3 and is " + column);
    }

    public final void get(Matrix3d m1) {
        this.SVD(m1, null);
    }

    public final void get(Matrix3f m1) {
        this.SVD(m1);
    }

    public final double get(Matrix3d m1, Vector3d t1) {
        this.get(t1);
        return this.SVD(m1, null);
    }

    public final double get(Matrix3f m1, Vector3d t1) {
        this.get(t1);
        return this.SVD(m1);
    }

    public final void get(Quat4f q1) {
        q1.set(this);
        q1.normalize();
    }

    public final void get(Quat4d q1) {
        q1.set(this);
        q1.normalize();
    }

    public final void get(Vector3d trans) {
        trans.x = this.m03;
        trans.y = this.m13;
        trans.z = this.m23;
    }

    public final void getRotationScale(Matrix3f m1) {
        m1.m00 = (float) this.m00;
        m1.m01 = (float) this.m01;
        m1.m02 = (float) this.m02;
        m1.m10 = (float) this.m10;
        m1.m11 = (float) this.m11;
        m1.m12 = (float) this.m12;
        m1.m20 = (float) this.m20;
        m1.m21 = (float) this.m21;
        m1.m22 = (float) this.m22;
    }

    public final void getRotationScale(Matrix3d m1) {
        m1.m00 = this.m00;
        m1.m01 = this.m01;
        m1.m02 = this.m02;
        m1.m10 = this.m10;
        m1.m11 = this.m11;
        m1.m12 = this.m12;
        m1.m20 = this.m20;
        m1.m21 = this.m21;
        m1.m22 = this.m22;
    }

    public final double getScale() {
        return this.SVD(null);
    }

    public final void setRotationScale(Matrix3d m1) {
        this.m00 = m1.m00;
        this.m01 = m1.m01;
        this.m02 = m1.m02;
        this.m10 = m1.m10;
        this.m11 = m1.m11;
        this.m12 = m1.m12;
        this.m20 = m1.m20;
        this.m21 = m1.m21;
        this.m22 = m1.m22;
    }

    public final void setRotationScale(Matrix3f m1) {
        this.m00 = m1.m00;
        this.m01 = m1.m01;
        this.m02 = m1.m02;
        this.m10 = m1.m10;
        this.m11 = m1.m11;
        this.m12 = m1.m12;
        this.m20 = m1.m20;
        this.m21 = m1.m21;
        this.m22 = m1.m22;
    }

    public final void setScale(double scale) {
        this.SVD(null, this);
        this.mulRotationScale(scale);
    }

    public final void setRow(int row, double x, double y2, double z, double w) {
        if (row == 0) {
            this.m00 = x;
            this.m01 = y2;
            this.m02 = z;
            this.m03 = w;
        } else if (row == 1) {
            this.m10 = x;
            this.m11 = y2;
            this.m12 = z;
            this.m13 = w;
        } else if (row == 2) {
            this.m20 = x;
            this.m21 = y2;
            this.m22 = z;
            this.m23 = w;
        } else {
            if (row != 3) {
                throw new ArrayIndexOutOfBoundsException("row must be 0 to 3 and is " + row);
            }
            this.m30 = x;
            this.m31 = y2;
            this.m32 = z;
            this.m33 = w;
        }
    }

    public final void setRow(int row, Vector4d v) {
        if (row == 0) {
            this.m00 = v.x;
            this.m01 = v.y;
            this.m02 = v.z;
            this.m03 = v.w;
        } else if (row == 1) {
            this.m10 = v.x;
            this.m11 = v.y;
            this.m12 = v.z;
            this.m13 = v.w;
        } else if (row == 2) {
            this.m20 = v.x;
            this.m21 = v.y;
            this.m22 = v.z;
            this.m23 = v.w;
        } else {
            if (row != 3) {
                throw new ArrayIndexOutOfBoundsException("row must be 0 to 3 and is " + row);
            }
            this.m30 = v.x;
            this.m31 = v.y;
            this.m32 = v.z;
            this.m33 = v.w;
        }
    }

    public final void setRow(int row, double[] v) {
        if (row == 0) {
            this.m00 = v[0];
            this.m01 = v[1];
            this.m02 = v[2];
            this.m03 = v[3];
        } else if (row == 1) {
            this.m10 = v[0];
            this.m11 = v[1];
            this.m12 = v[2];
            this.m13 = v[3];
        } else if (row == 2) {
            this.m20 = v[0];
            this.m21 = v[1];
            this.m22 = v[2];
            this.m23 = v[3];
        } else {
            if (row != 3) {
                throw new ArrayIndexOutOfBoundsException("row must be 0 to 3 and is " + row);
            }
            this.m30 = v[0];
            this.m31 = v[1];
            this.m32 = v[2];
            this.m33 = v[3];
        }
    }

    public final void getRow(int row, Vector4d v) {
        if (row == 0) {
            v.x = this.m00;
            v.y = this.m01;
            v.z = this.m02;
            v.w = this.m03;
        } else if (row == 1) {
            v.x = this.m10;
            v.y = this.m11;
            v.z = this.m12;
            v.w = this.m13;
        } else if (row == 2) {
            v.x = this.m20;
            v.y = this.m21;
            v.z = this.m22;
            v.w = this.m23;
        } else {
            if (row != 3) {
                throw new ArrayIndexOutOfBoundsException("row must be 0 to 3 and is " + row);
            }
            v.x = this.m30;
            v.y = this.m31;
            v.z = this.m32;
            v.w = this.m33;
        }
    }

    public final void getRow(int row, double[] v) {
        if (row == 0) {
            v[0] = this.m00;
            v[1] = this.m01;
            v[2] = this.m02;
            v[3] = this.m03;
        } else if (row == 1) {
            v[0] = this.m10;
            v[1] = this.m11;
            v[2] = this.m12;
            v[3] = this.m13;
        } else if (row == 2) {
            v[0] = this.m20;
            v[1] = this.m21;
            v[2] = this.m22;
            v[3] = this.m23;
        } else {
            if (row != 3) {
                throw new ArrayIndexOutOfBoundsException("row must be 0 to 3 and is " + row);
            }
            v[0] = this.m30;
            v[1] = this.m31;
            v[2] = this.m32;
            v[3] = this.m33;
        }
    }

    public final void setColumn(int column, double x, double y2, double z, double w) {
        if (column == 0) {
            this.m00 = x;
            this.m10 = y2;
            this.m20 = z;
            this.m30 = w;
        } else if (column == 1) {
            this.m01 = x;
            this.m11 = y2;
            this.m21 = z;
            this.m31 = w;
        } else if (column == 2) {
            this.m02 = x;
            this.m12 = y2;
            this.m22 = z;
            this.m32 = w;
        } else {
            if (column != 3) {
                throw new ArrayIndexOutOfBoundsException("column must be 0 to 3 and is " + column);
            }
            this.m03 = x;
            this.m13 = y2;
            this.m23 = z;
            this.m33 = w;
        }
    }

    public final void setColumn(int column, Vector4d v) {
        if (column == 0) {
            this.m00 = v.x;
            this.m10 = v.y;
            this.m20 = v.z;
            this.m30 = v.w;
        } else if (column == 1) {
            this.m01 = v.x;
            this.m11 = v.y;
            this.m21 = v.z;
            this.m31 = v.w;
        } else if (column == 2) {
            this.m02 = v.x;
            this.m12 = v.y;
            this.m22 = v.z;
            this.m32 = v.w;
        } else {
            if (column != 3) {
                throw new ArrayIndexOutOfBoundsException("column must be 0 to 3 and is " + column);
            }
            this.m03 = v.x;
            this.m13 = v.y;
            this.m23 = v.z;
            this.m33 = v.w;
        }
    }

    public final void setColumn(int column, double[] v) {
        if (column == 0) {
            this.m00 = v[0];
            this.m10 = v[1];
            this.m20 = v[2];
            this.m30 = v[3];
        } else if (column == 1) {
            this.m01 = v[0];
            this.m11 = v[1];
            this.m21 = v[2];
            this.m31 = v[3];
        } else if (column == 2) {
            this.m02 = v[0];
            this.m12 = v[1];
            this.m22 = v[2];
            this.m32 = v[3];
        } else {
            if (column != 3) {
                throw new ArrayIndexOutOfBoundsException("column must be 0 to 3 and is " + column);
            }
            this.m03 = v[0];
            this.m13 = v[1];
            this.m23 = v[2];
            this.m33 = v[3];
        }
    }

    public final void getColumn(int column, Vector4d v) {
        if (column == 0) {
            v.x = this.m00;
            v.y = this.m10;
            v.z = this.m20;
            v.w = this.m30;
        } else if (column == 1) {
            v.x = this.m01;
            v.y = this.m11;
            v.z = this.m21;
            v.w = this.m31;
        } else if (column == 2) {
            v.x = this.m02;
            v.y = this.m12;
            v.z = this.m22;
            v.w = this.m32;
        } else {
            if (column != 3) {
                throw new ArrayIndexOutOfBoundsException("column must be 0 to 3 and is " + column);
            }
            v.x = this.m03;
            v.y = this.m13;
            v.z = this.m23;
            v.w = this.m33;
        }
    }

    public final void getColumn(int column, double[] v) {
        if (column == 0) {
            v[0] = this.m00;
            v[1] = this.m10;
            v[2] = this.m20;
            v[3] = this.m30;
        } else if (column == 1) {
            v[0] = this.m01;
            v[1] = this.m11;
            v[2] = this.m21;
            v[3] = this.m31;
        } else if (column == 2) {
            v[0] = this.m02;
            v[1] = this.m12;
            v[2] = this.m22;
            v[3] = this.m32;
        } else {
            if (column != 3) {
                throw new ArrayIndexOutOfBoundsException("column must be 0 to 3 and is " + column);
            }
            v[0] = this.m03;
            v[1] = this.m13;
            v[2] = this.m23;
            v[3] = this.m33;
        }
    }

    public final void add(double scalar) {
        this.m00 += scalar;
        this.m01 += scalar;
        this.m02 += scalar;
        this.m03 += scalar;
        this.m10 += scalar;
        this.m11 += scalar;
        this.m12 += scalar;
        this.m13 += scalar;
        this.m20 += scalar;
        this.m21 += scalar;
        this.m22 += scalar;
        this.m23 += scalar;
        this.m30 += scalar;
        this.m31 += scalar;
        this.m32 += scalar;
        this.m33 += scalar;
    }

    public final void add(double scalar, Matrix4d m1) {
        this.set(m1);
        this.add(scalar);
    }

    public final void add(Matrix4d m1, Matrix4d m2) {
        this.set(m1.m00 + m2.m00, m1.m01 + m2.m01, m1.m02 + m2.m02, m1.m03 + m2.m03, m1.m10 + m2.m10, m1.m11 + m2.m11, m1.m12 + m2.m12, m1.m13 + m2.m13, m1.m20 + m2.m20, m1.m21 + m2.m21, m1.m22 + m2.m22, m1.m23 + m2.m23, m1.m30 + m2.m30, m1.m31 + m2.m31, m1.m32 + m2.m32, m1.m33 + m2.m33);
    }

    public final void add(Matrix4d m1) {
        this.m00 += m1.m00;
        this.m01 += m1.m01;
        this.m02 += m1.m02;
        this.m03 += m1.m03;
        this.m10 += m1.m10;
        this.m11 += m1.m11;
        this.m12 += m1.m12;
        this.m13 += m1.m13;
        this.m20 += m1.m20;
        this.m21 += m1.m21;
        this.m22 += m1.m22;
        this.m23 += m1.m23;
        this.m30 += m1.m30;
        this.m31 += m1.m31;
        this.m32 += m1.m32;
        this.m33 += m1.m33;
    }

    public final void sub(Matrix4d m1, Matrix4d m2) {
        this.set(m1.m00 - m2.m00, m1.m01 - m2.m01, m1.m02 - m2.m02, m1.m03 - m2.m03, m1.m10 - m2.m10, m1.m11 - m2.m11, m1.m12 - m2.m12, m1.m13 - m2.m13, m1.m20 - m2.m20, m1.m21 - m2.m21, m1.m22 - m2.m22, m1.m23 - m2.m23, m1.m30 - m2.m30, m1.m31 - m2.m31, m1.m32 - m2.m32, m1.m33 - m2.m33);
    }

    public final void sub(Matrix4d m1) {
        this.m00 -= m1.m00;
        this.m01 -= m1.m01;
        this.m02 -= m1.m02;
        this.m03 -= m1.m03;
        this.m10 -= m1.m10;
        this.m11 -= m1.m11;
        this.m12 -= m1.m12;
        this.m13 -= m1.m13;
        this.m20 -= m1.m20;
        this.m21 -= m1.m21;
        this.m22 -= m1.m22;
        this.m23 -= m1.m23;
        this.m30 -= m1.m30;
        this.m31 -= m1.m31;
        this.m32 -= m1.m32;
        this.m33 -= m1.m33;
    }

    public final void transpose() {
        double tmp = this.m01;
        this.m01 = this.m10;
        this.m10 = tmp;
        tmp = this.m02;
        this.m02 = this.m20;
        this.m20 = tmp;
        tmp = this.m03;
        this.m03 = this.m30;
        this.m30 = tmp;
        tmp = this.m12;
        this.m12 = this.m21;
        this.m21 = tmp;
        tmp = this.m13;
        this.m13 = this.m31;
        this.m31 = tmp;
        tmp = this.m23;
        this.m23 = this.m32;
        this.m32 = tmp;
    }

    public final void transpose(Matrix4d m1) {
        this.set(m1);
        this.transpose();
    }

    public final void set(double[] m) {
        this.m00 = m[0];
        this.m01 = m[1];
        this.m02 = m[2];
        this.m03 = m[3];
        this.m10 = m[4];
        this.m11 = m[5];
        this.m12 = m[6];
        this.m13 = m[7];
        this.m20 = m[8];
        this.m21 = m[9];
        this.m22 = m[10];
        this.m23 = m[11];
        this.m30 = m[12];
        this.m31 = m[13];
        this.m32 = m[14];
        this.m33 = m[15];
    }

    public final void set(Matrix3f m1) {
        this.m00 = m1.m00;
        this.m01 = m1.m01;
        this.m02 = m1.m02;
        this.m03 = 0.0;
        this.m10 = m1.m10;
        this.m11 = m1.m11;
        this.m12 = m1.m12;
        this.m13 = 0.0;
        this.m20 = m1.m20;
        this.m21 = m1.m21;
        this.m22 = m1.m22;
        this.m23 = 0.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }

    public final void set(Matrix3d m1) {
        this.m00 = m1.m00;
        this.m01 = m1.m01;
        this.m02 = m1.m02;
        this.m03 = 0.0;
        this.m10 = m1.m10;
        this.m11 = m1.m11;
        this.m12 = m1.m12;
        this.m13 = 0.0;
        this.m20 = m1.m20;
        this.m21 = m1.m21;
        this.m22 = m1.m22;
        this.m23 = 0.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }

    public final void set(Quat4d q1) {
        this.setFromQuat(q1.x, q1.y, q1.z, q1.w);
    }

    public final void set(AxisAngle4d a1) {
        this.setFromAxisAngle(a1.x, a1.y, a1.z, a1.angle);
    }

    public final void set(Quat4f q1) {
        this.setFromQuat(q1.x, q1.y, q1.z, q1.w);
    }

    public final void set(AxisAngle4f a1) {
        this.setFromAxisAngle(a1.x, a1.y, a1.z, a1.angle);
    }

    public final void set(Quat4d q1, Vector3d t1, double s) {
        this.set(q1);
        this.mulRotationScale(s);
        this.m03 = t1.x;
        this.m13 = t1.y;
        this.m23 = t1.z;
    }

    public final void set(Quat4f q1, Vector3d t1, double s) {
        this.set(q1);
        this.mulRotationScale(s);
        this.m03 = t1.x;
        this.m13 = t1.y;
        this.m23 = t1.z;
    }

    public final void set(Quat4f q1, Vector3f t1, float s) {
        this.set(q1);
        this.mulRotationScale(s);
        this.m03 = t1.x;
        this.m13 = t1.y;
        this.m23 = t1.z;
    }

    public final void set(Matrix4d m1) {
        this.m00 = m1.m00;
        this.m01 = m1.m01;
        this.m02 = m1.m02;
        this.m03 = m1.m03;
        this.m10 = m1.m10;
        this.m11 = m1.m11;
        this.m12 = m1.m12;
        this.m13 = m1.m13;
        this.m20 = m1.m20;
        this.m21 = m1.m21;
        this.m22 = m1.m22;
        this.m23 = m1.m23;
        this.m30 = m1.m30;
        this.m31 = m1.m31;
        this.m32 = m1.m32;
        this.m33 = m1.m33;
    }

    public final void set(Matrix4f m1) {
        this.m00 = m1.m00;
        this.m01 = m1.m01;
        this.m02 = m1.m02;
        this.m03 = m1.m03;
        this.m10 = m1.m10;
        this.m11 = m1.m11;
        this.m12 = m1.m12;
        this.m13 = m1.m13;
        this.m20 = m1.m20;
        this.m21 = m1.m21;
        this.m22 = m1.m22;
        this.m23 = m1.m23;
        this.m30 = m1.m30;
        this.m31 = m1.m31;
        this.m32 = m1.m32;
        this.m33 = m1.m33;
    }

    public final void invert(Matrix4d m1) {
        this.set(m1);
        this.invert();
    }

    public final void invert() {
        double s = this.determinant();
        if (s == 0.0) {
            return;
        }
        s = 1.0 / s;
        this.set(this.m11 * (this.m22 * this.m33 - this.m23 * this.m32) + this.m12 * (this.m23 * this.m31 - this.m21 * this.m33) + this.m13 * (this.m21 * this.m32 - this.m22 * this.m31), this.m21 * (this.m02 * this.m33 - this.m03 * this.m32) + this.m22 * (this.m03 * this.m31 - this.m01 * this.m33) + this.m23 * (this.m01 * this.m32 - this.m02 * this.m31), this.m31 * (this.m02 * this.m13 - this.m03 * this.m12) + this.m32 * (this.m03 * this.m11 - this.m01 * this.m13) + this.m33 * (this.m01 * this.m12 - this.m02 * this.m11), this.m01 * (this.m13 * this.m22 - this.m12 * this.m23) + this.m02 * (this.m11 * this.m23 - this.m13 * this.m21) + this.m03 * (this.m12 * this.m21 - this.m11 * this.m22), this.m12 * (this.m20 * this.m33 - this.m23 * this.m30) + this.m13 * (this.m22 * this.m30 - this.m20 * this.m32) + this.m10 * (this.m23 * this.m32 - this.m22 * this.m33), this.m22 * (this.m00 * this.m33 - this.m03 * this.m30) + this.m23 * (this.m02 * this.m30 - this.m00 * this.m32) + this.m20 * (this.m03 * this.m32 - this.m02 * this.m33), this.m32 * (this.m00 * this.m13 - this.m03 * this.m10) + this.m33 * (this.m02 * this.m10 - this.m00 * this.m12) + this.m30 * (this.m03 * this.m12 - this.m02 * this.m13), this.m02 * (this.m13 * this.m20 - this.m10 * this.m23) + this.m03 * (this.m10 * this.m22 - this.m12 * this.m20) + this.m00 * (this.m12 * this.m23 - this.m13 * this.m22), this.m13 * (this.m20 * this.m31 - this.m21 * this.m30) + this.m10 * (this.m21 * this.m33 - this.m23 * this.m31) + this.m11 * (this.m23 * this.m30 - this.m20 * this.m33), this.m23 * (this.m00 * this.m31 - this.m01 * this.m30) + this.m20 * (this.m01 * this.m33 - this.m03 * this.m31) + this.m21 * (this.m03 * this.m30 - this.m00 * this.m33), this.m33 * (this.m00 * this.m11 - this.m01 * this.m10) + this.m30 * (this.m01 * this.m13 - this.m03 * this.m11) + this.m31 * (this.m03 * this.m10 - this.m00 * this.m13), this.m03 * (this.m11 * this.m20 - this.m10 * this.m21) + this.m00 * (this.m13 * this.m21 - this.m11 * this.m23) + this.m01 * (this.m10 * this.m23 - this.m13 * this.m20), this.m10 * (this.m22 * this.m31 - this.m21 * this.m32) + this.m11 * (this.m20 * this.m32 - this.m22 * this.m30) + this.m12 * (this.m21 * this.m30 - this.m20 * this.m31), this.m20 * (this.m02 * this.m31 - this.m01 * this.m32) + this.m21 * (this.m00 * this.m32 - this.m02 * this.m30) + this.m22 * (this.m01 * this.m30 - this.m00 * this.m31), this.m30 * (this.m02 * this.m11 - this.m01 * this.m12) + this.m31 * (this.m00 * this.m12 - this.m02 * this.m10) + this.m32 * (this.m01 * this.m10 - this.m00 * this.m11), this.m00 * (this.m11 * this.m22 - this.m12 * this.m21) + this.m01 * (this.m12 * this.m20 - this.m10 * this.m22) + this.m02 * (this.m10 * this.m21 - this.m11 * this.m20));
        this.mul(s);
    }

    public final double determinant() {
        return (this.m00 * this.m11 - this.m01 * this.m10) * (this.m22 * this.m33 - this.m23 * this.m32) - (this.m00 * this.m12 - this.m02 * this.m10) * (this.m21 * this.m33 - this.m23 * this.m31) + (this.m00 * this.m13 - this.m03 * this.m10) * (this.m21 * this.m32 - this.m22 * this.m31) + (this.m01 * this.m12 - this.m02 * this.m11) * (this.m20 * this.m33 - this.m23 * this.m30) - (this.m01 * this.m13 - this.m03 * this.m11) * (this.m20 * this.m32 - this.m22 * this.m30) + (this.m02 * this.m13 - this.m03 * this.m12) * (this.m20 * this.m31 - this.m21 * this.m30);
    }

    public final void set(double scale) {
        this.m00 = scale;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m03 = 0.0;
        this.m10 = 0.0;
        this.m11 = scale;
        this.m12 = 0.0;
        this.m13 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = scale;
        this.m23 = 0.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }

    public final void set(Vector3d v1) {
        this.setIdentity();
        this.setTranslation(v1);
    }

    public final void set(double scale, Vector3d v1) {
        this.set(scale);
        this.setTranslation(v1);
    }

    public final void set(Vector3d v1, double scale) {
        this.m00 = scale;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m03 = scale * v1.x;
        this.m10 = 0.0;
        this.m11 = scale;
        this.m12 = 0.0;
        this.m13 = scale * v1.y;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = scale;
        this.m23 = scale * v1.z;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }

    public final void set(Matrix3f m1, Vector3f t1, float scale) {
        this.setRotationScale(m1);
        this.mulRotationScale(scale);
        this.setTranslation(t1);
        this.m33 = 1.0;
    }

    public final void set(Matrix3d m1, Vector3d t1, double scale) {
        this.setRotationScale(m1);
        this.mulRotationScale(scale);
        this.setTranslation(t1);
        this.m33 = 1.0;
    }

    public final void setTranslation(Vector3d trans) {
        this.m03 = trans.x;
        this.m13 = trans.y;
        this.m23 = trans.z;
    }

    public final void rotX(double angle) {
        double c2 = Math.cos(angle);
        double s = Math.sin(angle);
        this.m00 = 1.0;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m03 = 0.0;
        this.m10 = 0.0;
        this.m11 = c2;
        this.m12 = -s;
        this.m13 = 0.0;
        this.m20 = 0.0;
        this.m21 = s;
        this.m22 = c2;
        this.m23 = 0.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }

    public final void rotY(double angle) {
        double c2 = Math.cos(angle);
        double s = Math.sin(angle);
        this.m00 = c2;
        this.m01 = 0.0;
        this.m02 = s;
        this.m03 = 0.0;
        this.m10 = 0.0;
        this.m11 = 1.0;
        this.m12 = 0.0;
        this.m13 = 0.0;
        this.m20 = -s;
        this.m21 = 0.0;
        this.m22 = c2;
        this.m23 = 0.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }

    public final void rotZ(double angle) {
        double c2 = Math.cos(angle);
        double s = Math.sin(angle);
        this.m00 = c2;
        this.m01 = -s;
        this.m02 = 0.0;
        this.m03 = 0.0;
        this.m10 = s;
        this.m11 = c2;
        this.m12 = 0.0;
        this.m13 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 1.0;
        this.m23 = 0.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }

    public final void mul(double scalar) {
        this.m00 *= scalar;
        this.m01 *= scalar;
        this.m02 *= scalar;
        this.m03 *= scalar;
        this.m10 *= scalar;
        this.m11 *= scalar;
        this.m12 *= scalar;
        this.m13 *= scalar;
        this.m20 *= scalar;
        this.m21 *= scalar;
        this.m22 *= scalar;
        this.m23 *= scalar;
        this.m30 *= scalar;
        this.m31 *= scalar;
        this.m32 *= scalar;
        this.m33 *= scalar;
    }

    public final void mul(double scalar, Matrix4d m1) {
        this.set(m1);
        this.mul(scalar);
    }

    public final void mul(Matrix4d m1) {
        this.mul(this, m1);
    }

    public final void mul(Matrix4d m1, Matrix4d m2) {
        this.set(m1.m00 * m2.m00 + m1.m01 * m2.m10 + m1.m02 * m2.m20 + m1.m03 * m2.m30, m1.m00 * m2.m01 + m1.m01 * m2.m11 + m1.m02 * m2.m21 + m1.m03 * m2.m31, m1.m00 * m2.m02 + m1.m01 * m2.m12 + m1.m02 * m2.m22 + m1.m03 * m2.m32, m1.m00 * m2.m03 + m1.m01 * m2.m13 + m1.m02 * m2.m23 + m1.m03 * m2.m33, m1.m10 * m2.m00 + m1.m11 * m2.m10 + m1.m12 * m2.m20 + m1.m13 * m2.m30, m1.m10 * m2.m01 + m1.m11 * m2.m11 + m1.m12 * m2.m21 + m1.m13 * m2.m31, m1.m10 * m2.m02 + m1.m11 * m2.m12 + m1.m12 * m2.m22 + m1.m13 * m2.m32, m1.m10 * m2.m03 + m1.m11 * m2.m13 + m1.m12 * m2.m23 + m1.m13 * m2.m33, m1.m20 * m2.m00 + m1.m21 * m2.m10 + m1.m22 * m2.m20 + m1.m23 * m2.m30, m1.m20 * m2.m01 + m1.m21 * m2.m11 + m1.m22 * m2.m21 + m1.m23 * m2.m31, m1.m20 * m2.m02 + m1.m21 * m2.m12 + m1.m22 * m2.m22 + m1.m23 * m2.m32, m1.m20 * m2.m03 + m1.m21 * m2.m13 + m1.m22 * m2.m23 + m1.m23 * m2.m33, m1.m30 * m2.m00 + m1.m31 * m2.m10 + m1.m32 * m2.m20 + m1.m33 * m2.m30, m1.m30 * m2.m01 + m1.m31 * m2.m11 + m1.m32 * m2.m21 + m1.m33 * m2.m31, m1.m30 * m2.m02 + m1.m31 * m2.m12 + m1.m32 * m2.m22 + m1.m33 * m2.m32, m1.m30 * m2.m03 + m1.m31 * m2.m13 + m1.m32 * m2.m23 + m1.m33 * m2.m33);
    }

    public final void mulTransposeBoth(Matrix4d m1, Matrix4d m2) {
        this.mul(m2, m1);
        this.transpose();
    }

    public final void mulTransposeRight(Matrix4d m1, Matrix4d m2) {
        this.set(m1.m00 * m2.m00 + m1.m01 * m2.m01 + m1.m02 * m2.m02 + m1.m03 * m2.m03, m1.m00 * m2.m10 + m1.m01 * m2.m11 + m1.m02 * m2.m12 + m1.m03 * m2.m13, m1.m00 * m2.m20 + m1.m01 * m2.m21 + m1.m02 * m2.m22 + m1.m03 * m2.m23, m1.m00 * m2.m30 + m1.m01 * m2.m31 + m1.m02 * m2.m32 + m1.m03 * m2.m33, m1.m10 * m2.m00 + m1.m11 * m2.m01 + m1.m12 * m2.m02 + m1.m13 * m2.m03, m1.m10 * m2.m10 + m1.m11 * m2.m11 + m1.m12 * m2.m12 + m1.m13 * m2.m13, m1.m10 * m2.m20 + m1.m11 * m2.m21 + m1.m12 * m2.m22 + m1.m13 * m2.m23, m1.m10 * m2.m30 + m1.m11 * m2.m31 + m1.m12 * m2.m32 + m1.m13 * m2.m33, m1.m20 * m2.m00 + m1.m21 * m2.m01 + m1.m22 * m2.m02 + m1.m23 * m2.m03, m1.m20 * m2.m10 + m1.m21 * m2.m11 + m1.m22 * m2.m12 + m1.m23 * m2.m13, m1.m20 * m2.m20 + m1.m21 * m2.m21 + m1.m22 * m2.m22 + m1.m23 * m2.m23, m1.m20 * m2.m30 + m1.m21 * m2.m31 + m1.m22 * m2.m32 + m1.m23 * m2.m33, m1.m30 * m2.m00 + m1.m31 * m2.m01 + m1.m32 * m2.m02 + m1.m33 * m2.m03, m1.m30 * m2.m10 + m1.m31 * m2.m11 + m1.m32 * m2.m12 + m1.m33 * m2.m13, m1.m30 * m2.m20 + m1.m31 * m2.m21 + m1.m32 * m2.m22 + m1.m33 * m2.m23, m1.m30 * m2.m30 + m1.m31 * m2.m31 + m1.m32 * m2.m32 + m1.m33 * m2.m33);
    }

    public final void mulTransposeLeft(Matrix4d m1, Matrix4d m2) {
        this.set(m1.m00 * m2.m00 + m1.m10 * m2.m10 + m1.m20 * m2.m20 + m1.m30 * m2.m30, m1.m00 * m2.m01 + m1.m10 * m2.m11 + m1.m20 * m2.m21 + m1.m30 * m2.m31, m1.m00 * m2.m02 + m1.m10 * m2.m12 + m1.m20 * m2.m22 + m1.m30 * m2.m32, m1.m00 * m2.m03 + m1.m10 * m2.m13 + m1.m20 * m2.m23 + m1.m30 * m2.m33, m1.m01 * m2.m00 + m1.m11 * m2.m10 + m1.m21 * m2.m20 + m1.m31 * m2.m30, m1.m01 * m2.m01 + m1.m11 * m2.m11 + m1.m21 * m2.m21 + m1.m31 * m2.m31, m1.m01 * m2.m02 + m1.m11 * m2.m12 + m1.m21 * m2.m22 + m1.m31 * m2.m32, m1.m01 * m2.m03 + m1.m11 * m2.m13 + m1.m21 * m2.m23 + m1.m31 * m2.m33, m1.m02 * m2.m00 + m1.m12 * m2.m10 + m1.m22 * m2.m20 + m1.m32 * m2.m30, m1.m02 * m2.m01 + m1.m12 * m2.m11 + m1.m22 * m2.m21 + m1.m32 * m2.m31, m1.m02 * m2.m02 + m1.m12 * m2.m12 + m1.m22 * m2.m22 + m1.m32 * m2.m32, m1.m02 * m2.m03 + m1.m12 * m2.m13 + m1.m22 * m2.m23 + m1.m32 * m2.m33, m1.m03 * m2.m00 + m1.m13 * m2.m10 + m1.m23 * m2.m20 + m1.m33 * m2.m30, m1.m03 * m2.m01 + m1.m13 * m2.m11 + m1.m23 * m2.m21 + m1.m33 * m2.m31, m1.m03 * m2.m02 + m1.m13 * m2.m12 + m1.m23 * m2.m22 + m1.m33 * m2.m32, m1.m03 * m2.m03 + m1.m13 * m2.m13 + m1.m23 * m2.m23 + m1.m33 * m2.m33);
    }

    public boolean equals(Matrix4d m1) {
        return m1 != null && this.m00 == m1.m00 && this.m01 == m1.m01 && this.m02 == m1.m02 && this.m03 == m1.m03 && this.m10 == m1.m10 && this.m11 == m1.m11 && this.m12 == m1.m12 && this.m13 == m1.m13 && this.m20 == m1.m20 && this.m21 == m1.m21 && this.m22 == m1.m22 && this.m23 == m1.m23 && this.m30 == m1.m30 && this.m31 == m1.m31 && this.m32 == m1.m32 && this.m33 == m1.m33;
    }

    public boolean equals(Object o1) {
        return o1 != null && o1 instanceof Matrix4d && this.equals((Matrix4d) o1);
    }

    @Deprecated
    public boolean epsilonEquals(Matrix4d m1, float epsilon) {
        return Math.abs(this.m00 - m1.m00) <= (double) epsilon && Math.abs(this.m01 - m1.m01) <= (double) epsilon && Math.abs(this.m02 - m1.m02) <= (double) epsilon && Math.abs(this.m03 - m1.m03) <= (double) epsilon && Math.abs(this.m10 - m1.m10) <= (double) epsilon && Math.abs(this.m11 - m1.m11) <= (double) epsilon && Math.abs(this.m12 - m1.m12) <= (double) epsilon && Math.abs(this.m13 - m1.m13) <= (double) epsilon && Math.abs(this.m20 - m1.m20) <= (double) epsilon && Math.abs(this.m21 - m1.m21) <= (double) epsilon && Math.abs(this.m22 - m1.m22) <= (double) epsilon && Math.abs(this.m23 - m1.m23) <= (double) epsilon && Math.abs(this.m30 - m1.m30) <= (double) epsilon && Math.abs(this.m31 - m1.m31) <= (double) epsilon && Math.abs(this.m32 - m1.m32) <= (double) epsilon && Math.abs(this.m33 - m1.m33) <= (double) epsilon;
    }

    public boolean epsilonEquals(Matrix4d m1, double epsilon) {
        return Math.abs(this.m00 - m1.m00) <= epsilon && Math.abs(this.m01 - m1.m01) <= epsilon && Math.abs(this.m02 - m1.m02) <= epsilon && Math.abs(this.m03 - m1.m03) <= epsilon && Math.abs(this.m10 - m1.m10) <= epsilon && Math.abs(this.m11 - m1.m11) <= epsilon && Math.abs(this.m12 - m1.m12) <= epsilon && Math.abs(this.m13 - m1.m13) <= epsilon && Math.abs(this.m20 - m1.m20) <= epsilon && Math.abs(this.m21 - m1.m21) <= epsilon && Math.abs(this.m22 - m1.m22) <= epsilon && Math.abs(this.m23 - m1.m23) <= epsilon && Math.abs(this.m30 - m1.m30) <= epsilon && Math.abs(this.m31 - m1.m31) <= epsilon && Math.abs(this.m32 - m1.m32) <= epsilon && Math.abs(this.m33 - m1.m33) <= epsilon;
    }

    public int hashCode() {
        long bits = Double.doubleToLongBits(this.m00);
        int hash = (int) (bits ^ bits >> 32);
        bits = Double.doubleToLongBits(this.m01);
        hash ^= (int) (bits ^ bits >> 32);
        bits = Double.doubleToLongBits(this.m02);
        hash ^= (int) (bits ^ bits >> 32);
        bits = Double.doubleToLongBits(this.m03);
        hash ^= (int) (bits ^ bits >> 32);
        bits = Double.doubleToLongBits(this.m10);
        hash ^= (int) (bits ^ bits >> 32);
        bits = Double.doubleToLongBits(this.m11);
        hash ^= (int) (bits ^ bits >> 32);
        bits = Double.doubleToLongBits(this.m12);
        hash ^= (int) (bits ^ bits >> 32);
        bits = Double.doubleToLongBits(this.m13);
        hash ^= (int) (bits ^ bits >> 32);
        bits = Double.doubleToLongBits(this.m20);
        hash ^= (int) (bits ^ bits >> 32);
        bits = Double.doubleToLongBits(this.m21);
        hash ^= (int) (bits ^ bits >> 32);
        bits = Double.doubleToLongBits(this.m22);
        hash ^= (int) (bits ^ bits >> 32);
        bits = Double.doubleToLongBits(this.m23);
        hash ^= (int) (bits ^ bits >> 32);
        bits = Double.doubleToLongBits(this.m30);
        hash ^= (int) (bits ^ bits >> 32);
        bits = Double.doubleToLongBits(this.m31);
        hash ^= (int) (bits ^ bits >> 32);
        bits = Double.doubleToLongBits(this.m32);
        hash ^= (int) (bits ^ bits >> 32);
        bits = Double.doubleToLongBits(this.m33);
        return hash ^= (int) (bits ^ bits >> 32);
    }

    public final void transform(Tuple4d vec, Tuple4d vecOut) {
        vecOut.set(this.m00 * vec.x + this.m01 * vec.y + this.m02 * vec.z + this.m03 * vec.w, this.m10 * vec.x + this.m11 * vec.y + this.m12 * vec.z + this.m13 * vec.w, this.m20 * vec.x + this.m21 * vec.y + this.m22 * vec.z + this.m23 * vec.w, this.m30 * vec.x + this.m31 * vec.y + this.m32 * vec.z + this.m33 * vec.w);
    }

    public final void transform(Tuple4d vec) {
        this.transform(vec, vec);
    }

    public final void transform(Tuple4f vec, Tuple4f vecOut) {
        vecOut.set((float) (this.m00 * (double) vec.x + this.m01 * (double) vec.y + this.m02 * (double) vec.z + this.m03 * (double) vec.w), (float) (this.m10 * (double) vec.x + this.m11 * (double) vec.y + this.m12 * (double) vec.z + this.m13 * (double) vec.w), (float) (this.m20 * (double) vec.x + this.m21 * (double) vec.y + this.m22 * (double) vec.z + this.m23 * (double) vec.w), (float) (this.m30 * (double) vec.x + this.m31 * (double) vec.y + this.m32 * (double) vec.z + this.m33 * (double) vec.w));
    }

    public final void transform(Tuple4f vec) {
        this.transform(vec, vec);
    }

    public final void transform(Point3d point, Point3d pointOut) {
        pointOut.set(this.m00 * point.x + this.m01 * point.y + this.m02 * point.z + this.m03, this.m10 * point.x + this.m11 * point.y + this.m12 * point.z + this.m13, this.m20 * point.x + this.m21 * point.y + this.m22 * point.z + this.m23);
    }

    public final void transform(Point3d point) {
        this.transform(point, point);
    }

    public final void transform(Point3f point, Point3f pointOut) {
        pointOut.set((float) (this.m00 * (double) point.x + this.m01 * (double) point.y + this.m02 * (double) point.z + this.m03), (float) (this.m10 * (double) point.x + this.m11 * (double) point.y + this.m12 * (double) point.z + this.m13), (float) (this.m20 * (double) point.x + this.m21 * (double) point.y + this.m22 * (double) point.z + this.m23));
    }

    public final void transform(Point3f point) {
        this.transform(point, point);
    }

    public final void transform(Vector3d normal, Vector3d normalOut) {
        normalOut.set(this.m00 * normal.x + this.m01 * normal.y + this.m02 * normal.z, this.m10 * normal.x + this.m11 * normal.y + this.m12 * normal.z, this.m20 * normal.x + this.m21 * normal.y + this.m22 * normal.z);
    }

    public final void transform(Vector3d normal) {
        this.transform(normal, normal);
    }

    public final void transform(Vector3f normal, Vector3f normalOut) {
        normalOut.set((float) (this.m00 * (double) normal.x + this.m01 * (double) normal.y + this.m02 * (double) normal.z), (float) (this.m10 * (double) normal.x + this.m11 * (double) normal.y + this.m12 * (double) normal.z), (float) (this.m20 * (double) normal.x + this.m21 * (double) normal.y + this.m22 * (double) normal.z));
    }

    public final void transform(Vector3f normal) {
        this.transform(normal, normal);
    }

    public final void setRotation(Matrix3d m1) {
        double scale = this.SVD(null, null);
        this.setRotationScale(m1);
        this.mulRotationScale(scale);
    }

    public final void setRotation(Matrix3f m1) {
        double scale = this.SVD(null, null);
        this.setRotationScale(m1);
        this.mulRotationScale(scale);
    }

    public final void setRotation(Quat4f q1) {
        double scale = this.SVD(null, null);
        double tx = this.m03;
        double ty = this.m13;
        double tz = this.m23;
        double w0 = this.m30;
        double w2 = this.m31;
        double w3 = this.m32;
        double w4 = this.m33;
        this.set(q1);
        this.mulRotationScale(scale);
        this.m03 = tx;
        this.m13 = ty;
        this.m23 = tz;
        this.m30 = w0;
        this.m31 = w2;
        this.m32 = w3;
        this.m33 = w4;
    }

    public final void setRotation(Quat4d q1) {
        double scale = this.SVD(null, null);
        double tx = this.m03;
        double ty = this.m13;
        double tz = this.m23;
        double w0 = this.m30;
        double w2 = this.m31;
        double w3 = this.m32;
        double w4 = this.m33;
        this.set(q1);
        this.mulRotationScale(scale);
        this.m03 = tx;
        this.m13 = ty;
        this.m23 = tz;
        this.m30 = w0;
        this.m31 = w2;
        this.m32 = w3;
        this.m33 = w4;
    }

    public final void setRotation(AxisAngle4d a1) {
        double scale = this.SVD(null, null);
        double tx = this.m03;
        double ty = this.m13;
        double tz = this.m23;
        double w0 = this.m30;
        double w2 = this.m31;
        double w3 = this.m32;
        double w4 = this.m33;
        this.set(a1);
        this.mulRotationScale(scale);
        this.m03 = tx;
        this.m13 = ty;
        this.m23 = tz;
        this.m30 = w0;
        this.m31 = w2;
        this.m32 = w3;
        this.m33 = w4;
    }

    public final void setZero() {
        this.m00 = 0.0;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m03 = 0.0;
        this.m10 = 0.0;
        this.m11 = 0.0;
        this.m12 = 0.0;
        this.m13 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 0.0;
        this.m23 = 0.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 0.0;
    }

    public final void negate() {
        this.m00 = -this.m00;
        this.m01 = -this.m01;
        this.m02 = -this.m02;
        this.m03 = -this.m03;
        this.m10 = -this.m10;
        this.m11 = -this.m11;
        this.m12 = -this.m12;
        this.m13 = -this.m13;
        this.m20 = -this.m20;
        this.m21 = -this.m21;
        this.m22 = -this.m22;
        this.m23 = -this.m23;
        this.m30 = -this.m30;
        this.m31 = -this.m31;
        this.m32 = -this.m32;
        this.m33 = -this.m33;
    }

    public final void negate(Matrix4d m1) {
        this.set(m1);
        this.negate();
    }

    private void set(double m00, double m01, double m02, double m03, double m10, double m11, double m12, double m13, double m20, double m21, double m22, double m23, double m30, double m31, double m32, double m33) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m03 = m03;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m13 = m13;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
        this.m23 = m23;
        this.m30 = m30;
        this.m31 = m31;
        this.m32 = m32;
        this.m33 = m33;
    }

    private double SVD(Matrix3d rot3, Matrix4d rot4) {
        double n;
        double s = Math.sqrt((this.m00 * this.m00 + this.m10 * this.m10 + this.m20 * this.m20 + this.m01 * this.m01 + this.m11 * this.m11 + this.m21 * this.m21 + this.m02 * this.m02 + this.m12 * this.m12 + this.m22 * this.m22) / 3.0);
        if (rot3 != null) {
            this.getRotationScale(rot3);
            n = 1.0 / Math.sqrt(this.m00 * this.m00 + this.m10 * this.m10 + this.m20 * this.m20);
            rot3.m00 *= n;
            rot3.m10 *= n;
            rot3.m20 *= n;
            n = 1.0 / Math.sqrt(this.m01 * this.m01 + this.m11 * this.m11 + this.m21 * this.m21);
            rot3.m01 *= n;
            rot3.m11 *= n;
            rot3.m21 *= n;
            n = 1.0 / Math.sqrt(this.m02 * this.m02 + this.m12 * this.m12 + this.m22 * this.m22);
            rot3.m02 *= n;
            rot3.m12 *= n;
            rot3.m22 *= n;
        }
        if (rot4 != null) {
            if (rot4 != this) {
                rot4.setRotationScale(this);
            }
            n = 1.0 / Math.sqrt(this.m00 * this.m00 + this.m10 * this.m10 + this.m20 * this.m20);
            rot4.m00 *= n;
            rot4.m10 *= n;
            rot4.m20 *= n;
            n = 1.0 / Math.sqrt(this.m01 * this.m01 + this.m11 * this.m11 + this.m21 * this.m21);
            rot4.m01 *= n;
            rot4.m11 *= n;
            rot4.m21 *= n;
            n = 1.0 / Math.sqrt(this.m02 * this.m02 + this.m12 * this.m12 + this.m22 * this.m22);
            rot4.m02 *= n;
            rot4.m12 *= n;
            rot4.m22 *= n;
        }
        return s;
    }

    private float SVD(Matrix3f rot) {
        double t;
        double s = Math.sqrt((this.m00 * this.m00 + this.m10 * this.m10 + this.m20 * this.m20 + this.m01 * this.m01 + this.m11 * this.m11 + this.m21 * this.m21 + this.m02 * this.m02 + this.m12 * this.m12 + this.m22 * this.m22) / 3.0);
        double d = t = s == 0.0 ? 0.0 : 1.0 / s;
        if (rot != null) {
            this.getRotationScale(rot);
            rot.mul((float) t);
        }
        return (float) s;
    }

    private void mulRotationScale(double scale) {
        this.m00 *= scale;
        this.m01 *= scale;
        this.m02 *= scale;
        this.m10 *= scale;
        this.m11 *= scale;
        this.m12 *= scale;
        this.m20 *= scale;
        this.m21 *= scale;
        this.m22 *= scale;
    }

    private void setRotationScale(Matrix4d m1) {
        this.m00 = m1.m00;
        this.m01 = m1.m01;
        this.m02 = m1.m02;
        this.m10 = m1.m10;
        this.m11 = m1.m11;
        this.m12 = m1.m12;
        this.m20 = m1.m20;
        this.m21 = m1.m21;
        this.m22 = m1.m22;
    }

    private void setTranslation(Vector3f trans) {
        this.m03 = trans.x;
        this.m13 = trans.y;
        this.m23 = trans.z;
    }

    private void setFromQuat(double x, double y2, double z, double w) {
        double n = x * x + y2 * y2 + z * z + w * w;
        double s = n > 0.0 ? 2.0 / n : 0.0;
        double xs = x * s;
        double ys = y2 * s;
        double zs = z * s;
        double wx = w * xs;
        double wy = w * ys;
        double wz = w * zs;
        double xx = x * xs;
        double xy = x * ys;
        double xz = x * zs;
        double yy = y2 * ys;
        double yz = y2 * zs;
        double zz = z * zs;
        this.setIdentity();
        this.m00 = 1.0 - (yy + zz);
        this.m01 = xy - wz;
        this.m02 = xz + wy;
        this.m10 = xy + wz;
        this.m11 = 1.0 - (xx + zz);
        this.m12 = yz - wx;
        this.m20 = xz - wy;
        this.m21 = yz + wx;
        this.m22 = 1.0 - (xx + yy);
    }

    private void setFromAxisAngle(double x, double y2, double z, double angle) {
        double n = Math.sqrt(x * x + y2 * y2 + z * z);
        n = 1.0 / n;
        double c2 = Math.cos(angle);
        double s = Math.sin(angle);
        double omc = 1.0 - c2;
        this.m00 = c2 + (x *= n) * x * omc;
        this.m11 = c2 + (y2 *= n) * y2 * omc;
        this.m22 = c2 + (z *= n) * z * omc;
        double tmp1 = x * y2 * omc;
        double tmp2 = z * s;
        this.m01 = tmp1 - tmp2;
        this.m10 = tmp1 + tmp2;
        tmp1 = x * z * omc;
        tmp2 = y2 * s;
        this.m02 = tmp1 + tmp2;
        this.m20 = tmp1 - tmp2;
        tmp1 = y2 * z * omc;
        tmp2 = x * s;
        this.m12 = tmp1 - tmp2;
        this.m21 = tmp1 + tmp2;
    }
}

