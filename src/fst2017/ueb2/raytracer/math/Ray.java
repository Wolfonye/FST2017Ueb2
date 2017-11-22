package fst2017.ueb2.raytracer.math;

import fst2017.ueb2.raytracer.Scene;

public class Ray {
    private Vec3D start = new Vec3D(0, 0, 0);
    private Vec3D dir = new Vec3D(0, 0, 0);

    public void setStart(float x, float y, float z) {
        setStart(new Vec3D(x, y, z));
    }

    public void setDir(float dx, float dy, float dz) {
        setDir(new Vec3D(dx, dy, dz));
    }

    public void normalize() {
        getDir().normalize();
    }

    // see M�ller&Haines, page 305
    IPoint intersect(Triangle t) {
        float epsilon = IPoint.EPSILON;
        Vec3D e1 = t.p2.minus(t.p1);
        Vec3D e2 = t.p3.minus(t.p1);
        Vec3D p = getDir().cross(e2);
        float a = e1.dot(p);
        if ((a > -epsilon) && (a < epsilon)) return new IPoint(null, null, -1);
        float f = 1 / a;
        Vec3D s = getStart().minus(t.p1);
        float u = f * s.dot(p);
        if ((u < 0.0) || (u > 1.0)) return new IPoint(null, null, -1);
        Vec3D q = s.cross(e1);
        float v = f * getDir().dot(q);
        if ((v < 0.0) || (u + v > 1.0)) return new IPoint(null, null, -1);
        // intersection point is u,v
        float dist = f * e2.dot(q);
        if (dist < epsilon) return new IPoint(null, null, -1);
        Vec3D ip = t.p1.mult(1 - u - v).add(t.p2.mult(u)).add(t.p3.mult(v));
        //DEBUG.debug("Intersection point: "+ip.x+","+ip.y+","+ip.z);
        return new IPoint(t, ip, dist);
    }

    public IPoint hitObject(Scene scene) {
        IPoint isect = new IPoint(null, null, -1);
        float idist = -1;
        for (Triangle t : scene.getTriangles()) {
            IPoint ip = intersect(t);
            if (ip.dist != -1)
                if ((idist == -1) || (ip.dist < idist)) { // save that intersection
                    idist = ip.dist;
                    isect.ipoint = ip.ipoint;
                    isect.dist = ip.dist;
                    isect.setTriangle(t);
                }
        }
        return isect;  // return intersection point and normal
    }

    public Vec3D getStart() {
        return start;
    }

    public void setStart(Vec3D start) {
        this.start = start;
    }

    public Vec3D getDir() {
        return dir;
    }

    public void setDir(Vec3D dir) {
        this.dir = dir;
    }
}
