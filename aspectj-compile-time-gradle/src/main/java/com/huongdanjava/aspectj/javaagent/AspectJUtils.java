package com.huongdanjava.aspectj.javaagent;

import com.ea.agentloader.AgentLoader;

import java.util.Objects;

public class AspectJUtils {
	public static void loadAgent(){
		if(Objects.equals(System.getProperty("jdk.attach.allowAttachSelf"), "true")){
			System.out.println("> loadtime proxing");
			AgentLoader.loadAgentClass("org.aspectj.weaver.loadtime.Agent", "");
		}
	}
}
