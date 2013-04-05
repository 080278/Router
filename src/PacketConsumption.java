
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

    //holds the configuration file
    ConfigFile cfg;
    
    public PacketConsumption(ConfigFile cfg)
    {
        
        //set the configuration file
        this.cfg = cfg;
    }
    
    
//*****************************************************************************    
    //remove packets from simulation output buffer
    public void ConsumePackets(int TIME, int rndType, Queue<RouterPacket> []outputBuffer, ConfigFile cfg)
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
                if(((String)cfg.GetConfig("DISPLAY","Verbose")).compareToIgnoreCase("True") == 0)
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
    }
        
}
