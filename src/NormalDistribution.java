 /*
 * implements the Normal Distribution using Java's Gaussian provision
 * Implementation culled from the org.apache.commons libraries
 */
import java.util.*;

public class NormalDistribution extends Distribution
{
    
    //number of packets
    private int NumberOfPackets;
//******************************************    
    //distribution mean
    private double mean;
    
    private Queue q;
//******************************************
    
     //create normal distribution array
     int[] NDarray; // = new int[1000];
  
   //Constructor for normal distribution
    public NormalDistribution(double mean,double standardDeviation)
    {
        super(mean, standardDeviation);
        //initialize the queue for output
        q=new LinkedList();
        //getDistribution(0.5);
        //getDistribution();
    }
    
 
    //return the Queue holding the distribution
    public Queue GetDistributionQueue()
    {
        return q;
    }
        
    //NormalDistribution takes parameters: mean, standard deviation and uses the Random's gaussian method
    @Override
    //public int getDistribution(double standardDeviation)
    public int getDistribution()
    {    
        for (int i=0;i<10;i++)
        {
            int val = (int) (mean + standardDeviation * rand.nextGaussian());
            
            
            q.add(val);
            
            System.out.println(val);
        }
        return -1;
    }
    
    //************************
        //set the number of packets
        public void SetNumebrOfPackets(int packets)
        {
            NumberOfPackets = packets;
            NDarray = new int[NumberOfPackets];
        }
        
        public int getNumber()
        {
            int value=0;
            if (q.size() > 0)
            {
                value = (Integer)q.remove();
            }
            
            return value;
            
        }
        
        public void remove()
        {
            q.clear();
        }
        
        //set the mean of the distribution
        public void SetMean(double mean)
        {
            this.mean = mean;
        }
        //************************
   
        public double mean() 
        {
            //**************************
            return this.mean;
            //**************************
        }
        
    public static void main(String[] args)
    {
       
        NormalDistribution arrivalAmounts= new NormalDistribution(15,0.5);
        
        //arrivalAmounts.getNormalDistribution();
        
        
        
    }

}
