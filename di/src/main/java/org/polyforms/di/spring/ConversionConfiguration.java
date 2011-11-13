package org.polyforms.di.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Configuration Bean to register default beans for Spring conversion.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Configuration
@ImportResource("classpath:/org/polyforms/di/spring/module-context.xml")
interface ConversionConfiguration {
}
