package application.mechanics.base;

@SuppressWarnings("PublicField")
public class Angles {
    public double phi;
    public double theta;

    // TODO check for needed
    public Angles() {
    }

    public Angles(double phi, double theta) {
        this.phi = phi + Math.PI / 2;
        this.theta = theta;
    }

    @Override
    public String toString() {
        return "{phi:" + phi + ",theta:" + theta + '}';
    }
}
