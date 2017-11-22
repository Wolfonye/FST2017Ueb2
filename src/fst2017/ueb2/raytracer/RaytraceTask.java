package fst2017.ueb2.raytracer;

import fst2017.ueb2.raytracer.math.Ray;
import fst2017.ueb2.raytracer.util.RGB;

import java.util.concurrent.Callable;

class RaytraceTask implements Callable {
    SDRaytracer raytracer;
    SDRenderer renderer;
    int i;

    RaytraceTask(SDRaytracer raytrace, SDRenderer renderer, int ii) {
        this.renderer = renderer;
        this.raytracer = raytrace;
        i = ii;
    }

    public RGB[] call() {
        RGB[] col = new RGB[renderer.getHeight()];
        for (int j = 0; j < renderer.getHeight(); j++) {
            renderer.image[i][j] = new RGB(0, 0, 0);
            for (int k = 0; k < renderer.rayPerPixel; k++) {
                double di = i + (Math.random() / 2 - 0.25);
                double dj = j + (Math.random() / 2 - 0.25);
                if (renderer.rayPerPixel == 1) {
                    di = i;
                    dj = j;
                }
                Ray eye_ray = new Ray();
                eye_ray.setStart(renderer.startX, renderer.startY, renderer.startZ);   // ro
                eye_ray.setDir((float) (((0.5 + di) * renderer.tan_fovx * 2.0) / renderer.getWidth() - renderer.tan_fovx),
                        (float) (((0.5 + dj) * renderer.tan_fovy * 2.0) / renderer.getHeight() - renderer.tan_fovy),
                        (float) 1f);    // rd
                eye_ray.normalize();
                col[j] = renderer.image[i][j].addColor(raytracer.rayTrace(eye_ray, 0), 1.0f / renderer.rayPerPixel);
            }
        }
        return col;
    }
}
