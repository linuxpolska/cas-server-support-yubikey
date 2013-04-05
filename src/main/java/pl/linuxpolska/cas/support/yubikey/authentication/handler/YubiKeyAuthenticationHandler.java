package pl.linuxpolska.cas.support.yubikey.authentication.handler;

import javax.validation.constraints.NotNull;

import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.BadCredentialsAuthenticationException;
import org.jasig.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.jasig.cas.authentication.principal.Credentials;

import pl.linuxpolska.cas.support.yubikey.authentication.YubiKeyException;
import pl.linuxpolska.cas.support.yubikey.authentication.YubiKeyUtil;
import pl.linuxpolska.cas.support.yubikey.authentication.principal.YubiKeyCredentials;
import pl.linuxpolska.cas.support.yubikey.client.IYubiKeyAuthRepository;
import pl.linuxpolska.cas.support.yubikey.client.IYubiKeyResponse;
import pl.linuxpolska.cas.support.yubikey.client.YubiKeyStatus;

/**
 * 
 * @author ghalajko
 * 
 */
public class YubiKeyAuthenticationHandler extends
    AbstractPreAndPostProcessingAuthenticationHandler {

  /**
   * YubiKeyAuthRepository.
   */
  @NotNull
  private IYubiKeyAuthRepository repositor;

  /**
   * supports.
   */
  @Override
  public boolean supports(Credentials credentials) {
    return YubiKeyUtil.supports(credentials);
  }

  /**
   * doAuthentication
   */
  @Override
  protected boolean doAuthentication(Credentials credentials)
      throws AuthenticationException {

    final YubiKeyCredentials yubiKeyCredentials = (YubiKeyCredentials) credentials;

    log.debug("credential : {}", yubiKeyCredentials);

    final String otp = yubiKeyCredentials.getYubikeyOtp();

    if (!YubiKeyUtil.isValidOTPFormat(otp)) {
      log.debug("Wrong OTP format {}",otp);
      throw BadCredentialsAuthenticationException.ERROR;
    }

    IYubiKeyResponse response;
    try {
      response = repositor.verify(yubiKeyCredentials);
    } catch (YubiKeyException e) {
      log.error("verify err", e);
      return false;
    }

    log.debug("Rsponse: {}", response);

    if (YubiKeyStatus.OK == response.getStatus()) {
      if (response.getOtp() == null || !otp.equals(response.getOtp())) {
        throw BadCredentialsAuthenticationException.ERROR;
      }
      return true;
    }

    return false;
  }

  /**
   * 
   * @return
   */
  public IYubiKeyAuthRepository getRepositor() {
    return repositor;
  }

  /**
   * 
   * @param repositor
   */
  public void setRepositor(IYubiKeyAuthRepository repositor) {
    this.repositor = repositor;
  }

}
