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
/*        
        //if Active status was successful and the flag is true for having
        //control of the bus
        
        //get the RouterPacket in the InputBuffer selected
        RouterPacket peekPacket = (RouterPacket)inputBuffers[inputBufferNumber].peek();
        
        //esure there is a packet to move, packet activate bus successfully,bus becomes active 
        if((peekPacket != null) && (sequence == peekPacket.GetSequenceNumber()) && (GetBusActiveStatus(?)== true))
        {
            //ensure there is packet in the buffer
            if(inputBuffers[inputBufferNumber].size() > 0)
            {
                //remove RouterPacket from input buffer
                RouterPacket rPacket = (RouterPacket)inputBuffers[inputBufferNumber].remove();
                
                //update the time delivered
                rPacket.SetTimeDelivered(TIME);
                
                //set the recent packet moved
                recentPacket = rPacket;
                
                //set the recent bus used
                recentBus = ?;
                
                //move the data to the output buffer
                this.outputBuffers[outputBufferNumber].add(rPacket);
                
            }
        }
*/        
        //tell the bus used to send the packet, has only 1 bus
        return recentBus;
    }
    //attempt to Takes control of the Fabric Bus
    public boolean SetBusActiveStatus(int busNumber, int inputBufferNumber, int packetSequence)
    {
        //REMOVE IN CROSSBAR FABRIC TYPE
        //only one bus present in this fabric type, 
        //busNumber = 0;
        
        //ensure valid busNumber chosen
        if(((busNumber+1)<= VERTICALBUSES) && ((busNumber+1) > 0) &&
            (busActiveStatus[busNumber] == false))
        {
            //check if bus free, or already used by a buffer
            if ((currentInputBufferUsingTheBus == -1) ||
                (currentInputBufferUsingTheBus == inputBufferNumber)) 
            {
                //set the status of the bus
                busActiveStatus[busNumber] = true;

                //keep track of the buffer using the bus
                currentInputBufferUsingTheBus = inputBufferNumber;
                
                //keep track of the packet sequence using the bus
                sequence = packetSequence;
                
                //successfully controlled the bus
                return true;
            }
        }
        //was unable to control the bus
        return false;
    }
    
    
    //attempt to Release control of the Fabric Bus
    public boolean SetBusInActiveStatus(int busNumber, int inputBufferNumber, int packetSequence)
    {
        //REMOVE IN CROSSBAR FABRIC TYPE
        //only one bus present in this fabric type, 
        //busNumber = 0;
        
        //ensure valid busNumber chosen
        if(((busNumber+1)<= VERTICALBUSES) && ((busNumber+1) > 0) &&
            (busActiveStatus[busNumber] == true))
        {
            //check if bus free, or already used by a buffer
            if (((currentInputBufferUsingTheBus == -1) ||
                (currentInputBufferUsingTheBus == inputBufferNumber)) &&
                (sequence == packetSequence))
            {
                //set the status of the bus
                busActiveStatus[busNumber] = false;
                
                //no buffer using the bus
                currentInputBufferUsingTheBus = -1;
                
                //no packet using the bus
                sequence = -1;
                
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
        return recentPacket;
    }
    
    //get the recent packet moved
    public int GetRecentBus()
    {
        return recentBus;
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
        try
        {
        
        
        sequence += 1;
        input[0].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[1].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[2].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        sequence += 1;
        input[3].add(new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999, timeCreated, sequence));
        
        
        }
        catch(Exception e)
        {
            System.out.println("Error");
        }
        
        Crossbar tst = new Crossbar(5,input,output);
        
        FROM = 0;
        RouterPacket peekPacket = (RouterPacket)input[FROM].peek();
        tst.SetBusActiveStatus(0,FROM,peekPacket.GetSequenceNumber());
                
        
/*        
        tst.MovePacket(0, 2,000);
        tst.SetBusInActiveStatus(0,0,000);
        tst.MovePacket(1, 2,000);
        tst.SetBusInActiveStatus(0,1,000);
        tst.MovePacket(0, 2,000);
*/ 
    }
}
