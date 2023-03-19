package com.mageddo.os.tools.udpserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.time.LocalDateTime;

public class Main {

	public static void main(String[] args) throws IOException {

		final DatagramSocket server = new DatagramSocket(getPort(args));
		log(String.format("starting at port :%d", getPort(args)));

		final byte[] buff = new byte[getBufferSize(args)];
		while (!server.isClosed() && !Thread.currentThread().isInterrupted()){
			final DatagramPacket packet = new DatagramPacket(buff, 0, buff.length);
			server.receive(packet);
			log(new String(packet.getData(), 0, packet.getLength()));
		}
		System.out.println("Bye :)");
	}

	static int getPort(String[] args) {
		return args.length == 0 ? 3333 : Integer.parseInt(args[0]);
	}

	static int getBufferSize(String[] args){
		return args.length == 2 ? Integer.parseInt(args[1]) : 1024;
	}

	static void log(String line){
		System.out.printf("%s - %s%n", LocalDateTime.now(), line);
	}
}
