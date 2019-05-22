/*
 * Copyright © 2015 - 2019 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.treefinance.crawler.lang;

import com.treefinance.toolkit.util.json.Jackson;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since 2016年1月8日 上午11:47:35
 */
public class SynchronizedMap<K, V> implements Map<K, V> {

    private final Map<K, V> map;

    private final ReadWriteLock readWriteLock;

    public SynchronizedMap() {
        map = new HashMap<>();
        readWriteLock = new ReentrantReadWriteLock();
    }

    public SynchronizedMap(Map<K, V> m) {
        map = new HashMap<>(m);
        readWriteLock = new ReentrantReadWriteLock();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#size()
     */
    @Override
    public int size() {
        readWriteLock.readLock().lock();
        try {
            return map.size();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        readWriteLock.readLock().lock();
        try {
            return map.isEmpty();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    @Override
    public boolean containsKey(Object key) {
        readWriteLock.readLock().lock();
        try {
            return map.containsKey(key);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    @Override
    public boolean containsValue(Object value) {
        readWriteLock.readLock().lock();
        try {
            return map.containsValue(value);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#get(java.lang.Object)
     */
    @Override
    public V get(Object key) {
        readWriteLock.readLock().lock();
        try {
            return map.get(key);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public V put(K key, V value) {
        readWriteLock.writeLock().lock();
        try {
            return map.put(key, value);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#remove(java.lang.Object)
     */
    @Override
    public V remove(Object key) {
        readWriteLock.writeLock().lock();
        try {
            return map.remove(key);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#putAll(java.util.Map)
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        readWriteLock.writeLock().lock();
        try {
            map.putAll(m);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#clear()
     */
    @Override
    public void clear() {
        readWriteLock.writeLock().lock();
        try {
            map.clear();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#keySet()
     */
    @Override
    public Set<K> keySet() {
        readWriteLock.readLock().lock();
        try {
            return map.keySet();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#values()
     */
    @Override
    public Collection<V> values() {
        readWriteLock.readLock().lock();
        try {
            return map.values();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Map#entrySet()
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        readWriteLock.readLock().lock();
        try {
            return map.entrySet();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public String toString() {
        return Jackson.toJSONString(map);
    }
}
