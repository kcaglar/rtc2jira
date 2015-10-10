/*
 * Copyright (c) 2015 BISON Schweiz AG, All Rights Reserved.
 */
package to.rtc.rtc2jira.exporter.jira.mapping;

import to.rtc.rtc2jira.exporter.jira.entities.Issue;
import to.rtc.rtc2jira.storage.StorageEngine;

/**
 * @author roman.schaller
 *
 */
public class DescriptionMapping implements Mapping {

  @Override
  public void map(Object value, Issue issue, StorageEngine storage) {
    String description = (String) value;
    description = description.replaceAll("<br/>", "\r\n");
    issue.getFields().setDescription(description);
  }
}
