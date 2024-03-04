package org.izdevs.acidium.ai;

import java.util.HashSet;
import java.util.Set;

public interface AI {
    Navigator navigator = null;

    /**
     * @apiNote Please register ur states here plz.....
     */
    Set<State> states = new HashSet<>();

    /**
     * @apiNote Please use this instead, override this thing and set ur own states, I love this thing
     */


    /**
     *
     * @apiNote Please use this carefully, be sure to override before any f***ed usages
     */
    void react(State state);
}
