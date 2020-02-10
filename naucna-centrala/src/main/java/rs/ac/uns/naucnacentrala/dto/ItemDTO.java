package rs.ac.uns.naucnacentrala.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
    private String naziv;
    private List<Long> naciniPlacanja;
    private BigDecimal amount;
    private String redirectUrl;
    private String email;
}
