package my.edu.tarc.finalyearproject.Domain;

import com.google.firebase.database.Exclude;

public class ImageUploadDomain {

    private String id;
    private String url;
    private String key;



    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }


    public String getUrl() {

        return url;
    }


    public ImageUploadDomain(String id, String url) {
        this.id = id;
        this.url = url;

    }

    public ImageUploadDomain(){

    }


    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
