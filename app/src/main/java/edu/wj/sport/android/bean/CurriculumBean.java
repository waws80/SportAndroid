package edu.wj.sport.android.bean;

public class CurriculumBean {

    private int id;

    private String thumb;

    private String video;

    private String cname;

    private String tip;

    private String type;

    private long duration;

    private String content;

    public CurriculumBean(int id, String thumb, String video, String cname, String tip, String type, long duration, String content) {
        this.id = id;
        this.thumb = thumb;
        this.video = video;
        this.cname = cname;
        this.tip = tip;
        this.type = type;
        this.duration = duration;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CurriculumBean{" +
                "id=" + id +
                ", thumb='" + thumb + '\'' +
                ", video='" + video + '\'' +
                ", cname='" + cname + '\'' +
                ", tip='" + tip + '\'' +
                ", type='" + type + '\'' +
                ", duration=" + duration +
                ", content='" + content + '\'' +
                '}';
    }
}
