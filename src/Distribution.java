 /*
 * Base class for the different types of distributions
 */
import java.util.*;

public class Distribution 
{
    //holds the list of input buffers
    protected static Queue []inputBuffers;
    //size of input buffer queue
    protected static int inputbuffersize;
    
    public Distribution(int inputbuffersize)
    {
        this.inputbuffersize=inputbuffersize;
    }
    
    
}
