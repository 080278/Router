 /*
 * implements the Uniform Distribution
 */
import java.util.*;

public class UniformDistribution extends Distribution
{
    //create random object
    Random rand = new Random();
    
    //create normal distribution array
   int[] UDarray = new int[1000];
    
   private int standardDeviation;
   private int mean;
   
   
   //Uniform Distribution class constructor
    public  UniformDistribution(double mean,double standardDeviation)
    {
        super(mean, standardDeviation);
        getDistribution(4.5,1.5);
    }
    
        @Override
        public int getDistribution(double mean,double standardDeviation) 
        {
            for (int i=0;i<10;i++)
            {
                double value = (Math.log(1 / (1 - rand.nextDouble())) + mean) * Math.sqrt(standardDeviation);
                System.out.println((int)value);
            }
            return -1;
        } 
   
       
    public static void main(String[] args)
    {
           UniformDistribution distribution= new UniformDistribution(4.5,1.5);
   
    }
}
