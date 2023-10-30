package com.hk.mynote.po;

public class Memorandum {
    private Integer id;
    private String remark;
    private String memorandumTime;

    public Memorandum() {
    }

    public Memorandum(Integer id, String remark, String memorandumTime) {
        this.id = id;
        this.remark = remark;
        this.memorandumTime = memorandumTime;
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
}
