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
    //holds the current input buffer using the bus
    protected int currentInputBufferUsingTheBus;
    
    //moves a packet from input buffer to output buffer
    public abstract int MovePacket(int inputBufferNumber, int outputBufferNumber);
    
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
        currentInputBufferUsingTheBus = -1;
        
        //initialize each vertical bus to available
        for(int x=0;x< this.VERTICALBUSES;x++)
        {
            //initialize each vertical bus to available
            busActiveStatus[x] = false;
        }
    }
    
    //set the Active status of the bus
    public boolean SetBusActiveStatus(int busNumber, int inputBufferNumber)
    {
        //ensure valid busNumber chosen
        if(((busNumber+1)<= VERTICALBUSES) && ((busNumber+1) > 0))
        {
            //check if bus free, or already used by a buffer
            if ((currentInputBufferUsingTheBus == -1) ||
                (currentInputBufferUsingTheBus == inputBufferNumber))
            {
                //set the status of the bus
                busActiveStatus[busNumber] = true;

                //keep track of the buffer using the bus
                currentInputBufferUsingTheBus = inputBufferNumber;
                //successfully controlled the bus
                return true;
            }
        }
        //was unable to control the bus
        return false;
    }
    
    //set the InActive status of the bus
    public boolean SetBusInActiveStatus(int busNumber, int inputBufferNumber)
    {
        //ensure valid busNumber chosen
        if(((busNumber+1)<= VERTICALBUSES) && ((busNumber+1) > 0))
        {
            //check if bus free, or already used by a buffer
            if ((currentInputBufferUsingTheBus == -1) ||
                (currentInputBufferUsingTheBus == inputBufferNumber))
            {
                //set the status of the bus
                busActiveStatus[busNumber] = false;
                
                //no buffer using the bus
                currentInputBufferUsingTheBus = -1;
                //successfully released the bus
                return true;
            }
        }
        //was unable to control the bus
        return false;
    }

    
    //get the active status of the bus
    public boolean GetBusActiveStatus(int busNumber)
    {
        //get the status of the bus
        return busActiveStatus[busNumber];
    }
    
    //get which buffer using the Bus
    public int GetCurrentInputBufferUsingTheBus()
    {
        return currentInputBufferUsingTheBus;
    }
}
