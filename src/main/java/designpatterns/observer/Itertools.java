package designpatterns.observer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * All code here taken from https://github.com/samagra14/itertools_java It is used for the AI to
 * generate permutations.
 */

public class Itertools {

  @FunctionalInterface
  public interface Predicate<T> {

    boolean pred(T t);
  }

  @FunctionalInterface
  public interface Function<T, U> {

    U apply(List<T> list);
  }

  public static class PermutationGenerator {

    public static <T> List<List<T>> generatePermutations(List<T> list, int r) {
      List<List<T>> result = new ArrayList<>();
      List<T> tempList;
      List<List<Integer>> mappings = CombinationGenerator.combination(r, list.size());
      List<List<Integer>> rPermutations = permutations(r);
      for (List<Integer> combination :
          mappings) {
        for (List<Integer> permutation :
            rPermutations) {
          tempList = new ArrayList<>();
          for (int a :
              permutation) {
            tempList.add(list.get(combination.get(a - 1) - 1));
          }
          result.add(tempList);
        }
      }

      return result;
    }

    public static List<List<Integer>> permutations(int n) {
      List<List<Integer>> result = new ArrayList<>();
      int nfact = (int) factorial(n);
      List<Integer> tempList;
      int[] temp = new int[n];
      for (int i = 0; i < n; i++) {
        temp[i] = i + 1;
      }
      tempList = new ArrayList<>();
      int x;
      for (int i = 0; i < n; i++) {
        x = temp[i];
        tempList.add(x);
      }
      result.add(tempList);
      int m;
      int k;
      for (int i = 1; i < nfact; i++) {
        m = n - 1;
          while (temp[m - 1] > temp[m]) {
              m--;
          }
        k = n;
          while (temp[m - 1] > temp[k - 1]) {
              k--;
          }
        int swapVar;
        //swap m and k
        swapVar = temp[m - 1];
        temp[m - 1] = temp[k - 1];
        temp[k - 1] = swapVar;

        int p = m + 1;
        int q = n;
        while (p < q) {
          swapVar = temp[p - 1];
          temp[p - 1] = temp[q - 1];
          temp[q - 1] = swapVar;
          p++;
          q--;
        }
        tempList = new ArrayList<>();
        for (int j = 0; j < n; j++) {
          x = temp[j];
          tempList.add(x);
        }
        result.add(tempList);
      }
      return result;
    }

    static double factorial(int n) {
      int nfact = 1;
      for (int i = 1; i <= n; i++) {
        nfact *= i;
      }
      return nfact;
    }

    public static int[] generateNextProduct(int[] curr, int[] max) {
      int n = curr.length - 1;
      curr[n]++;
      for (int i = n; i > 0; i--) {
        if (curr[i] > max[i]) {
          curr[i] = 1;
          curr[i - 1]++;
        }
      }
      return curr;
    }

    public static int[] generateNextPermutation(int[] temp, int n) {
      int m = n - 1;
        while (temp[m - 1] > temp[m]) {
            m--;
        }
      int k = n;
        while (temp[m - 1] > temp[k - 1]) {
            k--;
        }
      int swapVar;
      //swap m and k
      swapVar = temp[m - 1];
      temp[m - 1] = temp[k - 1];
      temp[k - 1] = swapVar;

      int p = m + 1;
      int q = n;
      while (p < q) {
        swapVar = temp[p - 1];
        temp[p - 1] = temp[q - 1];
        temp[q - 1] = swapVar;
        p++;
        q--;
      }
      return temp;
    }

  }

  public static class CombinationGenerator {

    public static <T> List<List<T>> generateCombinations(List<T> list, int r) {
      List<List<T>> result = new ArrayList<>();
      List<List<Integer>> mappings = combination(r, list.size());
      List<T> tempList;
      for (List<Integer> singleCombination :
          mappings) {
        tempList = new ArrayList<>();
        for (int a :
            singleCombination) {
          tempList.add(list.get(a - 1));
        }
        result.add(tempList);
      }
      return result;
    }

