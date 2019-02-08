package com.avob.openadr.server.oadr20b.ven;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class VenPushModelCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		VenConfig config = null;
		try {
			config = (VenConfig) context.getBeanFactory().getBean("venConfig");
			return !config.getPullModel();
		} catch (NoSuchBeanDefinitionException ex) {

		}
		return true;
	}

}
