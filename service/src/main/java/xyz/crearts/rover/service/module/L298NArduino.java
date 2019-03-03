package xyz.crearts.rover.service.module;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import xyz.crearts.rover.arduino.dto.RoverEngineMove;
import xyz.crearts.rover.arduino.component.SerialTransport;

@Component
@Profile("arduino")
public class L298NArduino implements L298NController {
    private final SerialTransport arduino;

    public L298NArduino(SerialTransport arduino) {
        this.arduino = arduino;
    }

    @Override
    public void move(Engine engine, Direction direction, int speed) {
        this.arduino.sendMessage(
            new RoverEngineMove(
                (byte)engine.ordinal(),
                (byte)direction.ordinal(),
                (byte)speed // todo convert to range from 0 to 255
            )
        );
    }
}
