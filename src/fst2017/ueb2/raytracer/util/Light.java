package fst2017.ueb2.raytracer.util;

import fst2017.ueb2.raytracer.math.Vec3D;

public class Light {
    private RGB color;
    private Vec3D position;

    public Light(Vec3D pos, RGB c) {
        setPosition(pos);
        setColor(c);
    }

    public Vec3D getPosition() {
        return position;
    }

    public void setPosition(Vec3D position) {
        this.position = position;
    }

    public RGB getColor() {
        return color;
    }

    public void setColor(RGB color) {
        this.color = color;
    }
}
