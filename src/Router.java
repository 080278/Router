/*
 * This class is the main simulator class
 */

import java.util.*;
import java.net.*;

public class Router {

    //holds the clock time
    int TIME;
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
    
    
    //constructor
    public Router(int INPUTBUFFERS, int OUTPUTBUFFERS)
    {
        //set the clock time
        TIME = 0;
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
        int fabricSpeed = 5;
        
        byte[] buf = new byte[256];
        String s= "Testing 1,2,3....";
        buf = s.getBytes();
        
        try
        {

            for(int y=0; y<INPUTBUFFERS; y++)
                for(int x=0; x<4; x++)
                {
                    RouterPacket pck = new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999,
                                                        TIME);
                    AddInputPacket(pck,y);
                }
        }
        catch(Exception e)
        {
            System.out.println("Error");
        }
//**********************************************************        
        
        //create the fabric type, and pass input & output buffer 
        this.sFabric = new Bus(fabricSpeed,inputBuffer,outputBuffer);
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
        
        //add fabric switching events to the simulator
        readyQueue.add(new Event(TIME + sFabric.GetSpeed(), "FabricSwitching"));
        
        //begin the simulation
        while(readyQueue.size() != 0)
        {
            //get the next event from the queue
            current = readyQueue.poll();
            //update the simulator time
            TIME = current.GetTicks();
            
            //checkevent for fabric switching
            if (current.GetActionToBeTaken().compareToIgnoreCase("FabricSwitching") == 0)
            {
                //update the next fabric switch time
                current.SetTicks(current.GetTicks()+sFabric.GetSpeed());
                //put the updated fabric switch time back in the event queue
                readyQueue.add(current);
                
//*******************************************************************
//NEED TO USE THE APPROPIATE Random Distribution
                int from = st.nextInt(INPUTBUFFERS);
                int to = st.nextInt(OUTPUTBUFFERS);
            
//*******************************************************************
System.out.println("Input["+from+"]" +" = "+inputBuffer[from].size()+"    --> Output["+to+"]" + " = "+ outputBuffer[to].size());
//*******************************************************************            
                //randomly move packets
                busUsed = sFabric.MovePacket(from,to, TIME);
                //release the Bus used to send packet
                sFabric.SetBusInActiveStatus(busUsed, from);
//*******************************************************************
System.out.println("Input["+from+"]" +" = "+inputBuffer[from].size()+"    --> Output["+to+"]" + " = "+ outputBuffer[to].size()+"\n");
//******************************************************************* 
            }
            
            
        }
        
        
    }
    
    public static void main(String[] args) {
//**************     T E S T I N G  ***********************        
        int inputBuffers = 4;
        int outputBuffers =4;
//*********************************************************
        
        
        //Begin the simulation
        Router sim = new Router(inputBuffers,outputBuffers);
        
    }
}
