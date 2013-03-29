/*
 * interface for the fabric operations
 */
public interface InterfaceFabric {
    
    //moves a packet from input buffer to output buffer
    public abstract int MovePacket(int inputBufferNumber, int outputBufferNumber, int TIME);
    //attempt to Takes control of the Fabric Bus
    public abstract boolean SetBusActiveStatus(int busNumber, int inputBufferNumber, int sequence, int TIME);
    //attempt to Release control of the Fabric Bus
    public abstract boolean SetBusInActiveStatus(int busNumber, int inputBufferNumber, int sequence, int TIME);
    //get the recent packet moved
    public abstract RouterPacket GetRecentPacket();
    //get the recent bus used
    public abstract int GetRecentBus();
    //print status of the fabric
    public abstract void Print(boolean result, int ActiveTO, int sequence, boolean active,int TIME);
}   
