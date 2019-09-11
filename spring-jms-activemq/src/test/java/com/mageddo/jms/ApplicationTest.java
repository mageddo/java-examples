package com.mageddo.jms;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by elvis on 16/06/17.
 */
@Configuration
@Import(SpringJmsStarter.class)
public class ApplicationTest {}
