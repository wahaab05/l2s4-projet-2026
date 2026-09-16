package utils.datastructures;

public class Tuple2<A, B> {
    private final A value1;
    private final B value2;

    public Tuple2(A value1, B value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public A first() {
        return this.value1;
    }

    public B second() {
        return this.value2;
    }

	public String toString()
	{
		return this.value1.toString() +" "+ this.value2.toString() + "(🦈)";
	}
}

