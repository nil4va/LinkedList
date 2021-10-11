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

        Wagon nextWagon = this.nextWagon;
        while (nextWagon.hasNextWagon()) {
            nextWagon = nextWagon.nextWagon;
        }

        return nextWagon;
    }

    /**
     * @return  the length of the tail of wagons towards the end of the sequence
     * excluding this wagon itself.
     */
    public int getTailLength() {
        Wagon currentSelected = this.nextWagon;
        if (currentSelected == null) {
            return 0;
        }

        int length = 1;
        while (currentSelected.hasNextWagon()) {
            currentSelected = currentSelected.nextWagon;
            length++;
        }

        return length;
    }

    /**
     * Attaches the tail wagon behind this wagon, if and only if this wagon has no wagon attached at its tail
     * and if the tail wagon has no wagon attached in front of it.
     * @param tail the wagon to attach behind this wagon.
     * @throws IllegalStateException if this wagon already has a wagon appended to it.
     * @throws IllegalStateException if tail is already attached to a wagon in front of it.
     */
    public void attachTail(Wagon tail) {
        if (hasNextWagon()) {
            throw new IllegalStateException("Wagon " + this + " already has wagon " + getNextWagon() + " appended to it so " + tail + " cannot be appended.");
        }
        if (tail.hasPreviousWagon()) {
            throw new IllegalStateException("Wagon " + this + " already has wagon " + tail.getPreviousWagon() + " in front of it so " + tail + " cannot be appended.");
        }

        tail.setPreviousWagon(this);
        this.setNextWagon(tail);
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
        tail.setPreviousWagon(null);
        setNextWagon(null);

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
        setPreviousWagon(null);
        front.setNextWagon(null);

        return front;
    }

    /**
     * Replaces the tail of the front wagon with this wagon
     * Before such reconfiguration can be made,
     * the method first disconnects this wagon form its predecessor,
     * and the <code>front</code> wagon from its current tail.
     * @param front the wagon to which this wagon must be attached to.
     */
    public void reAttachTo(Wagon front) {
        if (hasPreviousWagon()) {
            getPreviousWagon().setNextWagon(null);
        }
        if (hasNextWagon()) {
            getNextWagon().setPreviousWagon(null);
        }
        front.setNextWagon(this);
        setPreviousWagon(front);
    }

    /**
     * Removes this wagon from the sequence that it is part of,
     * and reconnects its tail to the wagon in front of it, if it exists.
     */
    public void removeFromSequence() {
        Wagon front = this.previousWagon;
        Wagon tail = this.nextWagon;

        if (front != null) {
            front.setNextWagon(tail);
        }
        if (tail != null) {
            tail.setPreviousWagon(front);
        }

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
        Wagon previousWagon = this.getPreviousWagon();
        Wagon nextWagon = this.nextWagon;

        if (nextWagon == null) {
            setNextWagon(null);
            return this;
        }
        Wagon frontWagon = nextWagon.reverseSequence();
        setNextWagon(null);
        setPreviousWagon(null);
        reAttachTo(nextWagon);

        if (previousWagon != null) {
            previousWagon.setNextWagon(frontWagon);
        }

        frontWagon.setPreviousWagon(previousWagon);

        return frontWagon;
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

    @Override
    public String toString() {
        return "[Wagon-" + this.getId() + "]";
    }
}
