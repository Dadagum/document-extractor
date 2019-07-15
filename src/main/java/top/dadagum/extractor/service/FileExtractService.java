package top.dadagum.extractor.service;

import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.common.Term;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;
import top.dadagum.extractor.utils.FileExtractUtil;
import top.dadagum.extractor.utils.NlpUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @Author Honda
 * @Date 2019/7/7 11:27
 **/
public class FileExtractService {

    /**
     * 项目选择保留的词性
     */
    public static Set<String> keep = new HashSet<>();

    public static Set<String> garbage = new HashSet<>();

    static {
        // 保留
      //  keep.add(Nature.nr.toString()); // 人名
        keep.add(Nature.n.toString());
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
        garbage.add(Nature.n.toString());
        garbage.add(Nature.f.toString());
    }

    /**
     * 提取关键字，只是简单取词频前 k
     * @param text 文本信息
     * @param k 取词数量
     * @return 关键词
     * @throws IOException
     * @throws OpenXML4JException
     * @throws XmlException
     */
    public List<String> findKeyword(String text, int k) throws IOException, OpenXML4JException, XmlException {
        Map<String, Integer> map = new HashMap<>();
        List<Term> ner = NlpUtil.NER(text);
        for (Term term : ner) {
            if (FileExtractService.keep.contains(term.nature.toString())) {
                Integer value = Optional.ofNullable(map.get(term.word))
                        .map(x -> x+1).orElse(1);
                map.put(term.word, value);
            }
        }
        // 找出出现频率最大的 10 个词汇
        //  System.out.println(map);
        List<String> collect = map.entrySet().stream().sorted((x1, x2) -> x2.getValue() - x1.getValue()).limit(k).map(e -> e.getKey()).collect(Collectors.toList());
        return collect;
    }


}
