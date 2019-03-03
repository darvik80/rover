package xyz.crearts.rover.configuration;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GPIOConfig {
    @Bean
    public synchronized GpioController gpioController() {
        return null;//GpioFactory.getInstance();
    }
}
