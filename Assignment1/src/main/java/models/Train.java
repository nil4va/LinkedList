package models;

public class Train {
    private String origin;
    private String destination;
    private Locomotive engine;
    private Wagon firstWagon;

    /* Representation invariants:
        firstWagon == null || firstWagon.previousWagon == null
        engine != null
     */

    public Train(Locomotive engine, String origin, String destination) {
        this.engine = engine;
        this.destination = destination;
        this.origin = origin;
    }

    public boolean hasWagons() {
        return this.firstWagon != null;
    }

    public boolean isPassengerTrain() {
        if (!this.hasWagons()) {
            return false;
        }

        return this.firstWagon instanceof PassengerWagon;
    }

    public boolean isFreightTrain() {
        if (!this.hasWagons()) {
            return false;
        }

        return this.firstWagon instanceof FreightWagon;
    }

    public Locomotive getEngine() {
        return engine;
    }

    public Wagon getFirstWagon() {
        return firstWagon;
    }

    /**
     * Replaces the current sequence of wagons (if any) in the train
     * by the given new sequence of wagons (if any)
     * (sustaining all representation invariants)
     * @param wagon the first wagon of a sequence of wagons to be attached
     */
    public void setFirstWagon(Wagon wagon) {
        this.firstWagon = wagon;
    }

    public int getNumberOfWagons() {
        Wagon wagon = this.firstWagon;
        if (wagon == null) {
            return 0;
        }
        int i = 1; // Start by one so the firstWagon is also counted.
        while (wagon.hasNextWagon()) {
            wagon = wagon.getNextWagon();
            i++;
        }
        return i;
    }

    public Wagon getLastWagonAttached() {
        Wagon wagon = this.firstWagon;
        if (wagon == null) {
            return null;
        }

        while (wagon.hasNextWagon()) {
            wagon = wagon.getNextWagon();
        }

        return wagon;
    }

    /**
     * @return  the total number of seats on a passenger train
     *          (return 0 for a freight train)
     */
    public int getTotalNumberOfSeats() {
        int seats = 0;

        if (!this.isPassengerTrain()) {
            return seats;
        }

        Wagon wagon = this.firstWagon;
        if (wagon == null) {
            return seats;
        }

        for (int i = 0; i < getNumberOfWagons(); i++) {
            seats += ((PassengerWagon) wagon).getNumberOfSeats();
            wagon = wagon.getNextWagon();
        }
        return seats;
    }

    /**
     * calculates the total maximum weight of a freight train
     * @return  the total maximum weight of a freight train
     *          (return 0 for a passenger train)
     *
     */
    public int getTotalMaxWeight() {
        int weight = 0;

        if (!this.isFreightTrain()) {
            return weight;
        }

        Wagon wagon = this.firstWagon;
        if (wagon == null) {
            return weight;
        }

        for (int i = 0; i < getNumberOfWagons(); i++) {
            weight += ((FreightWagon) wagon).getMaxWeight();
            wagon = wagon.getNextWagon();
        }
        return weight;
    }

     /**
     * Finds the wagon at the given position (starting at 1 for the first wagon of the train)
     * @param position
     * @return  the wagon found at the given position
     *          (return null if the position is not valid for this train)
     */
    public Wagon findWagonAtPosition(int position) {
        Wagon wagon = this.firstWagon;
        if (position == 1 || wagon == null) {
            return wagon;
        }

        for (int i = 2; i <= position; i++) {
            if (!wagon.hasNextWagon()) {
                return null;
            }
            wagon = wagon.getNextWagon();
            if (i == position) {
                return wagon;
            }
        }
        return null;
    }

    /**
     * Finds the wagon with a given wagonId
     * @param wagonId
     * @return  the wagon found
     *          (return null if no wagon was found with the given wagonId)
     */
    public Wagon findWagonById(int wagonId) {
        Wagon wagon = this.firstWagon;
        if (wagon == null) {
            return null;
        }

        for (int i = 0; i < getNumberOfWagons(); i++) {
            if (wagon.getId() == wagonId) {
                return wagon;
            }
            wagon = wagon.getNextWagon();
        }

        return null;
    }

    /**
     * Determines if the given sequence of wagons can be attached to the train
     * Verfies of the type of wagons match the type of train (Passenger or Freight)
     * Verfies that the capacity of the engine is sufficient to pull the additional wagons
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return
     */
    public boolean canAttach(Wagon wagon) {
        if (wagon == firstWagon) {
            return false;
        }

        if (isFreightTrain() && !(wagon instanceof FreightWagon)){
            return false;
        }

        if (isPassengerTrain() && !(wagon instanceof PassengerWagon)){
            return false;
        }

        return getNumberOfWagons() < engine.getMaxWagons();
    }

    /**
     * Tries to attach the given sequence of wagons to the rear of the train
     * No change is made if the attachment cannot be made.
     * (when the sequence is not compatible or the engine has insufficient capacity)
     * if attachment is possible, the head wagon is first detached from its predecessors
     * @param wagon  the first wagon of a sequence of wagons to be attached
     * @return  whether the attachment could be completed successfully
     */
    public boolean attachToRear(Wagon wagon) {
        if (canAttach(wagon)) {
            if (firstWagon == null) {
                if (wagon.hasPreviousWagon()) {
                    wagon.getPreviousWagon().setNextWagon(null);
                    wagon.setPreviousWagon(null);
                }
                firstWagon = wagon;
                return true;
            }

            getLastWagonAttached().attachTail(wagon);
            return true;
        }
        return false;
    }


