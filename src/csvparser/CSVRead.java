package csvparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




/**
 * CSV read
 * @author kim
 * @version 1.0
 */

public class CSVRead {


	
	private String rFilename = null;
	private String wFilename = null;
	
	public CSVRead(){
		
	}
	
	public CSVRead(String filename){
		rFilename = filename;
		wFilename = filename;
	}
	
	public CSVRead(String rFilename, String wFilename){
		this.rFilename = rFilename;
		this.wFilename = wFilename;
	}
	
	/**
     * csv read  
     * 
     *
     * @param     filename  파일명
     * @return    data      파일값
     */
	public static List<String> readCSV(String filename){
		
		List<String> data = new ArrayList<String>();
		
		try(FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr)){
			
			String s = null;
		
			int line = 0;
			
			while((s=br.readLine()) != null)
			{
//				System.out.println(s[0]);
				if(line++==0)
					continue;
				data.add(s);
			}
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		return data;
	}
	
	
	
	public List<String> splitCSV(List<String> list){
		
		//String csvSplitBy = ";";
		
		List<String> data = new ArrayList<String>();
		
		
		Iterator<String> it = list.iterator();
		StringBuffer temp = new StringBuffer();		
		while(it.hasNext())
		{
			String array = (String) it.next();
			//StringBuffer temp = new StringBuffer();
			temp.setLength(0);
			int loop = 0;
			int length = array.length();
            for (int i=0;i<length;i++)
            {
            	char a = array.charAt(i);
            	if(a == ';')
            	{           		
            		if(loop == 1)
            		{
            			String str = temp.toString(); 
            			
            			if(castMatch(str)){
            				int index1 = str.indexOf("src") + 3;
            				int index2 = str.indexOf("SRC") + 3;
            				if(index1 > index2)
            					str = str.substring(index2);
            				else
            					str = str.substring(index1);
            			}
            			
            			str=str.replace("\"","");
            			str=str.replace("]","");
                		
            			data.add(str);
               		}
            		temp.setLength(0);
            		loop++;
            		continue;
            	}
            	
            	temp.append(a);
            }
            //System.out.print("\n");
		}
		
		return data;
	}
	
    public void writeCSV(List<String> data, String filename) 
    {    	
    	String currentDir = System.getProperty("user.dir");
    	
    	String subPath = mkDir(currentDir);
    	
        try(FileWriter fw = new FileWriter(subPath+"\\"+"_"+filename);
        	BufferedWriter out = new BufferedWriter(fw)) 
        {        	
            Iterator<String> it = data.iterator();
            
            out.write("Object List");
            out.newLine();
	        while (it.hasNext()) 
	        {
	            String s = (String)it.next();
	            out.write(s); 
	            out.newLine();
	            //System.out.println(s);
	        }  
                
           
        } catch (IOException e) {
            e.printStackTrace();
            
        }
    }
    

    
    public List<String> curFileList()
    {
    	
    	List<String> data = new ArrayList<String>();
    	
    	String currentDir = System.getProperty("user.dir");
    	
    	
    	File dir = new File(currentDir); 
		File[] fileList = dir.listFiles(); 
				
		for(int i = 0 ; i < fileList.length ; i++)
		{
			File file = fileList[i]; 
			String filename = file.getName();
			if(file.isFile() && csvMatch(filename))
			{
				data.add(file.getName());
			}
		}
		
		
		
		return data;
    }
    
    public static String mkDir(String dir)
    {
    	File file = new File(dir+"/보완대상");
    	file.mkdir();
    	return dir+"\\보완대상";
    }
    
    public void modifyCSV(List<String> list)
    {
    	 
		List<String> data = new ArrayList<String>();
		List<String> dt = new ArrayList<String>();
    	    	
    	Iterator<String> it = list.iterator();
    	
    	while(it.hasNext())
    	{
    		String filename = (String)it.next();
    		data = readCSV(filename);    		
    		dt = splitCSV(data);    		
    		writeCSV(dt,filename);
    	}
    }
    
    public static boolean csvMatch(String filename)
    {
    	Pattern p = Pattern.compile("(^[a-zA-Z0-9_가-핳(),. \\-']*.csv)");
    	Matcher m = p.matcher(filename);
    	
    	return m.find() ? true:false;   	
  
    }
    
    public static boolean castMatch(String object)
    {
    	
    	Pattern p = Pattern.compile("(^[a-zA-Z:\\\\\\[\"]*CASTMS[a-zA-Z:\\\\\\[\\]]*)");
    	
    	Matcher m = p.matcher(object);
    	
    	return m.find() ? true:false;   	
  
    }
		
	public static void main(String[] args) {
		
		CSVRead rc = new CSVRead();		
		
		List<String> curFile = rc.curFileList();
		
		rc.modifyCSV(curFile);

	}

}