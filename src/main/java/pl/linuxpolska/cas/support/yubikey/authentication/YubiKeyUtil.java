/**
 * 
 */
package pl.linuxpolska.cas.support.yubikey.authentication;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.jasig.cas.authentication.principal.Credentials;

import pl.linuxpolska.cas.support.yubikey.authentication.principal.YubiKeyCredentials;

/**
 * @author ghalajko
 * 
 */
public final class YubiKeyUtil {
  /**
   * OTP_MIN_LEN
   */
  public static final Integer OTP_MIN_LEN = 32;
  /**
   * OTP_MAX_LEN
   */
  public static final Integer OTP_MAX_LEN = 48;

  /**
   * YubiKeyUtil
   */
  private YubiKeyUtil() {
  }

  /**
   * supports
   * 
   * @param credentials
   * @return
   */
  public static boolean supports(final Credentials credentials) {
    return credentials != null
        && (YubiKeyCredentials.class.isAssignableFrom(credentials.getClass()));

  }

  /**
   * 
   * @param otp
   * @return
   */
  public static boolean isValidOTPFormat(String otp) {
    if (otp == null) {
      return false; // null strings aren't valid OTPs
    }
    int len = otp.length();
    boolean isPrintable = true;

    for (char c : otp.toCharArray()) {
      if (c < 0x20 || c > 0x7E) {
        isPrintable = false;
        break;
      }
    }
    return isPrintable && (OTP_MIN_LEN <= len && len <= OTP_MAX_LEN);
  }

  /**
   * 
   * @param otp
   * @return
   */
  public static String getPublicId(String otp) {
    if ((otp == null) || (otp.length() < OTP_MIN_LEN)) {
      // not a valid OTP format, throw an exception
      throw new IllegalArgumentException("The OTP is too short to be valid");
    }

    Integer len = otp.length();

    /*
     * The OTP part is always the last 32 bytes of otp. Whatever is before that
     * (if anything) is the public ID of the Yubikey. The ID can be set to ''
     * through personalization.
     */
    return otp.substring(0, len - OTP_MIN_LEN);
  }

  /**
   * 
   * @param str
   * @return
   */
  public static boolean isNullOrEmpty(String str) {
    return null == str || str.isEmpty() || str.trim().isEmpty();
  }

  /**
   * 
   * @param s
   * @return
   */
  public static boolean haveOnlyDigit(final String s) {
    for (int i = s.length() - 1; i >= 0; --i) {
      char ch = s.charAt(i);
      if ('0' > ch || '9' < ch) {
        return false;
      }
    }

    return true;
  }

  /**
   * 
   * @param data
   * @param key
   * @return
   * @throws YubiKeyException
   */
  public static String calculate(String data, byte[] key)
      throws YubiKeyException {
    HMac hmac = new HMac(new SHA1Digest());
    KeyParameter cryptoKey = new KeyParameter(key);
    hmac.init(cryptoKey);
    byte[] resBuf = new byte[hmac.getMacSize()];

    try {
      byte[] dataArray = data.getBytes("UTF-8");
      hmac.update(dataArray, 0, dataArray.length);
      hmac.doFinal(resBuf, 0);
    } catch (UnsupportedEncodingException e) {
      throw new YubiKeyException("Unsupported encoding (utf8?)", e);
    }

    return new String(Base64.encodeBase64(resBuf));
  }
}
