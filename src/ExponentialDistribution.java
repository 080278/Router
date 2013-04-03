
import java.util.Random;

 /*
 * implements the Exponential distribution
 */
import java.util.*;
public class ExponentialDistribution extends Distribution
{
    //create random object
    Random rand = new Random();
    
    //create normal distribution array
   int[] EDarray = new int[1000];
   
   private Queue q;
           
   
    public  ExponentialDistribution(int inputbuffersize)
    {
        
        super(inputbuffersize);
        q=new LinkedList();
        getExponentialDistribution(mean());
    }
    
        
        public int[] getExponentialDistribution(double mean) 
        {
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
            System.out.println(x);
            q.addAll(Arrays.asList(a));
            return EDarray;
        } 
     
        public int getNumber()
        {
            int value=0;
            if (q.size() > 0)
            {
                value=(Integer)q.remove();
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
            ExponentialDistribution arrivalAmounts= new ExponentialDistribution(10);    
            
             ExponentialDistribution arrivalPattern= new ExponentialDistribution(arrivalAmounts.getNumber());  
             arrivalPattern.print();
    }
}
