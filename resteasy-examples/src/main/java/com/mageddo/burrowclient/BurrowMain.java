package com.mageddo.burrowclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mageddo.jackson.JsonUtils;
import com.mageddo.resteasy.RestEasy;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BurrowMain {
	public static void main(String[] args) throws IOException {
		WebTarget target = RestEasy.newClient().target("http://kafka.com/v3/kafka/local/consumer/clientId-123/lag");

		final Response out = target.request().get();


		JsonNode m = JsonUtils.instance.readValue(out.readEntity(String.class), JsonNode.class);
		ArrayNode partions = (ArrayNode) m.at("/status/partitions");


		Map<String, Integer> topicStatus = new HashMap<>();

		for (Object partition : partions) {
			ObjectNode partitionMap = (ObjectNode) partition;
				String topic = partitionMap.path("topic").asText();
			if(!topicStatus.containsKey(topic)){
				topicStatus.put(topic, 0);
			}

			Integer actualValue = topicStatus.get(topic);
			topicStatus.put(topic, actualValue + partitionMap.path("current_lag").asInt());

		}


		System.out.println(new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(topicStatus));
	}
}
