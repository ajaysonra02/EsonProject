
package eson.component.animator;

import javax.swing.Timer;
import java.awt.event.*;
import java.util.ArrayList;

public final class Animator {

    private TimingSource timer;
    private TimingSource swingTimer;
    private TimingSourceTarget timingSourceTarget;
    
    private ArrayList<TimingTarget> targets = new ArrayList<>();
    private long startTime;	   
    private long currentStartTime; 
    private boolean intRepeatCount = true; 
    private boolean timeToStop = false;   
    private boolean hasBegun = false;
    private long pauseBeginTime = 0; 
    private boolean running = false;
    private double repeatCount = 1.0;
    private int startDelay;
    private RepeatBehavior repeatBehavior = RepeatBehavior.REVERSE;
    private EndBehavior endBehavior = EndBehavior.HOLD;
    private int duration;
    private int resolution = 20;    
    private float acceleration = 0;
    private float deceleration = 0.0f;
    private float startFraction = 0.0f;
    private Direction startDirection = Direction.FORWARD;
    private Direction direction;
    private Interpolator interpolator = LinearInterpolator.getInstance();
    
    public static enum EndBehavior {
	HOLD,
	RESET,
    };
    
    public static enum Direction {
	FORWARD,
	BACKWARD,
    };
    
    public static enum RepeatBehavior {
	LOOP,
	REVERSE
    };
    
    public static final int INFINITE = -1;

    private void validateRepeatCount(double repeatCount) {
        if (repeatCount < 1 && repeatCount != INFINITE) {
            throw new IllegalArgumentException("repeatCount (" + repeatCount + 
                    ") cannot be <= 0");
        }
    }
    
    public Animator(int duration) {
        this(duration, null);
    }
    
    public Animator(int duration, TimingTarget target) {
        this.duration = duration;
        addTarget(target);
	java.awt.Toolkit.getDefaultToolkit();	
        swingTimer = new SwingTimingSource();
        timer = swingTimer;
    }
    
    public Animator(int duration, double repeatCount,RepeatBehavior repeatBehavior, TimingTarget target) {
        this(duration, target);
        validateRepeatCount(repeatCount);
        this.repeatCount = repeatCount;
        this.repeatBehavior = (repeatBehavior != null) ? 
            repeatBehavior : RepeatBehavior.REVERSE;
	intRepeatCount = (Math.rint(repeatCount) == repeatCount);
    }    
    
    public Direction getStartDirection() {
        return startDirection;
    }
    
    public void setStartDirection(Direction startDirection) {
        throwExceptionIfRunning();
        this.startDirection = startDirection;
    }
    
    public Interpolator getInterpolator() {
        return interpolator;
    }
    
    public void setInterpolator(Interpolator interpolator) {
        throwExceptionIfRunning();
        this.interpolator = interpolator;
    }
    
    public void setAcceleration(float acceleration) {
        throwExceptionIfRunning();
        if (acceleration < 0 || acceleration > 1.0f) {
            throw new IllegalArgumentException("Acceleration value cannot lie" +
                    " outside [0,1] range");
        }
        if (acceleration > (1.0f - deceleration)) {
            throw new IllegalArgumentException("Acceleration value cannot be" +
                    " greater than (1 - deceleration)");
        }
        this.acceleration = acceleration;
    }
    
    public void setDeceleration(float deceleration) {
        throwExceptionIfRunning();
        if (deceleration < 0 || deceleration > 1.0f) {
            throw new IllegalArgumentException("Deceleration value cannot lie" +
                    " outside [0,1] range");
        }
        if (deceleration > (1.0f - acceleration)) {
            throw new IllegalArgumentException("Deceleration value cannot be" +
                    " greater than (1 - acceleration)");
        }
        this.deceleration = deceleration;
    }
    
    public float getAcceleration() {
        return acceleration;
    }
    
    public float getDeceleration() {
        return deceleration;
    }
    
    public void addTarget(TimingTarget target) {
        if (target != null) {
            synchronized (targets) {
                if (!targets.contains(target)) {
                    targets.add(target);
                }
            }
        }
    }
    
    public void removeTarget(TimingTarget target) {
        synchronized (targets) {
            targets.remove(target);
        }
    }
    
    private void throwExceptionIfRunning() {
        if (isRunning()) {
            throw new IllegalStateException("Cannot perform this operation " +
                    "while Animator is running");
        }
    }
    
    public int getResolution() {
	return resolution;
    }
    
