package vn.bluesky.agr;

import processing.core.PConstants;
import vn.bluesky.engine.GActivity;
import vn.bluesky.engine.GTextButton;
import vn.bluesky.segment2D.Dijkstra;
import vn.bluesky.segment2D.Segment2DFull;
import vn.bluesky.segment2D.Segment2DMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DuThien on 01/06/2017.
 */
public class App extends GActivity{

    Segment2DMap map;
    String logText = "";

    int d = 0;
    float ofsX = 0;
    float ofsY = 0;
    float hView = 1.4f;

    GTextButton addbt, removebt, backbt, dijkstrabt;


    Tool tool = new AddTool();
    GTextButton lastClick = null;

    @Override
    public void onCreated() {
        map = new Segment2DMap(512, 512);
        addbt = new GTextButton(p, p.width - 200, p.height - 400, 190, 50)
                .setText("ADD")
                .setOnClickListener(() -> {
                            tool = new AddTool();
                            addbt.active = true;
                            removebt.active = false;
                            dijkstrabt.active = false;
                        }
                );
        removebt = new GTextButton(p, p.width - 200, p.height - 400 + 60, 190, 50)
                .setText("REMOVE")
                .setOnClickListener(() -> {
                            tool = new RemoveTool();
                            addbt.active = false;
                            removebt.active = true;
                            dijkstrabt.active = false;
                        }
                );

        dijkstrabt = new GTextButton(p, p.width-200, p.height-400+ 120, 190, 50)
                .setText("Dijkstra").setOnClickListener(
                        () -> {
                            tool = new DijkstraTool();
                            addbt.active = false;
                            removebt.active = false;
                            dijkstrabt.active = true;
                        }
                );
        backbt = new GTextButton(p, p.width - 200, p.height - 60, 190, 50)
                .setText("BACK")
                .setOnClickListener(() -> {
                            finish();
                        }
                );

    }

    @Override
    public void draw() {
        p.background(0);
        int d = 0;

        drawGround(map.getRoot(), ofsX, ofsY, hView);
        drawEdge(map.getRoot(), ofsX, ofsY, hView);
        p.noStroke();
        p.fill(255, 0, 10);
        p.textSize(40);
        p.text(logText, 800, 50);

        addbt.draw();
        removebt.draw();
        dijkstrabt.draw();
        backbt.draw();
        tool.onDraw();
    }

    public void drawGround(Segment2DMap.Node node, float x, float y, float h) {
        if (node.isLeaf()) {
            p.fill(255 / (node.value + 1));
            if (node.value == 0)
                p.stroke(0, 255, 122);
            else
                p.stroke(200, 0, 0);
            p.strokeWeight(0.5f);
            p.rect(x + node.left * h, y + node.top * h, (node.right - node.left + 1) * h - 1, (node.bot - node.top + 1) * h - 1);
            p.strokeWeight(1);


        } else
            for (int i = 0; i < 4; ++i)
                drawGround(node.child[i], x, y, h);

    }

    public void drawEdge(Segment2DMap.Node node, float x, float y, float h) {

        if (node.isLeaf()) {
            if (node.value == 0) {
                p.stroke(100, 100, 255);
                for (Segment2DMap.Node v : node.nextVertex) {
                    if (
                            node.isLessIndexThan(v) &&
                                    v.value == 0)
                        p.line(x + (node.left + node.right + 1) * h / 2, y + (node.top + node.bot + 1) * h / 2,
                                x + (v.left + v.right + 1) * h / 2, y + (v.top + v.bot + 1) * h / 2);

                }
            }

        } else
            for (int i = 0; i < 4; ++i)
                drawEdge(node.child[i], x, y, h);

    }

    @Override
    public boolean onMousePressed() {

        lastClick = null;
        if (addbt.onPressed() == true)
            lastClick = addbt;
        if (removebt.onPressed() == true)
            lastClick = removebt;
        if (backbt.onPressed() == true)
            lastClick = backbt;
        if (dijkstrabt.onPressed() == true)
            lastClick = dijkstrabt;
        if (lastClick != null) return true;
        tool.onPressed();
        return true;
    }

