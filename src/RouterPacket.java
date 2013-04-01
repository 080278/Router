/*
 * implements a router packet
 */

import java.net.*;


public class RouterPacket{
       
    //holds the packet sequence number
    int sequence;
    //holds DatagramPacket
    private DatagramPacket dPacket;
    //holds the time created and submitted to INPUT
    private int timeCreated;
    //holds the time delivered to OUTPUT
    private int timeDelivered;
    //holds the destination buffer
    private int outputBuffer;
    //holds the source buffer
    private int inputBuffer;
    //holds the bus to be used
    private int bus;
    
    //constructor
    public RouterPacket(byte[] buf, int length, InetAddress address, int port, int time, int sequence)
    {
        //set the sequence number of the packet
        this.sequence = sequence;
        //set the time created
        timeCreated = time;
        //create the packet
        dPacket = new DatagramPacket(buf,length,address,port);
    }
    
    //set the bus to be used
    public void SetBus(int bus)
    {
        this.bus = bus;
    }
    
    //get the bus to be used
    public int GetBus()
    {
        return bus;
    }
    
    //set the input buffer number
    public void SetInputBuffer(int inputBuffer)
    {
        this.inputBuffer = inputBuffer;
    }
    
    //get the input buffer number
    public int GetInputBuffer()
    {
        return inputBuffer;
    }
    
    //set the output buffer number
    public void SetOutputBuffer(int outputBuffer)
    {
        this.outputBuffer = outputBuffer;
    }
    
    //get the output buffer number
    public int GetOutputBuffer()
    {
        return outputBuffer;
    }
    
    //get the time created
    public int GetTimeCreated()
    {
        return timeCreated;
    }
    
    //set the time created
    public void SetTimeCreated(int timeCreated)
    {
        this.timeCreated = timeCreated;
    }
    
    //set the time delivered
    public void SetTimeDelivered(int timeDelivered)
    {
        this.timeDelivered = timeDelivered;
    }
    
    //get the time delivered
    public int GetTimeDeliverd()
    {
        return timeDelivered;
    }
    
    //get the packet
    public DatagramPacket GetPacket()
    {
        return dPacket;
    }
    
    //get the packet sequence number
    public int GetSequenceNumber()
    {
        return sequence;
    }
    
}