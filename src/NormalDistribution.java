 /*
 * implements the Normal Distribution using Java's Gaussian provision
 * double value = rng.nextGaussian() * sd + mean;
 */
import java.util.*;
public class NormalDistribution extends Distribution
{
    
    //create random object
    Random rand = new Random();
    
    //create normal distribution array
   int[] NDarray = new int[1000];
    
    public NormalDistribution(int inputbuffersize)
    {
        super(inputbuffersize);
    }
    
    public int[] getNormalDistribution()
    { 
        int val;
        for (int i=0;i<NDarray.length;i++)
        {
            val = (int) Math.round(rand.nextGaussian());
            if (val >=0 && val<=inputbuffersize)
            {
                 
                 NDarray[i]=val;
            }
                
             System.out.println(NDarray[i]+ " ");
        }     
        return NDarray; 
    }
    
   public int mean() 
   {
        double sum = 0;
        for (int i = 0; i < inputbuffersize-1 ; i++) 
        {
            sum=sum+i;
        }
    return (int)Math.round(sum / inputbuffersize);
   }
    public static void main(String[] args)
    {
        NormalDistribution arrivalAmounts= new NormalDistribution(10);
        arrivalAmounts.getNormalDistribution();
        
        
        
    }

}
