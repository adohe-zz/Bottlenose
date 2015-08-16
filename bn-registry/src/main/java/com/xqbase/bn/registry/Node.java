package com.xqbase.bn.registry;

import java.util.Calendar;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The internal node.
 *
 * @author Tony He
 */
public class Node {

    public static final String KEY_SEPARATOR = "/";

    private final String key;
    private final boolean dir;
    private final String value;
    private final ConcurrentHashMap<String, Node> children;
    private final Calendar createTime;
    private Calendar expirationTime;
    private Integer ttl;

    public Node(String key, boolean dir, String value, Integer ttl) {
        this(key, dir, value, ttl, Calendar.getInstance());
    }

    private Node(String key, boolean dir, String value, Integer ttl, Calendar createTime) {
        this.key = key;
        this.dir = dir;
        this.value = value;
        this.children = this.dir ? new ConcurrentHashMap<String, Node>() : null;
        this.createTime = createTime;
        updateTtl(ttl);
    }

    public String getKey() {
        return key;
    }

    public String getLastKeySegment() {
        int lastSlashIndex = key.lastIndexOf(KEY_SEPARATOR);
        return lastSlashIndex != -1 ? key.substring(lastSlashIndex + 1) : key;
    }

    public boolean isDir() {
        return dir;
    }

    public String getValue() {
        return value;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public Integer getTtl() {
        return ttl;
    }

    public synchronized void updateTtl(Integer ttl) {
        this.ttl = ttl;
        if (ttl != null) {
            expirationTime = Calendar.getInstance();
            expirationTime.setTimeInMillis(expirationTime.getTimeInMillis() + ttl.intValue() * 1000);
        } else {
            expirationTime = null;
        }
    }

    public Calendar getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Calendar expirationTime) {
        this.expirationTime = expirationTime;
    }

    public synchronized boolean isExpired() {
        return expirationTime != null && Calendar.getInstance().after(expirationTime);
    }

    public boolean isEmpty() {
        return children == null || children.isEmpty();
    }

    public Object[] getChildKeys() {
        return children.keySet().toArray();
    }

    public Collection<Node> getChildren() {
        return children.values();
    }

    public Node getChild(String key) {
        return children.get(key);
    }

    public Node getChildBySubKey(String subKey) {
        return children.get(key + KEY_SEPARATOR + subKey);
    }

    public boolean containChild(String key) {
        return children.containsKey(key);
    }

    public boolean containChildBySubKey(String subKey) {
        return children.containsKey(key + KEY_SEPARATOR + subKey);
    }

    public boolean addChild(Node node) {
        if (!dir) {
            throw new IllegalStateException("No child for value node.");
        }
        Node existedNode = children.putIfAbsent(node.key, node);
        return existedNode == null;
    }

    public boolean isExpiring() {
        boolean result = true;

        for (Node child : children.values()) {
            if (child.ttl != null && child.ttl > 30)
                result = false;
        }

        return result;
    }

    public void replaceChild(Node node) {
        if (!dir) {
            throw new IllegalStateException("No child for value node.");
        }
        children.put(node.key, node);
    }

    public void removeChild(String key) {
        if (children != null) {
            children.remove(key);
        }
    }

    public Node clone(boolean recursive) {
        return clone(true, recursive);
    }

    protected Node clone(boolean includeChildren, boolean recursive) {
        Node node = new Node(key, dir, value, null, createTime);
        node.ttl = ttl;
        node.expirationTime = expirationTime;
        if (dir && includeChildren) {
            for (Node childInMemNode : children.values()) {
                if (childInMemNode.isExpired()) {
                    continue;
                }
                node.children.put(childInMemNode.key, childInMemNode.clone(recursive, recursive));
            }
        }
        return node;
    }

    @Override
    public String toString() {
        return key;
    }
}