package news.esports.com.esports.models;

import org.jsoup.select.Elements;

/**
 * Created by Renso on 10/1/2015.
 */
public class ArticleContent {

    public String image;
    public String title;
    public String desc;
    public String video;
    public Elements articleData;

    public ArticleContent(String image, String title, String desc, String video, Elements articleData){
        this.image = image;
        this.title = title;
        this.desc =desc;
        this.video = video;
        this.articleData = articleData;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public Elements getArticleData() {
        return articleData;
    }

    public void setArticleData(Elements articleData) {
        this.articleData = articleData;
    }
}
