package tigerhacks.android.tigerhacksapp;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrizeList {

    @SerializedName("prizes")
    @Expose
    private List<Prize> prizes = null;

    public List<Prize> getPrizes() {
        return prizes;
    }

    public void setPrizes(List<Prize> prizes) {
        this.prizes = prizes;
    }

}