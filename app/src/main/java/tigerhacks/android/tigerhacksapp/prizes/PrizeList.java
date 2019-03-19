package tigerhacks.android.tigerhacksapp.prizes;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import tigerhacks.android.tigerhacksapp.prizes.Prize;

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