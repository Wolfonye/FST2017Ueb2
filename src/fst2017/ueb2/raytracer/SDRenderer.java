package fst2017.ueb2.raytracer;

import fst2017.ueb2.raytracer.math.*;
import fst2017.ueb2.raytracer.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SDRenderer {

    private SDRaytracer sdRaytracer;
    private JFrame frame;
    private Scene scene;

    private boolean profiling = false;
    private int width = 1000;
    private int height = 1000;

    private Future[] futureList = new Future[getWidth()];
    private int nrOfProcessors = Runtime.getRuntime().availableProcessors();
    private ExecutorService eservice = Executors.newFixedThreadPool(nrOfProcessors);

    private int maxRec = 3;
    int rayPerPixel = 1;
    int startX, startY, startZ;

    double tan_fovx;
    double tan_fovy;

    RGB[][] image = new RGB[getWidth()][getHeight()];

    private float fovx = (float) 0.628;
    private float fovy = (float) 0.628;
    private RGB ambient_color = new RGB(0.01f, 0.01f, 0.01f);

    private int y_angle_factor = 4, x_angle_factor = -4;


    public SDRenderer() {
        Light[] lights = new Light[]{new Light(new Vec3D(0, 100, 0), new RGB(0.1f, 0.1f, 0.1f))
                , new Light(new Vec3D(100, 200, 300), new RGB(0.5f, 0, 0.0f))
                , new Light(new Vec3D(-100, 200, 300), new RGB(0.0f, 0, 0.5f))
                //,new Light(new fst2017.ueb2.raytracer.math.Vec3D(-100,0,0), new RGB(0.0f,0.8f,0.0f))
        };

        frame = new JFrame();
        createScene();
        sdRaytracer = new SDRaytracer(scene);
        sdRaytracer.setMaxRec(maxRec);
        scene.setLights(lights);
        sdRaytracer.setAmbientColor(ambient_color);
        sdRaytracer.setScene(scene);

        if (!profiling) renderImage();
        else profileRenderImage();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        JPanel area = new JPanel() {
            public void paint(Graphics g) {
                System.out.println("fovx=" + fovx + ", fovy=" + fovy + ", xangle=" + x_angle_factor + ", yangle=" + y_angle_factor);
                if (image == null) return;
                for (int i = 0; i < width; i++)
                    for (int j = 0; j < height; j++) {
                        g.setColor(image[i][j].color());
                        // zeichne einzelnen Pixel
                        g.drawLine(i, height - j, i, height - j);
                    }
            }
        };

        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                boolean redraw = false;
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    x_angle_factor--;
                    //mainLight.position.y-=10;
                    //fovx=fovx+0.1f;
                    //fovy=fovx;
                    //maxRec--; if (maxRec<0) maxRec=0;
                    redraw = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    x_angle_factor++;
                    //mainLight.position.y+=10;
                    //fovx=fovx-0.1f;
                    //fovy=fovx;
                    //maxRec++;if (maxRec>10) return;
                    redraw = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    y_angle_factor--;
                    //mainLight.position.x-=10;
                    //startX-=10;
                    //fovx=fovx+0.1f;
                    //fovy=fovx;
                    redraw = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    y_angle_factor++;
                    //mainLight.position.x+=10;
                    //startX+=10;
                    //fovx=fovx-0.1f;
                    //fovy=fovx;
                    redraw = true;
                }
                if (redraw) {
                    createScene();
                    renderImage();
                    frame.repaint();
                }
            }
        });

        area.setPreferredSize(new Dimension(getWidth(), getHeight()));
        contentPane.add(area);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String argv[]) {
        long start = System.currentTimeMillis();
        SDRenderer sdRenderer = new SDRenderer();
        long end = System.currentTimeMillis();
        long time = end - start;
        System.out.println("time: " + time + " ms");
        System.out.println("nrprocs=" + sdRenderer.nrOfProcessors);
    }


    void renderImage() {
        tan_fovx = Math.tan(fovx);
        tan_fovy = Math.tan(fovy);
        for (int i = 0; i < getWidth(); i++) {
            futureList[i] = (Future) eservice.submit(new RaytraceTask(sdRaytracer,this, i));
        }

        for (int i = 0; i < getWidth(); i++) {
            try {
                RGB[] col = (RGB[]) futureList[i].get();
                for (int j = 0; j < getHeight(); j++)
                    image[i][j] = col[j];
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            }
        }
    }

    void profileRenderImage() {
        long end, start, time;

        renderImage(); // initialisiere Datenstrukturen, erster Lauf verf�lscht sonst Messungen

        for (int procs = 1; procs < 6; procs++) {

            maxRec = procs - 1;
            System.out.print(procs);
            for (int i = 0; i < 10; i++) {
                start = System.currentTimeMillis();

                renderImage();

                end = System.currentTimeMillis();
                time = end - start;
                System.out.print(";" + time);
            }
            System.out.println("");
        }
    }

    void createScene() {
        scene = new Scene();
        scene.addCube(0, 35, 0, 10, 10, 10, new RGB(0.3f, 0, 0), 0.4f);       //rot, klein
        scene.addCube(-70, -20, -20, 20, 100, 100, new RGB(0f, 0, 0.3f), .4f);
        scene.addCube(-30, 30, 40, 20, 20, 20, new RGB(0, 0.4f, 0), 0.2f);        // gr�n, klein
        scene.addCube(50, -20, -40, 10, 80, 100, new RGB(.5f, .5f, .5f), 0.2f);
        scene.addCube(-70, -26, -40, 130, 3, 40, new RGB(.5f, .5f, .5f), 0.2f);


        Matrix mRx = Matrix.createRotation((float) (x_angle_factor * Math.PI / 16), "x");
        Matrix mRy = Matrix.createRotation((float) (y_angle_factor * Math.PI / 16), "y");
        Matrix mT = Matrix.createTranslation(0, 0, 200);
        Matrix m = mT.mult(mRx).mult(mRy);
        m.print();
        m.apply(scene.getTriangles());
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
