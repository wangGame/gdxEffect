package wk.demo.block;

import com.badlogic.gdx.utils.Array;

public class Main {
    public static void main(String[] args) {
        duratontest();
    }

    private static void duratontest() {
        Array<Integer> array = new Array<>();
        float time[] = {0.03f,0.04f,0.1F,0.5F,1F};
        float duration = 0;
        for (int i = 5; i > 0; i--) {
            array.add(i);
            duration = Math.max(duration, time[i-1]);
        }

    }

    private static void bziercure() {
        float cx1 = 0.25F;
        float cy1 = 0;
        float cx2 = 0.75F;
        float cy2 = 1;
        float tmpx = (-cx1 * 2 + cx2) * 0.03f, tmpy = (-cy1 * 2 + cy2) * 0.03f;
        float dddfx = ((cx1 - cx2) * 3 + 1) * 0.006f, dddfy = ((cy1 - cy2) * 3 + 1) * 0.006f;
        float ddfx = tmpx * 2 + dddfx, ddfy = tmpy * 2 + dddfy;
        float dfx = cx1 * 0.3f + tmpx + dddfx * 0.16666667f, dfy = cy1 * 0.3f + tmpy + dddfy * 0.16666667f;
        float x = dfx;
        float y = dfy;

        float curves[] = new float[18];
        int i = 0;
        for (int n = i + 19 - 1; i < n; i += 2) {
            curves[i] = x;
            curves[i + 1] = y;
            dfx += ddfx;
            dfy += ddfy;
            ddfx += dddfx;
            ddfy += dddfy;
            x += dfx;
            y += dfy;
        }

        System.out.println("result : ");
    }
}
