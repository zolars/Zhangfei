package circuitSolver;

import java.text.DecimalFormat;

public class Matrix {

	private double[][] matrix;
	private int rows;
	private int columns;
	private DecimalFormat form = new DecimalFormat("#.###");

	public Matrix(int rows, int columns) {
		this.matrix = new double[rows][columns];
		this.rows = matrix.length;
		this.columns = matrix[0].length;
	}

	public Matrix add(Matrix other) {
		Matrix sum = new Matrix(this.rows, this.columns);

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				sum.setValue(this.getValue(i, j) + other.getValue(i, j), i, j);
			}
		}
		return sum;
	}

	public Matrix subtract(Matrix other) {
		Matrix sum = new Matrix(this.rows, this.columns);

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				sum.setValue(this.getValue(i, j) - other.getValue(i, j), i, j);
			}
		}
		return sum;
	}

	public Matrix columnAugment(Matrix other) {
		Matrix newMatrix = new Matrix(this.rows, this.columns + other.columns);

		for (int i = 0; i < this.countRows(); i++) {
			for (int j = 0; j < this.countColumns(); j++) {
				newMatrix.setValue(this.getValue(i, j), i, j);
			}
		}

		for (int i = 0; i < other.countRows(); i++) {
			for (int j = 0; j < other.countColumns(); j++) {
				newMatrix.setValue(other.getValue(i, j), i, j + this.columns);
			}
		}
		return newMatrix;
	}

	public Matrix multiply(Matrix other) {
		Matrix product = new Matrix(this.rows, other.columns);

		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < other.countColumns(); j++) {
				double value = 0;
				for (int k = 0; k < this.rows; k++) {
					value += this.getValue(i, k) * other.getValue(k, j);
				}
				product.setValue(value, i, j);
			}
		}

		return product;
	}

	public Matrix rowSwap(int r1, int r2) {
		Matrix swapped = this;

		double[] row1 = new double[this.columns];
		double[] row2 = new double[this.columns];

		for (int i = 0; i < this.columns; i++) {
			row1[i] = this.getValue(r1, i);
			row2[i] = this.getValue(r2, i);
		}

		for (int i = 0; i < this.columns; i++) {
			swapped.setValue(row2[i], r1, i);
			swapped.setValue(row1[i], r2, i);
		}
		return swapped;
	}

	public Matrix columnSwap(int c1, int c2) {
		Matrix swapped = this;

		double[] col1 = new double[this.rows];
		double[] col2 = new double[this.rows];

		for (int i = 0; i < this.rows; i++) {
			col1[i] = this.getValue(i, c1);
			col2[i] = this.getValue(i, c2);
		}

		for (int i = 0; i < this.rows; i++) {
			swapped.setValue(col2[i], i, c1);
			swapped.setValue(col1[i], i, c2);
		}
		return swapped;
	}

	public Matrix removeRow(int row) {
		Matrix oldMatrix = this.rowSwap(row, 0);
		Matrix newMatrix = new Matrix(this.countRows() - 1, this.countColumns());
		for (int i = 1; i < oldMatrix.countRows(); i++) {
			for (int j = 0; j < oldMatrix.countColumns(); j++) {
				newMatrix.setValue(oldMatrix.getValue(i, j), i - 1, j);
			}
		}
		if (row != 0) {
			newMatrix = newMatrix.rowSwap(row - 1, 0);
		}
		return newMatrix;
	}

	public Matrix removeColumn(int column) {
		Matrix oldMatrix = this.columnSwap(column, 0);
		Matrix newMatrix = new Matrix(this.countRows(), this.countColumns() - 1);
		for (int i = 0; i < oldMatrix.countRows(); i++) {
			for (int j = 1; j < oldMatrix.countColumns(); j++) {
				newMatrix.setValue(oldMatrix.getValue(i, j), i, j - 1);
			}
		}
		if (column != 0) {
			newMatrix = newMatrix.columnSwap(0, column - 1);
		}
		return newMatrix;
	}

	public void setMatrix(double[][] matrix) {
		this.matrix = matrix;
		this.rows = matrix.length;
		this.columns = matrix[0].length;
	}

	public void setValue(double value, int row, int column) {
		matrix[row][column] = value;
	}

	public double getValue(int row, int column) {
		return matrix[row][column];
	}

	public static Matrix Identity(int side) {
		Matrix identity = new Matrix(side, side);
		for (int i = 0; i < side; i++) {
			identity.setValue(1, i, i);
		}
		return identity;
	}

	public static Matrix nullMatrix(int rows, int columns) {
		return new Matrix(rows, columns);
	}

	public int countRows() {
		return rows;
	}

	public int countColumns() {
		return columns;
	}

	public String toString() {
		String output = "";
		for (int i = 0; i < this.matrix.length; i++) {
			for (int j = 0; j < this.matrix[0].length; j++) {
				output += form.format(this.getValue(i, j)) + "\t";
			}
			output += "\n";
		}
		return output;
	}
}