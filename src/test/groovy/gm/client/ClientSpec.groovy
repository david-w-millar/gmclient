package gm.client

import co.freeside.betamax.*
import org.junit.*
import spock.lang.*

import co.freeside.betamax.httpclient.BetamaxRoutePlanner
import groovyx.net.http.RESTClient

import gm.client.models.*


class ClientSpec extends Specification {

  @Shared Client = new Client()

  //@Rule public Recorder recorder = new Recorder()

  void setupSpec() {
    BetamaxRoutePlanner.configure(client.client.client)
  }


  @Betamax(tape='gm')
  void 'I can get an email address'() {
    when:
    GetEmailAddressResponse emailAddress = client.getEmailAddress()

    then:
    emailAddress.email_addr
  }

  @Betamax(tape='gm')
  void 'I can set my email address'() {
    when:
    SetEmailAddressResponse address = client.setEmailAddress('mytester', 'guerrillamail.com')

    then:
    address.email_addr =~ 'mytester@guerrillamail'
  }


  @Betamax(tape='gm')
  void 'I can list my inbox contents and fetch mail'() {
    when:
    GetEmailListResponse emails = client.getEmailList()

    then:
    emails.list

    when:
    String id = emails.list.first().mail_id
    Email email = client.fetchEmail(id)

    then:
    email.mail_id
  }


  @Betamax(tape='gm')
  void 'I can check for new mail'() {
    when:
    GetEmailListResponse newMails = client.checkEmail()

    then:
    newMails.list != null
  }


  @Betamax(tape='gm')
  void 'I can destroy my session'() {
    expect:
    client.forgetMe()
  }

}
