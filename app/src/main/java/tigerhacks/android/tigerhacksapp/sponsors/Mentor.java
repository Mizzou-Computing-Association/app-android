package tigerhacks.android.tigerhacksapp.sponsors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mentor {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("skills")
    @Expose
    private String skills;
    @SerializedName("contact")
    @Expose
    private String contact;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

}