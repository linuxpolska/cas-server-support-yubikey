package pl.linuxpolska.cas.support.yubikey.client;

/**
 * 
 * @author ghalajko
 * 
 */
public interface IYubiKeyResponse {
  /**
   * Signature of the response, with the same API key as the request.
   * 
   * @return response signature
   */
  String getH();

  /**
   * UTC timestamp from the server when response was processed.
   * 
   * @return server UTC timestamp
   */
  String getT();

  /**
   * Server response to the request.
   * 
   * @see YubicoResponseStatus
   * @return response status
   */
  YubiKeyStatus getStatus();

  /**
   * Returns the internal timestamp from the YubiKey 8hz timer.
   * 
   * @return yubikey internal timestamp
   */
  String getTimestamp();

  /**
   * Returns the non-volatile counter that is incremented on power-up.
   * 
   * @return session counter
   */
  String getSessioncounter();

  /**
   * Returns the volatile counter that is incremented on each button-press,
   * starts at 0 after power-up.
   * 
   * @return session use counter
   */
  String getSessionuse();

  /**
   * Returns the amount of sync the server achieved before sending the response.
   * 
   * @return sync, in procent
   */
  String getSl();

  /**
   * Echos back the OTP from the request, should match.
   * 
   * @return otp
   */
  String getOtp();

  /**
   * Echos back the nonce from the request. Should match.
   * 
   * @return nonce
   */
  public String getNonce();

  /**
   * Returns the public id of the returned OTP
   * 
   * @return public id
   */
  public String getPublicId();
}
