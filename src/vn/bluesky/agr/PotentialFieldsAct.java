package vn.bluesky.agr;

import processing.core.PVector;
import vn.bluesky.engine.GActivity;
import vn.bluesky.engine.GTextButton;

/**
 * Created by DuThien on 31/05/2017.
 */
public class PotentialFieldsAct extends GActivity {
    PVector[] Ps = new PVector[20];
    float Pr = 4;
    float As = 150;
    PVector A = new PVector(400, 400);
    float Ar = 30;
    float Aalpha = 0.3f;


    PVector R = new PVector(400, 500);
    float Rr = 40;
    float Rs = 260;
    float Rbeta = 0.2f;
    GTextButton backbt;

    @Override
    public void onCreated() {
        for (int i = 0; i < Ps.length; ++i) {
            Ps[i] = new PVector(p.random(0, p.width), p.random(0, p.height));
        }
        backbt = new GTextButton(p, p.width - 200, p.height - 60, 190, 50)
                .setText("BACK")
                .setOnClickListener(() -> {
                            finish();
                        }
                );
    }

    @Override
    public void update(float deltaTime) {
        if (deltaTime > 0.1f) deltaTime = 0.1f;

        if (p.keyPressed) {
            switch (p.key) {
                case 'a':
                    R.x -= 100 * deltaTime;
                    break;
                case 's':
                    R.y += 100 * deltaTime;
                    break;
                case 'd':
                    R.x += 100 * deltaTime;
                    break;
                case 'w':
                    R.y -= 100 * deltaTime;
                    break;
            }
        }
        for (PVector P : Ps) {
            PVector d = attractive(A, Ar, As, P);
            float dx, dy;
            dx = d.x;
            dy = d.y;

            d = reject(R, Rr, Rs, P);
            dx += d.x;
            dy += d.y;
            {
                P.x += dx * 1 * deltaTime;
                P.y += dy * 1 * deltaTime;
            }

            for (PVector p1: Ps)
                if (p1!= P)
                {
                    d = reject(p1, 18, 19, P);
                    dx += d.x;
                    dy += d.y;
                    {
                        P.x += dx * 0.05 * deltaTime;
                        P.y += dy * 0.05 * deltaTime;
                    }
                }
        }

        p.background(0);
        p.noStroke();
        p.fill(100, 100, 255, 30);
        p.ellipse(A.x, A.y, Ar * 2 + As * 2, Ar * 2 + As * 2);

        p.fill(100, 100, 255);
        p.stroke(0, 0, 250);
        p.ellipse(A.x, A.y, Ar * 2, Ar * 2);


        p.stroke(255, 0, 250);
        p.fill(25, 200, 255);
        for (PVector P : Ps)
            p.ellipse(P.x, P.y, Pr * 2, Pr * 2);


        p.noStroke();
        p.fill(25, 100, 250, 30);
        p.ellipse(R.x, R.y, Rr * 2 + Rs * 2, Rr * 2 + Rs * 2);
        p.stroke(25, 100, 250);
        p.fill(250, 0, 0);
        p.ellipse(R.x, R.y, Rr * 2, Rr * 2);
        p.stroke(0, 255, 0);
        for (int i = 0; i < p.height / 20; i++)
            for (int j = 0; j < p.width / 20; j++) {
                PVector Q = new PVector(j * 20, i * 20);
                PVector d = attractive(A, Ar, As, Q);
                float dx = d.x;
                float dy = d.y;
                d = reject(R, Rr, Rs, Q);
                dx += d.x;
                dy += d.y;
                p.ellipse(Q.x, Q.y, 2, 2);
                p.line(Q.x, Q.y, (float) (Q.x + dx * 0.1), (float) (Q.y + dy * 0.1));
            }

        backbt.draw();
    }

    @Override
    public void onMouseMoved() {
        A = new PVector(p.mouseX, p.mouseY);
    }


    @Override
    public boolean onMousePressed() {
        backbt.onPressed();
        return true;
    }

    @Override
    public boolean onMouseReleased() {
        backbt.onReleased();
        return true;
    }

    /////////////////////////////////////////////
    PVector attractive(PVector G, float r, float s, PVector P) {
        float w = p.atan2((G.y - P.y), (G.x - P.x));
        float d = p.sqrt((G.y - P.y) * (G.y - P.y) + (G.x - P.x) * (G.x - P.x));
        float dx, dy;
        if (d < r) {
            dx = 0;
            dy = -0;
        } else if (r <= d && d <= s + r) {
            dx = (d - r) * p.cos(w);
            dy = (d - r) * p.sin(w);
        } else {
            dx = s * p.cos(w);
            dy = s * p.sin(w);
        }
        return new PVector(dx, dy);
    }


    float sign(float x) {
        if (x < 0) return -1;
        if (x > 0) return 1;
        return 0;
    }

    PVector reject(PVector G, float r, float s, PVector P) {
        float w = p.atan2((G.y - P.y), (G.x - P.x));
        float d = p.sqrt((G.y - P.y) * (G.y - P.y) + (G.x - P.x) * (G.x - P.x));
        float dx, dy;
        if (d < r) {
            dx = -sign(p.cos(w)) * 100;
            dy = -sign(p.sin(w)) * 100;
        } else if (r <= d && d <= s + r) {
            dx = -(s + r - d) * p.cos(w);
            dy = -(s + r - d) * p.sin(w);
        } else {
            dx = 0;
            dy = 0;
            ;
        }
        return new PVector(dx, dy);
    }
}
