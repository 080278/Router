
public class Event implements Comparable 
{
    //holds the event tick
    private int ticks;
    //holds the action to be taken
    private String action;

    //holds the bus to be used
    private int bus;
    //holds the Sequence of the packet holding the bus
    private int sequence;
    //holds the Input buffer holding the bus
    private int inputBuffer;
    //holds the Output buffer holding the bus
    private int outputBuffer;
    
    
    //constructor
    public Event(int ticks, String action)
    {
        //set the time to run
        this.ticks = ticks;
        //set the action to be taken
        this.action = action;
    }
    
    //set the release bus information
    public void SetBusReleaseInfo(int outputBuffer, int inputBuffer, int sequence, int bus)
    {
        this.outputBuffer = outputBuffer;
        this.inputBuffer = inputBuffer;
        this.sequence = sequence;
        this.bus = bus;
    }
    
    //get the output buffer number
    public int GetOutputBuffer()
    {
        return outputBuffer;
    }
    
    //get the input buffer number
    public int GetInputBuffer()
    {
        return inputBuffer;
    }
    
    //get the sequence number of the packet
    public int GetSequence()
    {
        return sequence;
    }
    
    //get the bus to be used
    public int GetBus()
    {
        return bus;
    }
    
    
    //set the object time
    public void SetTicks(int ticks)
    {
        this.ticks = ticks;
    }
    
    //get the object time
    public int GetTicks()
    {
        return ticks;
    }
    
    //get the action to be taken
    public String GetActionToBeTaken()
    {
        return action;
    }
    
    //implements the comparable interface
    public int compareTo(Object tmp )
    {
        int result;
        
        result = (this.ticks < ((Event)tmp).GetTicks() ? -1 : (this.ticks == ((Event)tmp).GetTicks() ? 0 : 1));
        
    return result;
    }
}
