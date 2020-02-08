package rs.ac.uns.naucnacentrala.camunda.types;

import org.camunda.bpm.engine.impl.form.type.AbstractFormFieldType;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.TypedValue;

import java.util.HashMap;
import java.util.Map;

public class MultiSelectionStringFormType extends AbstractFormFieldType {

    private String TYPE_NAME;
    protected Map<String, String> values=new HashMap<>();

    public MultiSelectionStringFormType(String name){
        this.TYPE_NAME=name;
    };

    public MultiSelectionStringFormType(Map<String, String> values) {
        this.values = values;
    }

    @Override
    public String getName() {
        return TYPE_NAME;
    }

    public Object getInformation(String key) {
        return key.equals("values") ? this.values : null;
    }


    public Map<String, String> getValues() {
        return this.values;
    }


    @Override
    public TypedValue convertToFormValue(TypedValue typedValue) {
        return Variables.objectValue(typedValue.getValue(),false).create();
    }

    @Override
    public TypedValue convertToModelValue(TypedValue typedValue) {
        return Variables.objectValue(typedValue.getValue(),false).create();
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
