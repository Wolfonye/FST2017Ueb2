package fst2017.ueb2.raytracer.math;

public class IPoint {
    private static float epsilon = 0.0001f;
    private Triangle triangle;
    private Vec3D ipoint;

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


    public Vec3D getIpoint() {
        return ipoint;
    }
    public void setIpoint(Vec3D ipoint) {
        this.ipoint = ipoint;
    }

    public float getDist() {
        return dist;
    }
    public void setDist(float dist) {
        this.dist = dist;
    }

    public static float getEpsilon() { return epsilon;}
    public void setEpsilon(float epsilon) { this.epsilon = epsilon;}
}
