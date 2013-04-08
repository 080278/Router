import java.util.*;
import java.net.InetAddress;

/*
 * This class implements the creation of packets
 */
public class PacketConsumption
{

    //holds the configuration file
    private ConfigFile cfg;
    //holds the number of output buffers
    private int OUTPUTBUFFERS;
    //holds the consumption amounts per time for each output buffer
    private Queue []timing;
    //holds the distribution type
    private Distribution dType;
    
    public PacketConsumption(ConfigFile cfg, int OUTPUTBUFFERS)
    {
        
        //set the configuration file
        this.cfg = cfg;
        //set the output buffers
        this.OUTPUTBUFFERS = OUTPUTBUFFERS;
        //create timing listing
        timing = new Queue[OUTPUTBUFFERS];
    }
    
    
//*****************************************************************************    
    //remove packets from simulation output buffer
    public void ConsumePackets(int TIME, Queue<RouterPacket> []outputBuffer, long SEED, PriorityQueue<Event> pQ,int PULSE, Event current)
    {
        //temporary hold the queue from distribution calculations
        Queue tmp = null;
      
        //holds the buffer to remove the packets from        
        int bufferNumber = -1;
        //uniform random selection of outputBuffer
        Random rnd = new Random(SEED);
        
        //output buffer number selection
        bufferNumber = rnd.nextInt(outputBuffer.length);
//******************************************************
//              DISTRIBUTION

        double discard = 0;
        //int discard = 0;
                
        //holds the number of packet found in the buffer
        int NumberOfPackets = outputBuffer[bufferNumber].size();
        //holds the mean of the OUTPUT-DISTRIBUTION from the config file
        double mean = Double.parseDouble(cfg.GetConfig("CONSUMPTION-DISTRIBUTION","Mean").toString());
        //holds the Standard Deviation of the OUTPUT-DISTRIBUTION from the config file
        double stdDev = Double.parseDouble(cfg.GetConfig("CONSUMPTION-DISTRIBUTION","Deviation").toString());
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
            if(((String)cfg.GetConfig("CONSUMPTION-DISTRIBUTION","Type")).
                    compareToIgnoreCase("Exponential") == 0)
            {
                //create class, set the number of time the packets are broken up
                dType = new ExponentialDistribution(NumberOfTimesPacketsAreDeliverd);
            }
            //check if OUTPUT-DISTRIBUTION = Uniform
            else if(((String)cfg.GetConfig("CONSUMPTION-DISTRIBUTION","Type")).
                    compareToIgnoreCase("Uniform") == 0)
            {
                //create class, set the number of time the packets are broken up
                dType = new UniformDistribution(mean, stdDev);
            }
            //check if OUTPUT-DISTRIBUTION = Normal
            else if(((String)cfg.GetConfig("CONSUMPTION-DISTRIBUTION","Type")).
                    compareToIgnoreCase("Normal") == 0)
            {
                //create class, set the number of time the packets are broken up
                dType = new NormalDistribution(mean, stdDev);
            }
            //set the mean
            dType.SetMean(mean);
            //set how many packets present
            dType.SetNumebrOfPackets(NumberOfPackets);
            //get the distribution of the packets to remove
            dType.getDistribution();

            tmp = dType.GetDistributionQueue();
            //ensure there is an entry of a distribution
            //if((timing[bufferNumber] != null) && (tmp.size() > 0))
            if(tmp.size() > 0)
            {
                //check for null,distribution queue is empty of packet discard timings
                if(timing[bufferNumber] == null) 
                {
                    timing[bufferNumber] = tmp;
                }
                else if(timing[bufferNumber].size() == 0)
                {
                    timing[bufferNumber] = tmp;
                }
            }
            
        
        
        
            if((timing[bufferNumber] != null) && (outputBuffer[bufferNumber].size() > 0))
            {
                try
                {
                    //remove a packet from the outputBuffers chosen at random
                    RouterPacket rp = (RouterPacket)outputBuffer[bufferNumber].peek();
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
                discard = Double.parseDouble(timing[bufferNumber].remove().toString());
//discard = (int)timing[bufferNumber].remove();
                try
                {
                    //add 1 Discard Packet events to the simulator
                    Event evt = new Event(TIME +(int)discard+PULSE, "DiscardPacket");
                    //get how many packets to discard
                    int numberOfPacketsToDiscard = (Integer)cfg.GetConfig("CLASSCONSUMPTIONRATES",
                        (String)cfg.GetConfig("OUTPUTBUFFERSCLASS", ("buffer"+(bufferNumber+1)) ));
                    
                    evt.SetBusReleaseInfo(bufferNumber, 0, numberOfPacketsToDiscard, 0);
                    pQ.add(evt);
                }
                catch(Exception e){}

            
    //*************************************************************** 
            }
        }
          
        //update the next Consumption Bus event time
        current.SetTicks(TIME+(int)discard+PULSE);
        //put the updated Consumption Bus event back in the ready queue
        pQ.add(current);
        
        
        
/*           
        
        int totalNumberOfPackets;
        try
        {
            //get buffer size limit
            totalNumberOfPackets = (Integer)cfg.GetConfig("CLASSCONSUMPTIONRATES",
                        (String)cfg.GetConfig("OUTPUTBUFFERSCLASS", ("buffer"+(bufferNumber+1)) ));
        }
        catch (Exception e){
            //apply default limit if Class not specified for Buffer in [OUTPUTBUFFERSCLASS]
            totalNumberOfPackets = (Integer)cfg.GetConfig("CLASSCONSUMPTIONRATES",
                        (String)cfg.GetConfig("OUTPUTBUFFERSCLASS", ("Default") ));
        }
                
        for(int y=0;y<totalNumberOfPackets;y++)
        {
            try
            {
                //remove a packet from the outputBuffers chosen at random
                RouterPacket rp = (RouterPacket)outputBuffer[bufferNumber].remove();
                if(((String)cfg.GetConfig("GENERAL","Verbose")).compareToIgnoreCase("True") == 0)
                {
                    System.out.println("Time: "+TIME+"    <CONSUMED> packet: "+rp.GetSequenceNumber()+"   from OutputBuffer[ "+ (bufferNumber+1) + "] = "+outputBuffer[bufferNumber].size());
                }
            }
            catch(Exception e)
            {
                //System.out.println("Time: "+TIME+"    NO packets to remove from OutputBuffer[ "+ bufferNumber + "]");
                break;
            }
        }
*/        
    }
    
    public void DiscardPackets(int TIME, Queue<RouterPacket> []outputBuffer, int bufferNumber, Event current, PriorityQueue<Event> pQ)
    {
        //remove the amount of packets specified to be removed in the 
        //CLASSCONSUMPTIONRATES section of the config file
        for(int y=0;y<current.GetSequence();y++)
        {
            try
            {
                //remove a packet from the outputBuffers chosen at random
                RouterPacket rp = (RouterPacket)outputBuffer[bufferNumber].remove();
                if(((String)cfg.GetConfig("GENERAL","Verbose")).compareToIgnoreCase("True") == 0)
                {
                    System.out.println("Time: "+TIME+"    <DISCARD> packet: "+rp.GetSequenceNumber()+"   from OutputBuffer[ "+ (bufferNumber+1) + "] = "+outputBuffer[bufferNumber].size());
                }
            }
            catch(Exception e)
            {
                //System.out.println("Time: "+TIME+"    NO packets to remove from OutputBuffer[ "+ bufferNumber + "]");

                //break;
            }
        }
        
        
    }
}
