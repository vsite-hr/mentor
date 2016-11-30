package hr.vsite.mentor.web.services.generators;

import java.util.UUID;

import org.fusesource.restygwt.rebind.JsonEncoderDecoderClassCreator;
import org.fusesource.restygwt.rebind.RestyJsonSerializerGenerator;

import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;

public class UUIDRestySerializerGenerator implements RestyJsonSerializerGenerator {

    @Override
    public Class<? extends JsonEncoderDecoderClassCreator> getGeneratorClass() {
        return UUIDSerializerGenerator.class;
    }

    @Override
    public JType getType(TypeOracle typeOracle) {
        return typeOracle.findType(UUID.class.getName());
    }

}