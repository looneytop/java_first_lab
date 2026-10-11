public class Complex {
    private final double re;
    private final double im;

    public String get_string_format() {
        String str = "";
        if (this.im >= 0) {
            str = String.valueOf(this.re) + "+" + String.valueOf(Math.abs(this.im)) + "i";
        } else {
            str = String.valueOf(this.re) + String.valueOf(this.im) + "i";
        }
        return str;
    }

    Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public Complex add(Complex other) {
        double result_re = this.re + other.re;
        double result_im = this.im + other.im;

        return new Complex(result_re, result_im);
    }

    public Complex subtract(Complex other) {
        double result_re = this.re - other.re;
        double result_im = this.im - other.im;

        return new Complex(result_re, result_im);
    }

    public Complex multiply(Complex other) {
        double result_re = this.re * other.re - this.im * other.im;
        double result_im = this.re * other.im + this.im * other.re;

        return new Complex(result_re, result_im);
    }

    public Complex divide(Complex other) {
        double denominator = other.re * other.re + other.im * other.im;
        if (denominator == 0) {
            throw new ArithmeticException("Try to div zero denominator");
        }
        double result_re = (this.re * other.re + this.im * other.im) / denominator;
        double result_im = (this.im * other.re - this.re * other.im) / denominator;

        return new Complex(result_re, result_im);
    }

    public double absSquared() {
        return this.re * this.re + this.im * this.im;
    }
};