package vn.bluesky.segment2D;

/**
 * Created by DuThien on 14/06/2017.
 */
public class Optimizer {

    public static class AngleSeg
    {
        double a, b;
        public AngleSeg(double a, double b)
        {
            while (a<0) a+= Math.PI*2;
            while (b<0) b+= Math.PI*2;
            while (a<0) a+= Math.PI*2;
            while (b<0) b+= Math.PI*2;
            while (a>=Math.PI*2) a-= Math.PI*2;
            while (b>=Math.PI*2) b-= Math.PI*2;
            if (a>b)
            {
                double t = a;
                a = b;
                b = t;
            }
            if (b-a> Math.PI)
            {
                a+= Math.PI*2;
                double t = a;
                a = b;
                b = t;
            }
            this.a = a;
            this.b = b;
        }

        public AngleSeg intersect(AngleSeg Y)
        {
            AngleSeg X = this;

            return null;
        }

    }
}
