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
    
//************** NEED TO CUSTOMIZE FOR CROSSBAR
    //moves a packet from input buffer to output buffer
    public void MovePacket(int inputBufferNumber, int outputBufferNumber)
    {
        //ONLY ONE BUS PRESENT IN THIS FABRIC
        //if Active status was successful and the flag is true for having
        //control of the bus
        if((SetBusActiveStatus(0, inputBufferNumber) == true) &&
           (GetBusActiveStatus(0)== true))
        {
            
            //move the data to the output buffer
            this.outputBuffers[outputBufferNumber].add(inputBuffers[inputBufferNumber].remove());
        }
    }

//************** NEED TO CUSTOMIZE FOR CROSSBAR
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

//************** NEED TO CUSTOMIZE FOR CROSSBAR    
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

}
