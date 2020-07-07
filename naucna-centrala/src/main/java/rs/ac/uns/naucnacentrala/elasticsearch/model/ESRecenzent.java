package rs.ac.uns.naucnacentrala.elasticsearch.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Getter
@Setter
@NoArgsConstructor
public class ESRecenzent {

    private String username;

    @GeoPointField
    private GeoPoint location;

}
