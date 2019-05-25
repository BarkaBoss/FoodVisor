package xyz.nokt.btf.foodadvisor;

public class RestaurantObj {

    String rest_name, rest_id, rest_mail, rest_phone, rest_address, rest_imgUrl, rest_features, rest_feat_loccont, rest_feat_sitInOut;

    public RestaurantObj()
    {}

    public RestaurantObj(String rest_name, String rest_id, String rest_mail, String rest_phone, String rest_address, String rest_imgUrl, String rest_features, String rest_feat_loccont, String rest_feat_sitInOut) {
        this.rest_name = rest_name;
        this.rest_id = rest_id;
        this.rest_mail = rest_mail;
        this.rest_phone = rest_phone;
        this.rest_address = rest_address;
        this.rest_imgUrl = rest_imgUrl;
        this.rest_features = rest_features;
        this.rest_feat_loccont = rest_feat_loccont;
        this.rest_feat_sitInOut = rest_feat_sitInOut;
    }

    public String getRest_feat_loccont() {
        return rest_feat_loccont;
    }

    public void setRest_feat_loccont(String rest_feat_loccont) {
        this.rest_feat_loccont = rest_feat_loccont;
    }

    public String getRest_feat_sitInOut() {
        return rest_feat_sitInOut;
    }

    public void setRest_feat_sitInOut(String rest_feat_sitInOut) {
        this.rest_feat_sitInOut = rest_feat_sitInOut;
    }

    public String getRest_name() {
        return rest_name;
    }

    public void setRest_name(String rest_name) {
        this.rest_name = rest_name;
    }

    public String getRest_id() {
        return rest_id;
    }

    public void setRest_id(String rest_id) {
        this.rest_id = rest_id;
    }

    public String getRest_mail() {
        return rest_mail;
    }

    public void setRest_mail(String rest_mail) {
        this.rest_mail = rest_mail;
    }

    public String getRest_phone() {
        return rest_phone;
    }

    public void setRest_phone(String rest_phone) {
        this.rest_phone = rest_phone;
    }

    public String getRest_address() {
        return rest_address;
    }

    public void setRest_address(String rest_address) {
        this.rest_address = rest_address;
    }

    public String getRest_imgUrl() {
        return rest_imgUrl;
    }

    public void setRest_imgUrl(String rest_imgUrl) {
        this.rest_imgUrl = rest_imgUrl;
    }

    public String getRest_features() {
        return rest_features;
    }

    public void setRest_features(String rest_features) {
        this.rest_features = rest_features;
    }
}
