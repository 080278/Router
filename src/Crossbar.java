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
    public void MovePacket(int inputBufferNumber, int outputBufferNumber)
    {
        
    }
}
