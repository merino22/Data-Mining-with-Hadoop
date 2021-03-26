/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cleaningdata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import static jdk.nashorn.tools.ShellFunctions.input;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;

/**
 *
 * @author Jean Carlo
 */
public class CleaningData {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        FileWriter fw = null;
        BufferedWriter bw = null;
        System.out.println("Inicio");
        ArrayList<String> deleteWords = new ArrayList();
        deleteWords = readPalabras();

        try {
            File fichero = new File("/Users/jeancasoto/Downloads/cleanDataComplete.txt");
            fw = new FileWriter(fichero);
            bw = new BufferedWriter(fw);
            long contador = 0;
            long anterior = 0;
            try {
                String thisLine = null;
                try {
                    BufferedReader br = new BufferedReader(new FileReader("/Users/jeancasoto/Downloads/pullRequestsComments.txt"));
                    long total = 0;
                    while ((thisLine = br.readLine()) != null) {
                        total++;
                        String original = thisLine;
                        String comment = removeUrl(original);
                        comment = DeleteEspecialChars(comment);
                        for (int i = 0; i < deleteWords.size(); i++) {
                            comment = delete(comment, deleteWords.get(i));
                        }
                        bw.append(comment + "\n");
                        bw.flush();

                        contador++;

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
                fw.close();
            } catch (Exception e) {
            }
        }
        System.out.println("Fin del programa");
    }

    public void AppendFile(File file, String append) {
    }

    public static String DeleteEspecialChars(String str1) {
        //Primero eliminamos las url's del proyecto
        return str1.toLowerCase().replaceAll("-", " ").replaceAll("_", " ").replaceAll("[^a-zA-Z0-9^ ]", "").replaceAll(",", "").replaceAll("'", "");
    }

    public static String removeUrl(String commentstr) {
        // Regex que remueve url's
    String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(commentstr);
        int i = 0;
        while (m.find()) {
            try {
                commentstr = commentstr.replace(m.group(0)," ").trim();
                i++;
            } catch (Exception e) {
                System.out.print("Error al quitar un comment");
                System.out.println(" " + e);

            }

        }
        return commentstr;
    }

    public static ArrayList<String> readPalabras() {
        String fichero = "/Users/jeancasoto/Downloads/DeleteWords.txt";
        ArrayList<String> deleteWords = new ArrayList();
        int cont = 0;
        try {
            FileReader fr = new FileReader(fichero);
            BufferedReader br = new BufferedReader(fr);

            String linea;
            while ((linea = br.readLine()) != null) {
                deleteWords.add(linea + "");
                cont++;
            }
            System.out.println(cont);
            fr.close();
        } catch (Exception e) {
            System.out.println("Error leyendo fichero " + fichero + ": " + e);
        }
        return deleteWords;
    }

    public static String delete(String Comment, String WordToDelete) {
        // Recibe un stopWord y lo compara con la palabtra 
        return Comment.replaceAll("\\b" + WordToDelete + "\\b", "");

    }

}
