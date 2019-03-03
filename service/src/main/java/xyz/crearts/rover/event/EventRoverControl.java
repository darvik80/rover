package xyz.crearts.rover.event;

import org.springframework.context.ApplicationEvent;
import xyz.crearts.rover.model.RoverControlState;

public class EventRoverControl  extends ApplicationEvent {
    private RoverControlState status;

    public EventRoverControl(Object source, RoverControlState status) {
        super(source);
        this.status = status;
    }

    public RoverControlState getStatus() {
        return status;
    }
}
