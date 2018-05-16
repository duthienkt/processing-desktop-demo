package vn.bluesky.segment2D;

/**
 * Created by DuThien on 31/05/2017.
 */
public class Segment2DFull<E> {
    public final int N;
    public final int K;
    int[][] CS;
    private Object[] data;

    public Segment2DFull(int K) {
        this.N = 1 << K;
        this.K = K;
        data = new Object[N * N];
        CS = new int[K + 1][];
        int s = N * N;
        for (int i = 0; i <= K; ++i) {
            CS[i] = new int[s];
            for (int j = 0; j < s; ++j)
                CS[i][j] = 0;
            s /= 4;
        }
    }



    public void move(int i, int j, int in, int jn) {
//        Object t = data[in*N + jn];
        data[in*N + jn] = data[i*N + j];
//        data[i*N + j] = t;
        move(0, i, j, in, jn);
    }

    private void move(int k, int i, int j, int in, int jn)
    {
        if (k > K) return;
        if (i == in && j == jn) return;
        int n = 1 << (K - k);
        CS[k][i* n + j]--;
        CS[k][in*n + jn]++;
        move(k + 1, i / 2, j / 2, in / 2, jn / 2);
    }

    double distance(int k, int i, int j, int ii, int jj) {
        int di = 0;
        int dj = 0;
        int d = 1<<k;
        if (i < ii) di = d* (ii - i) - d + 1;
        if (i > ii) di = d* (i - ii) - d + 1;
        if (j < jj) dj = d* (jj - j) - d + 1;
        if (j > jj) dj = d* (j - jj) - d + 1;
        return Math.sqrt(di*di + dj*dj);
    }

    public void set(E val, int i, int j) {
        data[i*N + j] = val;
        if (CS[0][i*N + j] == 0)
        {
            int n = N;
            for (int k = 0; k <= K; ++k)
            {
                ++CS[k][i*n + j];
                i /= 2;
                j /= 2;
                n /= 2;
            }
        }
    }

    public void remove(int i, int j) {
        if (CS[0][i*N + j] == 0)
        return;
        int n = N;
        for (int k = 0; k <= K; ++k)
        {
            ++CS[k][i*n + j];
            i /= 2;
            j /= 2;
            n /= 2;
        }
    }

    public int count(int i, int j){
        return CS[0][i*N + j];
    }

    public int vs_count;
    public int in_count;
    public String logText = "";
    private void forAll(int k, int i, int j, double dis, Reciver<E> func, Object param, int ii, int jj)
    {
        int r = 1 << k;
        int n = 1 << (K - k);
        if (CS[k][ii*n + jj] <= 0) return;
        if (distance(k,i / r, j / r, ii, jj) > dis) return;
        vs_count++;
        if (k <= 0)
        {
            func.onRecived((E)data[ii*n + jj], ii, jj, param);
            in_count++;
        }
        else
        {
            forAll(k - 1, i, j, dis, func, param, ii * 2, jj * 2);
            forAll(k - 1, i, j, dis, func, param, ii * 2, jj * 2 + 1);
            forAll(k - 1, i, j, dis, func, param, ii * 2 + 1, jj * 2 + 1);
            forAll(k - 1, i, j, dis, func, param, ii * 2 + 1, jj * 2);
        }

    }

    public void forAll(int i, int j, double dis, Reciver<E> func, Object param)
    {

        vs_count = 0;
        in_count = 0;
        forAll(K, i, j, dis, func, param, 0, 0);
        logText = ""+in_count+" enermys \nVisit "
                +vs_count +" nodes in "+dis*dis*3.1415+ "m2\nH =  "
                + ((dis*dis*3.1415)/vs_count);
    }




    public interface Reciver<E> {
        void onRecived(E item,int i, int j, Object param);
    }


}
