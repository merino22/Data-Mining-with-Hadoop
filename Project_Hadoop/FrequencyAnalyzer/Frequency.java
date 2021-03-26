
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aaron
 */
public class Frequency {
    public static void main(String[] args) throws IOException 
   {
      File f1=new File("/home/hdoop/Project_Hadoop/Project_Hadoop/WordCount/output_data/Output0/part-r-00000.txt"); //Creation of File Descriptor for input file
      int linecount=0;
      File f2=new File("/home/hdoop/Project_Hadoop/Project_Hadoop/WordCount2Freq/output_data/Output0/part-r-00000.txt"); //Creation of File Descriptor for input file
      int linecount2=0;
      FileReader fr=new FileReader(f1);
      BufferedReader br = new BufferedReader(fr);
      String s;              
      while((s=br.readLine())!=null)
      {
         linecount++;  
      }
      fr.close();
      FileReader fr2=new FileReader(f2);
      BufferedReader br2 = new BufferedReader(fr2);
      String str;              
      while((str=br2.readLine())!=null)
      {
         linecount2++;  
      }
      fr2.close();
      System.out.println("Word Count 1 Frequency:"+linecount);
      System.out.println("Word Count 2 Frequency:"+linecount2); //Print the line count
   }
}
