package org.izdevs.acidium.scheduling;

public class ScheduledTask implements Runnable {
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

    @Override
    public void run() {
        this.task.run();
    }

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
            this.run();
        } catch(Exception e){
            state = State.EXCEPTION;
            throw new RuntimeException(e);
        }

        state = State.FINISHED;
    }


}
