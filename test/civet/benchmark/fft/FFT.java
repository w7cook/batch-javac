package benchmark.fft;

import org.civet.Civet;


/**************************************************************************
 *                                                                         *
 *             Java Grande Forum Benchmark Suite - Version 2.0             *
 *                                                                         *
 *                            produced by                                  *
 *                                                                         *
 *                  Java Grande Benchmarking Project                       *
 *                                                                         *
 *                                at                                       *
 *                                                                         *
 *                Edinburgh Parallel Computing Centre                      *
 *                                                                         *
 *                email: epcc-javagrande@epcc.ed.ac.uk                     *
 *                                                                         *
 *      adapted from SciMark 2.0, author Roldan Pozo (pozo@cam.nist.gov)   *
 *             see below for previous history of this code                 *
 *                                                                         *
 [Modified by Ulrik for use with JSpec]
 *                                                                         *
 *      This version copyright (c) The University of Edinburgh, 1999.      *
 *                         All rights reserved.                            *
 *                                                                         *
 **************************************************************************/

//package fft;

/**
 * Computes FFT's of complex, double precision data where n is an integer power
 * of 2. This appears to be slower than the Radix2 method, but the code is
 * smaller and simpler, and it requires no extra storage.
 * <P>
 * 
 * @author Bruce R. Miller bruce.miller@nist.gov,
 * @author Derived from GSL (Gnu Scientific Library),
 * @author GSL's FFT Code by Brian Gough bjg@vvv.lanl.gov
 */

/*
 * See {@link ComplexDoubleFFT ComplexDoubleFFT} for details of data layout.
 */

public class FFT {
	/** Compute Fast Fourier Transform of (complex) data, in place. */

	public static void transform(double[] data, int datasize) {
		transform_internal(data, Civet.CT(-1), datasize);
	}

	/* ______________________________________________________________________ */

	protected static int log2(int n) {
		int log = 0;
		for (int k = 1; k < n; k = k * 2, log++)
			;
		if (n != (1 << log))
			throw new Error("FFT: Data length is not a power of 2!: " + n);
		return log;
	}

	public static double getPI() {
		return Math.PI;
	}

	public static double sin(double a) {
		return Math.sin(a);
	}

	protected static void transform_internal(double data[], int direction,
			int datasize) {
		int n = datasize / 2;
		if (n == 1)
			return; // Identity operation!
		int logn = log2(n);

		/* bit reverse the input data for decimation in time algorithm */
		bitreverse(data, datasize);

		/* apply fft recursion */
		for (
		int bit = Civet.CT(0, Civet.IsCT(n)), dual = Civet.CT(1, Civet.IsCT(n)); bit < logn; bit++, dual = dual * 2) {
			double w_real = Civet.CT(1.0);
			double w_imag = Civet.CT(0.0);

			double theta = Civet.CT(2.0 * direction * getPI() / (2.0 * (double) dual));
			double s = sin(theta);
			double t = sin(theta / 2.0);
			double s2 = 2.0 * t * t;

			/* a = 0 */
			for (
			int b = Civet.CT(0, Civet.IsCT(n)); b < n; b = b + 2 * dual) {
				int i = 2 * b;
				int j = 2 * (b + dual);

				double wd_real = data[j];
				double wd_imag = data[j + 1];

				data[j] = data[i] - wd_real;
				data[j + 1] = data[i + 1] - wd_imag;
				data[i] = data[i] + wd_real;
				data[i + 1] = data[i + 1] + wd_imag;
			}

			/* a = 1 .. (dual-1) */
			for (
			int a = Civet.CT(1, Civet.IsCT(dual)); a < dual; a++) {
				/* trignometric recurrence for w-> exp(i theta) w */
				{
					double tmp_real = w_real - s * w_imag - s2 * w_real;
					double tmp_imag = w_imag + s * w_real - s2 * w_imag;
					w_real = tmp_real;
					w_imag = tmp_imag;
				}
				for (
				int b = Civet.CT(0, Civet.IsCT(n)); b < n; b = b + 2 * dual) {
					int i = 2 * (b + a);
					int j = 2 * (b + a + dual);

					double z1_real = data[j];
					double z1_imag = data[j + 1];

					double wd_real = w_real * z1_real - w_imag * z1_imag;
					double wd_imag = w_real * z1_imag + w_imag * z1_real;

					data[j] = data[i] - wd_real;
					data[j + 1] = data[i + 1] - wd_imag;
					data[i] = data[i] + wd_real;
					data[i + 1] = data[i + 1] + wd_imag;
				}
			}
		}
	}

	protected static void bitreverse(double data[], int datasize) {
		/* This is the Goldrader bit-reversal algorithm */
		int n = datasize / 2;
		for (
		int i = Civet.CT(0, Civet.IsCT(n)), j = Civet.CT(0, Civet.IsCT(n)); i < n - 1; i++) {
			int ii = 2 * i;
			int jj = 2 * j;
			int k = n / 2;
			if (i < j) {
				double tmp_real = data[ii];
				double tmp_imag = data[ii + 1];
				data[ii] = data[jj];
				data[ii + 1] = data[jj + 1];
				data[jj] = tmp_real;
				data[jj + 1] = tmp_imag;
			}

			while (k <= j) {
				j = j - k;
				k = k / 2;
			}
			j = j + k;
		}
	}
}
