package org.jessica.core.util.vcollections;

import java.util.Collection;
import java.util.Iterator;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

public class FilteredCollection<E> implements Collection<E> {
	final Collection<E> baseCollection;
	final Predicate<? super E> predicate;

	public static void checkArgument(boolean expression) {
		if (!expression) {
			throw new IllegalArgumentException();
		}
	}

	public static <T> T checkNotNull(T reference) {
		if (reference == null) {
			throw new NullPointerException();
		}
		return reference;
	}

	FilteredCollection(Collection<E> baseCollection, Predicate<? super E> predicate) {
		this.baseCollection = baseCollection;
		this.predicate = predicate;
	}

	FilteredCollection<E> createCombined(Predicate<? super E> newPredicate) {
		return new FilteredCollection<E>(baseCollection, Predicates.<E> and(predicate, newPredicate));

	}

	@Override
	public boolean add(E element) {
		checkArgument(predicate.apply(element));
		return baseCollection.add(element);
	}

	@Override
	public boolean addAll(Collection<? extends E> collection) {
		for (E element : collection) {
			checkArgument(predicate.apply(element));
		}
		return baseCollection.addAll(collection);
	}

	@Override
	public void clear() {
		Iterables.removeIf(baseCollection, predicate);
	}

	@Override
	public boolean contains(Object element) {
		try {

			@SuppressWarnings("unchecked")
			E e = (E) element;
			return predicate.apply(e) && baseCollection.contains(element);
		} catch (NullPointerException e) {
			return false;
		} catch (ClassCastException e) {
			return false;
		}
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		for (Object element : collection) {
			if (!contains(element)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isEmpty() {
		return !Iterators.any(baseCollection.iterator(), predicate);
	}

	@Override
	public Iterator<E> iterator() {
		return Iterators.filter(baseCollection.iterator(), predicate);
	}

	@Override
	public boolean remove(Object element) {
		try {
			@SuppressWarnings("unchecked")
			E e = (E) element;
			return predicate.apply(e) && baseCollection.remove(element);
		} catch (NullPointerException e) {
			return false;
		} catch (ClassCastException e) {
			return false;
		}
	}

	@Override
	public boolean removeAll(final Collection<?> collection) {
		checkNotNull(collection);
		Predicate<E> combinedPredicate = new Predicate<E>() {
			@Override
			public boolean apply(E input) {
				return predicate.apply(input) && collection.contains(input);
			}
		};
		return Iterables.removeIf(baseCollection, combinedPredicate);
	}

	@Override
	public boolean retainAll(final Collection<?> collection) {
		checkNotNull(collection);
		Predicate<E> combinedPredicate = new Predicate<E>() {
			@Override
			public boolean apply(E input) {
				// See comment in contains() concerning predicate.apply(e)
				return predicate.apply(input) && !collection.contains(input);
			}
		};
		return Iterables.removeIf(baseCollection, combinedPredicate);
	}

	@Override
	public int size() {
		return Iterators.size(iterator());
	}

	@Override
	public Object[] toArray() {
		return Lists.newArrayList(iterator()).toArray();
	}

	@Override
	public <T> T[] toArray(T[] array) {
		return Lists.newArrayList(iterator()).toArray(array);
	}

	@Override
	public String toString() {
		return Iterators.toString(iterator());
	}
}
