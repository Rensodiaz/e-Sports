package news.esports.com.esports.Helpers;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import news.esports.com.esports.models.ArticleContent;

/**
 * Created by Renso on 10/1/2015.
 */
public class ArticleHelper {
    private final String tag = this.getClass().getSimpleName();
    public ArticleInterface dataTitleContent;
    public String url;

    public ArticleHelper(ArticleInterface dataTitleContent, String url){
        this.dataTitleContent = dataTitleContent;
        this.url = url;
        new Description().execute();
    };

    // Article AsyncTask
    private class Description extends AsyncTask<Void, Void, Void> {
        String title = "";
        String img ="";
        String video = "";
        StringBuilder desc = new StringBuilder();
        Element content;
        Elements content2;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {//TODO: check this because test are not passing here
                // Connect to the web site
                Log.e(tag, "url: "+url);
                //String url2 = "http://www.gosugamers.net/heroesofthestorm/features/4506-grubby-i-started-playing-heroes-and-never-looked-back";
                Document doc = Jsoup.connect(url).get();

                content = doc.getElementById("article");
                content2 = doc.getElementsByClass("light");
                Elements articleDesc = content.getElementsByTag("p");
                for (Element paragraph : articleDesc) {
                    desc.append("\n");
                    desc.append(paragraph.text());
                }
                title = content.getElementsByTag("h1").text();
                img = content.getElementsByTag("img").attr("src");
                video = content.getElementsByTag("iframe").attr("src");
            } catch (IOException e) {
                Log.e(tag, "failed to get the article IOEx: "+e.toString());
            } catch (NullPointerException ex){
                Log.e(tag, "failed to get the article null: "+ex.toString());
            } catch (RuntimeException re){
                Log.e(tag, "failed to get article runtime E: "+re.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            ArticleContent article = new ArticleContent(img, title, desc.toString(), video, content2);
            dataTitleContent.getContentData(article);
        }
    }
}
