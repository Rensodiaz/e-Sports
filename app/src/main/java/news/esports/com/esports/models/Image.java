package news.esports.com.esports.models;

/**
 * Created by Renso on 9/20/2015.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("alt")
    @Expose
    private String alt;
    @SerializedName("href")
    @Expose
    private String href;
    @SerializedName("src")
    @Expose
    private String src;
    @SerializedName("text")
    @Expose
    private String text;

    /**
     *
     * @return
     * The alt
     */
    public String getAlt() {
        return alt;
    }

    /**
     *
     * @param alt
     * The alt
     */
    public void setAlt(String alt) {
        this.alt = alt;
    }

    /**
     *
     * @return
     * The href
     */
    public String getHref() {
        return href;
    }

    /**
     *
     * @param href
     * The href
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     *
     * @return
     * The src
     */
    public String getSrc() {
        return src;
    }

    /**
     *
     * @param src
     * The src
     */
    public void setSrc(String src) {
        this.src = src;
    }

    /**
     *
     * @return
     * The text
     */
    public String getText() {
        return text;
    }

    /**
     *
     * @param text
     * The text
     */
    public void setText(String text) {
        this.text = text;
    }

}