    @Override
    public boolean onMouseReleased() {
        boolean res = false;
        if (lastClick != null) {
            res = lastClick.onReleased();
            lastClick = null;
        }
        else
            tool.onReleased();
        return true;
    }

    @Override
    public void onKeyPressed(int keyCode) {
//        System.out.println(keyCode);
        switch (keyCode) {
            case 38:
                ofsY += 10;
                break;
            case 37:
                ofsX += 10;
                break;
            case 40:
                ofsY -= 10;
                break;
            case 39:
                ofsX -= 10;
                break;
            case '.':
                hView = hView * 1.01f + 0.01f;
                break;
            case ',':
                hView = hView / 1.01f + 0.01f;
                break;

        }
        tool.onKeyPressed(keyCode);
    }


    interface Tool {
        void onPressed();

        void onReleased();

        void onDraw();

        void onKeyPressed(int keyCode);
    }

    class AddTool implements Tool {
        int bw = 27, bh = 27;

        @Override
        public void onPressed() {

        }

        @Override
        public void onReleased() {
            int t = p.millis();
            int x = (int) ((p.mouseX - ofsX) / hView);
            int y = (int) ((p.mouseY - ofsY) / hView);

            map.set(1, x, x + bw - 1, y, y + bh - 1);
            logText = "UpdateTime :" + (p.millis() - t) + "(ms)\nVertex : " + map.getRoot().countVertex(0) +
                    "\n Edge : " +
                    map.countEdge(0)
                    + "\n Free space : " + map.countCell(0);

        }

        @Override
        public void onDraw() {
            if (!p.mousePressed) {
                p.stroke(0, 0, 255);
            } else
                p.stroke(255, 0, 0);
            p.noFill();
            p.rect(p.mouseX, p.mouseY, bw * hView, bh * hView);
            p.fill(0, 200, 0);
            p.textSize(40);
            p.text("Block " + bw + " x " + bh, 200, p.height - 5);
        }

        @Override
        public void onKeyPressed(int keyCode) {
            if (keyCode == 'w' || keyCode == 'W') bh = (int) (p.max(bh / 1.1f, +1));
            if (keyCode == 's' || keyCode == 'S') bh = (int) (bh * 1.1f);
            if (keyCode == 'a' || keyCode == 'A') bw = (int) (p.max(bw / 1.1f, +1));
            if (keyCode == 'd' || keyCode == 'D') bw = (int) (bw * 1.1 + 1);
        }
    }

    class RemoveTool extends AddTool {
        @Override
        public void onReleased() {
            int t = p.millis();
            int x = (int) ((p.mouseX - ofsX) / hView);
            int y = (int) ((p.mouseY - ofsY) / hView);

            map.set(0, x, x + bw - 1, y, y + bh - 1);
            logText = "UpdateTime :" + (p.millis() - t) + "(ms)\nVertex : " + map.getRoot().countVertex(0) +
                    "\n Edge : " +
                    map.countEdge(0)
                    + "\n Free space : " + map.countCell(0);

        }
    }

