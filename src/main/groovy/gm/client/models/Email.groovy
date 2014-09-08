package gm.client.models

import groovy.transform.ToString
import groovy.transform.Immutable

@ToString(includeNames = true)
class Email {
  final String mail_id
  final String mail_from
  final String mail_subject
  final String mail_excerpt
  final String mail_timestamp
  final String mail_read
  final String mail_date
  final String mail_recipient
  final String source_id
  final String source_mail_id
  final String reply_to
  final String att

  // TODO: Decompose this into two classes: Email, and EmailPreview
  final String mail_body
  final String ver
  final String ref_mid
  final String content_type
  final String sid_token
}

