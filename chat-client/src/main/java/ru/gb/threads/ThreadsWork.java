package ru.gb.threads;

import java.util.Arrays;

public class ThreadsWork {
    private static final int arraySize = 10_000_000;
    private static int subArraysSize = arraySize / 2;
    private static float[] array = new float[arraySize];
    private static float[] subArray1 = new float[subArraysSize];
    private static float[] subArray2 = new float[subArraysSize];
    private static Thread thread1;
    private static Thread thread2;

    public static void main(String[] args) {
        Arrays.fill(array, 1);
        float[] a = Arrays.copyOf(changeArraysValues(array), array.length);
        Arrays.fill(array, 1);
        changeArraysValuesWithThreads(array);
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Arrays.equals(a, array));
    }


    public static float[] changeArraysValues(float[] array) {
        long a = System.currentTimeMillis();
        for (int i = 0; i < array.length; i++) {
            array[i] = (float) (array[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        System.out.println(System.currentTimeMillis() - a);
        return array;
    }

    public static void changeArraysValuesWithThreads(float[] array) {
        long a = System.currentTimeMillis();
        System.arraycopy(array, 0, subArray1, 0, subArraysSize);
        System.arraycopy(array, subArraysSize, subArray2, 0, subArraysSize);
        thread1 = new Thread(()->{
            for (int j = 0; j <subArraysSize; j++) {
                subArray1[j] = (float) (subArray1[j] * Math.sin(0.2f + j / 5) * Math.cos(0.2f + j / 5) * Math.cos(0.4f + j / 2));
            }
            System.arraycopy(subArray1, 0, array, 0, subArraysSize);
        });

        thread2 = new Thread(()->{
            int index = subArraysSize;
            for (int j = 0; j < subArraysSize; j++) {
                subArray2[j] = (float) (subArray2[j] * Math.sin(0.2f + index / 5) * Math.cos(0.2f + index / 5) * Math.cos(0.4f + index / 2));
                index++;
            }
            System.arraycopy(subArray2, 0, array, subArraysSize, subArraysSize);
        });
        thread1.start();
        thread2.start();
        System.out.println(System.currentTimeMillis() - a);
    }
}
