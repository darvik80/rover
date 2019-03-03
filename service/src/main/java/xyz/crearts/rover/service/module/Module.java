package xyz.crearts.rover.service.module;

import com.pi4j.io.gpio.GpioController;
import lombok.Getter;

public abstract class Module {
    @Getter
    private GpioController gpio;

    Module(GpioController controller) {
        this.gpio = gpio;
    }

}
