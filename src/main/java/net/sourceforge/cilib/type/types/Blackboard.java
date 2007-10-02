package net.sourceforge.cilib.type.types;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
		this.board = new HashMap<K, V>();
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

	@Override
	public boolean isInsideBounds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void randomise() {
		// TODO Auto-generated method stub

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
