package uzman.oguz;

import java.util.Comparator;

/**
 * Created by oguz on 18/11/2016.
 */
public class ArrayIndexComparator implements Comparator<Integer>
{
    private final double[] array;

    public ArrayIndexComparator(double[] array)
    {
        this.array = array;
    }

    public Integer[] createIndexArray()
    {
        Integer[] indexes = new Integer[array.length];
        for (int i = 0; i < array.length; i++)
        {
            indexes[i] = i; // Autoboxing
        }
        return indexes;
    }

    @Override
    public int compare(Integer index1, Integer index2)
    {
        // Autounbox from Integer to int to use as array indexes
        double diff = array[index1]-array[index2];
        return (diff) > 0.0 ? 1 : (diff == 0.0 ? 0 : -1 );
    }
}