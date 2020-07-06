package rs.ac.uns.naucnacentrala.elasticsearch.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;
import rs.ac.uns.naucnacentrala.model.Casopis;

import javax.persistence.*;

@Document(indexName = "naucni_rad")
@Setting(settingPath = "/essettings/serbiananalyzer.json")
@Getter
@Setter
@NoArgsConstructor
public class ESPaper {

    @Id
    private String id;

    private String naslov;

    private String apstrakt;

    private String kljucniPojmovi;

    private String sadrzaj;



}
