package xyz.crearts.rover.arduino.component;

import xyz.crearts.rover.arduino.dto.RoverRequest;
import xyz.crearts.rover.arduino.dto.RoverRequestId;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Encoder {
    abstract public RoverRequestId getReqId();

    public byte[] encode(RoverRequest message) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream os = new DataOutputStream(bos);
        toStream(message, os);
        os.flush();
        return bos.toByteArray();
    }

    public void toStream(RoverRequest message, DataOutputStream os) throws IOException {
        os.writeShort(message.getMagic());
        os.writeShort(message.getReqId().ordinal());
    }
}
