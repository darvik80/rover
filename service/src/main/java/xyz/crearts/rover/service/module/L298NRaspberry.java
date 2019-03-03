package xyz.crearts.rover.service.module;

import com.pi4j.io.gpio.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Profile({"pi1", "pi3"})
public class L298NRaspberry extends Module {
    public class MotorDC3V6V {
        private GpioPinPwmOutput speed;
        private GpioPinDigitalOutput forward;
        private GpioPinDigitalOutput backward;

        public MotorDC3V6V(GpioController gpio, Pin speed, Pin forward, Pin backward) {

            if (speed != null) {
                this.speed = gpio.provisionPwmOutputPin(speed, "SPEED", 0);
            }
            this.forward = gpio.provisionDigitalOutputPin(forward, "FORWARD", PinState.LOW);
            this.backward = gpio.provisionDigitalOutputPin(backward, "BACKWARD", PinState.LOW);
        }

    }

    private MotorDC3V6V leftMotor;
    private MotorDC3V6V rightMotor;

    L298NRaspberry(GpioController gpio) {
        super(gpio);
    }

    @PostConstruct
    @Profile("pi1")
    public void postConstructPI1() {
        // For Raspberry PI 1
        leftMotor = new MotorDC3V6V(this.getGpio(), null, RaspiPin.GPIO_06, RaspiPin.GPIO_05);
        rightMotor = new MotorDC3V6V(this.getGpio(), null, RaspiPin.GPIO_04, RaspiPin.GPIO_01);
    }

    @PostConstruct
    @Profile("arduino")
    public void postConstructArduino() {
        // For Raspberry PI 1
        leftMotor = new MotorDC3V6V(this.getGpio(), null, RaspiPin.GPIO_06, RaspiPin.GPIO_05);
        rightMotor = new MotorDC3V6V(this.getGpio(), null, RaspiPin.GPIO_04, RaspiPin.GPIO_01);
    }

    @PostConstruct
    @Profile("pi3")
    public void postConstructPI3() {
        // For Raspberry PI 3
        leftMotor = new MotorDC3V6V(this.getGpio(), RaspiPin.GPIO_23, RaspiPin.GPIO_24, RaspiPin.GPIO_25);
        rightMotor = new MotorDC3V6V(this.getGpio(), RaspiPin.GPIO_26, RaspiPin.GPIO_27, RaspiPin.GPIO_28);
    }
}
