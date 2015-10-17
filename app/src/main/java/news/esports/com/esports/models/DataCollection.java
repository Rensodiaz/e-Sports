package news.esports.com.esports.models;

/**
 * Created by Renso on 9/20/2015.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataCollection {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("frequency")
    @Expose
    private String frequency;
    @SerializedName("version")
    @Expose
    private Integer version;
    @SerializedName("newdata")
    @Expose
    private Boolean newdata;
    @SerializedName("lastrunstatus")
    @Expose
    private String lastrunstatus;
    @SerializedName("thisversionstatus")
    @Expose
    private String thisversionstatus;
    @SerializedName("thisversionrun")
    @Expose
    private String thisversionrun;
    @SerializedName("results")
    @Expose
    private Results results;

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The count
     */
    public Integer getCount() {
        return count;
    }

    /**
     *
     * @param count
     * The count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     *
     * @return
     * The frequency
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     *
     * @param frequency
     * The frequency
     */
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    /**
     *
     * @return
     * The version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     *
     * @param version
     * The version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     *
     * @return
     * The newdata
     */
    public Boolean getNewdata() {
        return newdata;
    }

    /**
     *
     * @param newdata
     * The newdata
     */
    public void setNewdata(Boolean newdata) {
        this.newdata = newdata;
    }

    /**
     *
     * @return
     * The lastrunstatus
     */
    public String getLastrunstatus() {
        return lastrunstatus;
    }

    /**
     *
     * @param lastrunstatus
     * The lastrunstatus
     */
    public void setLastrunstatus(String lastrunstatus) {
        this.lastrunstatus = lastrunstatus;
    }

    /**
     *
     * @return
     * The thisversionstatus
     */
    public String getThisversionstatus() {
        return thisversionstatus;
    }

    /**
     *
     * @param thisversionstatus
     * The thisversionstatus
     */
    public void setThisversionstatus(String thisversionstatus) {
        this.thisversionstatus = thisversionstatus;
    }

    /**
     *
     * @return
     * The thisversionrun
     */
    public String getThisversionrun() {
        return thisversionrun;
    }

    /**
     *
     * @param thisversionrun
     * The thisversionrun
     */
    public void setThisversionrun(String thisversionrun) {
        this.thisversionrun = thisversionrun;
    }

    /**
     *
     * @return
     * The results
     */
    public Results getResults() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(Results results) {
        this.results = results;
    }

}
