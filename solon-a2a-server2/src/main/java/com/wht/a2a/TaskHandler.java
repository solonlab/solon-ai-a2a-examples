package com.wht.a2a;


import com.wht.a2a.model.Message;
import com.wht.a2a.model.Task;

/**
 * TaskHandler is a functional interface for handling tasks
 */
@FunctionalInterface
public interface TaskHandler {
    /**
     * Handle a task
     * 
     * @param task the task to handle
     * @param message the message content
     * @return the processed task
     * @throws Exception exceptions during processing
     */
    Task handle(Task task, Message message) throws Exception;
} 