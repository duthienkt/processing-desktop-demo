package vn.bluesky.agr;

import vn.bluesky.engine.GActivity;
import vn.bluesky.engine.GTextButton;
import vn.bluesky.engine.MathUtils;
import vn.bluesky.segment2D.Segment2DFull;

/**
 * Created by DuThien on 31/05/2017.
 */
public class MatrixAct extends GActivity {
    Segment2DFull <Unit>seg;
    Unit []us;
    GTextButton backbt;
    @Override
    public void onCreated() {
        backbt = new GTextButton(p, p.width - 200, p.height - 60, 190, 50)
                .setText("BACK")
                .setOnClickListener(() -> {
                            finish();
                        }
                );
        seg = new Segment2DFull(10);
        us = new Unit[900];
        for (int i = 0; i< us.length; ++i)
        {
                us[i] = new Unit(seg, p.random(5, 1020),p.random(5, p.height-15) ,
                        p.random(-100, 100),  p.random(-100, 100), p.random(5, 40) );

        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void update(float deltaTime) {
        if (deltaTime>1/30f) deltaTime = 1/30f;
        for (int i = 0; i< us.length; ++i)
            us[i].update(deltaTime);
    }
    int logvs = 0;
    int logc = 0;
    @Override
    public void draw() {
        p.background(0);
        p.fill(255);
        p.stroke(10, 0, 255);
        p.rect(0, 0, 1024, p.height-1);
        for (int i = 0; i< us.length; ++i)
            us[i].draw();
        p.stroke(255, 0, 255);
        logvs = 0;
        logc = 0;
        for (int i = 0; i< us.length; ++i)
        {
            seg.forAll(us[i].i, us[i].j,us[i].r, (item, i1, j, param) ->
            {
                Unit u = (Unit) param;
                logc++;
//                if (u.hashCode()< item.hashCode())
                {
                    p.line(item.x, item.y, u.x, u.y);
                }
            }, us[i]);
            logvs+= seg.vs_count;
        }

        p.textSize(40);
        p.text("Visit " + logvs+ " nodes\n" +
                "H = "+ ((us.length*(us.length-1)/2.0)/logvs)+"\n"+ logc+ " collisions", 1050, 80);
        backbt.draw();
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

    @Override
    public void onMouseMoved() {

    }

    @Override
    public void onKeyPressed(int keyCode) {

    }

    @Override
    public void onKeyReleased(int keyCode) {

    }


    class Unit {
        public float x, y, dx, dy, r;
        public int i, j;
        private Segment2DFull seg;
        boolean active = false;
        public Unit(Segment2DFull seg, float x, float y, float dx, float dy, float r )
        {
            this.seg = seg;
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
            this.r = r;
            map2Matrix();
            seg.set(this, i, j);
        }
         int di[] = {-1, -1, 0, 1, 1, 1, 0, -1};
         int dj[] = {0, 1, 1, 1, 0, -1, -1, -1};

        private void map2Matrix(){
            i = (int)Math.round(y);
            j = (int)Math.round(x);

            if (seg.count(i, j)>=1)
            {
                int ii = i, jj = j;
                for (int k = 0; k< 8; ++k)
                {
                    i = ii+ di[k];
                    j = jj+ dj[k];
                    if (i<0 || j< 0|| i>=1024|| j>=1024) continue;
                    if (seg.count(i, j) == 0) return;
                }
            }

        }
        public void update(float deltaTime)
        {
            int li = i;
            int lj = j;
            if (x<=5) {
                x = 5;
                dx = p.abs(dx);
            }
            if (y<=5) {
                y = 5;
                dy = p.abs(dy);
            }

            if (x>=1000) {
                x = 1000;
                dx = -p.abs(dx);
            }
            if (y>=p.height-5) {
                y = p.height-5;
                dy = -p.abs(dy);
            }
            x+=dx*deltaTime;
            y+=dy*deltaTime;
            map2Matrix();
            seg.move(li, lj, i, j);
        }

        public void draw()
        {
            if (active)
                p.fill(255, 0, 0);
            else
                p.fill(0, 100, 255);
            p.stroke(80);
            p.ellipse(x, y, 5, 5);
        }

        public void setActive(boolean b){

        }
    };
}
