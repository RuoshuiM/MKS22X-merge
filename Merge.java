import java.util.Arrays;

public class Merge {
    
    /* sort the array from least to greatest value. This is a wrapper function */
    public static void mergesort(int[] data) {
        mergesort(data, new int[data.length], 0, data.length - 1);
    }
    
    /* sort the array from least to greatest value. This is a wrapper function */
    public static void slowMergesort(int[] data) {
        System.arraycopy(slowMergesortHelper(data), 0, data, 0, data.length);
    }

    private static int[] slowMergesortHelper(int[] data) {
        if (data.length <= 1) {
            return data;
        }

        int mid = data.length / 2;
        int[] low = Arrays.copyOfRange(data, 0, mid);
        int[] high = Arrays.copyOfRange(data, mid, data.length);

        low = slowMergesortHelper(low);
        high = slowMergesortHelper(high);

        return merge(low, high);
    }

    private static int[] merge(int[] a, int[] b) {

        int[] result = new int[a.length + b.length];

        for (int ai = 0, bi = 0, i = 0; i < result.length; i++) {
            if (ai == a.length) {
                while (i < result.length) {
                    result[i++] = b[bi++];
                }
                break;
            }
            if (bi == b.length) {
                while (i < result.length) {
                    result[i++] = a[ai++];
                }
                break;
            }
            if (a[ai] < b[bi]) {
                result[i] = a[ai];
                ai++;
            } else {
                result[i] = b[bi];
                bi++;
            }
        }

        return result;
    }

    /**
     * Mergesorts data using temp, merging back into data.
     * @param data
     * @param temp
     * @param lo Lower-bound of array-index, inclusive
     * @param hi Upper-bound of array-index, inclusive
     */
    private static void mergesort(int[] data, int[] temp, int lo, int hi) {
        if (lo >= hi) return;
        
        int mid = (lo + hi) / 2;
        
        System.arraycopy(data, lo, temp, lo, hi - lo + 1);

        mergesort(temp, data, lo, mid);
        mergesort(temp, data, mid + 1, hi);
        
        // debug
//        System.out.println("Merging: " + partialToString(temp, lo, mid + 1) + " and " + partialToString(temp, mid + 1, hi + 1));
        merge(data, temp, lo, mid, hi);
        
        // debug
//        System.out.println("Merged: " + partialToString(data, lo, hi + 1));
    }
    
    static String partialToString(int[] arr, int start, int end) {
        return Arrays.toString(Arrays.copyOfRange(arr, start, end));
    }
    
    /**
     * Merges temp into data
     * @param data
     * @param temp
     * @param low
     * @param mid
     * @param high
     */
    private static void merge(int[] data, int[] temp, int low, int mid, int high) {
        // first array(a): low ~ mid
        // second array(b): mid + 1 ~ high
        int resultLength = high - low + 1;
        int end = resultLength + low;
        int aStart = low;
        int bStart = mid + 1;
        int aLength = mid - aStart + 1;
        int bLength = high - bStart + 1;
        
        // debug
//        System.out.format("aSt: %d, bSt: %d, aLen: %d, bLen: %d, tLen: %d%n", aStart, bStart, aLength, bLength, resultLength);
        
        for (int ai = aStart, bi = bStart, i = low; i < end; i++) {
            if (ai == aLength + aStart) {
                while (i < end) {
                    data[i++] = temp[bi++];
                }
                break;
            }
            if (bi == bLength + bStart) {
                while (i < end) {
                    data[i++] = temp[ai++];
                }
                break;
            }
            if (temp[ai] < temp[bi]) {
                data[i] = temp[ai];
                ai++;
            } else {
                data[i] = temp[bi];
                bi++;
            }
        }
        
    }

    public static void main(String[] args) {        
        compareSorts();
    }

    public static void compareSorts() {
        System.out.println("Size\t\tMax Value\tquick/builtin ratio ");
        int[] MAX_LIST = { 1000000000, 500, 10 };
        for (int MAX : MAX_LIST) {
            for (int size = 31250; size < 2000001; size *= 2) {
                long qtime = 0;
                long btime = 0;
                // average of 5 sorts.
                for (int trial = 0; trial <= 5; trial++) {
                    int[] data1 = new int[size];
                    int[] data2 = new int[size];
                    for (int i = 0; i < data1.length; i++) {
                        data1[i] = (int) (Math.random() * MAX);
                        data2[i] = data1[i];
                    }
                    long t1, t2;
                    t1 = System.currentTimeMillis();
                    mergesort(data2);
                    t2 = System.currentTimeMillis();
                    qtime += t2 - t1;
                    t1 = System.currentTimeMillis();
                    Arrays.sort(data1);
                    t2 = System.currentTimeMillis();
                    btime += t2 - t1;
                    if (!Arrays.equals(data1, data2)) {
                        System.out.println("FAIL TO SORT!");
                        System.exit(0);
                    }
                }
                System.out.format("qtime: %d%nbtime: %d%n", qtime, btime);
                System.out.println(size + "\t\t" + MAX + "\t" + 1.0 * qtime / btime);
            }
            System.out.println();
        }
    }
}
