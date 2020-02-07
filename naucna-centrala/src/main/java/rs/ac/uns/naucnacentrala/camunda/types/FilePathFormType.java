package rs.ac.uns.naucnacentrala.camunda.types;


import org.camunda.bpm.engine.impl.form.type.AbstractFormFieldType;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.impl.value.PrimitiveTypeValueImpl;
import org.camunda.bpm.engine.variable.value.TypedValue;

public class FilePathFormType extends AbstractFormFieldType {

    private String TYPE_NAME;

    public FilePathFormType(String name){
        this.TYPE_NAME=name;
    }

    @Override
    public String getName() {
        return TYPE_NAME;
    }

    @Override
    public TypedValue convertToFormValue(TypedValue typedValue) {
        return typedValue;
    }

    @Override
    public TypedValue convertToModelValue(TypedValue typedValue) {
        return typedValue;
    }

    @Override
    public Object convertFormValueToModelValue(Object o) {
        return null;
    }

    @Override
    public String convertModelValueToFormValue(Object o) {
        return null;
    }
}
