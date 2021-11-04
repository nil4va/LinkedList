package nl.hva.ict.ads;

import java.util.Comparator;

public class Archer {
    public static int MAX_ARROWS = 3;
    public static int MAX_ROUNDS = 10;

    private final int id;         // TODO Once assigned a value is not allowed to change.
    private static int archerCreated = 135787;
    private String firstName;
    private String lastName;
    public int[][] pointsPerArrow = new int[MAX_ROUNDS + 1][MAX_ARROWS];

    // TODO add instance variable(s) to track the scores per round per arrow

    /**
     * Constructs a new instance of Archer and assigns a unique id to the instance.
     * Each new instance should be assigned a number that is 1 higher than the last one assigned.
     * The first instance created should have ID 135788;
     *
     * @param firstName the archers first name.
     * @param lastName  the archers surname.
     */
    public Archer(String firstName, String lastName) {
        // TODO initialise the new archer
        //  generate and assign an new unique id
        //  initialise the scores of the archer
        this.id = archerCreated++;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Registers the points for each of the three arrows that have been shot during a round.
     *
     * @param round  the round for which to register the points. First round has number 1.
     * @param points the points shot during the round, one for each arrow.
     */
    public void registerScoreForRound(int round, int[] points) {
        // TODO register the points into the archer's data structure for scores.
        pointsPerArrow[round] = points.clone();
    }


    /**
     * Calculates/retrieves the total score of all arrows across all rounds
     *
     * @return
     */
    public int getTotalScore() {
        // TODO calculate/get the total score that the archer has earned across all arrows of all registered rounds
        int totalPoints = 0;

        for (int[] point : pointsPerArrow) {
            for (int score : point) {
                totalPoints += score;
            }
        }

        return totalPoints;
    }

    /**
     * compares the scores/id of this archer with the scores/id of the other archer according to
     * the scoring scheme: highest total points -> least misses -> earliest registration
     * The archer with the lowest id has registered first
     *
     * @param other the other archer to compare against
     * @return negative number, zero or positive number according to Comparator convention
     */
    public int compareByHighestTotalScoreWithLeastMissesAndLowestId(Archer other) {
        // TODO compares the scores/id of this archer with the other archer
        //  and return the result according to Comparator conventions

        if (this.getTotalScore() < other.getTotalScore()) {
            return 1;
        } else if (this.getTotalScore() > other.getTotalScore()) {
            return -1;
        }

        if (this.getTotalMisses() > other.getTotalMisses()) {
            return 1;
        } else if (this.getTotalMisses()< other.getTotalMisses()) {
            return -1;
        }

        if (this.getId() > other.getId()) {
            return 1;
        } else if (this.getId() < other.getId()) {
            return -1;
        }

        return 0;
    }

    public int getTotalMisses() {
        // TODO calculate/get the total score that the archer has earned across all arrows of all registered rounds
        int totalMisses = 0;

        for (int[] point : pointsPerArrow) {
            for (int score : point) {
                if (score == 0)
                    totalMisses++;
            }
        }

        return totalMisses;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    // TODO provide a toSting implementation to format archers nicely

    @Override
    public String toString() {
        return id + " (" + getTotalScore() + ") " + firstName + " " + lastName;
    }
}
