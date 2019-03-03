package xyz.crearts.rover.arduino.component;

import org.springframework.stereotype.Component;
import xyz.crearts.rover.arduino.dto.RoverRequest;
import xyz.crearts.rover.arduino.dto.RoverRequestId;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SerialEncoder {
    private Map<RoverRequestId, Encoder> encoders;

    public SerialEncoder(List<Encoder> encoders) {
        this.encoders = encoders.stream().collect(Collectors.toMap(Encoder::getReqId, Function.identity()));
    }

    public byte[] encode(RoverRequest req) throws IOException {
        Encoder encoder = encoders.get(req.getReqId());
        return encoder.encode(req);
    }
}
