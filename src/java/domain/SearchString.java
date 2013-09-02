/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

/**
 *
 * @author Boban
 */
public class SearchString {

    private String operatingSystem;
    private String license;
    private String progrlang;
    private String tag;
    private String keyword;
    private int page;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public SearchString() {
        operatingSystem = null;
        license = null;
        progrlang = null;
        tag = null;
        keyword = null;

    }

    public SearchString(String operatingSystem, String license, String progrlang, String tag, String keyword) {
        this.operatingSystem = operatingSystem;
        this.license = license;
        this.progrlang = progrlang;
        this.tag = tag;
        this.keyword = keyword;
    }

    /**
     * @return the operatingSystem
     */
    public String getOperatingSystem() {
        return operatingSystem;
    }

    /**
     * @param operatingSystem the operatingSystem to set
     */
    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    /**
     * @return the license
     */
    public String getLicense() {
        return license;
    }

    /**
     * @param license the license to set
     */
    public void setLicense(String license) {
        this.license = license;
    }

    /**
     * @return the progrlang
     */
    public String getProgrlang() {
        return progrlang;
    }

    /**
     * @param progrlang the progrlang to set
     */
    public void setProgrlang(String progrlang) {
        this.progrlang = progrlang;
    }

    /**
     * @return the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @param keyword the keyword to set
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
