package application;

import java.util.Random;

public class SimulatedAnnealing {

    private static final int DIMENSIONS = 15;
    private static final double LOWER_BOUND = -2;
    private static final double UPPER_BOUND = 2;
    private double temperature = 1000;
    private double coolingRate = 0.95;
    private int maxIterations = 1000;
    private Random rand = new Random();

    public static double rastrigin(double[] x) {
        double A = 10;
        double sum = A * x.length;
        for (int i = 0; i < x.length; i++) {
            sum += x[i] * x[i] - A * Math.cos(2 * Math.PI * x[i]);
        }
        return sum;
    }

    private double[] generateNeighbor(double[] current) {
        double[] neighbor = current.clone();
        int idx = rand.nextInt(DIMENSIONS);
        double delta = (rand.nextDouble() - 0.5) * 0.1;
        neighbor[idx] = Math.min(UPPER_BOUND, Math.max(LOWER_BOUND, neighbor[idx] + delta));
        return neighbor;
    }

    public Result run() {
        long startTime = System.currentTimeMillis();

        double[] current = new double[DIMENSIONS];
        for (int i = 0; i < DIMENSIONS; i++) {
            current[i] = LOWER_BOUND + (UPPER_BOUND - LOWER_BOUND) * rand.nextDouble();
        }

        double[] best = current.clone();
        double bestValue = rastrigin(current);
        double[] convergence = new double[maxIterations];

        for (int i = 0; i < maxIterations; i++) {
            double[] neighbor = generateNeighbor(current);
            double currentValue = rastrigin(current);
            double neighborValue = rastrigin(neighbor);

            if (acceptanceProbability(currentValue, neighborValue, temperature) > rand.nextDouble()) {
                current = neighbor;
            }

            if (rastrigin(current) < bestValue) {
                best = current.clone();
                bestValue = rastrigin(current);
            }

            convergence[i] = bestValue;
            temperature *= coolingRate;
        }

        long endTime = System.currentTimeMillis(); 
        long runtime = endTime - startTime;

        return new Result(best, bestValue, convergence, runtime);
    }

    private double acceptanceProbability(double currentEnergy, double newEnergy, double temp) {
        if (newEnergy < currentEnergy) {
            return 1.0;
        }
        return Math.exp((currentEnergy - newEnergy) / temp);
    }

    public static class Result {
        public double[] solution;
        public double value;
        public double[] convergence;
        public long runtimeMs;

        public Result(double[] solution, double value, double[] convergence, long runtimeMs) {
            this.solution = solution;
            this.value = value;
            this.convergence = convergence;
            this.runtimeMs = runtimeMs;
        }

    }
}
