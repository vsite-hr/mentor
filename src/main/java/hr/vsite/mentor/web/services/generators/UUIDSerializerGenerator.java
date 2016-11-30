package hr.vsite.mentor.web.services.generators;

import org.fusesource.restygwt.rebind.JsonEncoderDecoderClassCreator;
import org.fusesource.restygwt.rebind.JsonEncoderDecoderInstanceLocator;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;

public class UUIDSerializerGenerator extends JsonEncoderDecoderClassCreator {

    public UUIDSerializerGenerator(TreeLogger logger, GeneratorContext context, JClassType source) throws UnableToCompleteException {
        super(logger, context, source);
    }

    @Override
    public void generate() throws UnableToCompleteException {
        locator = new JsonEncoderDecoderInstanceLocator(context, getLogger());
        generateSingleton(shortName);
        generateEncodeMethod();
        generateDecodeMethod();
    }

    private void generateEncodeMethod() throws UnableToCompleteException {
        p("public " + JSON_VALUE_CLASS + " encode(" + source.getQualifiedSourceName() + " value) {").i(1);
            p("if (value == null) {").i(1);
                p("return null;").i(-1);
            p("}");
            p("return new " + JSON_STRING_CLASS + "(value.toString());").i(-1);
        p("}");
        p();
    }

    private void generateDecodeMethod() throws UnableToCompleteException {
        p("public " + source.getQualifiedSourceName() + " decode(" + JSON_VALUE_CLASS + " value) {").i(1);
            p("if (value == null || value.isNull() != null ) {").i(1);
                p("return null;").i(-1);
            p("}");
            p("return java.util.UUID.fromString(value.isString().stringValue());").i(-1);
        p("}");
        p();
    }

}