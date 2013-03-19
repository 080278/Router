
public class Event implements Comparable 
{
    //holds the event tick
    private int ticks;
    //holds the action to be taken
    private String action;
    
    //constructor
    public Event(int ticks, String action)
    {
        //set the time to run
        this.ticks = ticks;
        //set the action to be taken
        this.action = action;
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
