package top.dadagum.extractor;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.collection.AhoCorasick.AhoCorasickDoubleArrayTrie;
import com.hankcs.hanlp.corpus.document.sentence.Sentence;
import com.hankcs.hanlp.corpus.document.sentence.word.IWord;
import com.hankcs.hanlp.dictionary.CoreDictionary;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;
import com.hankcs.hanlp.seg.Dijkstra.DijkstraSegment;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.hankcs.hanlp.tokenizer.SpeedTokenizer;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;
import org.junit.Assert;
import org.junit.Test;
import top.dadagum.extractor.service.FileExtractService;
import top.dadagum.extractor.utils.FileUtil;
import top.dadagum.extractor.utils.NlpUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @Author Honda
 * @Date 2019/7/6 11:16
 **/
public class HanlpTest {

    @Test
    public void hanHello() {
        System.out.println(HanLP.segment("你好，欢迎使用HanLP汉语处理包！"));
    }

    @Test
    public void wordSegment() {
        List<Term> termList = StandardTokenizer.segment("我的希望是希望张晚霞的背影被晚霞映红");
        System.out.println(termList);
    }

    @Test
    public void nlpWordSegnment() {
        System.out.println(NLPTokenizer.segment("我新造一个词叫幻想乡你能识别并标注正确词性吗？"));
// 注意观察下面两个“希望”的词性、两个“晚霞”的词性
        System.out.println(NLPTokenizer.analyze("我的希望是希望张晚霞的背影被晚霞映红").translateLabels());
        System.out.println(NLPTokenizer.analyze("支援臺灣正體香港繁體：微软公司於1975年由比爾·蓋茲和保羅·艾倫創立。"));
    }

    @Test
    public void indexSegment() {
        List<Term> termList = IndexTokenizer.segment("主副食品");
        for (Term term : termList)
        {
            System.out.println(term + " [" + term.offset + ":" + (term.offset + term.word.length()) + "]");
        }
    }

    @Test
    public void nShortestPath() {
        Segment nShortSegment = new NShortSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
        Segment shortestSegment = new DijkstraSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
        String[] testCase = new String[]{
                "今天，刘志军案的关键人物,山西女商人丁书苗在市二中院出庭受审。",
                "刘喜杰石国祥会见吴亚琴先进事迹报告团成员",
        };
        for (String sentence : testCase)
        {
            System.out.println("N-最短分词：" + nShortSegment.seg(sentence) + "\n最短路分词：" + shortestSegment.seg(sentence));
        }
    }

    @Test
    public void CRF() throws IOException {
        CRFLexicalAnalyzer analyzer = new CRFLexicalAnalyzer();
        String[] tests = new String[]{
                "上海华安工业（集团）公司董事长谭旭光和秘书胡花蕊来到美国纽约现代艺术博物馆参观",
        };
        for (String sentence : tests)
        {
            Sentence analyze = analyzer.analyze(sentence);
            List<IWord> wordList = analyze.wordList;
            System.out.println(wordList.size());
            for (IWord w : wordList) {
                System.out.println(w);
            }
        }
    }

    @Test
    public void speed() {
        String text = "江西鄱阳湖干枯，中国最大淡水湖变成大草原";
        System.out.println(SpeedTokenizer.segment(text));
        long start = System.currentTimeMillis();
        int pressure = 1000000;
        for (int i = 0; i < pressure; ++i)
        {
            SpeedTokenizer.segment(text);
        }
        double costTime = (System.currentTimeMillis() - start) / (double)1000;
        System.out.printf("分词速度：%.2f字每秒", text.length() * pressure / costTime);
    }

    @Test
    public void custom() {
        // 动态增加
        CustomDictionary.add("攻城狮");
        // 强行插入
        CustomDictionary.insert("白富美", "nz 1024");
        // 删除词语（注释掉试试）
//        CustomDictionary.remove("攻城狮");
        System.out.println(CustomDictionary.add("单身狗", "nz 1024 n 1"));
        System.out.println(CustomDictionary.get("单身狗"));

        String text = "攻城狮逆袭单身狗，迎娶白富美，走上人生巅峰";  // 怎么可能噗哈哈！

        // AhoCorasickDoubleArrayTrie自动机扫描文本中出现的自定义词语
        final char[] charArray = text.toCharArray();
        CustomDictionary.parseText(charArray, new AhoCorasickDoubleArrayTrie.IHit<CoreDictionary.Attribute>()
        {
            public void hit(int begin, int end, CoreDictionary.Attribute value)
            {
                System.out.printf("[%d:%d]=%s %s\n", begin, end, new String(charArray, begin, end - begin), value);
            }
        });

        // 自定义词典在所有分词器中都有效
        System.out.println(HanLP.segment(text));
    }

