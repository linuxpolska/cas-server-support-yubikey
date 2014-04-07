package pl.linuxpolska.cas.support.yubikey.client.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Base64;

import pl.linuxpolska.cas.support.yubikey.authentication.YubiKeyException;
import pl.linuxpolska.cas.support.yubikey.authentication.YubiKeyUtil;
import pl.linuxpolska.cas.support.yubikey.authentication.principal.YubiKeyCredentials;
import pl.linuxpolska.cas.support.yubikey.client.IYubiKeyAuthRepository;
import pl.linuxpolska.cas.support.yubikey.client.IYubiKeyResponse;
import pl.linuxpolska.cas.support.yubikey.client.YubiKeyStatus;
import pl.linuxpolska.cas.support.yubikey.server.WsapiYubiKeyController;

public class WsapiYubiKeyAuthRepository implements IYubiKeyAuthRepository {
  /**
   * LOG.
   */
  private final static Logger log = LoggerFactory
      .getLogger(WsapiYubiKeyAuthRepository.class);

  /**
   * 
   */
  private ExecutorCompletionService<IYubiKeyResponse> completionService;

  /**
   * clientId.
   */
  private String clientId;
  /**
   * key
   */
  private byte[] key;

  /**
   * A value 0 to 100 indicating percentage of syncing required by client, or
   * strings "fast" or "secure" to use server-configured values; if absent, let
   * the server decides
   */
  private int sl = -1;
  /**
   * Number of seconds to wait for sync responses; if absent, let the server
   * decides
   */
  private int timeout = -1;

  /**
   * wsapi_urls
   * 
   * "https://api.yubico.com/wsapi/2.0/verify",
   * "https://api2.yubico.com/wsapi/2.0/verify",
   * "https://api3.yubico.com/wsapi/2.0/verify",
   * "https://api4.yubico.com/wsapi/2.0/verify",
   * "https://api5.yubico.com/wsapi/2.0/verify"
   */
  private List<String> wsapiUrls;

  /**
   * WsapiYubiKeyAuthRepository.
   */
  public WsapiYubiKeyAuthRepository() {
    ThreadPoolExecutor pool = new ThreadPoolExecutor(0, 100, 250L,
        TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>());
    completionService = new ExecutorCompletionService<IYubiKeyResponse>(pool);
  }

