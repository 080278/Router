/*
 * This implements the base class for the switching fabrics. This class cannot
 * be instantiated.
 */

import java.util.*;

public abstract class SwitchingFabric implements InterfaceFabric{
    //set th number of vertical buses
    protected final int VERTICALBUSES;
    //holds the vertical bus active flag
    protected boolean []busActiveStatus;
    //holds the speed that the fabric is operating at
    private int speed;
    //holds the list of input buffers
    protected Queue []inputBuffers;
    //holds the list of output buffers
    protected Queue []outputBuffers;
//********************************************************************
//NEED TO MODIFY FOR MULTIPLE BUSES IN THE CROSS BAR FABRIC    
//IMPLEMENT USING ARRAY(S)    
    //holds the current input buffer using the bus
    //protected int currentInputBufferUsingTheBus;
    protected int []currentInputBufferUsingTheBus;
    //holds the packet sequence number using the bus(es)
    protected int sequence;
//********************************************************************    
    
    //moves a packet from input buffer to output buffer
    public abstract int MovePacket(int inputBufferNumber, int outputBufferNumber, int TIME);
    //attempt to Takes control of the Fabric Bus
    public abstract boolean SetBusActiveStatus(int busNumber, int inputBufferNumber, int sequence);
    //attempt to Release control of the Fabric Bus
    public abstract boolean SetBusInActiveStatus(int busNumber, int inputBufferNumber, int sequence);
    //get the recent packet moved
    public abstract RouterPacket GetRecentPacket();
    //get the recent bus used moved
    public abstract int GetRecentBus();
    
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
        //set the current input buffer occupying the fabric bus, -1 = none
        currentInputBufferUsingTheBus = new int[inputBuffers.length];
        for(int x=0; x<(inputBuffers.length);x++)
            currentInputBufferUsingTheBus[x] = -1;
        
        
        //initialize each vertical bus to available
        for(int x=0;x< this.VERTICALBUSES;x++)
        {
            //initialize each vertical bus to available
            busActiveStatus[x] = false;
        }
    }
    
    
    //get number of vertical buses
    public int GetVerticalBuses()
    {
        return VERTICALBUSES;
    }
    
    //get the active status of the bus
    public boolean GetBusActiveStatus(int busNumber)
    {
        //get the status of the bus
        return busActiveStatus[busNumber];
    }
    
    //get which buffer using the Bus
    public int GetCurrentInputBufferUsingTheBus(int busNumber)
    {
        return currentInputBufferUsingTheBus[busNumber];
    }
    
    //get which packet sequence using the Bus
    public int GetCurrentPacketUsingTheBus()
    {
        return sequence;
    }
    
    //get the speed of the fabric
    public int GetSpeed()
    {
        return speed;
    }
    
}
