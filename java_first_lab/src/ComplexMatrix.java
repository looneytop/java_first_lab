public class ComplexMatrix {
    private final int rows;
    private final int cols;
    private final Complex[][] matrix;

    ComplexMatrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Rows or cols <= 0");
        }
        this.rows = rows;
        this.cols = cols;
        this.matrix = new Complex[rows][cols];

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                this.matrix[i][j] = new Complex(0, 0);
            }
        }
    }

    public void set(int row, int col, Complex value) {
        if (value == null) {
            throw new IllegalArgumentException("Invalid setting parameter");
        }
        matrix[row][col] = value;
    }

    public Complex get(int row, int col) {
        return this.matrix[row][col];
    }

    public int getRows() {
        return this.rows;
    }

    public int getCols() {
        return this.cols;
    }

    public void checkSquare() {
        if (this.rows != this.cols) {
            throw new IllegalStateException("This matrix is not square");
        }
    }

    public ComplexMatrix findMatrixTransposition() {
        ComplexMatrix result = new ComplexMatrix(this.cols, this.rows);

        for (int i = 0; i < result.rows; ++i) {
            for (int j = 0; j < result.cols; ++j) {
                result.set(i, j, this.get(j, i));
            }
        }

        return result;
    }

    public Complex findDeterminant() {
        checkSquare();
        int n = this.rows;

        Complex[][] a = new Complex[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                a[i][j] = matrix[i][j];
            }
        }

        Complex determinant = new Complex(1, 0);
        int sign = 1;

        for (int k = 0; k < n; ++k) {
            int pivot_row = k;
            for (int i = k + 1; i < n; ++i) {
                if (a[i][k].absSquared() > a[pivot_row][k].absSquared()) {
                    pivot_row = i;
                }
            }

            if (a[pivot_row][k].absSquared() == 0.0) {
                return new Complex(0, 0);
            }

            if (pivot_row != k) {
                Complex[] temp = a[k];
                a[k] = a[pivot_row];
                a[pivot_row] = temp;
                sign = -sign;
            }

            Complex pivot = a[k][k];
            determinant = determinant.multiply(pivot);

            for (int i = k + 1; i < n; i++) {
                Complex factor = a[i][k].divide(pivot);
                a[i][k] = new Complex(0, 0);

                for (int j = k + 1; j < n; j++) {
                    a[i][j] = a[i][j].subtract(factor.multiply(a[k][j]));
                }
            }
        }

        if (sign < 0) {
            determinant = determinant.multiply(new Complex(-1, 0));
        }
        return determinant;
    }

    public ComplexMatrix findInverseMatrix() {
        checkSquare();
        int n = rows;
        // Квадрат относительного порога 1e-12 для ведущего элемента.
        final double toleranceSquared = 1e-24;
        double[] rowScaleSquared = new double[n];
        Complex[][] a = new Complex[n][2 * n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = matrix[i][j];
                rowScaleSquared[i] = Math.max(rowScaleSquared[i], a[i][j].absSquared());
                a[i][n + j] = new Complex(i == j ? 1 : 0, 0);
            }
        }

        for (int k = 0; k < n; k++) {
            int pivotRow = k;
            double largestRatio = 0.0;
            for (int i = k; i < n; i++) {
                double ratio = rowScaleSquared[i] == 0.0
                        ? 0.0 : a[i][k].absSquared() / rowScaleSquared[i];
                if (ratio > largestRatio) {
                    largestRatio = ratio;
                    pivotRow = i;
                }
            }

            if (largestRatio <= toleranceSquared) {
                throw new ArithmeticException("Матрица вырождена или слишком близка к вырожденной для вычислений");
            }

            if (pivotRow != k) {
                Complex[] temp = a[k];
                a[k] = a[pivotRow];
                a[pivotRow] = temp;
                double tempScale = rowScaleSquared[k];
                rowScaleSquared[k] = rowScaleSquared[pivotRow];
                rowScaleSquared[pivotRow] = tempScale;
            }

            Complex pivot = a[k][k];
            for (int j = 0; j < 2 * n; j++) {
                a[k][j] = a[k][j].divide(pivot);
            }

            for (int i = 0; i < n; i++) {
                if (i == k) {
                    continue;
                }

                Complex factor = a[i][k];
                for (int j = 0; j < 2 * n; j++) {
                    a[i][j] = a[i][j].subtract(factor.multiply(a[k][j]));
                }
            }
        }

        ComplexMatrix result = new ComplexMatrix(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result.set(i, j, a[i][n + j]);
            }
        }
        return result;
    }

    public ComplexMatrix add(ComplexMatrix other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            throw new IllegalArgumentException("Different matrix's parametrs (rows and cols)");
        }
        ComplexMatrix result = new ComplexMatrix(this.rows, this.cols);

        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.cols; ++j) {
                result.set(i, j, this.get(i, j).add(other.get(i, j)));
            }
        }

        return result;
    }

    public ComplexMatrix subtract(ComplexMatrix other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            throw new IllegalArgumentException("Different matrix's parametrs (rows and cols)");
        }
        ComplexMatrix result = new ComplexMatrix(this.rows, this.cols);

        for (int i = 0; i < result.rows; ++i) {
            for (int j = 0; j < result.cols; ++j) {
                result.set(i, j, this.get(i, j).subtract(other.get(i, j)));
            }
        }

        return result;
    }

    public ComplexMatrix multiply(ComplexMatrix other) {
        if (this.cols != other.rows) {
            throw new IllegalArgumentException("Incompatible matrix dimensions");
        }

        ComplexMatrix result = new ComplexMatrix(this.rows, other.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                Complex sum = new Complex(0, 0);
                for (int k = 0; k < this.cols; k++) {
                    sum = sum.add(this.get(i, k).multiply(other.get(k, j)));
                }
                result.set(i, j, sum);
            }
        }
        return result;
    }

    public ComplexMatrix divide(ComplexMatrix other) {
        ComplexMatrix inversive_matrix = other.findInverseMatrix();

        return this.multiply(inversive_matrix);
    }

    public void printMatrix() {
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                System.out.print(matrix[i][j].get_string_format() + " ");
            }
            System.out.print('\n');
        }
    }
};