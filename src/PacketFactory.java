
import java.util.*;
import java.net.InetAddress;

/*
 * This class implements the creation of packets
 */
public class PacketFactory
{
    
    //holds the packet sequence number
    int sequence;
    //holds the total number of packets to make
    int totalNumberOfPackets; 
    //holds the number of packets delivered
    int packetsDelivered;
    //holds the size of a packet
    int packetSize;
    //holds the time created
    int timeCreated;
    //holds packet created
    Queue packetCreated;
    //holds the total number of input buffers
    int totalNumberOfInputBuffers;
    
    public PacketFactory(int totalNumberOfPackets,int packetSize, int totalNumberOfInputBuffers)
    {
        
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
        //set the size of a packet
        this.packetSize = packetSize;
        //set the time created
        this.timeCreated = 0;
        //initialize the Packet Created buffers
        packetCreated = new LinkedList();
        //set the total number of input buffers
        this.totalNumberOfInputBuffers = totalNumberOfInputBuffers;
        
        //create all packets needed
        MakeAllPackets();
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
                                                timeCreated, sequence);
            //increment packet sequence
            sequence += 1;
            //increment packets created
            this.packetsDelivered += 1;
        }
        catch(Exception e)
        {
            System.out.println("Error creating packet");
        }
        
        //return packet generated
        return pck;
    }
    
    //get packet from the packet created queue
    public RouterPacket GetPacketCreated()
    {
        return (RouterPacket)packetCreated.poll();
    }
    
        
    public void MakeAllPackets()
    {
        while (packetsDelivered < totalNumberOfPackets)
        {
            
            //add created packet to packet created queue
            packetCreated.add(CreatePacket());
        }
    }

//*****************************************************************************    
    //deliver packet to simulator input buffer
    public void DeliverPacket(int rndType, Queue<RouterPacket> []inputBuffer)
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

//***************************************************************        
//NEED TO USE THE DISTRIBUTION TO DELIVER PACKETS APPROPIATELY  
        for(int y=0;y<inputBuffer.length;y++)
            for(int x=0;x<5;x++)
            {
                inputBuffer[y].add(GetPacketCreated());
            }
//***************************************************************        
    }
    
    
}
