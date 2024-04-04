package com.example.androidclientpico.ui.model;

public class RecommandBodyValue {
    /**
     * "id": "M2IxY2ZjOGI2NTI5NGRhMm",
     *       "lbl": "HitstreamDemo",
     *        "code": "com.And7Limited.HitstreamDemo",
     *        img:"https://jamma.globalvrpark.com/img_tmp/com.And7Limited.HitstreamDemo/img/HitstreamDemo.jpg",
     */
    public String id;
    public String lbl;
    public String img;
    public String code;
    public String Gamge_type;

    @Override
    public String toString() {
        return "RecommandBodyValue{" +
                "id='" + id + '\'' +
                ", lbl='" + lbl + '\'' +
                ", code='" + code + '\'' +
                ", img='" + img + '\'' +
                ", Gamge_type='" + Gamge_type + '\'' +
                '}';
    }
    //   @Override
//    public String tostring() {
//    return "RecommandBodyValue{" +
//            "id="+ id +
//            ",lbl=" + lbl + '\'' +
//            "code=" + code + '}';
//}
}
