package edu.wj.sport.android.bean;

public class UserBean {

    private String id;

    private String phoneNumber;

    private String nickname;

    private int sex;

    private long birthDate;

    private String avatar;

    public UserBean(String id, String phoneNumber, String nickname, int sex, long birthDate, String avatar) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.sex = sex;
        this.birthDate = birthDate;
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phoneNumber;
    }

    public void setPhone(String phone) {
        this.phoneNumber = phone;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }


    public long getBirthDate() {
        return birthDate;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setBirthDate(long birthDate) {
        this.birthDate = birthDate;
    }

    public String getSexStr(){
        String str = "男";
        if (getSex() == 0){
            str = "男";
        }else {
            str = "女";
        }
        return str;
    }


}
