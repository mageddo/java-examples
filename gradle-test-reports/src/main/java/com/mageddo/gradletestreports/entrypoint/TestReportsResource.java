package com.mageddo.gradletestreports.entrypoint;

import java.nio.file.Path;
import java.util.List;

import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import com.mageddo.gradletestreports.TestReportsService;

import io.smallrye.common.annotation.RunOnVirtualThread;
import lombok.RequiredArgsConstructor;

@Singleton
@RunOnVirtualThread
@RequiredArgsConstructor
@jakarta.ws.rs.Path("/api")
public class TestReportsResource {

  private final TestReportsService testReportsService;
  private final TestReportsMapper mapper;

  @GET
  @jakarta.ws.rs.Path("/projects")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ProjectRes> projects(@QueryParam("dir") String dir) {
    final var projects = this.testReportsService.findProjects(Path.of(dir));
    return this.mapper.toProjectRes(projects);
  }

  @GET
  @jakarta.ws.rs.Path("/results")
  @Produces(MediaType.APPLICATION_JSON)
  public List<TestResultSetRes> results(@QueryParam("project") String project) {
    final var resultSets = this.testReportsService.findResultSets(Path.of(project));
    return this.mapper.toResultSetRes(resultSets);
  }

  @GET
  @jakarta.ws.rs.Path("/tests")
  @Produces(MediaType.APPLICATION_JSON)
  public List<TestCaseRes> tests(
      @QueryParam("project") String project,
      @QueryParam("result") String result
  ) {
    final var cases = this.testReportsService.findTestCases(Path.of(project), result);
    return this.mapper.toTestCaseRes(cases);
  }
}
