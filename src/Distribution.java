 /*
 * Base class for the different types of distributions
 */
import java.util.*;

public abstract class Distribution 
{
    long SEED;
    //create random object
    protected Random rand;
    //holds the list of input buffers
    protected static Queue []inputBuffers;
    //size of input buffer queue
    protected static int inputbuffersize;
    //standard deviation of distribution
    protected double standardDeviation;
    //mean of distribution
    protected double mean;
    
    //constructor: Handles exponential distribution
    public Distribution(int inputbuffersize, long SEED)
    {
        this.SEED = SEED;
        this.inputbuffersize=inputbuffersize;
        rand = new Random(SEED);
    }
    
    //constructor: Handles Normal and uniform distribution using mean and standard deviation
    public Distribution(double mean, double standardDeviation,long SEED)
    {
        this.mean=mean;
        this.standardDeviation=standardDeviation;
        rand = new Random(SEED);
    }
    
     //set the standard deviation
       public void setStandardDeviation(int standardDeviation)
       {
           this.standardDeviation = standardDeviation;
       }
       
       //get the standard deviation
        public double getStandardDeviation()
       {
              return standardDeviation;
       }
    /*
        //set the mean
       public void setMean(int mean)
       {
           this.mean = mean;
       }
       
       //get the mean 
       public double getMean() 
       {
          return mean;
       }
       */
    //public abstract int getDistribution(double mean,double standardDeviation);
    public abstract int getDistribution();
    public abstract void SetMean(double mean);
    public abstract void SetNumebrOfPackets(int packets);
    public abstract void remove();
    public abstract int getNumber();
    public abstract Queue GetDistributionQueue();
}
