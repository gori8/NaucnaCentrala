package rs.ac.uns.naucnacentrala.camunda.types;

import org.camunda.bpm.engine.impl.form.type.AbstractFormFieldType;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.spin.plugin.variable.SpinValues;
import org.camunda.spin.plugin.variable.value.JsonValue;
import org.camunda.spin.plugin.variable.value.SpinValue;

import java.util.HashMap;
import java.util.Map;

public class AddChildrenFormType extends AbstractFormFieldType {

    private String TYPE_NAME;

    public AddChildrenFormType(String name){
        this.TYPE_NAME=name;
    };


    @Override
    public String getName() {
        return TYPE_NAME;
    }



    @Override
    public TypedValue convertToFormValue(TypedValue typedValue) {
        if(typedValue.getValue()!=null) {
            JsonValue retValue = SpinValues.jsonValue(typedValue.getValue().toString()).create();
            return retValue;
        }else{
            return typedValue;
        }
    }

    @Override
    public TypedValue convertToModelValue(TypedValue typedValue) {
        if(typedValue.getValue()!=null) {
            JsonValue retValue = SpinValues.jsonValue(typedValue.getValue().toString()).create();
            return retValue;
        }else{
            return typedValue;
        }
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
