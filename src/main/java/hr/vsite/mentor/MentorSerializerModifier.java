package hr.vsite.mentor;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import hr.vsite.mentor.unit.TextUnit;

public class MentorSerializerModifier extends BeanSerializerModifier {

	@SuppressWarnings("unchecked")
	@Override
    public JsonSerializer<?> modifySerializer(SerializationConfig serializationConfig, BeanDescription beanDescription, JsonSerializer<?> jsonSerializer) {
	
		if (beanDescription.getBeanClass().equals(TextUnit.class))
			return new TextUnit.Serializer((JsonSerializer<TextUnit>) jsonSerializer);
		
		return jsonSerializer;
		
    }

}
