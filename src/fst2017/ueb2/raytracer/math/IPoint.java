package fst2017.ueb2.raytracer.math;

public class IPoint {
    public final static float EPSILON = 0.0001f;
    private Triangle triangle;

    public Vec3D getIpoint() {
        return ipoint;
    }

    public void setIpoint(Vec3D ipoint) {
        this.ipoint = ipoint;
    }

    Vec3D ipoint;

    public float getDist() {
        return dist;
    }

    public void setDist(float dist) {
        this.dist = dist;
    }

    float dist;

    IPoint(Triangle tt, Vec3D ip, float d) {
        setTriangle(tt);
        ipoint = ip;
        dist = d;
    }

    public Triangle getTriangle() {
        return triangle;
    }

    public void setTriangle(Triangle triangle) {
        this.triangle = triangle;
    }
}
