//package com.mageddo.jms.queue;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.activemq.command.ActiveMQBytesMessage;
//import org.apache.activemq.command.ActiveMQObjectMessage;
//import org.apache.activemq.command.ActiveMQTextMessage;
//import org.apache.commons.io.IOUtils;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import java.io.DataInputStream;
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//
///**
// * Created by elvis on 15/06/17.
// */
//
//@RunWith(MockitoJUnitRunner.class)
//public class JsonConverterTest {
//
//	@InjectMocks
//	private JsonConverter jsonConverter;
//
//	@Before
//	public void before(){
//		jsonConverter.objectMapper = new ObjectMapper();
//	}
//
//	@Test
//	public void readValue_Class_FromTextMessageSuccess() throws Exception {
//
//		final ActiveMQTextMessage textMessage = new ActiveMQTextMessage();
//		textMessage.setText("{\"id\": 1, \"name\": \"Elvis\"}");
//
//		final PersonVO map = jsonConverter.readValue(textMessage, PersonVO.class);
//		assertEquals(1, map.getId());
//		assertEquals("Elvis", map.getName());
//	}
//
//	@Test
//	public void readValue_Class_FromObjectMessageSuccess() throws Exception {
//
//		final ActiveMQObjectMessage message = new ActiveMQObjectMessage();
//		message.setObject("{\"id\": 1, \"name\": \"Elvis\"}");
//
//		final PersonVO map = jsonConverter.readValue(message, PersonVO.class);
//		assertEquals(1, map.getId());
//		assertEquals("Elvis", map.getName());
//	}
//
//	@Test
//	public void readValue_Class_FromBytesMessageSuccess() throws Exception {
//
//		final ActiveMQBytesMessage message = new ActiveMQBytesMessage(){
//			{
//				dataIn = new DataInputStream(IOUtils.toInputStream("{\"id\": 1, \"name\": \"Elvis\"}", "UTF-8"));
//			}
//		};
//		message.setReadOnlyBody(true);
//
//		final PersonVO map = jsonConverter.readValue(message, PersonVO.class);
//		assertEquals(1, map.getId());
//		assertEquals("Elvis", map.getName());
//	}
//
//	@Test
//	public void readValue_Type_FromBytesMessageSuccess() throws Exception {
//		final ActiveMQBytesMessage message = new ActiveMQBytesMessage(){
//			{
//				dataIn = new DataInputStream(IOUtils.toInputStream("[{\"id\": 1, \"name\": \"Elvis\"}]", "UTF-8"));
//			}
//		};
//		message.setReadOnlyBody(true);
//
//		final List<PersonVO> persons = jsonConverter.readValue(message, new TypeReference<List<PersonVO>>() {});
//		assertEquals(1, persons.get(0).getId());
//		assertEquals("Elvis", persons.get(0).getName());
//	}
//
//	public static class PersonVO {
//
//		private int id;
//		private String name;
//
//		public int getId() {
//			return id;
//		}
//
//		public void setId(int id) {
//			this.id = id;
//		}
//
//		public String getName() {
//			return name;
//		}
//
//		public void setName(String name) {
//			this.name = name;
//		}
//	}
//
//
//}
