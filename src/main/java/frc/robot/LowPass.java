package frc.robot;

public class LowPass {
    private double tau;
    private double lpOld=0;
    private double out=0;

    public LowPass(double tau){
        this.tau=tau;
    }
    public void setOld(double old){
        lpOld = old;
    }
    public double calc(double in){
        out = this.lpOld + this.tau * (in - this.lpOld);
        this.lpOld=out;
        return((int)this.lpOld);
    }
}
