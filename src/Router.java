/*
 * This class is the main simulator class
 */

import java.util.*;
import java.net.*;

public class Router {

    //holds the clock time
    int TIME;
    //holds the tick interval an attempt to move a packet occurs
    int PULSE;
    
    
    //holds the ready queue
    private PriorityQueue<Event> readyQueue;
    
    //holds the input buffer(s) queue
    private Queue<RouterPacket> []inputBuffer;
    //holds the output buffer(s) queue
    private Queue<RouterPacket> []outputBuffer;
    //holds the number of input buffers
    private final int INPUTBUFFERS;
    //holds the number of output buffers
    private final int OUTPUTBUFFERS;
    //holds the switching fabric
    private SwitchingFabric sFabric;
    //holds the name of the Fabric Type
    private String FABRICTYPE;
    //holds the currently selected input buffer
    int FROM;
    //holds the currently selected output buffer
    int TO;
    
    //read the configuration file
    ConfigFile cfg;
    /*
     * Configuration variables
     */
    //size of packet
    int PACKETSIZE;
    //speed of fabric
    int FABRICSPEED;
    //number of packet created in the simulation
    int totalNumberOfPackets;
    //number of packet moved by the Fabric
    int NumberOfPacketsMoved;
    //************************
    
    
    //constructor
    //public Router(int INPUTBUFFERS, int OUTPUTBUFFERS, int pulse, int packetSize, int fabricSpeed, int totalNumberOfPackets)
    public Router()
    {
        //read the configuration file
        cfg = new ConfigFile();
        
        //set the clock time
        TIME = 0;
        //set the pulse of the router
        PULSE = (Integer)cfg.GetConfig("ROUTER", "pulse");
        //set the packet size
        PACKETSIZE = (Integer)cfg.GetConfig("PACKET", "size");
        //set the speed of the fabric
        FABRICSPEED = (Integer)cfg.GetConfig("FABRIC", "speed");
        //set the speed of the fabric
        FABRICTYPE = (String)cfg.GetConfig("FABRIC", "type");
        //set the total number of packets 
        this.totalNumberOfPackets = (Integer)cfg.GetConfig("PACKET", "quantity");
        //set the total number of packets moved by the Fabric
        NumberOfPacketsMoved = 0;
        
        
        //initialize the priority ready queue
        readyQueue = new PriorityQueue<Event>();
        //save the number of input buffers
        this.INPUTBUFFERS = (Integer)cfg.GetConfig("BUFFERS", "input");
        //save the number of output buffers
        this.OUTPUTBUFFERS = (Integer)cfg.GetConfig("BUFFERS", "output");
        //initialize the input buffers
        inputBuffer = new LinkedList[this.INPUTBUFFERS];
        //initialize the output buffers
        outputBuffer = new LinkedList[this.OUTPUTBUFFERS];
        
        //interate all buffers
        for(int x=0; x<INPUTBUFFERS; x++)
        {
            //initialize each input buffer
            inputBuffer[x] = new LinkedList();
        }
        
        //interate all buffers
        for(int x=0; x<OUTPUTBUFFERS; x++)
        {
            //initialize each output buffer
            outputBuffer[x] = new LinkedList();
        }
        
        //calls the method to configure the simulator
        CofigureSimulator();
        
        //runs the simulator
        RunSimulator();
    }

    //return simulation time
    public int GetTime()
    {
        return TIME;
    }
           
    //adds a packet to the input buffer indicated
    public void AddInputPacket(RouterPacket dPacket, int bufferNumber)
    {
        //put a packet in the chosen buffer
        inputBuffer[bufferNumber].add(dPacket);
        
System.out.println("Time: "+GetTime()+" -->   Delivered packet#: "+dPacket.GetSequenceNumber()+"   to  InputBuffer: "+bufferNumber+"   PacketTIME: "+dPacket.GetTimeCreated());
    }
    
    /*
     * loads the configuration file and configures the 
     * simulation
     */
    private void CofigureSimulator()
    {
        ConfigFile cfg = new ConfigFile();
        
        
        if (FABRICTYPE.compareToIgnoreCase("Bus") == 0)
        {
            //create the fabric type, and pass input & output buffer 
            this.sFabric = new Bus(FABRICSPEED,inputBuffer,outputBuffer);
        }
        else if (FABRICTYPE.compareToIgnoreCase("Memory") == 0)
        {
            //create the fabric type, and pass input & output buffer 
            this.sFabric = new Memory(FABRICSPEED,inputBuffer,outputBuffer);
        }
        if (FABRICTYPE.compareToIgnoreCase("Crossbar") == 0)
        {
            //create the fabric type, and pass input & output buffer 
            this.sFabric = new Crossbar(FABRICSPEED,inputBuffer,outputBuffer);
        }
//**********************    T E S T I N G   ****************  
/*
        PacketFactory pf = new PacketFactory(this,totalNumberOfPackets,PACKETSIZE ,INPUTBUFFERS);
        
        try
        {

            for(int y=0; y<INPUTBUFFERS; y++)
                for(int x=0; x<4; x++)
                {
                    
                    AddInputPacket(pf.CreatePacket(),y);
                    
                }
        }
        catch(Exception e)
        {
            System.out.println("Error");
        }
*/
//**********************************************************        
        
        
    }
    
