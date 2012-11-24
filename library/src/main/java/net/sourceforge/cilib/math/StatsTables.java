/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math;

public class StatsTables {

    private static int PRECISION = 3;

    public static double chisqrDistribution(int dof, double alpha) {
        double x;

        if ((alpha > 1) || (alpha <= 0)) {
            throw new IllegalArgumentException("Invalid p: " + alpha + "\n");
        } else if (alpha == 1) {
            x = 0;
        } else if (dof == 1) {
            x = Math.pow(uDistribution(alpha / 2), 2);
        } else if (dof == 2) {
            x = -2 * Math.log(alpha);
        } else {
            double u = uDistribution(alpha);
            double u2 = u * u;

            x = Math.max(0, dof + Math.sqrt(2 * dof) * u
                    + 2 / 3 * (u2 - 1)
                    + u * (u2 - 7) / 9 / Math.sqrt(2 * dof)
                    - 2 / 405 / dof * (u2 * (3 * u2 + 7) - 16));

            if (dof <= 100) {
                double x0;
                double p1;
                double z;
                do {
                    x0 = x;
                    if (x < 0) {
                        p1 = 1;
                    } else if (dof > 100) {
                        p1 = uProbability((Math.pow((x / dof), (1 / 3)) - (1 - 2 / 9 / dof))
                                / Math.sqrt(2 / 9 / dof));
                    } else if (x > 400) {
                        p1 = 0;
                    } else {
                        double i0;
                        double a;
                        if ((dof % 2) != 0) {
                            p1 = 2 * uProbability(Math.sqrt(x));
                            a = Math.sqrt(2 / Math.PI) * Math.exp(-x / 2) / Math.sqrt(x);
                            i0 = 1;
                        } else {
                            p1 = a = Math.exp(-x / 2);
                            i0 = 2;
                        }

                        for (double i = i0; i <= dof - 2; i += 2) {
                            a *= x / i;
                            p1 += a;
                        }
                    }
                    z = Math.exp(((dof - 1) * Math.log(x / dof) - Math.log(4 * Math.PI * x)
                            + dof - x - 1 / dof / 6) / 2);
                    x += (p1 - alpha) / z;
                    x = round_to_precision(x, PRECISION);
                } while ((dof < 31) && (Math.abs(x0 - x) > 1e-4));
            }
        }
        return x;
    }

    public static double chisqrProbability(int dof, double cv) {
        double p;

        if (cv <= 0) {
            p = 1;
        } else if (dof > 100) {
            p = uProbability((Math.pow((cv / dof), 1 / 3) - (1 - 2 / 9 / dof)) / Math.sqrt(2 / 9 / dof));
        } else if (cv > 400) {
            p = 0;
        } else {
            double a;
            double i;
            double i1;
            if ((dof % 2) != 0) {
                p = 2 * uProbability(Math.sqrt(cv));
                a = Math.sqrt(2 / Math.PI) * Math.exp(-cv / 2) / Math.sqrt(cv);
                i1 = 1;
            } else {
                p = a = Math.exp(-cv / 2);
                i1 = 2;
            }

            for (i = i1; i <= (dof - 2); i += 2) {
                a *= cv / i;
                p += a;
            }
        }

        return p;
    }

    public static double uDistribution(double alpha) {
        double y = -Math.log(4 * alpha * (1 - alpha));
        double x = Math.sqrt(
                y * (1.570796288
                + y * (.03706987906
                + y * (-.8364353589E-3
                + y * (-.2250947176E-3
                + y * (.6841218299E-5
                + y * (0.5824238515E-5
                + y * (-.104527497E-5
                + y * (.8360937017E-7
                + y * (-.3231081277E-8
                + y * (.3657763036E-10
                + y * .6936233982E-12)))))))))));

        if (alpha > .5) {
            x = -x;
        }

        return x;
    }

    public static double uProbability(double cv) {
        double p = 0;
        double absx = Math.abs(cv);

        if (absx < 1.9) {
            p = Math.pow((1
                    + absx * (.049867347
                    + absx * (.0211410061
                    + absx * (.0032776263
                    + absx * (.0000380036
                    + absx * (.0000488906
                    + absx * .000005383)))))), -16) / 2;
        } else if (absx <= 100) {
            for (int i = 18; i >= 1; i--) {
                p = i / (absx + p);
            }
            p = Math.exp(-.5 * absx * absx)
                    / Math.sqrt(2 * Math.PI) / (absx + p);
        }

        if (cv < 0) {
            p = 1 - p;
        }
        return p;
    }

    public static double tDistribution(int dof, double alpha) {
        if (alpha >= 1 || alpha <= 0) {
            throw new IllegalArgumentException("Invalid p: " + alpha + "\n");
        }

        if (alpha == 0.5) {
            return 0;
        } else if (alpha < 0.5) {
            return -tDistribution(dof, 1 - alpha);
        }

        double u = uDistribution(alpha);
        double u2 = Math.pow(u, 2);

        double a = (u2 + 1) / 4;
        double b = ((5 * u2 + 16) * u2 + 3) / 96;
        double c = (((3 * u2 + 19) * u2 + 17) * u2 - 15) / 384;
        double d = ((((79 * u2 + 776) * u2 + 1482) * u2 - 1920) * u2 - 945)
                / 92160;
        double e = (((((27 * u2 + 339) * u2 + 930) * u2 - 1782) * u2 - 765) * u2
                + 17955) / 368640;

        double x = u * (1 + (a + (b + (c + (d + e / dof) / dof) / dof) / dof) / dof);

        if (dof <= Math.pow(Math.log10(alpha), 2) + 3) {
            double round;
            do {
                double p1 = tProbability(dof, x);
                double n1 = dof + 1;
                double delta = (p1 - alpha)
                        / Math.exp((n1 * Math.log(n1 / (dof + x * x))
                        + Math.log(dof / n1 / 2 / Math.PI) - 1
                        + (1 / n1 - 1 / dof) / 6) / 2);
                x += delta;
                round = round_to_precision(delta, Math.abs(integer(Math.log10(Math.abs(x)) - 4)));
            } while (round != 0);
        }
        
        return x;
    }

