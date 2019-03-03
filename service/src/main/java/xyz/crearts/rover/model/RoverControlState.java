package xyz.crearts.rover.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoverControlState {
    private int leftWeal;
    private int rightWeal;
}

