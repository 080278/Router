
import java.util.Random;

 /*
 * implements the Exponential distribution
 */
import java.util.*;

public class ExponentialDistribution extends Distribution
{
    
    //number of packets
    private int NumberOfPackets;
//******************************************    
    //distribution mean
    private double mean;
//******************************************
    
   //create random object
   Random rand = new Random();
    
   //create distribution array
   //int[] EDarray;
   ArrayList EDarray;
   //create the queue for output
   private Queue q;
           
   
    public  ExponentialDistribution(int inputbuffersize)
    {
        
        super(inputbuffersize);
        //initialize the distribution array 
        EDarray = new ArrayList();
        //initialize the queue for output
        q=new LinkedList();
    }
    
    
        @Override
        public int getDistribution(double standardDeviation) 
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
            
            /*
            for(Object x:EDarray)  
            {
                System.out.println((int)x);
            }
            System.out.println("-----------");
            */
            
            
            
            //***************
            int total = 0;
            //***************
            
            for(int x:a)  
            //for(Object x:EDarray)  
            {
                System.out.println(x);
                q.add(x);
                //***************
                total += x;
                //***************
            }
            
            //***************
            System.out.println("<Total>: "+total);
            //***************
            System.out.println("<E N D>\n");
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
            this.mean = mean;
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
        //int NumberOfTimesPacketsAreDeliverd = 3;
        int InputBufferSize = 2;
        int NumberOfPackets = 25;
        //*************************
        double mean = 10;//2.9;
        //*************************
        
        System.out.println("Packets arrival distributions: ");
            ExponentialDistribution arrivalAmounts= new ExponentialDistribution(InputBufferSize);
            
            //**********************************
            arrivalAmounts.SetMean(mean);
            //**********************************
            arrivalAmounts.SetNumebrOfPackets(NumberOfPackets);
            arrivalAmounts.getDistribution(0);
            
            
        System.out.println("Arrival Patterns");
            ExponentialDistribution arrivalPattern= new ExponentialDistribution(InputBufferSize);  
            int packetAmt;
            while((packetAmt = arrivalAmounts.getNumber()) != -1)
            {
                arrivalPattern.SetNumebrOfPackets(packetAmt);
                arrivalPattern.SetMean(mean);
                arrivalPattern.getDistribution(0);
                arrivalPattern.remove();
            }

    }
}