    public void setResolution(int resolution) {
        if (resolution < 0) {
            throw new IllegalArgumentException("resolution must be >= 0");
        }
        throwExceptionIfRunning();
        this.resolution = resolution;
        timer.setResolution(resolution);
    }
    
    public int getDuration() {
	return duration;
    }
    
    public void setDuration(int duration) {
        throwExceptionIfRunning();
        this.duration = duration;
    }

    public double getRepeatCount() {
	return repeatCount;
    }
    
    public void setRepeatCount(double repeatCount) {
        validateRepeatCount(repeatCount);
        throwExceptionIfRunning();
        this.repeatCount = repeatCount;
    }

    public int getStartDelay() {
	return startDelay;
    }

    public void setStartDelay(int startDelay) {
        if (startDelay < 0) {
            throw new IllegalArgumentException("startDelay (" + startDelay + 
                    ") cannot be < 0");
        }
        throwExceptionIfRunning();
        this.startDelay = startDelay;
        timer.setStartDelay(startDelay);
    }

    public RepeatBehavior getRepeatBehavior() {
	return repeatBehavior;
    }
    
    public void setRepeatBehavior(RepeatBehavior repeatBehavior) {
        throwExceptionIfRunning();
        this.repeatBehavior = (repeatBehavior != null) ? 
            repeatBehavior : RepeatBehavior.REVERSE;
    }

    public EndBehavior getEndBehavior() {
	return endBehavior;
    }
    
    public void setEndBehavior(EndBehavior endBehavior) {
        throwExceptionIfRunning();
        this.endBehavior = endBehavior;
    }
    
    public float getStartFraction() {
        return startFraction;
    }
    
    public void setStartFraction(float startFraction) {
        if (startFraction < 0 || startFraction > 1.0f) {
            throw new IllegalArgumentException("initialFraction must be " +
                    "between 0 and 1");
        }
        throwExceptionIfRunning();
        this.startFraction = startFraction;
    }
    
    public void start() {
        throwExceptionIfRunning();
        hasBegun = false;
        running = true;
        direction = startDirection;
        startTime = (System.nanoTime() / 1000000) + getStartDelay();
        if (duration != INFINITE &&
                ((direction == Direction.FORWARD && startFraction > 0.0f) ||
                 (direction == Direction.BACKWARD && startFraction < 1.0f))) {
            float offsetFraction = (direction == Direction.FORWARD) ?
                startFraction : (1.0f - startFraction);
            long startDelta = (long)(duration * offsetFraction);
            startTime -= startDelta;
        }
	currentStartTime = startTime;
	timer.start();
    }

    public boolean isRunning() {
	return running;
    }

    public void stop() {
	timer.stop();
        end();
        timeToStop = false;
        running = false;
        pauseBeginTime = 0;
    }

    public void cancel() {
        timer.stop();
        timeToStop = false;
        running = false;
        pauseBeginTime = 0;
    }
    
    public void pause() {
        if (isRunning()) {
            pauseBeginTime = System.nanoTime();
            running = false;
            timer.stop();
        }
    }
    
    public void resume() {
        if (pauseBeginTime > 0) {
            long pauseDelta = (System.nanoTime() - pauseBeginTime) / 1000000;
            startTime += pauseDelta;
            currentStartTime += pauseDelta;
            timer.start();
            pauseBeginTime = 0;
            running = true;
        }
    }

    private void timingEvent(float fraction) {
        synchronized (targets) {
            for (int i = 0; i < targets.size(); ++i) {
                TimingTarget target = targets.get(i);
                target.timingEvent(fraction);
            }
        }
        if (timeToStop) {
            stop();
        }
    }
    
    private void begin() {
        synchronized (targets) {
            for (int i = 0; i < targets.size(); ++i) {
                TimingTarget target = targets.get(i);
                target.begin();
            }
        }
    }
    
    private void end() {
        synchronized (targets) {
            for (int i = 0; i < targets.size(); ++i) {
                TimingTarget target = targets.get(i);
                target.end();
            }
        }
    }
    
    private void repeat() {
        synchronized (targets) {
            for (int i = 0; i < targets.size(); ++i) {
                TimingTarget target = targets.get(i);
                target.repeat();
            }
        }
    }
    
