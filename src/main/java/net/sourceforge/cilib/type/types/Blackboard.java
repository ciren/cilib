package net.sourceforge.cilib.type.types;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple <tt>Blackboard</tt> implementation.
 * @author gpampara
 *
 * @param <K>
 * @param <V>
 */
public class Blackboard<K, V extends Type> extends AbstractType {
	private static final long serialVersionUID = -2589625146223946484L;
	private Map<K, V> board;
	
	public Blackboard() {
		this.board = new ConcurrentHashMap<K, V>();
	}
	
	public Blackboard(Blackboard<K, V> copy) {
		for (Map.Entry<K, V> entry : copy.board.entrySet()) {
    		K key = entry.getKey();
    		this.board.put(key, entry.getValue());
    	}
	}

	@Override
	public Blackboard<K, V> clone() {
		return new Blackboard<K, V>(this);
	}

	@Override
	public int getDimension() {
		return this.board.size();
	}

	@Override
	public String getRepresentation() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Not supported for <tt>Blackboard</tt>
	 * @throws UnsupportedOperationException always
	 */
	@Override
	public boolean isInsideBounds() {
		throw new UnsupportedOperationException("Not supported");
	}

	/**
	 * Not supported for <tt>Blackboard</tt>
	 * @throws UnsupportedOperationException always
	 */
	@Override
	public void randomise() {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public void reset() {
		this.board.clear();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public V put(K key, V value) {
		return this.board.put(key, value);
	}
	
	public V get(K key) {
		return this.board.get(key);
	}
	
	public Set<Map.Entry<K, V>> entrySet() {
		return this.board.entrySet();
	}

}
