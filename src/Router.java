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
    private final int INPUTBUFFERS;
    //holds the number of output buffers
    private final int OUTPUTBUFFERS;
    //holds the switching fabric
    private SwitchingFabric sFabric;
    
    
    //constructor
    public Router(int INPUTBUFFERS, int OUTPUTBUFFERS)
    {
        
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

    /*
     * loads the configuration file and configures the 
     * simulation
     */
    private void CofigureSimulator()
    {
//**********************    T E S T I N G   ****************        
        int speed = 25;
        
        Queue<DatagramPacket> []input= new LinkedList[4];
        Queue<DatagramPacket> []output= new LinkedList[4];
        
        for (int x=0;x<input.length;x++)
            input[x] = new LinkedList();
        for (int x=0;x<output.length;x++)
            output[x] = new LinkedList();
        
        byte[] buf = new byte[256];
        String s= "Testing 1,2,3....";
        buf = s.getBytes();
        
        try
        {
            DatagramPacket pck = new DatagramPacket(buf,buf.length,InetAddress.getByName("localhost"),9999);


            input[0].add(pck);
            input[0].add(pck);
            input[0].add(pck);
            input[0].add(pck);

            input[1].add(pck);
            input[1].add(pck);
            input[1].add(pck);
            input[1].add(pck);

            input[2].add(pck);
            input[2].add(pck);
            input[2].add(pck);
            input[2].add(pck);

            input[3].add(pck);
            input[3].add(pck);
            input[3].add(pck);
            input[3].add(pck);

        }
        catch(Exception e)
        {
            System.out.println("Error");
        }
//**********************************************************        
        
        //set the fabric type
        this.sFabric = new Bus(25,input,output);
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
