/*
 * This implements the class to read the config file
 */

import java.io.*;
import java.util.*;

public class ExportFile {
    
    //open the file output stream
    FileOutputStream outStream;
    //get the data stream for the file
    DataOutputStream out;
    //read data from the file
    BufferedWriter br;
    
    //constructor
    public ExportFile()
    {
        
        //write the config file
        WriteConfigFile();
    }
    
    private boolean WriteConfigFile()
    {
        //write status
        boolean status = false;
        
        
        try
        {
            //open the file output stream
            outStream = new FileOutputStream(System.getProperty("user.dir")+"/Data.csv");
            //get the data stream for the file
            out = new DataOutputStream(outStream);
            //read data from the file
            br = new BufferedWriter(new OutputStreamWriter(out));
            status = true;
        }
        catch(Exception e){}
            
                
        //return status of file read
        return status;
    }
    
    //get the section 
    public boolean WriteText(String text)
    {
        try{
            out.writeChars(text+"\n");
            return true;
        }
        catch(Exception e){}
        return false;
    }
    
    //close the file
    public boolean Close()
    {
        try{
            out.close();
            return true;
        }catch(Exception e){}
        return false;
    }
    
    
}
