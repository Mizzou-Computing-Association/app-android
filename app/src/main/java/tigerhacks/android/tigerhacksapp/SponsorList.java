package tigerhacks.android.tigerhacksapp;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SponsorList {

    @SerializedName("sponsors")
    @Expose
    private List<Sponsor> sponsors = null;

    public List<Sponsor> getSponsors() {
        return sponsors;
    }

    public void setSponsors(List<Sponsor> sponsors) {
        this.sponsors = sponsors;
    }

}