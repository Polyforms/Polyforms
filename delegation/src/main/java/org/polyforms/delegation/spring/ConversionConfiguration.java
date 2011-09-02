package org.polyforms.delegation.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:/org/polyforms/delegation/spring/module-context.xml")
interface ConversionConfiguration {
}
