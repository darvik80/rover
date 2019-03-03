package xyz.crearts.rover.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.crearts.rover.arduino.component.SerialTransport;
import xyz.crearts.rover.arduino.dto.RoverEngineMove;
import xyz.crearts.rover.arduino.dto.RoverLedBlink;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final SerialTransport arduino;

    public ApiController(SerialTransport arduino) {
        this.arduino = arduino;
    }

    @PostMapping("/blink")
    public void blinkAction() {
        arduino.sendMessage(new RoverLedBlink());
    }

    @PostMapping("/move")
    public void moveAction() throws InterruptedException {
        for (int idx = 20; idx < 250; idx += 10) {
            arduino.sendMessage(new RoverEngineMove((byte) 2, (byte) 0, (byte) idx));
            Thread.sleep(100);
        }
    }
    @PostMapping("/stop")
    public void stopAction() throws InterruptedException {
        for (int idx = 250; idx > 20; idx -= 10) {
            arduino.sendMessage(new RoverEngineMove((byte) 2, (byte) 0, (byte) idx));
            Thread.sleep(100);
        }

        arduino.sendMessage(new RoverEngineMove((byte) 2, (byte) 0, (byte) 0));
    }
}

