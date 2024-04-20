package org.izdevs.acidium.scheduling;

public class ScheduledTask{
    /**
     * Basic state representation of a scheduled task
     */
    enum State{
        SCHEDULED_WAITING,
        RUNNING,
        FINISHED,
        EXCEPTION //EXIT WITH EXCEPTION
    }
    /**
     * Runs this operation.
     */

    State state;



    /**
     * What is should run
     */
    Runnable task;


    public ScheduledTask(Runnable task){
        this.task = task;
    }


    /**
     * Executes the run
     */
    public void exec(){
        state = State.RUNNING;

        try{
            this.task.run();
        } catch(Exception e){
            state = State.EXCEPTION;
            throw new RuntimeException(e);
        }

        state = State.FINISHED;
    }


}
