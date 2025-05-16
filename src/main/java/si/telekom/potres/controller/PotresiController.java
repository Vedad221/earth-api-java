package si.telekom.potres.controller;

import si.telekom.potres.model.Potres;
import si.telekom.potres.model.Vreme;
import si.telekom.potres.service.PotresiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import si.telekom.potres.service.VremeService;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class PotresiController {
    private final PotresiService potresiService;


    @Autowired
    public PotresiController(PotresiService potresiService, VremeService vremeService) {
        this.potresiService = potresiService;


    }

    private final AtomicInteger stevec = new AtomicInteger(0);
    private final AtomicLong totalOdgovor = new AtomicLong(0);

    @GetMapping("/potresi/rekordi/mesecni")
    public Potres mesec() {
        long startVreme = System.currentTimeMillis();
        stevec.incrementAndGet();

        try {
            Potres rt;
            rt = potresiService.najdiZadnjiMesec();
            return rt;
        }
        finally {
            long odgovor= System.currentTimeMillis() - startVreme;
            totalOdgovor.addAndGet(odgovor);

        }
    }

    @GetMapping("/potresi/rekordi/tedenski")
    public Potres teden() {
        long startVreme = System.currentTimeMillis();
        stevec.incrementAndGet();


        try {
            Potres rt;
            rt = potresiService.najdiZadnjiTeden();
            return rt;
        }
        finally {
            long odgovor= System.currentTimeMillis() - startVreme;
            totalOdgovor.addAndGet(odgovor);
        }

    }
    @GetMapping("/potresi/zadnji")
    public Potres zadnji(){
        long startVreme = System.currentTimeMillis();
        stevec.incrementAndGet();


        try {
            Potres rt;
            rt = potresiService.najdiZadnji();
            return rt;
        }
        finally {
            long odgovor= System.currentTimeMillis() - startVreme;
            totalOdgovor.addAndGet(odgovor);

        }

    }
//    @GetMapping("/test")
//    public Vreme zadnji2(){
//
//        Vreme rt;
//        rt = vremeService.pridobiVreme(19.0566673278809,-155.468826293945);
//        return rt;
//
//    }
    @GetMapping("/statistika")
    public String getStatistika(){
        int totalKlic=stevec.get();
        long povprecje=totalKlic>0 ? totalOdgovor.get() / totalKlic: 0;
        return String.format("Statistika API klicov:\n" +
                "Stevilo klicov: %d\n" +
                "Povrecni cas odziva: %d ms", totalKlic, povprecje);
    }


}
