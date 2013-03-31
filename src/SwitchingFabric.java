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
    protected static Queue []inputBuffers;
    //holds the list of output buffers
    protected Queue []outputBuffers;
    //indicates a packet was sucessfully moved
    protected boolean packetMoved;
    //holds a list of available buses
    protected LinkedList availableBuses;
    //holds a list of inputBuffers using fabric bus
    protected LinkedList inputConnectedToBus;
//********************************************************************
//NEED TO MODIFY FOR MULTIPLE BUSES IN THE CROSS BAR FABRIC    
//IMPLEMENT USING ARRAY(S)    
    //holds the current input buffer using the bus
    //protected int currentInputBufferUsingTheBus;
    protected int []currentInputBufferUsingTheBus;
    //holds the packet sequence number using the bus(es)
    protected int []sequence;
//********************************************************************    
    
    //moves a packet from input buffer to output buffer
    public abstract int MovePacket(int inputBufferNumber, int outputBufferNumber, int TIME);
    //attempt to Takes control of the Fabric Bus
    public abstract boolean SetBusActiveStatus(int busNumber, int inputBufferNumber, int sequence, int TIME, ConfigFile cfg);
    //attempt to Release control of the Fabric Bus
    public abstract boolean SetBusInActiveStatus(int busNumber, int inputBufferNumber, int sequence, int TIME, ConfigFile cfg);
    //get the recent packet moved
    public abstract RouterPacket GetRecentPacket();
    //get the recent bus used moved
    public abstract int GetRecentBus();
    //print status of the fabric
    public abstract void Print(boolean result, int ActiveTO, int sequence, boolean active,int TIME);
    
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
        //initialize available buses
        availableBuses = new LinkedList();
        //initialize input connected to bus
        inputConnectedToBus = new LinkedList();
        //initialize buses
        sequence = new int[VERTICALBUSES];
        //initialize packetMoved
        packetMoved = false;
        //set the current input buffer occupying the fabric bus, -1 = none
//currentInputBufferUsingTheBus = new int[inputBuffers.length];
currentInputBufferUsingTheBus = new int[outputBuffers.length];
        //for(int x=0; x<(inputBuffers.length);x++)
        for(int x=0; x<(outputBuffers.length);x++)
            currentInputBufferUsingTheBus[x] = -1;
        
        
        //initialize each vertical bus to available
        for(int x=0;x< this.VERTICALBUSES;x++)
        {
            //initialize each vertical bus to available
            busActiveStatus[x] = false;
            //initialize each vertical bus to available
            availableBuses.add(x);
        }
    }
    
    //get input connection status
    public boolean GetInputBufferConnectionStatus(int buffer)
    {
        return inputConnectedToBus.contains((Object)buffer);
    }
    
    //get the size of the available bus
    public int GetAvailableBusCount()
    {
        return availableBuses.size();
    }
    
    //get bus number in availableBuses LinkedList
    public int GetBusNumberInAvailableBus(int index)
    {
        return Integer.parseInt(availableBuses.get(index).toString());
    }
    
    //get if packet was moved successfully
    public boolean GetPacketMoved()
    {
        return packetMoved;
    }
    
    //get input buffers
    public Queue[] GetInputBuffers()
    {
        return inputBuffers;
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
    public int GetCurrentPacketUsingTheBus(int busNumber)
    {
        return sequence[busNumber];
    }
    
    //get if packet sequence using a Bus
    public boolean GetSearchIfSequenceUsingTheBus(int sequence)
    {
        for(int seq:this.sequence)
        {
            if (seq == sequence)
                return true;
        }
        return false;
    }
    
    //get the speed of the fabric
    public int GetSpeed()
    {
        return speed;
    }
    
}
