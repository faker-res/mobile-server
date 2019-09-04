package com.lzkj.mobile.config;

public enum AwardOrderStatus {
    DEALING(0,"处理中"),
    SENDED(1,"已发货"),
    RECIEPED(2,"已收货"),
    REQUEST_RETURN(3,"申请退货"),
    AGREE_RETURN(4,"同意退货等待您发货"),
    REFUSE_RETURN(5,"拒绝退货"),
    SUCESS_RETURN(6,"退货成功且退款成功");
    private int status;

    private String describe;

    AwardOrderStatus(int status,String describe){
      this.status =status;
      this.describe =describe;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
    public static  String getDescribe(int status){
        String describe =new String();
        for(AwardOrderStatus s: AwardOrderStatus.values()){
              if(status ==s.getStatus()){
                  describe = s.getDescribe();
                   break;
              }
        }
        return describe;
    }
}
