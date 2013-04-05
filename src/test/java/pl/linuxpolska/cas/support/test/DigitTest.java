package pl.linuxpolska.cas.support.test;

import org.junit.Assert;
import org.junit.Test;

import pl.linuxpolska.cas.support.yubikey.authentication.YubiKeyUtil;

/**
 * 
 * @author ghalajko
 * 
 */
public class DigitTest {

  @Test
  public void test0() {
    boolean t = YubiKeyUtil.haveOnlyDigit("1233453");
    Assert.assertTrue(t);

  }

  @Test
  public void test1() {
    boolean t = YubiKeyUtil.haveOnlyDigit("00012345678900");
    Assert.assertTrue(t);

  }

  @Test
  public void test2() {
    boolean t;
    String tstr = "0123456789\u00F6";
    t = YubiKeyUtil.haveOnlyDigit(tstr);
    Assert.assertFalse(t);
  }

  @Test
  public void test3() {
    boolean t;
    String tstr = "0123456789a";
    t = YubiKeyUtil.haveOnlyDigit(tstr);
    Assert.assertFalse(t);
  }

  @Test
  public void tes4() {
    boolean t;
    String tstr = "a10123456789";
    t = YubiKeyUtil.haveOnlyDigit(tstr);
    Assert.assertFalse(t);
  }

  @Test
  public void tes6() {
    boolean t;
    String tstr = "\u003A";
    t = YubiKeyUtil.haveOnlyDigit(tstr);
    Assert.assertFalse(t);
  }

  @Test
  public void tes7() {
    boolean t;
    String tstr = "\u002F";
    t = YubiKeyUtil.haveOnlyDigit(tstr);
    Assert.assertFalse(t);
  }
}
