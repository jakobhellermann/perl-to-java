package com.sippy.wrapper.parent.response;

public class TnbResponse {
  boolean isTnb;
  String name;
  String tnb;

  public boolean getIsTnb() {
    return isTnb;
  }

  public void setIsTnb(boolean tnb) {
    isTnb = tnb;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTnb() {
    return tnb;
  }

  public void setTnb(String tnb) {
    this.tnb = tnb;
  }


  public TnbResponse(String tnb, String name, boolean isTnb) {
    this.name = name;
    this.tnb = tnb;
    this.isTnb = isTnb;
  }


}
