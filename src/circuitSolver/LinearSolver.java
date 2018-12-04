package circuitSolver;

import java.util.*;

public class LinearSolver {
	Map<Integer, Integer> swaps;

	public LinearSolver() {
		swaps = new HashMap<Integer, Integer>();
	}

	public Matrix gaussianElimination(Matrix A, Matrix b) {
		Matrix Ab = upperTriangular(A, b);
		// System.out.println(Ab);
		return backSubstitute(Ab);
	}

	public Matrix upperTriangular(Matrix A, Matrix b) {
		int rows = A.countRows();
		Matrix Ab = A.columnAugment(b);
		for (int i = 0; i < rows - 1; i++) {
			for (int k = i + 1; k < rows; k++) {
				double pivot = Ab.getValue(i, i);
				double belowPivot = Ab.getValue(k, i);
				// need to swap
				if (pivot == 0) {
					// look for something to swap with
					for (int n = k; n < rows; n++) {
						if (belowPivot != 0) {
							Ab = Ab.rowSwap(i, k);
							pivot = Ab.getValue(i, i);
							swaps.put(i, k);
							break;
						}
					}
				}
				// found a row to eliminate with
				if (belowPivot != 0) {
					double factor = -Ab.getValue(k, i) / pivot;
					Matrix eliminator = Matrix.Identity(rows);
					eliminator.setValue(factor, k, i);
					Ab = eliminator.multiply(Ab);
				}
			}
		}
		return Ab;
	}

	public Matrix backSubstitute(Matrix Ab) {
		int components = Ab.countRows();
		double[][] solution = new double[components][1];

		for (int i = components - 1; i >= 0; i--) {
			double rightSide = Ab.getValue(i, Ab.countColumns() - 1);
			for (int j = 0; j < Ab.countColumns() - 1; j++) {
				if (j != i) {
					rightSide -= solution[j][0] * Ab.getValue(i, j);
				}
			}

			if (Ab.getValue(i, i) != 0) {
				solution[i][0] = rightSide / Ab.getValue(i, i);
			} else {
				// no solution or infinitely many
				return null;
			}
		}
		Matrix response = new Matrix(components, 1);
		response.setMatrix(solution);

		// undo row swaps from upperTriangular
		for (int row : swaps.keySet()) {
			response.rowSwap(row, swaps.get(row));
		}

		swaps.clear();
		return response;
	}
}