    public void RunSimulator()
    {
//**************************************************************         
        //bus used to move the packet
        int busUsed;
       
        //generate random number
        Random st = new Random();
//**************************************************************   
        //holds the current event
        Event current;
        
        //create all packets needed
        PacketFactory pFactory = new PacketFactory(totalNumberOfPackets,PACKETSIZE, INPUTBUFFERS );
        
        //add Capture Available Bus events to the simulator
        readyQueue.add(new Event(TIME + PULSE, "CaptureBus"));
        
        
        //begin the simulation
        while((readyQueue.size() != 0) && (NumberOfPacketsMoved < totalNumberOfPackets))
        {
                        
            //get the next event from the queue
            current = readyQueue.poll();
            //update the simulator time
            TIME = current.GetTicks();
            
            //checkevent for Capture Available Bus
            if (current.GetActionToBeTaken().compareToIgnoreCase("CaptureBus") == 0)
            {
                //update the next Capture Available Bus events time
                current.SetTicks(current.GetTicks()+PULSE);
                //put the updated Capture Available Bus back in the event queue
                readyQueue.add(current);
                
                //holds peek at the RouterPacket sequence number
                RouterPacket peekPacket; 
                
//*******************************************************************
//NEED TO USE THE APPROPIATE Random Distribution
                //get another buffer
                FROM = st.nextInt(INPUTBUFFERS);
                    
                //ensure there's packet(s) in the selected input buffer, to be switched
                while(((peekPacket = (RouterPacket)inputBuffer[FROM].peek()) == null) && 
                       (NumberOfPacketsMoved < totalNumberOfPackets))
                {
                    
                    
//*************************                    
//NEED TO ALLOCATE PROPERLY                    
//puts 15 packets in the the Input Buffers, every time                
pFactory.DeliverPacket(1, inputBuffer, cfg);
//*************************


                    //get another buffer
                    FROM = st.nextInt(INPUTBUFFERS);
                }
                
                TO = st.nextInt(OUTPUTBUFFERS);
            
//*******************************************************************                
                
                //attempt to capture a free bus in the fabric
                /*
                 * Memory Fabric -  Maximum 1 bus = bus 0
                 * Bus Fabric - Maximum 1 bus = bus 0
                 * Crossbar Fabric - Maximum 1 INFINITY(max integer)
                 */ 
System.out.println("Time: "+TIME+"   Attempting to capture a Bus -> ");    
//******************************************************************************
//CANNOT BE '0', NEED VARIABLE, PROBLEM FOR Crossbar
//if(sFabric.SetBusActiveStatus(0,FROM,peekPacket.GetSequenceNumber()) == true)
if(sFabric.SetBusActiveStatus(0,FROM,peekPacket.GetSequenceNumber()) == true)
//******************************************************************************    
                {
System.out.println("    Got Bus: "+sFabric.GetRecentBus()+"    for Packet#: "+sFabric.sequence);                    
                    //add fabric switching events to the simulator
                    readyQueue.add(new Event(TIME + sFabric.GetSpeed(), "FabricSwitching"));
                }
            }
            
            //checkevent for fabric switching
            else if (current.GetActionToBeTaken().compareToIgnoreCase("FabricSwitching") == 0)
            {
                /*
                //update the next fabric switch time
                current.SetTicks(current.GetTicks()+sFabric.GetSpeed());
                //put the updated fabric switch time back in the event queue
                readyQueue.add(current);
                */
                
                //size limit of the Input buffer
                int limit;
                
                try
                {
                    //get buffer size limit
                    limit = (Integer)cfg.GetConfig("CLASS",
                                (String)cfg.GetConfig("OUTPUTBUFFERSCLASS", ("buffer"+(TO+1)) ));
                }
                catch (Exception e){
                    //apply default limit if Class not specified for Buffer in [INPUTBUFFERSCLASS]
                    limit = (Integer)cfg.GetConfig("CLASS",
                                (String)cfg.GetConfig("OUTPUTBUFFERSCLASS", ("Default") ));
                }

                //ensure that the Output Buffer limit(s) are obeyed
                if((outputBuffer[TO].size() + 1) <= limit)
                {
System.out.println("Input["+(FROM+1)+"]" +" = "+inputBuffer[FROM].size()+"    --> Output["+(TO+1)+"]" + " = "+ outputBuffer[TO].size());
//*******************************************************************            
                    //randomly move packets
                    busUsed = sFabric.MovePacket(FROM,TO, TIME);
                    //increment packets moved from INPUT to OUTPUT Buffer
                    NumberOfPacketsMoved += 1;
                
//*******************************************************************
System.out.println("Time: "+ TIME + "   Input["+(FROM+1)+"]" +" = "+
        inputBuffer[FROM].size()+"    --> Output["+(TO+1)+"]" + " = "+ 
        outputBuffer[TO].size()+"    <- Packet : "+
        sFabric.GetCurrentPacketUsingTheBus()+
        "   Created: "+sFabric.GetRecentPacket().GetTimeCreated()+
        "   Delivered: "+sFabric.GetRecentPacket().GetTimeDeliverd()+" ->\n");
//******************************************************************* 
                }
                else
                {
System.out.println("Time: "+ TIME + "    --> Output["+(TO+1)+"]" + " = "+ 
        outputBuffer[TO].size()+"    Packet(s)"+ "    Cannot deliver Packet:"+
        sFabric.GetCurrentPacketUsingTheBus()+ "    --> Output["+(TO+1)+"]" +
        " -> F U L L\n");                    
                    //get the recent bus used by the fabric
                    busUsed = sFabric.GetRecentBus();
                }
                //release the Bus used to send packet
                sFabric.SetBusInActiveStatus(busUsed, FROM,sFabric.GetCurrentPacketUsingTheBus());
            }
        }
        
    }
    
    public static void main(String[] args) {
//**************     T E S T I N G  ***********************        
        int inputBuffers = 4;
        int outputBuffers =4;
        int pulse = 5; //every two ticks the router attempt to move a packet
        int packetSize = 15;
        int fabricSpeed = 2;
        int totalNumberOfPackets = 1000; //number of packets to make
//*********************************************************
        
        //Begin the simulation
        Router sim = new Router();
        
    }
}
