
/*
 * This implements the Bus switching fabric
 */

import java.util.*;
import java.net.*;

public class Memory extends SwitchingFabric{
    
    //define the number of busses
    private static final int VERTICALBUSES = 1;
    //holds the last packet moved
    RouterPacket recentPacket;
    //holds the last bus captured
    int recentBus;
    //hold packets in Switching Fabric internal memory
    ArrayList<RouterPacket> internalMemory;
    
    //constructor
    public Memory(int speed, Queue []inputBuffers, Queue []outputBuffers)
    {
        //passes the constructor values to the base class SwitchingFabric
        super(speed, inputBuffers, outputBuffers, VERTICALBUSES);
        //indicate has no memory
        hasMemory = true;
        //set the recent bus, because it never changes for this fabric type
        recentBus = 0;
        //hold packets in Switching Fabric internal memory
        internalMemory = new ArrayList<RouterPacket>();
    }
    
    //find packet with sequence number in internalMemory
    public int FindRouterPacket(int seq)
    {
        int result = -1;
        //search for the sequence number
        for(int x=0; x<internalMemory.size();x++)
        {
            //compare each packet sequence with the sequence number
            if(internalMemory.get(x).GetSequenceNumber() == seq)
            {
                //set the index of packet if sequence matches
                result = x;
            }
        }
        
        //return index of packet
        return result;
    }
    
    //get the internalMemory size
    public int GetInternalMemorySize()
    {
        return internalMemory.size();
    }
    
    //get RouterPacket in internalMemory
    public RouterPacket GetInternalMemoryRouterPacket(int index)
    {
        return internalMemory.get(index);
    }
    
    //moves a packet from internalMemory to outputBuffer
    public int MoveInternalMemoryPacket(int inputBufferNumber,  int TIME)
    {
        //ONLY ONE BUS PRESENT IN THIS FABRIC
        //if Active status was successful and the flag is true for having
        //control of the bus

        //set packet moved default
        packetMoved = false;
        
        //get the RouterPacket in the InputBuffer selected
        RouterPacket peekPacket = (RouterPacket)internalMemory.get(inputBufferNumber);

        //esure there is a packet to move, packet activate bus successfully,bus becomes active 
        if((peekPacket != null) && (sequence[0] == peekPacket.GetSequenceNumber()) && 
           (GetBusActiveStatus(0)== true))
        {
            //ensure there is packet in the internalMemory
            if(internalMemory.size() > 0)
            {
//System.out.println("MOVEPACKET FROM: "+inputBufferNumber+" TO: "+outputBufferNumber);                                
                //get RouterPacket from Internal Memory
                RouterPacket rPacket = (RouterPacket)internalMemory.remove(inputBufferNumber);
                
                //update the time delivered
                rPacket.SetTimeDelivered(TIME);
                
                //set the recent packet moved
                recentPacket = rPacket;
                
                //set the recent bus used
                recentBus = 0;
                
                //set the output buffer of the packet
                //rPacket.SetOutputBuffer(rPacket.GetOutputBuffer());
                
                //move the data to the outputBuffer
                outputBuffers[rPacket.GetOutputBuffer()].add(rPacket);
                
                //move the data to the output buffer
                //this.outputBuffers[outputBufferNumber].add(rPacket);
                
                //set packet successfully moved 
                packetMoved = true;
                
            }
        }
        
        //tell the bus used to send the packet, has only 1 bus
        return recentBus;
    }
    
