package org.izdevs.acidium.world;

import com.google.re2j.Matcher;
import com.google.re2j.Pattern;

import java.util.HashMap;

public class PlaceHolderWorld extends World{
    Pattern pattern;
    /**
     * @param regex_match_by_world_name
     *  the regex to hold that tells the service what world matching the regex should the entity be assigned to when
     *  it is possible.
     */
    public PlaceHolderWorld(String regex_match_by_world_name) {
        //empty map
        super(false);
        try {
            this.pattern = Pattern.compile(regex_match_by_world_name);
        }catch(RuntimeException e){
            throw new IllegalArgumentException("illegal regex has been passed!",e);
        }
    }

    public boolean matches(World world){
        try{
            this.pattern.matcher(world.getName());
            return true;
        }catch(Exception e){
            throw new RuntimeException("match failed..., check the input:" + world.getName(),e);
        }
    }
}
