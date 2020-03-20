package ru.itmo.java;

public class HashTable {
    private static final float LOAD_FACTOR = 0.5f;
    private static final int STEP = 5563;

    private Entry[] elements;
    private boolean[] status;
    private float loadFactor;
    private int size = 0;

    public HashTable(int initialCapacity) {
        this(initialCapacity, LOAD_FACTOR);
    }

    public HashTable(int initialCapacity, float loadFactor) {
        this.elements = new Entry[initialCapacity];
        this.status = new boolean[initialCapacity];
        this.loadFactor = loadFactor;
    }

    public Object put(Object key, Object value) {
        if (size > elements.length * loadFactor) {
            ensureCapacity();
        }
        int hc = getCurrentIndex(key);
        if (elements[hc] == null) {
            hc = getNewIndex(key);
            if (status[hc]) {
                status[hc] = false;
            }
            size++;
            elements[hc] = new Entry(key, value);
            return null;
        }
        Object tmp = elements[hc].getValue();
        elements[hc] = new Entry(key, value);
        return tmp;
    }

    public Object get(Object key) {
        int hc = getCurrentIndex(key);
        if (elements[hc] == null) {
            return null;
        }
        return elements[hc].getValue();
    }

    public Object remove(Object key) {
        int hc = getCurrentIndex(key);
        if (elements[hc] == null) {
            return null;
        }
        size--;
        status[hc] = true;
        Object tmp = elements[hc].getValue();
        elements[hc] = null;
        return tmp;
    }

    public int size() {
        return size;
    }

    private int getNewIndex(Object key) {
        int length = elements.length;
        int hc = (key.hashCode() % length + length) % length;
        for (; elements[hc] != null; ) {
            hc = (hc + STEP) % length;
        }
        return hc;
    }

    private int getCurrentIndex(Object key) {
        int length = elements.length;
        int hc = (key.hashCode() % length + length) % length;
        for (; status[hc] || elements[hc] != null && !elements[hc].getKey().equals(key); ) {
            hc = (hc + STEP) % length;
        }
        return hc;
    }

    private void ensureCapacity() {
        int newLength = elements.length * 2;
        Entry[] tmpElements = elements;
        elements = new Entry[newLength];
        status = new boolean[newLength];
        size = 0;
        for (Entry element : tmpElements) {
            if (element != null) {
                this.put(element.key, element.value);
            }
        }
    }

    private static final class Entry {
        private Object key;
        private Object value;

        public Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }
    }
}
