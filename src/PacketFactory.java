
import java.util.*;
import java.net.InetAddress;

/*
 * This class implements the creation of packets
 */
public class PacketFactory implements Runnable
{
    //holds a reference to the Router object
    Router sim;
    //holds the packet sequence number
    int sequence;
    //holds the total number of packets to make
    int totalNumberOfPackets; 
    //holds the number of packets delivered
    int packetsDelivered;
    //holds the total number of input buffers available
    int totalNumberOfInputBuffers;
    //holds the size of a packet
    int packetSize;
    
    public PacketFactory(Router sim, int totalNumberOfPackets,int packetSize, int totalNumberOfInputBuffers)
    {
        //set reference to the running simulation
        this.sim = sim;
        //initialize the packet sequence
        sequence = 1;
        //ensure at least 1 packet is created
        if (totalNumberOfPackets < 1)
        {
            //default number of packets
            totalNumberOfPackets = 1;
        }
        //set the total number of packets to make
        this.totalNumberOfPackets = totalNumberOfPackets;
        //set packets delivered to the simulation
        packetsDelivered = 0;
        //set the total number of input buffers
        this.totalNumberOfInputBuffers = totalNumberOfInputBuffers;
        //set the size of a packet
        this.packetSize = packetSize;
    }
    
    //make data
    private byte[] CreateData()
    {
        //create byte buffer for data
        byte[] buf = new byte[packetSize];
        
        //holds the character bytes generated
        StringBuilder s = new StringBuilder();
        
        char c;
        
        //generate data for the packet
        for(int x=0; x< (packetSize/26);x++)
        {
            for(int y=0; y<26; y++)
            {
                s.append( Character.toString((char)(y+65) ));
            }
        }
        for(int x=0; x< (packetSize%26);x++)
        {
            s.append(Character.toString((char)(x+65) ));
        }
            
        //return byte of characters
        return buf = s.toString().getBytes();
    }
    
    //create packet
    private RouterPacket CreatePacket()
    {
        //holds the packet created
        RouterPacket pck = null;
        
        //create byte buffer with data
        byte[] buf = CreateData();
        
        try
        {
            //make the packet
            pck = new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999,
                                                sim.GetTime(), sequence);
            //increment packet sequence
            sequence += 1;
        }
        catch(Exception e)
        {
            System.out.println("Error creating packet");
        }
        
        //return packet generated
        return pck;
    }
    
    //deliver packet to input buffer
    private void DeliverPacket(int rndType)
    {
        //input buffer number
        int bufferNumber = 0;
        
        switch(rndType)
        {
            case 1:
//***************************************************
                Random rnd = new Random();
                //input buffer number
                bufferNumber = rnd.nextInt(this.totalNumberOfInputBuffers);
            break;
//***************************************************        
        }
        //the packet created
        RouterPacket tmp = CreatePacket();
        //deliver a packet to the simulation
        sim.AddInputPacket(tmp, bufferNumber);



        //increment the number of packets delivered
        packetsDelivered += 1;
    }
    public void run()
    {
        while (packetsDelivered < totalNumberOfPackets)
        {
            DeliverPacket(1);
        }
    }
    
}
