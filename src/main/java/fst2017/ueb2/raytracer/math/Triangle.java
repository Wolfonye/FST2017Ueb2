package fst2017.ueb2.raytracer.math;
import fst2017.ueb2.raytracer.util.RGB;
import lombok.Getter;
import lombok.Setter;

public class Triangle {
    Vec3D p1, p2, p3;
    @Getter @Setter private RGB color;
    @Getter @Setter private Vec3D normal;
    @Getter @Setter private float shininess;

    public Triangle(Vec3D pp1, Vec3D pp2, Vec3D pp3, RGB col, float sh) {
        p1 = pp1;
        p2 = pp2;
        p3 = pp3;
        setColor(col);
        setShininess(sh);
        Vec3D e1 = p2.minus(p1),
                e2 = p3.minus(p1);
        setNormal(e1.cross(e2));
        getNormal().normalize();
    }
}
