package xyz.crearts.rover.arduino.component;

public interface Decoder<T> {
    T decode(byte[] data);
}
