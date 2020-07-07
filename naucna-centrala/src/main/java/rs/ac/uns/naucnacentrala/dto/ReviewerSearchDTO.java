package rs.ac.uns.naucnacentrala.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewerSearchDTO {

    private boolean geoLoc;
    private boolean moreLikeThis;
    private String taskId;

}
