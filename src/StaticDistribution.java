 /*
 * implements the Static Distribution
 */
import java.util.*;
public class StaticDistribution extends Distribution
{
    //create random object
    Random rand = new Random();
    
    //create normal distribution array
   int[] SDarray = new int[1000];
    
    public  StaticDistribution(int inputbuffersize)
    {
        super(inputbuffersize);
        getStaticDistribution(mean());
    }
    
        
        public int[] getStaticDistribution(double mean) 
        {
             int val;
            for (int i=0;i<SDarray.length;i++)
            {
                val = (int) Math.round(-mean * Math.log(rand.nextDouble()));
                if (val >=0 && val<=inputbuffersize-1)
                {
                 
                     SDarray[i]=val;
                }
             System.out.print(SDarray[i]+ " ");
               
            }
            return SDarray;
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
           StaticDistribution why= new StaticDistribution(10);
       //System.out.println(why.getExponentialDistribution(why.mean()));
        
    }
}
