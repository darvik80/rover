package xyz.crearts.rover.arduino.dto;

public class RoverRequest {
    private short magic;
    private RoverRequestId reqId;

    public RoverRequest(RoverRequestId reqId) {
        this.magic = Const.ROVER_PROTOCOL_MAGIC;
        this.reqId = reqId;
    }

    public short getMagic() {
        return magic;
    }

    public void setMagic(short magic) {
        this.magic = magic;
    }

    public RoverRequestId getReqId() {
        return reqId;
    }

    public void setReqId(RoverRequestId reqId) {
        this.reqId = reqId;
    }
}
