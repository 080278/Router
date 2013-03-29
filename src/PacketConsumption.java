
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.*;
import java.net.InetAddress;

/*
 * This class implements the creation of packets
 */
public class PacketConsumption
{

    //holds the current time of the consumption
    int TIME;
    //holds the configuration file
    ConfigFile cfg;
    
    public PacketConsumption(int TIME, ConfigFile cfg)
    {
        
        //set the current time of the consumption
        this.TIME = TIME;
        //set the configuration file
        this.cfg = cfg;
    }
    
    
//*****************************************************************************    
    //remove packets from simulation output buffer
    public void ConsumePackets(int rndType, Queue<RouterPacket> []outputBuffer)
    {
        //holds the buffer to remove the packets from        
        int bufferNumber = -1;
        
        switch(rndType)
        {
            case 1:
//***************************************************************        
//NEED TO USE THE DISTRIBUTION TO DELIVER PACKETS APPROPIATELY                  
                Random rnd = new Random();
                //input buffer number
                bufferNumber = rnd.nextInt(outputBuffer.length);
            break;
//***************************************************************        
        }

        
        int totalNumberOfPackets;
        try
        {
            //get buffer size limit
            totalNumberOfPackets = (Integer)cfg.GetConfig("CLASSCONSUMPTIONRATES",
                        (String)cfg.GetConfig("OUTPUTBUFFERSCLASS", ("buffer"+bufferNumber) ));
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
                outputBuffer[bufferNumber].remove();
            }
            catch(Exception e)
            {
                System.out.println("NO packets to remove from OutputBuffer[ "+ bufferNumber + "]");
                break;
            }
        }
    }
        
}
