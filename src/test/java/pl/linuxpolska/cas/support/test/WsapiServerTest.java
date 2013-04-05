package pl.linuxpolska.cas.support.test;

import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import pl.linuxpolska.cas.support.yubikey.server.WsapiYubiKeyController;

/**
 * 
 * @author ghalajko
 * 
 */
public class WsapiServerTest {
  /**
   * test.
   * 
   * @throws Exception
   */
  @Test
  public void test() throws Exception {
    String uri = "http://ss/";

    MockHttpServletRequest req = new MockHttpServletRequest("GET", uri);
    req.addParameter("nonce", "12345678901234567891233");
    req.addParameter("id", "1234567890");
    req.addParameter("otp", "vvivijdejcktktrnllhhntckfuurvecgttlrfkgnnele");
    req.addParameter("sl", "100");
    
    MockHttpServletResponse res = new MockHttpServletResponse();

    WsapiYubiKeyController controll = new WsapiYubiKeyController();
    controll.setKey("oBVbNt7IZehZGR99rvq8d6RZ1DM=");
    
    controll.handleRequest(req, res);

    
    Assert.assertEquals(HttpServletResponse.SC_OK, res.getStatus());
    String txtReso = res.getContentAsString();
    System.out.println(txtReso);
  }
}
