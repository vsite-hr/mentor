package hr.vsite.mentor.web.places.tokens;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.http.client.URL;

public class TokenParser {

	public TokenParser(String token) {
		
		String[] tokens = token.split("\\" + TokenConstants.FilterMainSeparator, 2);
		
		primaryToken = URL.decode(tokens[0]);
		
		if (tokens.length > 1) {
			filters = new HashMap<String, String>();
			String[] filterComponents = tokens[1].split("\\" + TokenConstants.FilterSeparator, 0);
			for (String filter : filterComponents) {
				String[] keyValue = filter.split("\\" + TokenConstants.FilterKeyValueSeparator, 2);
				if (keyValue.length != 2)
					continue;
				filters.put(URL.decode(keyValue[0]), URL.decode(keyValue[1]));
			}
		} else {
			filters = null;
		}
		
	}
	
	public String getPrimaryToken() { return primaryToken; }
	public String[] getPrimaryTokens() {
		String[] tokens = primaryToken.split("\\" + TokenConstants.PrimaryTokenSeparator);
		for (int i = 0; i < tokens.length; ++i)
			if (tokens[i].equals(TokenConstants.NullToken))
				tokens[i] = null;
		return tokens;
	}
	public Map<String, String> getFilters() { return filters; }

	public final String primaryToken;
	public final Map<String, String> filters;
	
}
