package fst2017.ueb2.raytracer.math;
import fst2017.ueb2.raytracer.RGB;
public class Triangle {
    Vec3D p1, p2, p3;
    private RGB color;
    private Vec3D normal;
    private float shininess;

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

    public RGB getColor() {
        return color;
    }

    public void setColor(RGB color) {
        this.color = color;
    }

    public Vec3D getNormal() {
        return normal;
    }

    public void setNormal(Vec3D normal) {
        this.normal = normal;
    }

    public float getShininess() {
        return shininess;
    }

    public void setShininess(float shininess) {
        this.shininess = shininess;
    }
}
