// LostAndFoundManager.java - UPDATE THIS FILE
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class LostAndFoundManager {
    private final List<Item> items = new ArrayList<>();
    private final Map<String, List<Item>> categorizedItems = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock(true);
    
    // Using synchronized block instead of synchronized method
    public void addItem(Item item) {
        lock.lock();
        try {
            items.add(item);
            categorizedItems
                .computeIfAbsent(item.getCategory(), c -> new ArrayList<>())
                .add(item);
            
            // Log for debugging
            System.out.println(Thread.currentThread().getName() + 
                " added item: " + item.getItemId());
        } finally {
            lock.unlock();
        }
    }
    
    // Get all items with copy to prevent modification
    public List<Item> getAllItems() {
    lock.lock();
    try {
        return new ArrayList<>(items); 
    } finally {
        lock.unlock();
    }
}
    // Search with synchronized block
    public Optional<Item> searchById(String itemId) {
        lock.lock();
        try {
            return items.stream()
                .filter(i -> i.getItemId().equals(itemId))
                .findFirst();
        } finally {
            lock.unlock();
        }
    }
    
    // Batch add with single lock acquisition
    public void addAllItems(List<Item> newItems) {
    lock.lock();
    try {
        for (Item item : newItems) {
            items.add(item);
            categorizedItems
                .computeIfAbsent(item.getCategory(), c -> new ArrayList<>())
                .add(item);
        }
        System.out.println("Added " + newItems.size() + " items in batch");
    } finally {
        lock.unlock();
    }
}
    
    // Clear all items (with synchronization)
    public void clearAll() {
        lock.lock();
        try {
            items.clear();
            categorizedItems.clear();
            System.out.println("All items cleared");
        } finally {
            lock.unlock();
        }
    }
    
    // Get item count (thread-safe)
    public int getItemCount() {
        lock.lock();
        try {
            return items.size();
        } finally {
            lock.unlock();
        }
    }
    
    // Get categories count (thread-safe)
    public Map<String, Integer> getCategoryCounts() {
        lock.lock();
        try {
            Map<String, Integer> counts = new HashMap<>();
            for (Map.Entry<String, List<Item>> entry : categorizedItems.entrySet()) {
                counts.put(entry.getKey(), entry.getValue().size());
            }
            return counts;
        } finally {
            lock.unlock();
        }
    }
}