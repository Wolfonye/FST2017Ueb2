package fst2017.ueb2.raytracer.math;

import lombok.Getter;
import lombok.Setter;

public class IPoint {
    public final static float EPSILON = 0.0001f;
    @Getter @Setter private Triangle triangle;
    @Getter @Setter private Vec3D ipoint;
    @Getter @Setter private float dist;

    IPoint(Triangle tt, Vec3D ip, float d) {
        setTriangle(tt);
        ipoint = ip;
        dist = d;
    }
}
