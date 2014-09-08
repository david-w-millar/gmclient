package gm.client.models

import groovy.transform.ToString
import groovy.transform.Immutable

@ToString(includeNames = true)
class SetEmailAddressResponse {
  final String d
  final String alias_error
  final String alias
  final String email_addr
  final String email_timestamp
  final int site_id
  final int domain_id
  final String sid_token
}

