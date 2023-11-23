
package eson.component.animator;

public interface TimingTarget {
    
    public void timingEvent(float fraction);

    public void begin();
    
    public void end();
    
    public void repeat();
    
}
