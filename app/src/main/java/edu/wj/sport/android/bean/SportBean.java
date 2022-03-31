package edu.wj.sport.android.bean;

public class SportBean {

    private String id;

    private String userId;

    private double mileage;

    private long duration;

    private String points;

    private long createTime;


    public SportBean(String id, String userId, double mileage, long duration, String points, long createTime) {
        this.id = id;
        this.userId = userId;
        this.mileage = mileage;
        this.duration = duration;
        this.points = points;
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SportBean{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", mileage=" + mileage +
                ", duration=" + duration +
                ", points='" + points + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
