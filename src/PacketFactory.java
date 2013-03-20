
import java.net.InetAddress;

/*
 * This class implements the creation of packets
 */
public class PacketFactory {
    
    //holds the packet sequence number
    int sequence;
    //holds the current simulation time 
    int TIME;
    
    public PacketFactory()
    {
        //initialize the packet sequence
        sequence = 1;
        //initialize time
        TIME = 0;
    }
    
    //update the time of the factory
    public void SetTime(int time)
    {
        TIME = time;
    }
    
    //make data
    private byte[] CreateData(int packetSize)
    {
        //create byte buffer for data
        byte[] buf = new byte[packetSize];
        
        //holds the character bytes generated
        StringBuilder s = new StringBuilder();
        
        char c;
        
        //generate data for the packet
        for(int x=0; x< (packetSize/26);x++)
        {
            for(int y=0; y<26; y++)
            {
                s.append( Character.toString((char)(y+65) ));
            }
        }
        for(int x=0; x< (packetSize%26);x++)
        {
            s.append(Character.toString((char)(x+65) ));
        }
            
        //return byte of characters
        return buf = s.toString().getBytes();
    }
    
    //create packet
    public RouterPacket CreatePacket(int packetSize)
    {
        //holds the packet created
        RouterPacket pck = null;
        
        //create byte buffer with data
        byte[] buf = CreateData(packetSize);
        
        try
        {
            //make the packet
            pck = new RouterPacket(buf,buf.length,InetAddress.getByName("localhost"),9999,
                                                TIME, sequence);
            //increment packet sequence
            sequence += 1;
        }
        catch(Exception e)
        {
            System.out.println("Error creating packet");
        }
        
        //return packet generated
        return pck;
    }
    
    public static void main(String []args)
    {
        PacketFactory t = new PacketFactory();
        t.CreatePacket(7);
    }
}
