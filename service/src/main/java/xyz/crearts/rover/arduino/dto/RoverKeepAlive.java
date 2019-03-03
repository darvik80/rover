package xyz.crearts.rover.arduino.dto;

public class RoverKeepAlive extends RoverRequest {
    public RoverKeepAlive() {
        super(RoverRequestId.REQ_KEEP_ALIVE);
        this.setMagic((Const.ROVER_PROTOCOL_MAGIC));
    }
}
