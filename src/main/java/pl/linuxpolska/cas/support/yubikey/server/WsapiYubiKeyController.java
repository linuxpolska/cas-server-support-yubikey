package pl.linuxpolska.cas.support.yubikey.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import pl.linuxpolska.cas.support.yubikey.authentication.YubiKeyException;
import pl.linuxpolska.cas.support.yubikey.authentication.YubiKeyUtil;
import pl.linuxpolska.cas.support.yubikey.client.YubiKeyStatus;

/**
 * 
 * @author ghalajko
 * 
 */
public class WsapiYubiKeyController extends AbstractController {
  /**
   * CLIENT_ID_PARAM
   */
  public static final String CLIENT_ID_PARAM = "id";

  /**
   * NONCE_PARAM
   */
  public static final String NONCE_PARAM = "nonce";

  /**
   * OTP_PARAM
   */
  public static final String OTP_PARAM = "otp";

  /**
   * SL_PARAM
   */
  public static final String SL_PARAM = "sl";

  /**
   * TIMEOUT_PARAM
   */
  public static final String TIMEOUT_PARAM = "timeout";

  /**
   * H
   */
  public static final String H_PARAM = "h";

  /**
   * T
   */
  public static final String T_PARAM = "t";

  /**
   * T
   */
  public static final String STATUS_PARAM = "status";

  /**
   * log.
   */
  private final static Logger log = LoggerFactory
      .getLogger(WsapiYubiKeyController.class);

  /**
   * 
   */
  @NotNull
  private IYubiKeyServerRepository servrepo;

  /**
   * key
   */
  private byte[] key;

  /**
   * handleRequestInternal
   */
  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    response.setContentType("text/plain");

    String otp = request.getParameter(OTP_PARAM);
    String id = request.getParameter(CLIENT_ID_PARAM);
    String nonce = request.getParameter(NONCE_PARAM);
    String sl = request.getParameter(SL_PARAM);
    String timeout = request.getParameter(TIMEOUT_PARAM);
    String h = request.getParameter(H_PARAM);

    if (YubiKeyUtil.isNullOrEmpty(otp)) {
      String txtRespons = writeError(otp, nonce,
          YubiKeyStatus.MISSING_PARAMETER);
      writeText(response, txtRespons, HttpServletResponse.SC_OK);
      return null;
    } else {
      if (!YubiKeyUtil.isValidOTPFormat(otp)) {
        String txtRespons = writeError(otp, nonce, YubiKeyStatus.BAD_OTP);
        writeText(response, txtRespons, HttpServletResponse.SC_OK);
        return null;
      }
    }

    if (YubiKeyUtil.isNullOrEmpty(id)) {
      log.debug("Miss id param");
      String txtRespons = writeError(otp, nonce,
          YubiKeyStatus.MISSING_PARAMETER);
      writeText(response, txtRespons, HttpServletResponse.SC_OK);
      return null;
    }

    if (!YubiKeyUtil.isNullOrEmpty(timeout)
        && !YubiKeyUtil.haveOnlyDigit(timeout)) {
      log.debug("Miss timeout param");
      String txtRespons = writeError(otp, nonce,
          YubiKeyStatus.MISSING_PARAMETER);
      writeText(response, txtRespons, HttpServletResponse.SC_OK);
      return null;
    }

    if (YubiKeyUtil.isNullOrEmpty(nonce)) {
      log.debug("Miss nonce param");
      String txtRespons = writeError(otp, nonce,
          YubiKeyStatus.MISSING_PARAMETER);
      writeText(response, txtRespons, HttpServletResponse.SC_OK);
      return null;
    } else {
      if (16 > nonce.length() || 40 < nonce.length()) {
        log.debug("Miss nonce wrong length");
        String txtRespons = writeError(otp, nonce,
            YubiKeyStatus.MISSING_PARAMETER);
        writeText(response, txtRespons, HttpServletResponse.SC_OK);
        return null;
      }
    }

    if (YubiKeyUtil.isNullOrEmpty(sl)) {
      log.debug("Miss sl param");
      String txtRespons = writeError(otp, nonce,
          YubiKeyStatus.MISSING_PARAMETER);
      writeText(response, txtRespons, HttpServletResponse.SC_OK);
      return null;
    } else {
      if (!YubiKeyUtil.haveOnlyDigit(sl)) {
        log.debug("Miss sl should have only digits {}", sl);
        String txtRespons = writeError(otp, nonce,
            YubiKeyStatus.MISSING_PARAMETER);
        writeText(response, txtRespons, HttpServletResponse.SC_OK);
        return null;
      }

      int slNr = Integer.valueOf(sl);
      if (0 > slNr || 100 < slNr) {
        log.debug("Miss sl is too big or too low {}", sl);
        String txtRespons = writeError(otp, nonce,
            YubiKeyStatus.MISSING_PARAMETER);
        writeText(response, txtRespons, HttpServletResponse.SC_OK);
        return null;
      }
    }
    /**
     * Check data signature
     * 
     * @TODO DOKONCZYS
     */
    if (!YubiKeyUtil.isNullOrEmpty(h)) {

    }

    return null;
  }

  /**
   * getKey
   * 
   * @return
   */
  public String getKey() {
    return new String(Base64.encode(this.key));
  }

  /**
   * setKey
   * 
   * @param key
   */
  public void setKey(String key) {
    this.key = Base64.decode(key.getBytes());
  }

  /**
   * 
   * @param nonce
   * @return
   * @throws YubiKeyException
   */
  public String writeError(String otp, String nonce, YubiKeyStatus status)
      throws YubiKeyException {
    final StringBuilder buf = new StringBuilder(200);
    buf.append(T_PARAM).append('=').append(System.currentTimeMillis());
    buf.append("\r\n");
    buf.append(NONCE_PARAM).append('=').append(nonce);
    buf.append("\r\n");
    buf.append(STATUS_PARAM).append('=').append(status.toString());

    if (null != otp) {
      buf.append("\r\n");
      buf.append(OTP_PARAM).append('=').append(otp);
    }

    if (key != null) {
      String s;
      try {
        s = YubiKeyUtil.calculate(buf.toString(), key);
      } catch (YubiKeyException e) {
        throw new YubiKeyException("Failed signing of request", e);
      }
      buf.append("\r\n");
      buf.append(H_PARAM).append('=').append(s);
    }
    return buf.toString();
  }

  /**
   * 
   * @param response
   * @param text
   * @param status
   * @return
   */
  public static void writeText(final HttpServletResponse response,
      final CharSequence text, final int status) {
    PrintWriter printWriter;
    try {
      printWriter = response.getWriter();
      response.setStatus(status);
      printWriter.print(text);
    } catch (final IOException e) {
      log.error("Failed to write to response", e);
    }
  }

}
