package com.hk.mynote.po;

public class Memorandum {
    private Integer id;
    private String remark;
    private String memorandumTime;
    private Integer requestCode;

    public Memorandum() {
    }

    public Memorandum(Integer id, String remark, String memorandumTime, Integer requestCode) {
        this.id = id;
        this.remark = remark;
        this.memorandumTime = memorandumTime;
        this.requestCode = requestCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMemorandumTime() {
        return memorandumTime;
    }

    public void setMemorandumTime(String memorandumTime) {
        this.memorandumTime = memorandumTime;
    }

    public Integer getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(Integer requestCode) {
        this.requestCode = requestCode;
    }
}
