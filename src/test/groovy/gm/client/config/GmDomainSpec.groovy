package gm.client.config

import groovyx.net.http.*

import spock.lang.*

class GmDomainSpec extends Specification {

  @Unroll
  void "Guerrila mail domain #domain exists" () {
      when:
      RESTClient client = new RESTClient("http://${domain}")
      client.ignoreSSLIssues()

      then:
      client.head([:]).isSuccess()

      where:
      domain << GmDomain.values()*.name
  }

}




