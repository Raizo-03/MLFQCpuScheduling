package cpuscheduler;

public class Process {
    private String name;
    private String type;
    private int arrivalTime;
    private int burstTime;
    private int remainingTime;
    private String status;
    private int priority;
    private int waitingTime;
    private int turnaroundTime;
    private int completionTime;

    public Process(String name, String type, int arrivalTime, int burstTime) {
        this.name = name;
        this.type = type;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.status = "waiting";
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.completionTime = 0;
        setInitialPriority();
    }

    private void setInitialPriority() {
        switch (type) {
            case "system":
                priority = 1;
                break;
            case "interactive":
                priority = 2;
                break;
            case "batch":
                priority = 3;
                break;
            default:
                priority = 3;
                break;
        }
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void decrementRemainingTime() {
        remainingTime--;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void demote() {
        if (priority < 3) {
            priority++;
        }
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    @Override
    public String toString() {
        return name + " (" + type + ") Arrival: " + arrivalTime + " Burst: " + burstTime + " Remaining: " + remainingTime + " Status: " + status;
    }
}
