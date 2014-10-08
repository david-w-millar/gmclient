package gm.client.config

/**
 * GuerrillaMail supports these domains
 */
@SuppressWarnings('SerializableClassMustDefineSerialVersionUID')
enum GmDomain {

  guerrillamail_com('guerrillamail.com'),
  guerrillamailblock_com('guerrillamailblock.com'),
  sharklasers_com('sharklasers.com'),
  guerrillamail_net('guerrillamail.net'),
  guerrillamail_org('guerrillamail.org'),
  guerrillamail_biz('guerrillamail.biz'),
  spam4_me('spam4.me'),
  grr_la('grr.la'),
  guerrillamail_de('guerrillamail.de')

  private final String name

  GmDomain(final String domainName) {
    name = domainName
  }

}
