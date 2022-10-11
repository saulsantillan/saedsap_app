package com.mercadolibre.saedsap_demo_app.parseaXml;

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
import javax.xml.transform.TransformerConfigurationException;
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

public class ParseaXml {

    public static void main(String args []){
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new InputSource(new StringReader(getXml())));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            Element body = (Element) nodeList;
            XPath xPath = (XPath) XPathFactory.newInstance().newXPath();

            /**Obtener las transacciones con BTM**/
            NodeList transacciones =body.getElementsByTagName("OrgnlPmtInfAndSts");
            System.out.println("tamaño de transacciones "+transacciones.getLength());
            List<Node> listaTransacciones= new ArrayList<Node>();
            for(int i = 0; i <transacciones.getLength(); i++) {
                Node node = (Node) xPath.evaluate("TxInfAndSts/OrgnlEndToEndId", transacciones.item(i), XPathConstants.NODE);
                if(node.getTextContent().trim().startsWith("BTM")){
                    System.out.println(node.getTextContent());
                    listaTransacciones.add(transacciones.item(i));
                }
            }

            if(!listaTransacciones.isEmpty()){
                /**Limpiamos el DOM**/
                int tam= transacciones.getLength();
                for (int i = 0; i < tam ; i++) {
                    Element product = (Element) transacciones.item(0);
                    product.getParentNode().removeChild(product);
                }

                List<String> transIndividuales = parserTrans(xPath,listaTransacciones,doc,body);
                for(String tran:transIndividuales){
                    System.out.println(tran);
                }
                System.out.println(transIndividuales.size());
            }

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
    }

    private static List<String> parserTrans(XPath xPath,List<Node> listaTransacciones,Document doc,Element body) throws XPathExpressionException, TransformerException {
        /** Se realizan acciones no dinamicas OrgnlGrpInfAndSts**/
        Node orgnlGrpInfAndStsNode = (Node) xPath.evaluate("/Document/CstmrPmtStsRpt/OrgnlGrpInfAndSts", doc, XPathConstants.NODE);;
        Node node = (Node) xPath.evaluate("OrgnlNbOfTxs", orgnlGrpInfAndStsNode, XPathConstants.NODE);
        node.setTextContent("1");
        Node nbOfTxsPerStsNode = (Node) xPath.evaluate("NbOfTxsPerSts", orgnlGrpInfAndStsNode, XPathConstants.NODE);
        nbOfTxsPerStsNode.getParentNode().removeChild(nbOfTxsPerStsNode);
        Node ctrlSumNode = (Node) xPath.evaluate("CtrlSum", orgnlGrpInfAndStsNode, XPathConstants.NODE);

        Node cstmrPmtStsRptNode = (Node) xPath.evaluate("/Document/CstmrPmtStsRpt", doc, XPathConstants.NODE);;
        List<String> transancionesIndividuales= new ArrayList<>();

        for(int i = 0; i < listaTransacciones.size(); i++) {
            Node ammount = (Node) xPath.evaluate("TxInfAndSts/OrgnlTxRef/Amt/InstdAmt", listaTransacciones.get(i), XPathConstants.NODE);
            ctrlSumNode.setTextContent(ammount.getTextContent().trim());
           Node trans=validaREJECT(xPath,listaTransacciones.get(i));
            if(i>=1) {
                cstmrPmtStsRptNode.replaceChild(trans, listaTransacciones.get(i - 1));
            }else{
                cstmrPmtStsRptNode.appendChild(trans);
            }
            transancionesIndividuales.add(xmlToString(body));
        }
        return transancionesIndividuales;
    }

    private static String xmlToString(Element body) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        transformer = tf.newTransformer();
        StringWriter writer = new StringWriter();

        transformer.transform(new DOMSource(body), new StreamResult(writer));
        return writer.getBuffer().toString();

    }
    private static Node validaREJECT(XPath xPath,Node transaccion) throws XPathExpressionException {
        String txSts = (String) xPath.evaluate("TxInfAndSts/TxSts/text()", transaccion, XPathConstants.STRING);
        System.out.println(txSts);
        if(txSts.equals("RJCT")){
            NodeList addInfNode = (NodeList) xPath.evaluate("TxInfAndSts/StsRsnInf/AddtlInf", transaccion, XPathConstants.NODESET);
            StringBuffer addInf= new StringBuffer();
            for(int j = 0; j <addInfNode.getLength(); j++) {
                System.out.println(addInfNode.item(j).getTextContent().trim());
                addInf.append(addInfNode.item(j).getTextContent().trim());
                if(j==(addInfNode.getLength()-1)) {
                    addInfNode.item(j).setTextContent(addInf.toString());
                }else{
                    addInfNode.item(j).getParentNode().removeChild(addInfNode.item(j));
                }

            }
            System.out.println(addInf.toString());
        }
        return transaccion;
    }
    private static String getXml(){
        String accept = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<Document xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:iso:std:iso:20022:tech:xsd:pain.002.001.03\"><CstmrPmtStsRpt><GrpHdr><MsgId>CITIBANK/20220513-PSR/6448806</MsgId><CreDtTm>2022-05-13T11:40:48</CreDtTm><InitgPty><Id><OrgId><BICOrBEI>CITIUS33</BICOrBEI></OrgId></Id></InitgPty></GrpHdr><OrgnlGrpInfAndSts><OrgnlMsgId>mlu:withdraw:citibank:1652412300000</OrgnlMsgId><OrgnlMsgNmId>pain.001.001.03</OrgnlMsgNmId><OrgnlCreDtTm>2022-05-13T00:29:42</OrgnlCreDtTm><OrgnlNbOfTxs>43</OrgnlNbOfTxs><CtrlSum>234044.51</CtrlSum><NbOfTxsPerSts><DtldNbOfTxs>18</DtldNbOfTxs><DtldSts>ACSP</DtldSts></NbOfTxsPerSts></OrgnlGrpInfAndSts><OrgnlPmtInfAndSts><OrgnlPmtInfId>mlu:withdraw:citibank:1652412300000</OrgnlPmtInfId><TxInfAndSts><OrgnlEndToEndId>22315186494</OrgnlEndToEndId><TxSts>ACSP</TxSts><StsRsnInf><Orgtr><Nm>CitiDirect</Nm></Orgtr><Rsn><Cd>NARR</Cd></Rsn><AddtlInf>/00000000/CB Processed</AddtlInf></StsRsnInf><AcctSvcrRef>04986885000000003</AcctSvcrRef><OrgnlTxRef><Amt><InstdAmt Ccy=\"UYU\">4116.9</InstdAmt></Amt><ReqdExctnDt>2022-05-13</ReqdExctnDt><PmtTpInf><SvcLvl><Cd>NURG</Cd></SvcLvl><LclInstrm><Prtry>441</Prtry></LclInstrm></PmtTpInf><PmtMtd>TRF</PmtMtd><Dbtr><Nm>MERCADOPAGO S.A</Nm><PstlAdr><Ctry>UY</Ctry></PstlAdr></Dbtr><DbtrAcct><Id><Othr><Id>0061140026</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></DbtrAcct><DbtrAgt><FinInstnId><BIC>CITIUYMMXXX</BIC><ClrSysMmbId><MmbId>Uruguay</MmbId></ClrSysMmbId><PstlAdr><Ctry>UY</Ctry></PstlAdr></FinInstnId><BrnchId><Id>858</Id></BrnchId></DbtrAgt><CdtrAgt><FinInstnId><ClrSysMmbId><MmbId>113</MmbId></ClrSysMmbId></FinInstnId><BrnchId><Id>0001</Id></BrnchId></CdtrAgt><Cdtr><Nm>Vilmar Quiroga Silveira</Nm></Cdtr><CdtrAcct><Id><Othr><Id>9477951</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></CdtrAcct></OrgnlTxRef></TxInfAndSts></OrgnlPmtInfAndSts><OrgnlPmtInfAndSts><OrgnlPmtInfId>mlu:withdraw:citibank:1652412300000</OrgnlPmtInfId><TxInfAndSts><OrgnlEndToEndId>BTM22315186494</OrgnlEndToEndId><TxSts>ACSP</TxSts><StsRsnInf><Orgtr><Nm>CitiDirect</Nm></Orgtr><Rsn><Cd>NARR</Cd></Rsn><AddtlInf>/00000000/CB Processed</AddtlInf></StsRsnInf><AcctSvcrRef>04986885000000003</AcctSvcrRef><OrgnlTxRef><Amt><InstdAmt Ccy=\"UYU\">5089.9</InstdAmt></Amt><ReqdExctnDt>2022-05-13</ReqdExctnDt><PmtTpInf><SvcLvl><Cd>NURG</Cd></SvcLvl><LclInstrm><Prtry>441</Prtry></LclInstrm></PmtTpInf><PmtMtd>TRF</PmtMtd><Dbtr><Nm>MERCADOPAGO S.A</Nm><PstlAdr><Ctry>UY</Ctry></PstlAdr></Dbtr><DbtrAcct><Id><Othr><Id>0061140026</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></DbtrAcct><DbtrAgt><FinInstnId><BIC>CITIUYMMXXX</BIC><ClrSysMmbId><MmbId>Uruguay</MmbId></ClrSysMmbId><PstlAdr><Ctry>UY</Ctry></PstlAdr></FinInstnId><BrnchId><Id>858</Id></BrnchId></DbtrAgt><CdtrAgt><FinInstnId><ClrSysMmbId><MmbId>113</MmbId></ClrSysMmbId></FinInstnId><BrnchId><Id>0001</Id></BrnchId></CdtrAgt><Cdtr><Nm>Vilmar Quiroga Silveira</Nm></Cdtr><CdtrAcct><Id><Othr><Id>9477951</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></CdtrAcct></OrgnlTxRef></TxInfAndSts></OrgnlPmtInfAndSts><OrgnlPmtInfAndSts><OrgnlPmtInfId>mlu:withdraw:citibank:1652412300000</OrgnlPmtInfId><TxInfAndSts><OrgnlEndToEndId>BTM22315186494</OrgnlEndToEndId><TxSts>ACSP</TxSts><StsRsnInf><Orgtr><Nm>CitiDirect</Nm></Orgtr><Rsn><Cd>NARR</Cd></Rsn><AddtlInf>/00000000/CB Processed</AddtlInf></StsRsnInf><AcctSvcrRef>04986885000000003</AcctSvcrRef><OrgnlTxRef><Amt><InstdAmt Ccy=\"UYU\">6899.9</InstdAmt></Amt><ReqdExctnDt>2022-05-13</ReqdExctnDt><PmtTpInf><SvcLvl><Cd>NURG</Cd></SvcLvl><LclInstrm><Prtry>441</Prtry></LclInstrm></PmtTpInf><PmtMtd>TRF</PmtMtd><Dbtr><Nm>MERCADOPAGO S.A</Nm><PstlAdr><Ctry>UY</Ctry></PstlAdr></Dbtr><DbtrAcct><Id><Othr><Id>0061140026</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></DbtrAcct><DbtrAgt><FinInstnId><BIC>CITIUYMMXXX</BIC><ClrSysMmbId><MmbId>Uruguay</MmbId></ClrSysMmbId><PstlAdr><Ctry>UY</Ctry></PstlAdr></FinInstnId><BrnchId><Id>858</Id></BrnchId></DbtrAgt><CdtrAgt><FinInstnId><ClrSysMmbId><MmbId>113</MmbId></ClrSysMmbId></FinInstnId><BrnchId><Id>0001</Id></BrnchId></CdtrAgt><Cdtr><Nm>Vilmar Quiroga Silveira</Nm></Cdtr><CdtrAcct><Id><Othr><Id>9477951</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></CdtrAcct></OrgnlTxRef></TxInfAndSts></OrgnlPmtInfAndSts><OrgnlPmtInfAndSts><OrgnlPmtInfId>mlu:withdraw:citibank:1652412300000</OrgnlPmtInfId><TxInfAndSts><OrgnlEndToEndId>BTM22315186494</OrgnlEndToEndId><TxSts>ACSP</TxSts><StsRsnInf><Orgtr><Nm>CitiDirect</Nm></Orgtr><Rsn><Cd>NARR</Cd></Rsn><AddtlInf>/00000000/CB Processed</AddtlInf></StsRsnInf><AcctSvcrRef>04986885000000003</AcctSvcrRef><OrgnlTxRef><Amt><InstdAmt Ccy=\"UYU\">900900.9</InstdAmt></Amt><ReqdExctnDt>2022-05-13</ReqdExctnDt><PmtTpInf><SvcLvl><Cd>NURG</Cd></SvcLvl><LclInstrm><Prtry>441</Prtry></LclInstrm></PmtTpInf><PmtMtd>TRF</PmtMtd><Dbtr><Nm>MERCADOPAGO S.A</Nm><PstlAdr><Ctry>UY</Ctry></PstlAdr></Dbtr><DbtrAcct><Id><Othr><Id>0061140026</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></DbtrAcct><DbtrAgt><FinInstnId><BIC>CITIUYMMXXX</BIC><ClrSysMmbId><MmbId>Uruguay</MmbId></ClrSysMmbId><PstlAdr><Ctry>UY</Ctry></PstlAdr></FinInstnId><BrnchId><Id>858</Id></BrnchId></DbtrAgt><CdtrAgt><FinInstnId><ClrSysMmbId><MmbId>113</MmbId></ClrSysMmbId></FinInstnId><BrnchId><Id>0001</Id></BrnchId></CdtrAgt><Cdtr><Nm>Vilmar Quiroga Silveira</Nm></Cdtr><CdtrAcct><Id><Othr><Id>9477951</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></CdtrAcct></OrgnlTxRef></TxInfAndSts></OrgnlPmtInfAndSts></CstmrPmtStsRpt></Document>\n";
        String reject="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<Document xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:iso:std:iso:20022:tech:xsd:pain.002.001.03\"><CstmrPmtStsRpt><GrpHdr><MsgId>CITIBANK/20220513-PSR/6448280</MsgId><CreDtTm>2022-05-13T11:10:59</CreDtTm><InitgPty><Id><OrgId><BICOrBEI>CITIUS33</BICOrBEI></OrgId></Id></InitgPty></GrpHdr><OrgnlGrpInfAndSts><OrgnlMsgId>mlu:withdraw:citibank:1652410200000</OrgnlMsgId><OrgnlMsgNmId>pain.001.001.03</OrgnlMsgNmId><OrgnlCreDtTm>2022-05-13T00:29:55</OrgnlCreDtTm><OrgnlNbOfTxs>33</OrgnlNbOfTxs><CtrlSum>234044.51</CtrlSum><NbOfTxsPerSts><DtldNbOfTxs>1</DtldNbOfTxs><DtldSts>RJCT</DtldSts></NbOfTxsPerSts></OrgnlGrpInfAndSts><OrgnlPmtInfAndSts><OrgnlPmtInfId>mlu:withdraw:citibank:1652410200000</OrgnlPmtInfId><TxInfAndSts><OrgnlEndToEndId>BTM22314631276</OrgnlEndToEndId><TxSts>RJCT</TxSts><StsRsnInf><Orgtr><Nm>CitiDirect</Nm></Orgtr><Rsn><Cd>NARR</Cd></Rsn><AddtlInf>/99999999/CB Rejected</AddtlInf><AddtlInf>No account / Unable to Locate Accou</AddtlInf><AddtlInf>nt (R03 in US-ACH) (LA_83)</AddtlInf></StsRsnInf><AcctSvcrRef>04986885700000029</AcctSvcrRef><OrgnlTxRef><Amt><InstdAmt Ccy=\"UYU\">1490</InstdAmt></Amt><ReqdExctnDt>2022-05-13</ReqdExctnDt><PmtTpInf><SvcLvl><Cd>NURG</Cd></SvcLvl><LclInstrm><Prtry>441</Prtry></LclInstrm></PmtTpInf><PmtMtd>TRF</PmtMtd><Dbtr><Nm>MERCADOPAGO S.A</Nm><PstlAdr><Ctry>UY</Ctry></PstlAdr></Dbtr><DbtrAcct><Id><Othr><Id>0061140026</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></DbtrAcct><DbtrAgt><FinInstnId><BIC>CITIUYMMXXX</BIC><ClrSysMmbId><MmbId>Uruguay</MmbId></ClrSysMmbId><PstlAdr><Ctry>UY</Ctry></PstlAdr></FinInstnId><BrnchId><Id>858</Id></BrnchId></DbtrAgt><CdtrAgt><FinInstnId><ClrSysMmbId><MmbId>001</MmbId></ClrSysMmbId></FinInstnId><BrnchId><Id>0001</Id></BrnchId></CdtrAgt><Cdtr><Nm>Carmen Maclean</Nm></Cdtr><CdtrAcct><Id><Othr><Id>01044717525185</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></CdtrAcct></OrgnlTxRef></TxInfAndSts></OrgnlPmtInfAndSts><OrgnlPmtInfAndSts><OrgnlPmtInfId>mlu:withdraw:citibank:1652410200000</OrgnlPmtInfId><TxInfAndSts><OrgnlEndToEndId>22314631276</OrgnlEndToEndId><TxSts>RJCT</TxSts><StsRsnInf><Orgtr><Nm>CitiDirect</Nm></Orgtr><Rsn><Cd>NARR</Cd></Rsn><AddtlInf>/99999999/CB Rejected</AddtlInf><AddtlInf>No account / Unable to Locate Accou</AddtlInf><AddtlInf>nt (R03 in US-ACH) (LA_83)</AddtlInf></StsRsnInf><AcctSvcrRef>04986885700000029</AcctSvcrRef><OrgnlTxRef><Amt><InstdAmt Ccy=\"UYU\">1490</InstdAmt></Amt><ReqdExctnDt>2022-05-13</ReqdExctnDt><PmtTpInf><SvcLvl><Cd>NURG</Cd></SvcLvl><LclInstrm><Prtry>441</Prtry></LclInstrm></PmtTpInf><PmtMtd>TRF</PmtMtd><Dbtr><Nm>MERCADOPAGO S.A</Nm><PstlAdr><Ctry>UY</Ctry></PstlAdr></Dbtr><DbtrAcct><Id><Othr><Id>0061140026</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></DbtrAcct><DbtrAgt><FinInstnId><BIC>CITIUYMMXXX</BIC><ClrSysMmbId><MmbId>Uruguay</MmbId></ClrSysMmbId><PstlAdr><Ctry>UY</Ctry></PstlAdr></FinInstnId><BrnchId><Id>858</Id></BrnchId></DbtrAgt><CdtrAgt><FinInstnId><ClrSysMmbId><MmbId>001</MmbId></ClrSysMmbId></FinInstnId><BrnchId><Id>0001</Id></BrnchId></CdtrAgt><Cdtr><Nm>Carmen Maclean</Nm></Cdtr><CdtrAcct><Id><Othr><Id>01044717525185</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></CdtrAcct></OrgnlTxRef></TxInfAndSts></OrgnlPmtInfAndSts></CstmrPmtStsRpt></Document>";
        String sinBTM="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<Document xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:iso:std:iso:20022:tech:xsd:pain.002.001.03\"><CstmrPmtStsRpt><GrpHdr><MsgId>CITIBANK/20220513-PSR/6448806</MsgId><CreDtTm>2022-05-13T11:40:48</CreDtTm><InitgPty><Id><OrgId><BICOrBEI>CITIUS33</BICOrBEI></OrgId></Id></InitgPty></GrpHdr><OrgnlGrpInfAndSts><OrgnlMsgId>mlu:withdraw:citibank:1652412300000</OrgnlMsgId><OrgnlMsgNmId>pain.001.001.03</OrgnlMsgNmId><OrgnlCreDtTm>2022-05-13T00:29:42</OrgnlCreDtTm><OrgnlNbOfTxs>43</OrgnlNbOfTxs><CtrlSum>234044.51</CtrlSum><NbOfTxsPerSts><DtldNbOfTxs>18</DtldNbOfTxs><DtldSts>ACSP</DtldSts></NbOfTxsPerSts></OrgnlGrpInfAndSts><OrgnlPmtInfAndSts><OrgnlPmtInfId>mlu:withdraw:citibank:1652412300000</OrgnlPmtInfId><TxInfAndSts><OrgnlEndToEndId>22315186494</OrgnlEndToEndId><TxSts>ACSP</TxSts><StsRsnInf><Orgtr><Nm>CitiDirect</Nm></Orgtr><Rsn><Cd>NARR</Cd></Rsn><AddtlInf>/00000000/CB Processed</AddtlInf></StsRsnInf><AcctSvcrRef>04986885000000003</AcctSvcrRef><OrgnlTxRef><Amt><InstdAmt Ccy=\"UYU\">4116.9</InstdAmt></Amt><ReqdExctnDt>2022-05-13</ReqdExctnDt><PmtTpInf><SvcLvl><Cd>NURG</Cd></SvcLvl><LclInstrm><Prtry>441</Prtry></LclInstrm></PmtTpInf><PmtMtd>TRF</PmtMtd><Dbtr><Nm>MERCADOPAGO S.A</Nm><PstlAdr><Ctry>UY</Ctry></PstlAdr></Dbtr><DbtrAcct><Id><Othr><Id>0061140026</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></DbtrAcct><DbtrAgt><FinInstnId><BIC>CITIUYMMXXX</BIC><ClrSysMmbId><MmbId>Uruguay</MmbId></ClrSysMmbId><PstlAdr><Ctry>UY</Ctry></PstlAdr></FinInstnId><BrnchId><Id>858</Id></BrnchId></DbtrAgt><CdtrAgt><FinInstnId><ClrSysMmbId><MmbId>113</MmbId></ClrSysMmbId></FinInstnId><BrnchId><Id>0001</Id></BrnchId></CdtrAgt><Cdtr><Nm>Vilmar Quiroga Silveira</Nm></Cdtr><CdtrAcct><Id><Othr><Id>9477951</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></CdtrAcct></OrgnlTxRef></TxInfAndSts></OrgnlPmtInfAndSts><OrgnlPmtInfAndSts><OrgnlPmtInfId>mlu:withdraw:citibank:1652412300000</OrgnlPmtInfId><TxInfAndSts><OrgnlEndToEndId>22315186494</OrgnlEndToEndId><TxSts>ACSP</TxSts><StsRsnInf><Orgtr><Nm>CitiDirect</Nm></Orgtr><Rsn><Cd>NARR</Cd></Rsn><AddtlInf>/00000000/CB Processed</AddtlInf></StsRsnInf><AcctSvcrRef>04986885000000003</AcctSvcrRef><OrgnlTxRef><Amt><InstdAmt Ccy=\"UYU\">5089.9</InstdAmt></Amt><ReqdExctnDt>2022-05-13</ReqdExctnDt><PmtTpInf><SvcLvl><Cd>NURG</Cd></SvcLvl><LclInstrm><Prtry>441</Prtry></LclInstrm></PmtTpInf><PmtMtd>TRF</PmtMtd><Dbtr><Nm>MERCADOPAGO S.A</Nm><PstlAdr><Ctry>UY</Ctry></PstlAdr></Dbtr><DbtrAcct><Id><Othr><Id>0061140026</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></DbtrAcct><DbtrAgt><FinInstnId><BIC>CITIUYMMXXX</BIC><ClrSysMmbId><MmbId>Uruguay</MmbId></ClrSysMmbId><PstlAdr><Ctry>UY</Ctry></PstlAdr></FinInstnId><BrnchId><Id>858</Id></BrnchId></DbtrAgt><CdtrAgt><FinInstnId><ClrSysMmbId><MmbId>113</MmbId></ClrSysMmbId></FinInstnId><BrnchId><Id>0001</Id></BrnchId></CdtrAgt><Cdtr><Nm>Vilmar Quiroga Silveira</Nm></Cdtr><CdtrAcct><Id><Othr><Id>9477951</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></CdtrAcct></OrgnlTxRef></TxInfAndSts></OrgnlPmtInfAndSts><OrgnlPmtInfAndSts><OrgnlPmtInfId>mlu:withdraw:citibank:1652412300000</OrgnlPmtInfId><TxInfAndSts><OrgnlEndToEndId>22315186494</OrgnlEndToEndId><TxSts>ACSP</TxSts><StsRsnInf><Orgtr><Nm>CitiDirect</Nm></Orgtr><Rsn><Cd>NARR</Cd></Rsn><AddtlInf>/00000000/CB Processed</AddtlInf></StsRsnInf><AcctSvcrRef>04986885000000003</AcctSvcrRef><OrgnlTxRef><Amt><InstdAmt Ccy=\"UYU\">6899.9</InstdAmt></Amt><ReqdExctnDt>2022-05-13</ReqdExctnDt><PmtTpInf><SvcLvl><Cd>NURG</Cd></SvcLvl><LclInstrm><Prtry>441</Prtry></LclInstrm></PmtTpInf><PmtMtd>TRF</PmtMtd><Dbtr><Nm>MERCADOPAGO S.A</Nm><PstlAdr><Ctry>UY</Ctry></PstlAdr></Dbtr><DbtrAcct><Id><Othr><Id>0061140026</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></DbtrAcct><DbtrAgt><FinInstnId><BIC>CITIUYMMXXX</BIC><ClrSysMmbId><MmbId>Uruguay</MmbId></ClrSysMmbId><PstlAdr><Ctry>UY</Ctry></PstlAdr></FinInstnId><BrnchId><Id>858</Id></BrnchId></DbtrAgt><CdtrAgt><FinInstnId><ClrSysMmbId><MmbId>113</MmbId></ClrSysMmbId></FinInstnId><BrnchId><Id>0001</Id></BrnchId></CdtrAgt><Cdtr><Nm>Vilmar Quiroga Silveira</Nm></Cdtr><CdtrAcct><Id><Othr><Id>9477951</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></CdtrAcct></OrgnlTxRef></TxInfAndSts></OrgnlPmtInfAndSts><OrgnlPmtInfAndSts><OrgnlPmtInfId>mlu:withdraw:citibank:1652412300000</OrgnlPmtInfId><TxInfAndSts><OrgnlEndToEndId>22315186494</OrgnlEndToEndId><TxSts>ACSP</TxSts><StsRsnInf><Orgtr><Nm>CitiDirect</Nm></Orgtr><Rsn><Cd>NARR</Cd></Rsn><AddtlInf>/00000000/CB Processed</AddtlInf></StsRsnInf><AcctSvcrRef>04986885000000003</AcctSvcrRef><OrgnlTxRef><Amt><InstdAmt Ccy=\"UYU\">900900.9</InstdAmt></Amt><ReqdExctnDt>2022-05-13</ReqdExctnDt><PmtTpInf><SvcLvl><Cd>NURG</Cd></SvcLvl><LclInstrm><Prtry>441</Prtry></LclInstrm></PmtTpInf><PmtMtd>TRF</PmtMtd><Dbtr><Nm>MERCADOPAGO S.A</Nm><PstlAdr><Ctry>UY</Ctry></PstlAdr></Dbtr><DbtrAcct><Id><Othr><Id>0061140026</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></DbtrAcct><DbtrAgt><FinInstnId><BIC>CITIUYMMXXX</BIC><ClrSysMmbId><MmbId>Uruguay</MmbId></ClrSysMmbId><PstlAdr><Ctry>UY</Ctry></PstlAdr></FinInstnId><BrnchId><Id>858</Id></BrnchId></DbtrAgt><CdtrAgt><FinInstnId><ClrSysMmbId><MmbId>113</MmbId></ClrSysMmbId></FinInstnId><BrnchId><Id>0001</Id></BrnchId></CdtrAgt><Cdtr><Nm>Vilmar Quiroga Silveira</Nm></Cdtr><CdtrAcct><Id><Othr><Id>9477951</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></CdtrAcct></OrgnlTxRef></TxInfAndSts></OrgnlPmtInfAndSts></CstmrPmtStsRpt></Document>\n";
        return sinBTM;
    }
}
