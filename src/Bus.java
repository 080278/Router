/*
 * This implements the Bus switching fabric
 */

import java.util.*;

public class Bus extends SwitchingFabric{
    //define the number of busses
    public static final int VERTICALBUSES = 1;
    
    //constructor
    public Bus(int speed, Queue []inputBuffers, Queue []outputBuffers)
    {
        //passes the constructor values to the base class SwitchingFabric
        super(speed, inputBuffers, outputBuffers, VERTICALBUSES);
    }
    
    //moves a packet from input buffer to output buffer
    public void MovePacket(int inputBufferNumber, int outputBufferNumber)
    {
        
    }
    
}
