package hr.vsite.mentor.web.places.tokens;

import com.google.gwt.http.client.URL;

public class TokenBuilder {

	public TokenBuilder(String primaryToken) {
		strBuilder = new StringBuilder(URL.encode(primaryToken));
	}
	
	public TokenBuilder(String ... primaryTokens) {
		strBuilder = new StringBuilder();
		for (String primaryToken : primaryTokens) {
			if (strBuilder.length() > 0)
				strBuilder.append(URL.encode(TokenConstants.PrimaryTokenSeparator));
			strBuilder.append(URL.encode(primaryToken != null ? primaryToken : TokenConstants.NullToken));
		}
	}
	
	public TokenBuilder createFilter() {
		strBuilder.append(TokenConstants.FilterMainSeparator);
		return this;
	}
	
	public TokenBuilder appendFilter(String key, String value) {
		if (firstFilter) {
			firstFilter = false;
		} else {
			strBuilder.append(TokenConstants.FilterSeparator);
		}
		strBuilder.append(URL.encode(key));
		strBuilder.append(TokenConstants.FilterKeyValueSeparator);
		strBuilder.append(URL.encode(value));
		return this;
	}

	@Override
	public String toString() {
		return strBuilder.toString();
	}
	
	private StringBuilder strBuilder;
	private boolean firstFilter = true;

}
