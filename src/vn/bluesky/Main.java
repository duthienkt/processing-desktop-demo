package vn.bluesky;

import processing.core.PApplet;
import vn.bluesky.agr.MainAct;
import vn.bluesky.engine.GameApplet;

public class Main extends GameApplet {

    public Main() {
        super(new MainAct());
    }

    @Override
    public void settings() {
        fullScreen( P2D);
    }

    @Override
    public void keyPressed() {
        super.keyPressed();
        if (keyCode == 'P')
            save(frameCount+".png");

    }

    public static void main(String[] args) {

	    PApplet.main(Main.class.getName(), new String[]{ "--present", "InterfaceOutAndroid" });
    }



}
