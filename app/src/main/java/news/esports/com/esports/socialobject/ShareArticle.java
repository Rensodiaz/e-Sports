package news.esports.com.esports.socialobject;

/**
 * Created by Renso on 10/11/2015.
 */
public class ShareArticle {
    private String title;
    private String link;

    public ShareArticle(String title, String link){
        this.title = title;
        this.link = link;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
