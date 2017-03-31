/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.log4j.Logger;

public class FontTest {

    public static final String DEST = "/tmp/LiberationSans.pdf";
    public static final String FONT = "E:\\work\\vizboard\\dashboard\\src\\main\\webapp\\static\\lib\\fonts\\proxima\\proximanova-reg-webfont.woff";
final static Logger log = Logger.getLogger(FontTest.class);
    public static void main(String[] args) throws IOException, DocumentException {
        log.debug("Calling main function in FontTest class");
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FontTest().createPdf(DEST);
    }

    public void createPdf(String dest) throws IOException, DocumentException {
                log.debug("Calling createpdf function in FontTest class");
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        FontFactory.register(FONT, "proxima_nova_rgregular");
        Font f = FontFactory.getFont("proxima_nova_rgregular", "Cp1253", true);
        Paragraph p = new Paragraph("test", f);
        document.add(p);
        document.close();
    }
}
