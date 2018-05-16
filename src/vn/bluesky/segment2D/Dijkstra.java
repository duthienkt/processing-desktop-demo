package vn.bluesky.segment2D;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by DuThien on 31/05/2017.
 */
public class Dijkstra {
    public final Segment2DMap map;
    ResultNode[][] Q;
    private double startX, startY;
    private double destX, destY;
    private int title = 0;
    private PriorityQueue<ResultNode> queue;
    boolean cal = false;
    public Dijkstra(Segment2DMap map) {
        queue = new PriorityQueue<>((o1, o2) -> {
            if (o1.val*Math.log(o1.count) +o1.node.distance(destX, destY) < o2.val*Math.log(o2.count)+ o2.node.distance(destX, destY)) return -1;
            return 1;
        });
        this.map = map;
        Q = new ResultNode[map.height][];
        for (int i = 0; i < map.height; ++i)
            Q[i] = new ResultNode[map.width];
        renew();
    }

    public Dijkstra renew() {
        cal= false;
        for (int i = 0; i < map.height; ++i)
            for (int j = 0; j < map.width; ++j)
                Q[i][j] = null;
        queue.clear();
        res = null;
        return this;
    }

    public Dijkstra startPoint(double x, double y) {
        if (map.find(x, y) != map.find(startX, startY)) renew();
        startX = x;
        startY = y;
//        renew();

        return this;
    }

    public Dijkstra endPoint(double x, double y) {

        destX = x;
        destY = y;
        res = null;
        return this;
    }

    public Dijkstra freeValue(int t) {
        title = t;
        return this;
    }

    public void run() {

        Segment2DMap.Node st = map.find(startX, startY);
        Segment2DMap.Node ds = map.find(destX, destY);
        if (st == null || ds == null) return;
        if (st.value != title || ds.value != title) return;
        if (cal)
        {

        }
        else
        {
            queue.add(new ResultNode(st, 0));
        }
        if (Q[ds.top][ds.left]!= null)
        if(!Q[ds.top][ds.left].free) return;
        ResultNode u;
        while (!queue.isEmpty()) {
            u = queue.poll();

            Q[u.node.top][u.node.left] = u;
            u.free = false;
            if (u.node == ds) {
                return;
            }
            for (Segment2DMap.Node v : u.node.nextVertex) {

                if (v.value != title) continue;
                double l = u.node.edgeWeights(v) + u.val;
                if (Q[v.top][v.left] != null) {
                    if (!Q[v.top][v.left].free) continue;
                    if (l >= Q[v.top][v.left].val) continue;
                }
                ResultNode t = new ResultNode(v, l, u.node.left, u.node.top);
                queue.add(t);
            }
        }
    }

    private List<Segment2DMap.Node> res = null;
    public List<Segment2DMap.Node> getWay() {
        if (res!= null) return res;
        Segment2DMap.Node u = map.find(destX, destY);
        if (u == null) return null;
        ResultNode node = Q[u.top][u.left];
        if (node == null) return null;
        res = new LinkedList<>();

        while (node.trackLeft >= 0) {
            res.add(node.node);
            node = Q[node.trackTop][node.trackLeft];
        }
        res.add(node.node);
        return res;
    }

    class ResultNode {
        final Segment2DMap.Node node;
        public double val = 1999999999;
        int trackLeft;
        int trackTop;
        boolean free = true;
        int count ;
        public ResultNode(Segment2DMap.Node node, double val, int trackLeft, int trackTop) {
            count = Q[trackTop][trackLeft].count+1;
            this.node = node;
            this.val = val;
            this.trackLeft = trackLeft;
            this.trackTop = trackTop;
        }

        public ResultNode(Segment2DMap.Node node, double val) {
            count = 1;
            this.node = node;
            this.val = val;
            this.trackLeft = -1;
            this.trackTop = -1;
        }
    }
}
