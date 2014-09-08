package gm.client.models

import groovy.transform.ToString
import groovy.transform.Immutable

@ToString(includeNames = true)
class GetEmailAddressResponse {
  final String email_addr
  final String email_timestamp
  final String alias
  final String sid_token

  String getEmailName() {
    if( email_addr?.contains('@') )
      email_addr.split('@')[0]
    else
      ''
  }
}

