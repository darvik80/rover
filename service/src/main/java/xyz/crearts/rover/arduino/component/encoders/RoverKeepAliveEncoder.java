package xyz.crearts.rover.arduino.component.encoders;

import org.springframework.stereotype.Component;
import xyz.crearts.rover.arduino.dto.RoverRequestId;
import xyz.crearts.rover.arduino.component.Encoder;

@Component
public class RoverKeepAliveEncoder extends Encoder {
    @Override
    public RoverRequestId getReqId() {
        return RoverRequestId.REQ_KEEP_ALIVE;
    }
}
