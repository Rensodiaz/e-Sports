package news.esports.com.esports.models;

/**
 * Created by Renso on 9/20/2015.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Results {

    @SerializedName("collection1")
    @Expose
    private List<Collection1> collection1 = new ArrayList<>();

    /**
     *
     * @return
     * The collection1
     */
    public List<Collection1> getCollection1() {
        return collection1;
    }

    /**
     *
     * @param collection1
     * The collection1
     */
    public void setCollection1(List<Collection1> collection1) {
        this.collection1 = collection1;
    }

}
