package xyz.crearts.rover.arduino.dto;

import lombok.Builder;

@Builder
public class RoverHello extends RoverRequest {
    public RoverHello() {
        super(RoverRequestId.REQ_HELLO);
    }
}
