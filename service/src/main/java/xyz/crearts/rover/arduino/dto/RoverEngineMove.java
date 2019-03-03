package xyz.crearts.rover.arduino.dto;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper=false)
public class RoverEngineMove extends RoverRequest {
    private byte engine;
    private byte direction;
    private short speed;

    public RoverEngineMove() {
        super(RoverRequestId.REQ_ENGINE_MOVE);
    }

    public RoverEngineMove(byte engine, byte direction, short speed) {
        super(RoverRequestId.REQ_ENGINE_MOVE);
        this.engine = engine;
        this.direction = direction;
        this.speed = speed;
    }
}
