package top.dadagum.extractor.service;

import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.common.Term;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;
import top.dadagum.extractor.utils.FileUtil;
import top.dadagum.extractor.utils.NlpUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description TODO
 * @Author Honda
 * @Date 2019/7/7 11:27
 **/
public class FileExtractService {

    /**
     * 项目选择保留的词性
     */
    private static Set<String> keep = new HashSet<>();

    private static Set<String> garbage = new HashSet<>();

    static {
        // 保留
      //  natures.add(Nature.nr.toString()); // 人名
        keep.add(Nature.nrf.toString());
        keep.add(Nature.ns.toString()); // 地名
        keep.add(Nature.nsf.toString());
        keep.add(Nature.nt.toString()); // 机构
        keep.add(Nature.ntc.toString()); // 公司名
        keep.add(Nature.ntcf.toString()); // 工厂名
        keep.add(Nature.nto.toString()); // 政府机构
        keep.add(Nature.ntu.toString()); // 大学
        keep.add(Nature.nts.toString()); // 中小学
        keep.add(Nature.nth.toString()); // 中小学
        keep.add(Nature.nis.toString()); // 机构后缀
       // natures.add(Nature.nz.toString()); // 其它专业名词

        // 丢弃
        garbage.add(Nature.v.toString());
    }

    /**
     * 将一个文件（pdf, doc, ...）找出其中的关键字等便于进行内容的检索
     * @param path 文件存储位置
     */
    public void createFileIndex(String path) throws IOException, OpenXML4JException, XmlException {
        List<Term> res = null;
        // 转化为文本信息
        String str = FileUtil.extractString(path);
        if (str != null) {
            // 提取关键字
            List<String> keyword = NlpUtil.findKeyword(str);
            System.out.println(keyword);
            // 识别关键字并去掉一些词汇
            res = NlpUtil.NER(keyword);
            res = NlpUtil.removeNatures(res, garbage);
            System.out.println(res);
            // NER
            List<Term> ner = NlpUtil.NER(str);
            // 选取一些特定的词（人名 / 地点 / 机构 ...）
            ner = NlpUtil.keepNatures(ner, keep);
            // 融合
            res.addAll(ner);
            System.out.println(res);
        }
    }
}
