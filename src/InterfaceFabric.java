/*
 * interface for the fabric operations
 */
public interface InterfaceFabric {
    
    //moves a packet from input buffer to output buffer
    public abstract int MovePacket(int inputBufferNumber, int outputBufferNumber, int TIME);
    //attempt to Takes control of the Fabric Bus
    public abstract boolean SetBusActiveStatus(int busNumber, int inputBufferNumber, int sequence);
    //attempt to Release control of the Fabric Bus
    public abstract boolean SetBusInActiveStatus(int busNumber, int inputBufferNumber, int sequence);
}
