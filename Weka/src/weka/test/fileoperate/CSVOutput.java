package weka.test.fileoperate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class CSVOutput {

	public static void main(String[] args) throws IOException {

		String dir = "D://userinfo20141020.txt";
		String out_dir = "D://weka_user.csv";
		File file = new File(dir);
		File out_file = new File(out_dir);
		
		BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream(file),"gbk"));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out_file),"UTF-8"));
		
		String lineTxt = "";
		
		while((lineTxt=read.readLine())!=null){
//			System.out.println(lineTxt.split("\t").length);
			System.out.print("\""+lineTxt.replace("\t", "\",\"")+"\""+"\r\n");
			writer.write("\""+lineTxt.replace("\t", "\",\"")+"\""+"\r\n");
		}
		
		read.close();
		writer.close();
	}

}
