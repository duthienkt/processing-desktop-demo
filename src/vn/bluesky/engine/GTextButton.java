package vn.bluesky.engine;

import processing.core.PApplet;

/**
 * Created by DuThien on 31/05/2017.
 */
public class GTextButton {
    public int borderColor = 0xffffffff;
    public int backgroundHover = 0xffffffff;
    public int background = 0x10ffffff;
    public int backgroundActive = 0xff83DAF7;
    public int textColor = 0xffADF7C7;
    public boolean active = false;
    public float width, height;
    public float x, y;
    public GameApplet p;
    String text = "";
    Runnable onClickListener = null;

    public GTextButton(GameApplet p, float x, float y, float w, float h) {
        this.p = p;
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    public GTextButton setOnClickListener(Runnable onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    public GTextButton setText(String text) {
        this.text = text;
        return this;
    }

    public void draw() {
        if (active)
            p.fill(backgroundActive);
        else
            p.fill(isHover() ? backgroundHover : background);
        p.stroke(borderColor);
        p.rect(x, y, width, height, 3);
        p.pushStyle();
        p.textAlign(PApplet.CENTER, PApplet.CENTER);
        p.textSize(height/1.5f);
        p.noStroke();
        p.fill(textColor);
        p.text(text, x+width/2, y+height/2);
        p.popStyle();
    }

    public boolean isHover() {
        return p.mouseX <= x + width && p.mouseX >= x && p.mouseY <= y + height && p.mouseY >= y;
    }


    public boolean onPressed() {
        return isHover();
    }

    public boolean onReleased() {
        if (isHover())
            if (onClickListener != null)
                onClickListener.run();
        return true;

    }

}
