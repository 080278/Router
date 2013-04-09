/*
 * This class is the main simulator class
 */

import java.util.*;
import java.net.*;

public class Router {

    //holds the distribution type
    private Distribution dType;
    //holds the clock time
    int TIME;
    //holds the tick interval an attempt to move a packet occurs
    int PULSE;
    //holds the SEED value
    long SEED;
    
    //holds the ready queue
    private PriorityQueue<Event> readyQueue;
    //holds total input dropped packets
    int inputtotalDroppedPkts;
    //holds total output dropped packets
    int outputtotalDroppedPkts;
    //holds the input buffer(s) queue
    private Queue<RouterPacket> []inputBuffer;
    //holds the input BUFFER statistics
        double []inDelivery;
        double []inFull;
        double []inEmpty;
        double []inAvgPkts;
        int []inDroppedPkts;
    //holds the output buffer(s) queue
    private Queue<RouterPacket> []outputBuffer;
    //holds the output BUFFER statistics
        private double []outDelivery;
        private double []outFull;
        private double []outEmpty;
        private double []outAvgPkts;
        private int []outDroppedPkts;
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
    //holds the currently selected bus
    int BUS;
    
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
        
        try
        {
            if(((String)cfg.GetConfig("GENERAL", "Seed")).compareToIgnoreCase("Auto") == 0)
            {
                //save the current time seed
                SEED = System.currentTimeMillis();
            }
        }
        catch(Exception e)
        {
            
        
            SEED = Long.parseLong(((Object)cfg.GetConfig("GENERAL", "Seed")).toString());
        }
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
            //initialize input buffer statistics
            inDelivery = new double[INPUTBUFFERS];
            inFull = new double[INPUTBUFFERS];
            inEmpty = new double[INPUTBUFFERS];
            inAvgPkts = new double[INPUTBUFFERS];
            inDroppedPkts = new int[INPUTBUFFERS];
        //initialize the output buffers
        outputBuffer = new LinkedList[this.OUTPUTBUFFERS];
            //initialize output buffer statistics
            outDelivery = new double[OUTPUTBUFFERS];
            outFull = new double[OUTPUTBUFFERS];
            outEmpty = new double[OUTPUTBUFFERS];
            outAvgPkts = new double[OUTPUTBUFFERS];
            outDroppedPkts = new int[OUTPUTBUFFERS];
            
        //initialize total input dropped packets
        inputtotalDroppedPkts = 0;
        //initialize total input dropped packets
        outputtotalDroppedPkts = 0;
        //interate all buffers
        for(int x=0; x<INPUTBUFFERS; x++)
        {
            //initialize each input buffer
            inputBuffer[x] = new LinkedList();
            //initialize statistics
                inDelivery[x] = 0;
                inFull[x] = 0;
                inEmpty[x] = 0;
                inAvgPkts[x] = 0;
                inDroppedPkts[x] = 0;
        }
        
        //interate all buffers
        for(int x=0; x<OUTPUTBUFFERS; x++)
        {
            //initialize each output buffer
            outputBuffer[x] = new LinkedList();
            //initialize statistics
                outDelivery[x] = 0;
                outFull[x] = 0;
                outEmpty[x] = 0;
                outAvgPkts[x] = 0;
                outDroppedPkts[x] = 0;
        }
        
        //calls the method to configure the simulator
        CofigureSimulator();
        
