package top.dadagum.extractor;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;
import top.dadagum.extractor.utils.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * @Description TODO
 * @Author Honda
 * @Date 2019/7/6 16:25
 **/
public class FileTest {

    public static void doc() throws IOException {
        String docPath = "D:\\testing\\extract.doc";
        String str = FileUtil.doc2String(new File(docPath));
        System.out.println(str);
    }

    public static void pdf() throws IOException {
        String path = "D:\\testing\\梁宏达-实训点1-库表设计与高效存储v1.1.pdf";
        String str = FileUtil.pdf2String(new File(path));
        System.out.println(str);
    }

    public static void docx() throws IOException {
        String docPath = "D:\\testing\\extract.docx";
        String str = FileUtil.docx2String(new File(docPath));
        System.out.println(str);
    }

    public static void ppt() throws IOException {
        String docPath = "D:\\testing\\2018-3-4梁宏达进度汇报.ppt";
        String str = FileUtil.ppt2String(new File(docPath));
        System.out.println(str);
    }

    public static void pptx() throws OpenXML4JException, XmlException, IOException {
        String docPath = "D:\\testing\\2018-3-4梁宏达进度汇报.pptx";
        String str = FileUtil.pptx2String(docPath);
        System.out.println(str);
    }

    public static void xlsx() throws IOException {
        String path = "D:\\testing\\data.xlsx";
        String str = FileUtil.xlsx2String(path);
        System.out.println(str);
    }

    public static void xls() throws IOException {
        String path = "D:\\testing\\data.xls";
        String str = FileUtil.xls2String(path);
        System.out.println(str);
    }

    public static void txtUnicode() throws IOException {
        String path = "D:\\testing\\data_unicode.txt";
        String str = FileUtil.txt2String(new File(path));
        System.out.println(str);
    }

//    public static void txtUtf8() throws IOException {
//        String path = "D:\\testing\\data_utf8.txt";
//        String str = FileUtil.txt2String(new File(path));
//        System.out.println(str);
//    }
}
