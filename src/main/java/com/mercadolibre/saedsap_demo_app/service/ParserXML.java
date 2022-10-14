package com.mercadolibre.saedsap_demo_app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParserXML {
    private static final Logger log = LoggerFactory.getLogger(ParserXML.class);
    public List<String> parserTransacciones(String xml){
        List<String> transIndividuales= new ArrayList<>();
        try {
            log.info("Parseamos documento ");
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new InputSource(new StringReader(xml)));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            Element body = (Element) nodeList;
            XPath xPath = (XPath) XPathFactory.newInstance().newXPath();
            NodeList transacciones =body.getElementsByTagName("OrgnlPmtInfAndSts");
            log.info("tamaño de total tran "+transacciones.getLength());
            List<Node> listaTransacciones= new ArrayList<Node>();
            log.info("Obtener las transacciones con BTM");
            for(int i = 0; i <transacciones.getLength(); i++) {
                Node node = (Node) xPath.evaluate("TxInfAndSts/OrgnlEndToEndId", transacciones.item(i), XPathConstants.NODE);
                if(node.getTextContent().trim().startsWith("BTM")){
                    listaTransacciones.add(transacciones.item(i));
                }
            }
            if(!listaTransacciones.isEmpty()){
                int tam= transacciones.getLength();
                for (int i = 0; i < tam ; i++) {
                    Element product = (Element) transacciones.item(0);
                    product.getParentNode().removeChild(product);
                }

                transIndividuales = this.parserTrans(xPath,listaTransacciones,doc,body);
                for(String tran:transIndividuales){
                    log.info(tran);
                }

            }
            return transIndividuales;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return transIndividuales;
    }

    private List<String> parserTrans(XPath xPath,List<Node> listaTransacciones,Document doc,Element body) throws XPathExpressionException, TransformerException {
        log.info("Elimina nodo OrgnlGrpInfAndSts");
        Node orgnlGrpInfAndStsNode = (Node) xPath.evaluate("/Document/CstmrPmtStsRpt/OrgnlGrpInfAndSts", doc, XPathConstants.NODE);;
        orgnlGrpInfAndStsNode.getParentNode().removeChild(orgnlGrpInfAndStsNode);
        Node cstmrPmtStsRptNode = (Node) xPath.evaluate("/Document/CstmrPmtStsRpt", doc, XPathConstants.NODE);;
        List<String> transancionesIndividuales= new ArrayList<>();
        for(int i = 0; i < listaTransacciones.size(); i++) {
            Node trans=this.validaREJECT(xPath,listaTransacciones.get(i));
            if(i>=1) {
                cstmrPmtStsRptNode.replaceChild(trans, listaTransacciones.get(i - 1));
            }else{
                cstmrPmtStsRptNode.appendChild(trans);
            }
            transancionesIndividuales.add(xmlToString(body));
        }
        return transancionesIndividuales;
    }

    private Node validaREJECT(XPath xPath,Node transaccion) throws XPathExpressionException {
        String txSts = (String) xPath.evaluate("TxInfAndSts/TxSts/text()", transaccion, XPathConstants.STRING);
        if(txSts.equals("RJCT")){
            log.info("transaccion con sts RJCT");
            NodeList addInfNode = (NodeList) xPath.evaluate("TxInfAndSts/StsRsnInf/AddtlInf", transaccion, XPathConstants.NODESET);
            StringBuffer addInf= new StringBuffer();
            for(int j = 0; j <addInfNode.getLength(); j++) {
                addInf.append(addInfNode.item(j).getTextContent().trim());
                if(j==(addInfNode.getLength()-1)) {
                    addInfNode.item(j).setTextContent(addInf.toString());
                }else{
                    addInfNode.item(j).getParentNode().removeChild(addInfNode.item(j));
                }

            }
        }
        return transaccion;
    }

    private  String xmlToString(Element body) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        transformer = tf.newTransformer();
        StringWriter writer = new StringWriter();

        transformer.transform(new DOMSource(body), new StreamResult(writer));
        return writer.getBuffer().toString();

    }
}
