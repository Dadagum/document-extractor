package top.dadagum.extractor;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;
import org.junit.Test;
import top.dadagum.extractor.utils.FileExtractUtil;

import java.io.File;
import java.io.IOException;

/**
 * @Description TODO
 * @Author Honda
 * @Date 2019/7/6 16:25
 **/
public class FileTest {

    @Test
    public void doc() throws IOException, OpenXML4JException, XmlException {
        String docPath = "D:\\testing\\extract.doc";
        String str = FileExtractUtil.extractString(docPath);
        System.out.println(str);
    }

    @Test
    public void pdf() throws IOException, OpenXML4JException, XmlException {
        String path = "D:\\testing\\梁宏达-实训点1-库表设计与高效存储v1.1.pdf";
        String str = FileExtractUtil.extractString(path);
        System.out.println(str);
    }

    @Test
    public void docx() throws IOException, OpenXML4JException, XmlException {
        String docPath = "D:\\testing\\extract.docx";
        String str = FileExtractUtil.extractString(docPath);
        System.out.println(str);
    }

    @Test
    public void ppt() throws IOException, OpenXML4JException, XmlException {
        String docPath = "D:\\testing\\2018-3-4梁宏达进度汇报.ppt";
        String str = FileExtractUtil.extractString(docPath);
        System.out.println(str);
    }

    @Test
    public void pptx() throws OpenXML4JException, XmlException, IOException {
        String docPath = "D:\\testing\\2018-3-4梁宏达进度汇报.pptx";
        String str = FileExtractUtil.extractString(docPath);
        System.out.println(str);
    }

    @Test
    public void xlsx() throws IOException, OpenXML4JException, XmlException {
        String path = "D:\\testing\\data.xlsx";
        String str = FileExtractUtil.extractString(path);
        System.out.println(str);
    }

    @Test
    public void xls() throws IOException, OpenXML4JException, XmlException {
        String path = "D:\\testing\\data.xls";
        String str = FileExtractUtil.extractString(path);
        System.out.println(str);
    }

    public static void txtUnicode() throws IOException {
        String path = "D:\\testing\\data_unicode.txt";
        String str = FileExtractUtil.txt2String(new File(path));
        System.out.println(str);
    }







//    public static void txtUtf8() throws IOException {
//        String path = "D:\\testing\\data_utf8.txt";
//        String str = FileExtractUtil.txt2String(new File(path));
//        System.out.println(str);
//    }
}
