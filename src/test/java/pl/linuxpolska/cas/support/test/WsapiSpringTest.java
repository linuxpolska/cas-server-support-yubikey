package pl.linuxpolska.cas.support.test;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.AuthenticationManager;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.linuxpolska.cas.support.yubikey.authentication.principal.YubiKeyCredentials;
import pl.linuxpolska.cas.support.yubikey.client.YubiKeyStatus;

/**
 * 
 * @author ghalajko
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/wsapideployerConfigContext.xml")
public class WsapiSpringTest extends AbstractJUnit4SpringContextTests {
  /**
   * authenticationManager
   */
  @Autowired(required = true)
  private AuthenticationManager authenticationManager;

  /**
   * centralAuthenticationService
   */
  @Autowired(required = true)
  private CentralAuthenticationService centralAuthenticationService;

  /**
   * ticketRegistry
   */
  @Autowired(required = true)
  private TicketRegistry ticketRegistry;

  /**
   * @return the authenticationManager
   */
  public AuthenticationManager getAuthenticationManager() {
    return authenticationManager;
  }

  /**
   * @return the centralAuthenticationService
   */
  public CentralAuthenticationService getCentralAuthenticationService() {
    return centralAuthenticationService;
  }

  /**
   * @return the ticketRegistry
   */
  public TicketRegistry getTicketRegistry() {
    return ticketRegistry;
  }

  /**
   * @param authenticationManager
   *          the authenticationManager to set
   */
  public void setAuthenticationManager(
      AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  /**
   * @param centralAuthenticationService
   *          the centralAuthenticationService to set
   */
  public void setCentralAuthenticationService(
      CentralAuthenticationService centralAuthenticationService) {
    this.centralAuthenticationService = centralAuthenticationService;
  }

  /**
   * @param ticketRegistry
   *          the ticketRegistry to set
   */
  public void setTicketRegistry(TicketRegistry ticketRegistry) {
    this.ticketRegistry = ticketRegistry;
  }


  /**
   * 
   * @throws TicketException
   */
  @Test(expected = TicketException.class)
  public void testSpringTestD() throws TicketException {
    YubiKeyCredentials c = new YubiKeyCredentials();
    c.setUsername(YubiKeyStatus.BAD_OTP.name());
    c.setPassword(YubiKeyStatus.BAD_OTP.name());
    c.setYubikeyOtp("vvivijdejcktgtifdbligtifjlubkunnejccinnignkc" +
    		"");
    String ticket = centralAuthenticationService.createTicketGrantingTicket(c);
    Assert.assertNotNull(ticket);
  }

}
