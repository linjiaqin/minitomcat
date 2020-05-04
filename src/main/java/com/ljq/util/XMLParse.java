package com.ljq.util;

import com.ljq.MyServer;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;

public class XMLParse {
    private static Logger logger = LoggerFactory.getLogger(XMLParse.class);

    public static void parseWebXML(){
        String filePath= MyServer.class.getClassLoader().getResource("web.xml").getFile();
        logger.info("开始解析tomcat配置文件:"+filePath);
        parseXML(filePath);
    }
    public static HashMap<String, String> servletName = new HashMap<String, String>();
    public static HashMap<String, String> servletUrl = new HashMap<String, String>();
    public static void parseXML(String inputXml){
        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(inputXml);
            Element employees = document.getRootElement();
            for (Iterator i = employees.elementIterator(); i.hasNext(); ) {
                Element employee = (Element) i.next();
                //System.out.println(employee.getName());
                if (employee.getName().equals("servlet")){
                    String servletNames = "";
                    String servletClasses = "";
                    for (Iterator j = employee.elementIterator(); j.hasNext(); ) {
                        Element node = (Element) j.next();
                        //System.out.println(node.getName() + ":" + node.getText());
                        if (node.getName().equals("servlet-name")){
                            servletNames = node.getText();
                        }
                        else if (node.getName().equals("servlet-class")){
                            servletClasses = node.getText();
                        }
                    }
                    servletName.put(servletNames, servletClasses);
                }
                else if (employee.getName().equals("servlet-mapping")){
                    String servletNames = "";
                    String url = "";
                    for (Iterator j = employee.elementIterator(); j.hasNext(); ) {
                        Element node = (Element) j.next();
                        //System.out.println(node.getName() + ":" + node.getText());
                        if (node.getName().equals("servlet-name")){
                            servletNames = node.getText();
                        }
                        else if (node.getName().equals("url-pattern")){
                            url = node.getText();
                        }
                    }
                    servletUrl.put(url, servletNames);
                }
            }
        } catch (DocumentException e) {
            logger.error(e.getMessage());
        }
    }

//    public static void main(String[] args) {
//        String filePath= MyServer.class.getClassLoader().getResource("web.xml").getFile();
//        System.out.println(filePath);
//        parseXML(filePath);
//    }
}
