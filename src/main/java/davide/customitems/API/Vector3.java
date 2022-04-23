package davide.customitems.API;

public class Vector3 {
    private final double x;
    private final double y;
    private final double z;

    public Vector3(double x, double y, double z) {
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

    public Vector3 add(Vector3 vec) {
        return new Vector3(x + vec.x, y + vec.y, z + vec.z);
    }

    public Vector3 subtract(Vector3 vec) {
        return new Vector3(x - vec.x, y - vec.y, z - vec.z);
    }

    public Vector3 multiply(Vector3 vec) {
        return new Vector3(x * vec.x, y * vec.y, z * vec.z);
    }
}
