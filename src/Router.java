/*
 * This class is the main simulator class
 */

import java.util.*;
import java.net.*;

public class Router {

    //holds the input buffer(s) queue
    private Queue<DatagramPacket> []inputBuffer;
    //holds the output buffer(s) queue
    private Queue<DatagramPacket> []outputBuffer;
    //holds the number of input buffers
    
    //holds the number of output buffers
    
    
    //constructor
    public Router(int INPUTBUFFERS, int OUTPUTBUFFERS)
    {
        //initialize the input buffers
        inputBuffer = new LinkedList[INPUTBUFFERS];
        //initialize the output buffers
        outputBuffer = new LinkedList[OUTPUTBUFFERS];
        
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
        
    }
    
    public void RunSimulator()
    {
        
    }
    
    public static void main(String[] args) {
//*********************************************************        
        int inputBuffers = 4;
        int outputBuffers =4;
//*********************************************************
        
        
        //initialize the simulator
        Router sim = new Router(inputBuffers,outputBuffers);
        //run the simulator
        sim.RunSimulator();
    }
}
