package fr.sidranie.newsther.util;

/**
 * Describe the counting system of an object.
 * @param <T> The object to count. It is a read-only object.
 */
public class CountedObject<T> {
    private final T value;
    private int counter;

    /**
     * Create a new instance of a counting object.
     * This constructor defaultly set 0 as starting counter value
     * @param value The object to count
     */
    public CountedObject(T value) {
        this.value = value;
        this.counter = 0;
    }

    /**
     * Create a new instance of a counting object.
     * @param value The object to count
     * @param counter The starting counter value
     */
    public CountedObject(T value, int counter) {
        this.value = value;
        this.counter = counter;
    }

    /**
     * Increment the counter by 1
     */
    public void incrementCounter() {
        this.counter++;
    }

    /**
     * Add number to counter
     * @param number The number to add to counter.
     */
    public void add(int number) {
        this.counter += number;
    }

    /**
     * Decrement the counter by 1
     */
    public void decrementCounter() {
        this.counter--;
    }

    /**
     * Subtract number to counter
     * @param number The number to subtract to counter.
     */
    public void subtract(int number) {
        this.counter -= number;
    }

    public T getValue() {
        return value;
    }

    public int getCounter() {
        return counter;
    }
}
