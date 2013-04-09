
import java.util.Random;

 /*
 * implements the Exponential distribution
 */
import java.util.*;

public class ExponentialDistribution extends Distribution
{
    
    //number of packets
    private int NumberOfPackets;
   //create random object
   Random rand = new Random();
    
   //create distribution array
   //int[] EDarray;
   ArrayList EDarray;
   //create the queue for output
   private Queue q;
           
   
    public  ExponentialDistribution(int NumberOfTimesPacketsAreDeliverd, long SEED)
    {
        
        super(NumberOfTimesPacketsAreDeliverd, SEED);
        
        //initialize the distribution array 
        EDarray = new ArrayList();
        //initialize the queue for output
        q=new LinkedList();
    }
    
    
        @Override
        //public int getDistribution(double standardDeviation) 
        public int getDistribution() 
        {
            //*********************
            //mean = this.mean();
            //*********************
            
            //keeps track of packets created
            int PacketCounter = 0;
            
            int[] a =new int[inputbuffersize];
            
             int val;
            //for (int i=0;i<EDarray.length;i++)
            while (PacketCounter < NumberOfPackets)
            {   
                val = (int) Math.round((-mean) * Math.log(rand.nextDouble()));
                if (val >=0  && val<=inputbuffersize-1)
                {
                     //EDarray[i]=val;
                    //add each value to the distribution array
                    EDarray.add(val);
                     a[val] +=1;
                     //count packet 
                     PacketCounter += 1;
                }
                
              //  System.out.print(EDarray[i]+ " ");  
            }
            
            
            //for(Object x:EDarray)  
            //{
            //    System.out.print((int)x+" ");
            //}
            //System.out.println("\n-----------");
  

            
            
            
            //***************
            int total = 0;
            //***************
            
            for(int x:a)  
            //for(Object x:EDarray)  
            {
                //System.out.print(x+" ");
                q.add(x);
                //***************
                total += x;
                //***************
            }
            
            //***************
            //System.out.println("\n<Total>: "+total);
            //***************
            //System.out.println("<E N D>\n");
            /*
            List lst = Arrays.asList(a);
            q.addAll(lst);
            */
            
            //clear array distribution
            EDarray.clear();
            return -1;
        } 
     
        //set the number of packets
        public void SetNumebrOfPackets(int packets)
        {
            NumberOfPackets = packets;
        }
        
        //return the Queue holding the distribution
        public Queue GetDistributionQueue()
        {
            return q;
        }
        
        public int getNumber()
        {
            int value = -1;
            if (q.size() > 0)
            {
                value = (Integer)q.remove();
            }
            
            return value;
            
        }
        
        //************************
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
/*            
        double sum = 0;
        for (int i = 0; i < inputbuffersize ; i++) 
        {
            sum=sum+i;
        }
    return (sum / inputbuffersize);
*/ 
   }
   
    public static void main(String[] args)
    {
        
        long SEED = 287848937;
        int NumberOfPackets = 10 ;
        //*************************
        double mean = 10;//2.9;
        //*************************
        
        System.out.println("Packets arrival distributions: ");
            //for packet times delivery, seed to keep the same number
            Random rnd = new Random();
            //defaults to 1 unless packet count > 1
            int NumberOfTimesPacketsAreDeliverd = 1;
            //or else the random will be 0
            if(NumberOfPackets > 1)
            {
                do{
                     
                    NumberOfTimesPacketsAreDeliverd = rnd.nextInt(NumberOfPackets+1);
                }
                while(NumberOfTimesPacketsAreDeliverd == 0);
            }
            System.out.println("NumberOfTimesPacketsAreDeliverd: "+NumberOfTimesPacketsAreDeliverd);
            
            //based on the number of packets, a random number is chosen for the 
            //number of times packet delivery happens
            ExponentialDistribution arrivalAmounts= new ExponentialDistribution(NumberOfTimesPacketsAreDeliverd,SEED);
            
            //**********************************
            arrivalAmounts.SetMean(mean);
            //**********************************
            arrivalAmounts.SetNumebrOfPackets(NumberOfPackets);
            arrivalAmounts.getDistribution();
            
/*            
        System.out.println("Arrival Patterns");
            ExponentialDistribution arrivalPattern= new ExponentialDistribution(NumberOfTimesPacketsAreDeliverd);  
            int packetAmt;
            while((packetAmt = arrivalAmounts.getNumber()) != -1)
            {
                arrivalPattern.SetNumebrOfPackets(packetAmt);
                arrivalPattern.SetMean(mean);
                arrivalPattern.getDistribution();
                arrivalPattern.remove();
            }
*/
    }
}
