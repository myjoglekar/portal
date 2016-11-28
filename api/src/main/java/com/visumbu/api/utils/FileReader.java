/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.utils;

import com.google.api.ads.adwords.axis.v201603.rm.Rule;
import com.visumbu.api.bing.report.xml.bean.KeywordPerformanceReport;
import java.io.File;
import java.io.FileInputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.transform.sax.SAXSource;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author user
 */
public class FileReader {

    public static class NamespaceFilter extends XMLFilterImpl {

        private String usedNamespaceUri;
        private boolean addNamespace;

        //State variable
        private boolean addedNamespace = false;

        public NamespaceFilter(String namespaceUri,
                boolean addNamespace) {
            super();

            if (addNamespace) {
                this.usedNamespaceUri = namespaceUri;
            } else {
                this.usedNamespaceUri = "";
            }
            this.addNamespace = addNamespace;
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            if (addNamespace) {
                startControlledPrefixMapping();
            }
        }

        @Override
        public void startElement(String arg0, String arg1, String arg2,
                Attributes arg3) throws SAXException {

            super.startElement(this.usedNamespaceUri, arg1, arg2, arg3);
        }

        @Override
        public void endElement(String arg0, String arg1, String arg2)
                throws SAXException {

            super.endElement(this.usedNamespaceUri, arg1, arg2);
        }

        @Override
        public void startPrefixMapping(String prefix, String url)
                throws SAXException {

            if (addNamespace) {
                this.startControlledPrefixMapping();
            } else {
                //Remove the namespace, i.e. don´t call startPrefixMapping for parent!
            }

        }

        private void startControlledPrefixMapping() throws SAXException {

            if (this.addNamespace && !this.addedNamespace) {
                //We should add namespace since it is set and has not yet been done.
                super.startPrefixMapping("", this.usedNamespaceUri);

                //Make sure we dont do it twice
                this.addedNamespace = true;
            }
        }

    }

    public static Object readXML(File file, Class inputClass) {
        try {
            JAXBContext context = JAXBContext.newInstance(inputClass);
            Unmarshaller um = context.createUnmarshaller();

            XMLReader reader = XMLReaderFactory.createXMLReader();
            NamespaceFilter inFilter = new NamespaceFilter("test", false);
            inFilter.setParent(reader);
            InputSource is = new InputSource(new FileInputStream(file));
            SAXSource source = new SAXSource(inFilter, is);
            Object obj = (Object) um.unmarshal(source);
            return obj;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Object readXML(String fileName, Class inputClass) {
        try {
            JAXBContext context = JAXBContext.newInstance(inputClass);
            Unmarshaller um = context.createUnmarshaller();

            XMLReader reader = XMLReaderFactory.createXMLReader();
            NamespaceFilter inFilter = new NamespaceFilter("test", false);
            inFilter.setParent(reader);
            InputSource is = new InputSource(new FileInputStream(fileName));
            SAXSource source = new SAXSource(inFilter, is);
            Object obj = (Object) um.unmarshal(source);
            return obj;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void main(String[] argv) {
        String fileName = "F:\\test\\BJERB5ITE6B0TLPYB4A3ONUGVIWXRPGX.xml";
        KeywordPerformanceReport report = (KeywordPerformanceReport) FileReader.readXML(fileName, KeywordPerformanceReport.class);
        System.out.println(report);
    }
}
