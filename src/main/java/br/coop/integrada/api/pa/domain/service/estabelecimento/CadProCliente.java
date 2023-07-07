package br.coop.integrada.api.pa.domain.service.estabelecimento;

import java.util.List;
import org.w3c.dom.Element;
import java.io.IOException;
import java.util.ArrayList;
import org.w3c.dom.Document;

import org.w3c.dom.NodeList;
import java.io.StringReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import javax.xml.parsers.DocumentBuilder;
import org.apache.http.client.HttpClient;

import org.springframework.stereotype.Service;
import org.apache.http.client.methods.HttpGet;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import javax.xml.parsers.ParserConfigurationException;
import br.coop.integrada.api.pa.domain.model.cadpro.CadPro;

@Service
public class CadProCliente {
    private static String API_URL = "https://api.godigibee.io/pipeline/integrada-coop/v1";
    
    public List<CadPro> consultarCadpro(String cadpro, String uf){
    	cadpro = cadpro.replaceAll("[^0-9]+", "");
        List<CadPro> response = new ArrayList<>();
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();           
            
            HttpGet httpGet = new HttpGet(requestUrl(cadpro, uf));           
            httpGet.addHeader("apikey", "7qSlKLRXTRgwPPIDsEExtj1ApLSv7Pye");
                       
            HttpResponse httpResponse = httpClient.execute(httpGet);                   
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity);
            
            result = result.substring(result.indexOf("<infCons>"), (result.indexOf("</retConsCad>")));
           
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(result)));
            
            Element elemento = doc.getDocumentElement();
            NodeList nodeList = elemento.getElementsByTagName("infCad");  
           
            for( int i=0; i<nodeList.getLength(); i++ ) {
                CadPro dados = new CadPro();
                Element tagEvento = (Element) nodeList.item( i );
                dados.setUf(getChildTagValue(tagEvento, "UF"));
                dados.setCad(getChildTagValue(tagEvento, "IE"));  
                dados.setNome(getChildTagValue(tagEvento, "xNome"));
                
                if(getChildTagValue(tagEvento, "cSit").equals("0")) {
                	dados.setDescricao("CADPRO BAIXADO.");
                	dados.setBaixado(true);
                }
                
                if(getChildTagValue(tagEvento, "cSit").equals("1")) {
                	dados.setDescricao("CADPRO ATIVO.");
                	dados.setBaixado(false);
                }
                
                if(getChildTagValue(tagEvento, "CPF") != null) {
                	dados.setCpfCnpj(getChildTagValue(tagEvento, "CPF"));
                }
                
                if(getChildTagValue(tagEvento, "CNPJ") != null) {
                	dados.setCpfCnpj(getChildTagValue(tagEvento, "CNPJ"));
                }
                response.add(dados);                
            }
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        return response;
    }  
    
    private static String getChildTagValue( Element elem, String tagName ) throws Exception {
        NodeList children = elem.getElementsByTagName( tagName );
        if( children == null ) return null;
        Element child = (Element) children.item(0);
        if( child == null ) return null;
        return child.getFirstChild().getNodeValue();
    }
    
    public static Document stringToDom(String xmlSource) throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xmlSource)));
    } 
    
    public static String requestUrl(String cadpro, String ie){
        StringBuilder sb = new StringBuilder(API_URL);
        sb.append(String.format("/consulta-cadpro?IE=%s&UF=%s",cadpro, ie));
        return sb.toString();
    }
}