    @Test
    public void chineseName() {
        String[] testCase = new String[]{
                "签约仪式前，秦光荣、李纪恒、仇和等一同会见了参加签约的企业家。",
                "王国强、高峰、汪洋、张朝阳光着头、韩寒、小四",
                "张浩和胡健康复员回家了",
                "王总和小丽结婚了",
                "编剧邵钧林和稽道青说",
                "这里有关天培的有关事迹",
                "龚学平等领导,邓颖超生前",
        };
        Segment segment = HanLP.newSegment().enableNameRecognize(true);
        for (String sentence : testCase)
        {
            List<Term> termList = segment.seg(sentence);
            System.out.println(termList);
        }
    }

    @Test
    public void transliteration() {
        String[] testCase = new String[]{
                "一桶冰水当头倒下，微软的比尔盖茨、Facebook的扎克伯格跟桑德博格、亚马逊的贝索斯、苹果的库克全都不惜湿身入镜，这些硅谷的科技人，飞蛾扑火似地牺牲演出，其实全为了慈善。",
                "世界上最长的姓名是简森·乔伊·亚历山大·比基·卡利斯勒·达夫·埃利奥特·福克斯·伊维鲁莫·马尔尼·梅尔斯·帕特森·汤普森·华莱士·普雷斯顿。",
        };
        Segment segment = HanLP.newSegment().enableTranslatedNameRecognize(true);
        for (String sentence : testCase)
        {
            List<Term> termList = segment.seg(sentence);
            System.out.println(termList);
        }
    }

    @Test
    public void location() {
        String[] testCase = new String[]{
                "武胜县新学乡政府大楼门前锣鼓喧天",
                "蓝翔给宁夏固原市彭阳县红河镇黑牛沟村捐赠了挖掘机",
        };
        Segment segment = HanLP.newSegment().enablePlaceRecognize(true);
        for (String sentence : testCase)
        {
            List<Term> termList = segment.seg(sentence);
            System.out.println(termList);
        }
    }

    @Test
    public void org() {
        String[] testCase = new String[]{
                "我在上海林原科技有限公司兼职工作，",
                "我经常在台川喜宴餐厅吃饭，",
                "偶尔去地中海影城看电影。",
                "评估一项系统"
        };
        Segment segment = HanLP.newSegment().enableOrganizationRecognize(true);
        for (String sentence : testCase)
        {
            List<Term> termList = segment.seg(sentence);
            System.out.println(termList);
        }
    }

    @Test
    public void keyWordExtract() {
        String content = "程序员(英文Programmer)是从事程序开发、维护的专业人员。一般将程序员分为程序设计人员和程序编码人员，但两者的界限并不非常清楚，特别是在中国。软件从业人员分为初级程序员、高级程序员、系统分析员和项目经理四大类。";
        List<String> keywordList = HanLP.extractKeyword(content, 5);
        System.out.println(keywordList);
    }

    @Test
    public void digest() {
        String document = "算法可大致分为基本算法、数据结构的算法、数论算法、计算几何的算法、图的算法、动态规划以及数值分析、加密算法、排序算法、检索算法、随机化算法、并行算法、厄米变形模型、随机森林算法。\n" +
                "算法可以宽泛的分为三类，\n" +
                "一，有限的确定性算法，这类算法在有限的一段时间内终止。他们可能要花很长时间来执行指定的任务，但仍将在一定的时间内终止。这类算法得出的结果常取决于输入值。\n" +
                "二，有限的非确定算法，这类算法在有限的时间内终止。然而，对于一个（或一些）给定的数值，算法的结果并不是唯一的或确定的。\n" +
                "三，无限的算法，是那些由于没有定义终止定义条件，或定义的条件无法由输入的数据满足而不终止运行的算法。通常，无限算法的产生是由于未能确定的定义终止条件。";
        List<String> sentenceList = HanLP.extractSummary(document, 3);
        System.out.println(sentenceList);
    }

