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
    
    //get the time created
    public int GetTimeCreated()
    {
        return timeCreated;
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