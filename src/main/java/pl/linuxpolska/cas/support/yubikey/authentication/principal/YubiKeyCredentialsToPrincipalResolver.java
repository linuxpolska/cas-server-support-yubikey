package pl.linuxpolska.cas.support.yubikey.authentication.principal;

import org.jasig.cas.authentication.principal.AbstractPersonDirectoryCredentialsToPrincipalResolver;
import org.jasig.cas.authentication.principal.Credentials;

import pl.linuxpolska.cas.support.yubikey.authentication.YubiKeyUtil;

/**
 * 
 * @author ghalajko
 * 
 */
public class YubiKeyCredentialsToPrincipalResolver extends
    AbstractPersonDirectoryCredentialsToPrincipalResolver {

  /**
   * supports
   */
  @Override
  public boolean supports(Credentials credentials) {
    return YubiKeyUtil.supports(credentials);
  }

  /**
	 * 
	 */
  @Override
  protected String extractPrincipalId(Credentials credentials) {
    YubiKeyCredentials tkc = (YubiKeyCredentials) credentials;
    return YubiKeyUtil.getPublicId(tkc.getYubikeyOtp());
  }

}
