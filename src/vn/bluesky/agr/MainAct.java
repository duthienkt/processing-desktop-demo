package vn.bluesky.agr;

import processing.core.PApplet;
import processing.core.PFont;
import vn.bluesky.engine.GActivity;
import vn.bluesky.engine.GTextButton;

/**
 * Created by DuThien on 31/05/2017.
 */
public class MainAct extends GActivity {
    private GTextButton mapbt;
    private GTextButton collisbt;
    private GTextButton potenbt;
    private GTextButton posterbt;


    PFont timesFont_80;
    PFont timesFont_38;
    Background b;

    @Override
    public void onCreated() {
        p.smooth();
        mapbt = new GTextButton(p, p.width/2-200, p.height/2, 400, 60)
                .setText("Segment 2D Map")
                .setOnClickListener(() -> {startActivity(new MapAct());});

        collisbt = new GTextButton(p, p.width/2-200, p.height/2+ 100, 400, 60)
                .setText("Segment 2D Matrix")
                .setOnClickListener(() -> {startActivity(new MatrixAct());});
        potenbt = new GTextButton(p, p.width/2-200, p.height/2+ 200, 400, 60)
                .setText("Potential Fields")
                .setOnClickListener(() -> {startActivity(new PotentialFieldsAct());});
        posterbt = new GTextButton(p, p.width/2-200, p.height/2+ 300, 400, 60)
                .setText("Exit")
                .setOnClickListener(() -> {
//            startActivity(new App());
                    p.exit();
        });
        timesFont_80 = p.loadFont("data/times_80.vlw");
        timesFont_38 = p.loadFont("data/times_38.vlw");
        b = new Background();
        b.setup();
    }

    GTextButton lastClick = null;


    @Override
    public void draw() {
        p.background(0);
        b.draw();
        p.noStroke();
        p.fill(255, 10);
        p.rect(0, 0, p.width, p.height);
        p.pushStyle();
        p.textFont(timesFont_80);
        p.textSize(80);
        p.fill(255);
        p.noStroke();
        p.textAlign(PApplet.CENTER,PApplet.CENTER);
        p.text("THỰC TẬP TỐT NGHIỆP", p.width/2, 70);
        p.textSize(44);
        p.textFont(timesFont_38);
        p.text("Đề tài: Tìm đường đa đối tượng trong môi trường động", p.width/2+100, 170);
        p.pushMatrix();
            p.translate(p.width/2+100, 280);
            p.shearX(-0.2f);
            p.text("Giáo viên hướng dẫn: Vương Bá Thịnh\n       Sinh viên thực hiện: Phạm Quốc Du Thiên", 0, 0);

        p.popMatrix();
        p.popStyle();
        mapbt.draw();
        potenbt.draw();
        collisbt.draw();
        posterbt.draw();
        if (p.frameCount == 200 ) p.save("demo.png");
    }

    @Override
    public boolean onMousePressed() {
        b.mousePressed();
        lastClick = null;
        if (mapbt.onPressed() == true)
            lastClick = mapbt;
        if (collisbt.onPressed() == true)
            lastClick = collisbt;
        if (potenbt.onPressed() == true)
            lastClick = potenbt;
        if (posterbt.onPressed() == true)
            lastClick = posterbt;

        return lastClick != null;
    }

    @Override
    public boolean onMouseReleased() {
        b.mouseReleased();
        boolean res =  false;
        if (lastClick != null){
            res =  lastClick.onReleased();
            lastClick = null;
        }
        return res;
    }



    class Background{
        int count = 40;

        int maxSize = 100;
        int minSize = 20;
                float[][] e = new float[count][5];
        float ds=2;
        boolean dragging=false;
        int lockedCircle;
        int lockedOffsetX;
        int lockedOffsetY;
        void mousePressed () {

            for (int j=0;j< count;j++) {
                // If the circles are close...
                if (p.sq(e[j][0] - p.mouseX) + p.sq(e[j][1] - p.mouseY) < p.sq(e[j][2]/2)) {
                    // Store data showing that this circle is locked, and where in relation to the cursor it was
                    lockedCircle = j;
                    lockedOffsetX = p.mouseX - (int)e[j][0];
                    lockedOffsetY = p.mouseY - (int)e[j][1];
                    // Break out of the loop because we found our circle
                    dragging = true;
                    break;
                }
            }
        }
        // If user releases mouse...
        void mouseReleased() {
            // ..user is no-longer dragging
            dragging=false;
        }

        // Set up canvas
        void setup() {
            for (int j=0;j< count;j++) {
                e[j][0]=p.random(p.width); // X
                e[j][1]=p.random(p.height); // Y
                e[j][2]=p.random(minSize, maxSize); // Radius
                e[j][3]=p.random(-.12f, .12f); // X Speed
                e[j][4]=p.random(-.12f, .12f); // Y Speed
            }
        }

        // Begin main draw loop (called 25 times per second)
        void draw() {
            // Fill background black
           p.strokeWeight(1);
            // Begin looping through circle array
            for (int j=0;j< count;j++) {
                // Disable shape stroke/border
                p.noStroke();
                // Cache diameter and radius of current circle
                float radi=e[j][2];
                float diam=radi/2;
                if (p.sq(e[j][0] - p.mouseX) + p.sq(e[j][1] - p.mouseY) < p.sq(e[j][2]/2))
                    p.fill(64, 187, 128, 100); // green if mouseover
                else
                    p.fill(64, 128, 187, 100); // regular
                if ((lockedCircle == j && dragging)) {
                    // Move the particle's coordinates to the mouse's position, minus its original offset
                    e[j][0]=p.mouseX-lockedOffsetX;
                    e[j][1]=p.mouseY-lockedOffsetY;
                }
                // Draw circle
                p.ellipse(e[j][0], e[j][1], radi, radi);
                // Move circle
                e[j][0]+=e[j][3];
                e[j][1]+=e[j][4];


    /* Wrap edges of canvas so circles leave the top
     and re-enter the bottom, etc... */
                if ( e[j][0] < -diam      ) {
                    e[j][0] = p.width+diam;
                }
                if ( e[j][0] > p.width+diam ) {
                    e[j][0] = -diam;
                }
                if ( e[j][1] < 0-diam     ) {
                    e[j][1] = p.height+diam;
                }
                if ( e[j][1] > p.height+diam) {
                    e[j][1] = -diam;
                }

                // If current circle is selected...
                if ((lockedCircle == j && dragging)) {
                    // Set fill color of center dot to white..
                    p.fill(255, 255, 255, 255);
                    // ..and set stroke color of line to green.
                    p.stroke(128, 255, 0, 100);
                }
                else {
                    // otherwise set center dot color to black..
                    p.fill(0, 0, 0, 255);
                    // and set line color to turquoise.
                    p.stroke(64, 128, 128, 255);
                }

                // Loop through all circles
                for (int k=0;k< count;k++) {
                    // If the circles are close...
                    if ( p.sq(e[j][0] - e[k][0]) + p.sq(e[j][1] - e[k][1]) < p.sq(diam) ) {
                        // Stroke a line from current circle to adjacent circle
                        p.line(e[j][0], e[j][1], e[k][0], e[k][1]);
                    }
                }
                // Turn off stroke/border
                p.noStroke();
                // Draw dot in center of circle
                p.rect(e[j][0]-ds, e[j][1]-ds, ds*2, ds*2);
            }
        }
    }
}
