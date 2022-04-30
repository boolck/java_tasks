package com.boolck.tasks.eventbus;

/**
 * custom EventBusException
 */
public class EventBusException extends Exception{

    private static final long serialVersionUID = 5821450303093641404L;

    public EventBusException(String message){
        super(message);
    }

}