    public static double tProbability(int dof, double cv) {
        double a;
        double b;
        double w = Math.atan2(cv / Math.sqrt(dof), 1);
        double z = Math.pow(Math.cos(w), 2);
        double y = 1;

        for (int i = dof - 2; i >= 2; i -= 2) {
            y = 1 + (i - 1) / i * z * y;
        }

        if (dof % 2 == 0) {
            a = Math.sin(w) / 2;
            b = .5;
        } else {
            a = (dof == 1) ? 0 : Math.sin(w) * Math.cos(w) / Math.PI;
            b = .5 + w / Math.PI;
        }
        return Math.max(0, 1 - b - a * y);
    }

    public static double fDistribution(int n, int m, double p) {
        double x;

        if (p >= 1 || p <= 0) {
            throw new IllegalArgumentException("Invalid p: $p\n");
        }

        if (p == 1) {
            x = 0;
        } else if (m == 1) {
            x = 1 / Math.pow(tDistribution(n, 0.5 - p / 2), 2);
        } else if (n == 1) {
            x = Math.pow(tDistribution(m, p / 2), 2);
        } else if (m == 2) {
            double u = chisqrDistribution(m, 1 - p);
            int a = m - 2;
            x = 1 / (u / m * (1
                    + ((u - a) / 2
                    + (((4 * u - 11 * a) * u + a * (7 * m - 10)) / 24
                    + (((2 * u - 10 * a) * u + a * (17 * m - 26)) * u
                    - a * a * (9 * m - 6)) / 48 / n) / n) / n));
        } else if (n > m) {
            x = 1 / _subf2(m, n, 1 - p);
        } else {
            x = _subf2(n, m, p);
        }
        return x;
    }

    private static double _subf2(int n, int m, double p) {
        double u = chisqrDistribution(n, p);
        double n2 = n - 2;
        double x = u / n
                * (1
                + ((u - n2) / 2
                + (((4 * u - 11 * n2) * u + n2 * (7 * n - 10)) / 24
                + (((2 * u - 10 * n2) * u + n2 * (17 * n - 26)) * u
                - n2 * n2 * (9 * n - 6)) / 48 / m) / m) / m);
        double delta;
        do {
            double z = Math.exp(
                    ((n + m) * Math.log((n + m) / (n * x + m))
                    + (n - 2) * Math.log(x)
                    + Math.log(n * m / (n + m))
                    - Math.log(4 * Math.PI)
                    - (1 / n + 1 / m - 1 / (n + m)) / 6) / 2);
            delta = (fProbability(n, m, x) - p) / z;
            x += delta;
        } while (Math.abs(delta) > 3e-4);
        return x;
    }

    public static double fProbability(int n, int m, double x) {
        double p;

        if (x <= 0) {
            p = 1;
        } else if (m % 2 == 0) {
            double z = m / (m + n * x);
            double a = 1;
            for (int i = m - 2; i >= 2; i -= 2) {
                a = 1 + (n + i - 2) / i * z * a;
            }
            p = 1 - (Math.pow((1 - z), (n / 2)) * a);
        } else if (n % 2 == 0) {
            double z = n * x / (m + n * x);
            double a = 1;
            for (int i = n - 2; i >= 2; i -= 2) {
                a = 1 + (m + i - 2) / i * z * a;
            }
            p = Math.pow((1 - z), (m / 2)) * a;
        } else {
            double y = Math.atan2(Math.sqrt(n * x / m), 1);
            double z = Math.pow(Math.sin(y), 2);
            double a = (n == 1) ? 0 : 1;
            for (int i = n - 2; i >= 3; i -= 2) {
                a = 1 + (m + i - 2) / i * z * a;
            }
            double b = Math.PI;
            for (int i = 2; i <= m - 1; i += 2) {
                b *= (i - 1) / i;
            }
            double p1 = 2 / b * Math.sin(y) * Math.pow(Math.cos(y), m) * a;

            z = Math.pow(Math.cos(y), 2);
            a = (m == 1) ? 0 : 1;
            for (int i = m - 2; i >= 3; i -= 2) {
                a = 1 + (i - 1) / i * z * a;
            }
            p = Math.max(0, p1 + 1 - 2 * y / Math.PI
                    - 2 / Math.PI * Math.sin(y) * Math.cos(y) * a);
        }
        return p;
    }

    private static double round_to_precision(double x, double p) {
        return Math.round(x * Math.pow(10, p)) / Math.pow(10, p);
    }

    private static double integer(double i) {
        if (i > 0) {
            return Math.floor(i);
        } else {
            return Math.ceil(i);
        }
    }
}
