package vn.bluesky.engine;

/**
 * Created by DuThien on 20/05/2017.
 */

public class GActivity  {
    public GameApplet p;

    public final void onAttach(GameApplet p) {
        this.p = p;
    }

    public void onCreated() {

    }

    public void onResume() {

    }

    public void onPause() {

    }

    public void onStop() {

    }


    public final void finish() {
        p.popActivity(this);
    }

    public final void startActivity(GActivity act) {
        p.pushActivity(act);
    }


    public void draw() {

    }

    public void update(float deltaTime) {

    }



    public boolean onMousePressed() {
        return false;
    }


    public boolean onMouseReleased() {
        return false;
    }


    public void onMouseMoved() {

    }

    public void onKeyPressed(int keyCode)
    {

    }

    public void onKeyReleased(int keyCode)
    {

    }


}
