package net.spy.memcached;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import net.spy.memcached.transcoders.Transcoder;

/**
 * Base class for a Map interface to memcached.
 *
 * <p>
 * This Map interface makes memcached a bit easier to use for some purposes
 * by providing a limited Map implementation.
 * </p>
 *
 * <p>
 * Do note that nothing that iterates over the map will work (such is
 * memcached).  All iteration mechanisms will return empty iterators and
 * such.
 * </p>
 *
 * @param <V> the type of value taken and returned by this Map's underlying
 *            transcoder, and thus taken and returned by this Map.
 */
public class BaseCacheMap<V> implements Map<String, V> {

  private final String keyPrefix;
  private final Transcoder<V> transcoder;
  private final MemcachedClientIF client;
  private final int exp;

  /**
   * Build a BaseCacheMap.
   *
   * @param c          the underlying client
   * @param expiration the expiration for objects set through this Map
   * @param prefix     a prefix to ensure objects in this map are unique
   * @param t          the transcoder to serialize and deserialize objects
   */
  public BaseCacheMap(MemcachedClientIF c, int expiration,
                      String prefix, Transcoder<V> t) {
    super();
    keyPrefix = prefix;
    transcoder = t;
    client = c;
    exp = expiration;
  }

  public void clear() {
    // TODO:  Support a rolling key generation.
    throw new UnsupportedOperationException();
  }

  private String getKey(String k) {
    return keyPrefix + k;
  }

  public boolean containsKey(Object key) {
    return get(key) != null;
  }

  /**
   * This method always returns false, as truth cannot be determined without
   * iteration.
   */
  public boolean containsValue(Object value) {
    return false;
  }

  public Set<Map.Entry<String, V>> entrySet() {
    return Collections.emptySet();
  }

  public V get(Object key) {
    V rv = null;
    try {
      rv = client.get(getKey((String) key), transcoder);
    } catch (ClassCastException e) {
      // Most likely, this is because the key wasn't a String.
      // Either way, it's a no.
    }
    return rv;
  }

  public boolean isEmpty() {
    return false;
  }

  public Set<String> keySet() {
    return Collections.emptySet();
  }

  public void putAll(Map<? extends String, ? extends V> t) {
    for (Map.Entry<? extends String, ? extends V> me : t.entrySet()) {
      client.set(getKey(me.getKey()), exp, me.getValue());
    }
  }

  public V remove(Object key) {
    V rv = null;
    try {
      rv = get(key);
      client.delete(getKey((String) key));
    } catch (ClassCastException e) {
      // Not a string key.  Ignore.
    }
    return rv;
  }

  public int size() {
    return 0;
  }

  public Collection<V> values() {
    return Collections.emptySet();
  }

  public V put(String key, V value) {
    V rv = get(key);
    client.set(getKey(key), exp, value);
    return rv;
  }

}