    /**
     * Tries to insert the given sequence of wagons at the front of the train
     * No change is made if the insertion cannot be made.
     * (when the sequence is not compatible or the engine has insufficient capacity)
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return  whether the insertion could be completed successfully
     */
    public boolean insertAtFront(Wagon wagon) {
        if (canAttach(wagon)) {
            Wagon lastWagon = wagon;
            while(lastWagon.hasNextWagon()) {
                lastWagon = lastWagon.getNextWagon();
            }

            if (firstWagon != null) {
                firstWagon.setPreviousWagon(lastWagon);
            }
            lastWagon.setNextWagon(firstWagon);

            firstWagon = wagon;

            return true;
        }
        return false;
    }

    /**
     * Tries to insert the given sequence of wagons at/before the given wagon position in the train
     * No change is made if the insertion cannot be made.
     * (when the sequence is not compatible of the engine has insufficient capacity
     * or the given position is not valid in this train)
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return  whether the insertion could be completed successfully
     */
    public boolean insertAtPosition(int position, Wagon wagon) {
        if (!canAttach(wagon)) {
            return false;
        }

        Wagon currentWagon = findWagonAtPosition(position);
        if (currentWagon == null) {
            if (position == 1) {
                firstWagon = wagon;
                return true;
            }
            return false;
        }

        Wagon lastWagonOfInserted = wagon.getLastWagonAttached();
        lastWagonOfInserted.setNextWagon(currentWagon);

        System.out.println("hoi");
        currentWagon.getPreviousWagon().setNextWagon(wagon);
        currentWagon.setPreviousWagon(wagon);
        return true;
    }

    /**
     * Tries to remove one Wagon with the given wagonId from this train
     * and attach it at the rear of the given toTrain
     * No change is made if the removal or attachment cannot be made
     * (when the wagon cannot be found, or the trains are not compatible
     * or the engine of toTrain has insufficient capacity)
     * @param wagonId
     * @param toTrain
     * @return  whether the move could be completed successfully
     */
    public boolean moveOneWagon(int wagonId, Train toTrain) {
        Wagon wagonToMove = findWagonById(wagonId);
        if (wagonToMove == null || !toTrain.canAttach(wagonToMove)) {
            return false;
        }

        Wagon nextWagon = wagonToMove.getNextWagon();
        Wagon previousWagon = wagonToMove.getPreviousWagon();

        if (wagonToMove.hasPreviousWagon()) {
            if (nextWagon != null) {
                nextWagon.setPreviousWagon(previousWagon);
            }
            if (previousWagon != null) {
                previousWagon.setNextWagon(nextWagon);
            }
        } else {
            if (nextWagon != null) {
                nextWagon.setPreviousWagon(null);
            }
            setFirstWagon(nextWagon);
        }

        wagonToMove.setNextWagon(null);
        wagonToMove.setPreviousWagon(null);

        toTrain.attachToRear(wagonToMove);
        return true;
     }

    /**
     * Tries to split this train before the given position and move the complete sequence
     * of wagons from the given position to the rear of toTrain.
     * No change is made if the split or re-attachment cannot be made
     * (when the position is not valid for this train, or the trains are not compatible
     * or the engine of toTrain has insufficient capacity)
     * @param position
     * @param toTrain
     * @return  whether the move could be completed successfully
     */
    public boolean splitAtPosition(int position, Train toTrain) {
        Wagon currentWagon = findWagonAtPosition(position);
        if (currentWagon == null || !toTrain.canAttach(currentWagon)) {
            return false;
        }

        if (position == 1) {
            firstWagon = null;
        }

        if (currentWagon.hasPreviousWagon()) {
            currentWagon.getPreviousWagon().setNextWagon(null);
        }
        currentWagon.setPreviousWagon(null);
        toTrain.attachToRear(currentWagon);
        return true;
    }

    /**
     * Reverses the sequence of wagons in this train (if any)
     * i.e. the last wagon becomes the first wagon
     *      the previous wagon of the last wagon becomes the second wagon
     *      etc.
     * (No change if the train has no wagons or only one wagon)
     */
    public void reverse() {
        if (getNumberOfWagons() <= 1) {
            return;
        }

        setFirstWagon(firstWagon.reverseSequence());
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[Loc-").append(engine.getLocNumber()).append("]");

        int numberOfWagons = getNumberOfWagons();
        Wagon wagon = this.firstWagon;
        if (wagon != null) {
            for (int i = 0; i < numberOfWagons; i++) {
                stringBuilder.append(wagon.toString());
                wagon = wagon.getNextWagon();
            }
            stringBuilder.append(" ");
        }

        stringBuilder.append("with ")
                .append(numberOfWagons)
                .append(" wagons from ")
                .append(origin)
                .append(" to ")
                .append(destination);

        stringBuilder.append("\n").append("Total number of seats: ").append(getTotalNumberOfSeats());

        return stringBuilder.toString();
    }
}
