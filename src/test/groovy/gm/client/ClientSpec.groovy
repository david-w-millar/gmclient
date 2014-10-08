package gm.client

import org.junit.*
import spock.lang.*

import co.freeside.betamax.*
import co.freeside.betamax.tape.yaml.OrderedPropertyComparator
import co.freeside.betamax.tape.yaml.TapePropertyUtils
import co.freeside.betamax.httpclient.BetamaxRoutePlanner
import org.yaml.snakeyaml.introspector.Property

import gm.client.models.*

@Stepwise
class ClientSpec extends Specification {

  @Shared GMClient client = new GMClient()
  @Rule Recorder recorder = new Recorder()

  void setupSpec() {
    BetamaxRoutePlanner.configure(this.client.client.client)
    // Workaround for https://github.com/robfletcher/betamax/issues/141
    TapePropertyUtils.metaClass.sort = { Set<Property> properties, List<String> names ->
      new LinkedHashSet(properties.sort( true, new OrderedPropertyComparator(names)) )
    }

  }


  @Betamax(tape='gm')
  void 'I can get an email address'() {
    when:
    GetEmailAddressResponse emailAddress = client.emailAddress

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


  void 'The client has expected attributes' () {
    expect:
    client.sessionId
    client.email
    client.domainName
    client.currentSequenceId
  }


  @Betamax(tape='gm')
  void 'I can list my inbox contents and fetch mail'() {
    when:
    GetEmailListResponse emails = client.emailList

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
