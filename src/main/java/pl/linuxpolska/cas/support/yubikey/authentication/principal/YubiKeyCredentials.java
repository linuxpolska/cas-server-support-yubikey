package pl.linuxpolska.cas.support.yubikey.authentication.principal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.springframework.util.Assert;

/**
 * YubiKey
 * 
 * @author ghalajko
 * 
 */
public class YubiKeyCredentials extends UsernamePasswordCredentials {

  /**
   * serialVersionUID.
   */
  private static final long serialVersionUID = -3653912552687493961L;

  /**
   * Principal.
   */
  private final Principal principal;

  /**
   * yubikeyOtp.
   */
  @NotNull
  @Size(min = 1, max = 44, message = "required.password")
  private String yubikeyOtp;

  /**
	 * 
	 */
  public YubiKeyCredentials() {
    principal = null;
  }

  /**
   * 
   * @param principal
   */
  public YubiKeyCredentials(final Principal principal) {
    Assert.notNull(principal, "principal cannot be null");
    this.principal = principal;
  }

  /**
   * 
   * @return
   */
  public Principal getPrincipal() {
    return principal;
  }

  /**
   * @return the yubikeyOtp
   */
  public String getYubikeyOtp() {
    return yubikeyOtp;
  }

  /**
   * @param yubikeyOtp
   *          the yubikeyOtp to set
   */
  public void setYubikeyOtp(String yubikeyOtp) {
    this.yubikeyOtp = yubikeyOtp;
  }

}
