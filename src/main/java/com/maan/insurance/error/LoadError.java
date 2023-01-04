package com.maan.insurance.error;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LoadError {
	public void load(){
		InputStream in=null;
		try{
			in = getClass().getResourceAsStream(FILE_DWR_XML);
			addConfig(in);
		}catch (Exception e) {		
//			System.out.println(e);
		}finally{
			if(in !=null){ 
				try{in.close();}catch (Exception e) {
//					System.out.println(e);
				}
			}
		}

	}
	
	private void addConfig(InputStream in) throws ParserConfigurationException, IOException, SAXException
	  {
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        dbf.setValidating(true);
	        DocumentBuilder db = dbf.newDocumentBuilder();  
	        Document doc = db.parse(in); 
	        addConfig(doc);
	        	
	  }
	private void addConfig(Document doc)
    {
        Element root = doc.getDocumentElement();

        NodeList rootChildren = root.getChildNodes();
        for (int i = 0; i < rootChildren.getLength(); i++)
        {
            Node node = rootChildren.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element child = (Element) node;

                if (child.getNodeName().equals(ELEMENT_INIT))
                {
                    loadInits(child);
                }
                 
            }
        }
    }
	
	
	 private void loadInits(Element child)
	    { 	
		 Error error=new Error();
	        NodeList inits = child.getChildNodes();
	        String key="";
	        for (int j = 0; j < inits.getLength(); j++)
	        {
	            if (inits.item(j).getNodeType() == Node.ELEMENT_NODE)
	            {
	                Element initer = (Element) inits.item(j); 
	                try{	    	    		
	    	    		
	    	    			key=initer.getTextContent()	;   	    		
	    	    		if(initer.getNodeName().equalsIgnoreCase("code"))
	    	    			error.setCode(initer.getTextContent());
	    	    		if(initer.getNodeName().equalsIgnoreCase("message"))
	    	    			error.setMessage(initer.getTextContent());
	    	    		if(initer.getNodeName().equalsIgnoreCase("field"))
	    	    			error.setField(initer.getTextContent()); 
	    	    	}catch (Exception e) {
	    	    		e.printStackTrace();
	    			}
	            }
	        }
	        ErrorInstance.add(error,key);
	    }
	    
	 	private final String ELEMENT_INIT="error";
		private final String PACKAGE = "com/maan/travel/login//errorblock";
		protected final String FILE_DWR_XML = "/"+PACKAGE + "/errorcode.xml"; 
}
