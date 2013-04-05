package pl.linuxpolska.cas.support.test;

import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.junit.Assert;
import org.junit.Test;

import pl.linuxpolska.cas.support.yubikey.authentication.YubiKeyUtil;
import pl.linuxpolska.cas.support.yubikey.authentication.principal.YubiKeyCredentials;

/**
 * 
 * @author ghalajko
 * 
 */
public class UtilTest {
  /**
 * 
 */
  @Test
  public void credentialSupportUserPass() {
    boolean res = YubiKeyUtil.supports(new UsernamePasswordCredentials());

    Assert.assertFalse(res);
  }

  /**
 * 
 */
  @Test
  public void credentialSupportYubi() {
    boolean res = YubiKeyUtil.supports(new YubiKeyCredentials());
    Assert.assertTrue(res);
  }

}