        //runs the simulator
        RunSimulator();
    }

    //get size of the input buffer
    public Queue<RouterPacket>[] GetInputBuffer()
    {
        return inputBuffer;
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

        
    }
    
    //temporary hold the queue from distribution calculations
    Queue tmp = null;
    //holds the consumption amounts per time for each output buffer
    private Queue []timing;
    
    
    public void RunSimulator()
    {
//**************************************************************         
        //bus used to move the packet
        int busUsed;
       
//********************************************
//need to use distribution here        
        //generate random number
        Random st = new Random(SEED);
//********************************************
        
        
        //used to select randomly between inputBuffer or internalMemory
        //for the Memory Fabric type
        //this is an internal operation, DO NOT USE Distribution for this
        Random rIn = new Random(SEED);
        //holds the current event
        Event current;
        
        //holds peek at the RouterPacket sequence number
        RouterPacket peekPacket;
        
        //create all packets needed
        PacketFactory pFactory = new PacketFactory(totalNumberOfPackets,PACKETSIZE, INPUTBUFFERS, SEED );
        //create packet consumption object
        PacketConsumption pConsumer = new PacketConsumption(cfg, OUTPUTBUFFERS, SEED);
        
        //attempt to capture each bus for input packets
        for(int x=0; x<sFabric.GetVerticalBuses(); x++)
        {
            //add Capture Available Bus events to the simulator
            readyQueue.add(new Event(TIME + PULSE, "CaptureBus"));
        }
        //add 1 Consume Packet events to the simulator
        readyQueue.add(new Event(TIME + PULSE, "ConsumePacket"));
        
        //hold input buffer available for selection
        ArrayList<Integer> bSelect = new ArrayList<Integer>();
                  
        //begin the simulation
        while((readyQueue.size() != 0) && (NumberOfPacketsMoved < totalNumberOfPackets))
        {
                        
            //get the next event from the queue
            current = readyQueue.poll();
            //update the simulator time
            TIME = current.GetTicks();
            
            //clear the inputBuffer peek packet
            peekPacket = null;
            
            //checkevent for Capture Available Bus
            if (current.GetActionToBeTaken().compareToIgnoreCase("CaptureBus") == 0) 
            {
                //HOLDS the randomly chosen Input buffer to use inputBuffer=0 OR/ internalMemory=1
                int inputType = 0;
        
                //initialize bSelect to inputBuffer if not Memory Switching Fabric
                if(sFabric.GetHasMemory() == false)
                {
                    
                    //ensure that a buffer is chosen once per parallel processing
                    //in Crossbar to avoid the first packet activating more than 1
                    //bus
                    if(bSelect.size() == 0)
                    {
                        //prepare list of INPUT buffers
                        for(int x=0; x<INPUTBUFFERS; x++)
                        {

                            if(sFabric.GetInputBufferConnectionStatus(x) == false) 
                            {
                                //add each buffer
                                bSelect.add(x);
                            }
                        }
                    }
                } 
                else
                {
                    //choose randomly between inputBuffer and internalMemory 
                    //of the Switching Fabric
//*****************************************************************

//get the randomly chosen Input buffer to use inputBuffer OR/internalMemory
inputType = rIn.nextInt(2);
//clear the list everytime an input type is decided
bSelect.clear();

if(inputType == 0)
{
        
        //prepare list of INPUT buffers
        for(int x=0; x<INPUTBUFFERS; x++)
        {
            if(sFabric.GetInputBufferConnectionStatus(x) == false) 
            {
                //add each buffer
                bSelect.add(x);
            }
        }
} 
else if(inputType == 1)
{
    
        //prepare list of Internal Memory packet locations
        for(int x=0; x<((Memory)sFabric).GetInternalMemorySize(); x++)
        {
            if(sFabric.GetInputBufferConnectionStatus(
                    //get the outputBuffer number in the InternalMemory RouterPacket
                    ((Memory)sFabric).GetInternalMemoryRouterPacket(x).GetOutputBuffer()) == false) 
            {
                //add each buffer
                bSelect.add(x);
            }
        }
        
}
//*****************************************************************                    
                }
                /*
                //ensure that a buffer is chosen once per parallel processing
                //in Crossbar to avoid the first packet activating more than 1
                //bus
                if(bSelect.size() == 0)
                {
                    //prepare list of INPUT buffers
                    for(int x=0; x<INPUTBUFFERS; x++)
                    //for(int x=0; x<sFabric.GetVerticalBuses(); x++)
                    {
                        
                        if(sFabric.GetInputBufferConnectionStatus(x) == false) 
                        {
                            //add each buffer
                            bSelect.add(x);
                        }
                    }
                }
                */
                boolean busActive = false;
                if((bSelect.isEmpty()) && (inputType == 0))
                {
                    for(int i=0; i<this.OUTPUTBUFFERS;i++)
                    {
                        if(sFabric.GetBusActiveStatus(i) == true)
                        {
                            busActive = true;
                            break;
                        }
                    }
                }
                
                if((bSelect.size() > 0) || busActive)
                {
                    //update the next Capture Available Bus events time
                    current.SetTicks(current.GetTicks()+PULSE);
                    //put the updated Capture Available Bus back in the event queue
                    readyQueue.add(current);

                     

    //*******************************************************************
    //NEED TO USE THE APPROPIATE Random Distribution

                    //FROM = st.nextInt(INPUTBUFFERS);
    /*                
                    //ensure there's packet(s) in the selected input buffer, to be switched
    //while((peekPacket = (RouterPacket)inputBuffer[FROM].peek()) == null)
                    {
    //??need to check for no packet, get new set of packets   
    */ 
    //*****************************************************************************  
                    int idx;
                    
                    if(bSelect.size() > 0) 
                    {
                        
                        //select an inputBuffer OR/ InternalMemory packet
                        idx = st.nextInt(bSelect.size());
                        //get the index value
                        FROM = bSelect.get(idx);
                        
                        //remove the buffer already selected
                        bSelect.remove(idx);
                    }
    //*****************************************************************************
    /*                    
                    }
    //?? implement, each buffer input, first packet     
    */                
                    //if not InternalMemory chosen
                    if(inputType == 0 )
                    {
                        //ensure there's packet(s) in the selected input buffer, 
                        //to be switched and there is packets in the factory
                        while(((peekPacket = (RouterPacket)inputBuffer[FROM].peek()) == null) && 
                               (NumberOfPacketsMoved < totalNumberOfPackets) &&
                               (pFactory.GetPacketsRemaining() > 0))
                        {
        //*************************                    
        //NEED TO ALLOCATE PROPERLY                    
        //puts 1024 packets in the the Input Buffers, every time                
        pFactory.DeliverPacket(inputBuffer, cfg, TIME,this);
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
                                    if(sFabric.GetInputBufferConnectionStatus(x) == false) 
                                    {
                                        //add each buffer
                                        bSelect.add(x);
                                    }
                                }
                        }
        //*****************************************************************************  
                        if(bSelect.size() > 0)
                        {
                            //select a buffer
                            idx = st.nextInt(bSelect.size());
                            //get the index value
                            FROM = bSelect.get(idx);
                            
                            //remove the buffer already selected
                            bSelect.remove(idx);
                        }
        //*****************************************************************************
        //***********************************************************                    
                        }
                    }
                

                    //inputBuffer not empty and bus already active, InputBuffer used
                    if(sFabric.GetBusActiveStatus(BUS)) 
                    {
                        {
                            if(((String)cfg.GetConfig("GENERAL","Verbose")).compareToIgnoreCase("True") == 0)
                            {
                                //inputBuffer
                                if((inputType == 0 ) && (inputBuffer[FROM].size() > 0))
                                {
                                    sFabric.Print(false, TO,inputBuffer[FROM].peek().GetSequenceNumber(),true,TIME);
                                }
                                //Fabric internalMemory
                                else if(inputType == 1) 
                                {
                                    sFabric.Print(false, TO,
                                    ((Memory)sFabric).GetInternalMemoryRouterPacket(FROM).GetSequenceNumber(),
                                    true,TIME);
                                }
                            }
                        }
                    }
                    
                    
                    //ensure there is available bus(es)
                    if(sFabric.GetAvailableBusCount() > 0) 
                    {                
                        if(((inputType == 0 ) && (this.inputBuffer[FROM].size() > 0)) || 
                           ((inputType == 1 ) && (((Memory)sFabric).GetInternalMemorySize() > 0)))
                        {
                            if (inputType == 0 )
                            {
//************************************************************************************************                                
                                //set the TO buffer, based on available buses
                                TO = st.nextInt(OUTPUTBUFFERS);
                                //select an available bus
                                BUS = sFabric.GetBusNumberInAvailableBus(st.nextInt(sFabric.GetAvailableBusCount()));
//*******************************************************************************************************                                
                            }
                            else if (inputType == 1 )
                            {
                                //set the TO buffer, based on available buses
                                TO = st.nextInt(OUTPUTBUFFERS);
                                //select an available bus
                                BUS = sFabric.GetBusNumberInAvailableBus(st.nextInt(sFabric.GetAvailableBusCount()));
                                //TO = sFabric.GetBusNumberInAvailableBus(st.nextInt(OUTPUTBUFFERS));
                                //set the TO buffer, based on output specified in the RouterPacket
                                //TO = ((Memory)sFabric).GetInternalMemoryRouterPacket(FROM).GetOutputBuffer();
                            }
            //*******************************************************************                

                                //attempt to capture a free bus in the fabric
                                /*
                                 * Memory Fabric -  Maximum 1 bus = bus 0
                                 * Bus Fabric - Maximum 1 bus = bus 0
                                 * Crossbar Fabric - Maximum 1 INFINITY(max integer)
                                 */ 
            //System.out.println("Time: "+TIME+"   Packet: "+peekPacket.GetSequenceNumber()+"    Buffer: "+FROM+"    Attempting to capture a Bus -> ");    



                            /*ensure that a packet do not have a bus already active, 
                             * waiting for the outputBuffer to be consumed
                             */
                            if(((inputType == 0) &&
                               (sFabric.GetSearchIfSequenceUsingTheBus(peekPacket.GetSequenceNumber()) == false)) ||
                               ((inputType == 1) && (sFabric.GetSearchIfSequenceUsingTheBus(
                                ((Memory)sFabric).GetInternalMemoryRouterPacket(FROM).GetSequenceNumber()) == false)))     
                            {
//MAY HAVE A PROBLEM HERE
//inputBuffer / internalMemory have the same index                                
                                //try to set a bus active for switching a packet
                                if(((inputType == 0) &&
                                   (sFabric.SetBusActiveStatus(BUS,FROM,peekPacket.GetSequenceNumber(),TIME,cfg) == true)) ||
                                   ((inputType == 1) && (sFabric.SetBusActiveStatus(BUS,FROM,
                                    ((Memory)sFabric).GetInternalMemoryRouterPacket(FROM).GetSequenceNumber(),TIME,cfg) == true))) 
                                {
    if(((String)cfg.GetConfig("GENERAL","Verbose")).compareToIgnoreCase("True") == 0)
    {    
        if(inputType == 0)
            System.out.println("Time: "+ TIME +"    SetACTIVE  FROM input: "+(FROM+1)+" TO output: "+(TO+1)+" Sequence: "+peekPacket.GetSequenceNumber());                        
        else
            System.out.println("Time: "+ TIME +"    SetACTIVE  FROM input: "+(FROM+1)+" TO output: "+
                              (((Memory)sFabric).GetInternalMemoryRouterPacket(FROM).GetOutputBuffer()+1)+
                              " Sequence: "+
                              ((Memory)sFabric).GetInternalMemoryRouterPacket(FROM).GetSequenceNumber());                        
            //System.out.println("Time: "+ TIME +"    SetACTIVE  FROM input: "+(FROM+1)+" TO output: "+(TO+1)+" Sequence: "+((Memory)sFabric).GetInternalMemoryRouterPacket(FROM).GetSequenceNumber());                        
    }
    //System.out.println("    Got Bus: "+(sFabric.GetRecentBus()+1)+"    for Packet#: "+sFabric.sequence);                    
                                    Event evt;
                                   if(inputType == 0)
                                   {
                                        //create FabricSwitching event
                                        evt = new Event(TIME + sFabric.GetSpeed(), "FabricSwitching");
                                        //set the bus release information
                                        evt.SetBusReleaseInfo(TO, FROM, peekPacket.GetSequenceNumber(),BUS);
                                   }
                                   else //if(inputType == 1)
                                   {
                                        //create InternalMemory packet movement event
                                        evt = new Event(TIME + sFabric.GetSpeed(), "InternalMemory");
                                        //set the bus release information
                                        evt.SetBusReleaseInfo(((Memory)sFabric).GetInternalMemoryRouterPacket(FROM).GetOutputBuffer(),
                                            FROM, 
                                            ((Memory)sFabric).GetInternalMemoryRouterPacket(FROM).GetSequenceNumber(),
                                            BUS);
                                        /*
                                        evt.SetBusReleaseInfo(TO, FROM, 
                                            ((Memory)sFabric).GetInternalMemoryRouterPacket(FROM).GetSequenceNumber(),
                                            BUS);
                                        */
                                        
                                   }
                                   /*
                                    * release the Bus used to send packet
                                    */
                                   
                                   //put the event in the ready queue
                                   readyQueue.add(evt);

                                }

                            }
                        }
                    
                    }
                    
                    
                }
                    
                    
                else
                {
                    //update the next Capture Available Bus events time
                    current.SetTicks(current.GetTicks()+PULSE);
                    //put the updated Capture Available Bus back in the event queue
                    readyQueue.add(current);
                }   
            }
            //checkevent for Consume Packet from output bus
            else if (current.GetActionToBeTaken().compareToIgnoreCase("ConsumePacket") == 0)
            {
//**************************************************
//NEED TO CONSUME ACCORDING TO DISTRIBUTION
pConsumer.ConsumePackets(TIME, outputBuffer, readyQueue,PULSE, current);
//**************************************************
/*
                //update the next Consumption Bus event time
                current.SetTicks(current.GetTicks()+PULSE);
                //put the updated Consumption Bus event back in the ready queue
                readyQueue.add(current);
*/               
            }
            else if (current.GetActionToBeTaken().compareToIgnoreCase("DiscardPacket") == 0)
            {
//**************************************************
pConsumer.DiscardPackets(TIME, outputBuffer, current.GetOutputBuffer(), current, readyQueue);
//**************************************************
                
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
                
                //size limit of the Output buffer
                int limit;
                
                try
                {
                    //get buffer size limit
                    limit = (Integer)cfg.GetConfig("CLASS",
                                (String)cfg.GetConfig("OUTPUTBUFFERSCLASS", ("buffer"+(current.GetOutputBuffer()+1)) ));
                }
                catch (Exception e){
                    //apply default limit if Class not specified for Buffer in [OUTPUTBUFFERSCLASS]
                    limit = (Integer)cfg.GetConfig("CLASS",
                                (String)cfg.GetConfig("OUTPUTBUFFERSCLASS", ("Default") ));
                }

                //gather how many times a delivery attempt was made
                outDelivery[current.GetOutputBuffer()] += 1;
                
                //ensure that the Output Buffer limit(s) are obeyed
                if((outputBuffer[current.GetOutputBuffer()].size() + 1) <= limit)
                {

//*******************************************************************            
                    //randomly move packets
                    //busUsed = sFabric.MovePacket(FROM,TO, TIME);
                    busUsed = sFabric.MovePacket(current.GetInputBuffer(),
                                                 current.GetOutputBuffer(), TIME);
//?????
                    //handles fabric with internal memory
                    if(sFabric.GetHasMemory() == true)
                    {
                        //check packet was successfully moved to internalMemory
                        if(sFabric.GetPacketMoved())
                        {
                            
if(((String)cfg.GetConfig("GENERAL","Verbose")).compareToIgnoreCase("True") == 0)
{                    
    System.out.println("Time: "+ TIME + "    <InternalMemory><ARRIVED>    Packet: "+
            sFabric.GetCurrentPacketUsingTheBus(current.GetBus())+
            "    Input["+(current.GetInputBuffer()+1)+"]" +" = "+
            inputBuffer[current.GetInputBuffer()].size()+
            //"    --> Output["+(current.GetOutputBuffer()+1)+"]" +
            //" = "+ outputBuffer[current.GetOutputBuffer()].size()+
            "   Created: "+sFabric.GetRecentPacket().GetTimeCreated()+
            "   Delivered: "+sFabric.GetRecentPacket().GetTimeDeliverd()
+ "   InternalMemory = "+((Memory)sFabric).GetInternalMemorySize()+" Pkt(s)"            
            ); 
            //+"    PKT(S)-Moved:"+NumberOfPacketsMoved); 
}
                            /*
                            * release the Bus used to send packet to internal memory
                            */ 
                           //create release event
                           Event evt = new Event(TIME, "ReleaseBus");
                           //set the bus release information
                           evt.SetBusReleaseInfo(current.GetOutputBuffer()
                                               , current.GetInputBuffer()
                                               , current.GetSequence()
                                               , current.GetBus());
                           //put the event in the ready queue
                           readyQueue.add(evt);
                        }
                    }
                    else if(sFabric.GetHasMemory() == false)
                    {

                        //check packet was successfully moved
                        if(sFabric.GetPacketMoved())
                        {
                            //increment packets moved from INPUT to OUTPUT Buffer
                            NumberOfPacketsMoved += 1;
                            
                            //check if it is the only packet in the output buffer
                            if(outputBuffer[current.GetOutputBuffer()].size() == 1)
                            {
                                //gather how many times the buffer was empty
                                outEmpty[current.GetOutputBuffer()] += 1;
                            }
                            
                            //output average packets  
                            outAvgPkts[current.GetOutputBuffer()] += outputBuffer[current.GetOutputBuffer()].size();
                            outAvgPkts[current.GetOutputBuffer()] /=2;
                        }
/*                    
System.out.println("Time: "+ TIME +"    Input["+(current.GetInputBuffer())+"]" +" = "
        +inputBuffer[current.GetInputBuffer()].size()+"    --> Output["
        +(current.GetOutputBuffer())+"]" + " = "
        + outputBuffer[current.GetOutputBuffer()].size()+"    PKT(S)-Moved:"+NumberOfPacketsMoved);                
        * */
//*******************************************************************
                    /*
System.out.println("    Input["+(current.GetInputBuffer())+"]" +" = "
        +inputBuffer[current.GetInputBuffer()].size()+"    --> Output["
        +(current.GetOutputBuffer())+"]" + " = "
        + outputBuffer[current.GetOutputBuffer()].size());                    
                    */

if(((String)cfg.GetConfig("GENERAL","Verbose")).compareToIgnoreCase("True") == 0)
{                    
    if(sFabric.GetCurrentPacketUsingTheBus(current.GetBus()) != -1)
    {
        System.out.println("Time: "+ TIME + "    <OUTPUT><ARRIVED>    Packet: "+
            sFabric.GetCurrentPacketUsingTheBus(current.GetBus())+
            "    Input["+(current.GetInputBuffer()+1)+"]" +" = "+
            inputBuffer[current.GetInputBuffer()].size()+"    --> Output["+(current.GetOutputBuffer()+1)+"]" 
            + " = "+ outputBuffer[current.GetOutputBuffer()].size()+
            //"   Created: "+sFabric.GetRecentPacket().GetTimeCreated()+
            //"   Delivered: "+sFabric.GetRecentPacket().GetTimeDeliverd()+
            "    PKT(S)-Moved:"+NumberOfPacketsMoved); 
    }
}
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
                }
                else
                {
                    //gather how many times the buffer was full
                    outFull[current.GetOutputBuffer()] += 1;
                    //how many packets in total dropped
                    outputtotalDroppedPkts += 1;
                    //gather how many times packets were dropped
                    outDroppedPkts[current.GetOutputBuffer()] += 1;
                
if(((String)cfg.GetConfig("GENERAL","Verbose")).compareToIgnoreCase("True") == 0)
{                    
System.out.println("\nTime: "+ TIME + "    --> Output["+(current.GetOutputBuffer()+1)+"]" + " = "+ 
        outputBuffer[current.GetOutputBuffer()].size()+"    Packet(s)"+ "    Cannot deliver Packet"+
        //sFabric.GetCurrentPacketUsingTheBus(current.GetOutputBuffer())+ "    --> Output["+(TO+1)+"]" +
        //sFabric.GetCurrentPacketUsingTheBus(current.GetBus())+ 
        "    --> Output["+(current.GetOutputBuffer()+1)+"]" +" -> F U L L\n");                    
}
                    //get the recent bus used by the fabric
                    busUsed = sFabric.GetRecentBus();

                    //update the Switching Fabric Bus events time, this is because the 
                    //outputBuffer is full, hope that some packets have been removed
                    current.SetTicks(current.GetTicks()+PULSE);
                    //put the updated Switching Fabric Bus events back in the event queue
                    readyQueue.add(current);
                    
                    
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
                    
                
                }
                
                
            }
            
            //checkevent for move packet from Fabric InternalMemory to output buffer
            else if (current.GetActionToBeTaken().compareToIgnoreCase("InternalMemory") == 0)
            {
                
                //size limit of the Output buffer
                int limit;
                
                try
                {
                    //get buffer size limit
                    limit = (Integer)cfg.GetConfig("CLASS",
                                (String)cfg.GetConfig("OUTPUTBUFFERSCLASS", ("buffer"+(current.GetOutputBuffer()+1)) ));
                }
                catch (Exception e){
                    //apply default limit if Class not specified for Buffer in [OUTPUTBUFFERSCLASS]
                    limit = (Integer)cfg.GetConfig("CLASS",
                                (String)cfg.GetConfig("OUTPUTBUFFERSCLASS", ("Default") ));
                }

                //ensure that the Output Buffer limit(s) are obeyed
                if((outputBuffer[current.GetOutputBuffer()].size() + 1) <= limit)
                {

//*******************************************************************     
                    //find the packet in the internalMemory, with the matching sequence number
                    int idxOfPacket = ((Memory)sFabric).FindRouterPacket(current.GetSequence());
                    
                    if(idxOfPacket != -1)
                    {
                        //move internalMemory packets to outputBuffer
                        busUsed = ((Memory)sFabric).MoveInternalMemoryPacket(idxOfPacket, TIME);
                    }
                    
                    //check packet was successfully moved
                    if(sFabric.GetPacketMoved())
                    {
                        //increment packets moved from INPUT to OUTPUT Buffer
                        NumberOfPacketsMoved += 1;
                    
                        //check if it is the only packet in the output buffer
                        if(outputBuffer[current.GetOutputBuffer()].size() == 1)
                        {
                            //gather how many times the buffer was empty
                            outEmpty[current.GetOutputBuffer()] += 1;
                        }
                        
                        //output average packets  
                        outAvgPkts[current.GetOutputBuffer()] += outputBuffer[current.GetOutputBuffer()].size();
                        outAvgPkts[current.GetOutputBuffer()] /=2;

                        if(((String)cfg.GetConfig("GENERAL","Verbose")).compareToIgnoreCase("True") == 0)
                        {                    
                            System.out.println("Time: "+ TIME + "    <OUTPUT><ARRIVED><***   INTERNAL MEMORY   ***>    Packet: "+
                                    sFabric.GetCurrentPacketUsingTheBus(current.GetBus())+
                                    "    --> Output["+(current.GetOutputBuffer()+1)+"]" 
                                    + " = "+ outputBuffer[current.GetOutputBuffer()].size()+
                                    "   Created: "+sFabric.GetRecentPacket().GetTimeCreated()+
                                    "   Delivered: "+sFabric.GetRecentPacket().GetTimeDeliverd()+
                                    "    PKT(S)-Moved:"+NumberOfPacketsMoved); 
                        }
                    }
                        
                    
                }
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

            }
            //checkevent for Release Selected Bus
            else if (current.GetActionToBeTaken().compareToIgnoreCase("ReleaseBus") == 0)
            {
                //Release Active Bus
                //sFabric.SetBusInActiveStatus(current.GetOutputBuffer(), 
                sFabric.SetBusInActiveStatus(current.GetBus(), 
                                             current.GetInputBuffer(),
                                             current.GetSequence(),
                                             TIME, cfg);
/*
System.out.println("Time: "+ TIME +" SetINACTIVE FROM: "+current.GetInputBuffer()+
        " TO: "+current.GetOutputBuffer()+
        " Sequence: "+current.GetSequence()+"\n");                
*/ 
            }
        }
        
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
        String empty;
        String full;
        
        System.out.println("\n\n\n                          S U M M A R Y\n");
        
        System.out.println("Rate of Switching Fabric                     = "+((double)TIME)/(double)totalNumberOfPackets);
        System.out.println("Total <Input> Dropped Packet(s)              = "+inputtotalDroppedPkts);
        System.out.println("Total <Output> Dropped Packet(s)             = "+outputtotalDroppedPkts);
        
        System.out.println("\n\n*** <INPUT> Buffers ***");
        for(int x=0; x<INPUTBUFFERS;x++)
        {
            if(inDelivery[x] == 0)
            {
                empty = "0";
                full = "0";
            }
            else
            {
                empty = df.format(((inEmpty[x]/inDelivery[x])*100));
                full = df.format(((inFull[x]/inDelivery[x])*100));
            } 
            System.out.println();
            System.out.println("Input  Buffer["+(x+1)+"] Percentage Empty            = "+inEmpty[x]+"/"+inDelivery[x]+" = "+empty+"%");
            System.out.println("Input  Buffer["+(x+1)+"] Percentage Full             = "+inFull[x]+"/"+inDelivery[x]+" = "+full+"%");
            System.out.println("Input  Buffer["+(x+1)+"] Average Number of pkt(s)    = "+inAvgPkts[x]);
            System.out.println("Input  Buffer["+(x+1)+"] Dropped pkt(s)              = "+inDroppedPkts[x]);
        }
        
        System.out.println("\n\n*** <OUTPUT> Buffers ***");
        
        for(int x=0; x<OUTPUTBUFFERS;x++)
        {
            if(outDelivery[x] == 0)
            {
                empty = "0";
                full = "0";
            }
            else
            {
                empty = df.format(((outEmpty[x]/outDelivery[x])*100));
                full = df.format(((outFull[x]/outDelivery[x])*100));
            }        
            
            
            System.out.println();
            System.out.println("Output  Buffer["+(x+1)+"] Percentage Empty           = "+outEmpty[x]+"/"+outDelivery[x]+" = "+empty+"%");
            System.out.println("Output  Buffer["+(x+1)+"] Percentage Full            = "+outFull[x]+"/"+outDelivery[x]+" = "+full+"%");
            System.out.println("Output  Buffer["+(x+1)+"] Average Number of pkt(s)   = "+outAvgPkts[x]);
            System.out.println("Output  Buffer["+(x+1)+"] Dropped pkt(s)             = "+outDroppedPkts[x]);
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
