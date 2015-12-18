package com.twitterassistant.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class DisableUrlSessionFilter implements Filter {

  // Constants ---------------------------------------------------------------------------------------------- Constants

  private static final transient Logger LOG = LoggerFactory.getLogger(DisableUrlSessionFilter.class);

  // Instance Variables ---------------------------------------------------------------------------- Instance Variables

  // Constructors ---------------------------------------------------------------------------------------- Constructors

  // Public Methods ------------------------------------------------------------------------------------ Public Methods

  /**
   * Filters requests to disable URL-based session identifiers.
   */
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    // skip non-http requests
    if (!(request instanceof HttpServletRequest)) {
      chain.doFilter(request, response);
      return;
    }

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    // clear session if session id in URL
    if (httpRequest.isRequestedSessionIdFromURL()) {
      HttpSession session = httpRequest.getSession();
      if (session != null) {
        session.invalidate();
      }
    }

    // wrap response to remove URL encoding
    HttpServletResponseWrapper wrappedResponse = new HttpServletResponseWrapper(httpResponse) {
      @Override
      public String encodeRedirectUrl(String url) {
        return url;
      }

      @Override
      public String encodeRedirectURL(String url) {
        return url;
      }

      @Override
      public String encodeUrl(String url) {
        return url;
      }

      @Override
      public String encodeURL(String url) {
        return url;
      }
    };

    // process next request in chain
    chain.doFilter(request, wrappedResponse);
  }

  /**
   * Unused.
   */
  public void init(FilterConfig config) throws ServletException {
  }

  /**
   * Unused.
   */
  public void destroy() {
  }

  // Protected Methods ------------------------------------------------------------------------------ Protected Methods

  // Private Methods ---------------------------------------------------------------------------------- Private Methods

  // Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

} // end of class