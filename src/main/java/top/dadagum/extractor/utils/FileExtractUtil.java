package top.dadagum.extractor.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlException;
import top.dadagum.extractor.domain.FileSuffixType;

import java.io.*;
import java.nio.charset.Charset;
import java.text.NumberFormat;

/**
 * @Description 文件工具类
 * @Author Honda
 * @Date 2019/6/19 22:58
 **/
public class FileExtractUtil {

    public static String removeSpace(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != ' ' && str.charAt(i) != '\n' && str.charAt(i) != '\t' && str.charAt(i) != '\r') {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
     * 默认检测 txt 的字符集
     */
    private static String[] charsetsToBeTested = {"Unicode", "UTF-8", "UTF-16"};

    /**
     * 根据文件后缀名提取文本内容， 目前内容只支持 pdf, doc, docx, ppt, pptx, xls, xlsx 的提取
     * 如果是其它文件的类型，返回 String = null
     * @param path 文件路径
     * @return
     */
    public static String extractString(String path) throws IOException, OpenXML4JException, XmlException {
        String res = null;
        String suffix = getFileSuffix(path).toLowerCase();
        switch (suffix) {
            case "pdf":
                res = pdf2String(new File(path));
                break;
            case "doc":
                res = doc2String(new File(path));
                break;
            case "docx":
                res = docx2String(new File(path));
                break;
            case "ppt":
                res = ppt2String(new File(path));
                break;
            case "pptx":
                res = pptx2String(path);
                break;
            case "xls":
                res = xls2String(path);
                break;
            case "xlsx":
                res = xlsx2String(path);
                break;
            case "txt":
                res = txt2String(new File(path));
                break;
        }
        return res == null ? res : removeSpace(res);
    }

    /**
     * 得到文件后缀
     * @param fileName 文件名
     * @return
     */
    public static String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.')+1);
    }

    public static String pdf2String(File file) throws IOException {
        PDDocument document = null;
        String content = null;
        // 方式一：
        /**
         InputStream input = null;
         input = new FileInputStream( pdfFile );
         //加载 pdf 文档
         PDFParser parser = new PDFParser(new RandomAccessBuffer(input));
         parser.parse();
         document = parser.getPDDocument();
         **/
        // 方式二：
        document = PDDocument.load(file);
        // 获取页码
        int pages = document.getNumberOfPages();
        // 读文本内容
        PDFTextStripper stripper = new PDFTextStripper();
        // 设置按顺序输出
        stripper.setSortByPosition(true);
        stripper.setStartPage(1);
        stripper.setEndPage(pages);
        content = stripper.getText(document);
        return content;
    }

    public static String doc2String(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        WordExtractor extractor = new WordExtractor(fis);
        String str = extractor.getText();
        extractor.close();
        return str;
    }

    public static String docx2String(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        XWPFDocument docx = new XWPFDocument(fis);
        XWPFWordExtractor extractor = new XWPFWordExtractor(docx);
        String str = extractor.getText();
        extractor.close();
        return str;
    }

    public static String ppt2String(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        PowerPointExtractor extractor = new PowerPointExtractor(fis);
        String str = extractor.getText();
        extractor.close();
        return str;
    }

    public static String pptx2String(String path) throws IOException, OpenXML4JException, XmlException {
        return new XSLFPowerPointExtractor(POIXMLDocument.openPackage(path)).getText();
    }

    public static String xlsx2String(String path) throws IOException {
        StringBuilder content = new StringBuilder();
        XSSFWorkbook workbook = new XSSFWorkbook(path);
        for (int numSheets = 0; numSheets < workbook.getNumberOfSheets(); numSheets++) {
            if (null != workbook.getSheetAt(numSheets)) {
                XSSFSheet aSheet = workbook.getSheetAt(numSheets);// 获得一个sheet
                for (int rowNumOfSheet = 0; rowNumOfSheet <= aSheet
                        .getLastRowNum(); rowNumOfSheet++) {
                    if (null != aSheet.getRow(rowNumOfSheet)) {
                        XSSFRow aRow = aSheet.getRow(rowNumOfSheet); // 获得一个行
                        for (short cellNumOfRow = 0; cellNumOfRow <= aRow
                                .getLastCellNum(); cellNumOfRow++) {
                            if (null != aRow.getCell(cellNumOfRow)) {
                                XSSFCell aCell = aRow.getCell(cellNumOfRow);// 获得列值
                                if (convertCell(aCell).length() > 0) {
                                    content.append(convertCell(aCell));
                                }
                            }
                            content.append("\n");
                        }
                    }
                }
            }
        }
        return content.toString();
    }

    public static String xls2String(String path) throws IOException {
        StringBuilder content = new StringBuilder();
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(path));// 创建对Excel工作簿文件的引用
        for (int numSheets = 0; numSheets < workbook.getNumberOfSheets(); numSheets++) {
            if (null != workbook.getSheetAt(numSheets)) {
                HSSFSheet aSheet = workbook.getSheetAt(numSheets);// 获得一个sheet
                for (int rowNumOfSheet = 0; rowNumOfSheet <= aSheet
                        .getLastRowNum(); rowNumOfSheet++) {
                    if (null != aSheet.getRow(rowNumOfSheet)) {
                        HSSFRow aRow = aSheet.getRow(rowNumOfSheet); // 获得一个行
                        for (short cellNumOfRow = 0; cellNumOfRow <= aRow
                                .getLastCellNum(); cellNumOfRow++) {
                            if (null != aRow.getCell(cellNumOfRow)) {
                                HSSFCell aCell = aRow.getCell(cellNumOfRow);// 获得列值
                                if (convertCell(aCell).length() > 0) {
                                    content.append(convertCell(aCell));
                                }
                            }
                            content.append("\n");
                        }
                    }
                }
            }
        }
        return content.toString();
    }

    public static String txt2String(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        // 检查字符集
        Charset charset = CharsetDetector.detectCharset(file, charsetsToBeTested);
        if (charset != null) {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), charset);
            BufferedReader reader = new BufferedReader(isr);
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            reader.close();
        } else {
            throw new RuntimeException("Unrecognized charset.");
        }


        return sb.toString();
    }

    private static String convertCell(Cell cell) {
        NumberFormat formater = NumberFormat.getInstance();
        formater.setGroupingUsed(false);
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
                cellValue = formater.format(cell.getNumericCellValue());
                break;
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case BLANK:
                cellValue = cell.getStringCellValue();
                break;
            case BOOLEAN:
                cellValue = Boolean.valueOf(cell.getBooleanCellValue()).toString();
                break;
            case ERROR:
                cellValue = String.valueOf(cell.getErrorCellValue());
                break;
            default:
                cellValue = "";
        }
        return cellValue.trim();
    }
}
