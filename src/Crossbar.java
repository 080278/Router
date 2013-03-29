/*
 * This implements the Crossbar switching fabric
 */

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.*;

public class Crossbar extends SwitchingFabric{
        
    //holds the last packet moved
    RouterPacket recentPacket;
    //holds the last bus captured
    int recentBus;
    
    //constructor
    public Crossbar(int speed, Queue []inputBuffers, Queue []outputBuffers)
    {
        //VERTICALBUSES are the number of 'OutputBuffers.length'
        //passes the constructor values to the base class SwitchingFabric
        super(speed, inputBuffers, outputBuffers, outputBuffers.length);
        //set the default recent bus
        recentBus = 0;
    }
    

    //moves a packet from input buffer to output buffer
    public int MovePacket(int inputBufferNumber, int outputBufferNumber,int TIME)
    {
        //ONLY ONE BUS PRESENT IN THIS FABRIC
        //if Active status was successful and the flag is true for having
        //control of the bus
        
        //set packet moved default
        packetMoved = false;
        
        //get the RouterPacket in the InputBuffer selected
        RouterPacket peekPacket = (RouterPacket)inputBuffers[inputBufferNumber].peek();
        
        //esure there is a packet to move, packet activate bus successfully,bus becomes active 
        if((peekPacket != null) && (sequence[outputBufferNumber] == peekPacket.GetSequenceNumber()) && 
           (GetBusActiveStatus(recentBus)== true))
        {
            //ensure there is packet in the buffer
            if(inputBuffers[inputBufferNumber].size() > 0)
            {
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
                
                //set packet successfully moved
                packetMoved = true;
                
            }
        }
        
        //tell the bus used to send the packet, has only 1 bus
        return recentBus;
    }
    //attempt to Takes control of the Fabric Bus
    public boolean SetBusActiveStatus(int busNumber, int inputBufferNumber, int packetSequence, int TIME)
    {
        //REMOVE IN CROSSBAR FABRIC TYPE
        //only one bus present in this fabric type, 
        //busNumber = 0;
        
        //indicate if the packet has a bus already active
        boolean packetAlreadyHasAnActiveBus = false;
        
        //see if this packetSequence alread has a bus
        for(int y=0; y<VERTICALBUSES; y++)
        {
            if (sequence[y] == packetSequence)
                packetAlreadyHasAnActiveBus = true;
        }
//System.out.println("busNumber :- "+(busNumber+1)+ "   SIZE of currentInputBufferUsingTheBus: "+currentInputBufferUsingTheBus.length);
        //ensure valid busNumber chosen
        if(((busNumber+1)<= VERTICALBUSES) && ((busNumber+1) > 0) &&
            (busActiveStatus[busNumber] == false) &&
            (packetAlreadyHasAnActiveBus != true))
        {
            //check if bus free, or already used by a buffer
            if ((currentInputBufferUsingTheBus[busNumber] == -1) ||
                (currentInputBufferUsingTheBus[busNumber] == inputBufferNumber)) 
            {
                //set the status of the bus
                busActiveStatus[busNumber] = true;
                
                //remove the bus from available buses
                availableBuses.remove((Object)busNumber);
                
                //add input buffer connected to a bus
                inputConnectedToBus.add((Object)inputBufferNumber);

                //keep track of the buffer using the bus
                currentInputBufferUsingTheBus[busNumber] = inputBufferNumber;
                
                //keep track of the packet sequence using the bus
                sequence[busNumber] = packetSequence;
                
                //set the recently used bus
                recentBus = busNumber;

Print(true, busNumber,packetSequence,true,TIME);                
                //successfully controlled the bus
                return true;
            }
        }
        
Print(false, busNumber,packetSequence,true,TIME);        
        //was unable to control the bus
        return false;
    }
    
    
    //attempt to Release control of the Fabric Bus
    public boolean SetBusInActiveStatus(int busNumber, int inputBufferNumber, int packetSequence, int TIME)
    {
        //REMOVE IN CROSSBAR FABRIC TYPE
        //only one bus present in this fabric type, 
        //busNumber = 0;
        
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
                
                //add the bus back to available buses
                availableBuses.add((Object)busNumber);
                
                //remove input buffer NOT connected to a bus
                inputConnectedToBus.remove((Object)inputBufferNumber);
                
                //no buffer using the bus
                currentInputBufferUsingTheBus[busNumber] = -1;
                
                //no packet using the bus
                sequence[busNumber] = -1;

Print(true, busNumber,packetSequence,false,TIME);                
                //successfully released the bus
                return true;
            }
        }
        
