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
        
        //attempt to capture each bus for input packets
        for(int x=0; x<sFabric.GetVerticalBuses(); x++)
        {
            //add Capture Available Bus events to the simulator
            readyQueue.add(new Event(TIME + PULSE, "CaptureBus"));
        }
        
        //hold input buffer available for selection
        ArrayList<Integer> bSelect = new ArrayList<Integer>();
                
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
                //ensure that a buffer is chosen once per parallel processing
                //in Crossbar to avoid the first packet activating more than 1
                //bus
                if(bSelect.size() == 0)
                {
                    //prepare list of INPUT buffers
                    for(int x=0; x<INPUTBUFFERS; x++)
                    //for(int x=0; x<sFabric.GetVerticalBuses(); x++)
                    {
                        //add each buffer
                        bSelect.add(x);
                    }
                }
                //update the next Capture Available Bus events time
                current.SetTicks(current.GetTicks()+PULSE);
                //put the updated Capture Available Bus back in the event queue
                readyQueue.add(current);
                
                //holds peek at the RouterPacket sequence number
                RouterPacket peekPacket; 
                
//*******************************************************************
//NEED TO USE THE APPROPIATE Random Distribution
                
                //FROM = st.nextInt(INPUTBUFFERS);
/*                
                //ensure there's packet(s) in the selected input buffer, to be switched
//while((peekPacket = (RouterPacket)inputBuffer[FROM].peek()) == null)
                {
//??need to chack for no packet, get new set of packets   
*/ 
//*****************************************************************************                
                    //select a buffer
                    int idx = st.nextInt(bSelect.size());
                    //get the index value
                    FROM = bSelect.get(idx);
                    
//*****************************************************************************
/*                    
                }
//?? implement, each buffer input, first packet     
*/                
                //ensure there's packet(s) in the selected input buffer, to be switched
                while(((peekPacket = (RouterPacket)inputBuffer[FROM].peek()) == null) && 
                       (NumberOfPacketsMoved < totalNumberOfPackets))
                {
//*************************                    
//NEED TO ALLOCATE PROPERLY                    
//puts 1024 packets in the the Input Buffers, every time                
pFactory.DeliverPacket(1, inputBuffer, cfg);
//*************************

//***********************************************************
//NEED TO HANDLE RANDOM PROPERLY
//get another buffer
//FROM = st.nextInt(INPUTBUFFERS);
                //start a fresh list of buffers
                bSelect.clear();
                if(bSelect.size() == 0)
                {
                    //prepare list of INPUT buffers
                    for(int x=0; x<INPUTBUFFERS; x++)
                    //for(int x=0; x<sFabric.GetVerticalBuses(); x++)
                    {
                        //add each buffer
                        bSelect.add(x);
                    }
                }
//*****************************************************************************                
                    //select a buffer
                    idx = st.nextInt(bSelect.size());
                    //get the index value
                    FROM = bSelect.get(idx);
                    
//*****************************************************************************
//***********************************************************                    
                }
                //remove the buffer already selected
                bSelect.remove(idx);
                //set the TO buffer
                TO = st.nextInt(OUTPUTBUFFERS);
            
//*******************************************************************                
                
                    //attempt to capture a free bus in the fabric
                    /*
                     * Memory Fabric -  Maximum 1 bus = bus 0
                     * Bus Fabric - Maximum 1 bus = bus 0
                     * Crossbar Fabric - Maximum 1 INFINITY(max integer)
                     */ 
//System.out.println("Time: "+TIME+"   Packet: "+peekPacket.GetSequenceNumber()+"    Buffer: "+FROM+"    Attempting to capture a Bus -> ");    