    private float timingEventPreprocessor(float fraction) {
        if (acceleration != 0 || deceleration != 0.0f) {
            float runRate = 1.0f / (1.0f - acceleration/2.0f - 
                    deceleration/2.0f);
            if (fraction < acceleration) {
                float averageRunRate = runRate * (fraction / acceleration) / 2;
                fraction *= averageRunRate;
            } else if (fraction > (1.0f - deceleration)) {
                float tdec = fraction - (1.0f - deceleration);
                float pdec  = tdec / deceleration;
                fraction = runRate * (1.0f - ( acceleration / 2) -
                        deceleration + tdec * (2 - pdec) / 2);
            } else {
                fraction = runRate * (fraction - (acceleration / 2));
            }
            if (fraction < 0) {
                fraction = 0;
            } else if (fraction > 1.0f) {
                fraction = 1.0f;
            }
        }
        return interpolator.interpolate(fraction);
    }
    
    public long getTotalElapsedTime(long currentTime) {
        return (currentTime - startTime);
    }

    public long getTotalElapsedTime() {
        long currentTime = System.nanoTime() / 1000000;
        return getTotalElapsedTime(currentTime);
    }
    
    public long getCycleElapsedTime(long currentTime) {
        return (currentTime - currentStartTime);
    }

    public long getCycleElapsedTime() {
        long currentTime = System.nanoTime() / 1000000;
        return getCycleElapsedTime(currentTime);
    }
    
    public float getTimingFraction() {
        long currentTime = System.nanoTime() / 1000000;
        long cycleElapsedTime = getCycleElapsedTime(currentTime);
        long totalElapsedTime = getTotalElapsedTime(currentTime);
        double currentCycle = (double)totalElapsedTime / duration;
        float fraction;

        if (!hasBegun) {
            begin();
            hasBegun = true;
        }
        if ((duration != INFINITE) && (repeatCount != INFINITE) && 
                (currentCycle >= repeatCount)) {
            switch (endBehavior) {
            case HOLD:
                if (intRepeatCount) {
                    if (direction == Direction.BACKWARD) {
                        fraction = 0.0f;
                    } else {
                        fraction = 1.0f;
                    }
                } else {
                    fraction = Math.min(1.0f, 
                        ((float)cycleElapsedTime / duration));
                }
                break;
            case RESET:
                fraction = 0.0f;
                break;
            default:
                fraction = 0.0f;
                break;
            }
            timeToStop = true;
        } else if ((duration != INFINITE) && (cycleElapsedTime > duration)) {
            long actualCycleTime = cycleElapsedTime % duration;
            fraction = (float)actualCycleTime / duration;
            currentStartTime = currentTime - actualCycleTime;

            if (repeatBehavior == RepeatBehavior.REVERSE) {
                boolean oddCycles = 
                        ((int)(cycleElapsedTime / duration) % 2)
                        > 0;
                if (oddCycles) {
                    direction = (direction == Direction.FORWARD) ? 
                            Direction.BACKWARD :
                            Direction.FORWARD;
                }
                if (direction == Direction.BACKWARD) {
                    fraction = 1.0f - fraction;
                }
            }
            repeat();
        } else {
            fraction = 0.0f;
            if (duration != INFINITE) {
                fraction = (float)cycleElapsedTime / duration;
                if (direction == Direction.BACKWARD) {
                    fraction = (1.0f - fraction);
                }
                fraction = Math.min(fraction, 1.0f);
                fraction = Math.max(fraction, 0.0f);
            }
        }
        return timingEventPreprocessor(fraction);
    }
    
    public synchronized void setTimer(TimingSource timer) {
        throwExceptionIfRunning();
        if (this.timer != swingTimer) {
            this.timer.removeEventListener(timingSourceTarget);
        }
        if (timer == null) {
            this.timer = swingTimer;
        } else {
            this.timer = timer;
            if (timingSourceTarget == null) {
                timingSourceTarget = new TimingSourceTarget();
            }
            timer.addEventListener(timingSourceTarget);
        }
        // sync this new timer with existing timer properties
        this.timer.setResolution(resolution);
        this.timer.setStartDelay(startDelay);
    }
        
    class TimingSourceTarget implements TimingEventListener {
        public void timingSourceEvent(TimingSource timingSource) {
            if ((timer == timingSource) && running) {
                timingEvent(getTimingFraction());
            }
        }
    }
    
    private class SwingTimingSource extends TimingSource {
        Timer timer; 
        
        public SwingTimingSource() {
            timer = new Timer(resolution, new TimerTarget());
            timer.setInitialDelay(0);
        }
        
        @Override
        public void start() {
            timer.start();
        }
        
        @Override
        public void stop() {
            timer.stop();
        }
        
        @Override
        public void setResolution(int resolution) {
            timer.setDelay(resolution);
        }
        
        @Override
        public void setStartDelay(int delay) {
            timer.setInitialDelay(delay);
        }
    }
    
    private class TimerTarget implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                    timingEvent(getTimingFraction());
            }
    }

}

