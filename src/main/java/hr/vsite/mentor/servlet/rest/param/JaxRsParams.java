package hr.vsite.mentor.servlet.rest.param;

public class JaxRsParams {

	private JaxRsParams() {}
	
	public static JaxRsParam forClass(Class<?> clazz) {
		return new JaxRsParamImpl(clazz);
	}

}