  /**
   * @return the clientId
   */
  public String getClientId() {
    return clientId;
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
   * getWsapiUrls
   * 
   * @return
   */
  public List<String> getWsapiUrls() {
    return wsapiUrls;
  }

  /**
   * @param clientId
   *          the clientId to set
   */
  public void setClientId(String clientId) {
    this.clientId = clientId;
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
   * setWsapiUrls
   * 
   * @param wsapiUrls
   */
  public void setWsapiUrls(List<String> wsapiUrls) {
    this.wsapiUrls = wsapiUrls;
  }

  /**
   * 
   */
  @Override
  public IYubiKeyResponse verify(final YubiKeyCredentials credentials)
      throws YubiKeyException {
    String otp = credentials.getYubikeyOtp();
    if (!YubiKeyUtil.isValidOTPFormat(otp)) {
      throw new IllegalArgumentException("The OTP is not a valid format");
    }

    StringBuilder reqParam = new StringBuilder(200);
    // ID KLIENTA
    if (null != clientId) {
      reqParam.append(WsapiYubiKeyController.CLIENT_ID_PARAM).append('=')
          .append(clientId.toString());
    }
    // Identyfikator requestu
    String nonce = UUID.randomUUID().toString().replaceAll("-", "");
    reqParam.append('&').append(WsapiYubiKeyController.NONCE_PARAM).append('=')
        .append(nonce);
    reqParam.append('&').append(WsapiYubiKeyController.OTP_PARAM).append('=')
        .append(otp);

    if (0 <= sl) {
      reqParam.append('&').append(WsapiYubiKeyController.SL_PARAM).append('=')
          .append(sl);
    }

    if (0 <= timeout) {
      reqParam.append('&').append(WsapiYubiKeyController.TIMEOUT_PARAM)
          .append('=').append(timeout);
    }

    if (key != null) {
      String s;
      try {
        s = URLEncoder.encode(YubiKeyUtil.calculate(reqParam.toString(), key),
            "UTF-8");
      } catch (YubiKeyException e) {
        throw new YubiKeyException("Failed signing of request", e);
      } catch (UnsupportedEncodingException e) {
        throw new YubiKeyException("Failed to encode signature", e);
      }
      reqParam.append('&');
      reqParam.append(WsapiYubiKeyController.H_PARAM).append('=').append(s);
    }

    List<String> validationUrls = new ArrayList<String>(wsapiUrls.size());
    for (String wsapiUrl : wsapiUrls) {
      validationUrls.add(wsapiUrl + "?" + reqParam);
    }

    IYubiKeyResponse respons = fetch(validationUrls);

    return respons;
  }

  /**
   * 
   * @param urls
   * @return
   * @throws YubiKeyException
   */
  public IYubiKeyResponse fetch(List<String> urls) throws YubiKeyException {
    List<Future<IYubiKeyResponse>> tasks = new ArrayList<Future<IYubiKeyResponse>>();

    for (String url : urls) {
      log.debug("WSAPI Usery: {}", url);
      tasks.add(completionService.submit(new VerifyTask(url)));
    }

    IYubiKeyResponse response = null;
    try {
      int tasksDone = 0;
      Throwable savedException = null;
      Future<IYubiKeyResponse> futureResponse = completionService.poll(1L,
          TimeUnit.MINUTES);
      while (futureResponse != null) {
        try {
          tasksDone++;
          tasks.remove(futureResponse);
          response = futureResponse.get();
          if (!response.getStatus().equals(YubiKeyStatus.REPLAYED_REQUEST)) {
            break;
          }
        } catch (CancellationException ignored) {
          // this would be thrown by old cancelled calls.
          tasksDone--;
        } catch (ExecutionException e) {
          // tuck the real exception away and use it if we don't get any valid
          // answers.
          savedException = e.getCause();
        }
        if (tasksDone >= urls.size()) {
          break;
        }
        futureResponse = completionService.poll(1L, TimeUnit.MINUTES);
      }
      if (futureResponse == null || response == null) {
        if (savedException != null) {
          throw new YubiKeyException("a");
        } else {
          throw new YubiKeyException("b");
        }
      }
    } catch (InterruptedException e) {
      throw new YubiKeyException("c", e);
    }

    for (Future<IYubiKeyResponse> task : tasks) {
      task.cancel(true);
    }

    return response;
  }

  /**
   * 
   * @return
   */
  public int getSl() {
    return sl;
  }

  /**
   * 
   * @param sl
   */
  public void setSl(int sl) {
    this.sl = sl;
  }

  /**
   * 
   * @return
   */
  public int getTimeout() {
    return timeout;
  }

  /**
   * 
   * @param timeout
   */
  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }

  /**
   * 
   * @author ghalajko
   * 
   */
  static class VerifyTask implements Callable<IYubiKeyResponse> {
    private final String url;

    /**
     * Set up a VerifyTask for the Yubico Validation protocol v2
     * 
     * @param url
     *          the url to be used
     * @param userAgent
     *          the userAgent to be sent to the server, or NULL and one is
     *          calculated
     */
    public VerifyTask(String url) {
      this.url = url;
    }

    /**
     * Do the validation query for previous URL.
     * 
     * @throws Exception
     *           should not be anything but {@link IOException}
     */
    public IYubiKeyResponse call() throws Exception {
      URL url = new URL(this.url);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

      conn.setConnectTimeout(15000); // 15 second timeout
      conn.setReadTimeout(15000); // for both read and connect

      InputStream inStream = conn.getInputStream();
      BufferedReader in = new BufferedReader(new InputStreamReader(inStream));

      SimpleYubiKeyResponse resp = new SimpleYubiKeyResponse();
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        int ix = inputLine.indexOf("=");
        if (ix == -1)
          continue; // Invalid line
        String key = inputLine.substring(0, ix);
        String val = inputLine.substring(ix + 1);

        if ("h".equals(key)) {
          resp.setH(val);
        } else if ("t".equals(key)) {
          resp.setT(val);
        } else if ("otp".equals(key)) {
          resp.setOtp(val);
        } else if ("status".equals(key)) {
          resp.setStatus(YubiKeyStatus.valueOf(val));
        } else if ("timestamp".equals(key)) {
          resp.setTimestamp(val);
        } else if ("sessioncounter".equals(key)) {
          resp.setSessioncounter(val);
        } else if ("sessionuse".equals(key)) {
          resp.setSessionuse(val);
        } else if ("sl".equals(key)) {
          resp.setSl(val);
        } else if ("nonce".equals(key)) {
          resp.setNonce(val);
        }

      }
      in.close();

      if (resp.getStatus() == null) {
        throw new YubiKeyException("Status is null");
      }
      return resp;
    }
  }
}
