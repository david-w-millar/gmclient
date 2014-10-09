package gm.client

import static groovy.json.JsonOutput.*
import static groovyx.net.http.ContentType.*

import groovy.transform.ToString
import groovyx.net.http.RESTClient
import groovyx.net.http.HttpResponseDecorator as Response
import co.freeside.betamax.httpclient.BetamaxRoutePlanner

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.DeserializationFeature

import gm.client.models.*


/**
 * Main Entry point for the gm client
 *
 * Example Usage:
 *   Client client = new Client()
 *
 *   GetEmailAddressResponse getEmail = client.getEmailAddress()
 *   SetEmailAddressResponse getEmail = client.setEmailAddress('mytester', 'guerrillamail.com')
 *   GetEmailListResponse emails = client.getEmailList()
 *   Email email = client.fetchEmail('email_id_12345')
 *   GetEmailListResponse newMails = client.checkEmail()
 *
 *   client.forgetMe()
 */
@ToString( includeNames = true )
class GMClient {

  // Global
  RESTClient client = new RESTClient('http://guerrillamail.com/ajax.php', JSON)
  private ObjectMapper mapper

  // Session State
  private String sessionId
  private String email
  private final String domainName
  private String seq = '1'

  // Config
  private static final String DEFAULT_DOMAIN = 'guerrillamail.com'

  GMClient() {
    domainName = DEFAULT_DOMAIN
    initRestClient()
    initMapper()
  }

  GMClient(final String user, final String domain = DEFAULT_DOMAIN) {
    initRestClient()
    initMapper()
    setEmailAddress(user, domain)
  }

  private void initRestClient() {
    // Hackish, but this makes http-builder respect the default proxy
    BetamaxRoutePlanner.configure(client.client)
    client.ignoreSSLIssues()
  }

  private void initMapper() {
    mapper = new ObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .configure(SerializationFeature.INDENT_OUTPUT, true)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  }


  GetEmailAddressResponse getEmailAddress() {
    Response rawResponse = client.get(
      query: [ f: 'get_email_address', ip: '127.0.0.1', agent: 'Mozilla' ]
    )
    GetEmailAddressResponse response = mapper.readValue( toJson(rawResponse.data), GetEmailAddressResponse )
    email     = response.email_addr
    sessionId = response.sid_token
    response
  }


  SetEmailAddressResponse setEmailAddress(final String user, final String domain = 'guerrillamail.com') {
    Response rawResponse = client.post(
      query: [ f: 'set_email_user', domain: domain, email_user: user, lang: 'en', _: sessionId ]
    )
    SetEmailAddressResponse response = mapper.readValue( toJson(rawResponse.data), SetEmailAddressResponse )
    sessionId = response.sid_token
    response
  }


  /** Get entire email list */
  GetEmailListResponse getEmailList(int offset = 0) {
    Response rawResponse = client.get(
      query: [ f: 'get_email_list', offset: offset, domain: domainName, _: sessionId ]
    )
    GetEmailListResponse response = mapper.readValue( toJson(rawResponse.data), GetEmailListResponse)
    sessionId = response.sid_token
    response
  }


  /** Fetch a particular email, mark as read */
  Email fetchEmail(final String emailId) {
    Response rawResponse = client.get(
      query: [ f: 'fetch_email', email_id: "mr_${emailId}", domain: domainName, _: sessionId ]
    )
    Email response = mapper.readValue( toJson(rawResponse.data), Email)
    sessionId = response.sid_token
    response
  }


  /** Check for new Email */
  GetEmailListResponse checkEmail() {
    Response rawResponse = client.get(
      query: [ f: 'check_email', seq: seq, domain: domainName, _: sessionId ]
    )
    GetEmailListResponse response = mapper.readValue( toJson(rawResponse.data), GetEmailListResponse)
    sessionId = response.sid_token
    if ( ! response.list.isEmpty() ) {
      seq = response.list.last().mail_id
    }
    response
  }


  boolean forgetMe() {
    client.post(query: [f: 'forget_me' ])
  }


  String getSessionId()         { sessionId }
  String getEmail()             { email }
  String getDomainName()        { domainName }
  String getCurrentSequenceId() { seq }

}