Print(false, busNumber,packetSequence,false,TIME);        
        //was unable to control the bus
        return false;
    }
    
    //get the recent packet moved
    public RouterPacket GetRecentPacket()
    {
        return recentPacket;
    }
    
    //get the recent packet moved
    public int GetRecentBus()
    {
        return recentBus;
    }

    public static void Print(boolean result, int ActiveTO, int sequence, boolean active,int TIME)
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
        }
    }
    public static void main(String []args)
    {
        Queue<RouterPacket> []input= new LinkedList[4];
        Queue<RouterPacket> []output= new LinkedList[4];
        
        for (int x=0;x<input.length;x++)
            input[x] = new LinkedList();
        for (int x=0;x<output.length;x++)
            output[x] = new LinkedList();
        
        byte[] buf = new byte[256];
        String s= "Testing 1,2,3....";
        buf = s.getBytes();
        
        int sequence = 0;
        int timeCreated = 5;
        int FROM = 0;
        int active = 0;
        int inactive = 0;
        boolean result = false;
        
        try
        {
        
        
        sequence += 1;
        input[0].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[0].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[0].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[0].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[0].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[0].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[0].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[0].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[0].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[0].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[0].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[0].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[1].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[1].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[1].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[1].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[2].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[2].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[2].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[2].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[3].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[3].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[3].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[3].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        
        
        }
        catch(Exception e)
        {
            System.out.println("Error");
        }
        
        Crossbar tst = new Crossbar(5,input,output);
        RouterPacket peekPacket;
                
        
//test FROM = 1,2,3        
FROM = 0;
active = 0;
        peekPacket = (RouterPacket)input[FROM].peek();
        result = tst.SetBusActiveStatus(active,FROM,peekPacket.GetSequenceNumber(),0);
Print(result, active,peekPacket.GetSequenceNumber(),true,0);

FROM = 1;
active = 1;
        peekPacket = (RouterPacket)input[FROM].peek();
        result = tst.SetBusActiveStatus(active,FROM,peekPacket.GetSequenceNumber(),0);
Print(result, active,peekPacket.GetSequenceNumber(),true,0);

FROM = 1;
inactive = 1;
        result = tst.SetBusInActiveStatus(inactive,FROM,peekPacket.GetSequenceNumber(),0);
Print(result, inactive,peekPacket.GetSequenceNumber(),false,0);


FROM = 0;
active = 2;
        peekPacket = (RouterPacket)input[FROM].peek();
        result = tst.SetBusActiveStatus(active,FROM,peekPacket.GetSequenceNumber(),0);
Print(result, active,peekPacket.GetSequenceNumber(),true,0);

FROM = 0;
active = 3;
        peekPacket = (RouterPacket)input[FROM].peek();
        result = tst.SetBusActiveStatus(active,FROM,peekPacket.GetSequenceNumber(),0);
Print(result, active,peekPacket.GetSequenceNumber(),true,0);

/*
FROM = 0;
inactive = 1;
        result = tst.SetBusInActiveStatus(inactive,FROM,peekPacket.GetSequenceNumber());
Print(result, inactive,peekPacket.GetSequenceNumber(),false);
*/


/*        
        tst.MovePacket(0, 2,000);
        tst.SetBusInActiveStatus(0,0,000);
        tst.MovePacket(1, 2,000);
        tst.SetBusInActiveStatus(0,1,000);
        tst.MovePacket(0, 2,000);
*/ 
    }
}
