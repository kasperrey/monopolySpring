package be.kasperreynders.monopoly;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
public class HomeController {
    private final HashMap<Integer, Spel> spellen = new HashMap<>();
    private final Random random = new Random();

    @GetMapping("/{id}/data")
    public Data home(@PathVariable int id) {
        Spel spel = getSpel(id);
        return spel.getData();
    }

    @GetMapping("/{id}/keuzes")
    public KeuzesData kopen(@PathVariable int id) {
        return getSpel(id).keuzes();
    }

    @PostMapping("/{id}/zet")
    public Data doeZet(@PathVariable int id, @RequestBody KeuzeBevestigd keuzeBevestigd) {
        Spel spel = getSpel(id);
        spel.zet(keuzeBevestigd);
        return spel.getData();
    }

    private Spel getSpel(int id) {
        int idInMap = id;
        if (!spellen.containsKey(id)) {
            int randomInt = random.nextInt(1000000000)+1;
            while (spellen.containsKey(randomInt)) {
                randomInt = random.nextInt(1000000000)+1;
            }
            spellen.put(randomInt, new Spel(randomInt));
            idInMap = randomInt;
        }
        return spellen.get(idInMap);
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.HOURS)
    public void verwijderSpellen() {
        LocalDate nu = LocalDate.now();
        for (int key: spellen.keySet()) {
            Spel spel = spellen.get(key);
            if (spel.laatsteZet().plus(1, ChronoUnit.HOURS).isBefore(nu)) {
                spellen.remove(key);
            }
        }
    }
}
