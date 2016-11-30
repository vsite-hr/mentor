package hr.vsite.mentor.web.places.tokens;

import com.google.gwt.i18n.client.DateTimeFormat;

public class TokenConstants {

	public static final String PrimaryTokenSeparator		= "&";	// separates primary tokens in main part
	public static final String FilterMainSeparator			= "?";	// separates filter (key=value) parts from main part
	public static final String FilterSeparator				= ";";	// separates two filters
	public static final String FilterKeyValueSeparator		= "=";	// separates keys and value in filter
	public static final String FilterValueListSeparator		= ",";	// separates multiple values in value filter part (collections as values)
	public static final String FilterValueRangeSeparator	= "~";	// separates value ranges filter part

	public static final String NullToken					= "*";	// symbol used when token value is null (but token must be present, usually primary token)

	public static final DateTimeFormat DateFormat = DateTimeFormat.getFormat("dd-MM-yyyy");
	public static final DateTimeFormat DateAndTimeFormat = DateTimeFormat.getFormat("dd-MM-yyyy-HH-mm-ss");
	public static final DateTimeFormat HumanDateFormat = DateTimeFormat.getFormat("EE, dd.MM.yyyy.");

}
