package rs.ac.uns.naucnacentrala.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SearchResultDTO {

    private String naslov;

    private String autori;

    private String naucnaOblast;

    private String filepath;

    private List<SazetakDTO> dynamicHighlights;

}
