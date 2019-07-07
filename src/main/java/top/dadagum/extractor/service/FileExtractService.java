package top.dadagum.extractor.service;

import com.hankcs.hanlp.corpus.tag.Nature;

import java.util.HashSet;
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
    private static Set<String> natures = new HashSet<>();

    static {
        natures.add(Nature.nr.toString()); // 人名
        natures.add(Nature.nrf.toString());
        natures.add(Nature.ns.toString()); // 地名
        natures.add(Nature.nsf.toString());
        natures.add(Nature.nt.toString()); // 机构
        natures.add(Nature.ntc.toString()); // 公司名
        natures.add(Nature.ntcf.toString()); // 工厂名
        natures.add(Nature.ntu.toString()); // 大学
        natures.add(Nature.nis.toString()); // 机构后缀
        natures.add(Nature.nz.toString()); // 其它专业名词
    }

    /**
     * 将一个文件
     * @param path
     */
    public void createFileIndex(String path) {

    }
}
