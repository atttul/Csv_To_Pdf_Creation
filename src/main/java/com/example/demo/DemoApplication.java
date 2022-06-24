package com.example.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(DemoApplication.class, args);

		// Reading CSV file from Directory: C:\\source\\META_24_06_2022.csv
		String sourceCsv = "C:\\source\\META_24_06_2022.csv";
		String sourcePdf="C:\\source\\AWCFPCIM20220622012345601F0123124.pdf";
		String line = "";

		String[] values = {};
		ArrayList<String> list = new ArrayList<>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(sourceCsv));
			while ((line = br.readLine()) != null) {
				values = line.split(",");
				list.add(values[1]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Creating PDF files according to our CSV file
		for (int i = 1; i < list.size(); i++) {
			FileInputStream fis=new FileInputStream(sourcePdf);
			FileOutputStream fos=new FileOutputStream("C:\\folder\\" + list.get(i));
			
			int j;
			while((j=fis.read())!=-1) {
				fos.write((char)j);
			}
		}

		// Creating a zip file for all PDF files
		String sourcePath = "C:\\folder";
		File sourceFile = new File(sourcePath);
		FileOutputStream fos = new FileOutputStream("C:\\source\\folded.zip");
		ZipOutputStream zos = new ZipOutputStream(fos);
		fetchFileToZip(sourceFile, sourceFile.getName(), zos);
		zos.close();
		fos.close();
	}

	private static void Zipping(File sourceFile, String path, ZipOutputStream zos) throws IOException {
		FileInputStream fis = new FileInputStream(sourceFile);
		ZipEntry zipEntry = new ZipEntry(path);
		zos.putNextEntry(zipEntry);
		final byte[] buffer = new byte[1024];
		int len;
		while ((len = fis.read(buffer)) != -1) {
			zos.write(buffer, 0, len);
		}
		fis.close();
	}

	private static void fetchFileToZip(File toZip, String path, ZipOutputStream zos) throws IOException {
		if (toZip.isDirectory()) {
			File[] files = toZip.listFiles();
			for (File fileName : files) {
				fetchFileToZip(fileName, fileName.getName(), zos);
			}

		} else {
			Zipping(toZip, path, zos);
		}
	}

}