package pl.linuxpolska.cas.support.yubikey.client;

import pl.linuxpolska.cas.support.yubikey.authentication.YubiKeyException;
import pl.linuxpolska.cas.support.yubikey.authentication.principal.YubiKeyCredentials;

/**
 * 
 * @author ghalajko
 * 
 */
public interface IYubiKeyAuthRepository {
  /**
   * 
   * @param username
   * @param password
   * @param otp
   * @return
   */
  IYubiKeyResponse verify(YubiKeyCredentials credentials)
      throws YubiKeyException;
}