    //moves a packet from input buffer to internalMemory
    public int MovePacket(int inputBufferNumber, int outputBufferNumber, int TIME)
    {
        //ONLY ONE BUS PRESENT IN THIS FABRIC
        //if Active status was successful and the flag is true for having
        //control of the bus

        //set packet moved default
        packetMoved = false;
        
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
                
                //set the input buffer of the packet
                rPacket.SetInputBuffer(inputBufferNumber);
                
                //set the output buffer of the packet
                rPacket.SetOutputBuffer(outputBufferNumber);
                
                //move the data to the internalMemory buffer
                this.internalMemory.add(rPacket);
                
                //move the data to the output buffer
                //this.outputBuffers[outputBufferNumber].add(rPacket);
                
                //set packet successfully moved 
                packetMoved = true;
                
            }
        }

        //tell the bus used to send the packet, has only 1 bus
        return recentBus;
    }
    
    //set the Active status of the bus
    public boolean SetBusActiveStatus(int busNumber, int inputBufferNumber, int packetSequence, int TIME, ConfigFile cfg)
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
//System.out.println("busNumber :- "+(busNumber+1)+ "   SIZE of currentInputBufferUsingTheBus: "+currentInputBufferUsingTheBus.length);                
                //set the status of the bus
                busActiveStatus[busNumber] = true;
                
                //remove the bus from available buses
                availableBuses.remove(busNumber);
                
                //add input buffer connected to a bus
                inputConnectedToBus.add((Object)inputBufferNumber);

                //keep track of the buffer using the bus
                currentInputBufferUsingTheBus[busNumber] = inputBufferNumber;
                
                //keep track of the packet sequence using the bus
                sequence[busNumber] = packetSequence;
                
                //set the recently used bus
                recentBus = busNumber;
                
if(((String)cfg.GetConfig("GENERAL","Verbose")).compareToIgnoreCase("True") == 0)
{                
    Print(true, busNumber,packetSequence,true,TIME); 
}
                //successfully controlled the bus
                return true;
            }
        }
        
if(((String)cfg.GetConfig("GENERAL","Verbose")).compareToIgnoreCase("True") == 0)
{        
    Print(false, busNumber,packetSequence,true,TIME);   
}
        //was unable to control the bus
        return false;
    }
    
    //set the InActive status of the bus
    public boolean SetBusInActiveStatus(int busNumber, int inputBufferNumber, int packetSequence, int TIME, ConfigFile cfg)
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
                
                //add the bus back to available buses
                availableBuses.add(busNumber);
                
                //remove input buffer NOT connected to a bus
                inputConnectedToBus.remove((Object)inputBufferNumber);
                
                //no buffer using the bus
                currentInputBufferUsingTheBus[busNumber] = -1;
                
                //no packet using the bus
                sequence[0] = -1;
                
if(((String)cfg.GetConfig("GENERAL","Verbose")).compareToIgnoreCase("True") == 0)
{                
    Print(true, busNumber,packetSequence,false,TIME); 
}
                //successfully released the bus
                return true;
            }
        }
        
if(((String)cfg.GetConfig("GENERAL","Verbose")).compareToIgnoreCase("True") == 0)
{        
    Print(false, busNumber,packetSequence,false,TIME);  
}
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
        
    public void Print(boolean result, int ActiveTO, int sequence, boolean active,int TIME)
    {
        if(active)
        {
            if(result)        
                System.out.println("Time: "+ TIME +"    Attempting Bus: "+ (ActiveTO+1) +"   Set -> ACTIVE      Packet: "+sequence);
            else{         
                System.out.print("\nTime: "+ TIME +"    Attempting Bus: "+ (ActiveTO+1) +"   [Already Active]"+"   Packet: "+sequence );        
/*                
                for(int x=0; x<inputBuffers.length; x++)
                {
                    System.out.print("   InB"+(x+1)+": "+inputBuffers[x].size());
                }
*/ 
                System.out.print("\n");                
            }
        }
        else
        {
            if(result)        
                System.out.println("Time: "+ TIME +"    Attempting Bus: "+ (ActiveTO+1) +"   Set -> INACTIVE    Packet: "+sequence);
            //else                
                //System.out.println("    Bus: "+ (ActiveTO+1) +"   [Already InActive] Packet: "+sequence+"\n");        
        }
    }
/*    
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
        tst.SetBusInActiveStatus(0,0,0000,0);
        tst.MovePacket(1, 2,999);
        tst.SetBusInActiveStatus(0,1,0000,0);
        tst.MovePacket(0, 2,999);
    }
*/    
}
