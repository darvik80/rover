package xyz.crearts.rover.arduino.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class RoverLedBlink extends RoverRequest {
    public RoverLedBlink() {
        super(RoverRequestId.REQ_LED_BLINK);
    }
}
