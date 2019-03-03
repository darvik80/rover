package xyz.crearts.rover.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import xyz.crearts.rover.arduino.component.SerialTransport;
import xyz.crearts.rover.arduino.dto.RoverEngineMove;
import xyz.crearts.rover.event.EventRoverControl;

@Service
public class RoverService {
    private final SerialTransport arduino;

    public RoverService(SerialTransport arduino) {
        this.arduino = arduino;
    }

    @EventListener
    public void handleEventRoverControl(EventRoverControl eventRoverControl) {
        arduino.sendMessage(new RoverEngineMove((byte)1, (byte)0, (short)eventRoverControl.getStatus().getLeftWeal()));
        arduino.sendMessage(new RoverEngineMove((byte)0, (byte)0, (short)eventRoverControl.getStatus().getRightWeal()));
    }
}
