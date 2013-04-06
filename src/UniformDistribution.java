 /*
 * implements the Uniform Distribution
 */
import java.util.*;

public class UniformDistribution extends Distribution
{
    //number of packets
    private int NumberOfPackets;
//******************************************    
    //distribution mean
    private double mean;
    
    private Queue q;
//******************************************
    
    //create random object
    Random rand = new Random();
    
    //create normal distribution array
   int[] UDarray; // = new int[1000];
    
   private int standardDeviation;
   //private int mean;
   
   
   //Uniform Distribution class constructor
    public  UniformDistribution(double mean,double standardDeviation)
    {
        super(mean, standardDeviation);
        getDistribution(1.5);
    }
    
        @Override
        public int getDistribution(double standardDeviation) 
        {
            for (int i=0;i<10;i++)
            {
                double value = (Math.log(1 / (1 - rand.nextDouble())) + mean) * Math.sqrt(standardDeviation);
                System.out.println((int)value);
            }
            return -1;
        } 
        
        //************************
        //set the number of packets
        public void SetNumebrOfPackets(int packets)
        {
            NumberOfPackets = packets;
            UDarray = new int[NumberOfPackets];
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
   
       
    public static void main(String[] args)
    {
           UniformDistribution distribution= new UniformDistribution(4.5,1.5);
   
    }
}
