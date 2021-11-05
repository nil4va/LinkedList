package models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.function.BinaryOperator;

public class OrderedArrayList<E>
        extends ArrayList<E>
        implements OrderedList<E> {

    protected Comparator<? super E> ordening;   // the comparator that has been used with the latest sort
    protected int nSorted;                      // the number of items that have been ordered by barcode in the list
    // representation-invariant
    //      all items at index positions 0 <= index < nSorted have been ordered by the given ordening comparator
    //      other items at index position nSorted <= index < size() can be in any order amongst themselves
    //              and also relative to the sorted section

    public OrderedArrayList() {
        this(null);
    }

    public OrderedArrayList(Comparator<? super E> ordening) {
        super();
        this.ordening = ordening;
        this.nSorted = 0;
    }

    public Comparator<? super E> getOrdening() {
        return this.ordening;
    }

    @Override
    public void clear() {
        super.clear();
        this.nSorted = 0;
    }

    @Override
    public void sort(Comparator<? super E> c) {
        super.sort(c);
        this.ordening = c;
        this.nSorted = this.size();
    }

    // TODO override the ArrayList.add(index, item), ArrayList.remove(index) and Collection.remove(object) methods
    //  such that they sustain the representation invariant of OrderedArrayList
    //  (hint: only change nSorted as required to guarantee the representation invariant, do not invoke a sort)

    @Override
    public boolean add(E element) {
        super.add(element);
        return true;
    }

    @Override
    public void add(int index, E element) {
        int newNSorted = getNewNSortedForAdd(index, element);
        super.add(index, element);
        this.nSorted = newNSorted;
    }

    private int getNewNSortedForAdd(int index, E object){
        if (this.isEmpty()) {
            // If there is nothing yet, it's sorted by default.
            return 1;
        }

        if (this.ordening.compare(object, this.get(this.size() - 1)) >= 0) {
            // Adding object to the front and its smaller or equaled to the first element.
            return this.nSorted + 1;
        }

        if (this.ordening.compare(object, this.get(index - 1)) >= 0) {
            // The object is inserted into the sorted part of the list.
            // Return the current sorted amount plus one.
            return this.nSorted + 1;
        }

        if (this.ordening.compare(object, this.get(index - 1)) < 0) {
            // The object is inserted at the sorted part, and the new object is smaller than the object before it.
            // So everything before this item is sorted.
            return index;
        }

        return this.nSorted;
    }

    @Override
    public E remove(int index) {
        E removedElement = super.remove(index);
        this.nSorted--;
        return removedElement;
    }

    @Override
    public boolean remove(Object object) {
        boolean removed = super.remove(object);
        if (!removed) {
            return false;
        }
        this.nSorted--;
        return true;
    }

    @Override
    public void sort() {
        if (this.nSorted < this.size()) {
            this.sort(this.ordening);
        }
    }

    @Override
    public int indexOf(Object item) {
        if (item != null) {
            return indexOfByIterativeBinarySearch((E) item);
        } else {
            return -1;
        }
    }

    @Override
    public int indexOfByBinarySearch(E searchItem) {
        if (searchItem != null) {
            // some arbitrary choice to use the iterative or the recursive version
            return indexOfByRecursiveBinarySearch(searchItem);
        } else {
            return -1;
        }
    }

    /**
     * finds the position of the searchItem by an iterative binary search algorithm in the
     * sorted section of the arrayList, using the this.ordening comparator for comparison and equality test.
     * If the item is not found in the sorted section, the unsorted section of the arrayList shall be searched by linear search.
     * The found item shall yield a 0 result from the this.ordening comparator, and that need not to be in agreement with the .equals test.
     * Here we follow the comparator for ordening items and for deciding on equality.
     *
     * @param searchItem the item to be searched on the basis of comparison by this.ordening
     * @return the position index of the found item in the arrayList, or -1 if no item matches the search item.
     */
    public int indexOfByIterativeBinarySearch(E searchItem) {

        // TODO implement an iterative binary search on the sorted section of the arrayList, 0 <= index < nSorted
        //   to find the position of an item that matches searchItem (this.ordening comparator yields a 0 result)

        int from = 0;
        int to = this.size() - 1;

        while (from <= to) {
            int midIndex = from + (to - from) / 2;
            int compareResult = this.ordening.compare(searchItem, this.get(midIndex));

            if (compareResult < 0) {
                to = midIndex - 1;
            } else if (compareResult > 0) {
                from = midIndex + 1;
            } else {
                return midIndex;
            }
        }
        return linearSearch(searchItem);
    }

    /**
     * finds the position of the searchItem by a recursive binary search algorithm in the
     * sorted section of the arrayList, using the this.ordening comparator for comparison and equality test.
     * If the item is not found in the sorted section, the unsorted section of the arrayList shall be searched by linear search.
     * The found item shall yield a 0 result from the this.ordening comparator, and that need not to be in agreement with the .equals test.
     * Here we follow the comparator for ordening items and for deciding on equality.
     *
     * @param searchItem the item to be searched on the basis of comparison by this.ordening
     * @return the position index of the found item in the arrayList, or -1 if no item matches the search item.
     */
    public int indexOfByRecursiveBinarySearch(E searchItem) {
        // TODO implement a recursive binary search on the sorted section of the arrayList, 0 <= index < nSorted
        //   to find the position of an item that matches searchItem (this.ordening comparator yields a 0 result)

        // TODO if no match was found, attempt a linear search of searchItem in the section nSorted <= index < size()
        int index = recursiveIndexOf(searchItem, 0, this.nSorted - 1);
        if (index != -1) {
            return index;
        }
        return linearSearch(searchItem);
    }

    private int recursiveIndexOf(E searchItem, int low, int high) {
        if (low > high) {
            return -1;
        }

        int mid = low + (high - low) / 2;
        int comparatorResult = this.ordening.compare(searchItem, this.get(mid));
        if (comparatorResult < 0) {
            return recursiveIndexOf(searchItem, low, mid - 1);
        } else if (comparatorResult > 0) {
            return recursiveIndexOf(searchItem, mid + 1, high);
        } else {
            return mid;
        }
    }

    private int linearSearch(E searchItem) {
        ListIterator<E> iterator = this.listIterator(nSorted);
        while(iterator.hasNext()) {
            if (this.ordening.compare(iterator.next(), searchItem) == 0) {
                return iterator.previousIndex();
            }
        }

        return -1;
    }

    /**
     * finds a match of newItem in the list and applies the merger operator with the newItem to that match
     * i.e. the found match is replaced by the outcome of the merge between the match and the newItem
     * If no match is found in the list, the newItem is added to the list.
     *
     * @param newItem
     * @param merger  a function that takes two items and returns an item that contains the merged content of
     *                the two items according to some merging rule.
     *                e.g. a merger could add the value of attribute X of the second item
     *                to attribute X of the first item and then return the first item
     * @return whether a new item was added to the list or not
     */
    @Override
    public boolean merge(E newItem, BinaryOperator<E> merger) {
        if (newItem == null) {
            return false;
        }
        int matchedItemIndex = this.indexOfByRecursiveBinarySearch(newItem);

        if (matchedItemIndex < 0) {
            this.add(newItem);
        } else {
            E combined = merger.apply(this.get(matchedItemIndex), newItem);
            this.remove(matchedItemIndex);
            this.add(combined);
            this.sort();
        }
        return true;
    }
}