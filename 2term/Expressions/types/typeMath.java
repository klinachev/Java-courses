package expression.types;

public interface typeMath<T extends Number> {
    T add(T x, T y);
    T mull(T x, T y);
    T dec(T x, T y);
    T div(T x, T y);
    T parseString(String s);
    T count(T x);
    T negate(T x);
    T min(T x, T y);
    T max(T x, T y);
}
