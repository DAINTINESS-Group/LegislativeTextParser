package org.uoi.legislativetextparser.config;

public class Config {

    private String lawPdfPath;
    private String lawJsonPath;

    public Config(String lawPdfPath, String lawJsonPath) {
        this.lawPdfPath = lawPdfPath;
        this.lawJsonPath = lawJsonPath;
    }

    public String getLawPdfPath() {
        return lawPdfPath;
    }

    public void setLawPdfPath(String lawPdfPath) {
        this.lawPdfPath = lawPdfPath;
    }

    public String getLawJsonPath() {
        return lawJsonPath;
    }

    public void setLawJsonPath(String lawJsonPath) {
        this.lawJsonPath = lawJsonPath;
    }
}
