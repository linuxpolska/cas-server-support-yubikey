package pl.linuxpolska.cas.support.yubikey.server;

import pl.linuxpolska.cas.support.yubikey.authentication.YubiKeyException;

/**
 * 
 * @author ghalajko
 * 
 */
public interface IYubiKeyServerRepository {
  /**
   * 
   * @param clientId
   * @return
   */
  byte[] clientSecretKey(String clientId) throws YubiKeyException;
}
