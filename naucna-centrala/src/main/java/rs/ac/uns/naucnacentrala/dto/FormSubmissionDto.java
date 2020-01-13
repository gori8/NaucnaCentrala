package rs.ac.uns.naucnacentrala.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FormSubmissionDto implements Serializable{
	
	String fieldId;
	Object fieldValue;
	
	
	public FormSubmissionDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FormSubmissionDto(String fieldId, String fieldValue) {
		super();
		this.fieldId = fieldId;
		this.fieldValue = fieldValue;
	}

	
	
}
