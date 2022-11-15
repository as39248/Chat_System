package usecases.profile_changes;

import entities.ProfilePicture;

public class ChangeProfileOutputModel {
    private final int profileUID;

    private String name;
    private String description;
    private ProfilePicture pic;
    public ChangeProfileOutputModel(int profileUID, String name, String description,ProfilePicture pic) {
        this.profileUID = profileUID;
        this.pic = pic;
        this.name = name;
        this.description = description;
    }

    public int getProfileUID() {
        return profileUID;
    }


    public ProfilePicture getPic() {
        return pic;
    }

    public void setPic(ProfilePicture pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}


