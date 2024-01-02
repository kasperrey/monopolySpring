package be.kasperreynders.monopoly;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
public class HomeController {
    private HashMap<Integer, Spel> spellen = new HashMap<>();
    private Random random = new Random();

    @GetMapping("/{id}/data")
    public Data home(@PathVariable int id) {
        int idInMap = id;
        if (!spellen.containsKey(id)) {
            int randomInt = random.nextInt(1000000000)+1;
            while (spellen.containsKey(randomInt)) {
                randomInt = random.nextInt(1000000000)+1;
            }
            spellen.put(randomInt, new Spel(randomInt));
            idInMap = randomInt;
        }
        Spel spel = spellen.get(idInMap);
        spel.doeZet();
        return spel.getData();
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.HOURS)
    public void verwijderSpellen() {
        var nu = LocalDate.now();
        for (int key: spellen.keySet()) {
            Spel spel = spellen.get(key);
            if (spel.laatsteZet().plus(1, ChronoUnit.HOURS).isBefore(nu)) {
                spellen.remove(key);
            }
        }
    }
}
