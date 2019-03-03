package xyz.crearts.rover.arduino.component;

import com.fazecast.jSerialComm.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.crearts.rover.arduino.dto.Const;
import xyz.crearts.rover.arduino.dto.RoverKeepAlive;
import xyz.crearts.rover.arduino.dto.RoverRequest;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SerialTransport implements Runnable {
    private final static int PACKET_SIZE = 32;
    private final static byte[] empty = new byte[PACKET_SIZE];
    private final SerialEncoder encoder;
    private final String serialPortName;
    private Thread mainThread;

    public SerialTransport(@Value("${serial.port}") String serialPortName, SerialEncoder encoder) {
        this.serialPortName = serialPortName;
        this.encoder = encoder;
    }

    private void send(RoverRequest req, DataOutputStream os) throws IOException {
        byte[] data = this.encoder.encode(req);
        if (data.length > PACKET_SIZE) {
            throw new RuntimeException("Packet too big!");
        }
        os.write(data);
        if (data.length < PACKET_SIZE) {
            os.write(empty, 0, PACKET_SIZE-data.length);
        }
    }

    private BlockingQueue<RoverRequest> queue = new ArrayBlockingQueue<>(10);

    public void sendMessage(RoverRequest msg) {
        try {
            queue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void postConstruct() {
        mainThread = new Thread(this);
        mainThread.start();
    }

    @PreDestroy
    public void preDestroy() throws InterruptedException {
        mainThread.interrupt();
        mainThread.join();
    }


    @Override
    public void run() {
        for (SerialPort port : SerialPort.getCommPorts()) {
            System.out.println(port.getSystemPortName());
        }

        SerialPort port = SerialPort.getCommPort(serialPortName);
        try {
            while (true) {
                try {
                    if (!port.openPort()) {
                        Thread.sleep(1000);
                        continue;
                    }

                    port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
                    DataInputStream is = new DataInputStream(port.getInputStream());
                    DataOutputStream os = new DataOutputStream(port.getOutputStream());

                    // { sync state
                    if (port.bytesAvailable() > 2) {
                        byte[] data = new byte[port.bytesAvailable()-2];
                        is.read(data);
                    }

                    if (is.readShort() != Const.ROVER_PROTOCOL_MAGIC) {
                        log.warn("Available data: " + port.bytesAvailable());
                        throw new RuntimeException("Unsupported protocol");
                    }

                    os.writeShort(Const.ROVER_PROTOCOL_MAGIC);
                    if (is.readShort() != (Const.ROVER_PROTOCOL_MAGIC+1)) {
                        log.warn("Available data: " + port.bytesAvailable());
                        throw new RuntimeException("Unsupported protocol");
                    }
                    // } sync state

                    // { exchange state
                    while (true) {
                        RoverRequest req = queue.poll(5, TimeUnit.SECONDS);
                        if (req == null) {
                            req = new RoverKeepAlive();
                        }
                        log.info("Send message: " + req.getReqId());
                        this.send(req, os);
                        if (is.readShort() != (Const.ROVER_PROTOCOL_MAGIC-1)) {
                            throw new RuntimeException("Reset connection");
                        }
                    }
                    // } exchange state
                } catch (IOException | RuntimeException ex) {
                    log.error("Serial Thread exception", ex);
                    if (port.isOpen()) {
                        port.closePort();
                    }
                }
            }
        } catch(InterruptedException e){
            log.info("Stop serial thread");
        }

    }
}
