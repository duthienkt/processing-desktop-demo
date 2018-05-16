package vn.bluesky.segment2D;


import vn.bluesky.engine.MathUtils;

import java.util.LinkedList;

/**
 * Created by DuThien on 30/05/2017.
 */
public class Segment2DMap {
    public final int width;
    public final int height;
    private Node root;

    public Segment2DMap(int w, int h) {
        int t = 1;
        while (t < w || t < h) t *= 2;
        width = t;
        height = t;
        root = new Node(null, 0, width - 1, 0, height - 1);
        root.value = 0;
    }

    public Node getRoot() {
        return root;
    }

    public Node find(double x, double y)
    {
        return root.findLeaf(x, y);
    }

    public int countVertex(int value) {
        return root.countVertex(value);
    }

    public int countEdge(int value) {
        return root.countEdge(value);
    }

    public int countCell(int value)
    {
        return root.countCell(value);
    }

    public void set(int value, int left, int right, int top, int bot) {
        root.setValue(value, left, right, top, bot);
    }


    public class Node {
        public final Node par;
        public final int left;
        public final int right;
        public final int top;
        public final int bot;
        public Node[] child = null;
        public int value;
        public LinkedList<Node> nextVertex = new LinkedList<>();

        public Node(Node par, int left, int right, int top, int bot) {
            this.par = par;
            this.left = left;
            this.right = right;
            this.top = top;
            this.bot = bot;
        }

        public boolean isLeaf() {
            return child == null;
        }

        public boolean isCell() {
            return left == right && top == bot;
        }

        public void setValue(int value, int left, int right, int top, int bot) {

            if (left > this.right) return;
            if (right < this.left) return;
            if (top > this.bot) return;
            if (bot < this.top) return;
            if (left < this.left) left = this.left;
            if (right > this.right) right = this.right;
            if (top < this.top) top = this.top;
            if (bot > this.bot) bot = this.bot;
            if (isLeaf() && this.value == value) return;

            if (isLeaf()) {
                if (this.left == left && this.right == right && this.top == top && this.bot == bot) {
                    this.value = value;

                } else {
                    this.child = createChild();
                    clearConnection();
                    for (int i = 0; i < 4; ++i)
                        child[i].setValue(value, left, right, top, bot);
                }
            } else {
                for (int i = 0; i < 4; ++i)
                    child[i].setValue(value, left, right, top, bot);
            }
            notifyMerge();
        }


        private Node inheritanceConnection(Node p) {
            for (Node v : p.nextVertex) {
                if (isNext(v)) {
                    connectTo(v);
                    v.connectTo(this);
                }
            }
            return this;
        }

        private void clearConnection() {
            int c = 0;
            for (Node v : nextVertex) {
                v.disconnectTo(this);
//                c++;
            }
//            System.out.println(c);
            nextVertex.clear();
        }

        private int countEdge(int value) {
            int res = 0;

            if (isLeaf()) {
                if (this.value == value) {
                    for (Node v : nextVertex)
                        if (v.value == value)
                            if (isLessIndexThan(v))
                                res++;
                }
            } else {
                for (Node d : child)
                    res += d.countEdge(value);
            }
            return res;
        }


        public void connectTo(Node v) {
            if (!nextVertex.contains(v))
                nextVertex.add(v);
        }

        public void disconnectTo(Node v) {
            if (nextVertex.contains(v)) nextVertex.remove(v);
        }

        public boolean isNext(Node v) {
            //top, bot
            if (elapse(left, right, v.left, v.right)) {
                if (bot + 1 == v.top || v.bot + 1 == top) return true;
            }

            if (elapse(top, bot, v.top, v.bot)) {
                if (right + 1 == v.left || v.right + 1 == left) return true;
            }
            return false;


        }

        private boolean elapse(int a1, int b1, int a2, int b2) {
            if (a1 > b2 || b1 < a2) return false;
            return true;
        }


        public Node findLeaf(double x, double y) {
            if (!contain(x, y)) return null;
            if (isLeaf()) return this;
            for (int i = 0; i <= 4; ++i) {
                Node r = child[i].findLeaf(x, y);
                if (r != null) return r;
            }
            return null;
        }


        public boolean contain(double x, double y) {
            return x >= left && x < right + 1 && y >= top && y < bot + 1;
        }

        private void notifyMerge() {
            if (isLeaf()) return;
            for (int i = 0; i < 4; ++i) {
                if (!child[i].isLeaf()) return;
                if (child[i].value != child[0].value) return;
            }
            value = child[0].value;
            clearConnection();
            for (int i = 0; i < 4; ++i) {
                inheritanceConnection(child[i]);
                child[i].clearConnection();
            }
            child = null;
        }

        public Node[] createChild() {
            /**
             *      0      1
             *          +
             *      2      3
             */

            if (isCell()) return null;
            int midI = (top + bot) / 2;
            int midJ = (left + right) / 2;
            Node[] res = new Node[]{
                    new Node(this, left, midJ, top, midI).inheritanceConnection(this),
                    new Node(this, midJ + 1, right, top, midI).inheritanceConnection(this),
                    new Node(this, left, midJ, midI + 1, bot).inheritanceConnection(this),
                    new Node(this, midJ + 1, right, midI + 1, bot).inheritanceConnection(this),
            };
            res[0].connectTo(res[1]);
            res[1].connectTo(res[0]);

            res[0].connectTo(res[2]);
            res[2].connectTo(res[0]);
            res[1].connectTo(res[3]);
            res[3].connectTo(res[1]);

            res[2].connectTo(res[3]);
            res[3].connectTo(res[2]);
            for (int i = 0; i< 4; ++i)
                res[i].value = value;
            return res;

        }


        public double distance(double x, double y) {
            return Math.min(
                    Math.min(
                            MathUtils.distance(x, y, (double) left, (double) top),
                            MathUtils.distance(x, y, (double) left, (double) bot)),
                    Math.min(
                            MathUtils.distance(x, y, (double) right, (double) top),
                            MathUtils.distance(x, y, (double) right, (double) bot))
            );
        }

        public double edgeWeights(Node d) {
            double x = (left + right) / 2;
            double x1 = (d.left + d.right) / 2;

            double y = (top + bot) / 2;
            double y1 = (d.top + d.bot) / 2;
            return MathUtils.distance(x, y, x1, y1);

        }

        public int countVertex(int value) {
            if (isLeaf()) {
                if (this.value == value) return 1;
                else return 0;
            }
            int res = 0;
            for (Node v : child) {
                res += v.countVertex(value);
            }
            return res;
        }

        public int countCell(int value)
        {
            if (isLeaf()) {
                if (this.value == value) return (right-left+1)*(bot-top+1);
                else return 0;
            }
            int res = 0;
            for (Node v : child) {
                res += v.countCell(value);
            }
            return res;
        }


        public boolean isLessIndexThan(Node v) {
            if (top != v.top)
                return top < v.top;
            else return left < v.left;
        }

        private String keyS =null;
        @Override
        public String toString() {
            if (keyS == null)
                keyS = "["+left+","+right+","+top+","+bot+"]";
            return keyS;
        }
    }


}
