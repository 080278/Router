 /*
 * implements the Normal Distribution using Java's Gaussian provision
 * Implementation culled from the org.apache.commons libraries
 */
import java.util.*;

public class NormalDistribution extends Distribution
{
     //create normal distribution array
     int[] NDarray = new int[1000];
  
   //Constructor for normal distribution
    public NormalDistribution(double mean,double standardDeviation)
    {
        super(mean, standardDeviation);
        getDistribution(15.5,0.5);
    }
    
 
    //NormalDistribution takes parameters: mean, standard deviation and uses the Random's gaussian method
    @Override
    public int getDistribution(double mean, double standardDeviation)
    {    
        for (int i=0;i<10;i++)
        {
            int val = (int) (mean + standardDeviation * rand.nextGaussian());
            System.out.println(val);
        }
        return -1;
    }
    
   
    public static void main(String[] args)
    {
       
        NormalDistribution arrivalAmounts= new NormalDistribution(15.5,0.5);
        
        //arrivalAmounts.getNormalDistribution();
        
        
        
    }

}
