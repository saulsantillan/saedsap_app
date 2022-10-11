package com.mercadolibre.saedsap_demo_app.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserXMLTest {


    @Test
    void parserTransaccionesREJECT() {
        ParserXML parserXML= new ParserXML();

        String reject="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<Document xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:iso:std:iso:20022:tech:xsd:pain.002.001.03\"><CstmrPmtStsRpt><GrpHdr><MsgId>CITIBANK/20220513-PSR/6448280</MsgId><CreDtTm>2022-05-13T11:10:59</CreDtTm><InitgPty><Id><OrgId><BICOrBEI>CITIUS33</BICOrBEI></OrgId></Id></InitgPty></GrpHdr><OrgnlGrpInfAndSts><OrgnlMsgId>mlu:withdraw:citibank:1652410200000</OrgnlMsgId><OrgnlMsgNmId>pain.001.001.03</OrgnlMsgNmId><OrgnlCreDtTm>2022-05-13T00:29:55</OrgnlCreDtTm><OrgnlNbOfTxs>33</OrgnlNbOfTxs><CtrlSum>234044.51</CtrlSum><NbOfTxsPerSts><DtldNbOfTxs>1</DtldNbOfTxs><DtldSts>RJCT</DtldSts></NbOfTxsPerSts></OrgnlGrpInfAndSts><OrgnlPmtInfAndSts><OrgnlPmtInfId>mlu:withdraw:citibank:1652410200000</OrgnlPmtInfId><TxInfAndSts><OrgnlEndToEndId>BTM22314631276</OrgnlEndToEndId><TxSts>RJCT</TxSts><StsRsnInf><Orgtr><Nm>CitiDirect</Nm></Orgtr><Rsn><Cd>NARR</Cd></Rsn><AddtlInf>/99999999/CB Rejected</AddtlInf><AddtlInf>No account / Unable to Locate Accou</AddtlInf><AddtlInf>nt (R03 in US-ACH) (LA_83)</AddtlInf></StsRsnInf><AcctSvcrRef>04986885700000029</AcctSvcrRef><OrgnlTxRef><Amt><InstdAmt Ccy=\"UYU\">1490</InstdAmt></Amt><ReqdExctnDt>2022-05-13</ReqdExctnDt><PmtTpInf><SvcLvl><Cd>NURG</Cd></SvcLvl><LclInstrm><Prtry>441</Prtry></LclInstrm></PmtTpInf><PmtMtd>TRF</PmtMtd><Dbtr><Nm>MERCADOPAGO S.A</Nm><PstlAdr><Ctry>UY</Ctry></PstlAdr></Dbtr><DbtrAcct><Id><Othr><Id>0061140026</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></DbtrAcct><DbtrAgt><FinInstnId><BIC>CITIUYMMXXX</BIC><ClrSysMmbId><MmbId>Uruguay</MmbId></ClrSysMmbId><PstlAdr><Ctry>UY</Ctry></PstlAdr></FinInstnId><BrnchId><Id>858</Id></BrnchId></DbtrAgt><CdtrAgt><FinInstnId><ClrSysMmbId><MmbId>001</MmbId></ClrSysMmbId></FinInstnId><BrnchId><Id>0001</Id></BrnchId></CdtrAgt><Cdtr><Nm>Carmen Maclean</Nm></Cdtr><CdtrAcct><Id><Othr><Id>01044717525185</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></CdtrAcct></OrgnlTxRef></TxInfAndSts></OrgnlPmtInfAndSts><OrgnlPmtInfAndSts><OrgnlPmtInfId>mlu:withdraw:citibank:1652410200000</OrgnlPmtInfId><TxInfAndSts><OrgnlEndToEndId>22314631276</OrgnlEndToEndId><TxSts>RJCT</TxSts><StsRsnInf><Orgtr><Nm>CitiDirect</Nm></Orgtr><Rsn><Cd>NARR</Cd></Rsn><AddtlInf>/99999999/CB Rejected</AddtlInf><AddtlInf>No account / Unable to Locate Accou</AddtlInf><AddtlInf>nt (R03 in US-ACH) (LA_83)</AddtlInf></StsRsnInf><AcctSvcrRef>04986885700000029</AcctSvcrRef><OrgnlTxRef><Amt><InstdAmt Ccy=\"UYU\">1490</InstdAmt></Amt><ReqdExctnDt>2022-05-13</ReqdExctnDt><PmtTpInf><SvcLvl><Cd>NURG</Cd></SvcLvl><LclInstrm><Prtry>441</Prtry></LclInstrm></PmtTpInf><PmtMtd>TRF</PmtMtd><Dbtr><Nm>MERCADOPAGO S.A</Nm><PstlAdr><Ctry>UY</Ctry></PstlAdr></Dbtr><DbtrAcct><Id><Othr><Id>0061140026</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></DbtrAcct><DbtrAgt><FinInstnId><BIC>CITIUYMMXXX</BIC><ClrSysMmbId><MmbId>Uruguay</MmbId></ClrSysMmbId><PstlAdr><Ctry>UY</Ctry></PstlAdr></FinInstnId><BrnchId><Id>858</Id></BrnchId></DbtrAgt><CdtrAgt><FinInstnId><ClrSysMmbId><MmbId>001</MmbId></ClrSysMmbId></FinInstnId><BrnchId><Id>0001</Id></BrnchId></CdtrAgt><Cdtr><Nm>Carmen Maclean</Nm></Cdtr><CdtrAcct><Id><Othr><Id>01044717525185</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></CdtrAcct></OrgnlTxRef></TxInfAndSts></OrgnlPmtInfAndSts></CstmrPmtStsRpt></Document>";
        List<String> enc = parserXML.parserTransacciones(reject);
        assertEquals(1, enc.size());
    }

    @Test
    void parserTransaccionesACCEPT() {
        ParserXML parserXML= new ParserXML();

        String accept = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<Document xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:iso:std:iso:20022:tech:xsd:pain.002.001.03\"><CstmrPmtStsRpt><GrpHdr><MsgId>CITIBANK/20220513-PSR/6448806</MsgId><CreDtTm>2022-05-13T11:40:48</CreDtTm><InitgPty><Id><OrgId><BICOrBEI>CITIUS33</BICOrBEI></OrgId></Id></InitgPty></GrpHdr><OrgnlGrpInfAndSts><OrgnlMsgId>mlu:withdraw:citibank:1652412300000</OrgnlMsgId><OrgnlMsgNmId>pain.001.001.03</OrgnlMsgNmId><OrgnlCreDtTm>2022-05-13T00:29:42</OrgnlCreDtTm><OrgnlNbOfTxs>43</OrgnlNbOfTxs><CtrlSum>234044.51</CtrlSum><NbOfTxsPerSts><DtldNbOfTxs>18</DtldNbOfTxs><DtldSts>ACSP</DtldSts></NbOfTxsPerSts></OrgnlGrpInfAndSts><OrgnlPmtInfAndSts><OrgnlPmtInfId>mlu:withdraw:citibank:1652412300000</OrgnlPmtInfId><TxInfAndSts><OrgnlEndToEndId>22315186494</OrgnlEndToEndId><TxSts>ACSP</TxSts><StsRsnInf><Orgtr><Nm>CitiDirect</Nm></Orgtr><Rsn><Cd>NARR</Cd></Rsn><AddtlInf>/00000000/CB Processed</AddtlInf></StsRsnInf><AcctSvcrRef>04986885000000003</AcctSvcrRef><OrgnlTxRef><Amt><InstdAmt Ccy=\"UYU\">4116.9</InstdAmt></Amt><ReqdExctnDt>2022-05-13</ReqdExctnDt><PmtTpInf><SvcLvl><Cd>NURG</Cd></SvcLvl><LclInstrm><Prtry>441</Prtry></LclInstrm></PmtTpInf><PmtMtd>TRF</PmtMtd><Dbtr><Nm>MERCADOPAGO S.A</Nm><PstlAdr><Ctry>UY</Ctry></PstlAdr></Dbtr><DbtrAcct><Id><Othr><Id>0061140026</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></DbtrAcct><DbtrAgt><FinInstnId><BIC>CITIUYMMXXX</BIC><ClrSysMmbId><MmbId>Uruguay</MmbId></ClrSysMmbId><PstlAdr><Ctry>UY</Ctry></PstlAdr></FinInstnId><BrnchId><Id>858</Id></BrnchId></DbtrAgt><CdtrAgt><FinInstnId><ClrSysMmbId><MmbId>113</MmbId></ClrSysMmbId></FinInstnId><BrnchId><Id>0001</Id></BrnchId></CdtrAgt><Cdtr><Nm>Vilmar Quiroga Silveira</Nm></Cdtr><CdtrAcct><Id><Othr><Id>9477951</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></CdtrAcct></OrgnlTxRef></TxInfAndSts></OrgnlPmtInfAndSts><OrgnlPmtInfAndSts><OrgnlPmtInfId>mlu:withdraw:citibank:1652412300000</OrgnlPmtInfId><TxInfAndSts><OrgnlEndToEndId>BTM22315186494</OrgnlEndToEndId><TxSts>ACSP</TxSts><StsRsnInf><Orgtr><Nm>CitiDirect</Nm></Orgtr><Rsn><Cd>NARR</Cd></Rsn><AddtlInf>/00000000/CB Processed</AddtlInf></StsRsnInf><AcctSvcrRef>04986885000000003</AcctSvcrRef><OrgnlTxRef><Amt><InstdAmt Ccy=\"UYU\">5089.9</InstdAmt></Amt><ReqdExctnDt>2022-05-13</ReqdExctnDt><PmtTpInf><SvcLvl><Cd>NURG</Cd></SvcLvl><LclInstrm><Prtry>441</Prtry></LclInstrm></PmtTpInf><PmtMtd>TRF</PmtMtd><Dbtr><Nm>MERCADOPAGO S.A</Nm><PstlAdr><Ctry>UY</Ctry></PstlAdr></Dbtr><DbtrAcct><Id><Othr><Id>0061140026</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></DbtrAcct><DbtrAgt><FinInstnId><BIC>CITIUYMMXXX</BIC><ClrSysMmbId><MmbId>Uruguay</MmbId></ClrSysMmbId><PstlAdr><Ctry>UY</Ctry></PstlAdr></FinInstnId><BrnchId><Id>858</Id></BrnchId></DbtrAgt><CdtrAgt><FinInstnId><ClrSysMmbId><MmbId>113</MmbId></ClrSysMmbId></FinInstnId><BrnchId><Id>0001</Id></BrnchId></CdtrAgt><Cdtr><Nm>Vilmar Quiroga Silveira</Nm></Cdtr><CdtrAcct><Id><Othr><Id>9477951</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></CdtrAcct></OrgnlTxRef></TxInfAndSts></OrgnlPmtInfAndSts><OrgnlPmtInfAndSts><OrgnlPmtInfId>mlu:withdraw:citibank:1652412300000</OrgnlPmtInfId><TxInfAndSts><OrgnlEndToEndId>BTM22315186494</OrgnlEndToEndId><TxSts>ACSP</TxSts><StsRsnInf><Orgtr><Nm>CitiDirect</Nm></Orgtr><Rsn><Cd>NARR</Cd></Rsn><AddtlInf>/00000000/CB Processed</AddtlInf></StsRsnInf><AcctSvcrRef>04986885000000003</AcctSvcrRef><OrgnlTxRef><Amt><InstdAmt Ccy=\"UYU\">6899.9</InstdAmt></Amt><ReqdExctnDt>2022-05-13</ReqdExctnDt><PmtTpInf><SvcLvl><Cd>NURG</Cd></SvcLvl><LclInstrm><Prtry>441</Prtry></LclInstrm></PmtTpInf><PmtMtd>TRF</PmtMtd><Dbtr><Nm>MERCADOPAGO S.A</Nm><PstlAdr><Ctry>UY</Ctry></PstlAdr></Dbtr><DbtrAcct><Id><Othr><Id>0061140026</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></DbtrAcct><DbtrAgt><FinInstnId><BIC>CITIUYMMXXX</BIC><ClrSysMmbId><MmbId>Uruguay</MmbId></ClrSysMmbId><PstlAdr><Ctry>UY</Ctry></PstlAdr></FinInstnId><BrnchId><Id>858</Id></BrnchId></DbtrAgt><CdtrAgt><FinInstnId><ClrSysMmbId><MmbId>113</MmbId></ClrSysMmbId></FinInstnId><BrnchId><Id>0001</Id></BrnchId></CdtrAgt><Cdtr><Nm>Vilmar Quiroga Silveira</Nm></Cdtr><CdtrAcct><Id><Othr><Id>9477951</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></CdtrAcct></OrgnlTxRef></TxInfAndSts></OrgnlPmtInfAndSts><OrgnlPmtInfAndSts><OrgnlPmtInfId>mlu:withdraw:citibank:1652412300000</OrgnlPmtInfId><TxInfAndSts><OrgnlEndToEndId>BTM22315186494</OrgnlEndToEndId><TxSts>ACSP</TxSts><StsRsnInf><Orgtr><Nm>CitiDirect</Nm></Orgtr><Rsn><Cd>NARR</Cd></Rsn><AddtlInf>/00000000/CB Processed</AddtlInf></StsRsnInf><AcctSvcrRef>04986885000000003</AcctSvcrRef><OrgnlTxRef><Amt><InstdAmt Ccy=\"UYU\">900900.9</InstdAmt></Amt><ReqdExctnDt>2022-05-13</ReqdExctnDt><PmtTpInf><SvcLvl><Cd>NURG</Cd></SvcLvl><LclInstrm><Prtry>441</Prtry></LclInstrm></PmtTpInf><PmtMtd>TRF</PmtMtd><Dbtr><Nm>MERCADOPAGO S.A</Nm><PstlAdr><Ctry>UY</Ctry></PstlAdr></Dbtr><DbtrAcct><Id><Othr><Id>0061140026</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></DbtrAcct><DbtrAgt><FinInstnId><BIC>CITIUYMMXXX</BIC><ClrSysMmbId><MmbId>Uruguay</MmbId></ClrSysMmbId><PstlAdr><Ctry>UY</Ctry></PstlAdr></FinInstnId><BrnchId><Id>858</Id></BrnchId></DbtrAgt><CdtrAgt><FinInstnId><ClrSysMmbId><MmbId>113</MmbId></ClrSysMmbId></FinInstnId><BrnchId><Id>0001</Id></BrnchId></CdtrAgt><Cdtr><Nm>Vilmar Quiroga Silveira</Nm></Cdtr><CdtrAcct><Id><Othr><Id>9477951</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></CdtrAcct></OrgnlTxRef></TxInfAndSts></OrgnlPmtInfAndSts></CstmrPmtStsRpt></Document>\n";
        List<String> enc = parserXML.parserTransacciones(accept);
        assertEquals(3, enc.size());
    }
    @Test
    void parserTransaccionessinBTM() {
        ParserXML parserXML= new ParserXML();
        String sinBTM="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<Document xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:iso:std:iso:20022:tech:xsd:pain.002.001.03\"><CstmrPmtStsRpt><GrpHdr><MsgId>CITIBANK/20220513-PSR/6448806</MsgId><CreDtTm>2022-05-13T11:40:48</CreDtTm><InitgPty><Id><OrgId><BICOrBEI>CITIUS33</BICOrBEI></OrgId></Id></InitgPty></GrpHdr><OrgnlGrpInfAndSts><OrgnlMsgId>mlu:withdraw:citibank:1652412300000</OrgnlMsgId><OrgnlMsgNmId>pain.001.001.03</OrgnlMsgNmId><OrgnlCreDtTm>2022-05-13T00:29:42</OrgnlCreDtTm><OrgnlNbOfTxs>43</OrgnlNbOfTxs><CtrlSum>234044.51</CtrlSum><NbOfTxsPerSts><DtldNbOfTxs>18</DtldNbOfTxs><DtldSts>ACSP</DtldSts></NbOfTxsPerSts></OrgnlGrpInfAndSts><OrgnlPmtInfAndSts><OrgnlPmtInfId>mlu:withdraw:citibank:1652412300000</OrgnlPmtInfId><TxInfAndSts><OrgnlEndToEndId>22315186494</OrgnlEndToEndId><TxSts>ACSP</TxSts><StsRsnInf><Orgtr><Nm>CitiDirect</Nm></Orgtr><Rsn><Cd>NARR</Cd></Rsn><AddtlInf>/00000000/CB Processed</AddtlInf></StsRsnInf><AcctSvcrRef>04986885000000003</AcctSvcrRef><OrgnlTxRef><Amt><InstdAmt Ccy=\"UYU\">4116.9</InstdAmt></Amt><ReqdExctnDt>2022-05-13</ReqdExctnDt><PmtTpInf><SvcLvl><Cd>NURG</Cd></SvcLvl><LclInstrm><Prtry>441</Prtry></LclInstrm></PmtTpInf><PmtMtd>TRF</PmtMtd><Dbtr><Nm>MERCADOPAGO S.A</Nm><PstlAdr><Ctry>UY</Ctry></PstlAdr></Dbtr><DbtrAcct><Id><Othr><Id>0061140026</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></DbtrAcct><DbtrAgt><FinInstnId><BIC>CITIUYMMXXX</BIC><ClrSysMmbId><MmbId>Uruguay</MmbId></ClrSysMmbId><PstlAdr><Ctry>UY</Ctry></PstlAdr></FinInstnId><BrnchId><Id>858</Id></BrnchId></DbtrAgt><CdtrAgt><FinInstnId><ClrSysMmbId><MmbId>113</MmbId></ClrSysMmbId></FinInstnId><BrnchId><Id>0001</Id></BrnchId></CdtrAgt><Cdtr><Nm>Vilmar Quiroga Silveira</Nm></Cdtr><CdtrAcct><Id><Othr><Id>9477951</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></CdtrAcct></OrgnlTxRef></TxInfAndSts></OrgnlPmtInfAndSts><OrgnlPmtInfAndSts><OrgnlPmtInfId>mlu:withdraw:citibank:1652412300000</OrgnlPmtInfId><TxInfAndSts><OrgnlEndToEndId>22315186494</OrgnlEndToEndId><TxSts>ACSP</TxSts><StsRsnInf><Orgtr><Nm>CitiDirect</Nm></Orgtr><Rsn><Cd>NARR</Cd></Rsn><AddtlInf>/00000000/CB Processed</AddtlInf></StsRsnInf><AcctSvcrRef>04986885000000003</AcctSvcrRef><OrgnlTxRef><Amt><InstdAmt Ccy=\"UYU\">5089.9</InstdAmt></Amt><ReqdExctnDt>2022-05-13</ReqdExctnDt><PmtTpInf><SvcLvl><Cd>NURG</Cd></SvcLvl><LclInstrm><Prtry>441</Prtry></LclInstrm></PmtTpInf><PmtMtd>TRF</PmtMtd><Dbtr><Nm>MERCADOPAGO S.A</Nm><PstlAdr><Ctry>UY</Ctry></PstlAdr></Dbtr><DbtrAcct><Id><Othr><Id>0061140026</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></DbtrAcct><DbtrAgt><FinInstnId><BIC>CITIUYMMXXX</BIC><ClrSysMmbId><MmbId>Uruguay</MmbId></ClrSysMmbId><PstlAdr><Ctry>UY</Ctry></PstlAdr></FinInstnId><BrnchId><Id>858</Id></BrnchId></DbtrAgt><CdtrAgt><FinInstnId><ClrSysMmbId><MmbId>113</MmbId></ClrSysMmbId></FinInstnId><BrnchId><Id>0001</Id></BrnchId></CdtrAgt><Cdtr><Nm>Vilmar Quiroga Silveira</Nm></Cdtr><CdtrAcct><Id><Othr><Id>9477951</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></CdtrAcct></OrgnlTxRef></TxInfAndSts></OrgnlPmtInfAndSts><OrgnlPmtInfAndSts><OrgnlPmtInfId>mlu:withdraw:citibank:1652412300000</OrgnlPmtInfId><TxInfAndSts><OrgnlEndToEndId>22315186494</OrgnlEndToEndId><TxSts>ACSP</TxSts><StsRsnInf><Orgtr><Nm>CitiDirect</Nm></Orgtr><Rsn><Cd>NARR</Cd></Rsn><AddtlInf>/00000000/CB Processed</AddtlInf></StsRsnInf><AcctSvcrRef>04986885000000003</AcctSvcrRef><OrgnlTxRef><Amt><InstdAmt Ccy=\"UYU\">6899.9</InstdAmt></Amt><ReqdExctnDt>2022-05-13</ReqdExctnDt><PmtTpInf><SvcLvl><Cd>NURG</Cd></SvcLvl><LclInstrm><Prtry>441</Prtry></LclInstrm></PmtTpInf><PmtMtd>TRF</PmtMtd><Dbtr><Nm>MERCADOPAGO S.A</Nm><PstlAdr><Ctry>UY</Ctry></PstlAdr></Dbtr><DbtrAcct><Id><Othr><Id>0061140026</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></DbtrAcct><DbtrAgt><FinInstnId><BIC>CITIUYMMXXX</BIC><ClrSysMmbId><MmbId>Uruguay</MmbId></ClrSysMmbId><PstlAdr><Ctry>UY</Ctry></PstlAdr></FinInstnId><BrnchId><Id>858</Id></BrnchId></DbtrAgt><CdtrAgt><FinInstnId><ClrSysMmbId><MmbId>113</MmbId></ClrSysMmbId></FinInstnId><BrnchId><Id>0001</Id></BrnchId></CdtrAgt><Cdtr><Nm>Vilmar Quiroga Silveira</Nm></Cdtr><CdtrAcct><Id><Othr><Id>9477951</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></CdtrAcct></OrgnlTxRef></TxInfAndSts></OrgnlPmtInfAndSts><OrgnlPmtInfAndSts><OrgnlPmtInfId>mlu:withdraw:citibank:1652412300000</OrgnlPmtInfId><TxInfAndSts><OrgnlEndToEndId>22315186494</OrgnlEndToEndId><TxSts>ACSP</TxSts><StsRsnInf><Orgtr><Nm>CitiDirect</Nm></Orgtr><Rsn><Cd>NARR</Cd></Rsn><AddtlInf>/00000000/CB Processed</AddtlInf></StsRsnInf><AcctSvcrRef>04986885000000003</AcctSvcrRef><OrgnlTxRef><Amt><InstdAmt Ccy=\"UYU\">900900.9</InstdAmt></Amt><ReqdExctnDt>2022-05-13</ReqdExctnDt><PmtTpInf><SvcLvl><Cd>NURG</Cd></SvcLvl><LclInstrm><Prtry>441</Prtry></LclInstrm></PmtTpInf><PmtMtd>TRF</PmtMtd><Dbtr><Nm>MERCADOPAGO S.A</Nm><PstlAdr><Ctry>UY</Ctry></PstlAdr></Dbtr><DbtrAcct><Id><Othr><Id>0061140026</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></DbtrAcct><DbtrAgt><FinInstnId><BIC>CITIUYMMXXX</BIC><ClrSysMmbId><MmbId>Uruguay</MmbId></ClrSysMmbId><PstlAdr><Ctry>UY</Ctry></PstlAdr></FinInstnId><BrnchId><Id>858</Id></BrnchId></DbtrAgt><CdtrAgt><FinInstnId><ClrSysMmbId><MmbId>113</MmbId></ClrSysMmbId></FinInstnId><BrnchId><Id>0001</Id></BrnchId></CdtrAgt><Cdtr><Nm>Vilmar Quiroga Silveira</Nm></Cdtr><CdtrAcct><Id><Othr><Id>9477951</Id></Othr></Id><Tp><Prtry>CACC</Prtry></Tp></CdtrAcct></OrgnlTxRef></TxInfAndSts></OrgnlPmtInfAndSts></CstmrPmtStsRpt></Document>\n";
        List<String> enc = parserXML.parserTransacciones(sinBTM);
        assertEquals(0, enc.size());
    }
  }