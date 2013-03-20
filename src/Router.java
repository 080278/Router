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
    //holds the currently selected input buffer
    int FROM;
    //holds the currently selected output buffer
    int TO;
    
    /*
     * Configuration variables
     */
    //size of packet
    int PACKETSIZE;
    //speed of fabric
    int FABRICSPEED;
    //************************
    
    
    //constructor
    public Router(int INPUTBUFFERS, int OUTPUTBUFFERS, int pulse, int packetSize, int fabricSpeed)
    {
        //set the clock time
        TIME = 0;
        //set the pulse of the router
        PULSE = pulse;
        //set the packet size
        PACKETSIZE = packetSize;
        //set the speed of the fabric
        FABRICSPEED = fabricSpeed;
        //initialize the priority ready queue
        readyQueue = new PriorityQueue<Event>();
        //save the number of input buffers
        this.INPUTBUFFERS = INPUTBUFFERS;
        //save the number of output buffers
        this.OUTPUTBUFFERS = OUTPUTBUFFERS;
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

    //adds a packet to the input buffer indicated
    private void AddInputPacket(RouterPacket dPacket, int bufferNumber)
    {
        //put a packet in the chosen buffer
        inputBuffer[bufferNumber].add(dPacket);
    }
    
    /*
     * loads the configuration file and configures the 
     * simulation
     */
    private void CofigureSimulator()
    {
//**********************    T E S T I N G   ****************  
        
        PacketFactory pf = new PacketFactory();
        try
        {

            for(int y=0; y<INPUTBUFFERS; y++)
                for(int x=0; x<4; x++)
                {
                    
                    AddInputPacket(pf.CreatePacket(PACKETSIZE),y);
                    
                }
        }
        catch(Exception e)
        {
            System.out.println("Error");
        }
//**********************************************************        
        
        //create the fabric type, and pass input & output buffer 
        this.sFabric = new Bus(FABRICSPEED,inputBuffer,outputBuffer);
    }
    
    public void RunSimulator()
    {
//**************************************************************         
        //number of attempts to move packets
        //int iterations =10;
        
        //bus used to move the packet
        int busUsed;
        
       
        //generate random number
        Random st = new Random();
//**************************************************************   
        //holds the current event
        Event current;
        
        //add Capture Available Bus events to the simulator
        readyQueue.add(new Event(TIME + PULSE, "CaptureBus"));
        
        
        
        //begin the simulation
        while(readyQueue.size() != 0)
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
                
                //ensure there's packet(s) in the selected input buffer, to be switched
                while((peekPacket = (RouterPacket)inputBuffer[FROM].peek()) == null)
                {
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
                if(sFabric.SetBusActiveStatus(0,FROM,peekPacket.GetSequenceNumber()) == true)
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

System.out.println("Input["+FROM+"]" +" = "+inputBuffer[FROM].size()+"    --> Output["+TO+"]" + " = "+ outputBuffer[TO].size());
//*******************************************************************            
                //randomly move packets
                busUsed = sFabric.MovePacket(FROM,TO, TIME);
                
//*******************************************************************
System.out.println("Time: "+ TIME + "   Input["+FROM+"]" +" = "+
        inputBuffer[FROM].size()+"    --> Output["+TO+"]" + " = "+ 
        outputBuffer[TO].size()+"    <- Packet : "+
        sFabric.GetCurrentPacketUsingTheBus()+
        "   Created: "+sFabric.GetRecentPacket().GetTimeCreated()+
        "   Delivered: "+sFabric.GetRecentPacket().GetTimeDeliverd()+" ->\n");
//******************************************************************* 

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
//*********************************************************
        
        //Begin the simulation
        Router sim = new Router(inputBuffers,outputBuffers, pulse, packetSize,fabricSpeed);
        
    }
}
