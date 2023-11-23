
package eson.component.animator;

import java.util.ArrayList;

public abstract class TimingSource {

    private ArrayList<TimingEventListener> listeners = new ArrayList<TimingEventListener>();
    
    public abstract void start();
    
    public abstract void stop();
        
    public abstract void setResolution(int resolution);
    
    public abstract void setStartDelay(int delay);
    
    public final void addEventListener(TimingEventListener listener) {
        synchronized(listeners) {
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }
    }
    
    public final void removeEventListener(TimingEventListener listener) {
        synchronized(listeners) {
            listeners.remove(listener);
        }
    }
    
     protected final void timingEvent() {
         synchronized(listeners) {
             for (TimingEventListener listener : listeners) {
                 listener.timingSourceEvent(this);
             }
         }
    }
}