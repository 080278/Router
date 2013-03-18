
/*
 * This implements the Bus switching fabric
 */

import java.util.*;
import java.net.*;

public class Bus extends SwitchingFabric{
    
    //define the number of busses
    public static final int VERTICALBUSES = 1;
    
    //constructor
    public Bus(int speed, Queue []inputBuffers, Queue []outputBuffers)
    {
        //passes the constructor values to the base class SwitchingFabric
        super(speed, inputBuffers, outputBuffers, VERTICALBUSES);
    }
    
    //moves a packet from input buffer to output buffer
    public void MovePacket(int inputBufferNumber, int outputBufferNumber)
    {
        //ONLY ONE BUS PRESENT IN THIS FABRIC
        //if set active status was successful and the flag is true for having
        //control of the bus
        if((SetBusActiveStatus(0, inputBufferNumber) == true) &&
           (GetBusActiveStatus(0)== true))
        {
            
            //move the data to the output buffer
            this.outputBuffers[outputBufferNumber].add(inputBuffers[inputBufferNumber].remove());
        }
    }
    
        
    public static void main(String []args)
    {
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
        Bus tst = new Bus(75,input,output);
        tst.MovePacket(0, 2);
        tst.SetBusInActiveStatus(0,0);
        tst.MovePacket(1, 2);
        tst.SetBusInActiveStatus(0,1);
        tst.MovePacket(0, 2);
    }
    
}
