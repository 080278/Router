 /*
 * implements the Uniform Distribution
 */
import java.util.*;

public class UniformDistribution extends Distribution
{
    //number of packets
    private int NumberOfPackets;
    long SEED;
    
//******************************************    
    //create random object
   Random rand = new Random(SEED);
    
    private Queue q;
//******************************************
    
       
    //create normal distribution array
    //int[] 
           
    ArrayList UDarray; // = new int[1000];
    
   
   //Uniform Distribution class constructor
    public  UniformDistribution(double mean,double standardDeviation, long SEED)
    {
        //super(mean, standardDeviation,inputbuffersize);
        super(mean, standardDeviation, SEED);
        //initialize the queue for output
        q=new LinkedList();
        this.SEED = SEED;
        //super.mean = mean;
        //super.standardDeviation = standardDeviation;
        //getDistribution(1.5);
        //getDistribution();
    }
    
    
        
    
        @Override
        //public int getDistribution(double standardDeviation) 
        public int getDistribution() 
        {
            Random rnd = new Random(SEED);
            inputbuffersize = rnd.nextInt(NumberOfPackets+1);
            
            //keeps track of packets created
            int PacketCounter = 0;
            
            int[] a =new int[inputbuffersize];
            
            int val;
            
            while (PacketCounter < NumberOfPackets)
            { 
                val = (int)((Math.log(1 / (1 - rand.nextDouble())) + mean) * Math.sqrt(standardDeviation));
                    //add each value to the distribution array
                    q.add(val);
                    //a[(val%inputbuffersize)] +=1;
                PacketCounter +=1;
                //System.out.print((int)val+ " ");
            }
 
            //for(Object x:q)  
            //{
            //    System.out.print((int)x+" ");
            //}
            //System.out.println("\n-----------");
  

            
            
            
            //***************
            //int total = 0;
            //***************
            
            //for(int x:a)  
            //for(Object x:EDarray)  
            //{
                //System.out.print(x+" ");
            //    q.add(x);
                //***************
            //    total += x;
                //***************
            //}
            
                //System.out.println();
            //System.out.println((int)val);
            return -1;
        } 
        
        //************************
        //set the number of packets
        public void SetNumebrOfPackets(int packets)
        {
            NumberOfPackets = packets;
            //UDarray = new int[NumberOfPackets];
        }
        
        //return the Queue holding the distribution
        public Queue GetDistributionQueue()
        {
            return q;
        }
        
        public int getNumber()
        {
            int value=-1;
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
        int NumberOfPackets = 50;
        //*************************
        double mean = 4.5;//2.9;
        double StandardDeviation = 1.5;
        long SEED = 287848937;
        //*************************
        
           UniformDistribution distribution= new UniformDistribution(mean,StandardDeviation, SEED);
   
           //**********************************
            distribution.SetMean(mean);
            //**********************************
            distribution.SetNumebrOfPackets(NumberOfPackets);
            distribution.getDistribution();
        
    }
}
