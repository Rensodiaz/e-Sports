package news.esports.com.esports.models;

/**
 * Created by Renso on 9/20/2015.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Collection1 {

    @SerializedName("image")
    @Expose
    private Image image;
    @SerializedName("title")
    @Expose
    private Title title;
    @SerializedName("index")
    @Expose
    private Integer index;
    @SerializedName("url")
    @Expose
    private String url;

    /**
     *
     * @return
     * The image
     */
    public Image getImage() {
        return image;
    }

    /**
     *
     * @param image
     * The image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     *
     * @return
     * The title
     */
    public Title getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(Title title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     *
     * @param index
     * The index
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     *
     * @return
     * The url
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     * The url
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
