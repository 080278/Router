
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
    //holds the number of packets created
    int packetsCreated;
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
        //set packets created to the simulation
        packetsCreated = 0;
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
    
    //get packets remaining in the packetCreated Queue
    public int GetPacketsRemaining()
    {
        return packetCreated.size();
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
            this.packetsCreated += 1;
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
        while (packetsCreated < totalNumberOfPackets)
        {
            
            //add created packet to packet created queue
            packetCreated.add(CreatePacket());
        }
    }

//*****************************************************************************    
    //deliver packet to simulator input buffer
    public void DeliverPacket(int rndType, Queue<RouterPacket> []inputBuffer, ConfigFile cfg, int TIME, Router router)
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
//NEED TO USE THE DISTRIBUTION TO DELIVER PACKETS APPROPRIATELY  
for(int y=0;y<inputBuffer.length;y++)
    for(int x=0;x<1;x++)
            {
                //size limit of the Input buffer
                int limit;
                
                try
                {
                    //get buffer size limit
                    limit = (Integer)cfg.GetConfig("CLASS",
                                (String)cfg.GetConfig("INPUTBUFFERSCLASS", ("buffer"+(y+1)) ));
                }
                catch (Exception e){
                    //apply default limit if Class not specified for Buffer in [INPUTBUFFERSCLASS]
                    limit = (Integer)cfg.GetConfig("CLASS",
                                (String)cfg.GetConfig("INPUTBUFFERSCLASS", ("Default") ));
                }
                
                
                
                //gather how many times a delivery attempt was made
                router.inDelivery[y] += 1;
                
                //ensure that the Input Buffer limit(s) are obeyed
                if(((inputBuffer[y].size() + 1) <= limit) && (packetsDelivered < totalNumberOfPackets))
                {
                    //get packet in the created buffer
                    RouterPacket rP = GetPacketCreated();
                    //set the time the packet is delivery to the simulation inputBuffer
                    rP.SetTimeCreated(TIME);
                    
                    //put packet in the input buffer 
                    inputBuffer[y].add(rP);
                    
                    if(((String)cfg.GetConfig("DISPLAY","Verbose")).compareToIgnoreCase("True") == 0)
                    { 
                        System.out.println("Time: "+TIME+"    <INPUT><ARRIVED>    Packet: "+(packetsDelivered+1)+
                                "    InputBuffer: "+ (y+1));
                    }
                    //count number of packets delivered
                    packetsDelivered += 1;
                    
                    //check if it is the only packet in the input buffer
                    if(inputBuffer[y].size() == 1)
                    {
                        //gather how many times the buffer was empty
                        router.inEmpty[y] += 1;
                    }
                }
                else if((inputBuffer[y].size() + 1) > limit)
                {
                    //gather how many times the buffer was full
                    router.inFull[y] += 1;
                    
                    if(((String)cfg.GetConfig("DISPLAY","Verbose")).compareToIgnoreCase("True") == 0)
{                    
System.out.println("\nTime: "+ TIME + "    --> Input["+(y+1)+"]" + " = "+ 
        inputBuffer[y].size()+"    Packet(s)"+ "    Cannot deliver Packet"+
        "    --> Input["+(y+1)+"]" +" -> F U L L\n");                    
}
                    break;
                }
                else
                {
                    break;
                }
            }
//***************************************************************        
    }
    
    
}
