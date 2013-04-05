package pl.linuxpolska.cas.support.test;

import org.junit.Assert;
import org.junit.Test;

import pl.linuxpolska.cas.support.yubikey.authentication.YubiKeyUtil;

/**
 * 
 * @author ghalajko
 * 
 */
public class ExtractPublicIdTest {
  /**
   * Test
   */
  @Test
  public void testA() {
    String otp = "vvivijdejcktvcnnnvfukjdnctgnnidverkkgvlenjvh";
    String res = YubiKeyUtil.getPublicId(otp);
    Assert.assertEquals("vvivijdejckt", res);    
  }
  
  /**
   * Test
   */
  @Test(expected=IllegalArgumentException.class)
  public void testB() {
    YubiKeyUtil.getPublicId(null);  
  }
  
  /**
   * Test
   */
  @Test(expected=IllegalArgumentException.class)
  public void testc() {
    YubiKeyUtil.getPublicId("aaaaaaaaa");  
  }
}
