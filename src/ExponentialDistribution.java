
import java.util.Random;

 /*
 * implements the Exponential distribution
 */
import java.util.*;
public class ExponentialDistribution extends Distribution
{
//******************************************    
    //number of packets
    private int NumberOfPackets;
//******************************************
    
    //create random object
    Random rand = new Random();
    
    //create distribution array
   int[] EDarray;
   
   private Queue q;
           
   
    public  ExponentialDistribution(int inputbuffersize)
    {
        
        super(inputbuffersize);
        q=new LinkedList();
        
    }
    
    
        
        public int[] getExponentialDistribution() 
        {
            double mean = this.mean();
            int[] a =new int[inputbuffersize];
             int val;
            for (int i=0;i<EDarray.length;i++)
            {   
                val = (int) Math.round(-mean * Math.log(rand.nextDouble()));
                if (val >=0 && val<=inputbuffersize-1)
                {
                     EDarray[i]=val;
                     a[val] +=1;
                }
                
              //  System.out.print(EDarray[i]+ " ");  
            }
            for(int x:a)  
            {
                System.out.println(x);
                q.add(x);
                        
            }
            System.out.println("<E N D>\n");
            /*
            List lst = Arrays.asList(a);
            q.addAll(lst);
            */
            
            return EDarray;
        } 
     
        //set the number of packets
        public void SetNumebrOfPackets(int packets)
        {
            NumberOfPackets = packets;
            EDarray = new int[NumberOfPackets];
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
        
        public void print()
        {
            while (q.size() != 0)
            {
                System.out.println(getNumber());
            }
        }
        public double mean() 
        {
        double sum = 0;
        for (int i = 0; i < inputbuffersize ; i++) 
        {
            sum=sum+i;
        }
    return (sum / inputbuffersize);
   }
   
    public static void main(String[] args)
    {
        int NumberOfTimesPacketsAreDeliverd = 25;
        int NumberOfPackets = 250;
        
        System.out.println("Packets arrival distributions: ");
            ExponentialDistribution arrivalAmounts= new ExponentialDistribution(NumberOfTimesPacketsAreDeliverd);
            
            arrivalAmounts.SetNumebrOfPackets(NumberOfPackets);
            arrivalAmounts.getExponentialDistribution();
/*            
        System.out.println("Arrival Patterns");
            int num = arrivalAmounts.getNumber();
            ExponentialDistribution arrivalPattern= new ExponentialDistribution(num);  
        //System.out.println("port");
            arrivalPattern.print();
            */
    }
}
