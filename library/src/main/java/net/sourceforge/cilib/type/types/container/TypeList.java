/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.cilib.util.Visitor;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;

/**
 * General list container for all {@code Type} instances.
 *
 */
public class TypeList extends AbstractList<Type> {

    private static final long serialVersionUID = 136711882764612609L;
    private List<Type> components;

    /**
     * Create a new instance.
     */
    public TypeList() {
        this.components = new ArrayList<Type>();
    }

    /**
     * Create a new instance with the predefined size.
     * @param size The predefined size.
     */
    @Deprecated
    private TypeList(int size) {
        this.components = new ArrayList<Type>();
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    @Deprecated
    private TypeList(TypeList copy) {
        this.components = new ArrayList<Type>(copy.components.size());

        for (Type type : copy.components) {
            this.components.add(type.getClone());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeList getClone() {
        return new TypeList(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if ((o == null) || (this.getClass() != o.getClass())) {
            return false;
        }

        TypeList otherList = (TypeList) o;
        return this.components.equals(otherList.components);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.components == null ? 0 : this.components.hashCode());
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type get(int index) {
        return this.components.get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(int index, Type value) {
        this.components.set(index, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(int index, Type value) {
        this.components.add(index, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean append(AbstractList<Type> list) {
        for (Type type : list) {
            this.components.add(type);
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean prepend(AbstractList<Type> list) {
        for (int i = list.size() - 1; i >= 0; i--) {
            this.components.add(0, list.get(i));
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray() {
        return this.components.toArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeList subList(int fromIndex, int toIndex) {
        // Need to bump up the toIndex because the List.subList() operation is upper bound exclusive.
        List<Type> result = this.components.subList(fromIndex, toIndex + 1);
        TypeList sublist = new TypeList();

        for (Type type : result) {
            sublist.add(type);
        }

        return sublist;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(Type element) {
        return this.components.add(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(Collection<? extends Type> c) {
        return this.components.addAll(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        this.components.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Object o) {
        return this.components.contains(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return this.components.isEmpty();
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * @return an iterator over the elements in this list in proper sequence
     */
    @Override
    public Iterator<Type> iterator() {
        return this.components.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object o) {
        return this.components.remove(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        return this.components.removeAll(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return this.components.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(Visitor<Type> visitor) {
        for (Type type : this.components) {
            if (!visitor.isDone()) {
                visitor.visit(type);
            }
        }
    }

    @Override
    public void randomise() {
        for (int i = 0; i < components.size(); i++) {
            Type type = components.get(i);
            if (type instanceof Numeric) {
                Numeric numeric = (Numeric) type;
                numeric.randomise();
            }

            if (type instanceof StructuredType) {
                StructuredType structuredType = (StructuredType) type;
                structuredType.randomise();
            }
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.components.toArray(a);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.components.containsAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.components.retainAll(c);
    }
}
