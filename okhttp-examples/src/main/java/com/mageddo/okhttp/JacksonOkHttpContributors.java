package com.mageddo.okhttp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mageddo.okhttp.vo.Contributor;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class JacksonOkHttpContributors {

	public static final ObjectMapper PARSER = new ObjectMapper();
	private final String baseURI;

	public JacksonOkHttpContributors() {
		this.baseURI = "https://api.github.com";
	}

	public JacksonOkHttpContributors(String baseURI) {
		this.baseURI = baseURI;
	}

	public List<Contributor> findContributors() {
		OkHttpClient client = createDefaultHttpClient();

		// Create request for remote resource.
		Request request = new Request.Builder()
			.url(
				HttpUrl
					.parse(baseURI)
					.newBuilder()
					.addPathSegments("repos/square/okhttp/contributors")
					.build()
			)
			.build();


		try (
			Response response = client
				.newBuilder()
//				.readTimeout(1, TimeUnit.MILLISECONDS)
				.build()
				.newCall(request)
				.execute()
		) {
			// Deserialize HTTP response to concrete type.
			List<Contributor> contributors = PARSER.readValue(
				response.body().source().inputStream(),
				new TypeReference<List<Contributor>>(){}
			);

			// Sort list by the most contributions.
			contributors.sort(Comparator.comparing(Contributor::getContributions).reversed());

			// Output list of contributors.
			return contributors;
		} catch (IOException e){
			throw new UncheckedIOException(e);
		}
	}

	@NotNull
	private OkHttpClient createDefaultHttpClient() {
		return new OkHttpClient
			.Builder()
			.connectTimeout(1, TimeUnit.SECONDS)
			.callTimeout(1, TimeUnit.SECONDS)
			.readTimeout(3, TimeUnit.SECONDS)
			.writeTimeout(3, TimeUnit.SECONDS)
			.addInterceptor(chain -> {
				System.out.println("interceptor called");
				return chain.proceed(chain.request());
			})
			.connectionPool(new ConnectionPool(5, 3, TimeUnit.MINUTES))
			.build();
	}

}