    public static List<List<Integer>> combination(int r, int n) {
      int numberOfCombinations = (int) nCr(n, r);
      List<Integer> tempList;
      List<List<Integer>> result = new ArrayList<>();
      int[] temp = new int[r];
      for (int i = 0; i < r; i++) {
        temp[i] = i + 1;
      }
      tempList = new ArrayList<>();
      int x;
      for (int i = 0; i < r; i++) {
        x = temp[i];
        tempList.add(x);
      }
      result.add(tempList);
      int m, maxVal;
      for (int i = 1; i < numberOfCombinations; i++) {
        m = r;
        maxVal = n;
        while (temp[m - 1] == maxVal) {
          m = m - 1;
          maxVal--;
        }
        temp[m - 1]++;
        for (int j = m; j < r; j++) {
          temp[j] = temp[j - 1] + 1;
        }
        tempList = new ArrayList<>();
        for (int k = 0; k < r; k++) {
          x = temp[k];
          tempList.add(x);
        }
        result.add(tempList);
      }

      return result;
    }

    public static double nCr(int n, int r) {
      int rfact = 1, nfact = 1, nrfact = 1, temp1 = n - r, temp2 = r;
      if (r > n - r) {
        temp1 = r;
        temp2 = n - r;
      }
      for (int i = 1; i <= n; i++) {
        if (i <= temp2) {
          rfact *= i;
          nrfact *= i;
        } else if (i <= temp1) {
          nrfact *= i;
        }
        nfact *= i;
      }
      return nfact / (double) (rfact * nrfact);
    }

    public static int[] generateNextCombination(int[] temp, int n, int r) {
      int m = r;
      int maxVal = n;
      while (temp[m - 1] == maxVal) {
        m = m - 1;
        maxVal--;
      }
      temp[m - 1]++;
      for (int j = m; j < r; j++) {
        temp[j] = temp[j - 1] + 1;
      }
      return temp;
    }
  }

  /**
   * @param start
   * @param step
   * @return
   */
  public static Iterable<Integer> count(int start, int step) {
    return () -> new Iterator<>() {
      int count = start - step;

      @Override
      public boolean hasNext() {
        return true;
      }

      @Override
      public Integer next() {
        count += step;
        return count;
      }
    };
  }

  public static Iterable<Integer> count(int start) {
    return count(start, 1);
  }

  public static <T> Iterable<T> cycle(List<T> list) {
    return () -> new Iterator<>() {
      int count = -1;

      @Override
      public boolean hasNext() {
        count++;
        return true;
      }

      @Override
      public T next() {
        count = count % list.size();
        return list.get(count);
      }
    };
  }

  /**
   * @param t
   * @param n
   * @param <T>
   * @return
   */
  public static <T> Iterable<T> repeat(T t, int n) {
    return () -> new Iterator<>() {
      int count = -1;

      @Override
      public boolean hasNext() {
        count++;
        return count < n;
      }

      @Override
      public T next() {
        return t;
      }
    };
  }

  /**
   * @param t
   * @param <T>
   * @return
   */
  public static <T> Iterable<T> repeat(T t) {
    return () -> new Iterator<>() {
      @Override
      public boolean hasNext() {
        return true;
      }

      @Override
      public T next() {
        return t;
      }
    };
  }

  /**
   * @param iterables
   * @param <T>
   * @return
   */
  @SafeVarargs
  public static <T> Iterable<T> chain(List<T>... iterables) {
    int size = 0;
    for (List<T> list :
        iterables) {
      size += list.size();
    }
    int finalSize = size;
    return () -> new Iterator<>() {
      int counter = -1;
      int iterableNo = 0;
      int sizeTillNow = 0;

      @Override
      public boolean hasNext() {
        counter++;
        return counter < finalSize;
      }

      @Override
      public T next() {
        int presentIterableIndex = counter - sizeTillNow;
        while (presentIterableIndex >= iterables[iterableNo].size()) {
          sizeTillNow += iterables[iterableNo].size();
          presentIterableIndex = counter - sizeTillNow;
          iterableNo++;
        }
        return iterables[iterableNo].get(presentIterableIndex);
      }
    };
  }

