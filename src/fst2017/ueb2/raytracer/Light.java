package fst2017.ueb2.raytracer;

import fst2017.ueb2.raytracer.math.Vec3D;

class Light {
    RGB color;
    Vec3D position;

    Light(Vec3D pos, RGB c) {
        position = pos;
        color = c;
    }
}
