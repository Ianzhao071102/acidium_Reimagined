package org.izdevs.acidium.ai;


import com.dongbat.walkable.FloatArray;

public interface Navigator {
    void init();
    FloatArray navigate(int x, int x1, int y, int y1, int hitboxRadius);
}
