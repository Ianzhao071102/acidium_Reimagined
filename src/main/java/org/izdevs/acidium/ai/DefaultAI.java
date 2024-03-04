package org.izdevs.acidium.ai;

public class DefaultAI implements AI{

    /**
     * @param state
     * @apiNote Please use this carefully, be sure to override before any f***ed usages
     */
    @Override
    public void react(State state) {
        switch(state.name){
            case "Idling" ->{

            }

            // TODO ADD MORE STATES
        }
    }

}
