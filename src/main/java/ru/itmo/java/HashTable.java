package ru.itmo.java;

public class HashTable {
    private static final float LOAD_FACTOR = 0.5f;

    private Entry[] elements;
    private boolean[] status;
    private float loadFactor;
    private int size = 0;

    public HashTable(int initialCapacity) {
        this(initialCapacity, LOAD_FACTOR);
    }

    public HashTable(int initialCapacity, float loadFactor) {
        elements = new Entry[initialCapacity];
        status = new boolean[initialCapacity];
        this.loadFactor = loadFactor;
    }

    Object put(Object key, Object value) {
        if (size > elements.length * loadFactor) {
            this.ensureCapacity();
        }
        int hc = currentIndex(key);
        if (elements[hc] == null) {
            hc = newIndex(key);
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

    Object get(Object key) {
        int hc = currentIndex(key);
        if (elements[hc] == null) {
            return null;
        }
        return elements[hc].getValue();
    }

    Object remove(Object key) {
        int hc = currentIndex(key);
        if (elements[hc] == null) {
            return null;
        }
        size--;
        status[hc] = true;
        Object tmp = elements[hc].getValue();
        elements[hc] = null;
        return tmp;
    }

    int size() {
        return size;
    }

    int newIndex(Object key) {
        int hc = (key.hashCode() % elements.length + elements.length) % elements.length;
        for (; elements[hc] != null; ) {
            hc = (hc + 5563) % elements.length;
        }
        return hc;
    }

    int currentIndex(Object key) {
        int hc = (key.hashCode() % elements.length + elements.length) % elements.length;
        for (; status[hc] || elements[hc] != null && !elements[hc].getKey().equals(key); ) {
            hc = (hc + 5563) % elements.length;
        }
        return hc;
    }

    void ensureCapacity() {
        Entry[] tmp_elements = elements;
        elements = new Entry[tmp_elements.length * 2];
        status = new boolean[tmp_elements.length * 2];
        size = 0;
        for (Entry element : tmp_elements) {
            if (element != null) {
                this.put(element.key, element.value);
            }
        }
    }


    private static class Entry {
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
