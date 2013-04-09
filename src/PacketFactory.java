
import java.util.*;
import java.net.InetAddress;

/*
 * This class implements the creation of packets
 */
public class PacketFactory
{
    Random rnd;
    //holds the seed of the simulator
    long SEED;
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
    //holds the distribution type
    private Distribution dType;
    
    public PacketFactory(int totalNumberOfPackets,int packetSize, int totalNumberOfInputBuffers, long SEED)
    {
        //initialize the seed used by the simulator
        this.SEED = SEED;
        //initialize the random
        rnd = new Random(this.SEED);
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
    //remove packets from simulation output buffer
    public void Distribute(ConfigFile cfg)
    //public void XConsumePackets(int TIME, Queue<RouterPacket> []inputBuffer, PriorityQueue<Event> pQ,int PULSE, Event current)
    {
        //temporary hold the queue from distribution calculations
        Queue tmp = null;

        //double discard = 0;
        int discard = 0;
                
        //holds the number of packet to create
        int NumberOfPackets = totalNumberOfPackets;
        
        //holds the mean of the OUTPUT-DISTRIBUTION from the config file
        double mean = Double.parseDouble(cfg.GetConfig("FACTORY-DISTRIBUTION","Mean").toString());
        //holds the Standard Deviation of the OUTPUT-DISTRIBUTION from the config file
        double stdDev = Double.parseDouble(cfg.GetConfig("FACTORY-DISTRIBUTION","Deviation").toString());
        
        //for packet times delivery, seed to keep the same number of the simulator
        Random rnd1 = new Random(SEED);

        //defaults to 1 unless packet count > 1
        int NumberOfTimesPacketsAreDeliverd = 1;
        //or else the random will be 0
        if(NumberOfPackets > 0)
        {
            do{

                //based on the number of packets in output buffer,
                //a random number is chosen for the number of times packet delivery happens
                NumberOfTimesPacketsAreDeliverd = rnd1.nextInt(NumberOfPackets+1);
            }
            while(NumberOfTimesPacketsAreDeliverd == 0);
        

//***************************************************************        
//NEED TO USE THE DISTRIBUTION TYPE TO REMOVE PACKETS APPROPIATELY                  
                
            

            //check if OUTPUT-DISTRIBUTION = Exponential
            if(((String)cfg.GetConfig("FACTORY-DISTRIBUTION","Type")).
                    compareToIgnoreCase("Exponential") == 0)
            {
                //create class, set the number of time the packets are broken up
                dType = new ExponentialDistribution(NumberOfTimesPacketsAreDeliverd, SEED);
            }
                
            //check if OUTPUT-DISTRIBUTION = Uniform
            else if(((String)cfg.GetConfig("FACTORY-DISTRIBUTION","Type")).
                    compareToIgnoreCase("Uniform") == 0)
            {
                //create class, set the number of time the packets are broken up
                dType = new UniformDistribution(mean, stdDev,SEED);
                
            }
            //check if OUTPUT-DISTRIBUTION = Normal
            else if(((String)cfg.GetConfig("FACTORY-DISTRIBUTION","Type")).
                    compareToIgnoreCase("Normal") == 0)
            {
                //create class, set the number of time the packets are broken up
                dType = new NormalDistribution(mean, stdDev,SEED);
            }
             
            //set the mean
            dType.SetMean(mean);
            //set how many packets present
            dType.SetNumebrOfPackets(NumberOfPackets);
            //get the distribution of the packets to remove
            dType.getDistribution();

            //tmp = dType.GetDistributionQueue();
            
        
        /*
        
            if((timing[bufferNumber] != null) && (inputBuffer[bufferNumber].size() > 0))
            {
                try
                {
                    //remove a packet from the outputBuffers chosen at random
                    RouterPacket rp = (RouterPacket)inputBuffer[bufferNumber].peek();
                    if(((String)cfg.GetConfig("GENERAL","Verbose")).compareToIgnoreCase("True") == 0)
                    {
                        System.out.println("Time: "+TIME+"    <Consuming> packet: "+rp.GetSequenceNumber()+"   from OutputBuffer[ "+ (bufferNumber+1) + "] = "+outputBuffer[bufferNumber].size());
                    }
                }
                catch(Exception e)
                {
                    //System.out.println("Time: "+TIME+"    NO packets to remove from OutputBuffer[ "+ bufferNumber + "]");
                    //break;
                }

                //plus tick to discard the packet from the buffer
                //discard = Double.parseDouble(timing[bufferNumber].remove().toString());
                discard = (int)timing[bufferNumber].remove();
//discard = (int)timing[bufferNumber].remove();
                try
                {
                    //add 1 Discard Packet events to the simulator
                    //Event evt = new Event(TIME +(int)discard+PULSE, "DiscardPacket");
                    Event evt = new Event(TIME +(int)discard, "DiscardPacket");
                    //get how many packets to discard
                    int numberOfPacketsToDiscard = (Integer)cfg.GetConfig("CLASSCONSUMPTIONRATES",
                        (String)cfg.GetConfig("OUTPUTBUFFERSCLASS", ("buffer"+(bufferNumber+1)) ));
                    
                    evt.SetBusReleaseInfo(bufferNumber, 0, numberOfPacketsToDiscard, 0);
                    pQ.add(evt);
                }
                catch(Exception e){}

            
    //*************************************************************** 
            }
            */
        }
          
        //update the next Consumption Bus event time
        //current.SetTicks(TIME+(int)discard+PULSE);
        //put the updated Consumption Bus event back in the ready queue
        //pQ.add(current);
        
        
 
    }
    
//*****************************************************************************    
    //deliver packet to simulator input buffer
    public void DeliverPacket(Queue<RouterPacket> []inputBuffer, ConfigFile cfg, int TIME, Router router)
    {
        //input buffer number
        int bufferNumber = 0;
        
        
//***************************************************
// implement the factory distribution technique     
        
        if((dType == null) || (dType.GetDistributionQueue().size() == 0))
            Distribute(cfg);
        //dType.GetDistributionQueue();
        int pkts = (int)dType.GetDistributionQueue().remove();
                
                //input buffer number
                bufferNumber = rnd.nextInt(this.totalNumberOfInputBuffers);
//***************************************************        

//***************************************************************        
//NEED TO USE THE DISTRIBUTION TO DELIVER PACKETS APPROPRIATELY  
//for(int y=0;y<inputBuffer.length;y++)
//for(int y=0;y<1;y++)
    int y = bufferNumber;
    //for(int x=0;x<1;x++)
    for(int x=0;x<pkts;x++)
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
                    
                    if(((String)cfg.GetConfig("GENERAL","Verbose")).compareToIgnoreCase("True") == 0)
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
                    
                    //input average packets  
                    router.inAvgPkts[y] += inputBuffer[y].size();
                    router.inAvgPkts[y] /=2;
                }
                else if((inputBuffer[y].size() + 1) > limit)
                {
                    //gather how many times the buffer was full
                    router.inFull[y] += 1;
                    //how many packets in total dropped
                    router.inputtotalDroppedPkts += 1;
                    //gather how many times packets were dropped
                    router.inDroppedPkts[y] += 1;
                    
                    if(((String)cfg.GetConfig("GENERAL","Verbose")).compareToIgnoreCase("True") == 0)
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
