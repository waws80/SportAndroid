package edu.wj.sport.android.bean;

public class UserBean {

    private int id;

    private String phone;

    private String nickname;

    private int sex;

    public UserBean(int id, String phone, String nickname, int sex) {
        this.id = id;
        this.phone = phone;
        this.nickname = nickname;

        this.sex = sex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
