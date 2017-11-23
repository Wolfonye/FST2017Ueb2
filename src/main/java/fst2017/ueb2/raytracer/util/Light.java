package fst2017.ueb2.raytracer.util;

import fst2017.ueb2.raytracer.math.Vec3D;
import lombok.Getter;
import lombok.Setter;

public class Light {
    @Getter @Setter private RGB color;
    @Getter @Setter private Vec3D position;

    public Light(Vec3D pos, RGB c) {
        setPosition(pos);
        setColor(c);
    }
}