  /**
   * @param data
   * @param selectors
   * @param <T>
   * @return
   */
  public static <T> Iterable<T> compress(List<T> data, List<Boolean> selectors) {
    return () -> new Iterator<>() {
      int index = -1;

      @Override
      public boolean hasNext() {
        index++;
          while (index < data.size() && !selectors.get(index)) {
              index++;
          }
        return index < data.size();
      }

      @Override
      public T next() {
        return data.get(index);
      }
    };
  }

  /**
   * @param pred
   * @param seq
   * @param <T>
   * @return
   */
  public static <T> Iterable<T> dropWhile(Predicate<T> pred, List<T> seq) {
    return () -> new Iterator<>() {
      boolean next = false;
      int index = -1;

      @Override
      public boolean hasNext() {
        index++;
        while (!next && index < seq.size() && pred.pred(seq.get(index))) {
          index++;
        }
        next = true;
        return index < seq.size();
      }

      @Override
      public T next() {
        return seq.get(index);
      }
    };
  }

  /**
   * @param predicate
   * @param list
   * @param <T>
   * @return
   */
  public static <T> Iterable<T> ifilter(Predicate<T> predicate, List<T> list) {
    return () -> new Iterator<>() {
      int index = -1;

      @Override
      public boolean hasNext() {
        index++;
          while (index < list.size() && !predicate.pred(list.get(index))) {
              index++;
          }
        return index < list.size();
      }

      @Override
      public T next() {
        return list.get(index);
      }
    };
  }


  public static <T> Iterable<T> ifilterfalse(Predicate<T> predicate, List<T> list) {
    return () -> new Iterator<>() {
      int index = -1;

      @Override
      public boolean hasNext() {
        index++;
          while (index < list.size() && predicate.pred(list.get(index))) {
              index++;
          }
        return index < list.size();
      }

      @Override
      public T next() {
        return list.get(index);
      }
    };
  }

  public static <T> Iterable<T> islice(List<T> seq, int start, int stop, int step) {
    return () -> new Iterator<>() {
      int index = start - step;

      @Override
      public boolean hasNext() {
        index += step;
        return index < seq.size() && index < stop && index >= start;
      }

      @Override
      public T next() {
        return seq.get(index);
      }
    };
  }

  public static <T> Iterable<T> islice(List<T> seq, int stop, int step) {
    return islice(seq, 0, stop, step);
  }

  public static <T> Iterable<T> islice(List<T> seq, int stop) {
    return islice(seq, 0, stop, 1);
  }


  @SafeVarargs
  public static <T, U> Iterable<U> imap(Function<T, U> function, List<T>... lists) {
    return () -> new Iterator<>() {
      int index = -1;

      @Override
      public boolean hasNext() {
        index++;
        return index < lists[0].size();
      }

      @Override
      public U next() {
        List<T> list = new ArrayList<>();
        for (List<T> ts : lists) {
          list.add(ts.get(index));
        }
        return function.apply(list);
      }
    };
  }

  public static <T> Iterable<T> takeWhile(Predicate<T> pred, List<T> seq) {
    return () -> new Iterator<>() {
      boolean next = false;
      int index = -1;

      @Override
      public boolean hasNext() {
        index++;
        while (!next && index < seq.size() && !pred.pred(seq.get(index))) {
          index++;
        }
        next = true;
        return index < seq.size();
      }

      @Override
      public T next() {
        return seq.get(index);
      }
    };
  }

  @SafeVarargs
  public static <T> Iterable<List<T>> izip(List<T>... lists) {
    int smallest = lists[0].size();
    for (List<T> list :
        lists) {
        if (list.size() < smallest) {
            smallest = list.size();
        }
    }
    int finalSmallest = smallest;
    return () -> new Iterator<>() {
      int index = -1;

      @Override
      public boolean hasNext() {
        index++;
        return index < finalSmallest;
      }

      @Override
      public List<T> next() {
        List<T> temp = new ArrayList<>();
        for (List<T> list :
            lists) {
          temp.add(list.get(index));
        }
        return temp;
      }
    };
  }


