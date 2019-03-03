package xyz.crearts.rover.arduino.component.encoders;

import org.springframework.stereotype.Component;
import xyz.crearts.rover.arduino.dto.RoverEngineMove;
import xyz.crearts.rover.arduino.dto.RoverRequest;
import xyz.crearts.rover.arduino.dto.RoverRequestId;
import xyz.crearts.rover.arduino.component.Encoder;

import java.io.DataOutputStream;
import java.io.IOException;

@Component
public class RoverEngineMoveEncoder extends Encoder {
    public RoverRequestId getReqId() {
        return RoverRequestId.REQ_ENGINE_MOVE;
    }

    @Override
    public void toStream(RoverRequest message, DataOutputStream os) throws IOException {
        if (!(message instanceof RoverEngineMove)) {
            throw new IllegalArgumentException("Wrong request type");
        }
        super.toStream(message, os);

        RoverEngineMove msg = (RoverEngineMove)message;
        os.writeByte(msg.getEngine());
        os.writeByte(msg.getDirection());
        os.writeShort(msg.getSpeed());
    }
}
