/*
 * Copyright (c) 2009-2010, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Efficient Java Matrix Library (EJML).
 *
 * EJML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * EJML is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with EJML.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.ejml.alg.dense.decomposition;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.ops.MatrixFeatures;
import org.ejml.ops.RandomMatrices;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertTrue;


/**
 * @author Peter Abeles
 */
public class TestTriangularSolver {

    Random rand = new Random(0xff);

    @Test
    public void solveL_vector() {
        DenseMatrix64F L = createRandomLowerTriangular();

        DenseMatrix64F L_inv = L.copy();
        MatrixInvertSpecialized.invert3x3(L_inv.data);

        DenseMatrix64F B = RandomMatrices.createRandom(3,1,rand);
        DenseMatrix64F expected = RandomMatrices.createRandom(3,1,rand);
        DenseMatrix64F found = B.copy();

        TriangularSolver.solveL(L.data,found.data,3);
        CommonOps.mult(L_inv,B,expected);


        assertTrue(MatrixFeatures.isIdentical(expected,found,1e-8));
    }

    private DenseMatrix64F createRandomLowerTriangular() {
        DenseMatrix64F L = RandomMatrices.createRandom(3,3,rand);
        for( int i = 0; i < L.numRows; i++ ) {
            for( int j = i+1; j < L.numCols; j++ ) {
                L.set(i,j,0);
            }
        }
        return L;
    }

    @Test
    public void solveL_matrix() {
        DenseMatrix64F L = createRandomLowerTriangular();

        DenseMatrix64F L_inv = L.copy();
        MatrixInvertSpecialized.invert3x3(L_inv.data);

        DenseMatrix64F B = RandomMatrices.createRandom(3,4,rand);
        DenseMatrix64F expected = RandomMatrices.createRandom(3,4,rand);
        DenseMatrix64F found = B.copy();

        TriangularSolver.solveL(L.data,found.data,3,4);
        CommonOps.mult(L_inv,B,expected);

        assertTrue(MatrixFeatures.isIdentical(expected,found,1e-8));
    }

    @Test
    public void solveTranL() {
        DenseMatrix64F L = createRandomLowerTriangular();

        DenseMatrix64F B = RandomMatrices.createRandom(3,1,rand);
        DenseMatrix64F expected = RandomMatrices.createRandom(3,1,rand);
        DenseMatrix64F found = B.copy();

        TriangularSolver.solveTranL(L.data,found.data,3);

        CommonOps.transpose(L);
        DenseMatrix64F L_inv = L.copy();
        MatrixInvertSpecialized.invert3x3(L_inv.data);
        CommonOps.mult(L_inv,B,expected);

        assertTrue(MatrixFeatures.isIdentical(expected,found,1e-8));
    }

    @Test
    public void solveU() {
        DenseMatrix64F U = RandomMatrices.createRandom(3,3,rand);
        for( int i = 0; i < U.numRows; i++ ) {
            for( int j = 0; j < i; j++ ) {
                U.set(i,j,0);
            }
        }

        DenseMatrix64F U_inv = U.copy();
        MatrixInvertSpecialized.invert3x3(U_inv.data);

        DenseMatrix64F B = RandomMatrices.createRandom(3,1,rand);
        DenseMatrix64F expected = RandomMatrices.createRandom(3,1,rand);
        DenseMatrix64F found = B.copy();

        TriangularSolver.solveU(U.data,found.data,3);
        CommonOps.mult(U_inv,B,expected);

        assertTrue(MatrixFeatures.isIdentical(expected,found,1e-8));
    }
}
