package tigerhacks.android.tigerhacksapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Prize{

    @SerializedName("sponsor")
    @Expose
    private Integer sponsor;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("reward")
    @Expose
    private String reward;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("prizetype")
    @Expose
    private String prizetype;

    public Integer getSponsor() {
        return sponsor;
    }

    public void setSponsor(Integer sponsor) {
        this.sponsor = sponsor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrizetype() {
        return prizetype;
    }

    public void setPrizetype(String prizetype) {
        this.prizetype = prizetype;
    }

}