package com.mageddo.okhttp;

import com.mageddo.okhttp.vo.Contributor;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Comparator;
import java.util.List;

public class MoshiOkHttpContributors {

	private static final Moshi MOSHI = new Moshi.Builder().build();
	private static final JsonAdapter<List<Contributor>> CONTRIBUTORS_JSON_ADAPTER = MOSHI.adapter(
		Types.newParameterizedType(List.class, Contributor.class)
	);

	private final String baseURI;

	public MoshiOkHttpContributors() {
		this.baseURI = "https://api.github.com";
	}

	public MoshiOkHttpContributors(String baseURI) {
		this.baseURI = baseURI;
	}

	public List<Contributor> findContributors() {
		OkHttpClient client = new OkHttpClient();

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

		// Execute the request and retrieve the response.
		try (Response response = client.newCall(request).execute()) {
			// Deserialize HTTP response to concrete type.
			List<Contributor> contributors = CONTRIBUTORS_JSON_ADAPTER.fromJson(response.body().source());

			// Sort list by the most contributions.
			contributors.sort(Comparator.comparing(Contributor::getContributions).reversed());

			// Output list of contributors.
			return contributors;
		} catch (IOException e){
			throw new UncheckedIOException(e);
		}
	}

}
