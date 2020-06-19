package com.mageddo.jvmti;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mageddo.jvmti.classdelegate.InstanceId;
import com.mageddo.jvmti.classdelegate.scanning.InstanceFilter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Slf4j
@Singleton
public class RemoteClassInstanceService implements ClassInstanceService {

  private final ObjectMapper objectMapper;
  private final OkHttpClient client;
  private final HttpUrl baseUri;

  @Inject
  public RemoteClassInstanceService(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    this.client = new OkHttpClient();
    this.baseUri = HttpUrl.parse("http://localhost:8384");
  }

  @Override
  public InstanceValue getFieldValue(InstanceId id, FieldId fieldId) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setFieldValue(InstanceId id, FieldId fieldId, InstanceValue value) {
    log.info("status=setFieldValue, id={}, field={}, instance={}", id, fieldId, value);
  }

  @Override
  public InstanceValue methodInvoke(InstanceId id, String name, List<InstanceValue> args) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int filter(InstanceFilter filter) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int scanInstances(ClassId classId) {
    throw new UnsupportedOperationException();
  }

  @SneakyThrows
  @Override
  public List<InstanceValue> scanAndGetValues(ClassId classId) {
    final Response res = this.client
      .newCall(new Request
        .Builder()
        .get()
        .url(this.baseUri
          .newBuilder()
          .addQueryParameter("class", classId.getClassName())
          .build()
        )
        .build()
      )
      .execute();
    return this.objectMapper.readValue(res.body().string(), new TypeReference<List<InstanceValue>>(){});
  }
}
