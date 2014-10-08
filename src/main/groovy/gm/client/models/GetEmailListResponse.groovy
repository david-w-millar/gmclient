package gm.client.models

import groovy.transform.ToString

@ToString(includeNames = true)
class GetEmailListResponse {
  final String count
  final String email
  final String alias
  final String ts
  final String sid_token
  final List<Email> list

  List<Email> getEmails() {
    Collections.unmodifiableList(list)
  }
}