    boolean lock = false;
    class Unit
    {
        double sx, sy, dx, dy;
        long time = 0;
        List<Segment2DMap.Node> path = null;
        Unit (double x, double y){
            sx = x;
            sy = y;
        }
        void setDest(double x, double y)
        {
            path = null;
            dx = x;
            dy = y;
        }
        void findPath(Dijkstra engine){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (engine) {
                        logText = "Running...";
                        int t = p.millis();
                        engine.startPoint(dx, dy).endPoint(sx, sy);
                        engine.run();
                        logText = "Get result";
                        path = engine.getWay();
                        t -= p.millis();
                        t = -t;
                        time = t;
                        logText = "Dijkstra : " + t + "(ms)";
                        if (path == null) logText += "\nNull result";
                    }
                    lock = false;
                }
            }).start();
        }
        void  onDraw(){

            if (path == null) return;

            p.noStroke();

            for (Segment2DMap.Node node : path)
            {

                p.noStroke();
                p.fill(200, 200, 0, 100);
                p.rect(ofsX + node.left * hView, ofsY + node.top * hView,
                        (node.right - node.left + 1) * hView - 1, (node.bot - node.top + 1) * hView - 1);


            }

            p.stroke(0,255, 255);
            p.fill(100, 200, 0);
            p.ellipse(ofsX + (float) sx * hView, ofsY + (float)sy * hView,
                    10, 10);

            p.fill(255, 0, 255);
            p.ellipse(ofsX + (float) dx * hView, ofsY + (float)dy * hView,
                    10, 10);

        };

        void drawPath()
        {
            if (path== null) return;
            Segment2DMap.Node last = null;
            for (Segment2DMap.Node node : path)
            {
                if (last!= null)
                {
                    p.strokeWeight(4);
                    p.stroke(0, 255, 0, 100);
                    p.line(ofsX + (float)( last.left+last.right)/2* hView, ofsY + (float)(last.bot+last.top)/2*hView,
                            ofsX + (float)( node.right+node.left)/2* hView, ofsY + (float)(node.bot+node.top)/2*hView);
                    p.strokeWeight(1);
                }
                last = node;

            }
        }

    }

    class DijkstraTool implements Tool {
        Dijkstra engine = new Dijkstra(map);
        double sx, sy, dx, dy;
        Segment2DMap.Node sNode = null;
        Segment2DMap.Node dNode = null;
        long sumTime = -1;
        List<Unit> units = new ArrayList<>();
        @Override
        public void onPressed() {
            int x = (int) ((p.mouseX - ofsX) / hView);
            int y = (int) ((p.mouseY - ofsY) / hView);
            sx = x;
            sy = y;
            sNode = map.find(sx, sy);
        }

        @Override
        public void onReleased() {
            if (lock) return;
            int x = (int) ((p.mouseX - ofsX) / hView);
            int y = (int) ((p.mouseY - ofsY) / hView);
            dx = x;
            dy = y;
            lock = true;
            dNode = map.find(dx, dy);
            if ( dNode== null || sNode== null) return;

            Unit u = new Unit( sx, sy);
            u.setDest( dx, dy);
            u.findPath(engine);
            synchronized (units) {
                units.clear();
                units.add(u);

            }
        }

        @Override
        public void onDraw() {
            if (sNode != null)
            {
                p.stroke(255, 255, 100);
                p.noFill();
                p.rect(ofsX + sNode.left * hView, ofsY + sNode.top * hView,
                        (sNode.right - sNode.left + 1) * hView - 1, (sNode.bot - sNode.top + 1) * hView - 1);
            }

            if (dNode != null)
            {
                p.stroke(255, 255, 100);
                p.noFill();
                p.rect(ofsX + dNode.left * hView, ofsY + dNode.top * hView,
                        (dNode.right - dNode.left + 1) * hView - 1, (dNode.bot - dNode.top + 1) * hView - 1);
            }
            synchronized (units){
                sumTime = 0;
                for (Unit u: units) {
                    u.onDraw();
                    sumTime+= u.time;
                }
                for (Unit u: units)
                    u.drawPath();
            }
            if (units.size()>0)
            {
                p.noFill();
                p.stroke(255);
                p.pushStyle();
                p.textAlign(PConstants.RIGHT);
                p.text( units.size()+" unit : "+ sumTime + " ms", p.width-20, 200 );
                p.popStyle();
            }

        }

        @Override
        public void onKeyPressed(int keyCode) {

            if (keyCode == 85)
                if (dNode!= null)
                {
                    if (lock) return;
                    int x = (int) ((p.mouseX - ofsX) / hView);
                    int y = (int) ((p.mouseY - ofsY) / hView);
                    sx = x;
                    sy = y;
                    sNode = map.find(sx, sy);
                    if ( dNode== null || sNode== null) return;
                    Unit u = new Unit((float) sx,(float) sy);
                    u.setDest((float) dx,(float) dy);
                    u.findPath(engine);
                    synchronized (units) {
                        units.add(u);
                    }
                }
        }
    }

}