    @Test
    public void shortWordExtract() {
        String text = "算法工程师\n" +
                "算法（Algorithm）是一系列解决问题的清晰指令，也就是说，能够对一定规范的输入，在有限时间内获得所要求的输出。如果一个算法有缺陷，或不适合于某个问题，执行这个算法将不会解决这个问题。不同的算法可能用不同的时间、空间或效率来完成同样的任务。一个算法的优劣可以用空间复杂度与时间复杂度来衡量。算法工程师就是利用算法处理事物的人。\n" +
                "\n" +
                "1职位简介\n" +
                "算法工程师是一个非常高端的职位；\n" +
                "专业要求：计算机、电子、通信、数学等相关专业；\n" +
                "学历要求：本科及其以上的学历，大多数是硕士学历及其以上；\n" +
                "语言要求：英语要求是熟练，基本上能阅读国外专业书刊；\n" +
                "必须掌握计算机相关知识，熟练使用仿真工具MATLAB等，必须会一门编程语言。\n" +
                "\n" +
                "2研究方向\n" +
                "视频算法工程师、图像处理算法工程师、音频算法工程师 通信基带算法工程师\n" +
                "\n" +
                "3目前国内外状况\n" +
                "目前国内从事算法研究的工程师不少，但是高级算法工程师却很少，是一个非常紧缺的专业工程师。算法工程师根据研究领域来分主要有音频/视频算法处理、图像技术方面的二维信息算法处理和通信物理层、雷达信号处理、生物医学信号处理等领域的一维信息算法处理。\n" +
                "在计算机音视频和图形图像技术等二维信息算法处理方面目前比较先进的视频处理算法：机器视觉成为此类算法研究的核心；另外还有2D转3D算法(2D-to-3D conversion)，去隔行算法(de-interlacing)，运动估计运动补偿算法(Motion estimation/Motion Compensation)，去噪算法(Noise Reduction)，缩放算法(scaling)，锐化处理算法(Sharpness)，超分辨率算法(Super Resolution),手势识别(gesture recognition),人脸识别(face recognition)。\n" +
                "在通信物理层等一维信息领域目前常用的算法：无线领域的RRM、RTT，传送领域的调制解调、信道均衡、信号检测、网络优化、信号分解等。\n" +
                "另外数据挖掘、互联网搜索算法也成为当今的热门方向。\n" +
                "算法工程师逐渐往人工智能方向发展。";
        List<String> phraseList = HanLP.extractPhrase(text, 10);
        System.out.println(phraseList);
    }

    @Test
    public void sumup() {
        // 分词
        String text = "清华大学北京协和医院市政府佛山市一医院蔡徐坤和史蒂芬库里在广州市莱斯利镇的腾讯公司中参加公益活动和进入中国农业银行，               然后参观富士康，发现没有钱，            于是到瑞士银行。随后，再到白天鹅酒店中休息          。晚上，           广州市政府叫华南理工大学的李一和华工一小的同学到阿里大药房买达克宁，因为李二静脉扩张。然后著名篮球运动员蔡徐坤发表言论，               想参观广州教育部";
       // String text = "你好我是蔡徐坤，喜欢唱跳rap篮球";
//        Segment segment = HanLP.newSegment()
//                .enableOrganizationRecognize(true)
//                .enablePlaceRecognize(true)
//                .enableNameRecognize(true)
//                .enableTranslatedNameRecognize(true);

        List<Term> seg = NLPTokenizer.segment(text);
        //List<Term> seg = segment.seg(text);
        System.out.println(seg);

        // 短语提取
        List<String> list = HanLP.extractPhrase(text, 20);
        System.out.println(list);

        //摘要
        List<String> list1 = HanLP.extractSummary(text, 4);
        System.out.println(list1);
    }

    @Test
    public void convert() throws IOException, OpenXML4JException, XmlException {
        // pdf / doc 文件转换为字符串
        String path = "D:\\文档\\学习资料\\作业\\信息安全\\实验一 DES加解密算法编程实现\\实验一 DES加解密算法编程实现.doc";
        FileExtractService service = new FileExtractService();
        List<Term> fileIndex = service.createFileIndex(path);
        for (Term t : fileIndex) {
            String word = t.word;
            System.out.println(word.length());
        }
        System.out.println((int) fileIndex.get(5).word.charAt(0));
        System.out.println(fileIndex);
    }

    /**
     * 统计词频
     */
    @Test
    public void cntWord() throws OpenXML4JException, XmlException, IOException {
        Map<String, Integer> map = new HashMap<>();
        String path = "D:\\文档\\学习资料\\作业\\信息安全\\实验一 DES加解密算法编程实现\\实验一 DES加解密算法编程实现.doc";
        String text = FileUtil.extractString(path);
        text = FileUtil.removeSpace(text);
        Assert.assertNotNull(text);
        List<Term> ner = NlpUtil.NER(text);
        for (Term term : ner) {
            if (FileExtractService.keep.contains(term.nature.toString()) || term.nature.toString().equals("n")) {
                Integer value = Optional.ofNullable(map.get(term.word))
                        .map(x -> x+1).orElse(1);
                map.put(term.word, value);
            }
        }
        // 找出出现频率最大的 10 个词汇
      //  System.out.println(map);
        List<String> collect = map.entrySet().stream().sorted((x1, x2) -> x2.getValue() - x1.getValue()).limit(10).map(e -> e.getKey()).collect(Collectors.toList());
        System.out.println(collect);
    }

}
