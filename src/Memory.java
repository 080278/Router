 /*
 * This implements the Memory switching fabric
 */

import java.net.*;
import java.util.*;


public class Memory extends SwitchingFabric{
    
    Random randomGenerator = new Random();
    //define the number of busses
    private static final int VERTICALBUSES = 1;
    
   //holds the last packet moved
    RouterPacket recentPacket;
    
    //holds the last bus captured
    int recentBus;
    
    //create linked list to hold packets in memory
    LinkedList link = new LinkedList();

    
    
    //constructor
    public Memory(int speed, Queue []inputBuffers, Queue []outputBuffers)
    {
        //passes the constructor values to the base class SwitchingFabric
        super(speed, inputBuffers, outputBuffers,VERTICALBUSES);
    }
    
    //moves a packet from input buffer to output buffer
    public int MovePacket(int inputBufferNumber, int outputBufferNumber,int TIME)
    {
        RouterPacket peekPacket = (RouterPacket)inputBuffers[inputBufferNumber].peek();
        
        //ensure there is a packet to move, packet activate bus successfully,bus becomes active 
        if((peekPacket != null) && (sequence[outputBufferNumber] == peekPacket.GetSequenceNumber()) && (GetBusActiveStatus(0)== true))
        {
            if(inputBuffers[inputBufferNumber].size() > 0)
            {
                //get RouterPacket from input buffer
                RouterPacket rPacket = (RouterPacket)inputBuffers[inputBufferNumber].remove();
                //add packet to the list 
                link.add(rPacket); 
                 //set the recent packet moved
                recentPacket = rPacket;
                //set the recent bus used
                recentBus = 0;
                //generate random number contained in the list size
                int randomInt = randomGenerator.nextInt(link.size());
                //move the data to the output buffer
            this.outputBuffers[outputBufferNumber].add(randomInt);
            }
        }
        return 0;

    }
    //attempt to Takes control of the Fabric Bus
    public boolean SetBusActiveStatus(int busNumber, int inputBufferNumber, int packetSequence, int TIME, ConfigFile cfg)
    {
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
                
                //successfully controlled the bus
                return true;
            }
        }
        //was unable to control the bus
        return false;
    }
    //attempt to Release control of the Fabric Bus
    public boolean SetBusInActiveStatus(int busNumber, int inputBufferNumber, int packetSequence, int TIME, ConfigFile cfg)
    {
         //only one bus present in this fabric type, 
        busNumber = 0;
        
        //ensure valid busNumber chosen
        if(((busNumber+1)<= VERTICALBUSES) && ((busNumber+1) > 0) &&
            (busActiveStatus[busNumber] == true))
        {
            //check if bus free, or already used by a buffer
            if (((currentInputBufferUsingTheBus[busNumber] == -1) ||
                (currentInputBufferUsingTheBus[busNumber] == inputBufferNumber)) &&
                (sequence[busNumber] == packetSequence))
            {
                //set the status of the bus
                busActiveStatus[busNumber] = false;
                
                //no buffer using the bus
                currentInputBufferUsingTheBus[busNumber] = -1;
                
                //no packet using the bus
                sequence[busNumber] = -1;
                
                //successfully released the bus
                return true;
            }
        }
        //was unable to control the bus
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
    
        public void Print(boolean result, int ActiveTO, int sequence, boolean active,int TIME)
        {
            if(active)
            {
                if(result)        
                    System.out.println("Time: "+ TIME +"    Attempting Bus: "+ (ActiveTO+1) +"   Set -> ACTIVE      Packet: "+sequence);
                else{         
                    System.out.print("Time: "+ TIME +"    Attempting Bus: "+ (ActiveTO+1) +"   Packet: "+sequence+"   [Already Active]" );        
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
        
        /*public static void main(String []args)
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
        Memory tst = new Memory(5,input,output);
        tst.MovePacket(0, 2,999);
        tst.SetBusInActiveStatus(0,0,0000);
        tst.MovePacket(1, 2,999);
        tst.SetBusInActiveStatus(0,1,0000);
        tst.MovePacket(0, 2,999);
    }*/

}
