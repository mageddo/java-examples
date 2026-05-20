package com.mageddo.vendor.jira.entrypoint;

import com.mageddo.vendor.jira.IssueAppService;
import com.mageddo.vendor.jira.IssueStatusDuration;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@RunOnVirtualThread
@RequiredArgsConstructor
@Tag(name = "vendor-jira-issue")
@Path("/jmx/vendor/jira/issue")
public class IssueJMX {

  private final IssueAppService issueAppService;

  @GET
  @Path("/{issueKey}/status-duration")
  public IssueStatusDuration find(@PathParam("issueKey") String issueKey) {
    return this.issueAppService.find(issueKey);
  }
}
