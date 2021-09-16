package models;

public abstract class Wagon {
    protected int id;
    private Wagon nextWagon;
    private Wagon previousWagon;


    // representation invariant propositions:
    // tail-connection-invariant:   wagon.nextWagon == null or wagon == wagon.nextWagon.previousWagon
    // front-connection-invariant:  wagon.previousWagon == null or wagon = wagon.previousWagon.nextWagon

    public Wagon (int wagonId) {
        this.id = wagonId;
    }

    public int getId() {
        return id;
    }

    public Wagon getNextWagon() {
        return nextWagon;
    }

    public Wagon getPreviousWagon() {
        return previousWagon;
    }

    public boolean hasNextWagon() {
        return this.nextWagon != null;
    }

    public boolean hasPreviousWagon() {
        return this.previousWagon != null;
    }

    /**
     * Returns the last wagon attached to it, if there are no wagons attached to it then this wagon is the last wagon.
     * @return  the wagon
     */
    public Wagon getLastWagonAttached() {
        if (!this.hasNextWagon()) {
            return this;
        }

        // TODO: Validate if this even works, did not test any of it D:
        Wagon nextWagon = this.nextWagon;
        while (this.hasNextWagon()) {
            nextWagon = nextWagon.nextWagon;
        }

        return nextWagon;
    }

    /**
     * @return  the length of the tail of wagons towards the end of the sequence
     * excluding this wagon itself.
     */
    public int getTailLength() {
        // TODO traverse the tail and find its length

        Wagon nextWagon = this.nextWagon;
        while (this.hasNextWagon()) {
            nextWagon = nextWagon.nextWagon;
        }

        return 0;
    }

    /**
     * Attaches the tail wagon behind this wagon, if and only if this wagon has no wagon attached at its tail
     * and if the tail wagon has no wagon attached in front of it.
     * @param tail the wagon to attach behind this wagon.
     * @throws IllegalStateException if this wagon already has a wagon appended to it.
     * @throws IllegalStateException if tail is already attached to a wagon in front of it.
     */
    public void attachTail(Wagon tail) {
        if (tail.hasNextWagon() || tail.hasPreviousWagon()) {
            throw new IllegalStateException("The wagon has at least one wagon attached.");
        }

        if (!this.hasNextWagon()) {
            this.nextWagon = tail;
            return;
        }

        this.getLastWagonAttached().attachTail(tail);
    }

    /**
     * Detaches the tail from this wagon and returns the first wagon of this tail.
     * @return the first wagon of the tail that has been detached
     *          or <code>null</code> if it had no wagons attached to its tail.
     */
    public Wagon detachTail() {
        if (!this.hasNextWagon()) {
            return null;
        }

        Wagon tail = this.nextWagon;
        this.nextWagon = null;

        return tail;
    }

    /**
     * Detaches this wagon from the wagon in front of it.
     * No action if this wagon has no previous wagon attached.
     * @return  the former previousWagon that has been detached from,
     *          or null if it had no previousWagon.
     */
    public Wagon detachFront() {
        if (!this.hasPreviousWagon()) {
            return null;
        }

        Wagon front = this.previousWagon;
        this.previousWagon = null;

        return front;
    }

    /**
     * Replaces the tail of the <code>front</code> wagon by this wagon
     * Before such reconfiguration can be made,
     * the method first disconnects this wagon form its predecessor,
     * and the <code>front</code> wagon from its current tail.
     * @param front the wagon to which this wagon must be attached to.
     */
    public void reAttachTo(Wagon front) {
        this.setPreviousWagon(null);
        this.setNextWagon(null);

        front.setNextWagon(this);
    }

    /**
     * Removes this wagon from the sequence that it is part of,
     * and reconnects its tail to the wagon in front of it, if it exists.
     */
    public void removeFromSequence() {
        Wagon front = this.previousWagon;
        Wagon tail = this.nextWagon;

        front.setNextWagon(tail);
        tail.setPreviousWagon(front);

        this.setNextWagon(null);
        this.setPreviousWagon(null);
    }

    /**
     * Reverses the order in the sequence of wagons from this Wagon until its final successor.
     * The reversed sequence is attached again to the wagon in front of this Wagon, if any.
     * No action if this Wagon has no succeeding next wagon attached.
     * @return the new start Wagon of the reversed sequence (with is the former last Wagon of the original sequence)
     */
    public Wagon reverseSequence() {
        Wagon nextWagon = this.nextWagon;
        if (nextWagon == null) {
            return this;
        }

        Wagon front = nextWagon.reverseSequence();
        this.reAttachTo(front);

        return front;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNextWagon(Wagon nextWagon) {
        this.nextWagon = nextWagon;
    }

    public void setPreviousWagon(Wagon previousWagon) {
        this.previousWagon = previousWagon;
    }
}
