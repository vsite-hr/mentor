package hr.vsite.mentor.servlet.rest;

import javax.ws.rs.ext.ParamConverter;

import com.google.inject.AbstractModule;

import hr.vsite.mentor.course.Course;
import hr.vsite.mentor.lecture.Lecture;
import hr.vsite.mentor.servlet.rest.param.CourseParamConverter;
import hr.vsite.mentor.servlet.rest.param.JaxRsParams;
import hr.vsite.mentor.servlet.rest.param.LectureParamConverter;
import hr.vsite.mentor.servlet.rest.param.UnitParamConverter;
import hr.vsite.mentor.servlet.rest.param.UserParamConverter;
import hr.vsite.mentor.servlet.rest.providers.ExceptionMapperProvider;
import hr.vsite.mentor.servlet.rest.providers.GuiceParamConverterProvider;
import hr.vsite.mentor.servlet.rest.providers.ObjectMapperProvider;
import hr.vsite.mentor.servlet.rest.resources.RootResource;
import hr.vsite.mentor.servlet.rest.resources.UnitResource;
import hr.vsite.mentor.servlet.rest.resources.UserResource;
import hr.vsite.mentor.servlet.rest.resources.CourseResource;
import hr.vsite.mentor.servlet.rest.resources.LectureResource;
import hr.vsite.mentor.unit.Unit;
import hr.vsite.mentor.user.User;

/**
 * Place to register all JAX-RS resources
 */
public class ResourceModule extends AbstractModule {

	@Override 
	protected void configure() {

		// providers
		bind(ObjectMapperProvider.class);
		bind(GuiceParamConverterProvider.class);
		bind(ExceptionMapperProvider.class);
		
		// params
		bind(ParamConverter.class).annotatedWith(JaxRsParams.forClass(User.class)).to(UserParamConverter.class);
		bind(ParamConverter.class).annotatedWith(JaxRsParams.forClass(User.class)).to(UserParamConverter.class);
		bind(ParamConverter.class).annotatedWith(JaxRsParams.forClass(Course.class)).to(CourseParamConverter.class);
		bind(ParamConverter.class).annotatedWith(JaxRsParams.forClass(Lecture.class)).to(LectureParamConverter.class);
		bind(ParamConverter.class).annotatedWith(JaxRsParams.forClass(Unit.class)).to(UnitParamConverter.class);
		
		// resources
		bind(RootResource.class);
		bind(UserResource.class);
		bind(CourseResource.class);
		bind(LectureResource.class);
		bind(UnitResource.class);
		
	}

}
