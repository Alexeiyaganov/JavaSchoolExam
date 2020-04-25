package com.tsystems.javaschool.tasks.pyramid;

import java.util.Collections;
import java.util.List;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {
        // TODO : Implement your solution here
        //проверка на размерность и корректное содержимое
        if (inputNumbers.contains(null) || (inputNumbers.size() > 1000)) throw new CannotBuildPyramidException();
        Collections.sort(inputNumbers);

        //подсчет высоты итоговой матрицы и возможности построить пирамиду
        int size = inputNumbers.size();
        int heigth = 0;
        while (size > 0) {
            size -= heigth + 1;
            if (size < 0) {
                throw new CannotBuildPyramidException();
            } else {
                heigth++;
            }

        }

        //заполнение матрицы значениями
        int width = heigth * 2 - 1;
        int[][] piramidMatr = new int[heigth][width];
        int link = 0;
        for (int i = 0; i < heigth; i++) {
            int l = heigth - i - 1;
            for (int j = 0; j <= i; j++) {
                piramidMatr[i][l] = inputNumbers.get(link);
                link++;
                l += 2;
            }
        }

        return piramidMatr;
    }


}
