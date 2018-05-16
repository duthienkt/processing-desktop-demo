package vn.bluesky.engine;

import processing.core.PApplet;


public class GameApplet extends PApplet {
    /**
     * Stack of activity
     */

    private GActivity[] acts;
    private int nAct;
    private GActivity act;
    int lastUpdateTime;

    public GameApplet(GActivity root) {
        this.act = root;
    }

//    @Override
//    public void settings() {
//        fullScreen(P2D);
//    }

    @Override
    public void setup() {
        acts = new GActivity[40];
        nAct = 0;
        act.onAttach(this);
        act.onCreated();
        act.onResume();
        lastUpdateTime = millis();
    }

    public void pushActivity(GActivity act) {
        this.act.onPause();
        acts[nAct++] = this.act;
        this.act = act;
        act.onAttach(this);
        act.onCreated();
        act.onResume();
    }


    public void popActivity(GActivity act) {

        if (nAct <= 0) {

        } else
            if (this.act == act)
            {

            act.onPause();
            act.onStop();
            this.act = acts[--nAct];
            this.act.onResume();
        }
    }


    @Override
    public void draw() {
        int t = millis();
        act.update((t-lastUpdateTime)/1000.0f);
        lastUpdateTime = t;
        act.draw();
    }

    @Override
    public void mousePressed() {
        act.onMousePressed();
    }

    @Override
    public void mouseMoved() {
        act.onMouseMoved();
    }

    @Override
    public void mouseReleased() {
        act.onMouseReleased();
    }

    @Override
    public void mouseDragged() {
        act.onMouseMoved();
    }

    @Override
    public void keyPressed() {
        act.onKeyPressed(keyCode);
    }

    @Override
    public void keyReleased() {
        if (keyCode == ESC) exit();
        else
            act.onKeyReleased(keyCode);
    }
}
