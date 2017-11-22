package fst2017.ueb2.raytracer;

import fst2017.ueb2.raytracer.math.*;
import fst2017.ueb2.raytracer.util.Light;
import fst2017.ueb2.raytracer.util.RGB;
import fst2017.ueb2.raytracer.util.Scene;

/* Implementation of a very simple Raytracer
   Stephan Diehl, Universit�t Trier, 2010-2016
*/


public class SDRaytracer {
    private static final long serialVersionUID = 1L;
    private static final RGB BLACK = new RGB(0.0f, 0.0f, 0.0f);

    private RGB ambient_color;
    private Scene scene;
    private int maxRec;

    public SDRaytracer(){}
    public SDRaytracer(Scene scene){
        this.scene = scene;
    }

    RGB rayTrace(Ray ray, int rec) {
        if (rec > maxRec) return BLACK;
        IPoint ip = ray.hitObject(scene);  // (raytracer, p, n, triangle);
        if (ip.getDist() > IPoint.EPSILON)
            return lighting(ray, ip, rec);
        else
            return BLACK;
    }

    RGB lighting(Ray ray, IPoint ip, int rec) {
        Vec3D point = ip.getIpoint();
        Triangle triangle = ip.getTriangle();
        RGB color = triangle.getColor().addColor(ambient_color, 1);
        Ray shadow_ray = new Ray();
        for (Light light : scene.getLights()) {
            shadow_ray.setStart(point);
            shadow_ray.setDir(light.getPosition().minus(point).mult(-1));
            shadow_ray.getDir().normalize();
            IPoint ip2 = shadow_ray.hitObject(scene);
            if (ip2.getDist() < IPoint.EPSILON) {
                float ratio = Math.max(0, shadow_ray.getDir().dot(triangle.getNormal()));
                color = color.addColor(light.getColor(), ratio);
            }
        }
        Ray reflection = new Ray();
        //R = 2N(N*L)-L)    L ausgehender Vektor
        Vec3D L = ray.getDir().mult(-1);
        reflection.setStart(point);
        reflection.setDir(triangle.getNormal().mult(2 * triangle.getNormal().dot(L)).minus(L));
        reflection.getDir().normalize();
        RGB rcolor = rayTrace(reflection, rec + 1);
        float ratio = (float) Math.pow(Math.max(0, reflection.getDir().dot(L)), triangle.getShininess());
        color = color.addColor(rcolor, ratio);
        return (color);
    }


    public void setAmbientColor(RGB ambient_color) {
        this.ambient_color = ambient_color;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setMaxRec(int maxRec) {
        this.maxRec = maxRec;
    }

}


