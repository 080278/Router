/*
 * interface for the fabric operations
 */
public interface InterfaceFabric {
    
    //moves a packet from input buffer to output buffer
    public abstract void MovePacket(int inputBufferNumber, int outputBufferNumber);
    //attempt to Takes control of the Fabric Bus
    public abstract boolean SetBusActiveStatus(int busNumber, int inputBufferNumber);
    //attempt to Release control of the Fabric Bus
    public abstract boolean SetBusInActiveStatus(int busNumber, int inputBufferNumber);
}
