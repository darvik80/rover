package xyz.crearts.rover.service.module;

public interface L298NController {
    enum Engine {
        ENGINE_LEFT,
        ENGINE_RIGTH
    }

    enum Direction {
        DIR_FORWARD,
        DIR_BACKWARD
    }
    void move(Engine engine,Direction direction, int speed);
}
