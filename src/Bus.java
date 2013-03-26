
/*
 * This implements the Bus switching fabric
 */

import java.util.*;
import java.net.*;

public class Bus extends SwitchingFabric{
    
    //define the number of busses
    private static final int VERTICALBUSES = 1;
    //holds the last packet moved
    RouterPacket recentPacket;
    //holds the last bus captured
    int recentBus;
    
    //constructor
    public Bus(int speed, Queue []inputBuffers, Queue []outputBuffers)
    {
        //passes the constructor values to the base class SwitchingFabric
        super(speed, inputBuffers, outputBuffers, VERTICALBUSES);
        //set the recent bus, because it never changes for this fabric type
        recentBus = 0;
    }
    
    //moves a packet from input buffer to output buffer
    public int MovePacket(int inputBufferNumber, int outputBufferNumber, int TIME)
    {
        //ONLY ONE BUS PRESENT IN THIS FABRIC
        //if Active status was successful and the flag is true for having
        //control of the bus
        
        //get the RouterPacket in the InputBuffer selected
        RouterPacket peekPacket = (RouterPacket)inputBuffers[inputBufferNumber].peek();

        //esure there is a packet to move, packet activate bus successfully,bus becomes active 
        if((peekPacket != null) && (sequence[0] == peekPacket.GetSequenceNumber()) && 
           (GetBusActiveStatus(0)== true))
        {
            //ensure there is packet in the buffer
            if(inputBuffers[inputBufferNumber].size() > 0)
            {
//System.out.println("MOVEPACKET FROM: "+inputBufferNumber+" TO: "+outputBufferNumber);                                
                //get RouterPacket from input buffer
                RouterPacket rPacket = (RouterPacket)inputBuffers[inputBufferNumber].remove();
                
                //update the time delivered
                rPacket.SetTimeDelivered(TIME);
                
                //set the recent packet moved
                recentPacket = rPacket;
                
                //set the recent bus used
                recentBus = 0;
                
                //move the data to the output buffer
                this.outputBuffers[outputBufferNumber].add(rPacket);
                
            }
        }

        //tell the bus used to send the packet, has only 1 bus
        return recentBus;
    }
    
    //set the Active status of the bus
    public boolean SetBusActiveStatus(int busNumber, int inputBufferNumber, int packetSequence)
    {
        //REMOVE IN CROSSBAR FABRIC TYPE
        //only one bus present in this fabric type, 
        busNumber = 0;
        
        //ensure valid busNumber chosen
        if(((busNumber+1)<= VERTICALBUSES) && ((busNumber+1) > 0) &&
            (busActiveStatus[busNumber] == false))
        {
            //check if bus free, or already used by a buffer
            if ((currentInputBufferUsingTheBus[busNumber] == -1) ||
                (currentInputBufferUsingTheBus[busNumber] == inputBufferNumber)) 
            {
                //set the status of the bus
                busActiveStatus[busNumber] = true;

                //keep track of the buffer using the bus
                currentInputBufferUsingTheBus[busNumber] = inputBufferNumber;
                
                //keep track of the packet sequence using the bus
                sequence[busNumber] = packetSequence;
                
                //set the recently used bus
                recentBus = busNumber;
                
Print(true, busNumber,packetSequence,true);   
                //successfully controlled the bus
                return true;
            }
        }
        
Print(false, busNumber,packetSequence,true);   
        //was unable to control the bus
        return false;
    }
    
    //set the InActive status of the bus
    public boolean SetBusInActiveStatus(int busNumber, int inputBufferNumber, int packetSequence)
    {
                
        //REMOVE IN CROSSBAR FABRIC TYPE
        //only one bus present in this fabric type, 
        busNumber = 0;
        
        //ensure valid busNumber chosen
        if(((busNumber+1)<= VERTICALBUSES) && ((busNumber+1) > 0) &&
            (busActiveStatus[busNumber] == true))
        {
            //check if bus free, or already used by a buffer
            if (((currentInputBufferUsingTheBus[busNumber] == -1) ||
                (currentInputBufferUsingTheBus[busNumber] == inputBufferNumber)) &&
                (sequence[0] == packetSequence))
            {
                //set the status of the bus
                busActiveStatus[busNumber] = false;
                
                //no buffer using the bus
                currentInputBufferUsingTheBus[busNumber] = -1;
                
                //no packet using the bus
                sequence[0] = -1;
                
Print(true, busNumber,packetSequence,false); 
                //successfully released the bus
                return true;
            }
        }
        
Print(false, busNumber,packetSequence,false);  
        //was unable to control the bus
        return false;
    }
    
    //get the recent packet moved
    public RouterPacket GetRecentPacket()
    {
        return recentPacket;
    }
    
    //get the recent bus captured
    public int GetRecentBus()
    {
        return recentBus;
    }
        
    public static void Print(boolean result, int ActiveTO, int sequence, boolean active)
    {
        if(active)
        {
            if(result)        
                System.out.println("    Bus: "+ (ActiveTO+1) +"   Set -> ACTIVE      Packet: "+sequence);
            else                
                System.out.println("    Bus: "+ (ActiveTO+1) +"   [Already Active]   Packet: "+sequence);        
        }
        else
        {
            if(result)        
                System.out.println("    Bus: "+ (ActiveTO+1) +"   Set -> INACTIVE    Packet: "+sequence+"\n");
            //else                
                //System.out.println("    Bus: "+ (ActiveTO+1) +"   [Already InActive] Packet: "+sequence+"\n");        
        }
    }
    
    public static void main(String []args)
    {
        Queue<DatagramPacket> []input= new LinkedList[4];
        Queue<DatagramPacket> []output= new LinkedList[4];
        
        for (int x=0;x<input.length;x++)
            input[x] = new LinkedList();
        for (int x=0;x<output.length;x++)
            output[x] = new LinkedList();
        
        byte[] buf = new byte[256];
        String s= "Testing 1,2,3....";
        buf = s.getBytes();
        
        try
        {
        DatagramPacket pck = new DatagramPacket(buf,buf.length,InetAddress.getByName("localhost"),9999);
        
        
        input[0].add(pck);
        input[0].add(pck);
        input[0].add(pck);
        input[0].add(pck);
        
        input[1].add(pck);
        input[1].add(pck);
        input[1].add(pck);
        input[1].add(pck);
        
        input[2].add(pck);
        input[2].add(pck);
        input[2].add(pck);
        input[2].add(pck);
        
        input[3].add(pck);
        input[3].add(pck);
        input[3].add(pck);
        input[3].add(pck);

        }
        catch(Exception e)
        {
            System.out.println("Error");
        }
        Bus tst = new Bus(75,input,output);
        tst.MovePacket(0, 2,999);
        tst.SetBusInActiveStatus(0,0,0000);
        tst.MovePacket(1, 2,999);
        tst.SetBusInActiveStatus(0,1,0000);
        tst.MovePacket(0, 2,999);
    }
    
}
