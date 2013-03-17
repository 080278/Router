/*
 * interface for the fabric operations
 */
public interface InterfaceFabric {
    
    //moves a packet from input buffer to output buffer
    public abstract void MovePacket(int inputBufferNumber, int outputBufferNumber);
}
