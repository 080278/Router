/*
 * This implements the base class for the switching fabrics. This class cannot
 * be instantiated.
 */

import java.util.*;

public abstract class SwitchingFabric implements InterfaceFabric{
    //set th number of vertical buses
    public final int VERTICALBUSES;
    //holds the vertical bus active flag
    boolean []busActiveStatus;
    //holds the speed that the fabric is operating at
    int speed;
    //holds the list of input buffers
    Queue []inputBuffers;
    //holds the list of output buffers
    Queue []outputBuffers;
    
    
    //moves a packet from input buffer to output buffer
    public abstract void MovePacket(int inputBufferNumber, int outputBufferNumber);
    
    //constructor
    public SwitchingFabric(int speed, Queue []inputBuffers, Queue []outputBuffers, int VERTICALBUSES)
    {
        //set vertical busses
        this.VERTICALBUSES = VERTICALBUSES;
        //set the speed of the switching fabric
        this.speed = speed;
        //set the input buffers
        this.inputBuffers = inputBuffers;
        //set the output buffers
        this.outputBuffers = outputBuffers;
        //creates statuses for each vertical bus
        busActiveStatus = new boolean[this.VERTICALBUSES];
    }
    
    //set the active status of the bus
    public void SetBusActiveStatus(int busNumber, boolean status)
    {
        //set the status of the bus
        busActiveStatus[busNumber] = status;
    }
    
    //get the active status of the bus
    public boolean GetBusActiveStatus(int busNumber)
    {
        //get the status of the bus
        return busActiveStatus[busNumber];
    }
}
