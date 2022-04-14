package davide.customitems.API;

public class Vector {
    private final double x;
    private final double y;
    private final double z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Vector add(Vector vec) {
        return new Vector(x + vec.x, y + vec.y, z + vec.z);
    }

    public Vector subtract(Vector vec) {
        return new Vector(x - vec.x, y - vec.y, z - vec.z);
    }

    public Vector multiply(Vector vec) {
        return new Vector(x * vec.x, y * vec.y, z * vec.z);
    }
}
