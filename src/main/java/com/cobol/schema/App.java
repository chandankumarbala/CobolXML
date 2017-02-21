package com.cobol.schema;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

import net.sf.cb2xml.Cb2Xml;
import net.sf.cb2xml.Xml2Dat;
import net.sf.cb2xml.convert.HashtableToMainframe;
import net.sf.cb2xml.convert.MainframeToXml;
import net.sf.cb2xml.convert.XmlToMainframe;
import net.sf.cb2xml.util.FileUtils;
import net.sf.cb2xml.util.XmlUtils;

/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) {
		// System.out.println( "Hello World!" );
		/*
		 * String inputCobolCpyBook="E:\\java\\cobolFiles\\address.cpy"; String
		 * xmlOutputFIle="E:\\java\\cobolFiles\\addressXML.xml";
		 * 
		 * 
		 * String xmlString=Cb2Xml.convertToXMLString(new
		 * File(inputCobolCpyBook)); System.out.println("Generated XML \n"
		 * +xmlString); try { FileWriter fw = new FileWriter(xmlOutputFIle);
		 * fw.write(xmlString); fw.close(); } catch ( Exception e){
		 * e.printStackTrace(); }
		 * 
		 * //String[] fileArr={xmlOutputFIle,inputCobolCpyBook};
		 * //Xml2Dat.main(fileArr); Hashtable<String, String> hashtable = new
		 * Hashtable<String, String>(); hashtable.put("COMPANY-NAME", "DBS");
		 * hashtable.put("LAST-NAME", "Bala"); hashtable.put("FIRST-NAME",
		 * "chandan");
		 * 
		 * String maindata=new HashtableToMainframe().convert(hashtable,
		 * XmlUtils.fileToDom(xmlOutputFIle)); System.out.println(maindata);
		 */

		convertToCobolXML("src/main/resources/b.copybook");
		//convertFlatFileToXMLDataFile("src/main/resources/b.copybook", "src/main/resources/emptyFile.txt");
		//convertXMLDataFileToFlatFile("src/main/resources/b.copybook", "src/main/resources/b.copybook_data_xml.xml");
		generateEmpTyXMLFile("src/main/resources/b.copybook_xml.xml", "src/main/resources/emptyXml.xsl" );
	}

	public static void convertToCobolXML(String path) {

		String outPutFile = path + "_xml.xml";

		if (!(new File(outPutFile).exists())) {
			File cobolFile = new File(path);
			String XmlString = Cb2Xml.convertToXMLString(cobolFile);
			writeFile(outPutFile, XmlString);
		} else {
			System.out.println("File already there skipped creation");
		}
	}

	public static void writeFile(String filePath, String fileContents) {
		try {
			FileWriter fw = new FileWriter(new File(filePath));
			fw.write(fileContents);
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void  convertFlatFileToXMLDataFile(String cobolFilePath,String flatFilePath){
		String cobolXmlPath=cobolFilePath+ "_xml.xml";
		String sourceFileContents= FileUtils.readFile(flatFilePath).toString();
        Document copyBookXml = XmlUtils.fileToDom(cobolXmlPath);
        Document resultDocument = new MainframeToXml().convert(sourceFileContents, copyBookXml);
        final StringBuffer resultBuffer = XmlUtils.domToString(resultDocument);
        
        String outputFIlePath=cobolFilePath+"_data_xml.xml";
        writeFile(outputFIlePath, resultBuffer.toString());
	}
	
	public static void convertXMLDataFileToFlatFile(String cobolFilePath,String cobolXMlDataFilePath){
		String outputFIlePath=cobolFilePath+".dat";
		
		String cobolXmlPath=cobolFilePath+ "_xml.xml";
		Document sourceFileXml = XmlUtils.fileToDom(cobolXMlDataFilePath);
        Document copyBookXml = XmlUtils.fileToDom(cobolXmlPath);

        String resultString = new XmlToMainframe().convert(sourceFileXml, copyBookXml);
        writeFile(outputFIlePath,resultString);
	}
	
	
	public static void generateEmpTyXMLFile(String xmlFile, String xslFile){
		
		Source xml = new StreamSource(new File(xmlFile));
		Source xsl=new StreamSource(new File(xslFile));
		 try {
			 TransformerFactory factory=TransformerFactory.newInstance();
		      Templates template = factory.newTemplates(xsl);
		      Transformer transformer = template.newTransformer();
		      OutputStream out = new BufferedOutputStream(new FileOutputStream(xmlFile.replace(".xml", "_empty.xml")),1024);
		      transformer.transform(xml, new StreamResult(out));
		     
		      
		    } catch(Exception tce) {
		        tce.printStackTrace();
		    } 
		  
	}
}
