package davide.customitems.api;

public record Vector3(double x, double y, double z) {

    public Vector3 add(Vector3 vec) {
        return new Vector3(x + vec.x, y + vec.y, z + vec.z);
    }

    public Vector3 subtract(Vector3 vec) {
        return new Vector3(x - vec.x, y - vec.y, z - vec.z);
    }

    public Vector3 multiply(Vector3 vec) {
        return new Vector3(x * vec.x, y * vec.y, z * vec.z);
    }

    public Vector3 multiply(int m) {
        return new Vector3(x * m, y * m, z * m);
    }

    public Vector3 multiply(float m) {
        return new Vector3(x * m, y * m, z * m);
    }

    public Vector3 multiply(double m) {
        return new Vector3(x * m, y * m, z * m);
    }

    public Vector3 divide(Vector3 vec) {
        return new Vector3(x / vec.x, y / vec.y, z / vec.z);
    }

    public Vector3 copy(Vector3 vec) {
        return new Vector3(x, y, z);
    }
}
