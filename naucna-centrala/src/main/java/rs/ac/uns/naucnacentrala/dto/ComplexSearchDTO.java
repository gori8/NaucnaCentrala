package rs.ac.uns.naucnacentrala.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ComplexSearchDTO {

    private List<GroupDTO> groups;

    private String operator;

}
