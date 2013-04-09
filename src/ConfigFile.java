/*
 * This implements the class to read the config file
 */

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class ConfigFile {
    
    //use an array of hash table
    Hashtable configSections;
    
    //constructor
    public ConfigFile()
    {
        //initialize hashtable
        configSections = new Hashtable();
        //read the config file
        ReadConfigFile();
    }
    
    private boolean ReadConfigFile()
    {
        //read status
        boolean status = false;
        //holds each line read
        String data = null;
        //hold the name of the section of the config file
        String section = null;
        
        try
        {
            //open the file input stream
            FileInputStream inStream = new FileInputStream(System.getProperty("user.dir")+"/ConfigFile.cfg");
            //get the data stream for the file
            DataInputStream in = new DataInputStream(inStream);
            //read data from the file
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
                                  
            //iterate the file line by line
            while((data = br.readLine()) != null)
            {
                //ensure not a blank line
                if(data.length() != 0)
                {
                    //ensure not a comment
                    if(data.charAt(0) != '#') 
                    {
                        //check for section in the config file
                        if (data.charAt(0) == '[')
                        {
                            //get the section name of the config file
                            section = data.substring(1, data.length()-1);
                            //put section in the main hash table, to hold section config
                            configSections.put(section.toUpperCase(),new Hashtable());
                        }
                        else
                        {
                            //split the config line based on the equal sign
                            String []info = data.split("=");
                            
                            Hashtable tmpHash = (Hashtable)configSections.get(section);
                            
                            try
                            {
                                //enter config the section with the Integer values
                                tmpHash.put(info[0].trim().toUpperCase(),Integer.parseInt((info[1].trim().toLowerCase())));
                            }
                            catch(NumberFormatException e){
                                //enter config the section with the String values
                                tmpHash.put(info[0].trim().toUpperCase(),info[1].trim().toLowerCase());
                            }
                            
                        }

                    //read was successful
                    status = true;
                    }
                }
            }
            
            //close input
            in.close();
        }
        catch(Exception e){
            //get the error message
            //System.out.println("Error: " + e.getMessage());
            status = false;
        }
                
        //return status of file read
        return status;
    }
    
    //get the section 
    public Object GetConfig(String section, String option)
    {
        //get hashtable for the section
        Hashtable tmp = (Hashtable)configSections.get(section.toUpperCase());
        
        //get the value of the option
        return tmp.get(option.toUpperCase());
    }
    
    
    public static void main(String []args)
    {
        ConfigFile tst = new ConfigFile();
        System.out.println(tst.GetConfig("router","pulse"));
    }
}
