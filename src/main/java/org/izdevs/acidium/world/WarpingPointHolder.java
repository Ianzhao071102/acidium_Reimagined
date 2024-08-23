package org.izdevs.acidium.world;

import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WarpingPointHolder {
    @Getter
    Set<WarpingPoint> points;

    public void register(WarpingPoint p){
        points.add(p);
        log.debug("added 1 warping point:" + p.toString());
    }
    public void remove(WarpingPoint p){
        points.remove(p);
        log.debug("removed 1 warping point:" + p.toString());
    }
}