  @SafeVarargs
  public static <T> Iterable<List<T>> izipLongest(T fillValue, List<T>... lists) {
    int largest = lists[0].size();
    for (List<T> list :
        lists) {
        if (list.size() > largest) {
            largest = list.size();
        }
    }
    int finalLargest = largest;
    return () -> new Iterator<>() {
      int index = -1;

      @Override
      public boolean hasNext() {
        index++;
        return index < finalLargest;
      }

      @Override
      public List<T> next() {
        List<T> temp = new ArrayList<>();
        for (List<T> list :
            lists) {
            if (index < list.size()) {
                temp.add(list.get(index));
            } else {
                temp.add(fillValue);
            }
        }
        return temp;
      }
    };
  }

  @SafeVarargs
  public static <T> Iterable<List<T>> product(List<T>... lists) {
    int total = 1;
    int[] max = new int[lists.length];
    for (int i = 0; i < lists.length; i++) {
      max[i] = lists[i].size();
    }
    int[] initProduct = new int[lists.length];
    Arrays.fill(initProduct, 1);
    for (List<T> list :
        lists) {
      total *= list.size();
    }
    int finalTotal = total;
    return () -> new Iterator<>() {
      int index = -1;
      int[] presentProduct;

      @Override
      public boolean hasNext() {
        index++;
        return index < finalTotal;
      }

      @Override
      public List<T> next() {
          if (index == 0) {
              presentProduct = initProduct;
          } else {
              PermutationGenerator.generateNextProduct(presentProduct, max);
          }
        List<T> result = new ArrayList<>();
        for (int i = 0; i < presentProduct.length; i++) {
          result.add(lists[i].get(presentProduct[i] - 1));
        }
        return result;
      }
    };
  }

  public static <T> Iterable<List<T>> combinations(List<T> list, int r) {
    return () -> new Iterator<>() {
      int index = -1;
      final int total = (int) CombinationGenerator.nCr(list.size(), r);
      final int[] currCombination = new int[r];

      @Override
      public boolean hasNext() {
        index++;
        return index < total;
      }

      @Override
      public List<T> next() {
        if (index == 0) {
          for (int i = 0; i < currCombination.length; i++) {
            currCombination[i] = i + 1;
          }
        } else {
          CombinationGenerator.generateNextCombination(currCombination, list.size(), r);
        }
        List<T> result = new ArrayList<>();
        for (int aCurrCombination : currCombination) {
          result.add(list.get(aCurrCombination - 1));
        }
        return result;
      }
    };
  }

  public static <T> Iterable<List<T>> permutations(List<T> list, int r) {
    long rfact = (long) PermutationGenerator.factorial(r);
    long total = (long) CombinationGenerator.nCr(list.size(), r) * rfact;
    return () -> new Iterator<>() {
      int index = -1;
      int permNo = 0;
      final int[] currPermutation = new int[r];
      final int[] currCombination = new int[r];

      @Override
      public boolean hasNext() {
        index++;
        return index < total;
      }

      @Override
      public List<T> next() {
        if (index == 0) {
          permNo = 0;
          for (int i = 0; i < currCombination.length; i++) {
            currCombination[i] = i + 1;
            currPermutation[i] = i + 1;
          }

        } else if (((permNo + 1) % rfact) == 0) {
          permNo++;
          CombinationGenerator.generateNextCombination(currCombination, list.size(), r);
          for (int i = 0; i < currCombination.length; i++) {
            currPermutation[i] = i + 1;
          }
        } else {
          permNo++;
          PermutationGenerator.generateNextPermutation(currPermutation, r);
        }
        List<T> result = new ArrayList<>();
        for (int i = 0; i < r; i++) {
          result.add(list.get(currCombination[currPermutation[i] - 1] - 1));
        }
        return result;
      }
    };
  }
}