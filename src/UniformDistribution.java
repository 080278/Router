 /*
 * implements the Uniform Distribution
 */
import java.util.*;

public class UniformDistribution extends Distribution
{
    //number of packets
    private int NumberOfPackets;
//******************************************    

    private Queue q;
//******************************************
    
    //create random object
    Random rand = new Random();
    
    //create normal distribution array
   int[] UDarray; // = new int[1000];
    
   
   //Uniform Distribution class constructor
    public  UniformDistribution(double mean,double standardDeviation)
    {
        super(mean, standardDeviation);
        //initialize the queue for output
        q=new LinkedList();
        //super.mean = mean;
        //super.standardDeviation = standardDeviation;
        //getDistribution(1.5);
        //getDistribution();
    }
    
    //return the Queue holding the distribution
        public Queue GetDistributionQueue()
        {
            return q;
        }
        
    
        @Override
        //public int getDistribution(double standardDeviation) 
        public int getDistribution() 
        {
            double value  = -1;
            
            value = ((Math.log(1 / (1 - rand.nextDouble())) + mean) * Math.sqrt(standardDeviation));
            
            q.add(value);
                
            System.out.println(value);
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
            super.mean = mean;
        }
        //************************
   
        public void print()
        {
            while (q.size() != 0)
            {
                System.out.println(getNumber());
            }
        }
        
        public double mean() 
        {
            //**************************
            return this.mean;
            //**************************
        }
       
    public static void main(String[] args)
    {
        //int NumberOfTimesPacketsAreDeliverd = 0;
        int NumberOfPackets = 100;
        //*************************
        double mean = 4.5;//2.9;
        double StandardDeviation = 1.5;
        //*************************
        
           UniformDistribution distribution= new UniformDistribution(mean,StandardDeviation);
   
           //**********************************
            distribution.SetMean(mean);
            //**********************************
            distribution.SetNumebrOfPackets(NumberOfPackets);
            distribution.getDistribution();
    }
}