//if(sFabric.SetBusActiveStatus(TO,FROM,peekPacket.GetSequenceNumber(),TIME) == true)
if(sFabric.SetBusActiveStatus(TO,FROM,peekPacket.GetSequenceNumber(),TIME) == true)
                    {
System.out.println("Time: "+ TIME +" SetACTIVE FROM: "+FROM+" TO: "+TO+" Sequence: "+peekPacket.GetSequenceNumber());                        
//System.out.println("    Got Bus: "+(sFabric.GetRecentBus()+1)+"    for Packet#: "+sFabric.sequence);                    
                        //add fabric switching events to the simulator
                        /*
                        * release the Bus used to send packet
                        */ 
/*
                        if(sFabric instanceof Bus )
                        {
                            //always 1 bus
                            bus = 0;
                        }
*/ 
                       //create release event
                       Event evt = new Event(TIME + sFabric.GetSpeed(), "FabricSwitching");
                       //set the bus release information
                       evt.SetBusReleaseInfo(TO, FROM, peekPacket.GetSequenceNumber(),sFabric.GetRecentBus());
                       //put the event in the ready queue
                       readyQueue.add(evt);

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
                if((outputBuffer[current.GetOutputBuffer()].size() + 1) <= limit)
                {

//*******************************************************************            
                    //randomly move packets
                    //busUsed = sFabric.MovePacket(FROM,TO, TIME);
                    busUsed = sFabric.MovePacket(current.GetInputBuffer(),
                                                 current.GetOutputBuffer(), TIME);
                    //check packet was successfully moved
                    if(sFabric.GetPacketMoved())
                    {
                        //increment packets moved from INPUT to OUTPUT Buffer
                        NumberOfPacketsMoved += 1;
                    }
System.out.println("Time: "+ TIME +"    Input["+(current.GetInputBuffer())+"]" +" = "
        +inputBuffer[current.GetInputBuffer()].size()+"    --> Output["
        +(current.GetOutputBuffer())+"]" + " = "
        + outputBuffer[current.GetOutputBuffer()].size()+"    PKT(S)-Moved:"+NumberOfPacketsMoved);                
//*******************************************************************
                    /*
System.out.println("    Input["+(current.GetInputBuffer())+"]" +" = "
        +inputBuffer[current.GetInputBuffer()].size()+"    --> Output["
        +(current.GetOutputBuffer())+"]" + " = "
        + outputBuffer[current.GetOutputBuffer()].size());                    
                    */
                    
System.out.println("Time: "+ TIME + "    Input["+(current.GetInputBuffer())+"]" +" = "+
        inputBuffer[current.GetInputBuffer()].size()+"    --> Output["+(current.GetOutputBuffer())+"]" 
        + " = "+ outputBuffer[current.GetOutputBuffer()].size()+"    <- Packet : "+
        sFabric.GetCurrentPacketUsingTheBus(current.GetBus())); 
        /*sFabric.GetCurrentPacketUsingTheBus(current.GetOutputBuffer())+
        "   Created: "+sFabric.GetRecentPacket().GetTimeCreated()+
        "   Delivered: "+sFabric.GetRecentPacket().GetTimeDeliverd()+" ->");
        */

        /*
         * release the Bus used to send packet
         */ 
        //create release event
        Event evt = new Event(TIME, "ReleaseBus");
        //set the bus release information
        //evt.SetBusReleaseInfo(TO, FROM, sFabric.GetCurrentPacketUsingTheBus());
        evt.SetBusReleaseInfo(current.GetOutputBuffer()
                            , current.GetInputBuffer()
                            , current.GetSequence()
                            , current.GetBus());
        //put the event in the ready queue
        readyQueue.add(evt);
//******************************************************************* 
                }
                else
                {
System.out.println("\nTime: "+ TIME + "    --> Output["+(TO+1)+"]" + " = "+ 
        outputBuffer[TO].size()+"    Packet(s)"+ "    Cannot deliver Packet:"+
        //sFabric.GetCurrentPacketUsingTheBus(current.GetOutputBuffer())+ "    --> Output["+(TO+1)+"]" +
        sFabric.GetCurrentPacketUsingTheBus(current.GetBus())+ "    --> Output["+(TO+1)+"]" +
        " -> F U L L\n");                    
                    //get the recent bus used by the fabric
                    busUsed = sFabric.GetRecentBus();
                }
                
                
                //sFabric.SetBusInActiveStatus(busUsed, FROM,sFabric.GetCurrentPacketUsingTheBus());
                //sFabric.SetBusInActiveStatus(TO, FROM,sFabric.GetCurrentPacketUsingTheBus());
            }
            //checkevent for Release Selected Bus
            else if (current.GetActionToBeTaken().compareToIgnoreCase("ReleaseBus") == 0)
            {
                //Release Active Bus
                sFabric.SetBusInActiveStatus(current.GetOutputBuffer(), 
                                             current.GetInputBuffer(),
                                             current.GetSequence(),
                                             TIME);
/*
System.out.println("Time: "+ TIME +" SetINACTIVE FROM: "+current.GetInputBuffer()+
        " TO: "+current.GetOutputBuffer()+
        " Sequence: "+current.GetSequence()+"\n");                
*/ 
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
