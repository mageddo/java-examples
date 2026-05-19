package com.mageddo.vendor.jira.jmx;

import com.mageddo.vendor.jira.apiclient.MyselfApiClient;
import com.mageddo.vendor.jira.apiclient.MyselfRes;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@RunOnVirtualThread
@RequiredArgsConstructor
@Tag(name = "vendor-jira-myself")
@Path("/jmx/vendor/jira/myself")
public class MyselfJMX {

  private final MyselfApiClient myselfApiClient;

  @GET
  public MyselfRes find() {
    return this.myselfApiClient.find();
  }
}
