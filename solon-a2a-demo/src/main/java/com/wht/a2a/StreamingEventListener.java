package com.wht.a2a;

/**
 * Listener interface for streaming task events
 */
public interface StreamingEventListener {
    
    /**
     * Called when a streaming event is received
     * 
     * @param event the event object (could be TaskStatusUpdateEvent or TaskArtifactUpdateEvent)
     */
    void onEvent(Object event);
    
    /**
     * Called when an error occurs during streaming
     * 
     * @param exception the exception that occurred
     */
    void onError(Exception exception);
    
    /**
     * Called when the stream is completed
     */
    void onComplete();
} 