package top.dadagum.extractor;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;

import java.io.File;
import java.io.IOException;

/**
 * @Description 测试
 * @Author Honda
 * @Date 2019/6/19 22:21
 **/
public class AppRunner {

    public static void main(String[] args) throws IOException, OpenXML4JException, XmlException {
        //doc();
        //docx();
        //pdf();
        //pptx();
        //xls();
        txtUnicode();
    }

    public static void doc() throws IOException {
        String docPath = "D:\\testing\\extract.doc";
        String str = FileExtractor.doc2String(new File(docPath));
        System.out.println(str);
    }

    public static void pdf() throws IOException {
        String path = "D:\\testing\\梁宏达-实训点1-库表设计与高效存储v1.1.pdf";
        String str = FileExtractor.pdf2String(new File(path));
        System.out.println(str);
    }

    public static void docx() throws IOException {
        String docPath = "D:\\testing\\extract.docx";
        String str = FileExtractor.docx2String(new File(docPath));
        System.out.println(str);
    }

    public static void ppt() throws IOException {
        String docPath = "D:\\testing\\2018-3-4梁宏达进度汇报.ppt";
        String str = FileExtractor.ppt2String(new File(docPath));
        System.out.println(str);
    }

    public static void pptx() throws OpenXML4JException, XmlException, IOException {
        String docPath = "D:\\testing\\2018-3-4梁宏达进度汇报.pptx";
        String str = FileExtractor.pptx2String(docPath);
        System.out.println(str);
    }

    public static void xlsx() throws IOException {
        String path = "D:\\testing\\data.xlsx";
        String str = FileExtractor.xlsx2String(path);
        System.out.println(str);
    }

    public static void xls() throws IOException {
        String path = "D:\\testing\\data.xls";
        String str = FileExtractor.xls2String(path);
        System.out.println(str);
    }

    public static void txtUnicode() throws IOException {
        String path = "D:\\testing\\data_unicode.txt";
        String str = FileExtractor.txt2String(new File(path));
        System.out.println(str);
    }

//    public static void txtUtf8() throws IOException {
//        String path = "D:\\testing\\data_utf8.txt";
//        String str = FileExtractor.txt2String(new File(path));
//        System.out.println(str);
//    }
}
