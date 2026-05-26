import java.util.List;
import java.util.Random;

public class Selection {

    private static Random rand = new Random();

    public static Individual tournament(List<Individual> population, int tournamentSize) {
        Individual best = null;

        for(int i = 0; i < tournamentSize; i++) {

            Individual candidate = population.get(rand.nextInt(population.size()));

            if(best == null || candidate.fitness > best.fitness) {
                best = candidate;
            }
        }

        return best;
    }
}