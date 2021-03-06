package com.nemo9955.garden_revolution.utility;

public class IndexedObject<Obj> implements Comparable<IndexedObject<?>> {

	public Obj	object;
	public int	index;

	public IndexedObject(Obj object, int index) {
		this.object = object;
		this.index = index;
	}

	@Override
	public int compareTo( IndexedObject<?> o ) {

		if ( index > o.index )
			return 1;
		else if ( index == o.index )
			return 0;
		else
			return -1;

	}

}
