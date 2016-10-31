package hr.vsite.mentor.servlet.rest;

import java.time.Instant;

import javax.ws.rs.ext.ParamConverter;

import com.google.inject.AbstractModule;

import hr.vsite.mentor.servlet.rest.param.InstantParamConverter;
import hr.vsite.mentor.servlet.rest.param.JaxRsParams;
import hr.vsite.mentor.servlet.rest.providers.GuiceParamConverterProvider;
import hr.vsite.mentor.servlet.rest.providers.ObjectMapperProvider;
import hr.vsite.mentor.servlet.rest.resources.RootResource;

/**
 * Place to register all JAX-RS resources
 */
public class ResourceModule extends AbstractModule {

	@Override 
	protected void configure() {

		// providers
		bind(ObjectMapperProvider.class);
		bind(GuiceParamConverterProvider.class);
		
		// params
		bind(ParamConverter.class).annotatedWith(JaxRsParams.forClass(Instant.class)).to(InstantParamConverter.class);
		
		// resources
		bind(RootResource.class);
		
	}

}
