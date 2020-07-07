package rs.ac.uns.naucnacentrala.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class GroupDTO {

    private List<ElementDTO> elements;

    private String type;

}
