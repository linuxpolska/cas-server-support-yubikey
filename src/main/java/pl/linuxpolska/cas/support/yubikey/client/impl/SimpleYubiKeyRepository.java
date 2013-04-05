package pl.linuxpolska.cas.support.yubikey.client.impl;

import pl.linuxpolska.cas.support.yubikey.authentication.principal.YubiKeyCredentials;
import pl.linuxpolska.cas.support.yubikey.client.IYubiKeyAuthRepository;
import pl.linuxpolska.cas.support.yubikey.client.IYubiKeyResponse;
import pl.linuxpolska.cas.support.yubikey.client.YubiKeyStatus;
/**
 * 
 * @author ghalajko
 *
 */
public class SimpleYubiKeyRepository implements IYubiKeyAuthRepository {
  /**
   * response.
   */
  @Override
  public IYubiKeyResponse verify(YubiKeyCredentials credentials) {
    SimpleYubiKeyResponse resp = new SimpleYubiKeyResponse();
    resp.setOtp(credentials.getYubikeyOtp());

    String username = credentials.getUsername();
    YubiKeyStatus stat = YubiKeyStatus.BAD_SIGNATURE;
    try {
      stat = YubiKeyStatus.valueOf(username);
    } catch (IllegalArgumentException e) {
        
    }
    resp.setStatus(stat);
    return resp;
  }

}
