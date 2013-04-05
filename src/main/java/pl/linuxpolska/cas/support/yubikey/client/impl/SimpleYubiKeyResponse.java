package pl.linuxpolska.cas.support.yubikey.client.impl;

import pl.linuxpolska.cas.support.yubikey.client.IYubiKeyResponse;
import pl.linuxpolska.cas.support.yubikey.client.YubiKeyStatus;

/**
 * 
 * @author ghalajko
 * 
 */
public class SimpleYubiKeyResponse implements IYubiKeyResponse {
  /**
   * h
   */
  private String h;

  /**
   * nonce.
   */
  private String nonce;

  /**
   * otp
   */
  private String otp;

  /**
   * publicId
   */
  private String publicId;

  /**
   * sessioncounter.
   */
  private String sessioncounter;

  /**
   * sessionuse
   */
  private String sessionuse;

  /**
   * sl
   */
  private String sl;
  /**
   * status
   */
  private YubiKeyStatus status;

  /**
   * t
   */
  private String t;

  /**
   * timestamp
   */
  private String timestamp;
  @Override
  public String getH() {
    return h;
  }
  /**
   * 
   */
  @Override
  public String getNonce() {
    return nonce;
  }

  @Override
  public String getOtp() {
    return otp;
  }

  @Override
  public String getPublicId() {
    return publicId;
  }

  @Override
  public String getSessioncounter() {
    return sessioncounter;
  }

  @Override
  public String getSessionuse() {
    return sessionuse;
  }

  @Override
  public String getSl() {
    return sl;
  }

  @Override
  public YubiKeyStatus getStatus() {
    return status;
  }

  @Override
  public String getT() {
    return t;
  }

  @Override
  public String getTimestamp() {
    return timestamp;
  }

  /**
   * 
   * @param val
   */
  public void setH(String val) {
    h = val;
  }

  /**
   * @param nonce
   *          the nonce to set
   */
  public void setNonce(String nonce) {
    this.nonce = nonce;
  }

  /**
   * @param otp
   *          the otp to set
   */
  public void setOtp(String otp) {
    this.otp = otp;
  }

  /**
   * @param publicId
   *          the publicId to set
   */
  public void setPublicId(String publicId) {
    this.publicId = publicId;
  }

  /**
   * setSessioncounter
   * 
   * @param val
   */
  public void setSessioncounter(String val) {
    sessioncounter = val;
  }

  /**
   * 
   * @param val
   */
  public void setSessionuse(String val) {
    sessionuse = val;
  }

  /**
   * 
   * @param val
   */
  public void setSl(String val) {
    sl = val;
  }

  /**
   * @param status
   *          the status to set
   */
  public void setStatus(YubiKeyStatus status) {
    this.status = status;
  }

  /**
   * 
   * @param val
   */
  public void setT(String val) {
    t = val;
  }

  /**
   * timestamp
   * 
   * @param val
   */
  public void setTimestamp(String val) {
    timestamp = val;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(200);
    builder.append("SimpleYubiKeyResponse [");
    if (h != null) {
      builder.append("h=");
      builder.append(h);
      builder.append(", ");
    }
    if (nonce != null) {
      builder.append("nonce=");
      builder.append(nonce);
      builder.append(", ");
    }
    if (otp != null) {
      builder.append("otp=");
      builder.append(otp);
      builder.append(", ");
    }
    if (publicId != null) {
      builder.append("publicId=");
      builder.append(publicId);
      builder.append(", ");
    }
    if (sessioncounter != null) {
      builder.append("sessioncounter=");
      builder.append(sessioncounter);
      builder.append(", ");
    }
    if (sessionuse != null) {
      builder.append("sessionuse=");
      builder.append(sessionuse);
      builder.append(", ");
    }
    if (sl != null) {
      builder.append("sl=");
      builder.append(sl);
      builder.append(", ");
    }
    if (status != null) {
      builder.append("status=");
      builder.append(status);
      builder.append(", ");
    }
    if (t != null) {
      builder.append("t=");
      builder.append(t);
      builder.append(", ");
    }
    if (timestamp != null) {
      builder.append("timestamp=");
      builder.append(timestamp);
    }
    builder.append("]");
    return builder.toString();
  }

}
