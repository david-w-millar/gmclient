package gm.client.models

import groovy.transform.ToString
import groovy.transform.Immutable

@ToString(includeNames = true)
class GetEmailListResponse {
  final String count
  final String email
  final String alias
  final String ts
  final String sid_token
  final List<Email> list
  final Map stats

  List<Email> getEmails() {
    Collections.unmodifiableList(list)
  }

  Map getStats() {
    Collections.unmodifiableMap(stats)
  }
}

