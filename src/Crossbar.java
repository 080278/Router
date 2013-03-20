/*
 * This implements the Crossbar switching fabric
 */

import java.util.*;

public class Crossbar extends SwitchingFabric{
        
    //constructor
    public Crossbar(int speed, Queue []inputBuffers, Queue []outputBuffers)
    {
        //passes the constructor values to the base class SwitchingFabric
        super(speed, inputBuffers, outputBuffers, outputBuffers.length);
    }
    

    //moves a packet from input buffer to output buffer
    public int MovePacket(int inputBufferNumber, int outputBufferNumber,int TIME)
    {
        return -999999;
    }
    //attempt to Takes control of the Fabric Bus
    public boolean SetBusActiveStatus(int busNumber, int inputBufferNumber, int packetSequence)
    {
        return false;
    }
    //attempt to Release control of the Fabric Bus
    public boolean SetBusInActiveStatus(int busNumber, int inputBufferNumber, int packetSequence)
    {
        return false;
    }
    
    //get the recent packet moved
    public RouterPacket GetRecentPacket()
    {
        return null;
    }
    
    //get the recent packet moved
    public int GetRecentBus()
    {
        return 0;
    }

}
