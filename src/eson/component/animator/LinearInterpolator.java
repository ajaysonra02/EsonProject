
package eson.component.animator;

public final class LinearInterpolator implements Interpolator {
    
    private static LinearInterpolator instance = null;
    
    private LinearInterpolator() {}
    
    public static LinearInterpolator getInstance() {
        if (instance == null) {
            instance = new LinearInterpolator();
        }
        return instance;
    }
    
    @Override
    public float interpolate(float fraction) {
        return fraction;
    }
    
}
