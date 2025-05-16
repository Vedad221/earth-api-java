package si.telekom.potres.model;

import lombok.*;

@Data
public final class Potres {
    @Setter
    private Vreme vreme;
    private final String najblizjiKraj;
    private final String geoLokacija;
    private final double globina;

    public Potres( String najblizjiKraj, String geoLokacija, double globina) {
        this.najblizjiKraj = najblizjiKraj;
        this.geoLokacija = geoLokacija;
        this.globina = globina;
    }


}

