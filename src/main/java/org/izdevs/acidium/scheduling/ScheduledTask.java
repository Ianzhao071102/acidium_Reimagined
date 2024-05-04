package org.izdevs.acidium.scheduling;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

public class ScheduledTask{
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    public boolean async = false;
    /**
     * the ticks it should wait before being executed
     */
    public long destTick;
    public final long _indicator = destTick;

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
     * The current state of the task
     */
    State state = State.SCHEDULED_WAITING;



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
