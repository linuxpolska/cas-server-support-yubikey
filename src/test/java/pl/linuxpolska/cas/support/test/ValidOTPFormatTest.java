package pl.linuxpolska.cas.support.test;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import pl.linuxpolska.cas.support.yubikey.authentication.YubiKeyUtil;

/**
 * 
 * @author ghalajko
 * 
 */
@RunWith(Parameterized.class)
public class ValidOTPFormatTest {
  /**
   * 
   * @return
   */
  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
        { "vvivijdejckttbetlebjtebgdbhleeifnifkgldjhkfh", true },
        { "vvivijdejckttbetlebjtebgdbhleeifnifkgldjhkfh\n", false },
        { "vviv", false },
        { null, false },
        { "vvivijdejckttbetlebjtebgdbhleeifnifkgldjhkfhvvivijdejckttbetlebjtebgdbhleeifnifkgldjhkfh", false } });
  }

  /**
 * 
 */
  private String otp;
  /**
 * 
 */
  private boolean expected;

  /**
   * 
   */
  public ValidOTPFormatTest(String otp, boolean expected) {
    this.otp = otp;
    this.expected = expected;

  }

  /**
   */
  @Test
  public void test() {
    boolean res = YubiKeyUtil.isValidOTPFormat(otp);
    Assert.assertEquals(expected, res);
  }
}
