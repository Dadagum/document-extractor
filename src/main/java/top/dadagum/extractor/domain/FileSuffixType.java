package top.dadagum.extractor.domain;

public enum  FileSuffixType {
    /**
     * 图片
     */
    BMP("bmp"),
    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png"),
    GIF("gif"),
    TIFF("tif"),
    ICO("ico"),
    WEBP("webp"),
    PCX("pcx"),
    TGA("tga"),
    EXIF("exif"),
    FPX("fpx"),
    SVG("svg"),
    PSD("psd"),
    CDR("cdr"),
    PCD("pcd"),
    DXF("dxf"),
    UFO("ufo"),
    EPS("eps"),
    AI("ai"),
    HDRI("hdr"),
    RAW("raw"),
    WNF("wmf"),
    FLIC("flc"),
    EMF("emf"),

    /**
     * 视频
     */
    MP4("mp4"),
    AVI("avi"),
    FLV("flv"),
    MKV("mkv"),

    WMV("wmv"),
    ASF("asf"),
    ASX("asx"),

    RM("rm"),
    RA("ra"),
    RMVB("rmvb"),

    MOV("mov"),
    QT("qt"),

    DAT("dat"),
    MPEG("mpeg"),
    MPG("mpg"),

    /**
     * 音频
     */
    MP3("mp3"),
    WAV("wav"),

    /**
     * 文档
     */
    DOC("doc"),
    DOCX("docx"),
    TXT("txt"),
    XLS("xls"),
    XLSX("xlsx"),
    PPT("ppt"),
    PPTX("pptx"),
    PDF("pdf"),
    VSDX("vsdx"),
    ;

    private String value = "";
    FileSuffixType(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }
}
